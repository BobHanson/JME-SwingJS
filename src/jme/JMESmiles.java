package jme;

public class JMESmiles extends JMECore {

	// BH 2023.01.24 this class pulled out from JMEmol
	// BH 2023.01.25 adds JMEcore.
	/**
	 * temporary array that holds an atom selection
	 */
	private int a[]; 
	
	/**
	 * temporary array that holds bond types
	 */
	private int btype[]; 

	private boolean doMark;
	private boolean isQuery;
	private boolean autoez;

	
	

public JMESmiles(JMEmol mol, int part, boolean isQuery) {
		super(mol, part);
		this.isQuery = isQuery;
	}

	// ----------------------------------------------------------------------------
	// ----------------------------------------------------------------------------
	/**
	 * Original PE SMILES implementation with side effects: the order of my atoms
	 * will be changed unless the nocanonize option is set Must be a single fragment
	 * !!!!
	 * 
	 * @return
	 */
	String createSmilesWithSideEffect(Parameters mpars) {
		if (natoms == 0)
			return "";

		parameters = mpars;
		doMark = mpars.mark;
		autoez = mpars.smilesParams.autoez;

		int[] con1 = new int[natoms + 10]; // well a little bit too much memory
		int[] con2 = new int[natoms + 10]; // but the code is much cleaner than Vector
		// v niektorych exotoch je naozaj viac con ako atomov
		int[] branch = new int[natoms + 1];
		int[] candidate = new int[MAX_BONDS_ON_ATOM + 1];
		int[] parent = new int[natoms + 1];
		boolean[] isAromatic = new boolean[natoms + 1];

		boolean[] isRingBond = new boolean[nbonds + 1];
		int[] bondMinimumRingSize = new int[nbonds + 1];
		// boolean leftBranch[] = JMEUtil.createBArray(natoms+1); //BB: not used
		int nconnections = 0;

		// checkMultipart(true); //remove the small parts!!!
		// checkMultipart(false); //BB change: do not remove the small parts!!!
		// varaible a[] is now set

		// asi to treba takto komplikovane
		// btype RING_NONAROMATIC sa nepouziva ! (len aromatic)
		if (mpars.smilesParams.canonize && !haveQueryOrCoordBonds()) {
			Parameters.HydrogenParams pars = setHydrogenParams(mpars);
			deleteHydrogens(pars);
			cleanPolarBonds(mpars.smilesParams.polarnitro);
			findRingBonds(bondMinimumRingSize);
			boolean allowAromatic = mpars.smilesParams.allowaromatic;
			setBondTypes(isAromatic, bondMinimumRingSize, allowAromatic);
			canonize(mpars.computeValenceState); // btype[] sa tu znici
			setValenceState();
			// prec
			findRingBonds(bondMinimumRingSize);
			// v canonize sa to prehadze, dat to tam ?
			setBondTypes(isAromatic, bondMinimumRingSize, allowAromatic);
			// znovy vypoicta btype[]
		} else { // to treba pre stereochemiu
			findRingBonds(bondMinimumRingSize);
			btype = new int[nbonds + 1]; // inak to plni vo findAromatic
			for (int i = 1; i <= nbonds; i++)
				btype[i] = bonds[i].bondType;
		}
		for (int b = 1; b <= nbonds; b++) {
			isRingBond[b] = bondMinimumRingSize[b] > 0;
		}

		int atom = 1; // zacina sa najjednoduchsim
		a = new int[natoms + 1]; // defined globally, fills with 0

		// recursive marching through molecule, path stored int the field a[]
		int step = 1;
		a[atom] = step;
		int nbranch = 0;
		while (true) {
			int ncandidates = 0;
			for (int i = 1; i <= nv(atom); i++) {
				int atomx = v(atom)[i];
				if (a[atomx] > 0) { // hlada ring closure
					if (a[atomx] > a[atom])
						continue;
					if (atomx == parent[atom])
						continue;
					// naslo ring connection (musi ceknut ci uz to nie je zname)
					boolean newcon = true;
					for (int k = 1; k <= nconnections; k++)
						if (con1[k] == atom && con2[k] == atomx || con1[k] == atomx && con2[k] == atom) {
							newcon = false;
							break;
						}
					if (newcon) {
						nconnections++;
						con1[nconnections] = atom;
						con2[nconnections] = atomx;
					}
				} else
					candidate[++ncandidates] = atomx;
			}
			// teraz vetvenie podla poctu este nespracovanych susedov
			if (ncandidates == 0) {
				// nenaslo musi sa vratit o 1 branch nizsie (alebo ukoncit)
				if (step == natoms)
					break;
				// if(nbonds == 0) break; //BB
				atom = branch[nbranch--];
			} else if (ncandidates == 1) {
				parent[candidate[1]] = atom;
				atom = candidate[1];
				a[atom] = ++step;
			} else { // 2 a viac kandidatov
				branch[++nbranch] = atom;
				// musi volit z viacerych moznych pokracovani
				int atomnew = 0;
				// hlada nering branch
				for (int i = 1; i <= ncandidates; i++) {
					int b = getBondIndex(candidate[i], atom);
					// zmenene oproti mgpj
					// if (btype[b]==h.AROMATIC || btype[b]==h.RING_NONAROMATIC) continue;
					if (isRingBond[b])
						continue;
					atomnew = candidate[i];
					break;
				}
				// ring branch = or # (aby potom closing bolo -)
				if (atomnew == 0) { // nenaslo nonring branch
					for (int i = 1; i <= ncandidates; i++) {
						int b = getBondIndex(candidate[i], atom); // pozor bondType[], btype[]
						// if (isDouble(bondType[b]) || bondType[b]==TRIPLE) // !!!
						if (btype[b] == Bond.DOUBLE || btype[b] == Bond.TRIPLE) {
							atomnew = candidate[i];
							break;
						}
					}
				}
				// nenaslo multiple ring bond, zoberie teda prve mozne (ring - or :)
				if (atomnew == 0)
					atomnew = candidate[1];
				parent[atomnew] = atom;
				atom = atomnew;
				a[atom] = ++step; // nemoze byt koniec, lebo viac kandidatov
			}
		}
		// for (int i=1;i<=natoms;i++) System.out.println(i+" "+a[i]+" "+an[i]);
		// for (int i=1;i<=nconnections;i++) System.out.println(i+" "+con1[i]+"
		// "+con2[i]);

		// -------- 2. priechod
		parent = new int[natoms + 1]; // zmenene
		int aa[] = new int[natoms + 1]; // nove poradie
		boolean leftBracket[] = new boolean[natoms + 1];
		boolean rightBracket[] = new boolean[natoms + 1];

		nbranch = 0;
		step = 0;
		int atomold = 0;

		for (int i = 1; i <= natoms; i++)
			if (a[i] == 1) {
				atom = i;
				break;
			} // moze byt != 1?

		loopTwo: while (true) { // hlavny loop - kym neurobi vsetky atomy

			// adding bond
			if (atomold > 0)
				parent[atom] = atomold;

			// prida atom
			aa[++step] = atom; // poradie
			a[atom] = 0;

			// hlada dalsi atom
			// pri vetveni ide podla najnizsieho a[], ak viac moznosti urobi branch
			// musi naplnit atom (current) aj atomold (predosly s nim spojeny)
			int atomnew, ncandidates;
			while (true) {
				atomnew = 0;
				ncandidates = 0;
				int min = natoms + 1;
				cs1: for (int i = 1; i <= nv(atom); i++) {
					int atomx = v(atom)[i];
					// checknut ci atomx nie je s atom connected cislom
					for (int j = 1; j <= nconnections; j++)
						if (con1[j] == atomx && con2[j] == atom || con1[j] == atom && con2[j] == atomx)
							continue cs1;
					if (a[atomx] > 0) {
						ncandidates++;
						if (a[atomx] < min) {
							atomnew = atomx;
							min = a[atomx];
						}
					}
				}
				if (atomnew == 0) {
					// koniec branchu, alebo uzavrety kruh
					if (nbranch == 0)
						break loopTwo; // koniec 2. priechodu
					rightBracket[atom] = true;
					atom = branch[nbranch--];
					// musi ist znovu do loopu najst dalsi nespracovany atom
				} else
					break; // vnutorneho while loopu
			}

			atomold = atom;
			atom = atomnew;
			if (ncandidates > 1) {
				branch[++nbranch] = atomold;
				leftBracket[atom] = true;
			}

		} // end of 2. priechodu

		// identification of stereo atoms
		int slashBond[] = new int[nbonds + 1]; // info about / or \ bonds (1,0,or -1)
		int slimak[] = new int[natoms + 1]; // info about @ or @@ (1,0,or -1)
		if (mpars.smilesParams.stereo)
			smilesStereo(aa, parent, slashBond, slimak, bondMinimumRingSize, con1, con2, nconnections);

		// -------- vlastne vytvaranie SMILESu
		// poradie ako sa vytvara je ulozene v aa[] a parent[]
		boolean queryMode = false; // dqp support
		// all X atoms takes as query? how to improve this ?
		// 2009.04 reverted, caused problems c1:c:[Ir]:c:c:c:1
		// uncomment for dqp
		// for (int i=1;i<=natoms;i++)
		// if (an[i] == Atom.AN_X && !label[i].equals("H")) queryMode = true;

		StringBuffer smiles = new StringBuffer("");
		int ax[] = new int[natoms + 1]; // kvoli connections
		for (int i = 1; i <= natoms; i++)
			ax[aa[i]] = i; // ax[i] - kolky sa robi atom i
		for (int i = 1; i <= natoms; i++) {
			atom = aa[i];
			// assert (atom > 0);
			if (leftBracket[atom])
				smiles.append("(");
			if (parent[i] > 0)
				smilesAddBond(atom, parent[atom], smiles, slashBond, queryMode);
			smilesAddAtom(atom, smiles, isAromatic[atom], slimak);

			for (int j = 1; j <= nconnections; j++) {
				if (con1[j] == atom || con2[j] == atom) {
					// pridava len 1. vazbu na connection
					int atom2 = con2[j];
					if (atom2 == atom)
						atom2 = con1[j];
					if (ax[atom] < ax[atom2])
						smilesAddBond(con1[j], con2[j], smiles, slashBond, queryMode);
					if (j > 9)
						smiles.append("%");
					smiles.append(new Integer(j).toString());
				}
			}

			if (rightBracket[atom])
				smiles.append(")");
		}

		return smiles.toString();
	}

