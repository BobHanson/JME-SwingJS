package jme;
public class JMECore {

	public static class Parameters {
		
		public class SmilesParams {
			
			/**
			 * set false to generate fully Kekule SMILES (for SMARTS target)
			 * set true to generate fully aromatic SMILES (for SMARTS pattern)
			 */
			public boolean allowaromatic = true;
			/**
			 * ??
			 */
			public boolean stereo        = true;
			
			public boolean canonize      = true;
			/**
			 * ??
			 */
			public boolean autoez        = true;
			public boolean allHs         = false;
			public boolean star          = false;
			public boolean polarnitro    = false;
			
		}

		/**
		 * mainly handling of hydrogens removal
		 * @author bruno
		 *
		 */
		public class HydrogenParams {		

			public boolean keepStereoHs     = true;
			public boolean keepMappedHs     = true;
			public boolean keepIsotopicHs	= true;	
			public boolean removeHs 		= false;
			public boolean removeOnlyCHs    = false;
		
			/**
			 * (on hetero OH, NH2...)
			 */
			public boolean showHs           = true; 

		}

		public SmilesParams smilesParams     = new SmilesParams();
		public HydrogenParams hydrogenParams = new HydrogenParams();
		public boolean computeValenceState       = true;
		public boolean ignoreStereo              = false; // not implemented
		public boolean mark                      = false;
		
		/**
		 * atom map
		 */
		public boolean number                    = false; 
			
		public boolean showAtomMapNumberWithBackgroundColor = false; // new Jan 2020
		// these two options are related
		public boolean internalBondScalingForInput          = false; // internal scale the molecules from input files - only JME needs this

		/**
		 * do not invert y or center x for output
		 * 
		 * Interactive editor: must be false, testing suite: must be true
		 */
		public boolean keepSameCoordinatesForOutput         = false; 

	}

	static final int NSTART_SIZE_ATOMS_BONDS = 10;
	static final int MAX_BONDS_ON_ATOM = 6;

	final static Parameters DefaultParameters = new Parameters();


	/**
	 * just used for info(
	 */
	JMEStatusListener jmesl;

	Atom atoms[] = new Atom[NSTART_SIZE_ATOMS_BONDS];
	Bond bonds[] = new Bond[NSTART_SIZE_ATOMS_BONDS];

	int natoms = 0;
	int nbonds = 0;
	
	protected Boolean chiralFlag = Boolean.FALSE;

	public Parameters parameters;

	public JMECore(JMECore mol, int part) {
		setPart(mol, part);
	}

	public JMECore(JMEStatusListener jmesl, Parameters pars) {
		this.jmesl = jmesl;
		parameters = (pars == null ? new Parameters() : pars);
		atoms[0] = new Atom();
		natoms = 0;
		nbonds = 0;
	}

	public JMECore(JMECore m) {
		parameters = m.parameters;
		natoms = m.natoms;
		nbonds = m.nbonds;
		atoms = new Atom[natoms + 1];
		for (int i = this.atoms.length; --i >= 0;) {
			if (m.atoms[i] != null) {
				this.atoms[i] = m.atoms[i].deepCopy();
			}
		}

		bonds = new Bond[nbonds + 1];
		for (int i = this.bonds.length; --i >= 0;) {
			if (m.bonds[i] != null) {
				this.bonds[i] = m.bonds[i].deepCopy();
			}
		}

	}

	public int getAtomCount() {
		return natoms;
	}

	public int nAtoms() {
		return natoms;
	}

	public int getBondCount() {
		return nbonds;
	}

	public int nBonds() {
		return nbonds;
	}

	public Bond getBond(int bondIndex) {
		return this.bonds[bondIndex];
	}

	public String getAtomLabel(int i) {
		return atoms[i].getLabel();
	}

	boolean hasHydrogen() {
		for (int i = natoms; i >= 1; i--) {
			if (an(i) == Atom.AN_H) {
				return true;
			}
		}

		return false;

	}

	int an(int i) {
		return atoms[i].an;
	}

	void AN(int i, int an) {
		this.atoms[i].an = an;
	}

	public Atom getAtom(int i) {
		return atoms[i];
	}

	double x(int i) {
		return atoms[i].x;
	}

	double xo(int i) {
		return atoms[i].xo;
	}

	double y(int i) {
		return atoms[i].y;
	}

	double yo(int i) {
		return atoms[i].yo;
	}

	double z(int i) {
		return atoms[i].z;
	}

