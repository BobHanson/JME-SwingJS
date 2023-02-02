/**
 * 
 */
package jme.io;

import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolAdapterAtomIterator;
import org.jmol.api.JmolAdapterBondIterator;
import org.jmol.util.Edge;
import org.jmol.util.Elements;

import javajs.util.P3d;
import jme.core.Atom;
import jme.core.Bond;
import jme.core.JMECore;
import jme.ocl.SVGDepictorWithEmbeddedChemicalStructure;
import jme.util.Isotopes;
import jme.util.JMEUtil;

/**
 * @author bruno
 *
 */
public class JMEReader {

	public JMEReader() {
	}

	public enum MajorChemicalFormat {
		MOL, RXN, SDF, RDF, SMILES, SMARTS, SMIRKS, InChI, InChIkey, OCLCODE, JME, SVG, CSRML
	}

	public enum MinorChemicalFormat {
		V2000, V3000, extended
	}

	public enum Author {
		MDL, DAYLIGHT, IUPAC, OPENCHEMLIB, P_ERTL, MolecularNetworks
	}

	public MajorChemicalFormat majorChemicalFormat;
	public MinorChemicalFormat minorChemicalFormat;

	public Author author;
	public JMEReader embeddedChemicalFormat;

	protected int numberOfLines = 0;

	protected boolean isReaction;
	public boolean isInputEmpty = false;

	public boolean hasSpace;

	public String chemicalString;

	static protected JMEReader Empty = new JMEReader();

	final static Pattern InchiKeyPattern = Pattern.compile("^[A-Z]{14}\\-[A-Z]{10}\\-[A-Z]$");

	// From https://gist.github.com/lsauer/1312860
	// for smiles with length >= 6
	// final static Pattern Smiles6Pattern =
	// Pattern.compile("^([^J][a-z0-9@+\\-\\[\\]\\(\\)\\\\\\/%=#$]{6,})$");
	final static Pattern Smiles6Pattern = Pattern.compile("^[^J][a-z0-9@+\\-\\[\\]\\(\\)\\\\\\/%=#$:>]{6,}$",
			Pattern.CASE_INSENSITIVE);

	// TODO: j and other lowercase are not valid smiles atoms
	final static Pattern SmilesOrSmirksPattern = Pattern.compile("^[a-z0-9@+\\-\\[\\]\\(\\)\\\\\\/%=#$:>\\\\.]+$",
			Pattern.CASE_INSENSITIVE);
	final static Pattern NonSmilesPattern = Pattern.compile("j");

	final static Pattern SpacePattern = Pattern.compile("\\s+");

	final static Pattern Smartspattern = Pattern.compile("^[a-z0-9@+\\-\\[\\]\\(\\)\\\\\\/%=#$:\\\\.,;!&]+$",
			Pattern.CASE_INSENSITIVE);
	final static Pattern ExtendedSmartsExtraPattern = Pattern.compile("[\\^]"); // hybridisation in Chemotype Editor
	final static Pattern SmartsExtraPattern = Pattern.compile("[,;!&]", Pattern.CASE_INSENSITIVE);

	final static Pattern CSRMlpattern = Pattern.compile("\\s*^(<\\?xml\\s+[^>]+>)?\\s*<\\s*csrml\\b",
			Pattern.CASE_INSENSITIVE);

	final static Pattern URLpattern = Pattern.compile("$\\w+:\\/\\/"); // http://, file://

	
	public JMEReader(String chemicalString) {
		detectFormat(chemicalString);
	}

	public void reset() {
		init(Empty);

	}