	private Parameters.HydrogenParams setHydrogenParams(Parameters mpars) {
		boolean keepStereo = !mpars.smilesParams.stereo;
		Parameters.HydrogenParams pars = new Parameters().hydrogenParams;
		// BH TODO -- this should be allowed to be modifed based on user settings
		pars.keepStereoHs = keepStereo;
		pars.removeHs = true;
		pars.removeOnlyCHs = false;
		return pars;
	}

	// ----------------------------------------------------------------------------
	private void smilesAddAtom(int at, StringBuffer smiles, boolean isAromatic, int slimak[]) {
		String z = "X";
		Atom atom = this.atoms[at];
		int iso = atom.iso;
		int nh = atom.nh;
		int q = atom.q;
		int an = atom.an;

		boolean bracket = false;
		if (q != 0 || iso != 0)
			bracket = true;
		if (slimak[at] != 0)
			bracket = true;

		int map = this.findAtomMapForOutput(at);
		boolean isMapped = (map != 0);
		bracket = bracket || isMapped;

		if (doMark && atoms[at].backgroundColors[0] > 0) {
			bracket = true;
		}

		switch (an) {
		case Atom.AN_B:
			z = "B";
			break;
		case Atom.AN_C:
			if (isAromatic)
				z = "c";
			else
				z = "C";
			// tu aromaticitu ???
			break;
		case Atom.AN_N:
			if (isAromatic) {
				z = "n";
				if (nh > 0)
					bracket = true;
			} else
				z = "N";
			break;
		case Atom.AN_O:
			if (isAromatic)
				z = "o";
			else
				z = "O";
			break;
		case Atom.AN_P:
			if (isAromatic) {
				z = "p";
				if (nh > 0)
					bracket = true;
			} else
				z = "P";
			break;
		case Atom.AN_S:
			if (isAromatic)
				z = "s";
			else
				z = "S";
			break;
		case Atom.AN_SE:
			if (isAromatic)
				z = "se";
			else
				z = "Se";
			bracket = true;
			break;
		case Atom.AN_SI:
			z = "Si";
			bracket = true;
			break;
		case Atom.AN_F:
			z = "F";
			break;
		case Atom.AN_CL:
			z = "Cl";
			break;
		case Atom.AN_BR:
			z = "Br";
			break;
		case Atom.AN_I:
			z = "I";
			break;
		case Atom.AN_H:
			z = "H";
			bracket = true;
			break;
		// BB: remove R
		// case Atom.AN_R: z = "R"; bracket = true; break;
		// case Atom.AN_R1: z = "R1"; bracket = true; break;
		// case Atom.AN_R2: z = "R2"; bracket = true; break;
		// case Atom.AN_R3: z = "R3"; bracket = true; break;
		case Atom.AN_X:
			bracket = true;
			z = atoms[at].label;

			// BB June 2020
			if (z == null) {
				z = "X";
			}

			// special pre query - * a A
			// if ((lmark == unmarked) && (z.equals("*") || z.equals("a") || z.equals("A")))
			// bracket = false; //BB added lmark == unmarked , reason if there is an atom
			// ampping, then the braket must be used
			if ((!isMapped) && (z.equals("*") || z.equals("a") || z.equals("A")))
				bracket = false; // BB added lmark == unmarked , reason if there is an atom ampping, then the
									// braket must be used
			break;
		}

		if (Atom.chargedMetalType(an) > 0) {
			z = Atom.zlabel[an];
			bracket = true;
		}
		if (an >= Atom.AN_R && an <= Atom.AN_R_LAST) {
			bracket = true;
			z = Atom.zlabel[an]; // R, R1, R2, ...

		}
		if (bracket) {

			// BB
			if (iso != 0) {
				z = "[" + iso + z;
			} else {
				z = "[" + z;
			}

			if (slimak[at] == 1)
				z += "@";
			else if (slimak[at] == -1)
				z += "@@";

			// Bug found by Peter: pyrole n does not get an H
			// BB query atoms should define their own hydrogen count with the query box -Feb
			// 2015: can't reproduce the issue
			if (true) {
				if (nh == 1)
					z += "H";
				else if (nh > 1)
					z += "H" + nh;
			}
			if (q != 0) {
				if (q > 0)
					z += "+";
				else
					z += "-";
				if (Math.abs(q) > 1)
					z += Math.abs(q);
			}
			// if(lmark != unmarked) /*if (lmark > -1)*/ z += ":" + lmark;
			if (isMapped)
				/* if (lmark > -1) */ z += ":" + map;
			z += "]";
		}

		smiles.append(z);
	}

	// ----------------------------------------------------------------------------
	private void smilesAddBond(int atom1, int atom2, StringBuffer smiles, int slashBond[], boolean queryMode) {
		// adds bond to SMILES
		int b = getBondIndex(atom1, atom2);
		Bond bond = bonds[b];
		// what is the difference between btype[b] and bonds[b].bondType ?
		// btype is computed locally and contains information about aromaticity
		// idea: for the bond type, use bits inside an int: type & AROMATIC
		if (btype[b] != Bond.AROMATIC && bond.isDouble())
			smiles.append("=");
		else if (bond.isTriple())
			smiles.append("#");
		else if (bond.isQuery()) {
			/*
			 * String z = "?"; switch (stereob[b]) { case QB_ANY: z = "~"; break; case
			 * QB_AROMATIC: z = ":"; break; case QB_RING: z = "@"; break; case QB_NONRING: z
			 * = "!@"; break; }
			 */
			// 2007.10 dqp support
			String z = "?";
			Object o = bond.btag;
			if (o != null)
				z = (String) o;
			smiles.append(z);
		} else if (btype[b] == Bond.AROMATIC && queryMode)
			smiles.append(":");
		else if (bond.isCoordination())
			smiles.append("~"); // May 2017 stwereo is not encoded in the coordination

		// stereo
		else if (slashBond[b] == 1)
			smiles.append("/");
		else if (slashBond[b] == -1)
			smiles.append("\\");
		else {
			// single bond: no symbol
		}

	}

