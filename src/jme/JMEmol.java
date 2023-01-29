package jme;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolAdapterAtomIterator;
import org.jmol.api.JmolAdapterBondIterator;
import org.jmol.util.Edge;
import org.jmol.util.Elements;

import javajs.util.P3d;
import jme.core.Atom;
import jme.core.AtomBondCommon;
import jme.core.AtomicElements;
import jme.core.Bond;
import jme.core.Box;
import jme.core.JMECore;
import jme.core.JMESmiles;
import jme.gui.Actions;
import jme.gui.AtomDisplayLabel;
import jme.gui.GUI;
import jme.io.JMEWriter;
import jme.ocl.OclAdapter;


// --------------------------------------------------------------------------
public class JMEmol extends JMECore implements Graphical2DObject {


// constructors:
//	public JMEmol()  
//	public JMEmol(Parameters pars)  
//	public JMEmol(JME jme, Parameters pars)  
	
//	public JMEmol(JME jme, JMEmol mols[])  

//	public JMEmol(JME jme, JMEmol m, int part)  
//	public JMEmol(JME jme, JMEmol m, int part, Object NOT_USED)  
	
//	public JMEmol(JME jme, JmolAdapterAtomIterator atomIterator, JmolAdapterBondIterator bondIterator, Parameters pars) throws Exception  
//	public JMEmol(JME jme, String molecule, boolean hasCoordinates, Parameters pars) throws Exception  
//	public JMEmol(JME jme, String molFile, Parameters options)  

	
	
	public static class ReactionRole {
		public final static int NOROLE = 0;
		public final static int REACTANT = 1;
		public final static int AGENT = 2;
		public final static int PRODUCT = 3;

		public final static int all[] = { REACTANT, AGENT, PRODUCT };
		public final static int maxRole = PRODUCT;
		public final static int ANY = -1;

	};

	public JME jme; // parent

	public int chain[] = new int[101];

	public int touchedAtom = 0; // nesmu byt static kvoli reaction (multi?)
	public int touchedBond = 0;
	public int touched_org = 0; // original v rubber banding
	public double xorg, yorg; // center of ring in free space, rubber banding
	public int nchain; // pomocna variable pre CHAIN (aktualna dlzka pri rubber)
	boolean stopChain = false; 
	boolean needRecentering = false;
	boolean isQuery = false; // 2013.09

	static boolean TESTDRAW = false;

	Color uniColor = null;

	private int reactionRole = ReactionRole.NOROLE;

	// used for junit testing
	public JMEmol() {
		this(null, (Parameters) null);
	}

	public JMEmol(Parameters pars) {
		this(null, pars);
	}

	/**
	 * The primary entry point; construct an empty molecule
	 * 
	 * @param jme
	 * @param pars desired parameters or null for defaults
	 */
	public JMEmol(JME jme, Parameters pars) {
		super(jme, pars);
		this.jme = jme;
	}

	
	/**
	 * Construct a deep copy of the given molecule.
	 * 
	 * @param m
	 */
	JMEmol(JMEmol m) {
		super((JMECore) m);
		jme = m.jme;
		chiralFlag = m.chiralFlag;
		reactionRole = m.reactionRole;
	}

	/**
	 * Construct a molecule by merging molecules.
	 * 
	 * @param jme
	 * @param mols
	 */
	public JMEmol(JME jme, JMEmol mols[]) {	
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

	/**
	 * Construct a fragment molecule from a part of another molecule.
	 * 
	 * @param jme
	 * @param m
	 * @param part the fragment part to get
	 */
	public JMEmol(JME jme, JMEmol m, int part) {
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
			// bonds[nbonds].bondType = m.bonds[i].bondType;
			// stereob[nbonds] = m.stereob[i];
			newAddedBond.va = newn[atom1];
			newAddedBond.vb = newn[atom2];
			// btag[nbonds] = m.btag[i];
		}
		this.setNeighborsFromBonds(); // update the adjencylist
	}

	/**
	 * Duplicate of JMEmol(JME jme, JMEmol m, int part) , uses the internal
	 * partIndex instead of the this.a[] for the part information
	 * 
	 * @param m
	 * @param part
	 */
	public JMEmol(JME jme, JMEmol m, int part, Object NOT_USED) {
		this(jme, m.parameters);
		setPart(m, part);
		this.setChiralFlag(m.getChiralFlag());
	}

	// ----------------------------------------------------------------------------
	/**
	 * 
	 * Construct a JMEmol from a JME string
	 * 
	 * @param jme
	 * @param molecule  jme string, for example
	 * @param type JME, for example
	 * @param pars
	 * @throws Exception
	 */
	public JMEmol(JME jme, Object molecule, JME.SupportedFileFormat type, Parameters pars) throws Exception {
		this(jme, pars);
		if (molecule == null)
			return;		
		switch (type) {
		case JME:
			createFromJMEString((String) molecule);
			break;
		case MOL: // V2000, actually
			createFromMOLString((String) molecule);
			break;
		case JMOL:
			createFromJmolAdapter((Object[]) molecule);
			break;
		default:
			throw new IllegalArgumentException("Unrecognized format");
		} 
		setNeighborsFromBonds(); // will be callsd by complete() later, disable it?
		deleteHydrogens(pars.hydrogenParams);
		complete(pars.computeValenceState); // este raz, zachytit zmeny
	}

	public String createJME(Box boundingBox) {
		return JMEWriter.createJME(this, false, boundingBox);
	}

