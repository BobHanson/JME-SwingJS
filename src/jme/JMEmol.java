package jme;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import jme.JMEUtil.GWT;

// --------------------------------------------------------------------------
public class JMEmol extends Graphical2DObject {

	public static class ReactionRole {
		public final static int NOROLE = 0;
		public final static int REACTANT = 1;
		public final static int AGENT = 2;
		public final static int PRODUCT = 3;

		public final static int all[] = { REACTANT, AGENT, PRODUCT };
		public final static int maxRole = PRODUCT;
		public final static int ANY = -1;

	};

	public MoleculeHandlingParameters moleculeHandlingParameters;
	public static MoleculeHandlingParameters DefaultMoleculeHandlingParameters = new MoleculeHandlingParameters();

	public SmilesCreationParameters smilesParameters; // to be moved inside MoleculeHandlingParameters

	public JME jme; // parent

	// --- vlastne data pre molekulu
	int natoms = 0;
	int nbonds = 0;

	public int nAtoms() {
		return natoms;
	}

	public int nBonds() {
		return nbonds;
	}

	// public void setNbonds(int nbonds) {
	// this.nbonds = nbonds;
	// }
	static final int NSTART_SIZE_ATOMS_BONDS = 10;
	Atom atoms[] = new Atom[NSTART_SIZE_ATOMS_BONDS];
	Bond bonds[] = new Bond[NSTART_SIZE_ATOMS_BONDS];

	int a[]; // temporary array that holds an atom selection

	int chain[] = JMEUtil.createArray(101);
	int nmarked = 0; // obsolote in reaction automarking since 2009.04, still used in other marks
	int maxMark = 0; // updated only in autonumber

	int btype[]; // SINGLE,DOUBLE,TRIPLE,AROMATIC - len pre smi ? used locally, does not belog to
					// Bond (may be it should
	public int touchedAtom = 0; // nesmu byt static kvoli reaction (multi?)
	int touchedBond = 0;
	int touched_org = 0; // original v rubber banding
	double xorg, yorg; // center of ring in free space, rubber banding
	private boolean linearAdding = false; // pre ACTION_TBU (lepsie ???)
	int nchain; // pomocna variable pre CHAIN (aktualna dlzka pri rubber)
	boolean stopChain = false;
	boolean needRecentering = false;
	boolean isQuery = false; // 2013.09

	// static constants
	public static final int SINGLE = 1, DOUBLE = 2, TRIPLE = 3, AROMATIC = 5, QUERY = 9, COORDINATION = 0;
	static final int QB_ANY = 11, QB_AROMATIC = 12, QB_RING = 13, QB_NONRING = 14;
	static final int UP = 1, DOWN = 2, XUP = 3, XDOWN = 4, EITHER = 5, XEITHER = 6;
	static final int EZ = 10;
	public static final double RBOND = 25;
	static final int MAX_BONDS_ON_ATOM = 6;
	static boolean TESTDRAW = false;

	Color uniColor = null;

	protected Boolean chiralFlag = false;
	private int reactionRole = ReactionRole.NOROLE;

	// used for junit testing
	public JMEmol() {
		this(null, (MoleculeHandlingParameters) null);
	}

	public JMEmol(MoleculeHandlingParameters pars) {
		this(null, pars);
	}

	// ----------------------------------------------------------------------------
	public JMEmol(JME jme, MoleculeHandlingParameters pars) {
		this.jme = jme;
		moleculeHandlingParameters = (pars == null ? new MoleculeHandlingParameters() : pars);
		atoms[0] = new Atom(); // used in rubberbanding long alkyl chain
		natoms = 0;
		nbonds = 0;
		nmarked = 0;
	}

	/**
	 * Create a deep copy of the argument
	 * 
	 * @param m
	 */
	JMEmol(JMEmol m) {
		// vytvori kopiu molekuly m
		// nekopiruje v[][], nv[], nh[], bondCenterX[], bondCenterY[] - treba dimenzovat
		// v complete()
		jme = m.jme;
		moleculeHandlingParameters = m.moleculeHandlingParameters;

		reactionRole = m.reactionRole;

		// colorManager = m.colorManager;

		natoms = m.natoms;
		nbonds = m.nbonds;
		nmarked = m.nmarked;
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

		chiralFlag = m.chiralFlag;
	}