	// ----------------------------------------------------------------------------
	private void smilesStereo(int aa[], int parent[], int slashBond[], int slimak[], int bondMinimumRingSize[],
			int con1[], int con2[], int nconnections) {
		// --- identifikuje stereocentra, plni slashBond[] a slimak[]
		// (c) Peter Ertl - April 1999

		// pozor v aa[1] je ktory atom sa robi ako 1. (nie kolky sa robi atom 1)
		// Attention AA [1] wherein the hydrogen is being done as the first (How many
		// are doing atom 1)
		int ax[] = new int[natoms + 1];
		for (int at = 1; at <= natoms; at++)
			ax[aa[at]] = at; // ax[i] - kolky sa robi atom i

		// E,Z bonds
		// nesmie ist v poradi od 1 do nbonds, lebo si poprepisuje slash[]
		// preto to je robene v poradi kreacie smilesu
		boolean doneEZ[] = new boolean[nbonds + 1]; // kvoli connections
		for (int i = 1; i <= natoms; i++) {
			int atom1 = aa[i]; // aa[] nie ax[] !
			int atom2 = parent[atom1];
			int bi = getBondIndex(atom1, atom2);
			if (bi == 0)
				continue;
			stereoEZ(bi, ax, slashBond, bondMinimumRingSize);
			doneEZ[bi] = true;
		}
		// teraz este mozne spoje (malo pravdepodobne, ale pre istotu)
		for (int i = 1; i <= nbonds; i++) {
			if (!doneEZ[i])
				stereoEZ(i, ax, slashBond, bondMinimumRingSize);
		}
		doneEZ = null;

		// C4 stereocentra && allene
		atom_loop: for (int at = 1; at <= natoms; at++) {
			if (nv(at) < 2 || nv(at) > 4)
				continue;
			int nstereo = 0, doubleBonded = 0;
			for (int neighbor = 1; neighbor <= nv(at); neighbor++) {
				int bi = getBondIndex(at, v(at)[neighbor]);
				if (btype[bi] == Bond.AROMATIC)
					continue atom_loop;
				if (bonds[bi].bondType == Bond.SINGLE && upDownBond(bonds[bi], at) != 0)
					nstereo++;
				if (bonds[bi].bondType == Bond.DOUBLE)
					doubleBonded = v(at)[neighbor];
			}
			if (nstereo == 0)
				continue;

			if (doubleBonded > 0) // allene, e.g. =C<stereo
				stereoAllene(at, ax, slimak, parent, con1, con2, nconnections);
			else // --- C4 stereo
				stereoC4(at, parent, ax, con1, con2, nconnections, slimak);
		}
	}

	// ----------------------------------------------------------------------------
	private void stereoC4(int atom, int parent[], int ax[], int con1[], int con2[], int nconnections, int slimak[]) {

		// najde 4 referencne atomy (v poradi v akom su v SMILESe)
		int ref[] = new int[4]; // 0 - atom z ktoreho sa pozera
		int refx[] = new int[4]; // ci je up, dpwn, 0

		identifyNeighbors(atom, ax, parent, con1, con2, nconnections, ref);

		// reference bonds + help variables
		int nup = 0, ndown = 0;
		int up = 0, down = 0, marked = 0, nonmarked = 0; // jediny markovany / nem.
		for (int i = 0; i < 4; i++) {
			if (ref[i] <= 0)
				continue;
			int bi = getBondIndex(atom, ref[i]);
			refx[i] = upDownBond(bonds[bi], atom);
			if (refx[i] > 0) {
				nup++;
				up = ref[i];
				marked = ref[i];
			} else if (refx[i] < 0) {
				ndown++;
				down = ref[i];
				marked = ref[i];
			} else
				nonmarked = ref[i];
		}
		int nstereo = nup + ndown;

		// for (int i=0;i<4;i++) System.out.println(atom+" "+ref[i]+" "+refx[i]);

		int ox[]; // ref[] ordered clockwise
		int t[] = new int[4]; // rohy tetraedra, transformacia t[] urci @ || @@
		int stereoRef = 0; // ci t[0] je up || down
		if (atoms[atom].nv == 3) {
			if ((nup == 1 && ndown == 1) || (nstereo == 3 && nup > 0 && ndown > 0)) {
				info("Error in C3H stereospecification !");
				return;
			}
			int refAtom = ref[0];
			if (nstereo == 1)
				refAtom = marked;
			else if (nstereo == 2)
				refAtom = nonmarked;
			ox = C4order(atom, refAtom, ref);
			t[0] = marked;
			t[1] = -1;
			t[2] = ox[2];
			t[3] = ox[1];
			if (nup > 0)
				stereoRef = 1;
			else
				stereoRef = -1;
		}

		else if (nv(atom) == 4) {
			if (nstereo == 1) {
				ox = C4order(atom, marked, ref);
				t[0] = ox[0];
				t[1] = ox[3];
				t[2] = ox[2];
				t[3] = ox[1];
				if (nup > 0)
					stereoRef = 1;
				else
					stereoRef = -1;
			} else { // 2,3,4 stereobonds
				int refAtom = ref[0];
				if (nonmarked > 1)
					refAtom = nonmarked;
				if (nup == 1)
					refAtom = up;
				else if (ndown == 1)
					refAtom = down;
				ox = C4order(atom, refAtom, ref);
				int box[] = new int[4]; // up,down info for ox[] atoms
				for (int i = 0; i < 4; i++) {
					int bi = getBondIndex(atom, ox[i]);
					box[i] = upDownBond(bonds[bi], atom);
				}

				if (nstereo == 4) {
					if (nup == 0 || ndown == 0) {
						info("Error in C4 stereospecification !");
						return;
					} else if (nup == 1 || ndown == 1) { // 3/1 ta 1 je stereoRef
						t[0] = ox[0];
						t[1] = ox[3];
						t[2] = ox[2];
						t[3] = ox[1];
						stereoRef = box[0]; // up || down
					} else { // 2/2 - premeni na 2/0
						for (int i = 0; i < 4; i++)
							if (box[i] == -1)
								box[i] = 0;
						nstereo = 2;
					}
				} else if (nstereo == 3) {
					if (nup == 3 || ndown == 3) {
						// ref je single
						t[0] = ox[0];
						t[1] = ox[3];
						t[2] = ox[2];
						t[3] = ox[1];
						if (nup > 0)
							stereoRef = -1;
						else
							stereoRef = 1; // opacne ???
					} else { // 2,1,nonmarked, zmeni to na 2,0
						int d = 0;
						if (nup == 1) {
							d = 1;
							nup = 1;
						} else {
							d = -1;
							ndown = -1;
						}
						for (int i = 0; i < 4; i++)
							if (box[i] == d)
								box[i] = 0;
						nstereo = 2;
					}
				}
				if (nstereo == 2) { // if (nie else), lebo berie aj 4 a 3 zmenene na 2
					// 3 moznosti
					if (nup == 1 && ndown == 1) {
						// down nepocita (musi ju vyhodit z ox)
						if (ox[1] == down) {
							ox[1] = ox[2];
							ox[2] = ox[3];
						} else if (ox[2] == down) {
							ox[2] = ox[3];
						}
						t[0] = up;
						t[1] = down;
						t[2] = ox[2];
						t[3] = ox[1];
						stereoRef = 1;
					} else {
						// zistuje, ci markovane nie su vedla seba (to nesmie byt)
						if ((box[0] == box[1]) || (box[1] == box[2])) {
							info("Error in C4 stereospecification ! 2/0r");
							return;
						}
						if (box[0] != 0) {
							t[0] = ox[0];
							t[1] = ox[2];
							t[2] = ox[1];
							t[3] = ox[3];
						} else {
							t[0] = ox[1];
							t[1] = ox[3];
							t[2] = ox[2];
							t[3] = ox[0];
						}
						if (nup > 1)
							stereoRef = 1;
						else
							stereoRef = -1; // ???
					}
				}
			}
		}

		stereoTransformation(t, ref);

		if (t[2] == ref[2])
			slimak[atom] = 1;
		else if (t[2] == ref[3])
			slimak[atom] = -1;
		else
			info("Error in stereoprocessing ! - t30");

		slimak[atom] *= stereoRef;
	}