	public boolean detectFormat(String chemicalString) {

		reset();
		boolean success = false;

		if (chemicalString == null || URLpattern.matcher(chemicalString).find()) {
			return false;
		}

		numberOfLines = countLines(chemicalString, 5);

		// remove leading and trailing white spaces only if # LINES > 0 BECAUSE THE
		// FIRST LINE OF A MOLFILE CAN BE EMPTY
		if (numberOfLines <= 1) {
			this.chemicalString = chemicalString.trim();
		} else {
			this.chemicalString = chemicalString;
		}
		if (this.chemicalString.length() == 0) {
			isInputEmpty = true;
			return false;
		}

		hasSpace = SpacePattern.matcher(this.chemicalString).find();

		do {

			// starts with CSRML because it is very specific
			if (CSRMlpattern.matcher(this.chemicalString).find()) {
				this.majorChemicalFormat = MajorChemicalFormat.CSRML;
				this.author = Author.MolecularNetworks;

				break;
			}

			if (numberOfLines > 4) {
				if (chemicalString.startsWith("<")) {
					if (chemicalString.toLowerCase().startsWith("<svg")) {
						// Extract the embedded chemical within the SVG
						String mol = SVGDepictorWithEmbeddedChemicalStructure
								.extractEmbeddedChemicalString(chemicalString);
						if (mol != null) {
							this.embeddedChemicalFormat = new JMEReader(mol);
							if (this.embeddedChemicalFormat.majorChemicalFormat != null) {
								majorChemicalFormat = MajorChemicalFormat.SVG;
							}
						}

					}
					break;
				}

				if (detectMDLformat()) {
					break;
				}

			}

			if (numberOfLines == 1) {
				if (chemicalString.startsWith("InChI=") || chemicalString.startsWith("AuxInfo=")) {
					majorChemicalFormat = MajorChemicalFormat.InChI;
					break;
				}

				if (chemicalString.length() == 27) {
					Matcher m = InchiKeyPattern.matcher(chemicalString);
					if (m.find()) {
						majorChemicalFormat = MajorChemicalFormat.InChIkey;
						break;
					}
				}
				if (chemicalString.length() >= 1) {
					if (hasSpace) {
						// try JME string
						StringTokenizer st = new StringTokenizer(chemicalString, " |");
						try {
							String next;
							next = st.nextToken();
							while (next.equals("|")) {
								next = st.nextToken();
							}
							int natomsx = Integer.valueOf(next).intValue();
							int nbondsx = Integer.valueOf(st.nextToken()).intValue();

							// 3 tokens for each atom and each bond
							for (int i = 0; i < 3 * (natomsx + nbondsx); i++) {
								st.nextToken();
							}
							isReaction = chemicalString.indexOf(">") > 0;

							majorChemicalFormat = MajorChemicalFormat.JME;
							author = Author.P_ERTL;
							break;
						} catch (Exception e) {
							// it is not a JME format or the JME input does not have coordinates
						}
					} else {

						if (!NonSmilesPattern.matcher(this.chemicalString).find()) {
							boolean isReaction = chemicalString.indexOf(">") > 0;
							boolean isSmarts = Smartspattern.matcher(this.chemicalString).find();
							boolean canBeExtendedSmarts = ExtendedSmartsExtraPattern.matcher(this.chemicalString)
									.find();
							boolean isSmiles = SmilesOrSmirksPattern.matcher(this.chemicalString).find()
									&& ! SmartsExtraPattern.matcher(this.chemicalString).find() && !canBeExtendedSmarts;

							if (isSmiles) {
								majorChemicalFormat = MajorChemicalFormat.SMILES;
								if (isReaction) {
									this.majorChemicalFormat = MajorChemicalFormat.SMIRKS;
									this.isReaction = isReaction;
								}

								// Note: a smiles is a valid smarts, there is no way to detect the intention
							} else 
							if (isSmarts || canBeExtendedSmarts) {
								this.majorChemicalFormat = MajorChemicalFormat.SMARTS;
								if (canBeExtendedSmarts) {
									this.minorChemicalFormat = MinorChemicalFormat.extended;
								}
							}

							break;

						}

					}

				}

			}
		} while (false);

		success = majorChemicalFormat != null;

		if (majorChemicalFormat == MajorChemicalFormat.InChIkey || majorChemicalFormat == MajorChemicalFormat.InChI) {
			author = Author.IUPAC;
		}
		if (majorChemicalFormat == MajorChemicalFormat.SMILES || majorChemicalFormat == MajorChemicalFormat.SMARTS
				|| majorChemicalFormat == MajorChemicalFormat.SMIRKS) {
			author = Author.DAYLIGHT;
		}
		return success;
	}