	void XY(int i, double x, double y) {
		atoms[i].x = x;
		atoms[i].y = y;
	}

	void XY(Atom atom, double x, double y) {
		atom.x = x;
		atom.y = y;
	}

	void moveXY(int i, double x, double y) {
		atoms[i].moveXY(x, y);
	}

	public int q(int i) {
		return atoms[i].q;
	}

	void Q(int i, int charge) {
		atoms[i].q = charge;
	}

	public void incrQ(int i, int incr) {
		atoms[i].q += incr;
	}

	public int getIso(int i) {
		return atoms[i].iso;
	}

	public int getSumOfBondOrder(int i) {
		return atoms[i].sbo;
	}

	String atag(int i) {
		return atoms[i].atag;
	}

	int[] v(int i) {
		return atoms[i].v;
	}

	int nv(int i) {
		return atoms[i].nv;
	}

	void NV(int i, int nv) {
		atoms[i].nv = nv;
	}

	int incrNV(int i, int change) {
		return atoms[i].nv += change;
	}

	public void setPart(JMECore m, int part) {
		int newAtomIndexMap[] = new int[m.natoms + 1]; // cislovanie stare -> nove
		for (int i = 1; i <= m.natoms; i++) {
			if (m.atoms[i].partIndex != part)
				continue;
			createAtomFromOther(m.atoms[i]);

			newAtomIndexMap[i] = natoms;
		}
		for (int i = 1; i <= m.nbonds; i++) {
			Bond bond = m.bonds[i];
			if (bond.partIndex == part) {
				Bond newAddedBond = createAndAddBondFromOther(bond);
				newAddedBond.va = newAtomIndexMap[bond.va];
				newAddedBond.vb = newAtomIndexMap[bond.vb];
			}
		}
		this.setNeighborsFromBonds(); // update the adjencylist
	}

	Atom createAtomFromOther(Atom atomToDuplicate) {
		natoms++;
		if (natoms > atoms.length - 1) {
			int storage = atoms.length + 20;
			Atom newAtoms[] = new Atom[storage];
			System.arraycopy(atoms, 0, newAtoms, 0, atoms.length);
			atoms = newAtoms;
		}
		return this.atoms[natoms] = (atomToDuplicate == null ? new Atom() : (Atom) atomToDuplicate.deepCopy());
	}

	/**
	 * Create new bonds (standard Bond.SINGLE), allocating memory if necessary
	 * 
	 * @param otherBond
	 * @return new Bond
	 */
	Bond createAndAddBondFromOther(Bond otherBond) {
		nbonds++;
		if (nbonds > bonds.length - 1) {
			int storage = bonds.length + 10;
			Bond newBonds[] = new Bond[storage];
			System.arraycopy(bonds, 0, newBonds, 0, bonds.length);
			bonds = newBonds;
		}
		return bonds[nbonds] = (otherBond == null ? new Bond() : otherBond.deepCopy());
	}