	// ----------------------------------------------------------------------------
	private void identifyNeighbors(int atom, int ax[], int parent[], int con1[], int con2[], int nconnections,
			int ref[]) {
		// naplna ref[] od 0
		// urci sesedov atom-u v tom istom poradi ako su v SMILESe, uvazuje aj con
		int nref = -1;
		if (parent[atom] > 0)
			ref[++nref] = parent[atom];
		for (int i = 1; i <= nconnections; i++) { // poradie connections ako v SMILESe
			if (con1[i] == atom)
				ref[++nref] = con2[i];
			if (con2[i] == atom)
				ref[++nref] = con1[i];
		}
		for (int i = nref + 1; i < nv(atom); i++) {
			int min = natoms + 1;
			jloop: for (int j = 1; j <= nv(atom); j++) {
				int atomx = v(atom)[j];
				for (int k = 0; k < i; k++)
					if (atomx == ref[k])
						continue jloop;
				if (ax[atomx] < min) {
					min = ax[atomx];
					ref[i] = atomx;
				}
			}
		}
		// teraz su zotriedne od 0 do 3, prip od 0 do 2 ak H
		// H ref atom markovany ako -1
		if (parent[atom] == 0 && atoms[atom].nh > 0) { // nikdy nenastane, vzdy ma parent
			ref[3] = ref[2];
			ref[2] = ref[1];
			ref[1] = ref[0];
			ref[0] = -1;
			System.out.println("stereowarning #7");
		} else if (atoms[atom].nh > 0) {
			ref[3] = ref[2];
			ref[2] = ref[1];
			ref[1] = -1;
		}
	}

	// --------------------------------------------------------------------
	// Update PE Aug 2017
	// called from stereoC4
	int[] C4order(int center, int ref0, int ref[]) {
		// returns order of atoms around the center clockwise
		// return 2 or 3 atoms ox[1], ox[2] (ox[3])
		// ref0 in ox[0]
		// if only 1 bond is up/down it os ref0
		// if 2 bonds are up/down - nonmarked is in ref0
		int ox[] = new int[4];
		double dx, dy, rx;

		// reference angle ux-axis - center - ref0
		dx = x(ref0) - x(center);
		dy = y(ref0) - y(center);
		rx = Math.sqrt(dx * dx + dy * dy);
		if (rx < 0.001)
			rx = 0.001;
		double sin0 = dy / rx;
		double cos0 = dx / rx;

		// other atoms are rotated clockwise o uhol sin0, cos0
		// dx = x * cosa + y * sina
		// dy = y * cosa - x * sina

		int p[] = new int[4];
		for (int i = 0; i < 4; i++) {
			if (ref[i] == ref0 || ref[i] <= 0)
				continue;
			if (p[1] == 0) {
				p[1] = ref[i];
				continue;
			}
			if (p[2] == 0) {
				p[2] = ref[i];
				continue;
			}
			if (p[3] == 0) {
				p[3] = ref[i];
				continue;
			}
		}

		double sin[] = new double[4], cos[] = new double[4];
		for (int i = 1; i <= 3; i++) {
			if (i == 3 && p[3] == 0)
				continue;
			dx = (x(p[i]) - x(center)) * cos0 + (y(p[i]) - y(center)) * sin0;
			dy = (y(p[i]) - y(center)) * cos0 - (x(p[i]) - x(center)) * sin0;
			rx = Math.sqrt(dx * dx + dy * dy);
			if (rx < 0.001)
				rx = 0.001;
			sin[i] = dy / rx;
			cos[i] = dx / rx;
		}

		// sorts p[1] - p[3] - 1 smallest
		// 6 posible combinations
		int c12 = JMEUtil.compareAngles(sin[1], cos[1], sin[2], cos[2]);
		if (p[3] > 0) {
			int c23 = JMEUtil.compareAngles(sin[2], cos[2], sin[3], cos[3]);
			int c13 = JMEUtil.compareAngles(sin[1], cos[1], sin[3], cos[3]);
			if (c12 > 0 && c23 > 0) {
				ox[1] = p[1];
				ox[2] = p[2];
				ox[3] = p[3];
			} else if (c13 > 0 && c23 < 0) {
				ox[1] = p[1];
				ox[2] = p[3];
				ox[3] = p[2];
			} else if (c12 < 0 && c13 > 0) {
				ox[1] = p[2];
				ox[2] = p[1];
				ox[3] = p[3];
			} else if (c23 > 0 && c13 < 0) {
				ox[1] = p[2];
				ox[2] = p[3];
				ox[3] = p[1];
			} else if (c13 < 0 && c12 > 0) {
				ox[1] = p[3];
				ox[2] = p[1];
				ox[3] = p[2];
			} else if (c23 < 0 && c12 < 0) {
				ox[1] = p[3];
				ox[2] = p[2];
				ox[3] = p[1];
			}
		}
		// comparing angles 2 and 3
		else {
			if (c12 > 0) {
				ox[1] = p[1];
				ox[2] = p[2];
			} else {
				ox[1] = p[2];
				ox[2] = p[1];
			}

			// need to check that all 3 ref atoms are not on the same side
			double u12 = angle(center, p[1], p[2]);
			double u23 = angle(center, p[2], ref0);
			double u13 = angle(center, p[1], ref0);

			if ((u12 + u23) < Math.PI || (u23 + u13) < Math.PI || (u12 + u13) < Math.PI) {
				// 3 reference atoms are on one side
				// need to check also that the atom with up/down sign (ref0) is between 2 other
				// atoms
				if (u12 > u23 && u12 > u13) {
					int o = ox[1];
					ox[1] = ox[2];
					ox[2] = o;
				}
			}

		}
		ox[0] = ref0;
		return ox;
	}
	// ----------------------------------------------------------------------------

	// ----------------------------------------------------------------------------
	int[] C4orderOLD(int center, int ref0, int ref[]) {
		// vrati clockwise poradie ostatnych atomov okolo center, relativne k ref0
		// vrati 2, alebo 3 atomy ako ox[1], ox[2] (ox[3])
		// v ox[0] vrati ref0
		int ox[] = new int[4];
		double dx, dy, rx;

		// referencny uhol x-axis - center - ref0
		dx = x(ref0) - x(center);
		dy = y(ref0) - y(center);
		rx = Math.sqrt(dx * dx + dy * dy);
		if (rx < 0.001)
			rx = 0.001;
		double sin0 = dy / rx;
		double cos0 = dx / rx;

		// ostatne atomy otaca clockwise o uhol sin0, cos0
		// dx = x * cosa + y * sina
		// dy = y * cosa - x * sina

		// naplna p[]
		int p[] = new int[4];
		for (int i = 0; i < 4; i++) {
			if (ref[i] == ref0 || ref[i] <= 0)
				continue; // moze byt -1 ?? treba to
			if (p[1] == 0) {
				p[1] = ref[i];
				continue;
			}
			if (p[2] == 0) {
				p[2] = ref[i];
				continue;
			}
			if (p[3] == 0) {
				p[3] = ref[i];
				continue;
			}
		}

		double sin[] = new double[4], cos[] = new double[4];
		for (int i = 1; i <= 3; i++) {
			if (i == 3 && p[3] == 0)
				continue;
			dx = (x(p[i]) - x(center)) * cos0 + (y(p[i]) - y(center)) * sin0;
			dy = (y(p[i]) - y(center)) * cos0 - (x(p[i]) - x(center)) * sin0;
			rx = Math.sqrt(dx * dx + dy * dy);
			if (rx < 0.001)
				rx = 0.001;
			sin[i] = dy / rx;
			cos[i] = dx / rx;
		}

		// teraz zoradi p[1] - p[3] podla velkosti (najmensi bude 1, potom 2 ...)
		// 6 moznych kombinacii
		int c12 = JMEUtil.compareAngles(sin[1], cos[1], sin[2], cos[2]);
		if (p[3] > 0) {
			int c23 = JMEUtil.compareAngles(sin[2], cos[2], sin[3], cos[3]);
			int c13 = JMEUtil.compareAngles(sin[1], cos[1], sin[3], cos[3]);
			if (c12 > 0 && c23 > 0) {
				ox[1] = p[1];
				ox[2] = p[2];
				ox[3] = p[3];
			} else if (c13 > 0 && c23 < 0) {
				ox[1] = p[1];
				ox[2] = p[3];
				ox[3] = p[2];
			} else if (c12 < 0 && c13 > 0) {
				ox[1] = p[2];
				ox[2] = p[1];
				ox[3] = p[3];
			} else if (c23 > 0 && c13 < 0) {
				ox[1] = p[2];
				ox[2] = p[3];
				ox[3] = p[1];
			} else if (c13 < 0 && c12 > 0) {
				ox[1] = p[3];
				ox[2] = p[1];
				ox[3] = p[2];
			} else if (c23 < 0 && c12 < 0) {
				ox[1] = p[3];
				ox[2] = p[2];
				ox[3] = p[1];
			}
		}
		// porovnanie len 2 uhlov (2 and 3)
		else {
			if (c12 > 0) {
				ox[1] = p[1];
				ox[2] = p[2];
			} else {
				ox[1] = p[2];
				ox[2] = p[1];
			}
			// fix for the rare stereochemistry bug (2017,07)
			// for example: C[C@H]1CCC[C@H]2CC[C@@H]1O2
			// need to check whether all 3 ref atoms are not on the same side
			double u12 = angle(center, p[1], p[2]);
			double u23 = angle(center, p[2], ref0);
			double u13 = angle(center, p[1], ref0);

			// if this is the case, the reverting stereo
			if ((u12 + u23) < Math.PI || (u23 + u13) < Math.PI || (u12 + u13) < Math.PI) {
				int o = ox[1];
				ox[1] = ox[2];
				ox[2] = o;
			}
			// end of the fix

		}
		// System.out.println("center = "+center+" ref = "+ref0+" order "+ox[1]+"
		// "+ox[2]+" "+ox[3]);
		ox[0] = ref0;
		return ox;
	}