	protected boolean detectMDLformat() {
		if (chemicalString.contains("M  END") ||
		// if misspelled, check further
				(chemicalString.contains("M END")
						&& (chemicalString.contains("V2000") || chemicalString.contains("V3000")))) {
			author = Author.MDL;
			majorChemicalFormat = MajorChemicalFormat.MOL;
			if (chemicalString.contains("V2000")) {
				minorChemicalFormat = MinorChemicalFormat.V2000;
			}
			if (chemicalString.contains("V3000")) {
				minorChemicalFormat = MinorChemicalFormat.V3000;
			}

			if (chemicalString.startsWith("$RXN")) {
				majorChemicalFormat = MajorChemicalFormat.RXN;
			} else if (chemicalString.contains("$$$$")) {
				majorChemicalFormat = MajorChemicalFormat.SDF;
			}

			return true;
		}

		return false;

	}

	public boolean isReaction() {
		return isReaction || majorChemicalFormat == MajorChemicalFormat.RXN
				|| majorChemicalFormat == MajorChemicalFormat.SMIRKS;
	}

	public static int countLines(String str) {
		return countLines(str, -1);
	}

	/**
	 * http://stackoverflow.com/questions/2850203/count-the-number-of-lines-in-a-java-string
	 * 
	 * @param str
	 * @param stop : stop counting if number of lines found >= stop
	 * @return
	 */
	public static int countLines(String str, int stop) {
		if (str == null || str.length() == 0)
			return 0;
		int lines = 1;
		int len = str.length();
		for (int pos = 0; pos < len; pos++) {
			if (stop > 0 && lines > stop)
				return lines;

			char c = str.charAt(pos);
			if (c == '\r') {
				lines++;
				if (pos + 1 < len && str.charAt(pos + 1) == '\n')
					pos++;
			} else if (c == '\n') {
				lines++;
			}
		}
		return lines;
	}

	public boolean couldBeOclIdCode() {
		// TBC e.g it seems that each OCL has a "@"
		return !hasSpace;
	}

	public void init(JMEReader other) {
		this.author = other.author;
		this.majorChemicalFormat = other.majorChemicalFormat;
		this.minorChemicalFormat = other.minorChemicalFormat;
		this.isReaction = other.isReaction;
		this.embeddedChemicalFormat = other.embeddedChemicalFormat;
		this.chemicalString = other.chemicalString;

	}

	public boolean checkAndInitAsMDL() {
		return this.detectMDLformat();
	}

	// initialize my self as a MDL MOL v2000 format
	public JMEReader initAsV2000MOL() {
		reset();
		this.author = Author.MDL;
		this.majorChemicalFormat = MajorChemicalFormat.MOL;
		this.minorChemicalFormat = MinorChemicalFormat.V2000;
		this.isReaction = false;
		this.embeddedChemicalFormat = null;

		return this;
	}

	// initialize my self as a MDL MOL v3000 format
	public JMEReader initAsV3000MOL() {
		initAsV2000MOL();
		this.minorChemicalFormat = MinorChemicalFormat.V3000;

		return this;
	}

	public JMEReader initAsOClcode() {
		reset();
		author = Author.OPENCHEMLIB;
		majorChemicalFormat = MajorChemicalFormat.OCLCODE;

		return this;
	}

	/**
	 * compare all my fields except the chemicalString
	 */
	@Override
	public boolean equals(Object o) {
		// self check
		if (this == o)
			return true;
		// null check
		if (o == null)
			return false;
		// type check and cast
		if (getClass() != o.getClass())
			return false;
		JMEReader cfd = (JMEReader) o;

		// field comparison not the input chemical string
		return equals(author, cfd.author) && equals(majorChemicalFormat, cfd.majorChemicalFormat)
				&& equals(minorChemicalFormat, cfd.minorChemicalFormat)
				&& equals(isReaction, cfd.isReaction)
				&& equals(embeddedChemicalFormat, cfd.embeddedChemicalFormat);
		
		
	}