	/**
	 * Process the JME string: natoms nbonds (atomic_symbol x y) (va vb bondType)
	 * 
	 * atomic symbols for smiles-non-standard atoms may be in smiles form i.e O-,
	 * Fe2+, NH3+
	 * 
	 * @param jmeString
	 */
	private void createFromJMEString(String jmeString) {

		if (jmeString.startsWith("\""))
			jmeString = jmeString.substring(1, jmeString.length());
		if (jmeString.endsWith("\""))
			jmeString = jmeString.substring(0, jmeString.length() - 1);
		if (jmeString.length() < 1) {
			natoms = 0;
			return;
		}
		try {
			StringTokenizer st = new StringTokenizer(jmeString);
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
				atom.x = Double.valueOf(st.nextToken()).doubleValue();
				atom.y = -Double.valueOf(st.nextToken()).doubleValue();
			}
			// --- bonds
			for (int i = 1; i <= nbondsx; i++) {
				Bond bond = createAndAddBondFromOther(null);
				bond.va = Integer.valueOf(st.nextToken()).intValue();
				bond.vb = Integer.valueOf(st.nextToken()).intValue();
				int bondType = Integer.valueOf(st.nextToken()).intValue();
				// musi premenit bondType -1 up a -2 down (z va na vb) na stereob
				int stereob = 0;
				if (bondType == -1) {
					bondType = 1;
					stereob = Bond.STEREO_UP;
				} else if (bondType == -2) {
					bondType = 1;
					stereob = Bond.STEREO_DOWN;
				} else if (bondType == -5) {
					bondType = 2;
					stereob = Bond.STEREO_EZ;
				} // ez stereo
				// query bonds created in query window
				else if (bondType == Bond.QB_ANY || bondType == Bond.QB_AROMATIC || bondType == Bond.QB_RING
						|| bondType == Bond.QB_NONRING) {
					stereob = bondType;
					bondType = Bond.QUERY;
				}
				bond.bondType = bondType;
				bond.stereo = stereob;
			}


		} // end of try
		catch (Exception e) {
			System.err.println("read JSME string exception - " + e.getMessage());
			// e.printStackTrace();
			natoms = 0;
			throw (e);
		}
	}

	/**
	 * Create a molecule from MOLfile V2000 data.
	 * 
	 * @param molData
	 */
	private void createFromMOLString(String molData) {

		String line = "";
		String separator = JMEUtil.findLineSeparator(molData);

		// BB: if something else than the molfile, e.g. smiles
		if (separator == null) {
			return;
		}

		StringTokenizer st = new StringTokenizer(molData, separator, true);

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
		int valences[] = new int[natomsx + 1];
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
					int mark = Integer.valueOf(s).intValue();
					if (mark > 0) {
						setAtomMapFromInput(i, mark);
					}
				}
			}
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
			Bond bond = createAndAddBondFromOther(null);
			line = JMEUtil.nextData(st, separator);
			bond.va = Integer.valueOf(line.substring(0, 3).trim()).intValue();
			bond.vb = Integer.valueOf(line.substring(3, 6).trim()).intValue();
			int nasvx = Integer.valueOf(line.substring(6, 9).trim()).intValue();
			int bondType;

			if (nasvx == 1)
				bondType = Bond.SINGLE;
			else if (nasvx == 2)
				bondType = Bond.DOUBLE;
			else if (nasvx == 3)
				bondType = Bond.TRIPLE;
			// else if (nasvx == 4) bondType = Bond.AROMATIC;
			else if (nasvx == 8)
				bondType = Bond.COORDINATION; // see comment for the MOL writer

			// aromatic ???
			else
				bondType = Bond.QUERY;
			int stereoVal = 0;
			if (line.length() > 11)
				stereoVal = Integer.valueOf(line.substring(9, 12).trim()).intValue();
			// ??? treba s nasvx
			if (bondType == Bond.SINGLE || bondType == Bond.COORDINATION) {

				if (stereoVal == 1) {
					bond.stereo = Bond.STEREO_UP;
				} else if (stereoVal == 6) {
					bond.stereo = Bond.STEREO_DOWN;
				} else if (stereoVal == 4) {
					bond.stereo = Bond.STEREO_EITHER; // new Feb 2017
				}
			}
			if (nasvx == Bond.DOUBLE && stereoVal == 3) {
				bondType = Bond.DOUBLE;
				bond.stereo = Bond.STEREO_EZ;
			} // crossed bond, Feb 2017

			bond.bondType = bondType;
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
					// BH 2023.01.28 no linear addition or new action
					addBondToAtom(a, 0, false, false);
					setAtom(natoms, "R" + nr);
				}
			}
		}

		// remove hydrogens when reading molfile
		// TODO BB: should this be done if by the option nohydrogens is on ?
		deleteHydrogens(this.parameters.hydrogenParams);

		// BB: not a good idea
		// boolean orgKeepHydrogens = jme.keepHydrogens;
		// jme.keepHydrogens = false;
		// deleteHydrogens();
		// jme.keepHydrogens = orgKeepHydrogens;

		complete(this.parameters.computeValenceState); // este raz, zachytit zmeny

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
	}

	/**
	 * From Jmol's SmarterJmolAdapter -- could be any one of dozens of kinds of file.
	 * 
	 * @param iterators [atomIterator, bondIterator]
	 * @throws Exception
	 */
	private void createFromJmolAdapter(Object[] iterators) throws Exception {
		JmolAdapterAtomIterator atomIterator = (JmolAdapterAtomIterator) iterators[0];
		JmolAdapterBondIterator bondIterator = (JmolAdapterBondIterator) iterators[1];		
	    Map<Object, Integer> atomMap = new Hashtable<Object, Integer>();
	    while (atomIterator.hasNext()) {
	      String sym = Elements.elementSymbolFromNumber(atomIterator.getElementNumber());
	      // from Jmol -- could be 13C;
	      Atom a = createAtom(sym);
	      atomMap.put(atomIterator.getUniqueID(), Integer.valueOf(natoms));
	      P3d pt = atomIterator.getXYZ();
	      a.x = pt.x;
	      a.y = -pt.y;
	      a.q = atomIterator.getFormalCharge();
	      setAtom(natoms, JmolAdapter.getElementSymbol(atomIterator.getElement()));
	    }
	    while (bondIterator.hasNext()) {
	      Bond b = createAndAddBondFromOther(null);
	      b.va = atomMap.get(bondIterator.getAtomUniqueID1()).intValue();
	      b.vb = atomMap.get(bondIterator.getAtomUniqueID2()).intValue();
	      int bo = bondIterator.getEncodedOrder();
	      switch (bo) {
	      case Edge.BOND_STEREO_NEAR:
	        b.bondType = Bond.SINGLE;
	        b.stereo = Bond.STEREO_UP;
	        break;
	      case Edge.BOND_STEREO_FAR:
	        b.bondType = Bond.SINGLE;
	        b.stereo = Bond.STEREO_DOWN;
	        break;
	      case Edge.BOND_COVALENT_SINGLE:
	      case Edge.BOND_AROMATIC_SINGLE:
	        b.bondType = Bond.SINGLE;
	        break;
	      case Edge.BOND_COVALENT_DOUBLE:
	      case Edge.BOND_AROMATIC_DOUBLE:
	        b.bondType = Bond.DOUBLE;
	        break;
	      case Edge.BOND_COVALENT_TRIPLE:
	        b.bondType = Bond.TRIPLE;
	        break;
	      case Edge.BOND_AROMATIC:
	      case Edge.BOND_STEREO_EITHER:
	      default:
	        if ((bo & 0x07) != 0)
	          b.bondType = (bo & 0x07);
	        break;
	      }
	    }
	}

	/**
	 * This can be a problem for CDX or CDXML files with fragments.
	 * 
	 * @return
	 */
	protected boolean checkNeedsCleaning() {
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
	
	protected boolean mixPastelBackGroundColors = true;

	private double centerx = Double.NaN, centery;

	private AtomDisplayLabel[] atomLabels;

	final static boolean doTags = false; // compatibility with JMEPro

	/**
	 * Compute the average color for the atom or bond and set the color to the
	 * graphics use the .bacgroundColors array which is independent from the mark
	 * 
	 * @param og
	 * @param ab
	 * @param isAtom
	 * @return
	 */
	protected Color setPresetPastelBackGroundColor(PreciseGraphicsAWT og, int ab, boolean isAtom) {
		int colors[] = new int[1];
		boolean colorIsSet = false;

		boolean showMappingNumbers = this.parameters.number;
		boolean showColorMark = this.parameters.mark;
		boolean showAtomMapNumberWithBackgroundColor = this.parameters.showAtomMapNumberWithBackgroundColor;
		AtomBondCommon atomOrBond = isAtom ? this.atoms[ab] : this.bonds[ab];

		// an atom map can be mapped to the a given color of the palette, meaning that
		// visually
		// the reaction mapping could be easier to interpret than just the display of
		// numbers.
		if (showMappingNumbers && showAtomMapNumberWithBackgroundColor && isAtom) {
			Atom atom = this.atoms[ab];
			if (atom.isMapped()) {
				int map = atom.getMap();
				int max_map = jme.colorManager.numberOfBackgroundColors();
				// recycle the colors if too large atom map number
				while (map > max_map) {
					map -= max_map;
				}
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

		Color color = (colors != null && colors.length > 0 ? jme.colorManager.averageColor(colors) : null);
		if (color != null) {
			og.setColor(color);
		}
		return color;
	}

	public void scaleXY(double scale) {
		if (scale > 0) {
			for (int at = 1; at <= natoms; at++) {
				atoms[at].scaleXY(scale);
			}
			setBondCenters(); // needed for mouse over
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
		//assert (at1 != at2);
		Bond newBond = createAndAddBondFromOther(null); // the new bond index is this.nbonds
		addBothNeighbors(at1, at2); // set up the adjacency lists
		newBond.va = at1;
		newBond.vb = at2;
		// compute bond centers
		setBondCenter(newBond);
		newBond.bondType = bondType;
		return newBond;
	}

	void forceUniColor(Color color) {
		this.uniColor = color;
	}

	void resetForceUniColor() {
		this.uniColor = null;
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

	public void setAtomProperties(double xx, double yy, int ahc, int aq) {
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
	// int[] bd = new int[4);
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
		complete(computeValenceState);
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
		// if (jme.dimension == null) jme.dimension = size();
		// cim vacsia scale, tym viac sa molekula zmensuje

		this.setBondCenters(); // BB added June 2020

		return refBondLength;
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

		Rectangle2D.Double widthAndHeight = this.jme.getMolecularAreaBoundingBoxCoordinate00();
		xpix = widthAndHeight.width;
		ypix = widthAndHeight.height;

		if (xpix <= 0 || ypix <= 0) { // does this ever happen?
			needRecentering = true;
			return;
		}

		Rectangle2D.Double cad = computeBoundingBoxWithAtomLabels(null);

		double shiftx = xpix / 2 - cad.getCenterX(); // . center[0];
		double shifty = ypix / 2 - cad.getCenterY(); // center[1];
		if (!jme.nocenter)
			moveXY(shiftx * factor, shifty * factor);
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

	int testAtomAndBondTouch(double xx, double yy, boolean ignoreAtoms, boolean ignoreBonds, double[] retMin) {
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
	}

	// ----------------------------------------------------------------------------
	public void draw(PreciseGraphicsAWT og) {
		int atom1, atom2;
		double xa, ya, xb, yb;
		double sirka2s, sirka2c;
		double sirka2 = 2., sirka3 = 3.;

		if (this.nAtoms() == 0)
			return; // bug fix github #25

		boolean markColorBackground = parameters.mark;
		Color atomTextStrokeColorArray[] = new Color[this.nAtoms() + 1];

		og.setDefaultBackGroundColor(jme.canvasBg);

		// ked padne, aby aspon ukazalo ramcek
		// this should not be done here
		if (jme.options.depictBorder) {
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
		double rs = jme.options.bondBGrectRelativeSize;
		if (rs > 0) {
			for (int i = 1; i <= nbonds; i++) {
				// setPresetPastelBackGroundColor() returns null if no bacground colors have
				// been specified != mark
				if (setPresetPastelBackGroundColor(og, i, false) != null) {
					atom1 = bonds[i].va;
					atom2 = bonds[i].vb;
					setCosSin(atom1, atom2);
					sirka2c = (sirka3 * 3) * cosSin[0];
					sirka2s = (sirka3 * 3) * cosSin[1];

					sirka2s *= rs;
					sirka2c *= rs;

					double[] xr = new double[4], yr = new double[4];
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
		// this.parameters.showAtomMapNumberWithBackgroundColor) {
		double cs = sirka2 * 12;

		// atoms : circle behind the atom position - does not cover the atom symbol
		// (will be done later)
		rs = jme.options.atomBGcircleRelativeSize;
		if (rs > 0) {
			for (int i = 1; i <= natoms; i++) {
				Color backgroundColor = setPresetPastelBackGroundColor(og, i, true);
				if (backgroundColor != null) {
					double scs = cs * rs;
					og.fillOval(x(i) - scs / 2., y(i) - scs / 2., scs, scs);

					// if we have a high resolution screen or a large zoom factor,
					// then add a thin stroke to the text to improve readability
					if (
					// JMEUtil.isHighDPI() ||
					og.currentZoomFactor() >= 2) { // or JMSE zoom factor > 100 %
						Color contrastColor = ColorManager.contrast(backgroundColor); // the stroke color is either
																						// white or black
						// depending on the darkness of the backgroundColor
						atomTextStrokeColorArray[i] = contrastColor;
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
			if (jme.action == Actions.ACTION_DELGROUP && touchedBond == i && this.isRotatableBond(i)) { // duplicated
																										// logic
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

			if (bond.stereo == Bond.STEREO_XUP || bond.stereo == Bond.STEREO_XDOWN
					|| bond.stereo == Bond.STEREO_XEITHER) // kvoli spicke vazby
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
				setCosSin(atom1, atom2); // BH??? test distance was 1, not .001?
//				dx = xb - xa;
//				dy = yb - ya;
//				dd = Math.sqrt(dx * dx + dy * dy);
//				if (dd < 1.)
//					dd = 1.;
//				sina = dy / dd;
//				cosa = dx / dd;
			}
			switch (bond.bondType) {
			case Bond.DOUBLE:
				// BB crossed bond display: not magenta anymore
				// if (bond.stereo >= 10) og.setColor(Color.magenta); // E,Z je farebna
				sirka2c = sirka2 * cosSin[0];
				sirka2s = sirka2 * cosSin[1];
				if (bond.stereo != Bond.STEREO_EZ) {
					og.drawLine(xa + sirka2s, ya - sirka2c, xb + sirka2s, yb - sirka2c);
					og.drawLine(xa - sirka2s, ya + sirka2c, xb - sirka2s, yb + sirka2c);
				} else { // BB: crossed bond
					og.drawLine(xa + sirka2s, ya - sirka2c, xb - sirka2s, yb + sirka2c);
					og.drawLine(xa - sirka2s, ya + sirka2c, xb + sirka2s, yb - sirka2c);

				}
				og.setColor(Color.black);
				break;
			case Bond.TRIPLE:
				double ixa = xa;
				double iya = ya;
				double ixb = xb;
				double iyb = yb;
				og.drawLine(ixa, iya, ixb, iyb);
				double sirka3c = sirka3 * cosSin[0];
				double sirka3s = sirka3 * cosSin[1];
				og.drawLine(ixa + sirka3s, iya - sirka3c, ixb + sirka3s, iyb - sirka3c);
				og.drawLine(ixa - sirka3s, iya + sirka3c, ixb - sirka3s, iyb + sirka3c);
				/*
				 * g.drawLine((int)Math.round(xa+sirka3s),(int)Math.round(ya-sirka3c),
				 * (int)Math.round(bondCenterX+sirka3s),(int)Math.round(bondCenterY-sirka3c));
				 * g.drawLine((int)Math.round(xa-sirka3s),(int)Math.round(ya+sirka3c),
				 * (int)Math.round(bondCenterX-sirka3s),(int)Math.round(bondCenterY+sirka3c));
				 */
				break;
			case Bond.QUERY:// case 0: // dotted //BB : removed 0 because of coordination
				for (int k = 0; k < 10; k++) {
					double xax = xa - (xa - xb) / 10. * k;
					double yax = ya - (ya - yb) / 10. * k;
					og.drawLine(xax, yax, xax, yax);
				}
				// query bond text
				og.setFont(jme.gui.atomDrawingAreaFont);
				double h = GUI.stringHeight(jme.gui.atomDrawingAreaFontMet); // vyska fontu
				Object o = bond.btag;
				String z = "?";
				if (o != null)
					z = (String) o;
				double w = jme.gui.atomDrawingAreaFontMet.stringWidth(z);
				double xstart = (xa + xb) / 2. - w / 2.;
				double ystart = (ya + yb) / 2. + h / 2 - 1; // o 1 vyssie
				og.setColor(Color.magenta);
				og.drawString(z, xstart, ystart);
				og.setColor(Color.black);
				break;
			default: // Bond.SINGLE, alebo stereo
				if (bond.stereo == Bond.STEREO_UP || bond.stereo == Bond.STEREO_XUP) {
					sirka2c = sirka3 * cosSin[0];
					sirka2s = sirka3 * cosSin[1];
					double[] px = new double[3];
					double[] py = new double[3];
					px[0] = xb + sirka2s;
					py[0] = yb - sirka2c;
					px[1] = xa;
					py[1] = ya;
					px[2] = xb - sirka2s;
					py[2] = yb + sirka2c;
					og.fillPolygon(px, py, 3);
				} else if (bond.stereo == Bond.STEREO_DOWN || bond.stereo == Bond.STEREO_XDOWN) {
					sirka2c = sirka3 * cosSin[0];
					sirka2s = sirka3 * cosSin[1];
					for (double k = 0; k < 10; k++) {
						double xax = xa - (xa - xb) / 10. * k;
						double yax = ya - (ya - yb) / 10. * k;
						double sc = k / 10.;
						og.drawLine(xax + sirka2s * sc, yax - sirka2c * sc, xax - sirka2s * sc, yax + sirka2c * sc);
					}
				} else if (bond.stereo == Bond.STEREO_EITHER || bond.stereo == Bond.STEREO_XEITHER) {
					double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
					sirka2c = sirka3 * cosSin[0];
					sirka2s = sirka3 * cosSin[1];
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
			if (JMEmol.doTags) {
				String btag = bond.btag;
				if (btag != null && btag.length() > 0) {
					og.setFont(jme.gui.atomDrawingAreaFont);
					double h = GUI.stringHeight(jme.gui.atomDrawingAreaFontMet); // vyska fontu
					double w = jme.gui.atomDrawingAreaFontMet.stringWidth(btag);
					double xstart = (xa + xb) / 2. - w / 2.;
					double ystart = (ya + yb) / 2. + h / 2 - 1; // o 1 vyssie
					og.setColor(Color.red);
					og.drawString(btag, xstart, ystart);
					og.setColor(Color.black);
				}
			}
		}

		computeAtomLabels();

		og.setFont(jme.gui.atomDrawingAreaFont);
		double h = GUI.stringHeight(jme.gui.atomDrawingAreaFontMet); // vyska fontu

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
			if (atomLabels[i].noLabelAtom) {
				continue;
			}
			og.setBackGroundColor(); // set default background color
			setPresetPastelBackGroundColor(og, i, true);

			// surround the atom label with background color to mask the bonds around the
			// atom label
			atomLabels[i].fill(og);
			// color for the atom symbol
			og.setColor(JME.color[an(i)]);
			Color strokeColor = atomTextStrokeColorArray[i];
			atomLabels[i].draw(og, strokeColor, h, jme.gui.atomDrawingAreaFontMet);
		}

		// diplay atom maps of atoms that have been marked
		// marked atoms - islo by to do predosleho loopu zapasovat ???

		// 10-2018: do not show maps if option star is set
		if (!markColorBackground) {
			og.setFont(jme.gui.atomMapDrawingAreaFont);

			for (int i = 1; i <= natoms; i++) {
				AtomDisplayLabel al = atomLabels[i];
				String mapString = al.mapString;

				if (mapString == null)
					continue;

				double atomMapX = al.atomMapX;
				double atomMapY = al.atomMapY;

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
		if (JMEmol.doTags) {
			og.setFont(jme.gui.atomDrawingAreaFont);

			for (int i = 1; i <= natoms; i++) {
				Atom a = atoms[i];
				if (a.atag == null || a.atag.equals(""))
					continue;
				// int w = jme.atomDrawingAreaFontMet.stringWidth(zz[i]);
				AtomDisplayLabel al = atomLabels[i];
				double smallWidth = al.smallAtomWidthLabel;
				double fullWidth = al.fullAtomWidthLabel;

				double xstart = a.x - smallWidth / 2.;
				double ystart = a.y + h / 2 - 1; // o 1 vyssie
				og.setColor(Color.red);
				og.drawString(" " + a.atag, xstart + fullWidth, ystart);
			}
		}

		// mark touched bond or atom, or atoms marked to delete
		if ((touchedAtom > 0 || touchedBond > 0) && !JME.webme) {

			og.setColor(jme.action == Actions.ACTION_DELETE ? Color.red :
			// just checking: Hmm...jme.mouseShift ? Color.cyan :
					Color.blue);

			if (touchedAtom > 0 && jme.action != Actions.ACTION_DELGROUP) {
				Rectangle2D.Double r = atomLabels[touchedAtom].drawBox;
				og.drawRect(r.x, r.y, r.width, r.height);
			}

			if (touchedBond > 0 && jme.action != Actions.ACTION_MOVE_AT) {
				// don't show a rectangle around the bond if the
				// action is to move the atom

				atom1 = bonds[touchedBond].va;
				atom2 = bonds[touchedBond].vb;
				setCosSin(atom1, atom2);
//				dx = x(atom2) - x(atom1);
//				dy = y(atom2) - y(atom1);
//				dd = Math.sqrt(dx * dx + dy * dy);
//				if (dd < 1.)
//					dd = 1.;
//				sina = dy / dd;
//				cosa = dx / dd;
				sirka2c = (sirka3 + 1) * cosSin[0];
				sirka2s = (sirka3 + 1) * cosSin[1];
				double[] px = new double[5];
				double[] py = new double[5];
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
				if (jme.action != Actions.ACTION_DELGROUP) // pri DELGROUP nekresli modro
					og.drawPolygon(px, py, 5);

				if (jme.action == Actions.ACTION_DELGROUP && isRotatableBond(touchedBond)) {
					// ACTION_DELGROUP is a specila way of deleting a groug of atoms icon: -X-R
					// two parts of the molecule, one to keep and one to delete
					// the smallest that will be deleted mut be shown in red
					// only possible if the selected bond is not a ring bond
					// marks atoms with unpleasent fate (suggested by Bernd Rohde)
					// the atoms selected for deleting are in this.a[]?

					int va = bonds[touchedBond].va;
					int vb = bonds[touchedBond].vb;
					this.computeMultiPartIndices(touchedBond);
					int partA = atoms[va].partIndex;
					int partB = atoms[vb].partIndex;
					int sizeA = 0;
					int sizeB = 0;

					for (int i = 1; i <= natoms; i++) {
						int pi = atoms[i].partIndex;
						if (pi == partA) {
							sizeA++;
						} else if (pi == partB) {
							sizeB++;
						}
					}
					// choose the smallest part to be deleted
					int partToDelete = sizeA > sizeB ? partB : partA;
					// framing atoms to delete in red
					og.setColor(Color.red);
					for (int i = 1; i <= natoms; i++) {
						this.atoms[i].deleteFlag = false;
						if (atoms[i].partIndex == partToDelete) {
							this.atoms[i].deleteFlag = true;
							Rectangle2D.Double r = atomLabels[i].drawBox;
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
		boolean showHs = parameters.hydrogenParams.showHs;
		boolean showMap = (!parameters.mark
				|| parameters.showAtomMapNumberWithBackgroundColor);
		FontMetrics fm = jme.gui.atomDrawingAreaFontMet;
		double h = (/*jme == null ? 9.0 : */GUI.stringHeight(fm));
		double rb = RBOND;		
		atomLabels = AtomDisplayLabel.createLabels(this, rb, fm, h, showHs, showMap, atomLabels);
	}

	boolean hasAtomFlaggedToBeDeleted() {
		for (int at = 1; at <= natoms; at++) {
			if (atoms[at].deleteFlag)
				return true;
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return (natoms == 0);
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
	
	public Box computeBoundingBoxWithAtomLabels(Box union) {
		if (natoms == 0)
			return union;
		computeAtomLabels();
		for (int i = 1; i <= natoms; i++)
			union = this.atomLabels[i].drawBox.createUnion(union, union);
		return union;
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
		XY(atom, xNew, yNew);
	}

	/**
	 * Addition a new bond in mouseDrag mode: move the bond around with the mouse
	 * position.
	 */
	void rubberBanding(double xnew, double ynew) {
		// len pre vazby
		// povodny touchedAtom je ulozeny v touched_org (urobene v mouse_down)

		// last atom is the atom at the end of the new bond

		touchedAtom = 0;

		XY(0, xnew, ynew); // gives atom 0 the coordinates of the mouse pointer
		int atom = checkTouch(0); // in order to find a close enough atom
		if (jme.action != Actions.ACTION_CHAIN) { // pri chaine to blblo
			if (atom > 0) {
				touchedAtom = atom;
				if (atom == touched_org) {
					XY(natoms, xorg, yorg);// move the new atom to the coordinate of the origin atom "snap"
				} else {
					XY(natoms, x(atom), y(atom)); // move the new atom to the coordinate of the closest touched atom
				}
			} else {
				// bond width normal length follows mouse pointer
				setCosSin(touched_org, 0);
				XY(natoms, x(touched_org) + JMECore.RBOND * cosSin[0], y(touched_org) + JMECore.RBOND * cosSin[1]);
			}
			return;
		}
		// chain processing
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
					atoms[2].y = y(1) + rx / 2.;
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
						addBondToAtom(chain[0], 0, false, false); // adds new bond
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
					// info("You are too focused on chains, enough of it for now !");
					jme.showInfo("You are too focused on chains, enough of it for now !");
					nchain--;
					return;
				}
				addBondToAtom(natoms, (int) Math.round(ym), false, false);
				// this.jme.recordBondEvent("addBond"); // wait until finished
				jme.willPostSave(false); // do not store undo state
				chain[nchain] = natoms;
				if (checkTouch(natoms) > 0)
					stopChain = true;
			}
		}
		touchedAtom = 0;
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
				createAndAddNewBond(n, parent, Bond.SINGLE);
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
	int checkTouch(int atom) {
		return checkTouchToAtom(atom, 1, natoms);
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
		double min = GUI.TOUCH_LIMIT + 1;
		int touch = 0;
		for (int i = firstAtom; i <= lastAtom; i++) {
			if (atom == i)
				continue;
			// compute squared distance
			dx = x(atom) - x(i);
			dy = y(atom) - y(i);
			rx = dx * dx + dy * dy;
			if (rx < GUI.TOUCH_LIMIT)
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
		double min = 2 * GUI.TOUCH_LIMIT;
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

	public double sumAtomTooCloseContactsOfAddedFragment(int fragmentFirstAtom, int fragmentLastAtom) {
		double result = 0;
		for (int at = 1; at <= natoms; at++) { // MAY 2016 < <=
			if (at >= fragmentFirstAtom && at <= fragmentLastAtom)
				continue;

			result += this.sumAtomTooCloseContacts(at, fragmentFirstAtom, fragmentLastAtom);

		}
		return result;
	}

	// ----------------------------------------------------------------------------
	public void avoidTouch(int from) {
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
	/*
	 * Delete the bond and its atoms if they end not to be connected to any other
	 * atoms
	 * 
	 */
	public void deleteBond(int delbond) {
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
	public void deleteAtomGroup() {

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
				if (bonds[b].bondType == Bond.COORDINATION) {
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
	public void toggleBondStereo(int bondIndex) {
		// alebo vola z drawingArea.mouseDown s (touchBond) a bondType je rozna,
		// alebo z completeBond, vtedy je bondType vzdy 1
		// robi to inteligente, presmykuje medzi 4, len kde je to mozne
		// v stereob je uschovane aj querytype ked ide o Bond.QUERY bond

		Bond bond = this.bonds[bondIndex];
		if (bond.isSingle() || bond.isCoordination()) { // accept coordination bond with stereo
			// Bond.UP a Bond.DOWN daju hrot na va[], Bond.XUP, Bond.XDOWN na vb[]
			int atom1 = bonds[bondIndex].va;
			int atom2 = bonds[bondIndex].vb;
			if (nv(atom1) < 2 && nv(atom2) < 2) { // <=2 nemoze byt kvoli allenu
				bond.stereo = 0;
				info("Stereomarking meaningless on this bond !");
				return;
			}
			// atom1 - stary, atom2 - novy atom
			if (JME.webme) {
				// handling webme (up / down templates)
				// just switching up/xup and down/xdown
				if (!jme.revertStereo) {
					if (bond.stereo == Bond.STEREO_UP)
						bond.stereo = Bond.STEREO_XUP;
					else if (bond.stereo == Bond.STEREO_XUP)
						bond.stereo = Bond.STEREO_UP;
					else {
						if (nv(atom2) <= nv(atom1))
							bond.stereo = Bond.STEREO_UP;
						else
							bond.stereo = Bond.STEREO_XUP;
					}
				} else {
					if (bond.stereo == Bond.STEREO_DOWN)
						bond.stereo = Bond.STEREO_XDOWN;
					else if (bond.stereo == Bond.STEREO_XDOWN)
						bond.stereo = Bond.STEREO_DOWN;
					else {
						if (nv(atom2) <= nv(atom1))
							bond.stereo = Bond.STEREO_DOWN;
						else
							bond.stereo = Bond.STEREO_XDOWN;
					}
				}
			}

			// standard editor stuff
			switch (bond.stereo) {
			case 0: // aby bol hrot spravne (nie na nerozvetvenom)
				// Bond.UP dava normalne hrot na va[]
				if (nv(atom2) <= nv(atom1))
					bond.stereo = Bond.STEREO_UP;
				else
					bond.stereo = Bond.STEREO_XUP;
				break;
			case Bond.STEREO_UP:
				bond.stereo = Bond.STEREO_DOWN;
				break;
			case Bond.STEREO_DOWN:
				bond.stereo = Bond.STEREO_EITHER;
				break;
			case Bond.STEREO_EITHER:
				if (nv(atom2) > 2)
					bond.stereo = Bond.STEREO_XUP;
				else
					bond.stereo = Bond.STEREO_UP;
				break;
			case Bond.STEREO_XUP:
				bond.stereo = Bond.STEREO_XDOWN;
				break;
			case Bond.STEREO_XDOWN:
				bond.stereo = Bond.STEREO_XEITHER;
				break;
			case Bond.STEREO_XEITHER:
				if (nv(atom1) > 2)
					bond.stereo = Bond.STEREO_UP;
				else
					bond.stereo = Bond.STEREO_XUP;
				break;

			}
		} else if (bond.bondType == Bond.DOUBLE) {
			bond.toggleNormalCrossedDoubleBond();
			// if (bond.stereo == Bond.EZ) bond.stereo = 0; else bond.stereo = Bond.EZ;
		} else {
			info("Stereomarking allowed only on single and double bonds!");
		}
	}

	// ----------------------------------------------------------------------------
	// returns stereo atom to which this bond points
	int getStereoAtom(int bond) {
		// Bond.UP a Bond.DOWN daju hrot na va[], Bond.XUP, Bond.XDOWN na vb[]
		switch (bonds[bond].stereo) {
		case Bond.STEREO_UP:
		case Bond.STEREO_DOWN:
			return bonds[bond].va;
		case Bond.STEREO_XUP:
		case Bond.STEREO_XDOWN:
			return bonds[bond].vb;
		}
		return 0;
	}

	/**
	 * 
	 * @param atom         to be added to
	 * @param up           flip bond to other side - only possible if the touched
	 *                     atom has 1 valence values for flip: 0,-1 or 1
	 * @param forceLinear  if prev or this will be a triple bond
	 * @param addingDouble if this will be a double bond, as it will be linear, in
	 *                     that case, if the prev bond is double.
	 * @return true if the up was the parameter used
	 */
	public boolean addBondToAtom(int atom, int up, boolean forceLinear, boolean addingDouble) {
		boolean upWasUsed = false;
		createAtomFromOther(null);
		switch (nv(atom)) {
		case 0:
			XY(natoms, x(atom) + RBOND * .866, y(atom) + RBOND * .5);
			break;
		case 1:
			int atom1 = v(atom)[1];
			int atom3 = 0; // reference, aby to slo rovno
			if (nv(atom1) == 2) {
				if (v(atom1)[1] == atom) 
					atom3 = v(atom1)[2];
				else
					atom3 = v(atom1)[1];
			}
			double dx = x(atom) - x(atom1);
			double dy = y(atom) - y(atom1);
			double rx = Math.sqrt(dx * dx + dy * dy);
			if (rx < 0.001)
				rx = 0.001;
			double sina = dy / rx;
			double cosa = dx / rx;
			double xx;
			double yy;
			// checking for allene -N=C=S, X#C-, etc
			// chain je ako linear !
			Bond b = getBond(atom, atom1);
			if (forceLinear	|| b.bondType == Bond.TRIPLE
					|| (b.bondType == Bond.DOUBLE && addingDouble))
			{
				xx = rx + RBOND;
				yy = 0.;
			} else {
				xx = rx + RBOND * Math.cos(Math.PI / 3.);
				yy = RBOND * Math.sin(Math.PI / 3.);
			}
			if (atom3 > 0) // to keep growing chain linear
				if (((y(atom3) - y(atom1)) * cosa - (x(atom3) - x(atom1)) * sina) > 0.)
					yy = -yy;
			// flip bond to other site
			if (up > 0 && yy < 0.)
				yy = -yy;
			else if (up < 0 && yy > 0.)
				yy = -yy;
			XY(natoms, x(atom1) + xx * cosa - yy * sina, y(atom1) + yy * cosa + xx * sina);

			upWasUsed = true;

			break;

		case 2:
			double[] newPoint = new double[2];
			getNewPoint(atom, RBOND, newPoint);
			XY(natoms, newPoint[0], newPoint[1]);
			break;

		case 3:
		case 4:
		case 5:
			// postupne skusa linearne predlzenie vsetkych vazieb z act_a
			for (int i = 1; i <= nv(atom); i++) {
				atom1 = v(atom)[i];
				dx = x(atom) - x(atom1);
				dy = y(atom) - y(atom1);
				rx = Math.sqrt(dx * dx + dy * dy);
				if (rx < 0.001)
					rx = 0.001;
				XY(natoms, x(atom) + RBOND * dx / rx, y(atom) + RBOND * dy / rx);
				// teraz testuje ci sa nedotyka
				if (checkTouch(natoms) == 0 || i == nv(atom))
					break;
			}
			break;
		default: // error
			natoms--;
			info("Are you trying to draw an hedgehog ?", JME.LA_FAILED);
			return upWasUsed;
		}
		completeBond(atom);

		xorg = x(natoms);
		yorg = y(natoms); // used after moving, when moving !OK

		return upWasUsed;

	}

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

		int i = getBondIndex(atom, touched_org);
		if (i > 0) {
			nbonds--; // delete the just created new bond
			incrNV(touched_org, -1);
			// and increase the bond order between the two atom that were already bonded,
			// unless triple
			if (bonds[i].bondType < Bond.TRIPLE) {
				bonds[i].bondType++;
				bonds[i].stereo = 0;
			} // stereo zrusi
			else
				info("Maximum allowed bond order is 3 !");
			return;
		}
		if (nv(atom) == MAX_BONDS_ON_ATOM) {
			nbonds--; // delete the just created new bond
			incrNV(this.touched_org, -1);
			info("Not possible connection !");
			return;
		}

		// zmeni vazbove data na touched_org a atom
		bonds[nbonds].vb = atom;
		incrNV(this.touched_org, -1); // the new atom was added last, thus just need to decrease the nv
		this.addBothNeighbors(atom, touched_org);
		setBondCenter(bonds[nbonds]);
	}

	/**
	 * Create a bond between the touched atom and last added atom PE code - depends
	 * on JME
	 */
	void completeBond(int touchedAtom) {
		createAndAddNewBond(touchedAtom, natoms, 
				(jme.action == Actions.ACTION_BOND_DOUBLE ? Bond.DOUBLE
				: jme.action == Actions.ACTION_BOND_TRIPLE ? Bond.TRIPLE 
				: Bond.SINGLE));
		// creating new bond with stereo tool
		if (jme.action == Actions.ACTION_STEREO)
			toggleBondStereo(nbonds);
	}

	// necessary to add "smaller" bonds in scaled molecule bt WebME
//	double RBOND() {
//		return //(
//				//JME.scalingIsPerformedByGraphicsEngine ? 
//				JMECore.RBOND;
//				//			: JMECore.RBOND * jme.molecularAreaScalePixelsPerCoord);
//	}

	/**
	 * Add all other molecule (fragment) atoms and bonds to my self. Do not create a
	 * new bond or change the coordinates. all atomic and bond proipoerties are
	 * copied. The argument is not changed.
	 * 
	 * @param otherMol
	 */
	public void addOtherMolToMe(JMEmol otherMol) {
		int nn = natoms;
		for (int i = 1; i <= otherMol.natoms; i++) {
			createAtomFromOther(otherMol.atoms[i]);
			AN(natoms, otherMol.an(i));
		}
		for (int i = 1; i <= otherMol.nbonds; i++) {
			createAndAddBondFromOther(otherMol.bonds[i]); // create new bond and place it at the end: bonds[nbonds]
			bonds[nbonds].va = otherMol.bonds[i].va + nn;
			bonds[nbonds].vb = otherMol.bonds[i].vb + nn;
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
		return createAtomFromOther(null);
	}

	@Override
	protected Atom createAtomFromOther(Atom atomToDuplicate) {
		atomLabels = null;
		return super.createAtomFromOther(atomToDuplicate);
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

	// ----------------------------------------------------------------------------
	void setAtomHydrogenCount(int atom, int nh) {
		// upravuje to len pre X atomy !
		if (an(atom) == Atom.AN_X) {
			// label[atom] += "H";
			this.atoms[atom].label += "H";
			if (nh > 1)
				// label[atom] += nh;
				this.atoms[atom].label += nh;
		}
	}

	// ----------------------------------------------------------------------------
	void setAtomFormalCharge(int atom, int nq) {
		atoms[atom].q = nq;
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
			JMEUtil.log("Error in atom coloring");
			// e.printStackTrace();
		}
	}

	public void setAtomColors(String s, int delta) {
		setAtomOrBondColors(s, delta, true);

	}

	public void setBondColors(String s, int delta) {
		setAtomOrBondColors(s, delta, false);
	}

	public void addAtomColor(int at, int c) {
		if (at > 0 && at <= natoms) {
			atoms[at].addBackgroundColor(c);
		}

	}

	public int[] getAtomBackgroundColors(int at) {
		return (at > 0 && at <= natoms ? atoms[at].getBackgroundColors() : null);
	}

	public void addBondColor(int b, int c) {
		if (b > 0 && b <= this.nbonds) {
			bonds[b].addBackgroundColor(c);
		}
	}

	public int[] getBondBackgroundColors(int b) {
		return (b > 0 && b <= nbonds ? bonds[b].getBackgroundColors() : null);
	}

	/**
	 * Create a copy of my self. This code uses PE original code from the save()
	 * method
	 * 
	 * @param src
	 * @return
	 */
	public JMEmol deepCopy() {
		return new JMEmol(this);
	}

	boolean isRotatableBond(int a1, int a2) {
		return minimumRingSize(a1, a2) == 0;
	}

	boolean isRotatableBond(int b) {
		return isRotatableBond(bonds[b].va, bonds[b].vb);
	}

	// ----------------------------------------------------------------------------


//	/**
//	 * SIDE EFFECT: set this.a[] !!!!!!!!!!!!!!!!!! what is this variable good for?
//	 * 
//	 * @param removeSmall
//	 * @return the number of fragments? * this code is not used anymore
//	 * 
//	 */
//	public int checkMultipart(boolean removeSmall) {
//		// group prislusnost da do a[]
//		int nparts = 0;
//		boolean ok = false;
//		a = new int[natoms + 1); // a is used by other
//
//		while (true) {
//			for (int j = 1; j <= natoms; j++)
//				if (a[j] == 0) {
//					a[j] = ++nparts;
//					ok = true;
//					break;
//				}
//			if (!ok)
//				break;
//			while (ok) {
//				ok = false;
//				for (int j = 1; j <= nbonds; j++) {
//					int atom1 = bonds[j].va;
//					int atom2 = bonds[j].vb;
//					if (a[atom1] > 0 && a[atom2] == 0) {
//						a[atom2] = nparts;
//						ok = true;
//					} else if (a[atom2] > 0 && a[atom1] == 0) {
//						a[atom1] = nparts;
//						ok = true;
//					}
//				}
//			}
//		}
//		if (nparts < 2 || !removeSmall)
//			return nparts;
//
//		// najde najvacsiu
//		int size[] = new int[nparts + 1);
//		for (int i = 1; i <= natoms; i++)
//			size[a[i]]++;
//		int max = 0, largest = 1;
//		for (int i = 1; i <= nparts; i++)
//			if (size[i] > max) {
//				max = size[i];
//				largest = i;
//			}
//		// removing smaller part(s)
//		for (int i = natoms; i >= 1; i--)
//			if (a[i] != largest)
//				deleteAtom(i);
//
//		center(); // aby sa nedostalo do trap za okraj
//		info("Smaller part(s) removed !");
//		return 1;
//	}

	// used in the tests, should not be used in JME
	public String createSmiles() {
		return createSmiles(parameters); 
	}

	/**
	 * BB Create a smiles, does not affect my self, unlike the original
	 * implementation which needed the nocanonize option
	 * 
	 * @return smiles
	 */
	public String createSmiles(Parameters pars) {
		return JMESmiles.getSmiles(deepCopy(), pars, isQuery);
	}

	public int findAtomChargeForOutput(int atomIndex) {
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

			atom.setMapOrMark(map, !parameters.mark);
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
	public String createMolFile(String header) {
		return JMEWriter.createMolFile((JMECore) this, header, true, computeCoordinate2DboundingBox());
	}

	public void cleanAfterChanged(boolean polarNitro) {
		setValenceState();
		cleanPolarBonds(polarNitro); // TODO: need parameter polarnitro
	}

	// ----------------------------------------------------------------------------
	/**
	 * Change charge/Hydrogen count
	 * 
	 * @param atom
	 * @param type is always 0 in JME: means toggle the charge/hydrogen count
	 * @return
	 */
	public boolean changeCharge(int atom, int type) {
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
		case Atom.AN_H:
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

		case Atom.AN_B:
			if (sbo > 2)
				info(np + "this boron !");
			if (q(atom) == 0)
				Q(atom, 1);
			else if (q(atom) == 1)
				Q(atom, 0);
			break;
		case Atom.AN_C:
			// case Atom.AN_SI:
			if (sbo > 3)
				info(np + "this carbon !");
			else if (sbo < 4) {
				if (q(atom) == 0)
					Q(atom, -1);
				else if (q(atom) == -1)
					Q(atom, 1);
				else if (q(atom) == 1)
					Q(atom, 0);
			}
			break;
		case Atom.AN_N:
		case Atom.AN_P:
			if (sbo > 3)
				info(np + "multibonded N or P !");
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
		case Atom.AN_O: // -[O-] -O- =O -[O+]< >[O2+]< ...
		case Atom.AN_S: // asi sa na multivalent vykaslat
		case Atom.AN_SE:
			if (sbo > 2)
				info(np + "multibonded " + Atom.zlabel[an(atom)] + " !");
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
		case Atom.AN_F:
		case Atom.AN_CL:
		case Atom.AN_BR:
		case Atom.AN_I:
			if (sbo == 0 && q(atom) == 0)
				Q(atom, -1);
			else if (sbo == 0 && q(atom) == -1)
				Q(atom, 0);
			else
				info(np + "the halogen !");
			break;
		case Atom.AN_X:
			info("Use X button to change charge on the X atom !");
			break;
		}

		if (Atom.chargedMetalType(an(atom)) > 0)
			if (!this.toggleChargeAndHydrogenCountOfMetalAtom(atoms[atom], sbo))
				info(np + Atom.zlabel[an(atom)]);

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
		int maxMetalCharge = Atom.chargedMetalType(atom.an); // 1 for Na, 2 for Ca, 3 for Al
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
							info("Metallic " + Atom.zlabel[atom.an]);
					}

				}
				changed = (atom.q != q || atom.nh != nh);
				atom.q = q;
				atom.nh = nh;

			}
		}
		return changed;
	}

	public boolean markAtom(int newMark) {
		// Mark the touched atom

		// star marking of a part of a molecules
		if (parameters.mark) {
			if (jme.options.markOnly1) { // only one atom at a time can be marked
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
			hasBeenMarked = newMark != touchedAtomObject.getMap(); // BB new 2020
			touchedAtomObject.setMap(newMark);
			hasBeenMarked = true;
		}
		return hasBeenMarked;

	}

	public boolean markBond(int newMark) {
		// duplicated logic with markAtom

		// star marking of a part of a molecules
		if (parameters.mark) {
			// doColoring = -1;
			// //assert(activeMarker == this.parameters.markerMultiColor);
			if (jme.options.markOnly1) { // only one atom at a time can be marked
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

	// ----------------------------------------------------------------------------

	/**
	 * Warning: side effect: the order of the atoms is changed turned off
	 */
	public void numberAtomsSequentially() {
		for (int i = 1; i <= natoms; i++) {
			this.atoms[i].setMap(i);
		}
	}

//	// ----------------------------------------------------------------------------
//	@Override
//	public void XY(double x, double y) {
//	}

	public void info(String msg, int laFailed) {
		info(msg);
		jme.lastAction = laFailed;
	}

	public JMEmol compute2DcoordinatesIfMissing()  {
		return (has2Dcoordinates() ? null : OclAdapter.compute2Dcoordinates(this));
	}

	public void clearRotation() { 
		centerx = centery = Double.NaN;
	}

	public void rotate(double movex) {
		if (Double.isNaN(centerx)) {
			Box bbox = computeBoundingBoxWithAtomLabels(null);
			centerx = bbox.getCenterX();
			centery = bbox.getCenterY();
		}
		rotate(movex, centerx, centery);
	}

	public JMEmol reComputeBondOrderIfAromaticBondType() {
		return OclAdapter.reComputeBondOrderIfAromaticBondType(this);
	}

}

// ----------------------------------------------------------------------------