	private double angle(int p1, int p2, int p3) {
		// angle according to the cosine law
		double d;
		double r12 = Math.sqrt((d = x(p1) - x(p2)) * d + (d = y(p1) - y(p2)) * d);
		double r13 = Math.sqrt((d = x(p1) - x(p3)) * d + (d = y(p1) - y(p3)) * d);
		double r23 = Math.sqrt((d = x(p2) - x(p3)) * d + (d = y(p2) - y(p3)) * d);
		return Math.acos((r12 * r12 + r13 * r13 - r23 * r23) / (2. * r12 * r13));
	}

	private int[] doubleBondNeighborsOfAtom1WithoutDoubleBondedAtom(int atom1) {
		int neighbors[] = { 0, 0 };
		//assert (nv(atom1) <= 3);
		for (int j = 1, n = 0; j <= nv(atom1); j++) {
			int atomx = v(atom1)[j];
			int bi = getBondIndex(atom1, atomx);
			if (bondType(bi) == Bond.DOUBLE)
				continue;
			neighbors[n++] = atomx;
		}

		return neighbors;
	}

	// ----------------------------------------------------------------------------
	/**
	 * Fails with cumulene -BB correted
	 * 
	 * @param bond
	 * @param ax
	 * @param slashBond
	 * @param bondMinimumRingSize
	 */
	private void stereoEZ(int bond, int ax[], int slashBond[], int bondMinimumRingSize[]) {
		// each side of the double bond must have at least one substituent and not more
		// than 2

		// int bondType = this.bonds[bond].bondType;
		if (bondType(bond) != Bond.DOUBLE || btype[bond] == Bond.AROMATIC)
			return;

		// minimum ring size for having bond that can be either trans or cis in a ring
		if (bondMinimumRingSize[bond] > 0 && bondMinimumRingSize[bond] <= 7)
			return;
		// bondMinimumRingSize[bond] means no ring

		if (!autoez && bonds[bond].stereo != Bond.STEREO_EZ)
			return; // BB October 2016: stereo for large rings is missing

		// BB Feb 2017
		if (bonds[bond].stereo == Bond.STEREO_EZ)
			return;

		int atom1 = bonds[bond].va, atom2 = bonds[bond].vb;

		if (this.atoms[atom1].isCumuleneSP() && this.atoms[atom2].isCumuleneSP()) {
			return;
		}
		// look for a cumulene with an odd number of atoms
		int cumuleneAtoms[] = null;
		if (this.atoms[atom1].isCumuleneSP()) {
			cumuleneAtoms = this.findCumuleneChain(atom2);
		} else if (this.atoms[atom2].isCumuleneSP()) {
			cumuleneAtoms = this.findCumuleneChain(atom1);
		}

		// cumuleneAtoms[0] = number of atoms in the cumulene
		if (cumuleneAtoms != null) {
			if ((cumuleneAtoms[0] - 1) % 2 == 1) { // if odd number of =
				atom1 = cumuleneAtoms[1];
				atom2 = cumuleneAtoms[cumuleneAtoms[0]]; // end of cumulene
			} else
				return;
		}

		int a[] = { atom1, atom2 };

		// each side of the double bond must have at least one substituent and not more
		// than 2
		for (int eachBondAtom : a) {
			int nv = nv(eachBondAtom);
			// vyhodi =CH2, =O, =P-<, ...
			// if (nv(atom1)<2 || nv(atom2)<2 || nv(atom1)>3 || nv(atom2)>3)
			// return;
			if (nv < 2 || nv > 3)
				return;

		}

		if (ax[atom1] > ax[atom2]) {
			int d = atom1;
			atom1 = atom2;
			atom2 = d;
		}

		// start sith side 1

		// find atoms connected to atom1 : ref11 and ref12
		int[] neighbors = doubleBondNeighborsOfAtom1WithoutDoubleBondedAtom(atom1);
		int ref11 = neighbors[0];
		int ref12 = neighbors[1];
		int ref1 = 0;
		boolean ref1x = false; // ze nie je pred, ale za atom1

		// for (int j = 1; j <= nv(atom1); j++) {
		// int atomx = v(atom1)[j];
		// if (atomx == atom2)
		// continue;
		// if (ref11 == 0)
		// ref11 = atomx;
		// else
		// ref12 = atomx;
		// }
		if (ref12 > 0 && ax[ref11] > ax[ref12]) {
			int d = ref11;
			ref11 = ref12;
			ref12 = d;
		}
		//assert (ref11 > 0); // reff12 can be 0 (e.g. implicit H)
		int bi = getBondIndex(atom1, ref11);
		if (slashBond[bi] != 0) {
			ref1 = ref11;
		} else if (bondType(bi) == Bond.SINGLE && btype[bi] != Bond.AROMATIC)
			ref1 = ref11;

		if (ref1 == 0 && ref12 > 0) { // BB added ref12>0
			bi = getBondIndex(atom1, ref12);
			if (slashBond[bi] != 0)
				ref1 = ref12;
			else if (bondType(bi) == Bond.SINGLE && btype[bi] != Bond.AROMATIC)
				ref1 = ref12;
		}
		if (ax[ref1] > ax[atom1])
			ref1x = true;

		// find atoms connected to atom2 : ref21 and ref22
		// berie prazdnu (treba ref2x ???)
		neighbors = doubleBondNeighborsOfAtom1WithoutDoubleBondedAtom(atom2);
		int ref21 = neighbors[0];
		int ref22 = neighbors[1];

		// sulfonylmethanediamine bug fix
		if (ref21 == 0 && ref22 == 0) {
			return;
		}

		int ref2 = 0;
		// int ref2 = 0, ref21 = 0, ref22 = 0;
		// for (int j = 1; j <= nv(atom2); j++) {
		// int atomx = v(atom2)[j];
		// if (atomx == atom1)
		// continue;
		// if (ref21 == 0)
		// ref21 = atomx;
		// else
		// ref22 = atomx;
		// }
		// to dava 21 s vacsim ax[] (na rozdiel id 12 s mensim ax[])
		if (ref22 > 0 && ax[ref21] < ax[ref22]) {
			int d = ref21;
			ref21 = ref22;
			ref22 = d;
		}
		bi = getBondIndex(atom2, ref21);
		if (bondType(bi) == Bond.SINGLE && btype[bi] != Bond.AROMATIC && slashBond[bi] == 0)
			ref2 = ref21;
		if (ref2 == 0 && ref22 > 0) { // BB added ref22>0
			bi = getBondIndex(atom2, ref22);
			if (bondType(bi) == Bond.SINGLE && btype[bi] != Bond.AROMATIC)
				ref2 = ref22; // ref2x netreba
		}

		if (ref1 == 0 || ref2 == 0)
			return; // vyhadzuje aj allene vazby

		double dx = x(atom2) - x(atom1);
		double dy = y(atom2) - y(atom1);
		double rx = Math.sqrt(dx * dx + dy * dy);
		if (rx < 0.001)
			rx = 0.001;
		double sina = dy / rx;
		double cosa = dx / rx;
		double y1 = (y(ref1) - y(atom1)) * cosa - (x(ref1) - x(atom1)) * sina;
		double y2 = (y(ref2) - y(atom1)) * cosa - (x(ref2) - x(atom1)) * sina;
		if (Math.abs(y1) < 2 || Math.abs(y2) < 2) {
			info("Not unique E/Z geometry !");
			return;
		}
		int b1 = getBondIndex(ref1, atom1);
		int b2 = getBondIndex(ref2, atom2);
		int newSlash = 1;
		if (slashBond[b1] == 0) {
			// ceknut, ci nove stereo neprotireci uz exisujucej na ref1
			for (int j = 1; j <= nv(ref1); j++) {
				int atomx = v(ref1)[j];
				if (atomx == atom1)
					continue;
				bi = getBondIndex(ref1, atomx);
				if (slashBond[bi] != 0) {
					// zalezi od toho ci ta co uz je ma ax[atomx] <> ax[ref1]
					if (ax[atomx] > ax[ref1])
						newSlash = -slashBond[bi];
					else
						newSlash = slashBond[bi]; // lebo ax[atom] < ax[ref1]
					// ???
					break;
				}
			}
			slashBond[b1] = newSlash;
		}
		if (slashBond[b2] != 0) {
			System.err.println("E/Z internal error !");
			return; // prepisuje slash
		}
		// to aj zalezi, ci u tej prvej ref je pred, alebo za atomom (v smilese)
		if ((y1 > 0 && y2 > 0) || (y1 < 0 && y2 < 0))
			slashBond[b2] = -slashBond[b1];
		else
			slashBond[b2] = slashBond[b1];
		if (ref1x)
			slashBond[b2] = -slashBond[b2]; // ref1 je za atomom
	}