	// from Objects.equals, needed for unittests
	public static boolean equals(Object a, Object b) {
	        return (a == b) || (a != null && a.equals(b));
	    }

	public static void createJMEFromString(JMECore mol, String jmeString) {
		if (jmeString.startsWith("\""))
			jmeString = jmeString.substring(1, jmeString.length());
		if (jmeString.endsWith("\""))
			jmeString = jmeString.substring(0, jmeString.length() - 1);
		if (jmeString.length() < 1) {
			mol.natoms = 0;
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
				Atom atom = mol.createAtom(symbol);
				atom.x = Double.valueOf(st.nextToken()).doubleValue();
				atom.y = -Double.valueOf(st.nextToken()).doubleValue();
			}
			// --- bonds
			for (int i = 1; i <= nbondsx; i++) {
				Bond bond = mol.createAndAddBondFromOther(null);
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
			mol.finalizeMolecule();
		} // end of try
		catch (Exception e) {
			System.err.println("read JSME string exception - " + e.getMessage());
			// e.printStackTrace();
			mol.natoms = 0;
			throw (e);
		}
	}

	public static void createJMEFromMolData(JMECore mol, String molData) {

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

		mol.setChiralFlag(chiral == 1);

		// number of bonds to this atom including implicit H's
		int valences[] = new int[natomsx + 1];
		for (int i = 1; i <= natomsx; i++) {
			Atom atom = mol.createAtom();
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
			mol.setAtom(i, symbol); // sets an[i]

			// atom mapping - 61 - 63
			if (line.length() >= 62) {
				String s = line.substring(60, 63).trim();
				if (s.length() > 0) {
					int mark = Integer.valueOf(s).intValue();
					if (mark > 0) {
						mol.setAtomMapFromInput(i, mark);
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
						int iso = Isotopes.getIsotopicMassOfElementDelta(symbol, delta);
						if (iso < 0)
							iso = 0;
						mol.atoms[i].iso = iso;
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
						mol.Q(i, charge);
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
			Bond bond = mol.createAndAddBondFromOther(null);
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
					mol.atoms[a].q = Integer.valueOf(stq.nextToken()).intValue();
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
					mol.atoms[a].iso = Integer.valueOf(stq.nextToken()).intValue();

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
					mol.addBondToAtom(Bond.SINGLE, a, 0, false, 0);
					mol.setAtom(mol.natoms, "R" + nr);
				}
			}
		}

		// BB May 2017
		// compute the atom.nh if valence provided
		// example : AlH3 with implicit H's
		for (int i = 1; i <= natomsx; i++) {
			if (valences[i] > 0) {
				int nv = mol.atoms[i].nv;
				// valences[i] is the total number of bonds to this atom, including implicit H
				if (valences[i] != 15) {
					int nh = valences[i] - nv;
					if (nh > 0)
						mol.atoms[i].nh = nh;
				} else {
					mol.atoms[i].nh = 0;
				}
			}
		}
		mol.finalizeMolecule();
	}

	public static void createJMEFromJmolAdapter(JMECore mol, Object[] iterators) {
		JmolAdapterAtomIterator atomIterator = (JmolAdapterAtomIterator) iterators[0];
		JmolAdapterBondIterator bondIterator = (JmolAdapterBondIterator) iterators[1];
		Map<Object, Integer> atomMap = new Hashtable<Object, Integer>();
		while (atomIterator.hasNext()) {
			String sym = Elements.elementSymbolFromNumber(atomIterator.getElementNumber());
			// from Jmol -- could be 13C;
			Atom a = mol.createAtom(sym);
			atomMap.put(atomIterator.getUniqueID(), Integer.valueOf(mol.natoms));
			P3d pt = atomIterator.getXYZ();
			a.x = pt.x;
			a.y = -pt.y;
			a.q = atomIterator.getFormalCharge();
			mol.setAtom(mol.natoms, JmolAdapter.getElementSymbol(atomIterator.getElement()));
		}
		while (bondIterator.hasNext()) {
			Bond b = mol.createAndAddBondFromOther(null);
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

		mol.finalizeMolecule();
	}


}