	void cleanPolarBonds(boolean polarnitro) {
		// changing [X+]-[Y-] into X=Y (such as non-symmetric nitro bonds)
		// changing [X+]=[Y+] into X-Y (such as C+=C+ after fusing )
		// key polarnitro added since version 2002.05

		for (int i = 1; i <= nbonds; i++) {
			Bond bond = bonds[i];
			int atom1 = bond.va;
			int atom2 = bond.vb;
			int bondType = bond.bondType;

			if ((q(atom1) == 1 && q(atom2) == -1) || (q(atom1) == -1 && q(atom2) == 1)) {
				if (bondType == Bond.SINGLE || bondType == Bond.DOUBLE) { // tu nie E,Z

					// exceptions
					// not doing this by polarnitro set (since 2002.05)
					if (an(atom1) != Atom.AN_C && an(atom2) != Atom.AN_C && polarnitro)
						continue;
					// moved here 2011.10
					if (an(atom1) == Atom.AN_H || an(atom2) == Atom.AN_H)
						continue;
					if (an(atom1) == Atom.AN_B || an(atom2) == Atom.AN_B)
						continue;
					/*
					 * // not [H+]-[B-] in boranes (2005.02) if (an[atom1] == Atom.AN_H || an[atom2]
					 * == Atom.AN_H) continue; // // not [N+] [B-] 2011.10 if ((an[atom1]==Atom.AN_B
					 * && an[atom2]==JME_AN.N) || (an[atom1]==Atom.AN_N && an[atom2]==Atom.AN_B))
					 * continue;
					 */

					// not between halogenes
					if (an(atom1) == Atom.AN_F || an(atom1) == Atom.AN_CL || an(atom1) == Atom.AN_BR
							|| an(atom1) == Atom.AN_I || an(atom2) == Atom.AN_F || an(atom2) == Atom.AN_CL
							|| an(atom2) == Atom.AN_BR || an(atom2) == Atom.AN_I)
						continue; // 2005.10

					// System.err.println("CPB1 "+atom1+" "+atom2);
					Q(atom1, 0);
					Q(atom2, 0);
					bondType++;
					bond.bondType = bondType; // needed because valenceState computes sum bond orders
					setValenceState();
				}
			}

			// ??? nie, aspon 1 z nich musi byt C, (inak >N+=N+< meni na >NH+-NH+<)
			// if (q[atom1]==1 && q[atom2]==1 && (an[atom1]==h.C || an[atom2]==h.C)) {
			if (q(atom1) == 1 && q(atom2) == 1) {
				if (bondType == Bond.DOUBLE)
					bondType = Bond.SINGLE;
				else if (bondType == Bond.TRIPLE)
					bondType = Bond.DOUBLE;
				// System.err.println("CPB2");
				bond.bondType = bondType; // needed because valenceState computes sum bond orders
				setValenceState();
			}

			// this fixes rare WebME problems (Jun 09)
			// how this affects normal JME editing ?
			if (bondType == 4)
				bondType = 1;

			bond.bondType = bondType;
		}
	}

	// ----------------------------------------------------------------------------
	/**
	 * initialize the neighbor lists (adjency list) inside the atoms for a newly
	 * created mol?
	 */
	void setNeighborsFromBonds() {
		// fills helper fields v[][], nv[], ??? vzdy allocates memory

		for (int i = 1; i <= natoms; i++)
			atoms[i].nv = 0;
		// needed? I thinl hte array is always initialized to 0
		for (int i = 1; i <= nbonds; i++) {
			int atom1 = bonds[i].va;
			int atom2 = bonds[i].vb;
			addBothNeighbors(atom1, atom2);

		}
	}

	void addBothNeighbors(int at1, int at2) {
		atoms[at1].addNeighbor(at2);
		atoms[at2].addNeighbor(at1);
	}

	// ----------------------------------------------------------------------------
	void setValenceState() {
		for (int i = 1; i <= natoms; i++) {
			setAtomValenceState(i);
		}
	}