	// ----------------------------------------------------------------------------
	static int upDownBond(Bond bond, int atom) {
		// zistuje, ci stereo bond je relevantna k atomu (ci na nom je hrot vazby)
		// ci ide hore 1, dolu -1, alebo nie je stereo
		// pri UP a DOWN je hrot na va[bond], pri XUP a XDOWN na vb[bond]
		int sb = bond.stereo;
		if (sb == Bond.STEREO_NONE || sb > Bond.STEREO_LAST_KNOWN)
			return 0;
		if (sb == Bond.STEREO_UP && bond.va == atom)
			return 1;
		if (sb == Bond.STEREO_DOWN && bond.va == atom)
			return -1;
		if (sb == Bond.STEREO_XUP && bond.vb == atom)
			return 1;
		if (sb == Bond.STEREO_XDOWN && bond.vb == atom)
			return -1;
		return 0;
	}

	/**
	 * 
	 * @param startAtom
	 * @return an array of atom, indices where the first value is the number of
	 *         atoms in the cumulene
	 */
	private int[] findCumuleneChain(int startAtom) {
		int numberCumuleneAtoms = 1; // kolko ich je v chaine
		int currentCumuleneAtom = startAtom; // ala - actually processed atom
		int cumuleneAtoms[];
		cumuleneAtoms = new int[natoms + 1];
		// if we are in the middle of an allene, return
		if (atoms[startAtom].isCumuleneSP()) {
			cumuleneAtoms[0] = 0;
			return cumuleneAtoms;
		}
		cumuleneAtoms[1] = startAtom;
		FIND_CUMULENE_ATOM: while (true) { // loop po allene chain po double vazbe
			// loop over neighbors
			NEIGHBOR_LOOP: for (int ni = 1; ni <= nv(currentCumuleneAtom); ni++) {
				int atomx = v(currentCumuleneAtom)[ni];

				if (atomx == cumuleneAtoms[1] || atomx == cumuleneAtoms[numberCumuleneAtoms - 1])
					continue NEIGHBOR_LOOP;

				int bi = getBondIndex(currentCumuleneAtom, atomx);
				if (bonds[bi].bondType == Bond.DOUBLE && btype[bi] != Bond.AROMATIC) {
					cumuleneAtoms[++numberCumuleneAtoms] = atomx;
					currentCumuleneAtom = atomx;
					continue FIND_CUMULENE_ATOM;
				}
			}
			break FIND_CUMULENE_ATOM;
		}
		cumuleneAtoms[0] = numberCumuleneAtoms;

		return cumuleneAtoms;
	}

	// ----------------------------------------------------------------------------
	private void stereoAllene(int ati, int ax[], int slimak[], int parent[], int con1[], int con2[], int nconnections) {
		double dx, dy, rx, sina, cosa;

		int cumuleneAtoms[] = this.findCumuleneChain(ati);
		int numberCumuleneAtoms = cumuleneAtoms[0]; // kolko ich je v chaine

		// BB check the number of atoms in the cumulene - must be odd otherwise no
		// allene stereo: this is a double bond stereo
		if (numberCumuleneAtoms % 2 == 0)
			return; // parny allene

		// allene definovany start=center=end, aspon na al[1] stereob
		int start = cumuleneAtoms[1];
		int center = cumuleneAtoms[(numberCumuleneAtoms + 1) / 2];
		int end = cumuleneAtoms[numberCumuleneAtoms];

		// this could be simplified? SHould the number of implicit H be taken into
		// account?
		if (nv(end) < 2 || nv(end) > 3)
			return; // nv[al[1]] uz done

		int ref11 = 0, ref12 = 0, ref21 = 0, ref22 = 0, ref1 = 0, ref2 = 0;
		boolean ref1x = false, ref2x = false; // ci je 1. alebo 2. na atome
		// co s >n=C=n< - tam sa neda definovat stereo, lebo nie su ref.

		// find the two atoms connect to the start
		// ref. at start - ta musi byt aspon 1 stereo (inak by to tu nebolo)
		for (int ni = 1; ni <= nv(start); ni++) {
			int atomx = v(start)[ni];

			// BB
			if (atomx == cumuleneAtoms[1 + 1])
				continue;
			// int bi = bondIdentity(start, atomx);
			// if (bonds[bi].bondType != SINGLE || btype[bi] == AROMATIC)
			// continue;
			if (ref11 == 0)
				ref11 = atomx;
			else
				ref12 = atomx;
		}
		if (ax[ref12] > 0 && ax[ref11] > ax[ref12]) {
			int d = ref11;
			ref11 = ref12;
			ref12 = d;
		}
		ref1 = ref11;
		if (ref1 == 0) {
			ref1 = ref12;
			ref1x = true;
		}

		// reference at end
		for (int j = 1; j <= nv(end); j++) {
			int atomx = v(end)[j];
			// BB
			if (atomx == cumuleneAtoms[numberCumuleneAtoms - 1])
				continue;
			// int bi = bondIdentity(end, atomx);
			// if (bonds[bi].bondType != SINGLE || btype[bi] == AROMATIC)
			// continue;
			if (ref21 == 0)
				ref21 = atomx;
			else
				ref22 = atomx;
		}
		// //assert(ref22>0);
		if (ax[ref22] > 0 && ax[ref21] > ax[ref22]) {
			int d = ref21;
			ref21 = ref22;
			ref22 = d;
		}
		ref2 = ref21;
		if (ref2 == 0) {
			ref2 = ref22;
			ref2x = true;
		}

		// na start moze byt 1 alebo 2 stereo (ak 2 => opacne), na end nesmie byt
		// int ref11x = upDownBond(bondIdentity(start,ref11),start);
		// int ref12x = upDownBond(bondIdentity(start,ref12),start);
		// int ref21x = upDownBond(bondIdentity(end,ref21),end);
		// int ref22x = upDownBond(bondIdentity(end,ref22),end);
		// BB bug fix for # atoms at end < 3 (implicit H)
		int ref11x = ref11 > 0 ? upDownBond(bonds[getBondIndex(start, ref11)], start) : 0;
		int ref12x = ref12 > 0 ? upDownBond(bonds[getBondIndex(start, ref12)], start) : 0;
		int ref21x = ref21 > 0 ? upDownBond(bonds[getBondIndex(end, ref21)], end) : 0;
		int ref22x = ref22 > 0 ? upDownBond(bonds[getBondIndex(end, ref22)], end) : 0;

		if (Math.abs(ref11x + ref12x) > 1 || ref21x != 0 || ref22x != 0) {
			info("Bad stereoinfo on allene !");
			return;
		}

		// vlastne nam treba len 1 (up alebo down) a 3 (resp opak 4 ak je 3 H)
		// urcuje poziciu ref2
		dx = x(cumuleneAtoms[numberCumuleneAtoms - 1]) - x(end);
		dy = y(cumuleneAtoms[numberCumuleneAtoms - 1]) - y(end);
		rx = Math.sqrt(dx * dx + dy * dy);
		if (rx < 0.001)
			rx = 0.001;
		sina = dy / rx;
		cosa = dx / rx;
		double y2 = (y(ref2) - y(cumuleneAtoms[numberCumuleneAtoms - 1])) * cosa
				- (x(ref2) - x(cumuleneAtoms[numberCumuleneAtoms - 1])) * sina;

		// teraz to bude dost complikovane
		if (y2 > 0)
			slimak[center] = 1;
		else
			slimak[center] = -1;
		if (ref1x)
			slimak[center] *= -1;
		if (ref2x)
			slimak[center] *= -1;
		if (ref1 == ref11 && ref11x < 0)
			slimak[center] *= -1;
		if (ref1 == ref12 && ref12x < 0)
			slimak[center] *= -1;
		if (ax[ref1] > ax[ref2])
			slimak[center] *= -1;
	}