	// ----------------------------------------------------------------------------
	public JMEmol(JME jme, JMEmol mols[]) {
		// merge molecules to 1 molecule
		// iba docasne, pouziva sa pri zapise mol file;

		this(jme, mols.length > 0 && mols[0] != null ? mols[0].moleculeHandlingParameters : null);

		int nmols = mols.length;

		for (int i = 0; i < nmols; i++) {
			natoms += mols[i].natoms;
			nbonds += mols[i].nbonds;
			nmarked += mols[i].nmarked;

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
		this.fillFields(); // update the adjencylist
	}

	// ----------------------------------------------------------------------------
	// extracft a molecule part (fragment) of the my self (ensemble)
	public JMEmol(JME jme, JMEmol m, int part) {
		this(jme, m.moleculeHandlingParameters);
		m.computeMultiPartIndices(); // compute the partIndex
		int newn[] = JMEUtil.createArray(m.natoms + 1); // cislovanie stare -> nove
		for (int i = 1, n = m.natoms; i <= n; i++) {
			if (m.part(i) == part)
				newn[i] = natoms;
		}
		for (int i = 1; i <= m.nbonds; i++) {
			int atom1 = m.bonds[i].va;
			int atom2 = m.bonds[i].vb;
			if (m.part(atom1) != part && m.part(atom2) != part)
				continue;
			if (m.part(atom1) != part || m.part(atom2) != part) { // musia byt obidve part
				System.err.println("MOL multipart inconsistency - report bug !");
				continue;
			}
			Bond newAddedBond = createAndAddBondFromOther(m.bonds[i]);
			// bonds[nbonds].bondType = m.bonds[i].bondType;
			// stereob[nbonds] = m.stereob[i];
			newAddedBond.va = newn[atom1];
			newAddedBond.vb = newn[atom2];
			// btag[nbonds] = m.btag[i];
		}
		this.fillFields(); // update the adjencylist
	}

	/**
	 * Duplicated of JMEmol(JME jme, JMEmol m, int part) , uses the internal
	 * partIndex instead of the this.a[] for the part information
	 * 
	 * @param m
	 * @param part
	 */
	public JMEmol(JME jme, JMEmol m, int part, Object NOT_USED) {
		this(jme, m.moleculeHandlingParameters);
		int newAtomIndexMap[] = JMEUtil.createArray(m.natoms + 1); // cislovanie stare -> nove
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

		this.setChiralFlag(m.getChiralFlag());
		this.fillFields(); // update the adjencylist
	}

	// ----------------------------------------------------------------------------
	/**
	 * 
	 * @param jme
	 * @param molecule       : jme string
	 * @param hasCoordinates - not used anymore
	 * @param pars
	 * @throws Exception
	 */
	public JMEmol(JME jme, String molecule, boolean hasCoordinates, MoleculeHandlingParameters pars) throws Exception {
		// processes JME string
		// natoms nbonds (atomic_symbol x y) (va vb bondType)
		// atomic symbols for smiles-non-standard atoms may be in smiles form
		// i.e O-, Fe2+, NH3+
		// ak su tam vodiky, berie to cislo (aj H0) inak pre stand. atomy doplni
		// ak su standardne atomy v [] berie ich ako X
		// 2006.09 stereo double bond is -5

		this(jme, pars);
		// this.moleculeHandlingParameters = pars;

		// vyhodi "" na zaciatku a konci
		if (molecule.startsWith("\""))
			molecule = molecule.substring(1, molecule.length());
		if (molecule.endsWith("\""))
			molecule = molecule.substring(0, molecule.length() - 1);

		// System.err.println("TEST molecule in>"+molecule+"<");

		if (molecule.length() < 1) {
			natoms = 0;
			return;
		}
		try {
			StringTokenizer st = new StringTokenizer(molecule);
			int natomsx = Integer.valueOf(st.nextToken()).intValue();
			int nbondsx = Integer.valueOf(st.nextToken()).intValue();
			// natoms and nbonds filled in createAtom() & createBond()
			// System.err.println("TEST a b >"+natomsx + " " +nbondsx+"<");

			// --- reading basic data for atoms
			for (int i = 1; i <= natomsx; i++) {
				// Atom atom = this.atoms[i];
				// processing atomic symbol => Xx | Hn | charge | :n
				// symbol je vsetko od zaciatku do H + -
				// ak Xx spozna - spracuje vsetko, ak je to X, berie cely a testuje len :n
				String symbol = st.nextToken();
				Atom atom = this.createAtom(symbol);

				if (hasCoordinates) {
					atom.x = Double.valueOf(st.nextToken()).doubleValue();
					atom.y = -Double.valueOf(st.nextToken()).doubleValue();
				}
			}
			// --- bonds
			for (int i = 1; i <= nbondsx; i++) {
				Bond bond = createAndAddNewBond();
				bond.va = Integer.valueOf(st.nextToken()).intValue();
				bond.vb = Integer.valueOf(st.nextToken()).intValue();
				int bondType = Integer.valueOf(st.nextToken()).intValue();
				// musi premenit bondType -1 up a -2 down (z va na vb) na stereob
				int stereob = 0;
				if (bondType == -1) {
					bondType = 1;
					stereob = UP;
				} else if (bondType == -2) {
					bondType = 1;
					stereob = DOWN;
				} else if (bondType == -5) {
					bondType = 2;
					stereob = EZ;
				} // ez stereo
				// query bonds created in query window
				else if (bondType == QB_ANY || bondType == QB_AROMATIC || bondType == QB_RING
						|| bondType == QB_NONRING) {
					stereob = bondType;
					bondType = QUERY;
				}
				bond.bondType = bondType;
				bond.stereo = stereob;
			}

			fillFields(); // will be callsd by complete() later, disable it?

		} // end of try
		catch (Exception e) {
			System.err.println("read JSME string exception - " + e.getMessage());
			// e.printStackTrace();
			natoms = 0;
			throw (e);
		}
		deleteHydrogens(pars.hydrogenHandlingParameters);
		complete(pars.computeValenceState); // este raz, zachytit zmeny
	}

	// ----------------------------------------------------------------------------
	/**
	 * Read a MOL
	 * 
	 * @param jme
	 * @param molFile
	 */
	// would be clearer
	public JMEmol(JME jme, String molFile, MoleculeHandlingParameters options) {
		// MDL mol file
		this(jme, options);
		if (molFile != null)
			initFromMOL(molFile);
	}

	public JMEmol initFromMOL(String molFile) {

		//assert (this.moleculeHandlingParameters != null);

		if (molFile == null)
			return this;

		String line = "";
		String separator = JMEUtil.findLineSeparator(molFile);

		// BB: if something else than the molfile, e.g. smiles
		if (separator == null) {
			return this;
		}

		StringTokenizer st = new StringTokenizer(molFile, separator, true);

		for (int i = 1; i <= 4; i++) {
			line = JMEUtil.nextData(st, separator); 
		}
		int natomsx = Integer.valueOf(line.substring(0, 3).trim()).intValue();
		int nbondsx = Integer.valueOf(line.substring(3, 6).trim()).intValue();
		int chiral = 0;
		try {
			chiral = Integer.valueOf(line.substring(14, 15).trim()).intValue();
		} catch (Exception e) {
			// happens when the count line is mostly blank
		}

		this.setChiralFlag(chiral == 1);

		// number of bonds to this atom including implicit H's
		int valences[] = JMEUtil.createArray(natomsx + 1);
		for (int i = 1; i <= natomsx; i++) {
			Atom atom = createAtom();
			line = JMEUtil.nextData(st, separator);
			atom.x = Double.valueOf(line.substring(0, 10).trim()).doubleValue();
			atom.y = -Double.valueOf(line.substring(10, 20).trim()).doubleValue(); // Note: multiplied by -1
			atom.z = Double.valueOf(line.substring(20, 30).trim()).doubleValue();
			// symbol 32-34 dolava centrovany (v String 31-33)
			int endsymbol = 34;
			if (line.length() < 34)
				endsymbol = line.length();
			String symbol = line.substring(31, endsymbol).trim();
			// String q = line.substring(36,39);
			setAtom(i, symbol); // sets an[i]

			// atom mapping - 61 - 63
			if (line.length() >= 62) {
				String s = line.substring(60, 63).trim();
				if (s.length() > 0) {
					// 2007.03 fix not to put there 0
					int mark = Integer.valueOf(s).intValue();
					// if (mark > 0) {
					// touchedAtom = i;
					// jme.currentMark = mark;
					// mark(true);
					// touchedAtom = 0; // not to frame atom
					// }

					if (mark > 0) {
						this.setAtomMapFromInput(i, mark);
					}
				}
			}
			// System.out.println("atom "+i+" "+an[i]+" "+x(i)+" "+y[i]);

			// BB isotope -
			if (line.length() >= 36) {
				String s = line.substring(34, 36).trim();
				if (s.length() > 0) {
					int delta = Integer.valueOf(s).intValue();

					// delta can be [-3, +4]
					// 0 means natural abundance
					if (delta != 0 && delta >= -3 && delta <= 4) {
						int iso = AtomicElements.getIsotopicMassOfElementDelta(symbol, delta);
						if (iso < 0)
							iso = 0;
						this.atoms[i].iso = iso;
					}
				}
			}

			// BB charge - not tested
			if (line.length() >= 39) {
				String s = line.substring(37, 39).trim();
				if (s.length() > 0) {
					int delta = Integer.valueOf(s).intValue();
					// delta can be [-3, +4]
					// 0 means natural abundance
					if (delta > 0 && delta <= 7) {
						int charge = 0;
						switch (delta) {
						case 1:
							charge = 3;
							break;
						case 2:
							charge = 2;
							break;
						case 3:
							charge = 1;
							break;
						case 4:
							charge = 0; // TODO: doublet;
							break;
						case 5:
							charge = -1;
							break;
						case 6:
							charge = -2;
							break;
						case 7:
							charge = -3;
							break;

						}
						this.Q(i, charge);
					}
				}
			}

			// BB valence
			if (line.length() >= 45) {
				String s = line.substring(43, 45).trim();
				if (s.length() > 0)
					valences[i] = Integer.valueOf(s).intValue();
			}
		}

		for (int i = 1; i <= nbondsx; i++) {
			Bond bond = createAndAddNewBond();
			line = JMEUtil.nextData(st, separator);
			// System.out.println("bond"+line);
			bond.va = Integer.valueOf(line.substring(0, 3).trim()).intValue();
			bond.vb = Integer.valueOf(line.substring(3, 6).trim()).intValue();
			int nasvx = Integer.valueOf(line.substring(6, 9).trim()).intValue();
			int bondType;

			if (nasvx == 1)
				bondType = SINGLE;
			else if (nasvx == 2)
				bondType = DOUBLE;
			else if (nasvx == 3)
				bondType = TRIPLE;
			// else if (nasvx == 4) bondType = AROMATIC;
			else if (nasvx == 8)
				bondType = COORDINATION; // see comment for the MOL writer

			// aromatic ???
			else
				bondType = QUERY;
			int stereoVal = 0;
			if (line.length() > 11)
				stereoVal = Integer.valueOf(line.substring(9, 12).trim()).intValue();
			// ??? treba s nasvx
			if (bondType == SINGLE || bondType == COORDINATION) {

				if (stereoVal == 1) {
					bond.stereo = UP;
				} else if (stereoVal == 6) {
					bond.stereo = DOWN;
				} else if (stereoVal == 4) {
					bond.stereo = EITHER; // new Feb 2017
				}
			}
			if (nasvx == DOUBLE && stereoVal == 3) {
				bondType = DOUBLE;
				bond.stereo = EZ;
			} // crossed bond, Feb 2017

			bond.bondType = bondType;

			// System.out.println("bons "+i+" "+va[i]+" "+vb[i]+" "+bonds[i].bondType);
		}

		// reading charges and other information
		while (st.hasMoreTokens()) {
			if ((line = st.nextToken()) == null)
				break;
			if (line.startsWith("M  END"))
				break;

			if (line.startsWith("M  CHG")) {
				StringTokenizer stq = new StringTokenizer(line);
				stq.nextToken();
				stq.nextToken();
				int ndata = Integer.valueOf(stq.nextToken()).intValue();
				for (int i = 1; i <= ndata; i++) {
					int a = Integer.valueOf(stq.nextToken()).intValue();
					Q(a, Integer.valueOf(stq.nextToken()).intValue());
				}
			}

			// BB
			if (line.startsWith("M  ISO")) {
				StringTokenizer stq = new StringTokenizer(line);
				stq.nextToken();
				stq.nextToken();
				int ndata = Integer.valueOf(stq.nextToken()).intValue();
				for (int i = 1; i <= ndata; i++) {
					int a = Integer.valueOf(stq.nextToken()).intValue();
					// TODO: Change the atom symbol for display only
					// TODO: check validity atom index -> excepstion handling
					this.atoms[a].iso = Integer.valueOf(stq.nextToken()).intValue();

				}
			}

			if (line.startsWith("M  APO")) { // 2004.05
				StringTokenizer stq = new StringTokenizer(line);
				stq.nextToken();
				stq.nextToken();
				int ndata = Integer.valueOf(stq.nextToken()).intValue();
				for (int i = 1; i <= ndata; i++) {
					int a = Integer.valueOf(stq.nextToken()).intValue();
					int nr = Integer.valueOf(stq.nextToken()).intValue();
					// addinf Rnr to atom a
					touchedAtom = a;
					addBond();
					setAtom(natoms, "R" + nr);
					touchedAtom = 0;
				}
			}
		}

		// remove hydrogens when reading molfile
		// TODO BB: should this be done if by the option nohydrogens is on ?
		deleteHydrogens(this.moleculeHandlingParameters.hydrogenHandlingParameters);

		// BB: not a good idea
		// boolean orgKeepHydrogens = jme.keepHydrogens;
		// jme.keepHydrogens = false;
		// deleteHydrogens();
		// jme.keepHydrogens = orgKeepHydrogens;

		complete(this.moleculeHandlingParameters.computeValenceState); // este raz, zachytit zmeny

		// BB May 2017
		// compute the atom.nh if valence provided
		// example : AlH3 with implicit H's
		for (int i = 1; i <= natomsx; i++) {
			if (valences[i] > 0) {
				int nv = nv(i);
				// valences[i] is the total number of bonds to this atom, including implicit H
				if (valences[i] != 15) {
					int nh = valences[i] - nv;
					if (nh > 0)
						atoms[i].nh = nh;
				} else {
					atoms[i].nh = 0;
				}
			}
		}

		return this;

	}

	// ----------------------------------------------------------------------------

	public static JMEmol mergeMols(ArrayList<JMEmol> mols) {

		int n = mols.size();
		JMEmol result;

		if (n == 0)
			result = new JMEmol();
		else {

			result = new JMEmol(mols.get(0).jme, mols.toArray(new JMEmol[mols.size()]));
		}

		return result;
	}
	// ----------------------------------------------------------------------------

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

	int part(int at) {
		return atoms[at].partIndex;
	}

	void addBothNeighbors(int at1, int at2) {
		atoms[at1].addNeighbor(at2);
		atoms[at2].addNeighbor(at1);
	}

	protected boolean mixPastelBackGroundColors = true;

	public Color getPresetPastelBackGroundColor(int ab, boolean isAtom) {

		// TODO: clarify with comment
		// What is the diff between this.colorManager and
		// this.jme.this.moleculeHandlingParameters
		int colors[] = JMEUtil.createArray(1);
		boolean colorIsSet = false;

		boolean showMappingNumbers = this.moleculeHandlingParameters.number;
		boolean showColorMark = this.moleculeHandlingParameters.mark;
		boolean showAtomMapNumberWithBackgroundColor = this.moleculeHandlingParameters.showAtomMapNumberWithBackgroundColor;
		AtomBondCommon atomOrBond = isAtom ? this.atoms[ab] : this.bonds[ab];

		// an atom map can be mapped to the a given color of the palette, meaning that
		// visually
		// the reaction mapping could be easier to interpret than just the display of
		// numbers.
		if (showMappingNumbers && showAtomMapNumberWithBackgroundColor && isAtom) {
			Atom atom = this.atoms[ab];
			if (atom.isMapped()) {
				int map = atom.getMap();
				int max_map = this.jme.colorManager.numberOfBackgroundColors();
				// recycle the colors if too large atom map number
				while (map > max_map) {
					map -= max_map;
				}
				//assert (map >= 1);
				colors[0] = map;
				colorIsSet = true;

			}
		}

		if (!colorIsSet && showColorMark && atomOrBond.isMarked()) {
			colors[0] = atomOrBond.getMark(); // should we recycle the colors here?
			colorIsSet = true;
		}

		if (!colorIsSet) {
			// color mixing of background colors
			colors = isAtom ? this.getAtomBackgroundColors(ab) : this.getBondBackgroundColors(ab);
		}

		if (colors != null && colors.length > 0 && this.jme != null) { // the first color is fake
			return this.jme.colorManager.averageColor(colors);
		}
		return null;

	}

	/**
	 * Compute the average color for the atom or bond and set the color to the
	 * graphics use the .bacgroundColors array which is independent from the mark
	 * 
	 * @param og
	 * @param atom
	 * @param isAtom
	 * @return
	 */
	protected Color setPresetPastelBackGroundColor(PreciseGraphicsAWT og, int atom, boolean isAtom) {
		Color color = getPresetPastelBackGroundColor(atom, isAtom);
		if (color != null) {
			og.setColor(color);
		}
		return color;
	}

	/**
	 * Move all atoms, update the bond centers
	 * 
	 * @param dx
	 * @param dy
	 */
	@Override
	public void moveXY(double dx, double dy) {
		for (int at = 1; at <= natoms; at++) {
			atoms[at].moveXY(dx, dy);
		}
		this.findBondCenters(); // needed for mouse over
	}

	public void scaleXY(double scale) {
		if (scale > 0) {
			for (int at = 1; at <= natoms; at++) {
				atoms[at].scaleXY(scale);
			}
			this.findBondCenters(); // needed for mouse over
		}
	}

	Bond createAndAddNewBond(int atom1, int atom2) {
		return createAndAddNewBond(atom1, atom2, SINGLE);
	}

	/**
	 * Create and add a new bond to my bond list
	 * 
	 * @param atom1
	 * @param atom2
	 * @param bondType
	 * @return the new Bond
	 */
	Bond createAndAddNewBond(int at1, int at2, int bondType) {
		//assert (at1 != at2);
		Bond newBond = this.createAndAddNewBond(); // the new bond index is this.nbonds
		addBothNeighbors(at1, at2); // set up the adjacency lists
		newBond.va = at1;
		newBond.vb = at2;
		// compute bond centers
		newBond.initBondCenter(atoms);
		newBond.bondType = bondType;
		return newBond;
	}

	public int bondType(int bond) {
		return bonds[bond].bondType;
	}

	void forceUniColor(Color color) {
		this.uniColor = color;
	}

	void resetForceUniColor() {
		this.uniColor = null;
	}

	// used for SMILES generation
	boolean deleteHydrogens(boolean keepStereo) {
		MoleculeHandlingParameters.HydrogenHandlingParameters pars = new MoleculeHandlingParameters().hydrogenHandlingParameters;
		pars.keepStereoHs = keepStereo;
		pars.removeHs = true;
		pars.removeOnlyCHs = false;

		return deleteHydrogens(pars);
	}

	boolean deleteHydrogens(MoleculeHandlingParameters.HydrogenHandlingParameters pars) {
		boolean changed = false;

		if (pars.removeHs == false) {
			return changed;
		}

		fillFields(); // compute nv() adjancy list because itis needed below
		atom_loop: for (int i = natoms; i >= 1; i--) {
			int parent = v(i)[1];
			if (pars.removeOnlyCHs && an(parent) != JME.AN_C) {
				continue;
			}
			if (an(i) == JME.AN_H && nv(i) == 1 && q(i) == 0 && an(parent) != JME.AN_H && an(parent) < JME.AN_X 
					// X R R1 R2
			) {

				if (pars.keepIsotopicHs && this.atoms[i].iso != 0) {
					continue atom_loop;
				}
				// do not delete H with atom map
				if (pars.keepMappedHs && this.atoms[i].isMapped()) {
					continue atom_loop;
				}

				int bi = bondIdentity(i, parent);
				if (bonds[bi].bondType == SINGLE) {
					if (!(pars.keepStereoHs && bonds[bi].stereo != 0)) {
						deleteAtom(i); // deleteAtom will recompute the nv's
						changed = true;
					}

				}
			}
		}

		return changed;
	}

	/**
	 * @return the reactionRole
	 */
	public int getReactionRole() {
		return reactionRole;
	}

	/**
	 * @param reactionRole the reactionRole to set
	 */
	public void setReactionRole(int reactionRole) {
		this.reactionRole = reactionRole;
	}

	// ----------------------------------------------------------------------------
	// public functions for JMEcml
	public int getAtomCount() {
		return natoms;
	}

	public int getBondCount() {
		return nbonds;
	}

	/**
	 * @return the chiralFlag
	 */
	public Boolean getChiralFlag() {
		return chiralFlag;
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

	// was always in the code but never used
	// public double getX(int i) {return atoms[i].x * 1.4 / RBOND;}
	// public double getY(int i) {return atoms[i].y * 1.4 / RBOND;}
	public void setAtomProperties(double xx, double yy, int ahc, int aq) {
		// vztahuje sa na atom natoms
		// x[natoms] = xx; y[natoms] = yy;
		Atom atom = this.atoms[natoms];
		atom.x = xx;
		atom.y = yy;
		setAtomHydrogenCount(natoms, ahc);
		setAtomFormalCharge(natoms, aq);
	}

	public int getHydrogenCount(int i) {
		return atoms[i].nh;
	}

	public int getCharge(int i) {
		return q(i);
	}
	// public int[] getBondProperties(int i) {
	// int[] bd = JMEUtil.createArray(4);
	// bd[0] = bonds[i].va; bd[1] = bonds[i].vb; bd[2] = bonds[i].bondType; bd[3] =
	// bonds[i].stereo;
	// return bd;
	// }
	// TODO: change variable name - not clear
	// public void setBondProperties(int bp0, int bp1, int bp2, int bp3) {
	// // for actual bond, called after createBond() - so data for nbonds
	// bonds[nbonds].va = bp0; bonds[nbonds].vb = bp1; bonds[nbonds].bondType = bp2;
	// stereob[nbonds] = bp3;
	// }

	/**
	 * 
	 */
	public int geMaxAtomMap() {
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
		;

		for (int at = 1; at <= this.natoms; at++) {
			hasChanged = this.atoms[at].resetMap() || hasChanged;
		}
		this.maxMark = 0;

		return hasChanged;
	}

	// Feb 2020
	public boolean has2Dcoordinates() {
		if (this.nAtoms() <= 1) {
			return true;
		}
		if (has3Dcoordinates()) {
			return false;
		}

		if (this.nAtoms() == 2) {
			return x(1) != x(2) || y(1) != y(2);
		}
		for (int at = 1; at <= this.natoms; at++) {
			if (x(at) != 0 || y(at) != 0) {
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

	// ----------------------------------------------------------------------------
	public void completeMolecule(boolean computeValenceState) {
		// hodit to aj inde
		// fillFields(); //BB this is performedin complete()
		// deleteHydrogens(); // ??? treba
		// scaling();//scaling(); // would give NaN if 0 coordinates
		// center(); // centering in the editor canvas
		complete(computeValenceState);
	}

	// ----------------------------------------------------------------------------
	void complete(boolean computeValenceState) {
		// dimenzuje tie variables, co sa nedimenzovali v constructor JMEMol(m)
		fillFields();
		// int storage = bonds.length;
		// bondCenterX = JMEUtil.createArray(storage);
		// bondCenterY = JMEUtil.createArray(storage);
		findBondCenters();

		// March 2020: do valenceState() only if requested by the editor
		if (computeValenceState) {
			valenceState(); // nh a upravi q
		}
	}

	// ----------------------------------------------------------------------------
	/**
	 * New method to replace scaling() from the JMEmolList class
	 * 
	 * @return the reference bond length that was used for scaling or 0 if scaling
	 *         was not possible
	 */
	public double internalBondLengthScaling() {
		// proper scaling (to RBOND)
		double dx, dy, sumlen = 0., scale = 0.;

		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		double referenceBondLength = RBOND();

		for (int i = 1; i <= nbonds; i++) {
			dx = x(bonds[i].va) - x(bonds[i].vb);
			dy = y(bonds[i].va) - y(bonds[i].vb);
			double d = Math.sqrt(dx * dx + dy * dy);
			sumlen += d;
			max = Math.max(max, d);
			min = Math.min(min, d);
		}

		if (sumlen > 0) {
			if (nbonds > 0) {
				double average = sumlen / nbonds;

				// most of the time there is no significant difference between min and max
				if (average - min < max - average) { // a few bonds are much longer
					scale = min;
				} else {// a few bonds are much shorter
					scale = max;
				}

				scale = referenceBondLength / scale;
			} else if (natoms > 1) { // disconnected structure(s)
				scale = 3. * referenceBondLength
						/ Math.sqrt((x(1) - x(2)) * (x(1) - x(2)) + (y(1) - y(2)) * (y(1) - y(2)));
			}

			this.scaleXY(scale);
			// if (jme.dimension == null) jme.dimension = size();
			// cim vacsia scale, tym viac sa molekula zmensuje

			this.findBondCenters(); // BB added June 2020

			return referenceBondLength;
		}

		return 0;
	}

	public void center() {
		center(1.0);
	}

	public void center(double factor) {

		if (natoms == 0)
			return;

		// centers molecule within the window xpix x ypix
		//assert factor <= 1 && factor > 0;
		// ak depict => sd = 0
		// berie vonkajsie. nie vnutorne rozmery pri starte z main, preto korekcia
		// int xpix = jme.sd*18 - jme.sd - 20, ypix = jme.sd*16 - jme.sd*3 - 45;
		double xpix = 0, ypix = 0;

		if (jme != null) {
			Rectangle2D.Double widthAndHeight = this.jme.getMolecularAreaBoundingBoxCoordinate();
			xpix = widthAndHeight.width;
			ypix = widthAndHeight.height;
		}

		if (xpix <= 0 || ypix <= 0) { // does this ever happen?
			needRecentering = true;
			return;
		}

		Rectangle2D.Double cad = computeBoundingBoxWithAtomLabels();

		double shiftx = xpix / 2 - cad.getCenterX(); // . center[0];
		double shifty = ypix / 2 - cad.getCenterY(); // center[1];
		if (!jme.nocenter)
			moveXY(shiftx * factor, shifty * factor);
	}

	public double closestAtomDistance(double xx, double yy) {
		double min = Double.MAX_VALUE;

		for (int i = 1; i <= natoms; i++) {
			double rx = JMEUtil.squareEuclideanDist(xx, yy, x(i), y(i));
			if (rx < min) {
				min = rx;
			}
		}

		return Math.sqrt(min);
	}

	/**
	 * Need to be improved: should take the atom labels width and height into
	 * account
	 */
	@Override
	public double closestDistance(double x, double y) {
		return closestAtomDistance(x, y);
	}

	@Override
	public double centerX() {
		double sum = 0;
		for (int i = 1; i <= natoms; i++) {
			sum += x(i);
		}

		if (natoms > 0) {
			return sum / natoms;
		}

		return 0;

	}

	@Override
	public double centerY() {
		double sum = 0;
		for (int i = 1; i <= natoms; i++) {
			sum += y(i);
		}

		if (natoms > 0) {
			return sum / natoms;
		}
		return 0;
	}

	int testAtomAndBondTouch(double xx, double yy, boolean ignoreAtoms, boolean ignoreBonds, DoubleWrapper minWrapper) {
		int i, found = 0;
		double rx;
		double min = minWrapper.value;

		if (!ignoreBonds) {
			for (i = 1; i <= nbonds; i++) {
				// dx=xx-bondCenterX[i]; dy=yy-bondCenterY[i];
				// System.out.println(bondCenterX[i]);
				// System.out.println(bondCenterY[i]);
				// rx=dx*dx+dy*dy;
				Bond b = this.bonds[i];
				rx = JMEUtil.squareEuclideanDist(xx, yy, b.bondCenterX, b.bondCenterY);
				if (rx < min) {
					;
					min = rx;
					found = i;
				}

				if (found != 0) {
					// One problem : if the two atoms are very close, it is impossible to select the
					// bond
					// try with two more positions along the bond axis, at 1/3 and 2/3 of the
					// distance between the two atoms
					int va = b.va;
					int vb = b.vb;

					for (int third = 1; third <= 2; third++) {
						double x3 = x(va) + (double) (third) / 3 * (x(vb) - x(va));
						double y3 = y(va) + (double) (third) / 3 * (y(vb) - y(va));

						rx = JMEUtil.squareEuclideanDist(xx, yy, x3, y3);
						if (rx < min) {
							;
							min = rx;
						}
					}
				}
			}

			found *= -1; // negative for bond
		}

		if (!ignoreAtoms) {
			// Do the same for the atoms
			// min may be smaller for an atom even if a bond was found
			for (i = 1; i <= natoms; i++) {
				// dx=xx-x[i]; dy=yy-y[i];
				// rx=dx*dx+dy*dy;
				// rx=this.squareEuclideanDist(xx, yy, x[i], y[i]);
				rx = JMEUtil.squareEuclideanDist(xx, yy, x(i), y(i));
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
				double sqDistToAtom2 = JMEUtil.squareEuclideanDist(xx2, yy2, at2X, at2Y);
				// if too far away
				if (sqDistToAtom1 + sqDistToAtom2 > sqBondLength + min) {
					continue;
				}

				double dp = JMEUtil.dotProduct(xx2, yy2, at2X, at2Y);
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
					// break; //no break: there could be more than one long bond connected to the
					// same atom
				}

			}
		}
		minWrapper.value = min;
		return found;

	}

	public boolean hasCloseContactWith(JMEmol other, double minAtomDist) {

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

	// ----------------------------------------------------------------------------
	public void reset() {
		natoms = 0;
		nbonds = 0;
		nmarked = 0;
	}

	// ----------------------------------------------------------------------------
	void draw(PreciseGraphicsAWT og) {
		int atom1, atom2;
		double xa, ya, xb, yb, dx, dy, dd, sina = 1., cosa = 1.;
		double sirka2s, sirka2c;
		double sirka2 = 2., sirka3 = 3.;

		if (this.nAtoms() == 0)
			return; // bug fix github #25

		boolean markColorBackground = moleculeHandlingParameters.mark;
		Color atomTextStrokeColorArray[] = new Color[this.nAtoms() + 1];

		og.setDefaultBackGroundColor(jme.canvasBg);

		// ked padne, aby aspon ukazalo ramcek
		// this should not be done here
		if (jme.depictBorder) {
			og.setColor(Color.black);
			og.drawRect(0, 0, jme.dimension.width - 1, jme.dimension.height - 1);
		}

		if (this.uniColor != null) {
			og.overrideColor(this.uniColor);
		}

		// should not be done here
		if (needRecentering) {
			center();
			jme.alignMolecules(1, jme.moleculePartsList.size(), 0); // !!! nefunguje pre reakcion
			needRecentering = false;
			// System.err.println("DD recenter " + jme.dimension.width);
		}
		// TODO: no more reference to scalingIsPerformedByGraphicsEngine and
		// molecularAreaScale
		// atom + bond background coloring - done before drawing the atoms

		// bonds
		// color background for bonds (2 atoms must have a least one color in common)
		if (JME.bondBGrectRelativeSize > 0) {
			for (int i = 1; i <= nbonds; i++) {
				atom1 = bonds[i].va;
				atom2 = bonds[i].vb;

				// setPresetPastelBackGroundColor() returns null if no bacground colors have
				// been specified != mark
				if (setPresetPastelBackGroundColor(og, i, false) != null) {
					dx = x(atom2) - x(atom1);
					dy = y(atom2) - y(atom1);
					dd = Math.sqrt(dx * dx + dy * dy);
					if (dd < 1.)
						dd = 1.;
					sina = dy / dd;
					cosa = dx / dd;
					sirka2s = (sirka3 * 3) * sina;
					sirka2c = (sirka3 * 3) * cosa;

					sirka2s *= JME.bondBGrectRelativeSize;
					sirka2c *= JME.bondBGrectRelativeSize;

					double[] xr = JMEUtil.createDArray(4), yr = JMEUtil.createDArray(4);
					// xr[0] = x[atom1]+sirka2s; yr[0] = y[atom1]-sirka2c;
					// xr[1] = x[atom2]+sirka2s; yr[1] = y[atom2]-sirka2c;
					// xr[2] = x[atom2]-sirka2s; yr[2] = y[atom2]+sirka2c;
					// xr[3] = x[atom1]-sirka2s; yr[3] = y[atom1]+sirka2c;
					xr[0] = x(atom1) + sirka2s;
					yr[0] = y(atom1) - sirka2c;
					xr[1] = x(atom2) + sirka2s;
					yr[1] = y(atom2) - sirka2c;
					xr[2] = x(atom2) - sirka2s;
					yr[2] = y(atom2) + sirka2c;
					xr[3] = x(atom1) - sirka2s;
					yr[3] = y(atom1) + sirka2c;
					og.fillPolygon(xr, yr, 4);
				}
			}
		}

		// atom + bond background coloring - done before drawing the atoms
		// if (markColorBackground ||
		// this.moleculeHandlingParameters.showAtomMapNumberWithBackgroundColor) {
		{
			double cs = sirka2 * 12;

			// atoms : circle behind the atom position - does not cover the atom symbol
			// (will be done later)
			if (JME.atomBGcircleRelativeSize > 0) {
				for (int i = 1; i <= natoms; i++) {
					Color backgroundColor = setPresetPastelBackGroundColor(og, i, true);
					if (backgroundColor != null) {
						double scs = cs * JME.atomBGcircleRelativeSize;
						og.fillOval(x(i) - scs / 2., y(i) - scs / 2., scs, scs);

						// if we have a high resolution screen or a large zoom factor,
						// then add a thin stroke to the text to improve readability
						if (
								//JMEUtil.isHighDPI() || 
								og.currentZoomFactor() >= 2) { // or JMSE zoom factor > 100 %
							Color contrastColor = ColorManager.contrast(backgroundColor); // the stroke color is either
																							// white or black
							// depending on the darkness of the backgroundColor
							atomTextStrokeColorArray[i] = contrastColor;
						}

					}
				}
			}

		}

		// BB
		// boolean[] isRingBond = JMEUtil.createBArray(nbonds+1);
		// this.findRingBonds(isRingBond);

		// draw bonds
		for (int i = 1; i <= nbonds; i++) {
			// og.setColor(Color.black);

			Bond bond = bonds[i];
			atom1 = bond.va;
			atom2 = bond.vb;

			og.setColor(bond.isCoordination() ? Color.LIGHT_GRAY : Color.BLACK);

			// new June 2017
			if (jme.action == JME.ACTION_DELGROUP && touchedBond == i && this.isRotatableBond(i)) { // duplicated logic
																									// with code below
																									// for handling
																									// ACTION_DELGROUP

				// og.setColor(Color.RED);
				continue; // do not draw the bond , that is more visible than red color
			}
//			if (doColoring == 1) {
//				if (bgc(atom1) != 0 && bgc(atom1) == bgc(atom2)) {
//					setPresetPastelBackGroundColor(og, atom1, true); //based on the color of atom1
//				}
//			}

			if (bond.stereo == XUP || bond.stereo == XDOWN || bond.stereo == XEITHER) // kvoli spicke vazby
			{
				int d = atom1;
				atom1 = atom2;
				atom2 = d;
			}

			xa = x(atom1);
			ya = y(atom1);
			xb = x(atom2);
			yb = y(atom2);

			if (!(bond.isSingle() || bond.isCoordination()) || bond.stereo != 0) {
				dx = xb - xa;
				dy = yb - ya;
				dd = Math.sqrt(dx * dx + dy * dy);
				if (dd < 1.)
					dd = 1.;
				sina = dy / dd;
				cosa = dx / dd;
			}
			switch (bond.bondType) {
			case DOUBLE:
				// BB crossed bond display: not magenta anymore
				// if (bond.stereo >= 10) og.setColor(Color.magenta); // E,Z je farebna
				sirka2s = sirka2 * sina;
				sirka2c = sirka2 * cosa;
				if (bond.stereo != EZ) {
					og.drawLine(xa + sirka2s, ya - sirka2c, xb + sirka2s, yb - sirka2c);
					og.drawLine(xa - sirka2s, ya + sirka2c, xb - sirka2s, yb + sirka2c);
				} else { // BB: crossed bond
					og.drawLine(xa + sirka2s, ya - sirka2c, xb - sirka2s, yb + sirka2c);
					og.drawLine(xa - sirka2s, ya + sirka2c, xb + sirka2s, yb - sirka2c);

				}
				og.setColor(Color.black);
				break;
			case TRIPLE:
				double ixa = xa;
				double iya = ya;
				double ixb = xb;
				double iyb = yb;
				og.drawLine(ixa, iya, ixb, iyb);
				double sirka3s = sirka3 * sina;
				double sirka3c = sirka3 * cosa;
				og.drawLine(ixa + sirka3s, iya - sirka3c, ixb + sirka3s, iyb - sirka3c);
				og.drawLine(ixa - sirka3s, iya + sirka3c, ixb - sirka3s, iyb + sirka3c);
				/*
				 * g.drawLine((int)Math.round(xa+sirka3s),(int)Math.round(ya-sirka3c),
				 * (int)Math.round(bondCenterX+sirka3s),(int)Math.round(bondCenterY-sirka3c));
				 * g.drawLine((int)Math.round(xa-sirka3s),(int)Math.round(ya+sirka3c),
				 * (int)Math.round(bondCenterX-sirka3s),(int)Math.round(bondCenterY+sirka3c));
				 */
				break;
			case QUERY:// case 0: // dotted //BB : removed 0 because of coordination
				for (int k = 0; k < 10; k++) {
					double xax = xa - (xa - xb) / 10. * k;
					double yax = ya - (ya - yb) / 10. * k;
					og.drawLine(xax, yax, xax, yax);
				}
				// query bond text
				og.setFont(jme.atomDrawingAreaFont);
				double h = jme.stringHeight(jme.atomDrawingAreaFontMet); // vyska fontu
				/*
				 * String z = "?"; switch (stereob[i]) { case QB_ANY: z = "~"; break; case
				 * QB_AROMATIC: z = ":"; break; case QB_RING: z = "@"; break; case QB_NONRING: z
				 * = "!@"; break; }
				 */
				// 2007.10 dqp support
				Object o = bond.btag;
				String z = "?";
				if (o != null)
					z = (String) o;
				double w = jme.atomDrawingAreaFontMet.stringWidth(z);
				double xstart = (xa + xb) / 2. - w / 2.;
				double ystart = (ya + yb) / 2. + h / 2 - 1; // o 1 vyssie
				// g.setColor(Color.white);
				// g.fillRect(xstart-1,ystart-h+2,w+1,h-1);
				og.setColor(Color.magenta);
				og.drawString(z, xstart, ystart);
				og.setColor(Color.black);
				break;
			default: // SINGLE, alebo stereo
				if (bond.stereo == UP || bond.stereo == XUP) {
					sirka2s = sirka3 * sina;
					sirka2c = sirka3 * cosa;
					double[] px = JMEUtil.createDArray(3);
					double[] py = JMEUtil.createDArray(3);
					px[0] = xb + sirka2s;
					py[0] = yb - sirka2c;
					px[1] = xa;
					py[1] = ya;
					px[2] = xb - sirka2s;
					py[2] = yb + sirka2c;
					og.fillPolygon(px, py, 3);
				} else if (bond.stereo == DOWN || bond.stereo == XDOWN) {
					sirka2s = sirka3 * sina;
					sirka2c = sirka3 * cosa;
					for (double k = 0; k < 10; k++) {
						double xax = xa - (xa - xb) / 10. * k;
						double yax = ya - (ya - yb) / 10. * k;
						double sc = k / 10.;
						og.drawLine(xax + sirka2s * sc, yax - sirka2c * sc, xax - sirka2s * sc, yax + sirka2c * sc);
					}
				} else if (bond.stereo == EITHER || bond.stereo == XEITHER) {
					double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
					sirka2s = sirka3 * sina;
					sirka2c = sirka3 * cosa;
					double m = 8;
					for (double k = 0; k < m + 1; k += 1) {
						double xax = xa - (xa - xb) / m * k;
						double yax = ya - (ya - yb) / m * k;
						double sc = k / m; // thickness ?
						x1 = xax + sirka2s * sc;
						y1 = yax - sirka2c * sc;
						if (k > 0) {
							og.drawLine(x2, y2, x1, y1);
						}
						x2 = xax - sirka2s * sc;
						y2 = yax + sirka2c * sc;
						og.drawLine(x1, y1, x2, y2);

					}

				} else // normal single bonds
					og.drawLine(xa, ya, xb, yb);
				break;

			}
			// bond tags
			if (jme.doTags) {
				String btag = bond.btag;
				if (btag != null && btag.length() > 0) {
					og.setFont(jme.atomDrawingAreaFont);
					double h = jme.stringHeight(jme.atomDrawingAreaFontMet); // vyska fontu
					double w = jme.atomDrawingAreaFontMet.stringWidth(btag);
					double xstart = (xa + xb) / 2. - w / 2.;
					double ystart = (ya + yb) / 2. + h / 2 - 1; // o 1 vyssie
					og.setColor(Color.red);
					og.drawString(btag, xstart, ystart);
					og.setColor(Color.black);
				}
			}
		}

		this.computeAtomLabels();

		og.setFont(jme.atomDrawingAreaFont);
		double h = jme.stringHeight(jme.atomDrawingAreaFontMet); // vyska fontu

		// BB
		// try to improve the positioning and direction of the atom label when there are
		// either charges or implicit hydrogens
		// Direction example: -NH2 or H2N-
		// vertical positioning is currently not implemented
		// TODO: NH2, the 2 should subscript
		// TODO NH3+, the + should be superscript

		// draw atom background around the atom symbol if requested
		// draw atom symbol
		for (int i = 1; i <= natoms; i++) {

			Atom atom = this.atoms[i];
			if (atom.al.noLabelAtom) {
				continue;
			}
			Box r = atom.al.atomLabelBoundingBox;

			double xstart = atom.al.labelX;
			double ystart = atom.al.labelY;

			og.setBackGroundColor(); // set default background color
			setPresetPastelBackGroundColor(og, i, true);

			// surround the atom label with background color to mask the bonds around the
			// atom label
			double roundedCorner = r.height / 2; // the height of the box depends on the font height used to compute the
													// box size
			og.fillRoundRect(r.x, r.y, r.width, r.height, roundedCorner, roundedCorner);

			// color for the atom symbol
			og.setColor(JME.color[an(i)]);
			Color strokeColor = atomTextStrokeColorArray[i];

			og.drawStringWithStrokeAndBaselineShifts(atom.al.zz, xstart, ystart, strokeColor, h / 20,
					atom.al.subscripts, atom.al.superscripts);
		}

		// diplay atom maps of atoms that have been marked
		// marked atoms - islo by to do predosleho loopu zapasovat ???

		// 10-2018: do not show maps if option star is set
		if (!markColorBackground) {
			og.setFont(jme.atomMapDrawingAreaFont);

			for (int i = 1; i <= natoms; i++) {
				Atom at = this.atoms[i];
				String mapString = at.al.mapString;

				if (mapString == null)
					continue;

				double atomMapX = at.al.atomMapX;
				double atomMapY = at.al.atomMapY;

				og.setColor(Color.magenta); // default color for atom map - could be an option

				// duplicated code
				Color strokeColor = atomTextStrokeColorArray[i];

				if (strokeColor == null) {
					og.drawString(mapString, atomMapX, atomMapY);
				} else {
					og.drawStringWithStroke(mapString, atomMapX, atomMapY, strokeColor, h / 20);
				}

			}
		}

		// two bugs:
		// doTags does not take leftToRight into account
		// if both atom maps and tags are presents , they will overlap

		// BB: I changed this part but could not test it
		// tags (povodne to bolo label)
		if (jme.doTags) {
			og.setFont(jme.atomDrawingAreaFont);

			for (int i = 1; i <= natoms; i++) {
				if (atag(i) == null || atag(i).equals(""))
					continue;
				// int w = jme.atomDrawingAreaFontMet.stringWidth(zz[i]);
				Atom at = this.atoms[i];
				double smallWidth = at.al.smallAtomWidthLabel;
				double fullWidth = at.al.fullAtomWidthLabel;

				double xstart = x(i) - smallWidth / 2.;
				double ystart = y(i) + h / 2 - 1; // o 1 vyssie
				og.setColor(Color.red);
				og.drawString(" " + atag(i), xstart + fullWidth, ystart);
			}
		}

		// mark touched bond or atom, or atoms marked to delete
		if ((touchedAtom > 0 || touchedBond > 0) && !jme.webme) {

			og.setColor(jme.action == JME.ACTION_DELETE ? Color.red : Color.blue);

			if (touchedAtom > 0 && jme.action != JME.ACTION_DELGROUP) {
				Rectangle2D.Double r = atoms[touchedAtom].al.atomLabelBoundingBox;
				og.drawRect(r.x, r.y, r.width, r.height);
			}

			if (touchedBond > 0 && jme.action != JME.ACTION_MOVE_AT) { // don't show a rectangle around the bond if the
																		// action is to move the atom

				atom1 = bonds[touchedBond].va;
				atom2 = bonds[touchedBond].vb;
				dx = x(atom2) - x(atom1);
				dy = y(atom2) - y(atom1);
				dd = Math.sqrt(dx * dx + dy * dy);
				if (dd < 1.)
					dd = 1.;
				sina = dy / dd;
				cosa = dx / dd;
				sirka2s = (sirka3 + 1) * sina;
				sirka2c = (sirka3 + 1) * cosa;
				double[] px = JMEUtil.createDArray(5);
				double[] py = JMEUtil.createDArray(5);
				px[0] = x(atom1) + sirka2s;
				px[1] = x(atom2) + sirka2s;
				py[0] = y(atom1) - sirka2c;
				py[1] = y(atom2) - sirka2c;
				px[3] = x(atom1) - sirka2s;
				px[2] = x(atom2) - sirka2s;
				py[3] = y(atom1) + sirka2c;
				py[2] = y(atom2) + sirka2c;
				px[4] = px[0];
				py[4] = py[0]; // bug in 1.01
				if (jme.action != JME.ACTION_DELGROUP) // pri DELGROUP nekresli modro
					og.drawPolygon(px, py, 5);

				if (jme.action == JME.ACTION_DELGROUP && isRotatableBond(touchedBond)) {
					// ACTION_DELGROUP is a specila way of deleting a groug of atoms icon: -X-R
					// two parts of the molecule, one to keep and one to delete
					// the smallest that will be deleted mut be shown in red
					// only possible if the selected bond is not a ring bond
					// marks atoms with unpleasent fate (suggested by Bernd Rohde)
					// the atoms selected for deleting are in this.a[]?

					int va = bonds[touchedBond].va;
					int vb = bonds[touchedBond].vb;
					this.computeMultiPartIndices(touchedBond);
					int partA = part(va);
					int partB = part(vb);
					int sizeA = 0;
					int sizeB = 0;

					for (int i = 1; i <= natoms; i++) {
						if (part(i) == partA) {
							sizeA++;
						} else if (part(i) == partB) {
							sizeB++;
						}
					}
					// choose the smallest part to be deleted
					int partToDelete = sizeA > sizeB ? partB : partA;
					// framing atoms to delete in red
					og.setColor(Color.red);
					for (int i = 1; i <= natoms; i++) {
						this.atoms[i].deleteFlag = false;
						if (part(i) == partToDelete) {
							this.atoms[i].deleteFlag = true;
							Rectangle2D.Double r = this.atoms[i].al.atomLabelBoundingBox;
							og.drawRect(r.x, r.y, r.width, r.height);
						}
					}
				}
			}
		}

		if (this.uniColor != null) {
			og.resetOverrideColor();
		}

	}

	/**
	 * ///////////////////////////////////////////// // atom labels
	 * 
	 * //orientation of the atom labels with H or charges //either right to left
	 * (OH) or left to right (HO) // based on the relative position of the neighbors
	 * // bug fix November 2019: atom map
	 * 
	 */
	void computeAtomLabels() {
		int atom1, atom2;

		// first compute for each atom the average X of its neighbors
		// this will be used to determine the text orientation of the label
		double neighborXaverage[] = JMEUtil.createDArray(natoms + 1);
		int neighborCount[] = JMEUtil.createArray(natoms + 1);

		for (int i = 1; i <= nbonds; i++) {

			atom1 = bonds[i].va;
			atom2 = bonds[i].vb;

			double atom1X = x(atom1);
			double atom2X = x(atom2);

			neighborCount[atom1]++;
			neighborCount[atom2]++;

			neighborXaverage[atom1] = neighborXaverage[atom1] / neighborCount[atom1] + atom2X / neighborCount[atom1];
			neighborXaverage[atom2] = neighborXaverage[atom2] / neighborCount[atom2] + atom1X / neighborCount[atom2];
		}
		double h = 9.0;

		if (this.jme != null) {
			h = jme.stringHeight(jme.atomDrawingAreaFontMet); // vyska fontu

		}

		// BB
		// try to improve the positioning and direction of the atom label when there are
		// either charges or implicit hydrogens
		// Direction example: -NH2 or H2N-
		// vertical positioning is currently not implemented
		// TODO: NH2, the 2 should subscript
		// TODO NH3+, the + should be superscript

		int boundingBoxpadding = 2; // number of pixel of white space surrounding the atom labels

		for (int i = 1; i <= natoms; i++) {

			String z = getAtomLabel(i);
			if (z == null || z.length() < 1) {
				z = "*";
				System.err.println("Z error!");
			}

			Atom atom = this.atoms[i];
			atom.al = new AtomDisplayLabel(z);

			double smallWidth = 0; // used to position / center the atom label
			double fullWidth = 0;
			boolean leftToRight = true;

			if (an(i) == JME.AN_C && nv(i) > 0 && q(i) == 0 && atom.iso == 0 && !atom.isCumuleneSP() && !TESTDRAW) {
				// No atom atom label: C and a bond angle
				// atom.al.zz = z;
				// atom.al.smallAtomWidthLabel = atom.al.fullAtomWidthLabel =
				// jme.atomDrawingAreaFontMet.stringWidth(z);
				atom.al.noLabelAtom = true;

				// TODO: add bond thickness, different for double, triple
				// atom.al.atomLabelBoundingBox = new Box(atom.x, atom.y, 0, 0);

			} else {
				atom.al.noLabelAtom = false;
			}
			if (true) { // even if there are no labels, the bounding box is needed for mouse over

				// BB better display for OH or o- : the bond will point at the atom symbol "O"
				// and not at the center of the "OH"
				String atomSymbolsToBeUsedToComputeStringWidth = z;

				// determine the direction for the additional atom labels
				leftToRight = neighborXaverage[i] < x(i);
				boolean doSideMove = true; // the whole label will not be centered on the atom x,y coordinates

				// correction
				// if more than one neighbors and no clear x direction
				if (neighborCount[i] > 1 && Math.abs(neighborXaverage[i] - x(i)) < RBOND() / 3) {
					doSideMove = false;
				}
				// if more than 2 neighbors
				if (neighborCount[i] > 2) {
					doSideMove = false;
				}
				// if the two atoms are vertically aligned, then no right to left
				if (neighborCount[i] == 1 && Math.abs(neighborXaverage[i] - x(i)) < RBOND() / 10) {
					leftToRight = true;
				}

				String hydrogenSymbols = "";

				// nekresli C na allene (problemy s #C-)
				if (this.moleculeHandlingParameters.hydrogenHandlingParameters.showHs && !TESTDRAW) {
					int nh = this.atoms[i].nh;
					if (nh > 0) {
						hydrogenSymbols += "H";
						if (nh > 1) {
							hydrogenSymbols += nh; // add the number of hydrogens e.g for NH2
							// ???

						}
					}
				}

				// otherwise the bounding box for the mouse over will be too large
				if (atom.al.noLabelAtom) {
					hydrogenSymbols = "";
				}

				// BB isotopes
				String isoSymbol = "";
				if (atom.iso != 0) {
					isoSymbol = "[" + atom.iso + "]";
				}

				// charges
				String chargeSymbols = "";
				if (q(i) != 0) {
					if (Math.abs(q(i)) > 1)
						chargeSymbols += Math.abs(q(i));
					if (q(i) > 0)
						chargeSymbols += "+";
					else
						chargeSymbols += "-";

				}

				// testDraw ??
				String testDrawSymbol = "";

				if (TESTDRAW)
					testDrawSymbol += i; // add CT atom index

				if (leftToRight || !doSideMove) {
					atom.al.leftToRight = true;
					z = isoSymbol + z + hydrogenSymbols + chargeSymbols + testDrawSymbol; // NH2 , NH3+
				} else {
					atom.al.leftToRight = false;
					z = chargeSymbols + hydrogenSymbols + testDrawSymbol + isoSymbol + z; // H2N +H3N

				}

				if (!doSideMove) {
					atomSymbolsToBeUsedToComputeStringWidth = z;
				}
				atom.al.zz = z;

				// used to position / center the atom label
				smallWidth = jme.atomDrawingAreaFontMet.stringWidth(atomSymbolsToBeUsedToComputeStringWidth);
				// used to compute the bounding box of the atom label
				fullWidth = jme.atomDrawingAreaFontMet.stringWidth(z);

				atom.al.smallAtomWidthLabel = smallWidth;
				atom.al.fullAtomWidthLabel = fullWidth;

				atom.al.leftToRight = leftToRight;
				// TODO: atom mapping missing
				atom.al.atomLabelBoundingBox = createAtomLabelBoundingBoxRect(boundingBoxpadding, i, smallWidth,
						fullWidth, h, leftToRight);

				atom.al.mapString = null;

				atom.al.labelX = atom.al.atomLabelBoundingBox.x + boundingBoxpadding + 1; // see
																							// createAtomLabelBoundingBoxRect
																							// for the +1 (line
																							// thickness)
				atom.al.labelY = atom.al.atomLabelBoundingBox.y + h + boundingBoxpadding; // o 1 vyssie

				// place hydrogen count symbols subscript
				if (hydrogenSymbols.length() > 1) {
					int pos = z.indexOf(hydrogenSymbols);
					// H2 -> 2 subscript
					int[] styleIndices = { pos + 1, hydrogenSymbols.length() - 1 };

					atom.al.subscripts = new int[][] { styleIndices };
				}

				// place charge symbols superscript
				if (chargeSymbols.length() > 0) {
					int pos = z.indexOf(chargeSymbols);
					int[] styleIndices = { pos, chargeSymbols.length() };
					atom.al.superscripts = new int[][] { styleIndices };

				}

				// place isotope symbols superscript
				if (isoSymbol.length() > 0) {
					int pos = z.indexOf(isoSymbol);
					int[] styleIndices = { pos, isoSymbol.length() };

					if (atom.al.superscripts == null) {
						atom.al.superscripts = new int[][] { styleIndices };
					} else {
						atom.al.superscripts = new int[][] { atom.al.superscripts[0], styleIndices };
					}

				}

			}

			if (!moleculeHandlingParameters.mark || moleculeHandlingParameters.showAtomMapNumberWithBackgroundColor) { 
				if (!atom.hasBeenMapped()) {
					continue;
				}
				// to extend the size of the atomLabelBoundingBox
				Box box = atom.al.atomLabelBoundingBox;

				String mapString;

				mapString = " " + atom.getMap();

				double atomMapX;
				double atomMapY;
				if (!atom.al.noLabelAtom) {
					double atomMapStringWidth = jme.atomMapDrawingAreaFontMet.stringWidth(mapString);

					// remember: y points down
					double superscriptMove = h * 0.3; // BB: move the map symbol higher
					// double superscriptHeight = jme.atomMapDrawingAreaFontMet.getHeight();
					atomMapY = y(i) - superscriptMove;
					box.y -= superscriptMove;
					box.height += superscriptMove;
					box.width += atomMapStringWidth;

					if (leftToRight) {
						atomMapX = x(i) - smallWidth / 2. + fullWidth;
					} else {
						box.x -= atomMapStringWidth;
						atomMapX = x(i) + smallWidth / 2 - fullWidth - atomMapStringWidth;
					}

				} else {
					atomMapY = y(i) - (double) h * 0.1; // BB: move the map symbol higher
					atomMapX = x(i) + smallWidth / 4; // no atom symbol: put the map label closer, on the right
				}
				// double atomMapY = y(i) + h/2 - 1; // o 1 vyssie

				atom.al.atomMapX = atomMapX;
				atom.al.atomMapY = atomMapY;
				atom.al.mapString = mapString;
			}
		}
	}

	boolean hasAtomFlaggedToBeDeleted() {
		for (int at = 1; at <= natoms; at++) {
			if (atoms[at].deleteFlag)
				return true;
		}
		return false;
	}

	// BB: create a bounding box that surrounds the atom label with a padding of 1
	// pixel
	// smallWidth is the width of the atom label without charge and extra H
	// full width is the width of the atom label with everything
	// the box is bigger than the atom label itself (padding argument)
	Box createAtomLabelBoundingBoxRect(double padding, int atomIndex, double smallAtomWidthLabelArray,
			double smallAtomWidthLabelArray2, double height, boolean leftToRight) {
		int lineThickness = 1;

		// small width is used to compute the position xstart, that is the x position of
		// the label
		double xstart = x(atomIndex) - smallAtomWidthLabelArray / 2.;
		if (!leftToRight) {
			xstart -= (smallAtomWidthLabelArray2 - smallAtomWidthLabelArray); // move the xstart further left
		}
		double ystart = y(atomIndex) - height / 2; // o 1 vyssie

		// to take into account the line thickness
		xstart -= lineThickness;
		smallAtomWidthLabelArray2 += lineThickness;

		return new Box(xstart - padding, ystart - padding, smallAtomWidthLabelArray2 + 2 * padding,
				height + 2 * padding);

	}

	// ----------------------------------------------------------------------------
	@Override
	void move(double movex, double movey, Rectangle2D.Double boundingBoxLimits) {

		if (natoms == 0)
			return; // bbox is null if the molecule has no atoms

		super.move(movex, movey, boundingBoxLimits);

	}

	// ----------------------------------------------------------------------------
	void rotate(double movex) {
		// double center[] = JMEUtil.createDArray(4);

		if (natoms == 0)
			return; // bbox is null if the molecule has no atoms

		// get original position
		Box bbox = computeBoundingBoxWithAtomLabels();
		double centerx = bbox.getCenterX();
		double centery = bbox.getCenterY();

		// rotation
		double sinu = Math.sin(movex * Math.PI / 180.);
		double cosu = Math.cos(movex * Math.PI / 180.);
		for (int i = 1; i <= natoms; i++) {
			double xx = x(i) * cosu + y(i) * sinu;
			double yy = -x(i) * sinu + y(i) * cosu;
			// x[i] = xx; y[i] = yy;
			XY(i, xx, yy);
		}

		// moving to original position
		bbox = computeBoundingBoxWithAtomLabels();
		moveXY(centerx - bbox.getCenterX(), centery - bbox.getCenterY());
	}

	// ----------------------------------------------------------------------------
	/**
	 * returns x and y of the center of the molecule also w and h dimensions (for
	 * depict) returns null if the molecule has no atoms
	 *
	 * Bug: does not take into account the size the atom labels that will be used
	 * for displaying the molecule on the canvas
	 * 
	 * @param center, double[4]
	 */
	public Box boundingBox_OL() {
		Box bbox = null;

		if (natoms == 0)
			return bbox;

		double minx = Double.MAX_VALUE, maxx = Double.MIN_VALUE, miny = Double.MAX_VALUE, maxy = Double.MIN_VALUE;

		for (int i = 1; i <= natoms; i++) {
			if (x(i) < minx)
				minx = x(i);
			if (x(i) > maxx)
				maxx = x(i);
			if (y(i) < miny)
				miny = y(i);
			if (y(i) > maxy)
				maxy = y(i);
		}

		bbox = new Box();
		bbox.x = minx;
		bbox.y = miny;

		// for scaling in depict
		// bbox.width = Math.max(maxx - minx, RBOND);
		// bbox.height = Math.max(maxy - miny, RBOND);

		bbox.width = maxx - minx;
		bbox.height = maxy - miny;

		return bbox;
	}

	/**
	 * *Bug: estimate size the atom labels that will be used for displaying
	 * 
	 * @return
	 */
	public Box computeBoundingBoxWithAtomLabels() {
		Box bbox = null;

		if (natoms == 0)
			return bbox;

		this.computeAtomLabels();

		double minx = Double.MAX_VALUE, maxx = Double.MIN_VALUE, miny = Double.MAX_VALUE, maxy = Double.MIN_VALUE;

		for (int i = 1; i <= natoms; i++) {
			AtomDisplayLabel al = this.atoms[i].al;
			Box box = al.atomLabelBoundingBox;

			if (box.x < minx)
				minx = box.x;

			maxx = Math.max(box.x + box.width, maxx);

			if (box.y < miny)
				miny = box.y;

			maxy = Math.max(box.y + box.height, maxy);
		}

		bbox = new Box();
		bbox.x = minx;
		bbox.y = miny;

		// for scaling in depict
		// bbox.width = Math.max(maxx - minx, RBOND);
		// bbox.height = Math.max(maxy - miny, RBOND);

		bbox.width = maxx - minx;
		bbox.height = maxy - miny;

		return bbox;
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
	 * Move the touched atom to new coordinates
	 * 
	 * @param atom
	 * @param xNew
	 * @param yNew
	 */
	void atomRubberBanding(double xNew, double yNew) {
		if (this.touchedAtom > 0) {
			this.atomRubberBanding(this.touchedAtom, xNew, yNew);
		}
	}

	/**
	 * Move an atom to new coordinates
	 * 
	 * @param atom
	 * @param xNew
	 * @param yNew
	 */
	void atomRubberBanding(int atom, double xNew, double yNew) {
		// x[atom]=xNew;
		// y[atom]=yNew;
		XY(atom, xNew, yNew);
	}

	// ----------------------------------------------------------------------------
	// Addition a new bond in mouseDrag mode: move the bond around with the mouse
	// position
	void rubberBanding(double xnew, double ynew) {
		// len pre vazby
		// povodny touchedAtom je ulozeny v touched_org (urobene v mouse_down)

		// last atom is the atom at the end of the new bond

		touchedAtom = 0;

		// x[0] = xnew; y[0] = ynew; // position of the mouse (? nie totozne s natoms)
		XY(0, xnew, ynew); // gives atom 0 the coordinates of the mouse pointer
		int atom = checkTouch(0); // in order to find a close enough atom
		if (atom > 0 && jme.action != JME.ACTION_CHAIN) { // pri chaine to blblo
			touchedAtom = atom;
			if (atom != touched_org) { // make bond towards existing atom
				XY(natoms, x(atom), y(atom)); // move the new atom to the coordinate of the closest touched atom "snap"
				// actually it does not move while it still close to the touched atom
				// System.out.println("SNAP otheratom");
			} else { // this was standard position of the bond
				XY(natoms, xorg, yorg);// move the new atom to the coordinate of the origin atom "snap"
				// System.out.println("SNAP origin");

			}
		} else {
			if (jme.action == JME.ACTION_CHAIN) {
				// first atom (chain=1) was added in mouseDown
				// ma 4 moznosti: back, flip, add1, add2
				// chain[0] is origin, chain[1] moze len flipnut, nie deletnut
				// miesto add sa moze aj napojit na existing atom

				touchedBond = 0; // 2005.02 (no marked bond)
				// action according to the mouse position
				int last = chain[nchain]; // last atom
				int parent = chain[nchain - 1];
				double dx = x(last) - x(parent);
				double dy = y(last) - y(parent);
				double rx = Math.sqrt(dx * dx + dy * dy);
				if (rx < 1.0)
					rx = 1.0;
				double sina = dy / rx;
				double cosa = dx / rx;
				double vv = rx / 2. / Math.tan(Math.PI / 6.);
				// moving mouse pos
				double xx = xnew - x(parent);
				double yy = ynew - y(parent);
				double xm = -rx / 2. + xx * cosa + yy * sina; // relativ to "0"
				double ym = yy * cosa - xx * sina; // hore / dolu
				// zistuje poziciu mouse point relativne k trojuholniku
				if (xm < 0.) { // delete this atom
					// special treatment per 1. atom (inak sa vzdy vymaze)
					if (nchain > 1) { // !!!
						deleteAtom(natoms);
						// this.jme.recordAtomEvent("deleteAtom", natoms);
						nchain--;
						stopChain = false;
					} else if (natoms == 2) { // first 2 atoms / \ flip (4 positions)
						// up down flip
						if (y(2) - y(1) < 0 && ynew - y(1) > 0)
							atoms[2].y =y(1) + rx / 2.;
						else if (y(2) - y(1) > 0 && ynew - y(1) < 0)
							atoms[2].y = y(1) - rx / 2.;
						// left right flip
						if (x(2) - x(1) < 0 && xnew - x(1) > 0)
							atoms[2].x = x(1) + rx * .866;
						else if (x(2) - x(1) > 0 && xnew - x(1) < 0)
							atoms[2].x = x(1) - rx * .866;
					} else { // skusa flipnut 1. atom (x je vzdy -RBOND !) okolo chain[0]
						if (nv(chain[0]) == 2) { // i.e. moze flipnut
							int ref = v(chain[0])[1];
							if (ref == chain[1])
								ref = v(chain[0])[2];
							// flipne len ked mouse na opacnej strane ref---chain[0] ako ch[1]
							dx = x(chain[0]) - x(ref);
							dy = y(chain[0]) - y(ref);
							rx = Math.sqrt(dx * dx + dy * dy);
							if (rx < 1.0)
								rx = 1.0;
							sina = dy / rx;
							cosa = dx / rx;
							// moving mouse pos
							xx = xnew - x(ref);
							yy = ynew - y(ref);
							double ymm = yy * cosa - xx * sina; // hore / dolu
							// moving chain[1]
							xx = x(chain[1]) - x(ref);
							yy = y(chain[1]) - y(ref);
							double yc1 = yy * cosa - xx * sina; // hore / dolu
							if (ymm > 0. && yc1 < 0. || ymm < 0. && yc1 > 0.) { // su opacne
								int bd = nbonds;
								touchedAtom = chain[0];
								addBond(); // adds new bond
								deleteBond(bd); // delets old bond
								if (checkTouch(natoms) > 0)
									stopChain = true;
							}
						}
					}
				} else {
					if (stopChain)
						return;
					// calculates triangle height at this position
					double th = -1.; // mouse too far right
					if (xm < rx * 1.5)
						th = (rx * 1.5 - xm) * vv / (rx * 1.5);
					if (Math.abs(ym) > th) { // mouse above/below trinagle border
						nchain++;
						if (nchain > 100) {
							// jme.info("You are too focused on chains, enough of it for now !");
							jme.showInfo("You are too focused on chains, enough of it for now !");
							nchain--;
							return;
						}
						touchedAtom = natoms;
						addBond((int) Math.round(ym));
						// this.jme.recordBondEvent("addBond"); // wait until finished
						this.jme.willPostSave(false); // do not store undo state
						chain[nchain] = natoms;
						if (checkTouch(natoms) > 0)
							stopChain = true;
					}
				}

				touchedAtom = 0;
				// when starting from scratch ukazuje dlzku mensiu o 1
				int n = nchain;

				jme.info(n + ""); // napise dlzku do info
			} // end ACTION_CHAIN

			else { // bond width normal length follows mouse pointer
				double dx = xnew - x(touched_org);
				double dy = ynew - y(touched_org);
				double rx = Math.sqrt(dx * dx + dy * dy);
				if (rx < 1.0)
					rx = 1.0;
				double sina = dy / rx;
				double cosa = dx / rx;
				// x[natoms]=x(touched_org)+RBOND*cosa;
				// y[natoms]=y(touched_org)+RBOND*sina;

				XY(natoms, x(touched_org) + RBOND * cosa, y(touched_org) + RBOND * sina);

			}
		}
	}

	// ----------------------------------------------------------------------------
	void checkChain() {
		// called from mouseUp after finishing chain
		if (stopChain) { // if overlap, then last added atom
			int n = checkTouch(natoms);
			// adding bond natoms - natoms-1 to n
			if (nv(n) < MAX_BONDS_ON_ATOM) { // if ==, no error message
				// making bond n - nchain-1
				int parent = chain[nchain - 1];
				createAndAddNewBond(n, parent);
			}
			deleteAtom(natoms); // delete the last atom from the chain because of the closing with anonther atom
		}
		stopChain = false;
	}

	// ----------------------------------------------------------------------------
	/**
	 * checking touch of atom with my other atoms
	 * 
	 * @param atom
	 * @return the atom index that is the closest among the too close ones
	 */
	private int checkTouch(int atom) {
		return this.checkTouchToAtom(atom, 1, natoms);
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
	private int checkTouchToAtom(int atom, int firstAtom, int lastAtom) {
		// checking touch of atom with other atoms

		//assert lastAtom <= natoms;
		//assert firstAtom >= 1;
		//assert firstAtom <= lastAtom;

		double dx, dy, rx;
		double min = JME.TOUCH_LIMIT + 1;
		int touch = 0;
		for (int i = firstAtom; i <= lastAtom; i++) {
			if (atom == i)
				continue;
			// compute squared distance
			dx = x(atom) - x(i);
			dy = y(atom) - y(i);
			rx = dx * dx + dy * dy;
			if (rx < JME.TOUCH_LIMIT)
				if (rx < min) {
					min = rx;
					touch = i;
				}
		}
		return touch;
	}

	private double sumAtomTooCloseContacts(int atom, int firstAtom, int lastAtom) {
		// checking touch of atom with other atoms

		//assert lastAtom <= natoms;
		//assert firstAtom >= 1;
		//assert firstAtom <= lastAtom;

		double dx, dy, rx;
		double min = 2 * JME.TOUCH_LIMIT;
		double sum = 0;

		for (int i = firstAtom; i <= lastAtom; i++) {
			if (atom == i)
				continue;
			// compute squared distance
			dx = x(atom) - x(i);
			dy = y(atom) - y(i);
			rx = dx * dx + dy * dy;
			if (rx < min) {
				if (rx == 0) {
					rx = 0.0001;
				}
				sum += 1 / rx;
			}
		}
		return sum;
	}

	protected int countNumberOverlapAtomOfAddedFragment(int fragmentFirstAtom, int fragmentLastAtom) {
		int result = 0;
		for (int at = 1; at <= natoms; at++) { // MAY 2016 < <=
			if (at >= fragmentFirstAtom && at <= fragmentLastAtom)
				continue;

			if (this.checkTouchToAtom(at, fragmentFirstAtom, fragmentLastAtom) != 0) {
				result++;
			}
		}
		return result;
	}

	protected double sumAtomTooCloseContactsOfAddedFragment(int fragmentFirstAtom, int fragmentLastAtom) {
		double result = 0;
		for (int at = 1; at <= natoms; at++) { // MAY 2016 < <=
			if (at >= fragmentFirstAtom && at <= fragmentLastAtom)
				continue;

			result += this.sumAtomTooCloseContacts(at, fragmentFirstAtom, fragmentLastAtom);

		}
		return result;
	}

	// ----------------------------------------------------------------------------
	void avoidTouch(int from) {
		// checking atom overlap and moving atoms away from each other
		// moving always atom with the higher number
		// called after GROUP or CHAIN

		// BB: it is not the whole group that is moved, usually it a single atom of an
		// added ring
		// it could be more than one atom if there is more than one touch

		// should we have a loop that move atoms until there are no more overlap?

		// double dx,dy,rx;
		// double min=TOUCH_LIMIT+1;
		if (from == 0)
			from = natoms; // checks last from atoms

		for (int i = natoms; i > natoms - from; i--) {
			int n = checkTouch(i);
			if (n == 0)
				continue;
			// moving i away from n

			// BB rx is never used BUG????????????????
			// dx = x[i] - x[n]; dy = y[i] - y[n];
			// rx = dx * dx + dy * dy; //BB !!!!! rx is never used!!!!!
			// x[i] += 6; //BB: should at least use a DIRECTION ? - seems to work
			// y[i] += 6;
			moveXY(i, 6, 6);
		}
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
				bondI.initOtherFromMe(bondJ);
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
			// jme.clear(); //FIXME: this is not the right place to call code on jme
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

	// ----------------------------------------------------------------------------
	/*
	 * Delete the bond and its atoms if they end not to be connected to any other
	 * atoms
	 * 
	 */
	void deleteBond(int delbond) {
		deleteBond(delbond, true);
	}

	/**
	 * 
	 * @param delbond
	 * @param deleteLonelyAtoms
	 */
	void deleteBond(int delbond, boolean deleteLonelyAtoms) {
		// deletes bond between atoms delat1 and delat2
		int i, k, atom1, atom2;

		atom1 = bonds[delbond].va;
		atom2 = bonds[delbond].vb;

		for (i = delbond; i < nbonds; i++) {
			this.bonds[i] = this.bonds[i + 1];
		}
		nbonds--;

		// updating nv[] and v[][]
		k = 0;
		int ni = nv(atom1);
		for (i = 1; i <= ni; i++)
			if (v(atom1)[i] != atom2)
				v(atom1)[++k] = v(atom1)[i];
		NV(atom1, k);
		k = 0;
		ni = nv(atom2);
		for (i = 1; i <= ni; i++)
			if (v(atom2)[i] != atom1)
				v(atom2)[++k] = v(atom2)[i];
		NV(atom2, k);

		if (deleteLonelyAtoms) {
			// deleting lonely atom(s)
			if (atom1 < atom2) {
				k = atom1;
				atom1 = atom2;
				atom2 = k;
			}
			if (nv(atom1) == 0)
				deleteAtom(atom1);
			if (nv(atom2) == 0)
				deleteAtom(atom2);
		}
	}

	// ----------------------------------------------------------------------------
	/*
	 * Delete all atoms that have been marked for deletion (atom.deleteFlag == true)
	 */
	void deleteAtomGroup() {

		// does not work only bonds are deleted
		// for (int i=1;i<=natoms;i++) {
		// if(atoms[i].deleteFlag)
		// deleteAtom(i);
		// }

		while (true) {
			int atd = 0;
			for (int i = natoms; i >= 1; i--)
				if (atoms[i].deleteFlag && i > atd) {
					atd = i;
				}
			if (atd == 0)
				break;
			deleteAtom(atd);
			atoms[atd].deleteFlag = false;
		}

	}

	/**
	 * remove all coordination bonds. returns the number of bonds that have been
	 * removed Application: SMILES cannot represent them
	 */
	public int deleteCoordinationBonds() {
		int cbCount = 0;
		B: while (true) {
			for (int b = 1; b <= nbonds; b++) {
				if (bonds[b].bondType == COORDINATION) {
					this.deleteBond(b, false); // this decreases nbonds, thus the loop is not valid anymore and has to
												// be restarted
					cbCount++;
					continue B;
				}
			}
			break;
		}

		return cbCount;

	}

	// ----------------------------------------------------------------------------
	// called after delete atom/bond/group
	// when >NH+< remove charge, so it will be -N< and not -NH+<
	void backCations(int atom) {
		for (int i = 1; i <= nv(atom); i++) {
			int j = v(atom)[i];
			if (q(j) > 0)
				incrQ(i, -1); // q(j)--;
		}
	}

	// ----------------------------------------------------------------------------
	void backCations(int atom1, int atom2) {
		if (q(atom1) > 0)
			incrQ(atom1, -1);
		if (q(atom2) > 0)
			incrQ(atom1, -1);
	}

	// ----------------------------------------------------------------------------
	void flipGroup(int atom) {
		// flip group on this atom
		if (nv(atom) < 2)
			return;
	}

	// ----------------------------------------------------------------------------
	/**
	 * toggle the stereo status of the selected bond
	 * 
	 * @param bondIndex
	 */
	void stereoBond(int bondIndex) {
		// alebo vola z drawingArea.mouseDown s (touchBond) a bondType je rozna,
		// alebo z completeBond, vtedy je bondType vzdy 1
		// robi to inteligente, presmykuje medzi 4, len kde je to mozne
		// v stereob je uschovane aj querytype ked ide o QUERY bond

		Bond bond = this.bonds[bondIndex];
		if (bond.isSingle() || bond.isCoordination()) { // accept coordination bond with stereo
			// UP a DOWN daju hrot na va[], XUP, XDOWN na vb[]
			int atom1 = bonds[bondIndex].va;
			int atom2 = bonds[bondIndex].vb;
			if (nv(atom1) < 2 && nv(atom2) < 2) { // <=2 nemoze byt kvoli allenu
				bond.stereo = 0;
				jme.info("Stereomarking meaningless on this bond !");
				return;
			}
			// atom1 - stary, atom2 - novy atom
			if (jme.webme) {
				// handling webme (up / down templates)
				// just switching up/xup and down/xdown
				if (!jme.revertStereo) {
					if (bond.stereo == UP)
						bond.stereo = XUP;
					else if (bond.stereo == XUP)
						bond.stereo = UP;
					else {
						if (nv(atom2) <= nv(atom1))
							bond.stereo = UP;
						else
							bond.stereo = XUP;
					}
				} else {
					if (bond.stereo == DOWN)
						bond.stereo = XDOWN;
					else if (bond.stereo == XDOWN)
						bond.stereo = DOWN;
					else {
						if (nv(atom2) <= nv(atom1))
							bond.stereo = DOWN;
						else
							bond.stereo = XDOWN;
					}
				}
			}

			// standard editor stuff
			switch (bond.stereo) {
			case 0: // aby bol hrot spravne (nie na nerozvetvenom)
				// UP dava normalne hrot na va[]
				if (nv(atom2) <= nv(atom1))
					bond.stereo = UP;
				else
					bond.stereo = XUP;
				break;
			case UP:
				bond.stereo = DOWN;
				break;
			case DOWN:
				bond.stereo = EITHER;
				break;
			case EITHER:
				if (nv(atom2) > 2)
					bond.stereo = XUP;
				else
					bond.stereo = UP;
				break;
			case XUP:
				bond.stereo = XDOWN;
				break;
			case XDOWN:
				bond.stereo = XEITHER;
				break;
			case XEITHER:
				if (nv(atom1) > 2)
					bond.stereo = UP;
				else
					bond.stereo = XUP;
				break;

			}
		} else if (bond.bondType == DOUBLE) {
			bond.toggleNormalCrossedDoubleBond();
			// if (bond.stereo == EZ) bond.stereo = 0; else bond.stereo = EZ;
		} else {
			jme.info("Stereomarking allowed only on single and double bonds!");
		}
	}

	// ----------------------------------------------------------------------------
	// returns stereo atom to which this bond points
	int getStereoAtom(int bond) {
		// UP a DOWN daju hrot na va[], XUP, XDOWN na vb[]
		switch (bonds[bond].stereo) {
		case UP:
		case DOWN:
			return bonds[bond].va;
		case XUP:
		case XDOWN:
			return bonds[bond].vb;
		}
		return 0;
	}
	// ----------------------------------------------------------------------------

	public void addBond() {
		addBondToAtom(touchedAtom, 0);
	}

	void addBond(int up) {
		this.addBondToAtom(touchedAtom, up);
	}

	/**
	 * 
	 * @param up flip bond to other side - only possible if the touched atom has 1
	 *           valence values for flip: 0,-1 or 1
	 * @return true if the up was parameter was used
	 */
	boolean addBondToAtom(int selectedAtom, int up) {
		// pridava atom a jeho koordinaty
		boolean upWasUsed = false;

		createAtom();
		switch (nv(selectedAtom)) {
		case 0:
			// x[natoms] = x(selectedAtom) + RBOND() * .866;
			// y[natoms] = y(selectedAtom) + RBOND() * .5;
			XY(natoms, x(selectedAtom) + RBOND() * .866, y(selectedAtom) + RBOND() * .5);
			break;
		case 1:
			int atom1 = v(selectedAtom)[1];
			int atom3 = 0; // reference, aby to slo rovno
			if (nv(atom1) == 2) {
				if (v(atom1)[1] == selectedAtom)
					atom3 = v(atom1)[2];
				else
					atom3 = v(atom1)[1];
			}
			double dx = x(selectedAtom) - x(atom1);
			double dy = y(selectedAtom) - y(atom1);
			double rx = Math.sqrt(dx * dx + dy * dy);
			if (rx < 0.001)
				rx = 0.001;
			double sina = dy / rx;
			double cosa = dx / rx;
			double xx = rx + RBOND() * Math.cos(Math.PI / 3.);
			double yy = RBOND() * Math.sin(Math.PI / 3.);
			// checking for allene -N=C=S, X#C-, etc
			// chain je ako linear !
			int i = bondIdentity(selectedAtom, atom1);
			if ((bonds[i].bondType == TRIPLE) || jme.action == JME.ACTION_BOND_TRIPLE
					|| (!isSingle(i) && (jme.action == JME.ACTION_BOND_DOUBLE || jme.action == JME.ACTION_BOND_TRIPLE))
					|| linearAdding) // linearAdding pre ACTION_TBU
			{
				xx = rx + RBOND();
				yy = 0.;
			}
			if (atom3 > 0) // to keep growing chain linear
				if (((y(atom3) - y(atom1)) * cosa - (x(atom3) - x(atom1)) * sina) > 0.)
					yy = -yy;
			// flip bond to other site
			if (up > 0 && yy < 0.)
				yy = -yy;
			else if (up < 0 && yy > 0.)
				yy = -yy;

			// x[natoms] = x(atom1) +xx*cosa - yy*sina;
			// y[natoms] = y(atom1) +yy*cosa + xx*sina;
			XY(natoms, x(atom1) + xx * cosa - yy * sina, y(atom1) + yy * cosa + xx * sina);

			upWasUsed = true;

			break;

		case 2:
			double[] newPoint = JMEUtil.createDArray(2);
			addPoint(selectedAtom, (double) RBOND(), newPoint);
			XY(natoms, newPoint[0], newPoint[1]);
			break;

		case 3:
		case 4:
		case 5:
			// postupne skusa linearne predlzenie vsetkych vazieb z act_a
			for (i = 1; i <= nv(selectedAtom); i++) {
				atom1 = v(selectedAtom)[i];
				dx = x(selectedAtom) - x(atom1);
				dy = y(selectedAtom) - y(atom1);
				rx = Math.sqrt(dx * dx + dy * dy);
				if (rx < 0.001)
					rx = 0.001;
				XY(natoms, x(selectedAtom) + RBOND() * dx / rx, y(selectedAtom) + RBOND() * dy / rx);
				// teraz testuje ci sa nedotyka
				if (checkTouch(natoms) == 0 || i == nv(selectedAtom))
					break;
			}
			break;
		default: // error
			natoms--;
			jme.info("Are you trying to draw an hedgehog ?");
			jme.lastAction = JME.LA_FAILED; // aby nevolalo checkNewBond
			return upWasUsed;
		}
		completeBond();

		xorg = x(natoms);
		yorg = y(natoms); // used after moving, when moving !OK

		return upWasUsed;

	}

	// ----------------------------------------------------------------------------
	// necessary to add "smaller" bonds in scaled molecule bt WebME
	double RBOND() {
		return (JME.scalingIsPerformedByGraphicsEngine ? RBOND : RBOND * jme.molecularAreaScale);
	}

	// ----------------------------------------------------------------------------
	/**
	 * Create a bond between the touched atom and last added atom PE code - depends
	 * on JME
	 */
	void completeBond() {
		Bond bond = createAndAddNewBond(touchedAtom, natoms);
		if (jme.action == JME.ACTION_BOND_DOUBLE)
			bond.bondType = DOUBLE;
		if (jme.action == JME.ACTION_BOND_TRIPLE)
			bond.bondType = TRIPLE;
		// creating new bond with stereo tool
		if (jme.action == JME.ACTION_STEREO)
			stereoBond(nbonds);
	}
	// ----------------------------------------------------------------------------

	/**
	 * A new bond between the last atom and the touched atom has been created. if
	 * the two atoms are too close, then increase the bond order between the two
	 * atoms instead of creating a new bond
	 */
	void checkBond() {
		// check ci sa novo pridany atom neprekryva s nejakym starym
		// natoms bol posledne pridany atom, bol pridany k touchedAtom

		/*
		 * google translation: whether the newly added atom overlap with some old natoms
		 * was recently added atom , was added to the Touched
		 * 
		 */
		// check for touch of end of new bond with some atom
		int atom = checkTouch(natoms); // the atom index that is the closest among the too close ones
		if (atom == 0)
			return;

		// skutocne sa dotyka atomu atom = actually touches the Atom
		natoms--;

		int i = this.bondIdentity(atom, this.touched_org);
		if (i != 0) {
			nbonds--; // delete the just created new bond
			incrNV(this.touched_org, -1);
			// and increase the bond order between the two atom that were already bonded,
			// unless triple
			if (bonds[i].bondType < TRIPLE) {
				bonds[i].bondType++;
				bonds[i].stereo = 0;
			} // stereo zrusi
			else
				jme.info("Maximum allowed bond order is 3 !");
			return;

		}
		if (nv(atom) == MAX_BONDS_ON_ATOM) {
			nbonds--; // delete the just created new bond
			incrNV(this.touched_org, -1);
			jme.info("Not possible connection !");
			return;
		}

		// zmeni vazbove data na touched_org a atom
		bonds[nbonds].vb = atom;
		incrNV(this.touched_org, -1); // the new atom was added last, thus just need to decrease the nv
		this.addBothNeighbors(atom, touched_org);
		bonds[nbonds].initBondCenter(atoms);

	}

	// ----------------------------------------------------------------------------
	void addGroup(boolean emptyCanvas) {
		//
		touched_org = touchedAtom;
		int nadded = 0;
		if (jme.action == JME.ACTION_GROUP_TBU || jme.action == JME.ACTION_GROUP_CCL3
				|| jme.action == JME.ACTION_GROUP_CF3 || jme.action == JME.ACTION_GROUP_SULFO
				|| jme.action == JME.ACTION_GROUP_PO3H2 || jme.action == JME.ACTION_GROUP_SO2NH2) {
			addBond();
			touchedAtom = natoms;
			linearAdding = true; // pre addAtom
			addBond();
			linearAdding = false;
			touchedAtom = natoms - 1;
			addBond();
			touchedAtom = natoms - 2;
			addBond();
			if (jme.action == JME.ACTION_GROUP_CCL3) {
				AN(natoms, JME.AN_CL);
				AN(natoms - 1, JME.AN_CL);
				AN(natoms - 2, JME.AN_CL);
			}
			if (jme.action == JME.ACTION_GROUP_CF3) {
				AN(natoms, JME.AN_F);
				AN(natoms - 1, JME.AN_F);
				AN(natoms - 2, JME.AN_F);
			}
			if (jme.action == JME.ACTION_GROUP_SULFO) {
				AN(natoms, JME.AN_O);
				AN(natoms - 1, JME.AN_O);
				AN(natoms - 2, JME.AN_O);
				AN(natoms - 3, JME.AN_S);
				bonds[nbonds].bondType = DOUBLE;
				bonds[nbonds - 1].bondType = DOUBLE;
			}
			if (jme.action == JME.ACTION_GROUP_SO2NH2) {
				AN(natoms, JME.AN_O);
				AN(natoms - 1, JME.AN_O);
				AN(natoms - 2, JME.AN_N);
				AN(natoms - 3, JME.AN_S);
				bonds[nbonds].bondType = DOUBLE;
				bonds[nbonds - 1].bondType = DOUBLE;
			}
			if (jme.action == JME.ACTION_GROUP_PO3H2) {
				AN(natoms, JME.AN_O);
				AN(natoms - 1, JME.AN_O);
				AN(natoms - 2, JME.AN_O);
				AN(natoms - 3, JME.AN_P);
				bonds[nbonds].bondType = DOUBLE;
			}
			nadded = 4;
		} else if (jme.action == JME.ACTION_GROUP_NHSO2ME) {
			addBond();
			AN(natoms, JME.AN_N);
			touchedAtom = natoms;
			addBond();
			AN(natoms, JME.AN_S);
			touchedAtom = natoms;
			linearAdding = true;
			addBond();
			linearAdding = false;
			touchedAtom = natoms - 1;
			addBond();
			AN(natoms, JME.AN_O);
			bonds[nbonds].bondType = DOUBLE;
			touchedAtom = natoms - 2;
			addBond();
			AN(natoms, JME.AN_O);
			bonds[nbonds].bondType = DOUBLE;
			nadded = 5;
		} else if (jme.action == JME.ACTION_GROUP_NITRO) {

			addBond();
			AN(natoms, JME.AN_N);
			touchedAtom = natoms;
			if (this.jme.polarnitro) {
				this.changeCharge(touchedAtom, 1);
			}
			addBond();
			AN(natoms, JME.AN_O);
			bonds[nbonds].bondType = DOUBLE;
			touchedAtom = natoms - 1;

			addBond();
			AN(natoms, JME.AN_O);
			if (this.jme.polarnitro) {
				bonds[nbonds].bondType = SINGLE;
				this.changeCharge(natoms, -1);
			} else {
				bonds[nbonds].bondType = DOUBLE;
			}
			nadded = 3;
		} else if (jme.action == JME.ACTION_GROUP_COO) {
			addBond();
			touchedAtom = natoms;
			addBond();
			AN(natoms, JME.AN_O);
			touchedAtom = natoms - 1;
			addBond();
			AN(natoms, JME.AN_O);
			bonds[nbonds].bondType = DOUBLE;
			nadded = 3;
		} else if (jme.action == JME.ACTION_GROUP_COOME) {
			addBond();
			touchedAtom = natoms;
			addBond();
			AN(natoms, JME.AN_O);
			touchedAtom = natoms;
			addBond();
			touchedAtom = natoms - 2;
			addBond();
			AN(natoms, JME.AN_O);
			bonds[nbonds].bondType = DOUBLE;
			nadded = 4;
		}
		// BB
		else if (jme.action == JME.ACTION_GROUP_CON) {
			addBond();
			touchedAtom = natoms;
			addBond();
			AN(natoms, JME.AN_N);
			touchedAtom = natoms - 1;
			addBond();
			AN(natoms, JME.AN_O);
			bonds[nbonds].bondType = DOUBLE;
			nadded = 3;
		}
		// BB
		else if (jme.action == JME.ACTION_GROUP_NCO) {
			addBond();
			AN(natoms, JME.AN_N);
			touchedAtom = natoms;
			addBond();
			touchedAtom = natoms;
			addBond();
			AN(natoms, JME.AN_O);
			bonds[nbonds].bondType = DOUBLE;
			nadded = 3;
		} else if (jme.action == JME.ACTION_GROUP_OCOME) {
			addBond();
			AN(natoms, JME.AN_O);
			touchedAtom = natoms;
			addBond();
			touchedAtom = natoms;
			addBond();
			touchedAtom = natoms - 1;
			addBond();
			bonds[nbonds].bondType = DOUBLE;
			AN(natoms, JME.AN_O);
			nadded = 4;
		} else if (jme.action == JME.ACTION_GROUP_NME2) {
			addBond();
			AN(natoms, JME.AN_N);
			touchedAtom = natoms;
			addBond();
			touchedAtom = natoms - 1;
			addBond();
			nadded = 3;
		} else if (jme.action == JME.ACTION_GROUP_CC) {
			addBond();
			touchedAtom = natoms;
			linearAdding = true; // pre addAtom
			addBond();
			bonds[nbonds].bondType = TRIPLE;
			linearAdding = false; // pre addAtom
			nadded = 2;
		} else if (jme.action == JME.ACTION_GROUP_COH) { // -C=O (fixed 2005.04
			addBond();
			touchedAtom = natoms;
			addBond();
			bonds[nbonds].bondType = DOUBLE;
			AN(natoms, JME.AN_O);
			nadded = 2;
		} else if (jme.action == JME.ACTION_GROUP_dO) { // =O
			addBond();
			bonds[nbonds].bondType = DOUBLE;
			AN(natoms, JME.AN_O);
			nadded = 1;
		} else if (jme.action == JME.ACTION_GROUP_CCC) {
			addBond();
			touchedAtom = natoms;
			linearAdding = true; // pre addAtom
			addBond();
			touchedAtom = natoms;
			bonds[nbonds].bondType = TRIPLE;
			addBond();
			linearAdding = false; // pre addAtom
			nadded = 3;
		} else if (jme.action == JME.ACTION_GROUP_CYANO) {
			addBond();
			touchedAtom = natoms;
			linearAdding = true; // pre addAtom
			addBond();
			bonds[nbonds].bondType = TRIPLE;
			AN(natoms, JME.AN_N);
			linearAdding = false; // pre addAtom
			nadded = 2;
		} else if (jme.action == JME.ACTION_GROUP_CF) {
			addBond();
			AN(natoms, JME.AN_F);
			nadded = 1;
		} else if (jme.action == JME.ACTION_GROUP_CL) {
			addBond();
			AN(natoms, JME.AN_CL);
			nadded = 1;
		} else if (jme.action == JME.ACTION_GROUP_CB) {
			addBond();
			AN(natoms, JME.AN_BR);
			nadded = 1;
		} else if (jme.action == JME.ACTION_GROUP_CI) {
			addBond();
			AN(natoms, JME.AN_I);
			nadded = 1;
		} else if (jme.action == JME.ACTION_GROUP_CN) {
			addBond();
			AN(natoms, JME.AN_N);
			nadded = 1;
		} else if (jme.action == JME.ACTION_GROUP_CO) {
			addBond();
			AN(natoms, JME.AN_O);
			nadded = 1;
		} else if (jme.action == JME.ACTION_GROUP_C2) {
			addBond();
			touchedAtom = natoms;
			addBond();
			nadded = 2;
		} else if (jme.action == JME.ACTION_GROUP_C3) {
			addBond();
			touchedAtom = natoms;
			addBond();
			touchedAtom = natoms;
			addBond();
			nadded = 3;
		} else if (jme.action == JME.ACTION_GROUP_C4) {
			addBond();
			touchedAtom = natoms;
			addBond();
			touchedAtom = natoms;
			addBond();
			touchedAtom = natoms;
			addBond();
			nadded = 4;
		} else if (jme.action == JME.ACTION_GROUP_TEMPLATE) {
			// 2008.1 adding defined tamplate
			nadded = addGroupTemplate(emptyCanvas);
			// nadded = 4; // ???? WRONG
		}

		avoidTouch(nadded); // 2009.2, predtym 4

		touchedAtom = touched_org;
		if (emptyCanvas)
			touchedAtom = 0;
	}
	// ----------------------------------------------------------------------------

	void addRing() {
		// adding ring atoms
		// (bonds are added in completeRing)
		int atom1, atom2, atom3;
		double dx, dy, rx, sina, cosa, xx, yy;
		double diel, rc, uhol, xstart, ystart;
		int returnTouch = -1; // stopka pridavanie

		int nmembered = 6;
		switch (jme.action) {
		case JME.ACTION_RING_3:
			nmembered = 3;
			break;
		case JME.ACTION_RING_4:
			nmembered = 4;
			break;
		case JME.ACTION_RING_5:
		case JME.ACTION_RING_FURANE:
		case JME.ACTION_RING_3FURYL:
			nmembered = 5;
			break;
		case JME.ACTION_RING_6:
		case JME.ACTION_RING_PH:
			nmembered = 6;
			break;
		case JME.ACTION_RING_7:
			nmembered = 7;
			break;
		case JME.ACTION_RING_8:
			nmembered = 8;
			break;
		case JME.ACTION_RING_9:
			nmembered = 9;
			break;
		}

		diel = Math.PI * 2. / nmembered;
		rc = Math.sqrt(RBOND() * RBOND() / 2. / (1. - Math.cos(diel)));

		if (touchedAtom > 0) {
			// --- adding ring at the end of the bond
			if (nv(touchedAtom) < 2) {
				addRingToBond(nmembered, diel, rc);
			} else {
				if (!jme.mouseShift && !jme.spiroAdding) {
					// adding bond and ring
					returnTouch = touchedAtom;
					addBond();
					touchedAtom = natoms;
					addRingToBond(nmembered, diel, rc);
				} else {
					// checking whether cad do spiro
					jme.spiroAdding = false;
					if (jme.action == JME.ACTION_RING_PH || jme.action == JME.ACTION_RING_FURANE
							|| jme.action == JME.ACTION_RING_3FURYL) {
						jme.info("ERROR - cannot add aromatic spiro ring !");
						jme.lastAction = JME.LA_FAILED; // aby nevolalo checkNewRing
						return;
					}
					for (int i = 1; i <= nv(touchedAtom); i++) {
						// int bo = bondType[bondIdentity(touchedAtom,v(touchedAtom)[i])];
						int bo = bonds[bondIdentity(touchedAtom, v(touchedAtom)[i])].bondType;
						if (i > 2 || bo != SINGLE) {
							jme.info("ERROR - spiro ring not possible here !");
							jme.lastAction = JME.LA_FAILED; // aby nevolalo checkNewRing
							return;
						}
					}
					// --- adding spiro ring
					double[] newPoint = JMEUtil.createDArray(2);
					addPoint(touchedAtom, rc, newPoint);
					dx = x(touchedAtom) - newPoint[0];
					dy = y(touchedAtom) - newPoint[1];
					rx = Math.sqrt(dx * dx + dy * dy);
					if (rx < 0.001)
						rx = 0.001;
					sina = dy / rx;
					cosa = dx / rx;
					for (int i = 1; i <= nmembered; i++) {
						Atom newAtom = createAtom();
						uhol = diel * i + Math.PI * .5;
						// x[natoms]=newPoint[0]+rc*(Math.sin(uhol)*cosa-Math.cos(uhol)*sina);
						// y[natoms]=newPoint[1]+rc*(Math.cos(uhol)*cosa+Math.sin(uhol)*sina);
						XY(newAtom, newPoint[0] + rc * (Math.sin(uhol) * cosa - Math.cos(uhol) * sina),
								newPoint[1] + rc * (Math.cos(uhol) * cosa + Math.sin(uhol) * sina));
					}
				}
			}
		}

		// fusing ring
		else if (touchedBond > 0) {
			int revert;
			atom1 = bonds[touchedBond].va;
			atom2 = bonds[touchedBond].vb;
			// hlada ref. atom atom3
			atom3 = 0;
			if (nv(atom1) == 2) {
				if (v(atom1)[1] != atom2)
					atom3 = v(atom1)[1];
				else
					atom3 = v(atom1)[2];
			} else if (nv(atom2) == 2) {
				if (v(atom2)[1] != atom1)
					atom3 = v(atom2)[1];
				else
					atom3 = v(atom2)[2];
				revert = atom1;
				atom1 = atom2;
				atom2 = revert; // atom3 on atom1
			}
			if (atom3 == 0) // no clear reference atom
				if (v(atom1)[1] != atom2)
					atom3 = v(atom1)[1];
				else
					atom3 = v(atom1)[2];

			dx = x(atom2) - x(atom1);
			dy = y(atom2) - y(atom1);
			rx = Math.sqrt(dx * dx + dy * dy);
			if (rx < 0.001)
				rx = 0.001;
			sina = dy / rx;
			cosa = dx / rx;
			xx = rx / 2.;
			yy = rc * Math.sin((Math.PI - diel) * .5);
			revert = 1;
			if (((y(atom3) - y(atom1)) * cosa - (x(atom3) - x(atom1)) * sina) > 0.) {
				yy = -yy;
				revert = 0;
			}
			xstart = x(atom1) + xx * cosa - yy * sina;
			ystart = y(atom1) + yy * cosa + xx * sina;
			for (int i = 1; i <= nmembered; i++) {
				Atom newAtom = createAtom();
				uhol = diel * (i + .5) + Math.PI * revert;
				// x[natoms]=xstart+rc*(Math.sin(uhol)*cosa-Math.cos(uhol)*sina);
				// y[natoms]=ystart+rc*(Math.cos(uhol)*cosa+Math.sin(uhol)*sina);
				XY(newAtom, xstart + rc * (Math.sin(uhol) * cosa - Math.cos(uhol) * sina),
						ystart + rc * (Math.cos(uhol) * cosa + Math.sin(uhol) * sina));
				// next when fusing to the "long" bond
				if (revert == 1) {
					if (i == nmembered) {
						XY(newAtom, x(atom1), y(atom1));
						/* x(natoms)=x(atom1); y(natoms)=y(atom1); */}
					if (i == nmembered - 1) {
						XY(newAtom, x(atom2), y(atom2));
						/* x[natoms]=x(atom2); y[natoms]=y(atom2); */}
				} else {
					if (i == nmembered - 1) {
						XY(newAtom, x(atom1), y(atom1));
						/* x[natoms]=x(atom1); y[natoms]=y(atom1); */}
					if (i == nmembered) {
						XY(newAtom, x(atom2), y(atom2));
						/* x[natoms]=x(atom2); y[natoms]=y(atom2); */}
				}
			}
		}

		// new ring in free space
		else {
			double helpv = 0.5;
			if (nmembered == 6)
				helpv = 0.;
			for (int i = 1; i <= nmembered; i++) {
				Atom newAtom = createAtom();
				uhol = diel * (i - helpv);
				// x[natoms] = xorg + rc*Math.sin(uhol);
				// y[natoms] = yorg + rc*Math.cos(uhol);
				XY(newAtom, xorg + rc * Math.sin(uhol), yorg + rc * Math.cos(uhol));
			}
		}

		completeRing(nmembered);
		// a aby to bolo uz po mouse down OK
		checkRing(nmembered);
		// po check ring (inak nerobi avoid), pri stopke pridavani
		if (returnTouch > -1)
			touchedAtom = returnTouch;
	}

	// ----------------------------------------------------------------------------
	void addRingToBond(int nmembered, double diel, double rc) {
		double sina, cosa, dx, dy, rx, uhol;
		int atom1 = 0;
		if (nv(touchedAtom) == 0) {
			sina = 0.;
			cosa = 1.;
		} else {
			atom1 = v(touchedAtom)[1];
			dx = x(touchedAtom) - x(atom1);
			dy = y(touchedAtom) - y(atom1);
			rx = Math.sqrt(dx * dx + dy * dy);
			if (rx < 0.001)
				rx = 0.001;
			sina = dy / rx;
			cosa = dx / rx;
		}
		double xstart = x(touchedAtom) + rc * cosa;
		double ystart = y(touchedAtom) + rc * sina;
		for (int i = 1; i <= nmembered; i++) {
			Atom newAtom = createAtom();
			uhol = diel * i - Math.PI * .5;
			// x[natoms]=xstart+rc*(Math.sin(uhol)*cosa-Math.cos(uhol)*sina);
			// y[natoms]=ystart+rc*(Math.cos(uhol)*cosa+Math.sin(uhol)*sina);
			newAtom.XY(xstart + rc * (Math.sin(uhol) * cosa - Math.cos(uhol) * sina),
					ystart + rc * (Math.cos(uhol) * cosa + Math.sin(uhol) * sina));
		}
	}

	// ----------------------------------------------------------------------------
	void completeRing(int nmembered) {
		// adding bonds between ring atoms

		int atom = 0, atom3;
		for (int i = 1; i <= nmembered; i++) {
			createAndAddNewBond();
			// bondType[nbonds]=SINGLE; // is single is the deault of createBond()
			atom = natoms - nmembered + i;
			NV(atom, 2); // set number of neighbors to 2
			bonds[nbonds].va = atom;
			bonds[nbonds].vb = atom + 1; // setup the new bond between atom and atom + 1
		}
		bonds[nbonds].vb = natoms - nmembered + 1; // close the ring

		// alternating double bonds for phenyl and furane template
		// 2007.12 fixed problematic adding
		if (jme.action == JME.ACTION_RING_PH) {
			bonds[nbonds - 4].bondType = DOUBLE;
			bonds[nbonds - 2].bondType = DOUBLE;
			bonds[nbonds - 0].bondType = DOUBLE;
			if (touchedBond > 0) {
				if (isSingle(touchedBond)) {
					// fancy stuff - fusing two phenyls by single bond
					atom3 = 0;
					if (nv(bonds[touchedBond].va) > 1) {
						atom3 = v(bonds[touchedBond].va)[1];
						atom = bonds[touchedBond].va;
						if (atom3 == bonds[touchedBond].vb)
							atom3 = v(bonds[touchedBond].va)[2];
					}
					if (atom3 == 0 && nv(bonds[touchedBond].vb) > 1) {
						atom3 = v(bonds[touchedBond].vb)[1];
						atom = bonds[touchedBond].vb;
						if (atom3 == bonds[touchedBond].vb)
							atom3 = v(bonds[touchedBond].vb)[2];
					}
					if (atom3 > 0)
						// checking if bond atom3-atom is multiple
						for (int i = 1; i <= nbonds; i++)
							if ((bonds[i].va == atom3 && bonds[i].vb == atom)
									|| (bonds[i].va == atom && bonds[i].vb == atom3)) {
								if (!isSingle(i)) {
									bonds[nbonds - 4].bondType = SINGLE;
									bonds[nbonds - 2].bondType = SINGLE;
									bonds[nbonds - 0].bondType = SINGLE;
									bonds[nbonds - 5].bondType = DOUBLE;
									bonds[nbonds - 3].bondType = DOUBLE;
									bonds[nbonds - 1].bondType = TRIPLE;
								}
								break;
							}
				} else {
					bonds[nbonds - 4].bondType = SINGLE;
					bonds[nbonds - 2].bondType = SINGLE;
					bonds[nbonds - 0].bondType = SINGLE;
					bonds[nbonds - 5].bondType = DOUBLE;
					bonds[nbonds - 3].bondType = DOUBLE;
					bonds[nbonds - 1].bondType = DOUBLE;
				}
			}
		} else if (jme.action == JME.ACTION_RING_FURANE || jme.action == JME.ACTION_RING_3FURYL) {
			// fused pridava celkom inteligente (akurat ze jed dolava O je dolu)
			// treba to zmenit na hore ??? (asi nie)
			// zatial nefixuje C+ after fusing
			// 2008.11 fixed furane adding (opposite double bond)
			if (touchedBond > 0) {
				if (bonds[touchedBond].bondType == SINGLE) {
					// nned to check whether it is not =C-C= bond
					boolean isConjugated = false;
					for (int i = 1; i <= nv(bonds[touchedBond].va); i++) {
						int ax = v(bonds[touchedBond].va)[i];
						if (bonds[bondIdentity(bonds[touchedBond].va, ax)].bondType > SINGLE) {
							isConjugated = true;
							break;
						}
					}
					for (int i = 1; i <= nv(bonds[touchedBond].vb); i++) {
						int ax = v(bonds[touchedBond].vb)[i];
						if (bonds[bondIdentity(bonds[touchedBond].vb, ax)].bondType > SINGLE) {
							isConjugated = true;
							break;
						}
					}
					if (!isConjugated)
						bonds[touchedBond].bondType = DOUBLE;
				}
				bonds[nbonds - 4].bondType = DOUBLE;
				AN(natoms - 2, JME.AN_O);
			} else if (touchedAtom > 0) {
				if (jme.action == JME.ACTION_RING_FURANE) {
					bonds[nbonds - 4].bondType = SINGLE;
					bonds[nbonds - 2].bondType = SINGLE;
					bonds[nbonds - 1].bondType = SINGLE;
					bonds[nbonds - 3].bondType = DOUBLE;
					bonds[nbonds - 0].bondType = DOUBLE;
					AN(natoms - 1, JME.AN_O);
				} else {
					bonds[nbonds - 3].bondType = SINGLE;
					bonds[nbonds - 2].bondType = SINGLE;
					bonds[nbonds - 0].bondType = SINGLE;
					bonds[nbonds - 4].bondType = DOUBLE;
					bonds[nbonds - 1].bondType = DOUBLE;
					AN(natoms - 2, JME.AN_O);
				}
			} else { // new furane ring
				bonds[nbonds - 3].bondType = SINGLE;
				bonds[nbonds - 2].bondType = SINGLE;
				bonds[nbonds - 0].bondType = SINGLE;
				bonds[nbonds - 4].bondType = DOUBLE;
				bonds[nbonds - 1].bondType = DOUBLE;
				AN(natoms - 2, JME.AN_O);
			}
		}

	}

	// ----------------------------------------------------------------------------
	void checkRing(int nmembered) {
		// checks if newly created ring doesn't touch with some atoms +compute the v and
		// bondCenter

		int parent[] = JMEUtil.createArray(natoms + 1);

		// complete the adjacency list of the newly created bonds
		// * should have been done when the new bonds had been created TODO
		for (int i = 1; i <= nmembered; i++) {
			int ratom = natoms - nmembered + i;
			int rbond = nbonds - nmembered + i;
			v(ratom)[1] = ratom - 1;
			v(ratom)[2] = ratom + 1;
			bonds[rbond].initBondCenter(atoms);
		}
		// close ring
		v(natoms - nmembered + 1)[1] = natoms;
		v(natoms)[2] = natoms - nmembered + 1;

		// zistuje, ci sa nejake nove atomy dotykaju so starymi
		for (int i = natoms - nmembered + 1; i <= natoms; i++) { // loop over new ring atoms
			parent[i] = 0;
			double min = JME.TOUCH_LIMIT + 1;
			int tooCloseAtom = 0;
			for (int j = 1; j <= natoms - nmembered; j++) { // loop over older atoms
				double dx = x(i) - x(j);
				double dy = y(i) - y(j);
				double rx = dx * dx + dy * dy;
				if (rx < JME.TOUCH_LIMIT)
					if (rx < min) {
						min = rx;
						tooCloseAtom = j;
					} // BB break missing? TODO can we have more than one too close atom? No because
						// it finds the also the closest atom
			}
			if (tooCloseAtom > 0) // dotyk noveho atomu i so starym atomom atom
				if (touchedAtom == 0 || tooCloseAtom == touchedAtom) // ked stopka len ten 1
					parent[i] = tooCloseAtom;
		}
		// parent[i] and i must be merged?
		// robi nove vazby
		int noldbonds = nbonds - nmembered;
		bloop: for (int i = noldbonds + 1; i <= noldbonds + nmembered; i++) {
			int atom1 = bonds[i].va;
			int atom2 = bonds[i].vb;
			if (parent[atom1] > 0 && parent[atom2] > 0) { // in case of a touched bond?
				// ak parenty nie su viazane urobi medzi nimi novu vazbu
				for (int k = 1; k <= noldbonds; k++) {
					if ((bonds[k].va == parent[atom1] && bonds[k].vb == parent[atom2])
							|| (bonds[k].vb == parent[atom1] && bonds[k].va == parent[atom2]))
						continue bloop;
				}
				// BB create bond between parent[atom1] and parent[atom2]?
				this.createAndAddNewBond(parent[atom1], parent[atom2], bonds[i].bondType);
			} else if (parent[atom1] > 0) {
				// BB create bond between parent[atom1] and atom2?
				this.createAndAddNewBond(parent[atom1], atom2, bonds[i].bondType);
			} else if (parent[atom2] > 0) {
				// BB create bond between parent[atom2] and atom1?
				this.createAndAddNewBond(parent[atom2], atom1, bonds[i].bondType);
			}
		}

		// nakoniec vyhodi atomy, co maju parentov
		int noldatoms = natoms - nmembered;
		for (int i = natoms; i > noldatoms; i--) {
			if (parent[i] > 0) {
				deleteAtom(i);
				// 2007.12 checking 5-nasobnost u C
				if (an(parent[i]) == JME.AN_C) {
					int sum = 0;
					for (int j = 1; j <= nv(parent[i]); j++) {
						int a2 = v(parent[i])[j];
						for (int k = 1; k <= nbonds; k++) {
							if ((bonds[k].va == parent[i] && bonds[k].vb == a2)
									|| (bonds[k].va == a2 && bonds[k].vb == parent[i]))
								sum += bonds[k].bondType;
						}
					}
					if (sum > 4) {
						// zmeni nove vazby na single
						// more intelligent and keep double bond ???
						for (int k = noldbonds + 1; k <= noldbonds + nmembered; k++)
							bonds[k].bondType = SINGLE;
					}
				}
			}
		}

		// if stopka avoid
		if (touchedAtom > 0)
			avoidTouch(nmembered);

	}

	// ----------------------------------------------------------------------------
	private void addPoint(int touchedAtom, double rbond, double[] newPoint) {
		// adding new atom to source with two bonds already
		// called when creating new bond or ring center
		int atom1 = v(touchedAtom)[1];
		int atom2 = v(touchedAtom)[2];
		double dx = x(atom2) - x(atom1);
		double dy = -(y(atom2) - y(atom1));
		double rx = Math.sqrt(dx * dx + dy * dy);
		if (rx < 0.001)
			rx = 0.001;
		double sina = dy / rx;
		double cosa = dx / rx;
		// vzd. act_a od priamky atom1-atom2
		double vzd = Math.abs((y(touchedAtom) - y(atom1)) * cosa + (x(touchedAtom) - x(atom1)) * sina);
		if (vzd < 1.0) { // perpendicular to linear moiety
			dx = x(touchedAtom) - x(atom1);
			dy = y(touchedAtom) - y(atom1);
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
			dx = x(touchedAtom) - xpoint;
			dy = y(touchedAtom) - ypoint;
			rx = Math.sqrt(dx * dx + dy * dy);
			if (rx < 0.001)
				rx = 0.001;
			newPoint[0] = x(touchedAtom) + rbond * dx / rx;
			newPoint[1] = y(touchedAtom) + rbond * dy / rx;
		}
	}
	// ----------------------------------------------------------------------------

	// adding template store in jme.tmol to clicked atom
	// anchor atom in the template is marked by :1
	// emptyCanvas indicates that (artificial) touchedAtom should be deleted
	int addGroupTemplate(boolean emptyCanvas) {
		// finding mark:1 in template
		// qw

		JMEmol tmol = jme.templateMolecule;

		// BB: without the next two lines, the GWT optimizer fails (one can still
		// compile with the option -draftCompile and -XenableClosureCompiler)
		if (tmol == null || tmol.natoms == 0)
			return 0;

		int mark1 = 0;

		// find the atom that is marked in the template: this is the joining atom
		// for (int k=1;k<=tmol.nmarked;k++) {
		// int atom = tmol.mark[k][0];
		// if (tmol.mark[k][1] == 1) mark1 = atom;
		// }

		mark1 = tmol.findFirstMappdedOrMarkedAtom(); // bug fix 2022-02

		if (mark1 == 0) {
			mark1 = 1; // the template has not marked atoms
		}

		// //assert(mark1 > 0) ;

		int nn = natoms;

		// getting dummy point in original molecule
		int source = touchedAtom;

		BondDirection bd = new BondDirection();

		boolean hasTwoPossibleAddAngle = bd.initBondCreate(this, source, 1);

		BondDirection alternativeBD = null;
		if (hasTwoPossibleAddAngle) {
			alternativeBD = new BondDirection();
			alternativeBD.initBondCreate(this, source, -1);

		}

		BondDirection templateBD = new BondDirection();
		templateBD.initBondCreate(tmol, mark1, 0);

		// add the template atoms to myself no binding yet
		// do not yet move or rotate the template part
		this.addOtherMolToMe(tmol);
		complete(this.moleculeHandlingParameters.computeValenceState);

		// remove the map coming from the template
		this.atoms[nn + mark1].resetMap();

		if (!emptyCanvas) {

			templateBD.moveAndRotateFragment(this, nn + 1, natoms, source, bd);

			if (hasTwoPossibleAddAngle) {

				// count the number of touched atoms for the first bond direction
				double closeContactFactor = this.sumAtomTooCloseContactsOfAddedFragment(nn + 1, natoms);
				// if there is a close contact atom
				// may be the alternative bond direction has less touch atom

				// count the number of touched atoms for the alterantive bond direction

				// first restore the template coordinates inside my self
				for (int ta = 1; ta <= tmol.natoms; ta++) {
					// this.x[nn+ta] = tmol.x(ta);
					// this.y[nn+ta] = tmol.y(ta);
					XY(nn + ta, tmol.x(ta), tmol.y(ta));
				}

				templateBD.moveAndRotateFragment(this, nn + 1, natoms, source, alternativeBD);
				double alternativecloseContactFactor = this.sumAtomTooCloseContactsOfAddedFragment(nn + 1, natoms);

				if (alternativecloseContactFactor <= closeContactFactor) {
					// chose the alternative BD which is already set

				} else {
					// chose the first BD

					// first restore the template coordinates inside my self
					for (int ta = 1; ta <= tmol.natoms; ta++) {
						// this.x[nn+ta] = tmol.x(ta);
						// this.y[nn+ta] = tmol.y(ta);
						XY(nn + ta, tmol.x(ta), tmol.y(ta));
					}

					templateBD.moveAndRotateFragment(this, nn + 1, natoms, source, bd); // restore the rotation and
																						// translation using the first
																						// bd
				}

			}
		}

		// adding connecting bond
		createAndAddNewBond();
		bonds[nbonds].va = source;
		bonds[nbonds].vb = mark1 + nn;

		// for (int i=1;i<=natoms;i++)
		// System.out.println(i+" "+an[i]);
		// for (int i=1;i<=nbonds;i++)
		// System.out.println(i+" "+va[i]+" "+vb[i]+" "+bondType[i]);

		// cleanup the atom map //
		if (emptyCanvas) {
			deleteAtom(source);
			center();
		}
		complete(this.moleculeHandlingParameters.computeValenceState);

		return tmol.natoms; // BB needed later by avoidTouch
	}

	int getFirstMappedAtom() {
		for (int at = 1; at <= nAtoms(); at++) {
			if (this.atoms[at].isMapped()) {
				return at;
			}
		}

		return 0;
	}

	/**
	 * 
	 * @return 0 if not found
	 */
	protected int findFirstMappdedOrMarkedAtom() {
		for (int i = 1; i <= this.natoms; i++) {
			Atom at = this.atoms[i];
			if (at.isMappedOrMarked()) {
				return i;
			}
		}

		return 0;

	}

	public boolean hasMappedOrMarkedAtom() {
		return findFirstMappdedOrMarkedAtom() > 0;
	}

	/**
	 * Add all other molecule (fragment) atoms and bonds to my self. Do not create a
	 * new bond or change the coordinates. all atomic and bond proipoerties are
	 * copied. The argument is not changed.
	 * 
	 * @param otherMol
	 */
	protected void addOtherMolToMe(JMEmol otherMol) {
		int nn = natoms;

		// add the template atoms to myself no binding yet
		for (int i = 1; i <= otherMol.natoms; i++) {
			createAtomFromOther(otherMol.atoms[i]);
			AN(natoms, otherMol.an(i));
			// q[natoms] = otherMol.q(i);
			// iso[natoms] = otherMol.iso[i];
			// nh[natoms] = otherMol.nh[i];
			// x[natoms] = otherMol.x[i];
			// y[natoms] = otherMol.y[i];

			// missing things ?
		}

		// add the template bonds to myself - do not yet move or rotate the template
		// part
		for (int i = 1; i <= otherMol.nbonds; i++) {
			createAndAddBondFromOther(otherMol.bonds[i]); // create new bond and place it at the end: bonds[nbonds]
			bonds[nbonds].va = otherMol.bonds[i].va + nn;
			bonds[nbonds].vb = otherMol.bonds[i].vb + nn;
			// bonds[nbonds].bondType = otherMol.bonds[i].bondType;

			// BB add stereo
			// this.stereob[nbonds] = otherMol.stereob[i];
		}

	}

	// BB note: generic would not work with int or double
	/**
	 * 
	 * @param array
	 * @param newSize
	 * @return
	 */

	// ----------------------------------------------------------------------------
	public Atom createAtom() {
		return this.createAtomFromOther(null);
	}

	Atom createAtomFromOther(AtomBondCommon atomToDuplicate) {
		// creating new atom with AN_C
		// allocating memory if necessary
		natoms++;
		if (!GWT.isScript()) {
			// System.err.println(natoms + " " + an.length + " " + this.atoms.length );
			// if (natoms > an.length-1) {
			if (natoms > atoms.length - 1) {
				// int storage = an.length + 2;
				int storage = atoms.length + 20;
				// System.err.println("expand: " + natoms + " " + storage + " " +
				// this.atoms.length );

				// an = JMEUtil.growArray(an, storage);
				// q = JMEUtil.growArray(q, storage);

				// BB
				// iso = JMEUtil.growArray(iso, storage);

				// nh = JMEUtil.growArray(nh, storage);

				// backgroundColor = JMEUtil.growArray(backgroundColor, storage);

				// atag = JMEUtil.growArray(atag, storage);

				// label = JMEUtil.growArray(label, storage);

				// x = JMEUtil.growArray(x, storage);
				// y = JMEUtil.growArray(y, storage);

				// int n_v[][] = new int[storage][MAX_BONDS_ON_ATOM+1];
				// System.arraycopy(v,0,n_v,0,v.length);
				// v = n_v;

				// nv = JMEUtil.growArray(nv, storage);
				Atom newAtoms[] = new Atom[storage];
				System.arraycopy(atoms, 0, newAtoms, 0, atoms.length);
				this.atoms = newAtoms;

			}
		} else {
			/* Javascript arrays grow automatically when using a larger index */
		}
		// AN(natoms, JME.AN_C);
		// Q(natoms, 0);
		// backgroundColor[natoms] = 0;
		// atag[natoms] = null;
		// nh[natoms] = 0;
		// iso[natoms] = 0; //BB

		if (atomToDuplicate != null) {
			this.atoms[natoms] = (Atom) atomToDuplicate.deepCopy();
		} else {
			this.atoms[natoms] = new Atom();

		}

		return this.atoms[natoms];
	}

	// ----------------------------------------------------------------------------
	Atom createAtom(String symbol) {
		// parses SMILES-like atomic label and set atom parameters
		Atom atom = createAtom(); // sets natoms

		setAtom(natoms, symbol);

		return atom;
	}

	// setAtom is used when reding input file and when using the X button in the GUI
	// ----------------------------------------------------------------------------
	void setAtom(int atom, String symbol) {
		// volane pri spracovavani mol alebo jme z createAtom
		// alebo pri kliknuti na X atom x x boxu
		// aj po query

		// if in [] forces this valence state as AN_X, 2004.01
		if (symbol.startsWith("[") && symbol.endsWith("]")) {
			symbol = symbol.substring(1, symbol.length() - 1);
			AN(atom, JME.AN_X);
			atoms[atom].label= symbol;
			atoms[atom].nh = 0;
			return;
		}

		if (symbol.length() < 1)
			System.err.println("Error - null atom !");

		// BB: isotopic : 13C
		// symbol = this.atoms[atom].parseAtomSymbolIsotop(symbol);
		symbol = this.atoms[atom].parseAtomicSymbolPatternIsotopMappAndCharge(symbol, this.moleculeHandlingParameters);

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
		// int qpos = Math.max(symbol.indexOf("+"),symbol.indexOf("-"));

		/*
		 * 
		 * // spracuje label a odsekne ju //handling atom map if (this.map) { String
		 * smark = symbol.substring(dpos+1); // fixed in 2010.01 //maxMark =
		 * Integer.valueOf(smark).intValue() - 1; // v mark() je ++ //makos // try {
		 * //if a valid integer can be found in the symbol // jme.currentMark =
		 * Integer.parseInt(smark); // } // catch (Exception e) { // jme.currentMark =
		 * 0; // } // //touchedAtom = atom; // kvoli mark() // // //BB code copied from
		 * MOL reading // if (jme.currentMark > 0) { // touchedAtom = atom; //
		 * mark(true); // touchedAtom = 0; // not to frame atom // }
		 * 
		 * try { //if a valid integer can be found in the symbol int map =
		 * Integer.parseInt(smark);
		 * 
		 * if (this.moleculeHandlingParameters.markerMultiColor) {
		 * this.atoms[atom].backgroundColorIndex = map; } else {
		 * this.atoms[atom].setMap(map); }
		 * 
		 * } catch (Exception e) { //TODO: error reporting }
		 * 
		 * 
		 * 
		 * //mark(); // odsekne z konca :label symbol = symbol.substring(0,dpos);
		 * //touchedAtom = 0; }
		 */
		atomProcessing: {
			if (isQuery) {
				atoms[atom].label = symbol;
				AN(atom, JME.AN_X);
				atoms[atom].nh = 0;
				break atomProcessing;
			}

			// skusa, ci to je standard atom
			String as = symbol;

			// testuje > 0 nie > -1 (aby pokrylo H a zaciatok atomu s + -
			if (hpos > 0)
				as = symbol.substring(0, hpos);
			// else if (qpos > 0) as = symbol.substring(0,qpos);

			AN(atom, JMEUtil.checkAtomicSymbol(as)); // as & symbol su rozdielne/
			if (an(atom) == JME.AN_X)
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
			if (an(atom) == JME.AN_X) {
				atoms[atom].nh = nhs;
			}
			// co ostatne atomy ??? force ich

			// charge
			// int charge = 0;
			// if (qpos > 0) {
			// char c = symbol.charAt(qpos++);
			// if (c == '+') charge = 1;
			// else if (c == '-') charge = -1;
			// if (charge != 0) {
			// c = symbol.charAt(qpos++);
			// if (c >= '0' && c <= '9') c *= c - '0';
			// else {
			// while (c=='+') {charge++; c = symbol.charAt(qpos++);}
			// while (c=='-') {charge--; c = symbol.charAt(qpos++);}
			// }
			// }
			// }
			// Q(atom ,charge);
		}
	}

	// ----------------------------------------------------------------------------
	void setAtomHydrogenCount(int atom, int nh) {
		// upravuje to len pre X atomy !
		if (an(atom) == JME.AN_X) {
			// label[atom] += "H";
			this.atoms[atom].label += "H";
			if (nh > 1)
				// label[atom] += nh;
				this.atoms[atom].label += nh;
		}
	}

	// ----------------------------------------------------------------------------
	void setAtomFormalCharge(int atom, int nq) {
		Q(atom, nq); // setne aj pre X atomy
		// musi byt volane po setAtomHydrogenCount
		/*
		 * if (an(atom) == JME.AN_X) { if (nq > 0) label[atom] += "+"; else label[atom]
		 * += "-"; if (Math.abs(nq) > 1) label[atom] += Math.abs(nq); }
		 */
	}
	// ----------------------------------------------------------------------------

	/**
	 * 
	 * @param s      CSV string with atom (or bond) and color indices
	 * @param        delta: ensemble vs mol correction, increment of atom/bon index
	 *               to move to another fragment
	 * @param isAtom
	 */
	public void setAtomOrBondColors(String s, int delta, boolean isAtom) {

		StringTokenizer st = new StringTokenizer(s, ",");
		try {
			while (st.hasMoreTokens()) {
				int atomOrBond = Integer.valueOf(st.nextToken()).intValue() - delta;
				int color = Integer.valueOf(st.nextToken()).intValue();
				if (isAtom)
					addAtomColor(atomOrBond, color);
				else {
					addBondColor(atomOrBond, color); // Sept 2019
				}
			}
		} catch (Exception e) {
			System.err.println("Error in atom coloring");
			GWT.log("Error in atom coloring");
			// e.printStackTrace();
		}
	}

	public void setAtomColors(String s, int delta) {
		this.setAtomOrBondColors(s, delta, true);

	}

	public void setBondColors(String s, int delta) {
		this.setAtomOrBondColors(s, delta, false);

	}

	public void addAtomColor(int at, int c) {
		// if (c < 0 || c > this.colorManager.numberOfBackgroundColors()) {
		// return;
		// };
		if (at > 0 && at <= this.natoms) {
			AtomBondCommon atom = this.atoms[at];
			atom.addBackgroundColor(c);
		}

	}

	// FIXME: duplocate code with bgc()
	public int[] getAtomBackgroundColors(int at) {
		if (at > 0 && at <= this.natoms) {
			AtomBondCommon atom = this.atoms[at];
			return atom.backgroundColors;
		}

		return null;

	}

	public void addBondColor(int b, int c) {
		// if (c < 0 || c > this.colorManager.numberOfBackgroundColors()) {
		// return;
		// };
		if (b > 0 && b <= this.nbonds) {
			AtomBondCommon bond = this.bonds[b];
			bond.addBackgroundColor(c);
		}

	}

	public int[] getBondBackgroundColors(int b) {
		if (b > 0 && b <= this.nbonds) {
			AtomBondCommon bond = this.bonds[b];
			return bond.backgroundColors;
		}

		return null;

	}

	protected void resetChemicalObjectColors(AtomBondCommon chemicalObjects[]) {
		for (AtomBondCommon chemicalObject : chemicalObjects) {

			if (chemicalObject != null) {
				chemicalObject.resetBackgroundColors();
			}
		}
	}


	// ----------------------------------------------------------------------------
	Bond createAndAddNewBond() {
		return createAndAddBondFromOther(null);
	}

	Bond createAndAddBondFromOther(AtomBondCommon otherBond) {
		// creates new bonds (standard SINGLE)
		// allocating memory if necessary
		nbonds++;
		if (nbonds > bonds.length - 1) {
			int storage = bonds.length + 10;
			Bond newBonds[] = new Bond[storage];
			System.arraycopy(bonds, 0, newBonds, 0, bonds.length);
			this.bonds = newBonds;
		}
		Bond newBond = (Bond) (otherBond != null ? otherBond.deepCopy() : new Bond());
		this.bonds[nbonds] = newBond;
		return newBond;
	}

	// ----------------------------------------------------------------------------
	private void findBondCenters() {
		for (int b = 1; b <= nbonds; b++) {
			bonds[b].initBondCenter(atoms);
		}
	}

	/**
	 * Create a copy of my self. This code uses PE original code from the save()
	 * method
	 * 
	 * @param src
	 * @return
	 */
	JMEmol deepCopy() {
		return new JMEmol(this);
	}

	boolean isRotatableBond(int a1, int a2) {
		return minimumRingSize(a1, a2) == 0;
	}

	boolean isRotatableBond(int b) {
		return isRotatableBond(bonds[b].va, bonds[b].vb);
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
		int bondDist[] = JMEUtil.createArray(natoms + 1);
		int visited[] = JMEUtil.createArray(natoms + 1);
		int toVisit[] = JMEUtil.createArray(natoms + 1);

		// initialization
		bondDist[a1] = 1;
		toVisit[1] = a1;

		BOND_SPHERE_LOOP: while (true) {
			int visitCount = 0;
			for (int v = 1; toVisit[v] != 0; v++) {
				int at = toVisit[v];
				//assert (at != a2);

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

	// ----------------------------------------------------------------------------
	public void findRingBonds(int sizes[]) {
		// modifikuje a[]
		for (int i = 1; i <= nbonds; i++) {
			// isRingBond[i] = ! isRotatableBond(bonds[i].va,bonds[i].vb);
			int rs = minimumRingSize(bonds[i]);
			sizes[i] = rs;

		}
	}

	// ----------------------------------------------------------------------------
	public void findMinimumRingBondSize(int minBondRingSizes[]) {
		for (int i = 1; i <= nbonds; i++) {
			minBondRingSizes[i] = minimumRingSize(bonds[i]);
		}
	}

	// ----------------------------------------------------------------------------
	boolean isInRing(int atom, int minBondRingSizes[]) {
		for (int i = 1; i <= nv(atom); i++) {
			if (minBondRingSizes[bondIdentity(atom, v(atom)[i])] > 0)
				return true;
		}
		return false;
	}

	// ----------------------------------------------------------------------------
	void findAromatic(boolean isAromatic[], int minBondRingSizes[]) {
		// two pass

		btype = JMEUtil.createArray(nbonds + 1);
		boolean pa[] = JMEUtil.createBArray(natoms + 1); // possible aromatic

		for (int i = 1; i <= natoms; i++) {
			pa[i] = false;
			isAromatic[i] = false;
			if (!isInRing(i, minBondRingSizes))
				continue;
			// if (nv(i)+nh[i]>3) continue; // >X< nemoze byt aromaticky (ako s nabojmi?)
			if (nv(i) + atoms[i].nh > 3)
				continue; // >X< nemoze byt aromaticky (ako s nabojmi?)
			switch (an(i)) {
			case JME.AN_C:
			case JME.AN_N:
			case JME.AN_P:
			case JME.AN_O:
			case JME.AN_S:
			case JME.AN_SE:
				pa[i] = true;
				break;
			case JME.AN_X:
				// 2013.09
				if (atoms[i].label.startsWith("A"))
					pa[i] = false;
				else
					pa[i] = true;
				break;
			}

		}

		// 2013.09
		if (isQuery)
			doRingQueryCheck(isAromatic, minBondRingSizes);

		// 2. prechod, ide po ring vazbach a cekuje, zaroven plni aj btype[]
		// ignoruje stereo !!!
		for (int b = 1; b <= nbonds; b++) {
			if (isSingle(b))
				btype[b] = SINGLE;
			else if (isDouble(b))
				btype[b] = DOUBLE;
			else if (bonds[b].bondType == TRIPLE)
				btype[b] = TRIPLE;
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
			boolean a[] = JMEUtil.createBArray(natoms + 1); // plni na false
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
								btype[b] = AROMATIC;
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
		boolean ra[] = JMEUtil.createBArray(natoms + 1); // ring atoms
		boolean doCheck = false;
		for (int b = 1; b <= nbonds; b++) {
			int atom1 = bonds[b].va;
			int atom2 = bonds[b].vb;
			ra[atom1] = true;
			ra[atom2] = true;
			if (an(atom1) == JME.AN_X || an(atom2) == JME.AN_X)
				doCheck = true;
		}
		if (!doCheck)
			return; // no query atoms in rings

		bondloop1: for (int b = 1; b <= nbonds; b++) {
			if (minBondRingSizes[b] == 0)
				continue;
			int atom1 = bonds[b].va;
			int atom2 = bonds[b].vb;
			boolean a[] = JMEUtil.createBArray(natoms + 1); // tracking
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
									if (an(k) == JME.AN_O) {
										AN(k, JME.AN_X);
										atoms[k].label=  "#8";
									}
									if (an(k) == JME.AN_N) {
										AN(k, JME.AN_X);
										atoms[k].label= "#7";
									}
									if (an(k) == JME.AN_S) {
										AN(k, JME.AN_X);
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

	// ----------------------------------------------------------------------------
	void canonize() {
		// #1 atom will be simplest
		boolean ok;
		int a[] = JMEUtil.createArray(natoms + 1); // current ranking
		int aold[] = JMEUtil.createArray(natoms + 1);
		long d[] = JMEUtil.createLArray(natoms + 1);
		// primes
		long prime[] = JMEUtil.createLArray(natoms + 2); // +2 primes return minimum 2 values
		prime = JMEUtil.generatePrimes(natoms);

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
			if (xan == JME.AN_X) {
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
				deltaIso = AtomicElements.getDeltaIsotopicMassOfElement(this.getAtomLabel(i), atom.iso);
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
							d[i] *= an(atom) * btype[bondIdentity(i, atom)];
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
					JMEUtil.swap(this.atoms, i, s);
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
				// TODO: create new bond bunction: swapInternalAtoms()
				int du = bonds[i].va;
				bonds[i].va = bonds[i].vb;
				bonds[i].vb = du;
				if (bonds[i].stereo == UP)
					bonds[i].stereo = XUP;
				else if (bonds[i].stereo == DOWN)
					bonds[i].stereo = XDOWN;
				else if (bonds[i].stereo == XUP)
					bonds[i].stereo = UP;
				else if (bonds[i].stereo == XDOWN)
					bonds[i].stereo = DOWN;
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
			// changes i-th and b-th bond
			// int du;
			// du = bonds[i].va; bonds[i].va = bonds[b].va; bonds[b].va = du;
			// du = bonds[i].vb; bonds[i].vb = bonds[b].vb; bonds[b].vb = du;
			// du = bonds[i].bondType; bonds[i].bondType = bonds[b].bondType;
			// bonds[b].bondType = du;

			JMEUtil.swap(this.bonds, i, b);
			// du = bonds[i].stereo; bonds[i].stereo = stereob[b]; stereob[b] = du;
			// String ds = btag[i]; btag[i] = btag[b]; btag[b] = ds;
		}
		// btype sa znici, ale neskor v createSmiles sa znovu vypocita

		// fillFields();
		// findBondCenters();
		complete(this.moleculeHandlingParameters.computeValenceState);

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
				if (bondType == SINGLE || bondType == DOUBLE) { // tu nie E,Z

					// exceptions
					// not doing this by polarnitro set (since 2002.05)
					if (an(atom1) != JME.AN_C && an(atom2) != JME.AN_C && polarnitro)
						continue;
					// moved here 2011.10
					if (an(atom1) == JME.AN_H || an(atom2) == JME.AN_H)
						continue;
					if (an(atom1) == JME.AN_B || an(atom2) == JME.AN_B)
						continue;
					/*
					 * // not [H+]-[B-] in boranes (2005.02) if (an[atom1] == JME.AN_H || an[atom2]
					 * == JME.AN_H) continue; // // not [N+] [B-] 2011.10 if ((an[atom1]==JME.AN_B
					 * && an[atom2]==JME_AN.N) || (an[atom1]==JME.AN_N && an[atom2]==JME.AN_B))
					 * continue;
					 */

					// not between halogenes
					if (an(atom1) == JME.AN_F || an(atom1) == JME.AN_CL || an(atom1) == JME.AN_BR
							|| an(atom1) == JME.AN_I || an(atom2) == JME.AN_F || an(atom2) == JME.AN_CL
							|| an(atom2) == JME.AN_BR || an(atom2) == JME.AN_I)
						continue; // 2005.10

					// System.err.println("CPB1 "+atom1+" "+atom2);
					Q(atom1, 0);
					Q(atom2, 0);
					bondType++;
					bond.bondType = bondType; // needed because valenceState computes sum bond orders
					valenceState();
				}
			}

			// ??? nie, aspon 1 z nich musi byt C, (inak >N+=N+< meni na >NH+-NH+<)
			// if (q[atom1]==1 && q[atom2]==1 && (an[atom1]==h.C || an[atom2]==h.C)) {
			if (q(atom1) == 1 && q(atom2) == 1) {
				if (bondType == DOUBLE)
					bondType = SINGLE;
				else if (bondType == TRIPLE)
					bondType = DOUBLE;
				// System.err.println("CPB2");
				bond.bondType = bondType; // needed because valenceState computes sum bond orders
				valenceState();
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
	void fillFields() {
		// fills helper fields v[][], nv[], ??? vzdy allocates memory

		// int storage = an.length; // lebo pridava memory po skokoch
		// int storage = atoms.length; // lebo pridava memory po skokoch
		// v = JMEUtil.createArray(storage, MAX_BONDS_ON_ATOM+1); //new
		// int[storage][MAX_BONDS_ON_ATOM+1];
		// nv = JMEUtil.createArray(storage);

		for (int i = 1; i <= natoms; i++)
			NV(i, 0); // needed? I thinl hte array is always initialized to 0
		for (int i = 1; i <= nbonds; i++) {
			// if (nv(va[i]) < MAX_BONDS_ON_ATOM) // 2002.08 predtym <=
			// //v(va[i])[++nv[va[i]]]=vb[i];
			// v(va[i])[incrNV(va[i])]=vb[i];
			// if (nv(vb[i]) < MAX_BONDS_ON_ATOM)
			// //v(vb[i])[++nv[vb[i]]]=va[i];
			// v(vb[i])[incrNV(vb[i])]=va[i];
			int atom1 = bonds[i].va;
			int atom2 = bonds[i].vb;
			addBothNeighbors(atom1, atom2);

		}
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

		NEWPART: while (true) {
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
				break NEWPART;

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
					Atom atom1 = this.atoms[bond.va];
					Atom atom2 = this.atoms[bond.vb];
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

	/**
	 * Split my self into parts
	 * 
	 * @return an array of JMEmol
	 */
	public JMEmol[] splitMultiparts() {
		int nparts = computeMultiPartIndices();
		// JMEmol[] results = (JMEmol[]) JMEUtil.createOArray(nparts);//cast error at
		// runtime
		JMEmol[] results = new JMEmol[nparts];
		for (int part = 1; part <= nparts; part++) {
			results[part - 1] = new JMEmol(this.jme, this, part, null); // new implementation that uses the internal
																		// partIndex variable

		}

		return results;
	}

	/**
	 * SIDE EFFECT: set this.a[] !!!!!!!!!!!!!!!!!! what is this variable good for?
	 * 
	 * @param removeSmall
	 * @return the number of fragments? * this code is not used anymore
	 * 
	 */
	public int checkMultipart(boolean removeSmall) {
		// group prislusnost da do a[]
		int nparts = 0;
		boolean ok = false;
		a = JMEUtil.createArray(natoms + 1); // a is used by other

		while (true) {
			for (int j = 1; j <= natoms; j++)
				if (a[j] == 0) {
					a[j] = ++nparts;
					ok = true;
					break;
				}
			if (!ok)
				break;
			while (ok) {
				ok = false;
				for (int j = 1; j <= nbonds; j++) {
					int atom1 = bonds[j].va;
					int atom2 = bonds[j].vb;
					if (a[atom1] > 0 && a[atom2] == 0) {
						a[atom2] = nparts;
						ok = true;
					} else if (a[atom2] > 0 && a[atom1] == 0) {
						a[atom1] = nparts;
						ok = true;
					}
				}
			}
		}
		if (nparts < 2 || !removeSmall)
			return nparts;

		// najde najvacsiu
		int size[] = JMEUtil.createArray(nparts + 1);
		for (int i = 1; i <= natoms; i++)
			size[a[i]]++;
		int max = 0, largest = 1;
		for (int i = 1; i <= nparts; i++)
			if (size[i] > max) {
				max = size[i];
				largest = i;
			}
		// removing smaller part(s)
		for (int i = natoms; i >= 1; i--)
			if (a[i] != largest)
				deleteAtom(i);

		center(); // aby sa nedostalo do trap za okraj
		jme.info("Smaller part(s) removed !");
		return 1;
	}

	// used in the tests, should not be used in JME
	public String createSmiles() {
		return createSmiles(this.smilesParameters); // will use the default parameters from SmilesCreationParameters
													// class
	}

	/**
	 * BB Create a smiles, does not affect my self, unlike the original
	 * implementation which needed the nocanonize option
	 * 
	 * @return smiles
	 */
	public String createSmiles(SmilesCreationParameters pars) {
		String result = "";

		JMEmol copy = this.deepCopy();

		// from now on, coordination bonds will be shown with "~"
		// copy.deleteCoordinationBonds(); //change nbonds
		JMEmol parts[] = copy.splitMultiparts(); // parts contains deepcopies

		// return copy.createSmilesWithSideEffect(jme.stereo, jme.canonize);

		for (int p = 0; p < parts.length; p++) {
			String smiles = parts[p].createSmilesWithSideEffect(pars);
			if (result.length() > 0) {
				result += "."; // separator between molecules
			}
			result += smiles;
		}

		return result;

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
	String createSmilesWithSideEffect(SmilesCreationParameters pars) {
		if (natoms == 0)
			return "";

		if (pars == null)
			pars = new SmilesCreationParameters();

		this.smilesParameters = pars;

		int[] con1 = JMEUtil.createArray(natoms + 10); // well a little bit too much memory
		int[] con2 = JMEUtil.createArray(natoms + 10); // but the code is much cleaner than Vector
		// v niektorych exotoch je naozaj viac con ako atomov
		int[] branch = JMEUtil.createArray(natoms + 1);
		int[] candidate = JMEUtil.createArray(MAX_BONDS_ON_ATOM + 1);
		int[] parent = JMEUtil.createArray(natoms + 1);
		boolean[] isAromatic = JMEUtil.createBArray(natoms + 1);

		boolean[] isRingBond = JMEUtil.createBArray(nbonds + 1);
		int[] bondMinimumRingSize = JMEUtil.createArray(nbonds + 1);
		// boolean leftBranch[] = JMEUtil.createBArray(natoms+1); //BB: not used
		int nconnections = 0;

		// checkMultipart(true); //remove the small parts!!!
		// checkMultipart(false); //BB change: do not remove the small parts!!!
		// varaible a[] is now set

		boolean noQueryBonds = true;
		for (int b = 1; b <= nbonds; b++) {
			if (bonds[b].bondType == QUERY || bonds[b].isCoordination()) { // the canonize function goes into an
																			// infinite loop if there is a coordination
																			// bond
				noQueryBonds = false;
				break;
			}
		}
		// asi to treba takto komplikovane
		// btype RING_NONAROMATIC sa nepouziva ! (len aromatic)
		if (smilesParameters.canonize && noQueryBonds) {

			deleteHydrogens(!pars.stereo);
			cleanPolarBonds(pars.polarnitro);
			findRingBonds(bondMinimumRingSize);
			findAromatic(isAromatic, bondMinimumRingSize); // naplni btype
			canonize(); // btype[] sa tu znici
			valenceState();
			// prec
			findRingBonds(bondMinimumRingSize); // v canonize sa to prehadze, dat to tam ?
			findAromatic(isAromatic, bondMinimumRingSize); // znovy vypoicta btype[]
		} else { // to treba pre stereochemiu
			findRingBonds(bondMinimumRingSize);
			btype = JMEUtil.createArray(nbonds + 1); // inak to plni vo findAromatic
			for (int i = 1; i <= nbonds; i++)
				btype[i] = bonds[i].bondType;
		}
		for (int b = 1; b <= nbonds; b++) {
			isRingBond[b] = bondMinimumRingSize[b] > 0;
		}

		int atom = 1; // zacina sa najjednoduchsim
		a = JMEUtil.createArray(natoms + 1); // defined globally, fills with 0

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
					int b = bondIdentity(candidate[i], atom);
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
						int b = bondIdentity(candidate[i], atom); // pozor bondType[], btype[]
						// if (isDouble(bondType[b]) || bondType[b]==TRIPLE) // !!!
						if (btype[b] == DOUBLE || btype[b] == TRIPLE) {
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
		parent = JMEUtil.createArray(natoms + 1); // zmenene
		int aa[] = JMEUtil.createArray(natoms + 1); // nove poradie
		boolean leftBracket[] = JMEUtil.createBArray(natoms + 1);
		boolean rightBracket[] = JMEUtil.createBArray(natoms + 1);

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
		int slashBond[] = JMEUtil.createArray(nbonds + 1); // info about / or \ bonds (1,0,or -1)
		int slimak[] = JMEUtil.createArray(natoms + 1); // info about @ or @@ (1,0,or -1)
		if (smilesParameters.stereo)
			smilesStereo(aa, parent, slashBond, slimak, bondMinimumRingSize, con1, con2, nconnections);

		// -------- vlastne vytvaranie SMILESu
		// poradie ako sa vytvara je ulozene v aa[] a parent[]
		boolean queryMode = false; // dqp support
		// all X atoms takes as query? how to improve this ?
		// 2009.04 reverted, caused problems c1:c:[Ir]:c:c:c:1
		// uncomment for dqp
		// for (int i=1;i<=natoms;i++)
		// if (an[i] == JME.AN_X && !label[i].equals("H")) queryMode = true;

		StringBuffer smiles = new StringBuffer("");
		int ax[] = JMEUtil.createArray(natoms + 1); // kvoli connections
		for (int i = 1; i <= natoms; i++)
			ax[aa[i]] = i; // ax[i] - kolky sa robi atom i
		for (int i = 1; i <= natoms; i++) {
			atom = aa[i];
			//assert (atom > 0);
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

	// ----------------------------------------------------------------------------
	private void smilesAddAtom(int at, StringBuffer smiles, boolean isAromatic, int slimak[]) {
		String z = "X";
		Atom atom = this.atoms[at];
		int iso = atom.iso;
		int nh = atom.nh;
		int q = atom.q;
		int an = atom.an;

		boolean bracket = false;
		// BB isotop
		// if (q[at] != 0 || iso[at] != 0) bracket = true;
		if (q != 0 || iso != 0)
			bracket = true;
		if (slimak[at] != 0)
			bracket = true;

		// if the atom is mapped, then a bracket for the atom map number is needed
		// int unmarked = -999999999 ; //BB
		// int lmark = unmarked;
		// for (int i=1;i<=nmarked;i++) if (mark[i][0]==atom) {lmark=mark[i][1];break;}
		// //if (lmark > -1) bracket = true;
		// if (lmark != unmarked) bracket = true;

		int map = this.findAtomMapForOutput(at);
		boolean isMapped = map != 0;
		bracket = bracket || isMapped;

		// if (jme.allHs) bracket = true; // May 2020: jme.allH is never set dynamically
		// if (jme.star && backgroundColor[atom] > 0) {bracket = true; lmark = 1;}
		if (moleculeHandlingParameters.mark && atoms[at].backgroundColors[0] > 0) {
			bracket = true;
		}

		switch (an) {
		case JME.AN_B:
			z = "B";
			break;
		case JME.AN_C:
			if (isAromatic)
				z = "c";
			else
				z = "C";
			// tu aromaticitu ???
			break;
		case JME.AN_N:
			if (isAromatic) {
				z = "n";
				if (nh > 0)
					bracket = true;
			} else
				z = "N";
			break;
		case JME.AN_O:
			if (isAromatic)
				z = "o";
			else
				z = "O";
			break;
		case JME.AN_P:
			if (isAromatic) {
				z = "p";
				if (nh > 0)
					bracket = true;
			} else
				z = "P";
			break;
		case JME.AN_S:
			if (isAromatic)
				z = "s";
			else
				z = "S";
			break;
		case JME.AN_SE:
			if (isAromatic)
				z = "se";
			else
				z = "Se";
			bracket = true;
			break;
		case JME.AN_SI:
			z = "Si";
			bracket = true;
			break;
		case JME.AN_F:
			z = "F";
			break;
		case JME.AN_CL:
			z = "Cl";
			break;
		case JME.AN_BR:
			z = "Br";
			break;
		case JME.AN_I:
			z = "I";
			break;
		case JME.AN_H:
			z = "H";
			bracket = true;
			break;
		// BB: remove R
		// case JME.AN_R: z = "R"; bracket = true; break;
		// case JME.AN_R1: z = "R1"; bracket = true; break;
		// case JME.AN_R2: z = "R2"; bracket = true; break;
		// case JME.AN_R3: z = "R3"; bracket = true; break;
		case JME.AN_X:
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

		if (JME.chargedMetalType(an) > 0) {
			z = JME.zlabel[an];
			bracket = true;
		}
		if (an >= JME.AN_R && an <= JME.AN_R_LAST) {
			bracket = true;
			z = JME.zlabel[an]; // R, R1, R2, ...

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
		int b = bondIdentity(atom1, atom2);
		Bond bond = bonds[b];
		// what is the difference between btype[b] and bonds[b].bondType ?
		// btype is computed locally and contains information about aromaticity
		// idea: for the bond type, use bits inside an int: type & AROMATIC
		if (btype[b] != AROMATIC && bond.isDouble())
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
		} else if (btype[b] == AROMATIC && queryMode)
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
		int ax[] = JMEUtil.createArray(natoms + 1);
		for (int at = 1; at <= natoms; at++)
			ax[aa[at]] = at; // ax[i] - kolky sa robi atom i

		// E,Z bonds
		// nesmie ist v poradi od 1 do nbonds, lebo si poprepisuje slash[]
		// preto to je robene v poradi kreacie smilesu
		boolean doneEZ[] = JMEUtil.createBArray(nbonds + 1); // kvoli connections
		for (int i = 1; i <= natoms; i++) {
			int atom1 = aa[i]; // aa[] nie ax[] !
			int atom2 = parent[atom1];
			int bi = bondIdentity(atom1, atom2);
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
				int bi = bondIdentity(at, v(at)[neighbor]);
				if (btype[bi] == AROMATIC)
					continue atom_loop;
				if (bonds[bi].bondType == SINGLE && upDownBond(bi, at) != 0)
					nstereo++;
				if (bonds[bi].bondType == DOUBLE)
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
		int ref[] = JMEUtil.createArray(4); // 0 - atom z ktoreho sa pozera
		int refx[] = JMEUtil.createArray(4); // ci je up, dpwn, 0

		identifyNeighbors(atom, ax, parent, con1, con2, nconnections, ref);

		// reference bonds + help variables
		int nup = 0, ndown = 0;
		int up = 0, down = 0, marked = 0, nonmarked = 0; // jediny markovany / nem.
		for (int i = 0; i < 4; i++) {
			if (ref[i] <= 0)
				continue;
			int bi = bondIdentity(atom, ref[i]);
			refx[i] = upDownBond(bi, atom);
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
		int t[] = JMEUtil.createArray(4); // rohy tetraedra, transformacia t[] urci @ || @@
		int stereoRef = 0; // ci t[0] je up || down
		if (nv(atom) == 3) {
			if ((nup == 1 && ndown == 1) || (nstereo == 3 && nup > 0 && ndown > 0)) {
				jme.info("Error in C3H stereospecification !");
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
				int box[] = JMEUtil.createArray(4); // up,down info for ox[] atoms
				for (int i = 0; i < 4; i++) {
					int bi = bondIdentity(atom, ox[i]);
					box[i] = upDownBond(bi, atom);
				}

				if (nstereo == 4) {
					if (nup == 0 || ndown == 0) {
						jme.info("Error in C4 stereospecification !");
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
							jme.info("Error in C4 stereospecification ! 2/0r");
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

		JMEUtil.stereoTransformation(t, ref);

		if (t[2] == ref[2])
			slimak[atom] = 1;
		else if (t[2] == ref[3])
			slimak[atom] = -1;
		else
			jme.info("Error in stereoprocessing ! - t30");

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
		int ox[] = JMEUtil.createArray(4);
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
		int p[] = JMEUtil.createArray(4);
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

		double sin[] = JMEUtil.createDArray(4), cos[] = JMEUtil.createDArray(4);
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
			int bi = bondIdentity(atom1, atomx);
			if (bondType(bi) == DOUBLE)
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
		if (bondType(bond) != DOUBLE || btype[bond] == AROMATIC)
			return;

		// minimum ring size for having bond that can be either trans or cis in a ring
		if (bondMinimumRingSize[bond] > 0 && bondMinimumRingSize[bond] <= 7)
			return;
		// bondMinimumRingSize[bond] means no ring

		// if (!(bonds[bond].stereo==EZ || (jme.autoez && !isRingBond[bond])))
		// return;
		if (!(bonds[bond].stereo == EZ || this.smilesParameters.autoez))
			return; // BB October 2016: stereo for large rings is missing

		// BB Feb 2017
		if (bonds[bond].stereo == EZ)
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
		int bi = bondIdentity(atom1, ref11);
		if (slashBond[bi] != 0) {
			ref1 = ref11;
		} else if (bondType(bi) == SINGLE && btype[bi] != AROMATIC)
			ref1 = ref11;

		if (ref1 == 0 && ref12 > 0) { // BB added ref12>0
			bi = bondIdentity(atom1, ref12);
			if (slashBond[bi] != 0)
				ref1 = ref12;
			else if (bondType(bi) == SINGLE && btype[bi] != AROMATIC)
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
		bi = bondIdentity(atom2, ref21);
		if (bondType(bi) == SINGLE && btype[bi] != AROMATIC && slashBond[bi] == 0)
			ref2 = ref21;
		if (ref2 == 0 && ref22 > 0) { // BB added ref22>0
			bi = bondIdentity(atom2, ref22);
			if (bondType(bi) == SINGLE && btype[bi] != AROMATIC)
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
			jme.info("Not unique E/Z geometry !");
			return;
		}
		int b1 = bondIdentity(ref1, atom1);
		int b2 = bondIdentity(ref2, atom2);
		int newSlash = 1;
		if (slashBond[b1] == 0) {
			// ceknut, ci nove stereo neprotireci uz exisujucej na ref1
			for (int j = 1; j <= nv(ref1); j++) {
				int atomx = v(ref1)[j];
				if (atomx == atom1)
					continue;
				bi = bondIdentity(ref1, atomx);
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
	private int upDownBond(int bond, int atom) {
		// zistuje, ci stereo bond je relevantna k atomu (ci na nom je hrot vazby)
		// ci ide hore 1, dolu -1, alebo nie je stereo
		// pri UP a DOWN je hrot na va[bond], pri XUP a XDOWN na vb[bond]
		int sb = bonds[bond].stereo;
		if (sb < 1 || sb > 4)
			return 0;
		if (sb == UP && bonds[bond].va == atom)
			return 1;
		if (sb == DOWN && bonds[bond].va == atom)
			return -1;
		if (sb == XUP && bonds[bond].vb == atom)
			return 1;
		if (sb == XDOWN && bonds[bond].vb == atom)
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
		cumuleneAtoms = JMEUtil.createArray(natoms + 1);
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

				int bi = bondIdentity(currentCumuleneAtom, atomx);
				if (bonds[bi].bondType == DOUBLE && btype[bi] != AROMATIC) {
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
		int ref11x = ref11 > 0 ? upDownBond(bondIdentity(start, ref11), start) : 0;
		int ref12x = ref12 > 0 ? upDownBond(bondIdentity(start, ref12), start) : 0;
		int ref21x = ref21 > 0 ? upDownBond(bondIdentity(end, ref21), end) : 0;
		int ref22x = ref22 > 0 ? upDownBond(bondIdentity(end, ref22), end) : 0;

		if (Math.abs(ref11x + ref12x) > 1 || ref21x != 0 || ref22x != 0) {
			jme.info("Bad stereoinfo on allene !");
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

	// ----------------------------------------------------------------------------
	/**
	 * Crate a represenation in JME format
	 * 
	 * @return
	 */
	public String createJME(Box boundingBox) {
		return createJME(false, boundingBox);
	}

	public String createJME(boolean jmeh, Box boundingBox) {
		// cislovanie nezavisle od smilesu
		// parameter jmeh urcuje pridavanie H
		String s = "" + natoms + " " + nbonds;

		this.transformAtomCoordinatesForOutput(boundingBox);

		for (int i = 1; i <= natoms; i++) {
			Atom atom = this.atoms[i];
			int iso = atom.iso;
			String z = "";

			// BB iso
			if (iso != 0) {
				z += iso;
			}

			z += getAtomLabel(i);
			// if (jme.jmeh && an[i] != JME.AN_C && nh[i] > 0) {
			// if (jme.jmeh && atoms[i].nh > 0) { // aj pre C
			if ((jmeh || getValenceForMolOutput(i) > 0) && atoms[i].nh > 0) { // aj pre C
				z += "H";
				if (atoms[i].nh > 1)
					z += atoms[i].nh;
			}

			// naboje
			if (q(i) != 0) {
				if (q(i) > 0)
					z += "+";
				else
					z += "-";
				if (Math.abs(q(i)) > 1)
					z += Math.abs(q(i));
			}

			// mapping
			// int lmark = -1;
			// for (int j=1;j<=nmarked;j++) if(mark[j][0]==i) {lmark=mark[j][1]; break;}
			// if (jme.star && backgroundColor[i] > 0) lmark = 1;
			// if (lmark > -1) z+= ":" + lmark;

			int map = this.findAtomMapForOutput(i);
			if (map != 0)
				z += ":" + map;

			// inverts y coordinate
			s += " " + z + " " + JMEUtil.fformat(xo(i), 0, 2) + " " + JMEUtil.fformat(yo(i), 0, 2);
		}
		for (int i = 1; i <= nbonds; i++) {
			int a1 = bonds[i].va, a2 = bonds[i].vb, nas = bonds[i].bondType;
			int stereo = bonds[i].stereo;
			if (stereo == UP)
				nas = -1;
			else if (stereo == DOWN)
				nas = -2;
			else if (stereo == XUP) {
				nas = -1;
				int d = a1;
				a1 = a2;
				a2 = d;
			} else if (stereo == XDOWN) {
				nas = -2;
				int d = a1;
				a1 = a2;
				a2 = d;
			} else if (stereo == EZ) {
				nas = -5;
			} // 2006.09
			// query (typ is stored in stereob)
			if (bonds[i].bondType == QUERY)
				nas = stereo;
			s += " " + a1 + " " + a2 + " " + nas;
		}
		return s;
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
			map = atom.getMapOrMark(!moleculeHandlingParameters.mark);
		}

		return map;
	}

	protected int findAtomChargeForOutput(int atomIndex) {
		int charge = 0;
		if (atomIndex > 0 && atomIndex <= this.nAtoms()) {
			Atom atom = this.atoms[atomIndex];
			charge = atom.q();
		}

		return charge;
	}

	public boolean hasMarkedAtom() {
		for (int at = 1; at <= natoms; at++) {
			if (findAtomMapForOutput(at) > 0) {
				return true;
			}
		}

		return false;
	}

	protected int setAtomMapFromInput(int atomIndex, int map) {
		if (atomIndex > 0 && atomIndex <= this.nAtoms()) {
			Atom atom = this.atoms[atomIndex];

			atom.setMapOrMark(map, !moleculeHandlingParameters.mark);
		}

		return map;
	}

	// --------------------------------------------------------------------------
	/**
	 * Create a MOL or RXN. The paprameter header is usually the SMILES
	 * 
	 * @param header
	 * @return
	 */
	String createMolFile(String header) {
		return this.createMolFile(header, true);
	}

	public boolean canBeChiral() {

		for (int i = 1; i <= nbonds; i++) {
			if (bonds[i].bondType == SINGLE && bonds[i].stereo > 0) {
				return true;
			}
		}

		return false;
	}

	protected int mdlChiralFlag() {
		return this.getChiralFlag() && this.canBeChiral() ? 1 : 0;
	}

	protected String mdlHeaderLines(String header, boolean stampDate, boolean isV3000) {
		int writeChiralFlag = isV3000 ? 0 : mdlChiralFlag(); // for V3000, the chiral flag is set in the COUNTS line

		// BH 2023.15 not allowing first line to be blank or spaces only, as that can be
		// clipped improperly
		String s = (header != null && (header = header.trim()).length() > 0 ? header : "_");
		if (s.length() > 79)
			s = s.substring(0, 76) + "...";
		s += JME.separator;
		// since 2006.01 added one space to header line (two newlines causes problems in
		// tokenizer)

		String versiondate = "JME" + JME.version + " " + new Date();

		// the 2D/3D dimension is critical and required
		// if(stampDate) {
		s += JMEUtil.getSDFDateLine("JME" + JME.version) + JME.separator;
//		} else {
//			s += JME.separator;
//		}
		s += versiondate + JME.separator;
		// counts line
		s += JMEUtil.iformat(isV3000 ? 0 : natoms, 3) + JMEUtil.iformat(isV3000 ? 0 : nbonds, 3);
		s += "  0  0" + JMEUtil.iformat(writeChiralFlag, 3) + "  0  0  0  0  0999 " + (isV3000 ? "V3000" : "V2000")
				+ JME.separator;

		return s;

	}

	/**
	 * Generate a string for the bond. For v3000 the M 30 and the bond index must be
	 * added
	 * 
	 * @param bond
	 * @param isV3000
	 * @return
	 */
	protected String mdlStereoBond(Bond bond, boolean isV3000) {

		int bondType = bond.bondType;
		int a1 = bond.va;
		int a2 = bond.vb;
		// int nas = bondType; //whatis nas?
		// if (isSingle(i)) nas=1; else if (isDouble(i)) nas=2;
		int stereo = 0;

		/*
		 * V2000 Single bonds: 0 = not stereo, 1 = Up, 4 = Either, 6 = Down, Double
		 * bonds: 0 = Use x-, y-, z-coords from atom block to determine cis or trans, 3
		 * = Cis or trans (either) double bond
		 * 
		 */
		if (bondType == SINGLE || bondType == COORDINATION) {
			switch (bond.stereo) {
			case UP:
			case XUP:
				stereo = 1;
				break;
			case DOWN:
			case XDOWN:
				stereo = 6;
				break;
			case EITHER:
			case XEITHER:
				stereo = 4;
			}
			// swap atom labels if needed
			switch (bond.stereo) {
			case XUP:
			case XDOWN:
			case XEITHER:
				int t = a1;
				a1 = a2;
				a2 = t;

			}
		}
		/*
		 * V3000 0 = none (default) 1 = up 2 = either 3 = down
		 * 
		 */
		if (bondType == DOUBLE && bond.stereo == EZ) {
			stereo = 3; // 3 = Cis or trans (either) double bond
		}

		// convert v2000 to v3000 conventions
		if (isV3000) {
			switch (stereo) {
			case 6:
				stereo = 3;
				break;
			case 4:
			case 3:
				stereo = 2;

			}
		}

		if (bondType == COORDINATION) {
			bondType = 8; // MDL CT does not support coordination, convention used here is the same as in
							// other software, officialy 8 means any
		} else if (bondType == QUERY || bondType == AROMATIC) { // Not sure about this convention, see test case
																// testReadRXNwithTwoAgents
			bondType = 4;
		}
		String bonds;
		if (isV3000) {
			bonds = bondType + " " + a1 + " " + a2;
			if (stereo != 0)
				bonds += " CFG=" + stereo;
		} else {
			bonds = JMEUtil.iformat(a1, 3) + JMEUtil.iformat(a2, 3) + JMEUtil.iformat(bondType, 3) + JMEUtil.iformat(stereo, 3) + "  0  0  0";
		}

		return bonds;
	}

	/**
	 * Center and scale back the bond length to about 1.4 because JSME has rescaled
	 * the bond to RBOND (internalBonsScaling) When reading a MOL, the y coordinated
	 * were inverted, thus invert back y coordinate to match ISISDraw coord system
	 * 
	 * if the argument is null, then the boundingBox of the molecule will be
	 * computed. Using this argument might be useful for a JMEmolList for which each
	 * molecules are recentered globally, for the ensemble
	 */
	public void transformAtomCoordinatesForOutput(Box boundingBox) {

		// atoms
		double scale = 1.4 / RBOND; // ??? co je standard scale ???
		// where is this coming from?

		if (boundingBox == null) {
			boundingBox = this.computeCoordinate2DboundingBox();
		}

		if (boundingBox == null) { // h appens when they are no atoms
			return;
		}

		double ymax = boundingBox.y + boundingBox.height;
		double xmin = boundingBox.x;

		// this is used for the test suite or non GUI applications
		if (this.moleculeHandlingParameters.keepSameCoordinatesForOutput) {
			xmin = 0.0;
			ymax = 0.0;
			scale = 1.0;
		}

		for (int i = 1; i <= natoms; i++) {
			Atom atom = atoms[i];
			double x = x(i);
			double y = y(i);

			x = (x(i) - xmin) * scale; // center and scale
			y = (ymax - y(i)) * scale; // inverts y coordinate to match ISISDraw coord system

			atom.xo = x;
			atom.yo = y;
		}

	}

	/**
	 * Create a MOL or RXN. The parameter header is usually the SMILES stampdate is
	 * set to false for the tests
	 * 
	 * @param header
	 * @return
	 */
	public String createMolFile(String header, boolean stampDate) {
		// LP complained about this - v300 does not do it
		// if (natoms == 0) return ""; // 2008.12

		String s = mdlHeaderLines(header, stampDate, false);
		// this.moleculeHandlingParameters;

		// atoms
		// where is this coming from?

		this.transformAtomCoordinatesForOutput(null);

		for (int i = 1; i <= natoms; i++) {
			Atom atom = atoms[i];
			double x = atom.xo;
			double y = atom.yo;

			s += JMEUtil.fformat(x, 10, 4) + JMEUtil.fformat(y, 10, 4) + JMEUtil.fformat(0.0, 10, 4);
			String z = getAtomLabel(i);

			if (z.length() == 1)
				z += "  ";
			else if (z.length() == 2)
				z += " ";
			else if (z.length() > 3)
				z = "Q  "; // query ???
			s += " " + z;

			// isotope, charge
			int charge = 0;
			if (q(i) > 0 && q(i) < 4)
				charge = 4 - q(i);
			else if (q(i) < 0 && q(i) > -4)
				charge = 4 - q(i);
			// BB
			int deltaIsotop = 0;
			if (atom.iso != 0) {
				int delta = AtomicElements.getDeltaIsotopicMassOfElement(getAtomLabel(i), this.atoms[i].iso);
				if (delta >= -3 && delta <= 4) {
					deltaIsotop = delta;
				}
			}

			// BB: valence field vv
			int vv = 0; // no markings

			// if(atom.nh>0) {
			// vv = atom.nh+atom.nv; //ChemWriterr OK
			// } else {
			// //metal like [Al]
			// if(q(i) == 0 && JME.chargedMetalType(atom.an) !=0) {
			// // Datawarrior does not recognise it correctly
			// //ChemWriter shows an error
			// //Marvin JS, ChemDoodle ignores this field
			// vv = 15; //explicit 0 valence
			// }
			// }
			vv = getValenceForMolOutput(i);

			z = JMEUtil.iformat(deltaIsotop, 2) + JMEUtil.iformat(charge, 3) + "  0" + JMEUtil.iformat(vv, 3) + "  0  0  0  0  0";

			// adding atom mapping (if any)
			// int lmark = -1;
			// for (int j=1;j<=nmarked;j++)
			// if(mark[j][0]==i) {
			// lmark=mark[j][1];
			// break;
			// }

			int map = this.findAtomMapForOutput(i);
			// BB: star marking: code almost identical to createJME
			// if (lmark == -1 && jme!=null&& jme.star && backgroundColor[i] > 0) lmark = 1;
			// if (!atom.isMapped() && jme!=null&& jme.star && backgroundColor[i] > 0)
			// lmark = 1;

			// if (lmark > -1) z += iformat(lmark,3); else z += " 0";
			z += JMEUtil.iformat(map, 3);
			s += z + "  0  0" + JME.separator;

		}
		// bonds
		for (int i = 1; i <= nbonds; i++) {
			s += mdlStereoBond(this.bonds[i], false) + JME.separator;
			;
		}

		// charges on standard atoms
		for (int i = 1; i <= natoms; i++) {
			if (q(i) != 0) {
				s += "M  CHG  1" + JMEUtil.iformat(i, 4) + JMEUtil.iformat(q(i), 4) + JME.separator;
			}

			// ISO BB
			if (this.atoms[i].iso != 0) {
				s += "M  ISO  1" + JMEUtil.iformat(i, 4) + JMEUtil.iformat(this.atoms[i].iso, 4) + JME.separator;
			}
		}

		// radical (X atoms)
		/*
		 * for (int i=1;i<=nradicals;i++) { if (an[i] == JME.AN_X) { s += "M  RAD  1" +
		 * iformat(radical[i],4) + iformat(2,4) + JME.separator; } }
		 */

		s += "M  END" + JME.separator;
		return s;
	}

	int getValenceForMolOutput(int at) {
		// Provide valence count only for metals that are handled yet by JME

		int val = 0;
		Atom atom = atoms[at];

		if (atom.nh > 0 && JME.chargedMetalType(atom.an) != 0) {
			val = atom.nh + atom.nv;
		}

		return val;
	}

	// ----------------------------------------------------------------------------
	String createExtendedMolFile2(String smiles) {
		return createExtendedMolFile(smiles, true);
	}

	// ----------------------------------------------------------------------------
	// Molfile V3000 - 2006.09
	public String createExtendedMolFile(String header, boolean stampDate) {

		// int nradicals = 0;
		// int[] radical = JMEUtil.createArray(natoms+1);
		// finding whether molecule is chiral

		String s = mdlHeaderLines(header, stampDate, true);

		// int chiral = 0;
		// for (int i=1;i<=nbonds;i++) if (bonds[i].stereo != 0) {chiral = 1; break;}

		String mv30 = "M  V30 ";
		// header
		s += mv30 + "BEGIN CTAB" + JME.separator;
		// at the end should be chiral flag 1 or 0
		s += mv30 + "COUNTS " + natoms + " " + nbonds + " 0 0 " + mdlChiralFlag() + JME.separator;
		s += mv30 + "BEGIN ATOM" + JME.separator;
		// atoms
		this.transformAtomCoordinatesForOutput(null);

		for (int i = 1; i <= natoms; i++) {
			s += mv30;
			// inverts y coordinate to match ISISDraw coord system
			String z = getAtomLabel(i);
			s += i + " " + z;

			// int m = 0;
			// int lmark = -1;
			// for (int j=1;j<=nmarked;j++) if(mark[j][0]==i) {lmark=mark[j][1]; break;}
			// if (lmark > -1) m = lmark; // ignores 0 mark
			int m = this.findAtomMapForOutput(i);
			;

			s += " " + JMEUtil.fformat(xo(i), 0, 4) + " " + JMEUtil.fformat(yo(i), 0, 4) + " " + JMEUtil.fformat(0.0, 0, 4) + " " + m;
			if (q(i) != 0)
				s += " CHG=" + q(i);
			// JME curremtly ignoring isotopes & radicals
			// if (q[i] != 0) s+ " MASS="+q[i];
			if (atoms[i].iso > 0) { // MAy 2020
				s += " MASS=" + atoms[i].iso;
			}

			int val = getValenceForMolOutput(i);
			if (val != 0)
				s += " VAL=" + val;

			s += JME.separator;

			// TODO: isotopoe handling?
		}
		s += mv30 + "END ATOM" + JME.separator;
		s += mv30 + "BEGIN BOND" + JME.separator;
		// bonds
		for (int i = 1; i <= nbonds; i++) {
			s += mv30 + i + " " + mdlStereoBond(this.bonds[i], true) + JME.separator;
		}
		s += mv30 + "END BOND" + JME.separator;

		// stereo collections
		// MDLV30/STEABS /STERACn /STERELn
		ArrayList<Integer> abs = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> orlists = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> mixlists = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < 10; i++) {
			orlists.add(null);
			mixlists.add(null);
		}
		for (int i = 1; i <= natoms; i++) {
			String atag = atoms[i].atag;
			if (atag == null || atag.length() == 0)
				continue;
			if (atag.equals("abs"))
				abs.add(new Integer(i));
			else if (atag.startsWith("mix")) {
				int n = Integer.parseInt(atag.substring(3));
				ArrayList<Integer> o = (mixlists.size() > n ? mixlists.get(n) : null);
				ArrayList<Integer> l = (o == null ? new ArrayList<Integer>() : o);
				l.add(new Integer(i));
				mixlists.set(n, l);
			} else if (atag.startsWith("or")) {
				int n = Integer.parseInt(atag.substring(2));
				ArrayList<Integer> o = (orlists.size() > n ? orlists.get(n) : null);
				ArrayList<Integer> l = (o == null ? new ArrayList<Integer>() : o);
				l.add(new Integer(i));
				orlists.set(n, l);
			}
		}
		s += addCollection("MDLV30/STEABS", abs, mv30);
		if (orlists.size() > 0)
			for (int i = 1; i < orlists.size(); i++)
				s += addCollection("MDLV30/STEREL" + i, orlists.get(i), mv30);
		if (mixlists.size() > 0)
			for (int i = 1; i < mixlists.size(); i++)
				s += addCollection("MDLV30/STERAC" + i, mixlists.get(i), mv30);

		s += mv30 + "END CTAB" + JME.separator;
		s += "M  END" + JME.separator;
		return s;
	}

	// ----------------------------------------------------------------------------
	String addCollection(String name, ArrayList<Integer> list, String mv30) {
		if (list == null || list.size() == 0)
			return "";
		String s = "";
		s += mv30 + "BEGIN COLLECTION" + JME.separator;
		s += mv30 + name + " [ATOMS=(" + list.size();
		for (Iterator<Integer> i = list.iterator(); i.hasNext();)
			s += " " + i.next();
		s += ")]" + JME.separator;
		s += mv30 + "END COLLECTION" + JME.separator;
		return s;
	}

	// ----------------------------------------------------------------------------
	public String getAtomLabel(int i) {
		String z = JME.zlabel[an(i)]; // aj C kvoli ramcekom
		if (an(i) == JME.AN_X)
			z = atoms[i].label;
		return z;
	}

	boolean hasHydrogen() {
		for (int i = natoms; i >= 1; i--) {
			if (an(i) == JME.AN_H) {
				return true;
			}
		}

		return false;

	}

	public Bond getBond(int bondIndex) {
		return this.bonds[bondIndex];
	}

	// ----------------------------------------------------------------------------
	int bondIdentity(int atom1, int atom2) {
		for (int i = 1; i <= nbonds; i++) {
			Bond bond = bonds[i];
			int va = bond.va;
			int vb = bond.vb;
			if (va == atom1 && vb == atom2 || va == atom2 && vb == atom1)
				return i;
		}
		return 0;
	}

	// ----------------------------------------------------------------------------
	/**
	 * 
	 * @param atom1
	 * @param atom2
	 * @return null if no bond were found
	 */
	Bond bondIdentityBond(int atom1, int atom2) {
		int bondIndex = this.bondIdentity(atom1, atom2);
		//assert (bondIndex > 0 && bondIndex <= nbonds);

		return this.bonds[bondIndex];
	}

	// ----------------------------------------------------------------------------
	boolean isSingle(int bond) {
		return bonds[bond].isSingle();

	}

	// ----------------------------------------------------------------------------
	private boolean isDouble(int bond) {
		return this.bonds[bond].isDouble();
	}

	// ----------------------------------------------------------------------------
	// BB
	int findBondTypeOfTwoAtomMaps(int m1, int m2) {
		return 0;
	}

	void cleanAfterChanged(boolean polarNitro) {
		valenceState();
		cleanPolarBonds(polarNitro); // TODO: need parameter polarnitro
	}

	// ----------------------------------------------------------------------------
	void valenceState() {
		// System.out.println("@@@@ valence state");
		for (int i = 1; i <= natoms; i++) {
			atoms[i].sbo = sumBondOrders(i);
			atomValenceState(i);
		}
	}

	// ----------------------------------------------------------------------------
	void atomValenceState(int i) {
		Atom atom = atoms[i];
		int sbo = atom.sbo; // sum bond orders to nonhydrogen atoms

		if (sbo == -1) {
			atom.nh = 0;
			return;
		} // query bond
		switch (atom.an) {
		// added 2005.02
		case JME.AN_H: // -[H+]- allowed (as in boranes)
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
		case JME.AN_B: // BH2+ BH3 BH4- BH5 BH6+ BH7++
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
		case JME.AN_C:
		case JME.AN_SI: // Si since 2007.02
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
		case JME.AN_N:
		case JME.AN_P:
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
		case JME.AN_O: // -[O-] -O- =O -[O+]< >[O2+]< ...
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
		case JME.AN_S:
		case JME.AN_SE: // Se rozoznava kvoli aromaticite
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
		case JME.AN_F:
		case JME.AN_CL:
		case JME.AN_BR:
		case JME.AN_I:
			if (sbo >= 1)
				Q(i, sbo - 1);
			atom.nh = 1 - sbo + q(i);
			// potialto org, odteraz zmena kvoli superhalogenom
			if (sbo > 2) {
				Q(i, 0);
				atom.nh = 0;
			}
			break;
		case JME.AN_R:
		case JME.AN_X:
			atom.nh = 0;
			break;
		}

		// BB change metal 1 only if common situation
		// sbo cannot change, q might be a user request, nh is computed

		int maxMetalCharge = JME.chargedMetalType(atom.an);
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
		if (jme != null && jme.relativeStereo && atag(i) != null && atag(i).length() > 0) {
			boolean ok = false;
			for (int j = 1; j <= nv(i); j++) {
				int bond = bondIdentity(i, v(i)[j]);
				if (i == bonds[bond].va && (bonds[bond].stereo == UP || bonds[bond].stereo == DOWN)) {
					ok = true;
					break;
				}
				if (i == bonds[bond].vb && (bonds[bond].stereo == XUP || bonds[bond].stereo == XDOWN)) {
					ok = true;
					break;
				}
			}
			if (!ok)
				atoms[i].atag = "";
		}
	}

	// ----------------------------------------------------------------------------
	/**
	 * Change charge/Hydrogen count
	 * 
	 * @param atom
	 * @param type is always 0 in JME: means toggle the charge/hydrogen count
	 * @return
	 */
	boolean changeCharge(int atom, int type) {
		// click with +/- on atom
		// 2002.05 --- pridana moznost C+
		// 2005.03 --- pridany type, moze byt 0 1 -1
		String np = "Charge change not possible on ";

		// for webme
		if (type == 1) {
			incrQ(atom, 1);
			return true;
		} else if (type == -1) {
			incrQ(atom, -1);
			return true;
		}

		int startCharge = q(atom);
		int startNH = atoms[atom].nh;
		// standard jme behaviour
		// int sbo = sumBondOrders(atom);
		int sbo = getSumOfBondOrder(atom);
		if (sbo == -1) { // query
			if (type == 0) {
				if (q(atom) == 0)
					Q(atom, 1);
				else if (q(atom) == 1)
					Q(atom, -1);
				else if (q(atom) == -1)
					Q(atom, 0);
			}
		}
		switch (an(atom)) {
		case JME.AN_H:
			// Sept 2016: change charge cycle: 0 -> 1 -> -1 -> 0
			if (sbo == 0) {
				if (q(atom) == 0)
					Q(atom, 1);
				else if (q(atom) == 1)
					Q(atom, -1);
				else {
					Q(atom, 0);
				}
			}

			break;

		case JME.AN_B:
			if (sbo > 2)
				jme.info(np + "this boron !");
			if (q(atom) == 0)
				Q(atom, 1);
			else if (q(atom) == 1)
				Q(atom, 0);
			break;
		case JME.AN_C:
			// case JME.AN_SI:
			if (sbo > 3)
				jme.info(np + "this carbon !");
			else if (sbo < 4) {
				if (q(atom) == 0)
					Q(atom, -1);
				else if (q(atom) == -1)
					Q(atom, 1);
				else if (q(atom) == 1)
					Q(atom, 0);
			}
			break;
		case JME.AN_N:
		case JME.AN_P:
			if (sbo > 3)
				jme.info(np + "multibonded N or P !");
			else if (sbo == 3 && q(atom) == 0)
				Q(atom, 1);
			else if (sbo == 3 && q(atom) == 1)
				Q(atom, 0);
			else if (sbo < 3 && q(atom) == 0)
				Q(atom, 1);
			else if (sbo < 3 && q(atom) == 1)
				Q(atom, -1);
			else if (sbo < 3 && q(atom) == -1)
				Q(atom, 0);
			break;
		case JME.AN_O: // -[O-] -O- =O -[O+]< >[O2+]< ...
		case JME.AN_S: // asi sa na multivalent vykaslat
		case JME.AN_SE:
			if (sbo > 2)
				jme.info(np + "multibonded " + JME.zlabel[an(atom)] + " !");
			else if (sbo == 2 && q(atom) == 0)
				Q(atom, 1);
			else if (sbo == 2 && q(atom) == 1)
				Q(atom, 0);
			else if (sbo < 2 && q(atom) == 0)
				Q(atom, -1);
			else if (sbo < 2 && q(atom) == -1)
				Q(atom, 1);
			else if (sbo < 2 && q(atom) == 1)
				Q(atom, 0);
			break;
		case JME.AN_F:
		case JME.AN_CL:
		case JME.AN_BR:
		case JME.AN_I:
			if (sbo == 0 && q(atom) == 0)
				Q(atom, -1);
			else if (sbo == 0 && q(atom) == -1)
				Q(atom, 0);
			else
				jme.info(np + "the halogen !");
			break;
		case JME.AN_X:
			jme.info("Use X button to change charge on the X atom !");
			break;
		}

		if (JME.chargedMetalType(an(atom)) > 0)
			if (!this.toggleChargeAndHydrogenCountOfMetalAtom(atoms[atom], sbo))
				jme.info(np + JME.zlabel[an(atom)]);

		return startCharge != q(atom) || startNH != atoms[atom].nh;
	}

	/**
	 * Change the charge and hydrogen count of a metal atom e.g.Na => Na+, Ca =>
	 * Ca++ works only for atom with only two oxydation states: 0 and >0 like Na
	 * 
	 * @param atom
	 * @param sbo  : sum of bond order of the atom
	 * @return
	 */
	boolean toggleChargeAndHydrogenCountOfMetalAtom(Atom atom, int sbo) {
		/*
		 * See also: atomDelete increases H count valenceState can decrease the charge
		 * and H count
		 */
		boolean changed = false;
		int maxMetalCharge = JME.chargedMetalType(atom.an); // 1 for Na, 2 for Ca, 3 for Al
		if (maxMetalCharge > 0) {
			int maxChargeIncrease = maxMetalCharge - sbo;
			if (maxChargeIncrease > 0) {
				int q = atom.q;
				int nh = atom.nh;

				if (q + nh < maxChargeIncrease) { // Na=>Na+, Ca+ => Ca++, Al+ => Al3+,
					q += maxChargeIncrease - nh;
				} else if (q + nh == maxChargeIncrease) {
					if (q == maxChargeIncrease) {
						q = 0;
						nh = maxChargeIncrease;
					} // Na+ => NaH , Ca++ => CaH2, Ca+H => CaH2
					else { // nh == maxChargeIncrease
						q = 0;
						nh = 0; // NaH => Na, CaH2 = > Ca
						if (sbo == 0)
							jme.info("Metallic " + JME.zlabel[atom.an]);
					}

				}
				changed = (atom.q != q || atom.nh != nh);
				atom.q = q;
				atom.nh = nh;

			}
		}
		return changed;
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
			Bond bond = bondIdentityBond(atom, v(atom)[i]);
			if (bond == null) {
				//assert (bond != null);
			}
			if (bond.isSingle())
				sbo += 1;
			else if (bond.isDouble())
				sbo += 2;
			else if (bond.bondType == TRIPLE)
				sbo += 3;
			else if (bond.bondType == QUERY)
				return -1; // query bond
			// else System.out.println("bond "+bond+" inconsistent info!"+bondType[bond]);
		}

		return sbo;
	}

	public boolean markAtom(int newMark) {
		// Mark the touched atom

		// star marking of a part of a molecules
		if (moleculeHandlingParameters.mark) {
			if (jme.markOnly1) { // only one atom at a time can be marked
				for (int at = 1; at <= natoms; at++) {
					if (at != touchedAtom) {
						this.atoms[at].resetMark(); // if not done, the atoms with maps will still stay blue
					}
				}
			}
			if (newMark != this.atoms[touchedAtom].getMark()) {
				this.atoms[touchedAtom].setMark(newMark);
				return true;
			} else { // same color requested twice: toggle
				this.atoms[touchedAtom].resetMark();
				return false;
			}
		}

		boolean hasBeenMarked;

		// normal number marking
		// checking whether this atom is marked
		Atom touchedAtomObject = this.atoms[touchedAtom];
		if (newMark <= 0) {
			// removing this marking
			touchedAtomObject.resetMap();
			hasBeenMarked = false;
		} else {
			// replacing the current mark
			// if (!jme.mouseShift) { //with shift, the same map is used
			// maxMark++;
			// }
			hasBeenMarked = newMark != touchedAtomObject.getMap(); // BB new 2020
			touchedAtomObject.setMap(newMark);
			hasBeenMarked = true;
		}
		return hasBeenMarked;

	}

	public boolean markBond(int newMark) {
		// duplicated logic with markAtom

		// star marking of a part of a molecules
		if (moleculeHandlingParameters.mark) {
			// doColoring = -1;
			// //assert(activeMarker == this.moleculeHandlingParameters.markerMultiColor);
			if (jme.markOnly1) { // only one atom at a time can be marked
				for (int at = 1; at <= nbonds; at++) {
					if (at != touchedBond) {
						this.bonds[at].resetMark(); // if not done, the atoms with maps will still stay blue
					}

				}
			}

			if (newMark != this.bonds[touchedBond].getMark()) {
				this.bonds[touchedBond].setMark(newMark);

				return true;
			} else { // same color requested twice: toggle
				this.bonds[touchedBond].resetMark();
				return false;
			}

		}

		return false;
	}

	// Remove this code
//	public boolean toggleMarkTouchedBond() {
//
//		
//
//		if (moleculeHandlingParameters.mark && this.touchedBond > 0 && this.touchedBond <= this.nBonds()) {
//			//doColoring = -1;
//
//			if(jme.markOnly1) { //only one bond at a time can be marked
//				for(int i = 1; i <= nbonds; i ++) {
//					if(i != touchedBond) {
//						this.bonds[i].resetMark(); //if not done, the atoms with maps will still stay blue
//					}
//
//				}
//			}
//			Bond touchedBond = this.bonds[this.touchedBond];
//			
//			if ( ! touchedBond.isMarked()) {
//				touchedBond.setMark(true);
//				BGC(touchedBond, 4); //TODO : uses a color enum
//		
//				return true;
//			}
//			else {
//				touchedBond.setMark(false);
//				BGC(touchedBond, 0);
//				return false;
//			}
//		}
//		
//		return false;
//
//
//	}

	// ----------------------------------------------------------------------------

	/**
	 * Warning: side effect: the order of the atoms is changed turned off
	 */
	public void numberAtoms() {
		nmarked = 0; // vymaze marky
		maxMark = 0; // vymaze marky

		for (int i = 1; i <= natoms; i++) {
			this.atoms[i].setMap(i);
		}
	}

	// ----------------------------------------------------------------------------
	@Override
	public void XY(double x, double y) {
	}

}
// ----------------------------------------------------------------------------