	/**
	 * determine atom.nh and atom.q from atom.sbo (sum of bond ordes)
	 * @param i
	 */
	void setAtomValenceState(int i) {
		Atom atom = atoms[i];
		int sbo = atom.sbo = sumBondOrders(i); // sum bond orders to nonhydrogen atoms
		if (sbo == -1) {
			atom.nh = 0;
			return;
		} // query bond
		switch (atom.an) {
		// added 2005.02
		case Atom.AN_H: // -[H+]- allowed (as in boranes)
			if (sbo == 2)
				Q(i, 1);
			// BB Sept 2016
			else if (sbo >= 1) {
				Q(i, 0);
			} else {
				// do nothing: keep the positive or the negative charge when sbo ==0
				// H+, H-
			}
			atom.nh = 0;
			break;

		// B changed in 2005.02
		case Atom.AN_B: // BH2+ BH3 BH4- BH5 BH6+ BH7++
			if (sbo == 3 || sbo == 5) {
				atom.nh = 0;
				Q(i, 0);
			} else if (sbo < 3) { // onlu here charge switch +/0 possible
				atom.nh = 3 - sbo - q(i);
			} else if (sbo == 4) {
				Q(i, -1);
				atom.nh = 0;
			} else if (sbo > 5) {
				Q(i, sbo - 5);
				atom.nh = 0;
			}
			break;
		case Atom.AN_C:
		case Atom.AN_SI: // Si since 2007.02
			if (sbo < 4) { // special treatment of carbocations and anions
				if (q(i) > 0)
					atom.nh = 2 - sbo + q(i);
				else if (q(i) < 0)
					atom.nh = 2 - sbo - q(i);
				else
					atom.nh = 4 - sbo;
			} else { // sbo >= 4
						// q[i] = sbo - 4;
				Q(i, sbo - 4);
				atom.nh = 4 - sbo + q(i);
			}
			break;
		case Atom.AN_N:
		case Atom.AN_P:
			if (sbo < 3)
				atom.nh = 3 - sbo + q(i);
			// else if (sbo == 3) {if(q[i] < 0) q[i] = 0; nh[i] = 3 - sbo + q[i];}
			// else if (sbo == 3) {q[i] = 0; nh[i] = 3 - sbo;} // 2002.12
			// 2004.04 >[NH+]- sbo 3 charge 1
			else if (sbo == 3) {
				if (q(i) < 0) {
					Q(i, 0);
					atom.nh = 0;
				} else if (q(i) > 0)
					atom.nh = q(i);
				else
					atom.nh = 3 - sbo; // 2002.12
			} else if (sbo == 4) {
				Q(i, 1);
				atom.nh = 0;
			} // nh=0 fix 2002.08
				// january 2002, pri sbo > 4, da vodiky na 0
				// 10.2005 XF6+ changed to XF6-
			else if (sbo == 6) {
				Q(i, -1);
				atom.nh = 0;
			} else {
				Q(i, sbo - 5);
				;
				atom.nh = 0;
			} // i.e. 5 && > 6 ???

			break;
		case Atom.AN_O: // -[O-] -O- =O -[O+]< >[O2+]< ...
			// if (sbo == 2 && q[i] < 0) q[i] = 0;
			// if (sbo == 2) q[i] = 0; // 2002.12
			if (sbo == 2) {
				if (q(i) < 0) {
					Q(i, 0);
					atom.nh = 0;
				} else if (q(i) > 0)
					atom.nh = q(i);
				else
					atom.nh = 2 - sbo;
			}
			if (sbo > 2)
				Q(i, sbo - 2);
			atom.nh = 2 - sbo + q(i);
			break;
		case Atom.AN_S:
		case Atom.AN_SE: // Se rozoznava kvoli aromaticite
			// multibond handling nh zmenene v 2002.12
			if (sbo < 2)
				atom.nh = 2 - sbo + q(i);
			// else if (sbo == 2) {if(q[i] < 0) q[i] = 0; nh[i] = 2 - sbo + q[i];}
			// else if (sbo == 2) {q[i] = 0; nh[i] = 2 - sbo;} // 2002.12
			else if (sbo == 2) {
				if (q(i) < 0) {
					Q(i, 0);
					atom.nh = 0;
				} else if (q(i) > 0)
					atom.nh = q(i);
				else
					atom.nh = 2 - sbo;
			} else if (sbo == 3) {
				// >S- ma byt S+, -S= ma byt SH
				if (nv(i) == 2) {
					Q(i, 0);
					atom.nh = 1;
				} else {
					Q(i, 1);
					atom.nh = 0;
				}
			} else if (sbo == 4) {
				Q(i, 0);
				atom.nh = 0;
			} else if (sbo == 5) {
				Q(i, 0);
				atom.nh = 1;
			} else {
				Q(i, sbo - 6);
				atom.nh = 0;
			}
			break;
		case Atom.AN_F:
		case Atom.AN_CL:
		case Atom.AN_BR:
		case Atom.AN_I:
			if (sbo >= 1)
				Q(i, sbo - 1);
			atom.nh = 1 - sbo + q(i);
			// potialto org, odteraz zmena kvoli superhalogenom
			if (sbo > 2) {
				Q(i, 0);
				atom.nh = 0;
			}
			break;
		case Atom.AN_R:
		case Atom.AN_X:
			atom.nh = 0;
			break;
		}

		// BB change metal 1 only if common situation
		// sbo cannot change, q might be a user request, nh is computed

		int maxMetalCharge = Atom.chargedMetalType(atom.an);
		if (maxMetalCharge > 0) {

			while (atom.q + atom.nh + sbo > maxMetalCharge) {
				if (atom.nh > 0) {
					atom.nh--;
					continue;
				}
				if (atom.q > 0) {
					atom.q--;

				} else
					break;
			}
		}

		if (atom.nh < 0)
			atom.nh = 0; // to be sure

		// 2006.09 removes tag if not stereo atom

// 2023.01 BH relativeStereo is not implemented; always false
//		if (jme != null && jme.relativeStereo && atag(i) != null && atag(i).length() > 0) {
//			boolean ok = false;
//			for (int j = 1; j <= nv(i); j++) {
//				int bond = bondIdentity(i, v(i)[j]);
//				if (i == bonds[bond].va && (bonds[bond].stereo == Bond.UP || bonds[bond].stereo == Bond.DOWN)) {
//					ok = true;
//					break;
//				}
//				if (i == bonds[bond].vb && (bonds[bond].stereo == Bond.XUP || bonds[bond].stereo == Bond.XDOWN)) {
//					ok = true;
//					break;
//				}
//			}
//			if (!ok)
//				atoms[i].atag = "";
//		}
	}