	public static String getSmiles(JMEmol deepCopy, Parameters pars, boolean isQuery) {
		// from now on, coordination bonds will be shown with "~"
		int nparts = deepCopy.computeMultiPartIndices();
		JMESmiles[] parts = new JMESmiles[nparts];
		for (int part = 1; part <= nparts; part++) {
			parts[part - 1] = new JMESmiles(deepCopy, part, isQuery);
			// new implementation that uses the internal
			// partIndex variable
		}
		String result = "";
		for (int p = 0; p < parts.length; p++) {
			String smiles = parts[p].createSmilesWithSideEffect(pars);
			if (result.length() > 0) {
				result += "."; // separator between molecules
			}
			result += smiles;
			parts[p] = null;
		}
		return result;
	}	

	public void findRingBonds(int sizes[]) {
		for (int i = 1; i <= nbonds; i++) {
			sizes[i] = minimumRingSize(bonds[i]);
		}
	}

	boolean isInRing(int atom, int minBondRingSizes[]) {
		for (int i = 1; i <= nv(atom); i++) {
			if (minBondRingSizes[getBondIndex(atom, v(atom)[i])] > 0)
				return true;
		}
		return false;
	}

	// ----------------------------------------------------------------------------
	void setBondTypes(boolean isAromatic[], int minBondRingSizes[], boolean allowAromatic) {
		// two pass

		btype = new int[nbonds + 1];
		boolean pa[] = new boolean[natoms + 1]; // possible aromatic

		if (allowAromatic) {
			for (int i = 1; i <= natoms; i++) {
				pa[i] = false;
				isAromatic[i] = false;
				if (!isInRing(i, minBondRingSizes))
					continue;
				// if (nv(i)+nh[i]>3) continue; // >X< nemoze byt aromaticky (ako s nabojmi?)
				if (nv(i) + atoms[i].nh > 3)
					continue; // >X< nemoze byt aromaticky (ako s nabojmi?)
				switch (an(i)) {
				case Atom.AN_C:
				case Atom.AN_N:
				case Atom.AN_P:
				case Atom.AN_O:
				case Atom.AN_S:
				case Atom.AN_SE:
					pa[i] = true;
					break;
				case Atom.AN_X:
					// 2013.09
					if (atoms[i].label.startsWith("A"))
						pa[i] = false;
					else
						pa[i] = true;
					break;
				}
			}
		}
		// 2013.09
		if (isQuery)
			doRingQueryCheck(isAromatic, minBondRingSizes);

		// 2. prechod, ide po ring vazbach a cekuje, zaroven plni aj btype[]
		// ignoruje stereo !!!
		for (int b = 1; b <= nbonds; b++) {
			if (isSingle(b))
				btype[b] = Bond.SINGLE;
			else if (isDouble(b))
				btype[b] = Bond.DOUBLE;
			else if (bonds[b].bondType == Bond.TRIPLE)
				btype[b] = Bond.TRIPLE;
			else
				System.err.println("problems in findAromatic " + bonds[b].bondType);
		}
		bondloop: for (int b = 1; b <= nbonds; b++) {
			if (minBondRingSizes[b] == 0)
				continue;
			int atom1 = bonds[b].va;
			int atom2 = bonds[b].vb;
			if (!pa[atom1] || !pa[atom2])
				continue;

			// loop cez molekulu len po pa[] atomoch
			boolean a[] = new boolean[natoms + 1]; // plni na false
			for (int i = 1; i <= nv(atom1); i++) {
				int atom = v(atom1)[i];
				if (atom != atom2 && pa[atom])
					a[atom] = true;
			}

			boolean ok = false;
			while (true) {
				for (int i = 1; i <= natoms; i++) {
					ok = false;
					if (a[i] && pa[i] && i != atom1) {
						for (int j = 1; j <= nv(i); j++) {
							int atom = v(i)[j];
							if (atom == atom2) { // bond b je v aromatickom kruhu
								isAromatic[atom1] = true;
								isAromatic[atom2] = true;
								btype[b] = Bond.AROMATIC;
								continue bondloop;
							}
							if (!a[atom] && pa[atom]) {
								a[atom] = true;
								ok = true;
							}
						}
					}
					if (ok)
						break;
				}
				if (!ok)
					break;
			}

		} // --- bondloop

	}

	// ----------------------------------------------------------------------------
	// new in 2013.09
	void doRingQueryCheck(boolean isAromatic[], int minBondRingSizes[]) {
		// check ring smarts
		// non-query ring atom should change from O,N,S,o,n,s to X #8,#7,#16
		// check whether there are some query atoms in rings
		boolean ra[] = new boolean[natoms + 1]; // ring atoms
		boolean doCheck = false;
		for (int b = 1; b <= nbonds; b++) {
			int atom1 = bonds[b].va;
			int atom2 = bonds[b].vb;
			ra[atom1] = true;
			ra[atom2] = true;
			if (an(atom1) == Atom.AN_X || an(atom2) == Atom.AN_X)
				doCheck = true;
		}
		if (!doCheck)
			return; // no query atoms in rings

		bondloop1: for (int b = 1; b <= nbonds; b++) {
			if (minBondRingSizes[b] == 0)
				continue;
			int atom1 = bonds[b].va;
			int atom2 = bonds[b].vb;
			boolean a[] = new boolean[natoms + 1]; // tracking
			for (int i = 1; i <= nv(atom1); i++) {
				int atom = v(atom1)[i];
				if (atom != atom2 && ra[atom])
					a[atom] = true;
			}

			boolean ok = false;
			while (true) {
				for (int i = 1; i <= natoms; i++) {
					ok = false;
					if (a[i] && ra[i] && i != atom1) {
						for (int j = 1; j <= nv(i); j++) {
							int atom = v(i)[j];
							if (atom == atom2) { // bond b je v aromatickom kruhu

								for (int k = 1; k <= natoms; k++) {
									if (!a[k])
										continue;
									if (an(k) == Atom.AN_O) {
										AN(k, Atom.AN_X);
										atoms[k].label=  "#8";
									}
									if (an(k) == Atom.AN_N) {
										AN(k, Atom.AN_X);
										atoms[k].label= "#7";
									}
									if (an(k) == Atom.AN_S) {
										AN(k, Atom.AN_X);
										atoms[k].label= "#16";
									}
								}

								continue bondloop1;
							}
							if (!a[atom] && ra[atom]) {
								a[atom] = true;
								ok = true;
							}
						}
					}
					if (ok)
						break;
				}
				if (!ok)
					break;
			}

		} // --- bondloop1 end
	}

