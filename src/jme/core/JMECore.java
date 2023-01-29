package jme.core;

import jme.event.JMEStatusListener;

/**
 * A molecule class that has no GUI dependencies. Subclasses include JMEmol, JMESmiles, and JMEWriter.
 * 
 * It does not need to be abstract. 
 * 
 * 
 * @author hansonr
 *
 */
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

	public static final int NSTART_SIZE_ATOMS_BONDS = 10;
	public static final int MAX_BONDS_ON_ATOM = 6;

	final static Parameters DefaultParameters = new Parameters();


	/**
	 * just used for info(
	 */
	public JMEStatusListener jmesl;

	public Atom atoms[] = new Atom[NSTART_SIZE_ATOMS_BONDS];
	public Bond bonds[] = new Bond[NSTART_SIZE_ATOMS_BONDS];

	public int natoms = 0;
	public int nbonds = 0;
	
	public Boolean chiralFlag = Boolean.FALSE;

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
		chiralFlag = m.chiralFlag;
		
		atoms = new Atom[natoms + 1];
		for (int i = atoms.length; --i >= 0;) {
			if (m.atoms[i] != null) {
				atoms[i] = m.atoms[i].deepCopy();
			}
		}
		
		bonds = new Bond[nbonds + 1];
		for (int i = bonds.length; --i >= 0;) {
			if (m.bonds[i] != null) {
				bonds[i] = m.bonds[i].deepCopy();
			}
		}
	}

	public JMECore(JMEStatusListener jme, JMECore m, int part) {
		this(jme, m.parameters);
		m.computeMultiPartIndices(); // compute the partIndex
		int newn[] = new int[m.natoms + 1]; // cislovanie stare -> nove
		for (int i = 1, n = m.natoms; i <= n; i++) {
			if (atoms[i].partIndex == part)
				newn[i] = natoms;
		}
		for (int i = 1; i <= m.nbonds; i++) {
			int atom1 = m.bonds[i].va;
			int atom2 = m.bonds[i].vb;
			int p1 = atoms[atom1].partIndex;
			int p2 = atoms[atom2].partIndex;			
			if (p1 != part && p2 != part)
				continue;
			if (p1 != p2) { // musia byt obidve part
				System.err.println("MOL multipart inconsistency - report bug !");
				continue;
			}
			Bond newAddedBond = createAndAddBondFromOther(m.bonds[i]);
			newAddedBond.va = newn[atom1];
			newAddedBond.vb = newn[atom2];
		}
		this.setNeighborsFromBonds(); // update the adjencylist
	}

	public JMECore(JMEStatusListener jme, JMECore[] mols) {
		this(jme, (Parameters) null);
		if (mols.length > 0 && mols[0] != null)
			parameters = mols[0].parameters; 
		int nmols = mols.length;
		for (int i = 0; i < nmols; i++) {
			natoms += mols[i].natoms;
			nbonds += mols[i].nbonds;
			// if any is chiral, then the whole new molecule is chiral
			if (mols[i].getChiralFlag())
				this.setChiralFlag(true);
		}
		this.atoms = new Atom[natoms + 1];
		this.bonds = new Bond[nbonds + 1];

		int na = 0, nb = 0, nadd = 0;
		for (int i = 0; i < nmols; i++) {
			for (int j = 1, ni = mols[i].natoms; j <= ni; j++) {
				na++;
				this.atoms[na] = mols[i].atoms[j].deepCopy();
			}
			for (int j = 1, ni = mols[i].nbonds; j <= ni; j++) {
				nb++;
				bonds[nb] = mols[i].bonds[j].deepCopy();
				bonds[nb].va += nadd;
				bonds[nb].vb += nadd;
			}
			nadd = na;
		}
		setNeighborsFromBonds(); // update the adjencylist
	}

	public boolean isEmpty() {
		return (natoms == 0);
	}

	public void reset() {
		natoms = 0;
		nbonds = 0;
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
		return bonds[bondIndex];
	}

	public String getAtomLabel(int i) {
		return atoms[i].getLabel();
	}

	public boolean hasHydrogen() {
		for (int i = natoms; i >= 1; i--) {
			if (an(i) == Atom.AN_H) {
				return true;
			}
		}

		return false;

	}

	public int an(int i) {
		return atoms[i].an;
	}

	public void AN(int i, int an) {
		atoms[i].an = an;
	}

	public Atom getAtom(int i) {
		return atoms[i];
	}

	public double x(int i) {
		return atoms[i].x;
	}

	public double y(int i) {
		return atoms[i].y;
	}

	public double z(int i) {
		return atoms[i].z;
	}

	public void XY(int i, double x, double y) {
		atoms[i].x = x;
		atoms[i].y = y;
	}

	public static void XY(Atom atom, double x, double y) {
		atom.x = x;
		atom.y = y;
	}

	public void moveXY(int i, double x, double y) {
		atoms[i].moveXY(x, y);
	}

	public int q(int i) {
		return atoms[i].q;
	}

	public void Q(int i, int charge) {
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

	public String atag(int i) {
		return atoms[i].atag;
	}

	public int[] v(int i) {
		return atoms[i].v;
	}

	public int nv(int i) {
		return atoms[i].nv;
	}

	public void NV(int i, int nv) {
		atoms[i].nv = nv;
	}

	public int incrNV(int i, int change) {
		return atoms[i].nv += change;
	}

	/**
	 * @return the chiralFlag
	 */
	public Boolean getChiralFlag() {
		return chiralFlag;
	}

	public boolean canBeChiral() {
		for (int i = 1; i <= nbonds; i++) {
			if (bonds[i].bondType == Bond.SINGLE && bonds[i].stereo > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param chiralFlag the chiralFlag to set return true if my chiral flag has
	 *                   been changed
	 */
	public boolean setChiralFlag(Boolean chiralFlag) {
		if (this.chiralFlag != chiralFlag) {
			this.chiralFlag = chiralFlag;
			return true;
		}

		return false;
	}

	public void setPart(JMECore m, int part) {
		chiralFlag = m.chiralFlag;
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
		setNeighborsFromBonds(); // update the adjencylist
	}

	/**
	 * This method can be overridden to add more features.
	 * 
	 * @param atomToDuplicate
	 * @return
	 */
	protected Atom createAtomFromOther(Atom atomToDuplicate) {
		natoms++;
		if (natoms > atoms.length - 1) {
			int storage = atoms.length + 20;
			Atom newAtoms[] = new Atom[storage];
			System.arraycopy(atoms, 0, newAtoms, 0, atoms.length);
			atoms = newAtoms;
		}
		return atoms[natoms] = (atomToDuplicate == null ? new Atom() : (Atom) atomToDuplicate.deepCopy());
	}

	protected void cleanPolarBonds(boolean polarnitro) {
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
	protected void setNeighborsFromBonds() {
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

	public void addBothNeighbors(int at1, int at2) {
		atoms[at1].addNeighbor(at2);
		atoms[at2].addNeighbor(at1);
	}

	// ----------------------------------------------------------------------------
	public void setValenceState() {
		for (int i = 1; i <= natoms; i++) {
			setAtomValenceState(i);
		}
	}

	/**
	 * determine atom.nh and atom.q from atom.sbo (sum of bond ordes)
	 * @param i
	 */
	public void setAtomValenceState(int i) {
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
				if (atoms[i].nv == 2) {
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
//			for (int j = 1; j <= atoms[i].nv; j++) {
//				int bond = bondIdentity(i, a.v[j]);
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
	public int sumBondOrders(int atom) {

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
	public Bond getBond(int atom1, int atom2) {
		return bonds[getBondIndex(atom1, atom2)];
	}

	/**
	 * Find the bond index between two atoms, or 0 if no bond exists.
	 * 
	 * @param atom1
	 * @param atom2
	 * @return the bond index or 0
	 */
	public int getBondIndex(int atom1, int atom2) {
		for (int i = 1; i <= nbonds; i++) {
			if (bonds[i].isAB(atom1, atom2)) {
				return i;
			}
		}
		return 0;
	}

	public boolean isSingle(int bond) {
		return bonds[bond].isSingle();

	}

	public boolean isDouble(int bond) {
		return bonds[bond].isDouble();
	}

	public int bondType(int bond) {
		return bonds[bond].bondType;
	}

	public void complete(boolean computeValenceState) {
		setNeighborsFromBonds();
		setBondCenters();
		// March 2020: do valenceState() only if requested by the editor
		if (computeValenceState) {
			setValenceState(); // nh a upravi q
		}
	}

	public void setBondCenters() {
		for (int b = 1; b <= nbonds; b++) {
			setBondCenter(bonds[b]);
		}
	}

	public void setBondCenter(Bond b) {
		b.bondCenterX = (atoms[b.va].x + atoms[b.vb].x) / 2;
		b.bondCenterY = (atoms[b.va].y + atoms[b.vb].y) / 2;
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
	 * @param i
	 * @return the map
	 */
	public int findAtomMapForOutput(int i) {
		return (i > 0 && i <= natoms ? atoms[i].getMapOrMark(!parameters.mark) : 0);
	}

	/**
	 * 
	 * @return 0 if not found
	 */
	public int findFirstMappdedOrMarkedAtom() {
		for (int i = 1; i <= natoms; i++) {
			Atom at = atoms[i];
			if (at.isMappedOrMarked()) {
				return i;
			}
		}

		return 0;

	}

	public boolean hasMappedOrMarkedAtom() {
		return findFirstMappdedOrMarkedAtom() > 0;
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
	public boolean deleteHydrogens(Parameters.HydrogenParams pars) {
		boolean changed = false;

		if (pars.removeHs == false) {
			return changed;
		}

		setNeighborsFromBonds(); // compute nv() adjancy list because itis needed below
		atom_loop: for (int i = natoms; i >= 1; i--) {
			Atom a = atoms[i];
			Atom parent = atoms[a.v[1]];
			if (pars.removeOnlyCHs && parent.an != Atom.AN_C) {
				continue;
			}
			if (a.an == Atom.AN_H && a.nv == 1 && a.q == 0 && parent.an != Atom.AN_H && parent.an < Atom.AN_X
			// X R R1 R2
			) {

				if (pars.keepIsotopicHs && a.iso != 0) {
					continue atom_loop;
				}
				// do not delete H with atom map
				if (pars.keepMappedHs && a.isMapped()) {
					continue atom_loop;
				}

				Bond b = getBond(i, a.v[1]);
				if (b.bondType == Bond.SINGLE) {
					if (!(pars.keepStereoHs && b.stereo != 0)) {
						deleteAtom(i); // deleteAtom will recompute the nv's
						changed = true;
					}

				}
			}
		}

		return changed;
	}

	public void info(String msg) {
		if (jmesl != null)
			jmesl.info(msg);
		else
			System.err.println(msg);
	}

//	int getFirstMappedAtom() {
//		for (int at = 1; at <= natoms; at++) {
//			if (atoms[at].isMapped()) {
//				return at;
//			}
//		}
//		return 0;
//	}


	public boolean hasAromaticBondType() {
		if (nbonds < 1 ) {
			return false;
		}
		for(int b = 1; b <= nbonds; b++) {
			Bond bond = bonds[b];
			if( bond.bondType == 4 || bond.bondType == Bond.AROMATIC || bond.bondType == Bond.QUERY) { //TODO: molfile reader should use AROMATIC?
				return true;
			}
		}
		return false;
	}

	public double distance(int atom1, int atom2) {
		double dx = atoms[atom2].x - atoms[atom1].x;
		double dy = atoms[atom2].y - atoms[atom1].y;
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public double bondDistance(int i) {
		return distance(bonds[i].va, bonds[i].vb);
	}

	public static double squareEuclideanDist(double x1, double y1, double x2, double y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		return dx * dx + dy * dy;
	}

	public static double dotProduct(double x1, double y1, double x2, double y2) {
		return x1 * x2 + y1 * y2;
	}

	protected final double[] cosSin = new double[2];
	// static constants
	public static final double RBOND = 25;

	public void setCosSin(int atom1, int atom2) {
		double dx = x(atom2) - x(atom1);
		double dy = y(atom2) - y(atom1);
		double rx = Math.sqrt(dx * dx + dy * dy);
		if (rx < 0.001)
			rx = 0.001;
		cosSin[0] = dx / rx;
		cosSin[1] = dy / rx;
	}


	protected void rotate(double movex, double centerx, double centery) {
		if (natoms == 0)
			return; // bbox is null if the molecule has no atoms

		// get original position
		moveXY(-centerx, -centery);

		// rotation
		double sinu = Math.sin(movex * Math.PI / 180.);
		double cosu = Math.cos(movex * Math.PI / 180.);
		for (int i = 1; i <= natoms; i++) {
			double xx = x(i) * cosu + y(i) * sinu;
			double yy = -x(i) * sinu + y(i) * cosu;
			atoms[i].x = xx;
			atoms[i].y = yy;
		}
		// moving to original position
		moveXY(centerx, centery);
	}

	/**
	 * Move all atoms, update the bond centers
	 * 
	 * @param dx
	 * @param dy
	 */
	public void moveXY(double dx, double dy) {
		for (int at = 1; at <= natoms; at++) {
			atoms[at].moveXY(dx, dy);
		}
		setBondCenters();
	}

	/**
	 * Compute the atom and bond partIndex
	 * 
	 * @return the number of parts (0 if there are no atoms)
	 */
	public int computeMultiPartIndices() {

		return computeMultiPartIndices(0);
	}

	public int computeMultiPartIndices(int bondToBeIgnored) {

		int nparts = 0;

		// reset the partIndex of atoms and bonds
		for (int at = 1; at <= natoms; at++) {
			atoms[at].partIndex = 0;
		}

		for (int b = 1; b <= nbonds; b++) {
			bonds[b].partIndex = 0;
		}

		while (true) {
			boolean newPartAssignedToAtom = false;
			// find first atom that has no partIndex assigned
			for (int at = 1; at <= natoms; at++) {
				Atom atom = atoms[at];
				if (atom.partIndex == 0) {
					atom.partIndex = ++nparts;
					newPartAssignedToAtom = true;
					break;
				}
			}
			if (!newPartAssignedToAtom)
				break;

			while (newPartAssignedToAtom) {
				newPartAssignedToAtom = false;

				// loop over the bonds - give the same part index to both atoms of the bond
				for (int b = 1; b <= nbonds; b++) {
					if (b == bondToBeIgnored)
						continue;
					Bond bond = bonds[b];
					if (bond.partIndex > 0) {
						continue;
					}
					Atom atom1 = atoms[bond.va];
					Atom atom2 = atoms[bond.vb];
					if (atom1.partIndex != atom2.partIndex) { // only one of the two is 0
						bond.partIndex = atom1.partIndex = atom2.partIndex = nparts;
						newPartAssignedToAtom = true;
					} else {
						bond.partIndex = atom1.partIndex; // this is a ring closure bond if atom1.partIndex > 0
					}
				}
			}
		}

		return nparts;
	}

	public Box computeCoordinate2DboundingBox() {
		Box bbox = null;

		if (natoms == 0)
			return bbox;

		double minx = Double.MAX_VALUE, maxx = Double.MIN_VALUE, miny = Double.MAX_VALUE, maxy = Double.MIN_VALUE;

		for (int i = 1; i <= natoms; i++) {
			double x = x(i);
			double y = y(i);

			if (x < minx)
				minx = x;

			maxx = Math.max(x, maxx);

			if (y < miny)
				miny = y;

			maxy = Math.max(y, maxy);
		}

		bbox = new Box();
		bbox.x = minx;
		bbox.y = miny;

		bbox.width = maxx - minx;
		bbox.height = maxy - miny;

		return bbox;
	}

	/**
	 * Determine the best position to add a newly bonded atom to an existing atom in
	 * the molecule.
	 * 
	 * @param pt
	 * @param rbond
	 * @param newPoint
	 */
	public void getNewPoint(int pt, double rbond, double[] newPoint) {
		// adding new atom to source with two bonds already
		// called when creating new bond or ring center
		int atom1 = v(pt)[1];
		int atom2 = v(pt)[2];
		double dx = x(atom2) - x(atom1);
		double dy = -(y(atom2) - y(atom1));
		double rx = Math.sqrt(dx * dx + dy * dy);
		if (rx < 0.001)
			rx = 0.001;
		double sina = dy / rx;
		double cosa = dx / rx;
		// vzd. act_a od priamky atom1-atom2
		double vzd = Math.abs((y(pt) - y(atom1)) * cosa + (x(pt) - x(atom1)) * sina);
		if (vzd < 1.0) { // perpendicular to linear moiety
			dx = x(pt) - x(atom1);
			dy = y(pt) - y(atom1);
			rx = Math.sqrt(dx * dx + dy * dy);
			if (rx < 0.001)
				rx = 0.001;
			double xx = rx;
			double yy = rbond;
			sina = dy / rx;
			cosa = dx / rx;
			newPoint[0] = x(atom1) + xx * cosa - yy * sina;
			newPoint[1] = y(atom1) + yy * cosa + xx * sina;
		} else { // da do stredu tych 2 vazieb a oproti nim
			double xpoint = (x(atom1) + x(atom2)) / 2.;
			double ypoint = (y(atom1) + y(atom2)) / 2.;
			dx = x(pt) - xpoint;
			dy = y(pt) - ypoint;
			rx = Math.sqrt(dx * dx + dy * dy);
			if (rx < 0.001)
				rx = 0.001;
			newPoint[0] = x(pt) + rbond * dx / rx;
			newPoint[1] = y(pt) + rbond * dy / rx;
		}
	}

	/**
	 * Create new bonds (standard Bond.SINGLE), allocating memory if necessary
	 * 
	 * @param otherBond
	 * @return new Bond
	 */
	public Bond createAndAddBondFromOther(Bond otherBond) {
		nbonds++;
		if (nbonds > bonds.length - 1) {
			int storage = bonds.length + 10;
			Bond newBonds[] = new Bond[storage];
			System.arraycopy(bonds, 0, newBonds, 0, bonds.length);
			bonds = newBonds;
		}
		return bonds[nbonds] = (otherBond == null ? new Bond() : otherBond.deepCopy());
	}

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
			atoms[i] = atoms[i + 1];
		}
		natoms--;
		if (natoms == 0) {
			return;
		}

		// updating nv[] and v[][]
		// updating also nh on neighbors (added in Oct 04 to fix canonisation)
		for (i = 1; i <= natoms; i++) {
			Atom a = atoms[i];
			int k = 0;
			int ni = a.nv;
			for (j = 1; j <= ni; j++) {
				atom1 = a.v[j];

				if (atom1 == delatom) {
					// a.nh++; // added nh[i]++ 10.04
					a.nh += deltaSBO; // BB
					continue;
				}
				if (atom1 > delatom)
					atom1--;
				a.v[++k] = atom1;
			}
			NV(i, k);
		}

	}

	public Atom createAtom() {
		return createAtomFromOther(null);
	}

	/**
	 * Used when reding input file and when using the X button in the GUI.
	 * 
	 * @param atom
	 * @param symbol
	 */
	public void setAtom(int atom, String symbol) {
		// volane pri spracovavani mol alebo jme z createAtom
		// alebo pri kliknuti na X atom x x boxu
		// aj po query

		// if in [] forces this valence state as AN_X, 2004.01
		if (symbol.startsWith("[") && symbol.endsWith("]")) {
			symbol = symbol.substring(1, symbol.length() - 1);
			AN(atom, Atom.AN_X);
			atoms[atom].label= symbol;
			atoms[atom].nh = 0;
			return;
		}

		if (symbol.length() < 1)
			System.err.println("Error - null atom !");

		// BB: isotopic : 13C
		// symbol = this.atoms[atom].parseAtomSymbolIsotop(symbol);
		symbol = this.atoms[atom].parseAtomicSymbolPatternIsotopMappAndCharge(symbol, this.parameters);

		/*
		 * iso[atom] = 0; Pattern p = Pattern.compile("^(\\d+)([A-Z][a-z]?)(\\b.*)");
		 * Matcher m = p.matcher(symbol); if(m.find()) { int isomass =
		 * Integer.parseInt(m.group(1)); String element = m.group(2);
		 * if(AtomicElements.isKnown(element, isomass)) { iso[atom] = isomass; symbol =
		 * element + m.group(3); //add the rest of the match to the symbol
		 * 
		 * }
		 * 
		 * }
		 */

		// ak je tam , alebo ; ide o query aj ked zacina so znamym symbolom
		boolean isQuery = false;
		if (symbol.indexOf(",") > -1)
			isQuery = true;
		if (symbol.indexOf(";") > -1)
			isQuery = true;
		if (symbol.indexOf("#") > -1)
			isQuery = true;
		if (symbol.indexOf("!") > -1)
			isQuery = true;
		int hpos = symbol.indexOf("H");
		atomProcessing: {
			if (isQuery) {
				atoms[atom].label = symbol;
				AN(atom, Atom.AN_X);
				atoms[atom].nh = 0;
				break atomProcessing;
			}
			String as = symbol;
			if (hpos > 0)
				as = symbol.substring(0, hpos);
			AN(atom, Atom.checkAtomicSymbol(as)); // as & symbol su rozdielne/
			if (an(atom) == Atom.AN_X)
				atoms[atom].label= as;

			symbol += " "; // aby netrebalo stale checkovat koniec

			// number of hydrogens (moze but aj H0)
			int nhs = 0;
			if (hpos > 0) { // > 0, nie -1
				nhs = 1;
				char c = symbol.charAt(++hpos);
				if (c >= '0' && c <= '9')
					nhs = c - '0';
			}
			if (an(atom) == Atom.AN_X) {
				atoms[atom].nh = nhs;
			}
		}
	}
	
	/**
	 * Create and add a new bond to my bond list
	 * 
	 * @param atom1
	 * @param atom2
	 * @param bondType
	 * @return the new Bond
	 */
	public Bond createAndAddNewBond(int at1, int at2, int bondType) {
		// assert (at1 != at2);
		Bond newBond = createAndAddBondFromOther(null); // the new bond index is this.nbonds
		addBothNeighbors(at1, at2); // set up the adjacency lists
		newBond.va = at1;
		newBond.vb = at2;
		// compute bond centers
		setBondCenter(newBond);
		newBond.bondType = bondType;
		return newBond;
	}


	/**
	 * 
	 * @param bondType     the bond type being added
	 * @param atom         to be added to
	 * @param up           flip bond to other side - only possible if the touched
	 *                     atom has 1 valence values for flip: 0,-1 or 1
	 * @param forceLinear  if forcing this to be linear while building
	 * @param addingDouble if this will be a double bond, as it will be linear, in
	 *                     that case, if the prev bond is double.
	 * @param limit        the limit for counting a touch
	 * @return null if too many bonds already, true if the up was the parameter used
	 */
	public Boolean addBondToAtom(int bondType, int ia, int up, boolean forceLinear, double limit) {
		boolean upWasUsed = false;
		Atom atom = atoms[ia];
		if (atom.nv > 5) {
//			info("Are you trying to draw an hedgehog ?", JME.LA_FAILED);
			return null;
		}
		createAtomFromOther(null);
		switch (atom.nv) {
		case 0:
			XY(natoms, atom.x + RBOND * .866, atom.y + RBOND * .5);
			break;
		case 1:
			int ia1 = atom.v[1];
			Atom atom1 = atoms[ia1];
			Atom atom3 = (atom1.nv != 2 ? null : atom1.v[1] == ia ? atoms[atom1.v[2]] : atoms[atom1.v[1]]);
			double dx = atom.x - atom1.x;
			double dy = atom.y - atom1.y;
			double rx = Math.sqrt(dx * dx + dy * dy);
			if (rx < 0.001)
				rx = 0.001;
			double sina = dy / rx;
			double cosa = dx / rx;
			double xx;
			double yy;
			// checking for allene -N=C=S, X#C-, etc
			// chain je ako linear !
			Bond b = getBond(ia, ia1);
			if (forceLinear	
					|| bondType == Bond.TRIPLE || b.bondType == Bond.TRIPLE
					|| bondType == Bond.DOUBLE && b.bondType == Bond.DOUBLE) {
				xx = rx + RBOND;
				yy = 0.;
			} else {
				xx = rx + RBOND * Math.cos(Math.PI / 3.);
				yy = RBOND * Math.sin(Math.PI / 3.);
			}
			if (atom3 != null) // to keep growing chain linear
				if (((atom3.y - atom1.y) * cosa - (atom3.x - atom1.x) * sina) > 0)
					yy = -yy;
			// flip bond to other site
			if (up > 0 && yy < 0)
				yy = -yy;
			else if (up < 0 && yy > 0)
				yy = -yy;
			XY(natoms, atom1.x + xx * cosa - yy * sina, atom1.y + yy * cosa + xx * sina);
			upWasUsed = true;
			break;
		case 2:
			double[] newPoint = new double[2];
			getNewPoint(ia, RBOND, newPoint);
			XY(natoms, newPoint[0], newPoint[1]);
			break;
		case 3:
		case 4:
		case 5:
			// postupne skusa linearne predlzenie vsetkych vazieb z act_a
			for (int i = 1; i <= atom.nv; i++) {
				atom1 = atoms[atom.v[i]];
				dx = atom.x - atom1.x;
				dy = atom.y - atom1.y;
				rx = Math.sqrt(dx * dx + dy * dy);
				if (rx < 0.001)
					rx = 0.001;
				XY(natoms, atom.x + RBOND * dx / rx, atom.y + RBOND * dy / rx);
				// teraz testuje ci sa nedotyka
				if (i == atom.nv || checkTouchToAtom(natoms, 1, natoms, limit, true) == 0)
					break;
			}
			break;
		}
		createAndAddNewBond(ia, natoms, bondType);
		return Boolean.valueOf(upWasUsed);
 	}

	/**
	 * checking touch of atom with other atoms in my self selected by the range
	 * [first, last]
	 * 
	 * @param atom
	 * @param first    atom for the atom loop
	 * @param lastAtom index for the atom loop
	 * @return the atom index that is the closest among the too close ones if close
	 *         enough, or 0 if no atoms is close enough
	 */
	protected int checkTouchToAtom(int ia, int firstAtom, int lastAtom, double limit, boolean justOne) {
		// checking touch of atom with other atoms
		double dx, dy, rx;
		double min = limit + 1;
		int touch = 0;
		Atom atom = atoms[ia];
		for (int i = firstAtom; i <= lastAtom; i++) {
			if (ia == i)
				continue;
			// compute squared distance
			dx = atom.x - atoms[i].x;
			dy = atom.y - atoms[i].y;
			rx = dx * dx + dy * dy;
			if (rx < limit)
				if (rx < min) {
					min = rx;
					touch = i;
					if (justOne)
						return i;
				}
		}
		return touch;
	}

	public boolean hasCloseContactWith(JMECore other, double minAtomDist) {

		for (int a1 = 1, n = nAtoms(); a1 <= n; a1++) {
			Atom at1 = getAtom(a1);
			for (int a2 = 1, n2 = other.nAtoms(); a2 <= n2; a2++) {
				Atom at2 = other.getAtom(a2);
				if (at1.hasCloseContactWith(at2, minAtomDist)) {
					return true;
				}
			}
		}
		return false;
	}

	public int testAtomAndBondTouch(double xx, double yy, boolean ignoreAtoms, boolean ignoreBonds, double[] retMin) {
		int i, found = 0;
		double rx;
		double min = retMin[0];
		if (!ignoreBonds) {
			for (i = 1; i <= nbonds; i++) {
				Bond b = this.bonds[i];
				rx = squareEuclideanDist(xx, yy, b.bondCenterX, b.bondCenterY);
				if (rx < min) {
					min = rx;
					found = -i;
//				}
// BH -- why would one check ALL bonds, and then not set found?
//				if (found != 0) {
					// here if ANY atom is found??
					// One problem : if the two atoms are very close, it is impossible to select the
					// bond
					// try with two more positions along the bond axis, at 1/3 and 2/3 of the
					// distance between the two atoms
					double ax = atoms[b.va].x;
					double ay = atoms[b.va].y;
					double vx = atoms[b.vb].x - ax;
					double vy = atoms[b.vb].y - ay;

					for (int third = 1; third <= 2; third++) {
						double x3 = ax + third * vx / 3;
						double y3 = ay + third * vy / 3;
						rx = squareEuclideanDist(xx, yy, x3, y3);
						if (rx < min) {
							min = rx;
						}
					}
				}
			}
		}

		if (!ignoreAtoms) {
			// Do the same for the atoms
			// min may be smaller for an atom even if a bond was found
			for (i = 1; i <= natoms; i++) {
				rx = squareEuclideanDist(xx, yy, x(i), y(i));
				if (rx < min) {
					min = rx;
					found = i;
				}
			}
		}		
		// BB: handle case for which the bond length is larger than usual
		// TODO: complicated, there must be a way to simplify the trigonometry
		if (found == 0 && !ignoreBonds) {
			for (i = 1; i <= nbonds; i++) {
				int at1 = this.bonds[i].va;
				int at2 = this.bonds[i].vb;
				double at1X = x(at1);
				double at1Y = y(at1);
				double at2X = x(at2);
				double at2Y = y(at2);

				// work with two vectors:
				// at1->xx, at1->at2

				// new coordinate reference: at1
				at2X -= at1X;
				at2Y -= at1Y;
				double xx2 = xx - at1X;
				double yy2 = yy - at1Y;

				double sqBondLength = at2X * at2X + at2Y * at2Y;
				double sqDistToAtom1 = xx2 * xx2 + yy2 * yy2;
				double sqDistToAtom2 = squareEuclideanDist(xx2, yy2, at2X, at2Y);
				// if too far away
				if (sqDistToAtom1 + sqDistToAtom2 > sqBondLength + min) {
					continue;
				}

				double dp = dotProduct(xx2, yy2, at2X, at2Y);
				if (dp < 0) {
					continue; // mouse is on the other side of the bond
				}

				// projection of the xx2 vector on the at2 vector gives the cos angle
				sqBondLength = Math.sqrt(sqBondLength);
				sqDistToAtom1 = Math.sqrt(sqDistToAtom1);

				double cos = dp / (sqBondLength * sqDistToAtom1);
				if (cos >= 1) {
					continue;
				}
				double otherAngle = Math.PI * 0.5 - Math.acos(cos); // triangle: sum of all angles is Pi: "cos" +
																	// otherAngle + 90 deg
				double dist = sqDistToAtom1 * Math.cos(otherAngle);
				dist *= dist; // min is the square of the minmum distance
				if (dist < min) {
					found = i * -1;
					min = dist;
					//no break: there could be more than one long bond connected to the
					// same atom
				}

			}
		}
		retMin[0] = min;
		return found;
	}

	public void scaleXY(double scale) {
		if (scale > 0) {
			for (int at = 1; at <= natoms; at++) {
				atoms[at].scaleXY(scale);
			}
			setBondCenters(); // needed for mouse over
		}
	}

	public Atom createAtom(String symbol) {
		// parses SMILES-like atomic label and set atom parameters
		Atom atom = createAtom(); // sets natoms
		setAtom(natoms, symbol);
		return atom;
	}

	protected void setAtomHydrogenCount(int atom, int nh) {
		Atom a = atoms[atom];
		if (a.an == Atom.AN_X) {
			a.label += "H";
			if (nh > 1)
				a.label += nh;
		}
	}

	public int getHydrogenCount(int i) {
		return atoms[i].nh;
	}

	public int getCharge(int i) {
		return atoms[i].q;
	}

	public int getMaxAtomMap() {
		if (this.natoms == 0)
			return 0;
		int max = -999999;
		for (int at = 1; at <= this.natoms; at++) {
			int map = this.atoms[at].getMap();
			if (map > max)
				max = map;

		}
		return max;
	}

	public boolean resetAtomMaps() {
		boolean hasChanged = false;
		for (int at = 1; at <= this.natoms; at++) {
			hasChanged = atoms[at].resetMap() || hasChanged;
		}
		return hasChanged;
	}

	public boolean has2Dcoordinates() {
		if (natoms == 0) {
			return true;
		}
		if (has3Dcoordinates()) {
			return false;
		}
		if (natoms == 2) {
			return atoms[1].x != atoms[2].x || atoms[1].y != atoms[2].y;
		}
		for (int i = 1; i <= this.natoms; i++) {
			if (atoms[i].x != 0 || atoms[i].y != 0) {
				return true;
			}
		}
		return false;
	}

	// Feb 2020
	/**
	 * If at least one z value != 0 TODO: all atoms have the same z value => 2D
	 * 
	 * @return
	 */
	public boolean has3Dcoordinates() {
		if (this.nAtoms() <= 1) {
			return true;
		}
		for (int at = 1; at <= this.natoms; at++) {
			// if (z(at) != 0) {
			if (Math.abs(z(at)) > 0.001) {
				// CHEMBL CHEMBL3752999 patch: z != 0
				// 4.8805 -14.0706 0.0001 C 0 0
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return the reference bond length that was used for scaling or 0 if scaling
	 *         was not possible
	 */
	public double internalBondLengthScaling() {
		// proper scaling (to RBOND)
		double sumlen = 0, scale = 0;
		double max = 0;
		double min = Double.MAX_VALUE;
		double refBondLength = RBOND;

		for (int i = 1; i <= nbonds; i++) {
			Bond b = bonds[i];
			double d = distance(b.va, b.vb);
			sumlen += d;
			if (d > max)
				max = d;
			if (d < min)
				min = d;
		}

		if (sumlen == 0) 
			return 0;
		if (nbonds > 0) {
			double average = sumlen / nbonds;
			// most of the time there is no significant difference between min and max
			// a few bonds are much longer -- min
			// a few bonds are much shorter -- max
			scale = (average - min < max - average ? min : max);
			scale = refBondLength / scale;
		} else if (natoms > 1) { // disconnected structure(s)
			scale = 3 * refBondLength / distance(1, 2);
		}
		this.scaleXY(scale);
		this.setBondCenters(); // BB added June 2020
		return refBondLength;
	}

	public double centerX() {
		double sum = 0;
		for (int i = 1; i <= natoms; i++) {
			sum += x(i);
		}
		return (natoms > 0 ? sum / natoms : 0);
	}

	public double centerY() {
		double sum = 0;
		for (int i = 1; i <= natoms; i++) {
			sum += y(i);
		}
		return (natoms > 0 ? sum / natoms : 0);
	}

	public double closestAtomDistance(double xx, double yy) {
		double min = Double.MAX_VALUE;

		for (int i = 1; i <= natoms; i++) {
			double rx = squareEuclideanDist(xx, yy, x(i), y(i));
			if (rx < min) {
				min = rx;
			}
		}
		return Math.sqrt(min);
	}

	/**
	 * Checking for too-close atoms. This can be a problem for CDX or CDXML files with fragments.
	 * 
	 * @return
	 */
	public boolean checkNeedsCleaning() {
		// have atoms; check for very close
		for (int i = natoms + 1; --i >= 1;) {
			double x = atoms[i].x;
			double y = atoms[i].y;
			for (int j = i; --j >= 1;) {
				double x2 = atoms[j].x;
				double y2 = atoms[j].y;
				if (Math.abs(x - x2) + Math.abs(y - y2) < 2) {
					return true;
				}
			}
		}
		return false;
	}

	public int setAtomMapFromInput(int atomIndex, int map) {
		if (atomIndex > 0 && atomIndex <= this.nAtoms()) {
			Atom atom = this.atoms[atomIndex];

			atom.setMapOrMark(map, !parameters.mark);
		}

		return map;
	}

	/**
	 * 
	 * @param delbond
	 * @param deleteLonelyAtoms
	 */
	public void deleteBond(int delbond, boolean deleteLonelyAtoms) {
		// deletes bond between atoms delat1 and delat2

		int a1 = bonds[delbond].va;
		Atom atom1 = atoms[a1];
		int a2 = bonds[delbond].vb;
		Atom atom2 = atoms[a2];

		for (int i = delbond; i < nbonds; i++) {
			this.bonds[i] = this.bonds[i + 1];
		}
		nbonds--;

		// updating nv[] and v[][]
		int k = 0;
		int ni = atom1.nv;
		for (int i = 1; i <= ni; i++)
			if (atom1.v[i] != a2)
				atom1.v[++k] = atom1.v[i];
		atom1.nv = k;
		k = 0;
		ni = atom2.nv;
		for (int i = 1; i <= ni; i++)
			if (atom2.v[i] != a1)
				atom2.v[++k] = atom2.v[i];
		atom2.nv = k;
		
		if (deleteLonelyAtoms) {
			// deleting lonely atom(s)
			if (a1 < a2) {
				k = a1;
				a1 = a2;
				a2 = k;
			}
			if (atoms[a1].nv == 0)
				deleteAtom(a1);
			if (atoms[a2].nv == 0)
				deleteAtom(a2);
		}
	}

	/**
	 * Finish up after creation from JME string, MOL data, or Jmol adapter
	 */
	public void finalizeMolecule() {
		setNeighborsFromBonds();
		deleteHydrogens(parameters.hydrogenParams);
		complete(parameters.computeValenceState); // este raz, zachytit zmeny
		if (parameters.internalBondScalingForInput) {
			internalBondLengthScaling();
		}
	}

}