	// ----------------------------------------------------------------------------
	/**
	 * compute sum of bond orders k nonhydrogen atoms
	 * 
	 * @param atom
	 * @return
	 */
	int sumBondOrders(int atom) {

		int sbo = 0;
		for (int i = 1; i <= nv(atom); i++) {
			Bond bond = getBond(atom, v(atom)[i]);
			if (bond == null) {
				// assert (bond != null);
			}
			if (bond.isSingle())
				sbo += 1;
			else if (bond.isDouble())
				sbo += 2;
			else if (bond.bondType == Bond.TRIPLE)
				sbo += 3;
			else if (bond.bondType == Bond.QUERY)
				return -1; // query bond
			// else System.out.println("bond "+bond+" inconsistent info!"+bondType[bond]);
		}

		return sbo;
	}

	/**
	 * 
	 * @param atom1
	 * @param atom2
	 * @return null if no bond was found
	 */
	Bond getBond(int atom1, int atom2) {
		return this.bonds[getBondIndex(atom1, atom2)];
	}

	/**
	 * Find the bond index between two atoms, or 0 if no bond exists.
	 * 
	 * @param atom1
	 * @param atom2
	 * @return the bond index or 0
	 */
	int getBondIndex(int atom1, int atom2) {
		for (int i = 1; i <= nbonds; i++) {
			if (bonds[i].isAB(atom1, atom2)) {
				return i;
			}
		}
		return 0;
	}

	boolean isSingle(int bond) {
		return bonds[bond].isSingle();

	}

	boolean isDouble(int bond) {
		return this.bonds[bond].isDouble();
	}

	public int bondType(int bond) {
		return bonds[bond].bondType;
	}

	void complete(boolean computeValenceState) {
		setNeighborsFromBonds();
		setBondCenters();
		// March 2020: do valenceState() only if requested by the editor
		if (computeValenceState) {
			setValenceState(); // nh a upravi q
		}
	}

	protected void setBondCenters() {
		for (int b = 1; b <= nbonds; b++) {
			bonds[b].setBondCenter(atoms);
		}
	}

	/**
	 * minimum ring size of bond between two atoms
	 * 
	 * @param b : Bond instaqnce
	 * @return the smallest ring size if the bond belongs to a ring, otherwise 0
	 */
	public int minimumRingSize(Bond b) {
		int a1 = b.va;
		int a2 = b.vb;
		return minimumRingSize(a1, a2);
	}

	/**
	 * minimum ring size of bond between two atoms
	 * 
	 * @param a1 : atom index
	 * @param a2 : atom index
	 * @return the smallest ring size if the bond belongs to a ring, otherwise 0
	 */
	public int minimumRingSize(int a1, int a2) {
		/*
		 * Algorithm based on BFS search, increase the bond sphere around atom 1, one
		 * step at a time, until atom 2 is found
		 */

		// all arrays are initialized to 0
		int bondDist[] = new int[natoms + 1];
		int visited[] = new int[natoms + 1];
		int toVisit[] = new int[natoms + 1];

		// initialization
		bondDist[a1] = 1;
		toVisit[1] = a1;

		BOND_SPHERE_LOOP: while (true) {
			int visitCount = 0;
			for (int v = 1; toVisit[v] != 0; v++) {
				int at = toVisit[v];
				// assert (at != a2);

				// loop through all neigbors of at
				NEIGHBOR_LOOP: for (int n = 1; n <= nv(at); n++) {
					int neighbor = v(at)[n];

					// skip a2 because it is bounded directly to a1
					if (neighbor == a2 && at == a1)
						continue NEIGHBOR_LOOP;

					// if the neighbor has not been visited yet
					if (bondDist[neighbor] == 0) {
						bondDist[neighbor] = bondDist[at] + 1;

						// store the neighbor in the visited array such that it will be processed in the
						// next BOND_SPHERE_LOOP
						visitCount++;
						visited[visitCount] = neighbor;
					}
				}

				if (bondDist[a2] > 2) // distance with other atom a2 found: STOP here
					break BOND_SPHERE_LOOP;

			}

			if (visitCount == 0) // no ring found: STOP here
				break BOND_SPHERE_LOOP;

			visited[visitCount + 1] = 0; // mark end of VISIT_LOOP

			{ // swap the two arrays
				int tmp[] = toVisit;
				toVisit = visited;
				visited = tmp;
			}
		}

		return bondDist[a2];
	}