	void canonize(boolean computeValenceState) {
		// #1 atom will be simplest
		boolean ok;
		int a[] = new int[natoms + 1]; // current ranking
		int aold[] = new int[natoms + 1];
		long d[] = new long[natoms + 1];
		// primes
		long prime[] = new long[natoms + 2]; // +2 primes return minimum 2 values
		prime = JMESmiles.generatePrimes(natoms);

		// seeds
		for (int i = 1; i <= natoms; i++) {
			Atom atom = this.atoms[i];
			int xbo = 1; // product of bond orders
			for (int j = 1; j <= nbonds; j++) { // efektivnejsie ako s bond identity
				if (bonds[j].va == i || bonds[j].vb == i) {
					xbo *= btype[j]; // 1,2,3 alebo 5 (AROMATIC)
				}
			}
			int xan = an(i);
			if (xan == Atom.AN_X) {
				String zlabel = atoms[i].label;
				// nekanonizuje query, ale to nevadi
				if (zlabel != null && zlabel.length() > 0) { // BB avoid String index out of range: 0
					int c1 = zlabel.charAt(0) - 'A' + 1; // +1 aby sa nekrylo z h.AN
					int c2 = 0;
					if (zlabel.length() > 1)
						c2 = zlabel.charAt(1) - 'a';
					if (c1 < 0)
						c1 = 0;
					if (c2 < 0)
						c2 = 0; // pre qry - zostava visiet pri #
					xan = c1 * 28 + c2;
				}
			}
			int qq = 0;
			if (q(i) != 0) { // most of the time charge is 0
				if (q(i) < -2)
					qq = 1;
				else if (q(i) == -2)
					qq = 2;
				else if (q(i) == -1)
					qq = 3;
				else if (q(i) == 1)
					qq = 4;
				else if (q(i) == 2)
					qq = 5;
				else if (q(i) > 2)
					qq = 6;
			}
			// BB
			int deltaIso = 0;
			// if(iso[i] != 0) {
			if (atom.iso != 0) {
				// deltaIso = AtomicElements.getDeltaIsotopicMassOfElement(this.getAtomLabel(i),
				// iso[i]);
				deltaIso = AtomicElements.getDeltaIsotopicMassOfElement(getAtomLabel(i), atom.iso);
				// what should happen if deltaIso < 0?
				if (deltaIso < 0) {
					deltaIso = 10 - deltaIso; // never tested
				}
			}

			// (x musi byt maximum+1)
			int xx = 126;
			int dFactor = xbo;
			dFactor += atoms[i].nh * xx;
			xx *= 7;
			dFactor += qq * xx;
			xx *= 7;
			if (deltaIso != 0) // keep the exact previous behavior if not isotop because I am not sure of side
								// effects
				dFactor += deltaIso * xx;
			xx *= 7; // BB not sure this is correct
			dFactor += xan * xx;
			xx *= 783; // 27*28+26+1
			dFactor += nv(i) * xx;

			d[i] = dFactor;
		}

		int breaklevel = 0;
		while (true) {
			// sorting
			if (canonsort(a, d))
				break;
			// comparing with aold[]
			ok = false;
			for (int i = 1; i <= natoms; i++)
				if (a[i] != aold[i]) {
					aold[i] = a[i];
					ok = true;
				}

			if (ok) { // cize ci sa pohlo dopredu
				for (int i = 1; i <= natoms; i++) {
					d[i] = 1;
					for (int j = 1; j <= nv(i); j++)
						d[i] *= prime[a[v(i)[j]]];
				}
				breaklevel = 0;
			} else { // musi break degeneraciu
				if (breaklevel > 0) { // just random breaking
					for (int i = 1; i <= natoms; i++)
						d[i] = 1;
					bd: for (int i = 1; i <= natoms - 1; i++)
						for (int j = i + 1; j <= natoms; j++)
							if (a[i] == a[j]) {
								d[i] = 2;
								break bd;
							}
				} else { // skusa inteligente
					for (int i = 1; i <= natoms; i++) {
						d[i] = 1;
						// co E,Z,stereovazby, je to OK ???
						for (int j = 1; j <= nv(i); j++) {
							int atom = v(i)[j];
							d[i] *= an(atom) * btype[getBondIndex(i, atom)];
						}
					}
					breaklevel = 1;
				}
			}
			canonsort(a, d);
			for (int i = 1; i <= natoms; i++)
				d[i] = aold[i] * natoms + a[i];
		}

		// reordering atoms podla a[]
		for (int i = 1; i <= natoms; i++)
			aold[i] = a[i];
		// [0] used as a swap space
		for (int s = 1; s <= natoms; s++) {
			for (int i = 1; i <= natoms; i++) {
				if (aold[i] == s) { // changes s and i
					swap(this.atoms, i, s);
					aold[i] = aold[s];
					aold[s] = s;
					break;
				}
			}
		}

		// marked atoms
		// for (int i=1;i<=nmarked;i++) mark[i][0] = a[mark[i][0]]; //mark[][1] zostane
		// System.out.println("mrk1 "+mark[1][0]+" "+mark[2][0]+" "+mark[3][0]);

		// canonization of bonds (pozor na stereo !)
		for (int i = 1; i <= nbonds; i++) {
			bonds[i].va = a[bonds[i].va];
			bonds[i].vb = a[bonds[i].vb];
			if (bonds[i].va > bonds[i].vb) {
				int du = bonds[i].va;
				bonds[i].va = bonds[i].vb;
				bonds[i].vb = du;
				if (bonds[i].stereo == Bond.STEREO_UP)
					bonds[i].stereo = Bond.STEREO_XUP;
				else if (bonds[i].stereo == Bond.STEREO_DOWN)
					bonds[i].stereo = Bond.STEREO_XDOWN;
				else if (bonds[i].stereo == Bond.STEREO_XUP)
					bonds[i].stereo = Bond.STEREO_UP;
				else if (bonds[i].stereo == Bond.STEREO_XDOWN)
					bonds[i].stereo = Bond.STEREO_DOWN;
			}
		}

		// sorting bonds according to va & vb
		// ez ????
		for (int i = 1; i < nbonds; i++) {
			int minva = natoms;
			int minvb = natoms;
			int b = 0;
			for (int j = i; j <= nbonds; j++) {
				if (bonds[j].va < minva) {
					minva = bonds[j].va;
					minvb = bonds[j].vb;
					b = j;
				} else if (bonds[j].va == minva && bonds[j].vb < minvb) {
					minvb = bonds[j].vb;
					b = j;
				}
			}
			swap(this.bonds, i, b);
		}
		complete(computeValenceState);

	}

	public static void swap(AtomBondCommon[] array, int i, int j) {
		AtomBondCommon temp = array[j];
		array[j] = array[i];
		array[i] = temp;

	}


	// ----------------------------------------------------------------------------
	boolean canonsort(int a[], long d[]) {
		// v d[] su podla coho sa triedi, vysledok do a[] (1 1 1 2 3 3 ...)
		// pozor ! d[] nesmie byt 0
		// returns true ak nth = natoms (cize ziadna degeneracia)
		long min = 0;
		int nth = 0;
		int ndone = 0;
		while (true) {
			nth++;
			for (int i = 1; i <= natoms; i++)
				if (d[i] > 0) {
					min = d[i];
					break;
				}
			for (int i = 1; i <= natoms; i++)
				if (d[i] > 0 && d[i] < min)
					min = d[i];
			for (int i = 1; i <= natoms; i++)
				if (d[i] == min) {
					a[i] = nth;
					d[i] = 0;
					ndone++;
				}
			if (ndone == natoms)
				break;
		}
		if (nth == natoms)
			return true;
		else
			return false;
	}

	// ----------------------------------------------------------------------------
	public static long[] generatePrimes(int n) {
		/*
	Prime Number Generator
	code by Mark Chamness (modified slightly by Peter Ertl)
	This subroutine calculates first n prime numbers (starting with 2)
	It stores the first 100 primes it generates. Then it evaluates the rest
	based on those up to prime[100] squared
		 */
		int npn;
		long[] pn = new long[n+2];
		int[] prime = new int[100];
		int test=5, index=0;
		int num=0;
		boolean check=true;
		prime[0]=3;
		pn[1] = 2; pn[2] = 3; npn=2;
		if (n<3) return pn; // very rear case
		while(test<(prime[num]*prime[num])) {
			index=0; check=true;
			while(check==true && index<=num && test>=(prime[index]*prime[index])) {
				if(test%prime[index] == 0)  check=false;
				else index++;
			}
			if(check==true) {
				pn[++npn] = test;
				if (npn >= n) return pn;
				if(num<(prime.length-1)) {
					num++;
					prime[num]=test;
				}
			}
			test+=2;
		}
		System.err.println("ERROR - Prime Number generator failed !");
		return pn;
	}

	public static void stereoTransformation(int t[], int ref[]) {
		// System.out.println(t[0]+" "+t[1]+" "+t[2]+" "+t[3]+" --- ");
		int d = 0;
		if (ref[0] == t[1]) // 0,1 2,3
		{
			d = t[0];
			t[0] = t[1];
			t[1] = d;
			d = t[2];
			t[2] = t[3];
			t[3] = d;
		} else if (ref[0] == t[2]) // 0,2 1,3
		{
			d = t[2];
			t[2] = t[0];
			t[0] = d;
			d = t[1];
			t[1] = t[3];
			t[3] = d;
		} else if (ref[0] == t[3]) // 0,3 1,2
		{
			d = t[3];
			t[3] = t[0];
			t[0] = d;
			d = t[1];
			t[1] = t[2];
			t[2] = d;
		}
	
		if (ref[1] == t[2]) // 1,2 2,3
		{
			d = t[1];
			t[1] = t[2];
			t[2] = d;
			d = t[2];
			t[2] = t[3];
			t[3] = d;
		} else if (ref[1] == t[3]) // 1,3 2,3
		{
			d = t[1];
			t[1] = t[3];
			t[3] = d;
			d = t[2];
			t[2] = t[3];
			t[3] = d;
		}
	}



}