	/**
	 * Return the atom map as saved in the CT unless it is zero, in which case, if
	 * star mode and the atom has a specified background color, then returns 1.
	 * Returns 0 if no mapping.
	 * 
	 * @param atomIndex
	 * @return the map
	 */
	protected int findAtomMapForOutput(int atomIndex) {
		int map = 0;
		if (atomIndex > 0 && atomIndex <= this.nAtoms()) {
			Atom atom = this.atoms[atomIndex];
			map = atom.getMapOrMark(!parameters.mark);
		}

		return map;
	}

	public boolean haveQueryOrCoordBonds() {
		for (int b = 1; b <= nbonds; b++) {
			switch (bonds[b].bondType) {
			case Bond.QUERY:
			case Bond.COORDINATION:
				return true;
			}
		}
		return false;
	}

	// ----------------------------------------------------------------------------
	/**
	 * delete the atom and the associated bonds CHange nh, even if valenstate in JME
	 * is set to false
	 * 
	 * @param delatom
	 */
	public void deleteAtom(int delatom) {
		int i, j, atom1, atom2;

		// Actualizes bonds
		j = 0;
		int deltaSBO = 0; // BB:
		for (i = 1; i <= nbonds; i++) {
			Bond bondI = bonds[i];
			atom1 = bondI.va;
			atom2 = bondI.vb;
			if (atom1 != delatom && atom2 != delatom) {
				j++;
				Bond bondJ = bonds[j]; // BondJ is replacing BondI
				bondI.copyTo(bondJ);
				bondJ.va = atom1;
				if (atom1 > delatom)
					bondJ.va--;
				bondJ.vb = atom2;
				if (atom2 > delatom)
					bondJ.vb--;
			} else {
				deltaSBO += bondI.bondType;
			}
		}
		nbonds = j;

		for (i = delatom; i < natoms; i++) {
			this.atoms[i] = this.atoms[i + 1];
		}
		natoms--;
		if (natoms == 0) {
			return;
		}

		// updating nv[] and v[][]
		// updating also nh on neighbors (added in Oct 04 to fix canonisation)
		for (i = 1; i <= natoms; i++) {
			int k = 0;
			int ni = nv(i);
			for (j = 1; j <= ni; j++) {
				atom1 = v(i)[j];

				if (atom1 == delatom) {
					// atoms[i].nh++; // added nh[i]++ 10.04
					atoms[i].nh += deltaSBO; // BB
					continue;
				}
				if (atom1 > delatom)
					atom1--;
				v(i)[++k] = atom1;
			}
			NV(i, k);
		}

	}

	boolean deleteHydrogens(Parameters.HydrogenParams pars) {
		boolean changed = false;

		if (pars.removeHs == false) {
			return changed;
		}

		setNeighborsFromBonds(); // compute nv() adjancy list because itis needed below
		atom_loop: for (int i = natoms; i >= 1; i--) {
			int parent = v(i)[1];
			if (pars.removeOnlyCHs && an(parent) != Atom.AN_C) {
				continue;
			}
			if (an(i) == Atom.AN_H && nv(i) == 1 && q(i) == 0 && an(parent) != Atom.AN_H && an(parent) < Atom.AN_X
			// X R R1 R2
			) {

				if (pars.keepIsotopicHs && this.atoms[i].iso != 0) {
					continue atom_loop;
				}
				// do not delete H with atom map
				if (pars.keepMappedHs && this.atoms[i].isMapped()) {
					continue atom_loop;
				}

				int bi = getBondIndex(i, parent);
				if (bonds[bi].bondType == Bond.SINGLE) {
					if (!(pars.keepStereoHs && bonds[bi].stereo != 0)) {
						deleteAtom(i); // deleteAtom will recompute the nv's
						changed = true;
					}

				}
			}
		}

		return changed;
	}

	protected void info(String msg) {
		if (jmesl != null)
			jmesl.info(msg);
		else
			System.err.println(msg);
	}

//	int getFirstMappedAtom() {
//		for (int at = 1; at <= natoms; at++) {
//			if (this.atoms[at].isMapped()) {
//				return at;
//			}
//		}
//		return 0;
//	}



}
