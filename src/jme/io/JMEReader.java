/**
 * 
 */
package jme.io;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

import jme.JME;
import jme.JMEmol;
import jme.JMEmolList;
import jme.core.Atom;
import jme.core.Bond;
import jme.core.JMECore;
import jme.core.JMECore.Parameters;
import jme.js.AsyncCallback;
import jme.util.Isotopes;
import jme.util.JMEUtil;

/**
 * @author bruno
 *
 */
public class JMEReader {


	/**
	 * Based on code in chem/IDCodeParserWithoutCoordinateInventor, 
	 * does a relatively thorough check that this is indeed an OCL IDCode.
	 * 
	 * @author hansonr
	 *
	 */
	private static class OclCheck {
		
		private int nAvail = 6;
		private int pt;
		private byte[] idcode;
		private int mData;
		private int abits;
		private int bbits;
		private int nBytes;

		private OclCheck() {}
		
		protected static boolean isOclIdCode(String s) {
			return new OclCheck().isIDCode(s);
		}
		
		private boolean isIDCode(String s) {
			// remove coordinates
			nBytes = s.indexOf('!');
			if (nBytes < 0)
				nBytes = s.indexOf('#');
			if (nBytes < 0)
				nBytes = s.length();
			if (nBytes < 10 || nBytes > 1000) // reasonable?
				return false;
			idcode = s.substring(0, nBytes).getBytes();
			mData = (idcode[0] & 0x3F) << 11;
			try {
				if (idcode == null || idcode.length == 0)
					return false;
				abits = decodeBits(4);
				bbits = decodeBits(4);
				int version = 8;
				if (abits > 8) {    
					// abits is the version number
					version = abits;
					abits = bbits;
				}
				if (version != 8 && version != 9)
					return false;
				int allAtoms = decodeBits(abits);
				int allBonds = decodeBits(bbits);
				int closureBonds = 1 + allBonds - allAtoms;
				if (allAtoms == 0 || closureBonds < 0 || closureBonds > allAtoms - 2)
					return false;
				int nitrogens = decodeBits(abits);
				int oxygens = decodeBits(abits);
				int otherAtoms = decodeBits(abits);
				int chargedAtoms = decodeBits(abits);
				checkBits(nitrogens);
				checkBits(oxygens);
				checkBits(otherAtoms);
				checkBits(chargedAtoms);
				return true;
			} catch (Throwable e) {
				return false;
			}
		}
		private void checkBits(int n) {
			if (n != 0) {
				for (int i = 0; i < n; i++)
					decodeBits(abits);
			}
		}
		
		private int decodeBits(int bits) {
			// loop for EVERY bit. Couldn't this have been done more
			// efficiently without reversing all the bits? 
			// I think so....
			int allBits = bits;
			int data = 0;
			while (bits != 0) {
				if (nAvail == 0) {
					if (++pt >= idcode.length)
						throw new NullPointerException();
					mData = (idcode[pt] & 0x3F) << 11;
					nAvail = 6;
				}
				data |= ((0x00010000 & mData) >> (16 - allBits + bits));
				mData <<= 1;
				bits--;
				nAvail--;
			}
			return data;
		}
	}
	
	
	private static enum MajorChemicalFormat {
		CDX, CDXML, MOL, RXN, SDF, SMILES, SMARTS, SMIRKS, InChI, InChIkey, OCLCODE, JME, SVG, CSRML
	}

	private static enum MinorChemicalFormat {
		V2000, V3000, extended
	}

	private static enum Author {
		MDL, DAYLIGHT, IUPAC, OPENCHEMLIB, P_ERTL, MolecularNetworks, RevitySignals
	}

	public static enum SupportedInputFileFormat {
		CDX, CDXML, INCHI, INCHIKEY, JME, SMARTS, SMILES, MOL, MOL_V3000, OCLCODE, RXN, SMIRKS
	}

	private MajorChemicalFormat majorChemicalFormat;
	private MinorChemicalFormat minorChemicalFormat;

	private Author author;
	private JMEReader embeddedChemicalFormat;

	protected boolean isReaction;

	private String chemicalString;

	private String error;
	
	public String getError() {
		return error;
	}

	private SupportedInputFileFormat fileTypeRead;
	
	public SupportedInputFileFormat getFileTypeRead() {
		return fileTypeRead;
	}

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

	
	private byte[] chemicalBytes;
	
	private boolean formatDetected;
	
	public boolean wasFormatDetected() {
		return formatDetected;
	}


	public JMEReader(Object chemicalStringOrBytes) {
		formatDetected = detectFormat(chemicalStringOrBytes);
	}

	private void reset() {
		init(null);

	}

	private static byte[] cdxMagic = new byte[] { 'V', 'j', 'C', 'D' };
	  
	private static boolean isCDXML(String s) {
		return (s.indexOf("<CDXML") >= 0);
	}

	private static boolean isCDX(byte[] b) {
		return bytesMatch(b, 0, cdxMagic);
	}

	private static boolean bytesMatch(byte[] a, int pt, byte[] b) {
		if (b.length > a.length - pt)
			return false;
		for (int i = b.length; --i >= 0;) {
			if (a[pt + i] != b[i])
				return false;
		}
		return true;
	}

	private boolean detectFormat(Object chemicalStringOrBytes) {
		reset();
		boolean success = false;

		if (chemicalStringOrBytes instanceof byte[]) {
			chemicalBytes = (byte[]) chemicalStringOrBytes;
			if (isCDX(chemicalBytes)) {
				majorChemicalFormat = MajorChemicalFormat.CDX;
				return true;
			}
			chemicalStringOrBytes = new String(chemicalBytes);
			chemicalBytes = null;
		}

		chemicalString = (String) chemicalStringOrBytes;
		if (chemicalString == null || URLpattern.matcher(chemicalString).find()) {
			return false;
		}

		if (isCDXML(chemicalString)) {
			majorChemicalFormat = MajorChemicalFormat.CDXML;
			return true;
		}

		int numberOfLines = countLines(chemicalString, 5);

		// remove leading and trailing white spaces only if # LINES > 0 BECAUSE THE
		// FIRST LINE OF A MOLFILE CAN BE EMPTY
		if (numberOfLines == 1) {
			chemicalString = chemicalString.trim();
		}
		if (chemicalString.length() == 0) {
			return false;
		}

		do {

			// starts with CSRML because it is very specific
			if (CSRMlpattern.matcher(chemicalString).find()) {
				majorChemicalFormat = MajorChemicalFormat.CSRML;
				break;
			}

			if (numberOfLines > 4) {
				if (chemicalString.startsWith("<")) {
					if (chemicalString.toLowerCase().startsWith("<svg")) {
						// Extract the embedded chemical within the SVG
						String mol = ChemicalMimeType.extractEmbeddedChemicalString(chemicalString);
						if (mol != null) {
							this.embeddedChemicalFormat = new JMEReader(mol);
							if (this.embeddedChemicalFormat.majorChemicalFormat != null) {
								majorChemicalFormat = MajorChemicalFormat.SVG;
							}
						}

					}
					break;
				}
				if (chemicalString.contains("M  END") ||
				// if misspelled, check further
						(chemicalString.contains("M END")
								&& (chemicalString.contains("V2000") || chemicalString.contains("V3000")))) {
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
					boolean hasSpace = SpacePattern.matcher(chemicalString).find();
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
							break;
						} catch (Exception e) {
							// it is not a JME format or the JME input does not have coordinates
						}
					} else if (OclCheck.isOclIdCode(chemicalString)) {
						majorChemicalFormat = MajorChemicalFormat.OCLCODE;
						break;
					} else if (!NonSmilesPattern.matcher(chemicalString).find()) {
						boolean isReaction = chemicalString.indexOf(">") > 0;
						boolean isSmarts = Smartspattern.matcher(chemicalString).find();
						boolean canBeExtendedSmarts = ExtendedSmartsExtraPattern.matcher(chemicalString).find();
						boolean isSmiles = SmilesOrSmirksPattern.matcher(chemicalString).find()
								&& !SmartsExtraPattern.matcher(chemicalString).find() && !canBeExtendedSmarts;

						if (isSmiles) {
							majorChemicalFormat = MajorChemicalFormat.SMILES;
							if (isReaction) {
								majorChemicalFormat = MajorChemicalFormat.SMIRKS;
								this.isReaction = isReaction;
							}

							// Note: a smiles is a valid smarts, there is no way to detect the intention
						} else if (isSmarts || canBeExtendedSmarts) {
							majorChemicalFormat = MajorChemicalFormat.SMARTS;
							if (canBeExtendedSmarts) {
								this.minorChemicalFormat = MinorChemicalFormat.extended;
							}
						}
						break;
					}

				}

			}
		} while (false);

		if (majorChemicalFormat != null) {
			success = true;
			setAuthor();
		}
		return success;
	}

	private void setAuthor() {
		switch (majorChemicalFormat) {
		case InChIkey:
		case InChI:
			author = Author.IUPAC;
			break;
		case SMILES:
		case SMARTS:
		case SMIRKS:
			author = Author.DAYLIGHT;
			break;
		case CSRML:
			author = Author.MolecularNetworks;
			break;
		case CDX:
		case CDXML:
			author = Author.RevitySignals;
			break;
		case JME:
			author = Author.P_ERTL;
			break;
		case MOL:
		case RXN:
		case SDF:
			author = Author.MDL;
			break;
		case OCLCODE:
			author = Author.OPENCHEMLIB;
			break;
		case SVG:
			// author set separately
			break;
		}
	}


	private boolean isReaction() {
		return isReaction || majorChemicalFormat == MajorChemicalFormat.RXN
				|| majorChemicalFormat == MajorChemicalFormat.SMIRKS;
	}

//	private static int countLines(String str) {
//		return countLines(str, -1);
//	}

	/**
	 * http://stackoverflow.com/questions/2850203/count-the-number-of-lines-in-a-java-string
	 * 
	 * @param str
	 * @param stop : stop counting if number of lines found >= stop
	 * @return
	 */
	private static int countLines(String str, int stop) {
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

	private void init(JMEReader other) {
		if (other == null) {
			author = null;
			majorChemicalFormat = null;
			minorChemicalFormat = null;
			isReaction = false;
			// don't clear the chemicalString or chemicalBytes
			return;
		}
		author = other.author;
		majorChemicalFormat = other.majorChemicalFormat;
		minorChemicalFormat = other.minorChemicalFormat;
		isReaction = other.isReaction;
		embeddedChemicalFormat = other.embeddedChemicalFormat;
		chemicalString = other.chemicalString;
		chemicalBytes = other.chemicalBytes;
	}

//	// initialize my self as a MDL MOL v2000 format
//	private JMEReader initAsV2000MOL() {
//		reset();
//		this.author = Author.MDL;
//		majorChemicalFormat = MajorChemicalFormat.MOL;
//		this.minorChemicalFormat = MinorChemicalFormat.V2000;
//		this.isReaction = false;
//		this.embeddedChemicalFormat = null;
//
//		return this;
//	}

//	// initialize my self as a MDL MOL v3000 format
//	private JMEReader initAsV3000MOL() {
//		initAsV2000MOL();
//		this.minorChemicalFormat = MinorChemicalFormat.V3000;
//		return this;
//	}

//	private JMEReader initAsOClcode() {
//		reset();
//		author = Author.OPENCHEMLIB;
//		majorChemicalFormat = MajorChemicalFormat.OCLCODE;
//		return this;
//	}

	public static JMEmolList readMDLstringInput(String s, Parameters pars) {
		JMEmolList molList = new JMEmolList();
		try {
			molList.isReaction = s.startsWith("$RXN");
			if (molList.isReaction) {
				molList.addAll(readReactionMols(s, pars));
			} else {
				molList.add(readSingleMOL(s, pars));
			}
		} catch (Exception e) {
			molList.error = e;
			return null;
		}
		return molList;
	}

	/*
	 * parse and read the argument and put the molecules into a vector
	 *
	 */
	// TODO: handle input with 3D or no coordinates
	public static JMEmolList readJMEstringInput(String molecule, Parameters pars) {

		// | is molecular separator
		// > is reaction separator
		JMEmolList molList = new JMEmolList();
		StringTokenizer st = new StringTokenizer(molecule, "|>", true);
		molList.isReaction = (molecule.indexOf(">") > -1); // false means it is a molecule
		int nt = st.countTokens();
		int roleIndex = 0;

		for (int i = 1; i <= nt; i++) {
			String s = st.nextToken();
			s.trim();
			if (s.equals("|"))
				continue;
			if (s.equals(">")) {
				roleIndex++;
				continue;
			}

			if (roleIndex > 3) {
				return molList.setErrorMsg("too many \">\"");
			}

			JMEmol mol = null;

			try {
				mol = new JMEmol(null, s, JMEReader.SupportedInputFileFormat.JME, pars);
				if (mol.natoms == 0) {
					// this.showError("problems in reading/processing molecule !"); //old original
					// error message
					return molList.setErrorMsg("0 atoms found in \"" + s + "\"");
				}

			} catch (Exception e) {
				return molList.setErrorMsg(JME.makeErrorMessage(e) + ": " + s);
			}
			molList.add(mol);
			if (molList.isReaction) {
				mol.setReactionRole(JMEmol.ReactionRole.all[roleIndex]);
			}
		}
		return molList;
	}

	private static List<JMEmol> readReactionMols(String s, Parameters pars) throws Exception {
		List<JMEmol> mols = new ArrayList<>();
			String separator = JMEUtil.findLineSeparator(s);
			StringTokenizer st = new StringTokenizer(s, separator, true);
			String line = "";
			for (int i = 1; i <= 5; i++) {
				line = JMEUtil.nextData(st, separator);
			}
			// TODO: exception handling
			int nr = Integer.valueOf(line.substring(0, 3).trim()).intValue();
			int np = Integer.valueOf(line.substring(3, 6).trim()).intValue();
			// support of agents, this is not standard, same convention as in Marvin JS
			int na = 0;
			if (line.length() >= 9) {
				na = Integer.valueOf(line.substring(6, 9).trim()).intValue();
			}
	
			JMEUtil.nextData(st, separator); // 1. $MOL
			for (int p = 1; p <= nr + np + na; p++) {
				String m = "";
				while (true) {
					String ns = JMEUtil.nextData(st, separator);
					if (ns == null || ns.equals("$MOL"))
						break;
					else
						m += ns + separator;
				}
				// System.err.print("MOLS"+p+separator+m);
				JMEmol mol = readSingleMOL(m, pars);
				mols.add(mol);
				if (p <= nr) {
					mol.setReactionRole(JMEmol.ReactionRole.REACTANT);
				} else if (p > nr && p <= nr + np) {
					mol.setReactionRole(JMEmol.ReactionRole.PRODUCT);
				} else {
					mol.setReactionRole(JMEmol.ReactionRole.AGENT);
				}
			}
			return mols;
	}

	private static JMEmol readSingleMOL(String s, Parameters pars) throws Exception {
		return new JMEmol(null, s, JMEReader.SupportedInputFileFormat.MOL, pars);
	}

	public static void createMolFromString(JMECore mol, String jmeString) {
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

	public static void createMolFromMolData(JMECore mol, String molData) {

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

	public void readMoleculeData(JME jme, boolean runAsync, AsyncCallback callback, boolean recordEvent,
			boolean repaint) {
		if (majorChemicalFormat == MajorChemicalFormat.SVG && embeddedChemicalFormat != null) {
			// copy the embedded chemical format to jmeReader
			init(embeddedChemicalFormat);
		}
		if (author == Author.MDL && minorChemicalFormat != MinorChemicalFormat.V3000) {
			// bug: handling "|" as a line separator

			// TODO : handleReadMolFileRXN is async because of the 2D coordinate computation
			if (jme.handleReadMolFileRXN(chemicalString, false))
				fileTypeRead = isReaction() ? SupportedInputFileFormat.RXN : SupportedInputFileFormat.MOL;
			else {
				error = "Invalid V2000 molfile";
			}
			return;
		}

		if (author == Author.P_ERTL) {
			if (jme.readMolecule(chemicalString, false)) { // will do repaint later after event recording
				fileTypeRead = SupportedInputFileFormat.JME;
			} else {
				error = "Invalid JME string";
			}
			return;
		}
		if (majorChemicalFormat == MajorChemicalFormat.CSRML) {
			error = "Reading " + majorChemicalFormat + " is not supported";
			return;
		}
		Runnable r = () -> {
			oclSuccess(jme, callback, recordEvent, repaint);
		};
		if (runAsync) {
			// code splitting used to run OpenChemlib code
			SwingUtilities.invokeLater(r);
		} else {
			r.run();
		}
	}

	private void oclSuccess(JME jme, AsyncCallback callback, boolean recordEvent, boolean repaint) {
		String error = null;
		String convertedmolFile = null;
		SupportedInputFileFormat fileTypeRead = null;
		switch (majorChemicalFormat) {
		case MOL:
			if (minorChemicalFormat == MinorChemicalFormat.V3000) {
				try {
					convertedmolFile = v3000toV2000MOL(chemicalString);
					if (convertedmolFile == null) {
						throw new Exception("V3000 read failed.");
					}
					fileTypeRead = SupportedInputFileFormat.MOL_V3000;
					jme.sdfPastedMessage.innerString = "V3000 conversion provided by OpenChemLib";
				} catch (Exception e) {
					error = e.getMessage();
				}
				break;
			}
			break;
		case SMARTS:
		case SMILES:
		case SMIRKS:
			try {
				convertedmolFile = jme.SMILESorSMIRKStoMolOrRXN(chemicalString);
				switch (majorChemicalFormat) {
				case SMIRKS:
					fileTypeRead = SupportedInputFileFormat.SMIRKS;
					break;
				case SMILES:
					fileTypeRead = SupportedInputFileFormat.SMILES;
					break;
				case SMARTS:
					fileTypeRead = SupportedInputFileFormat.SMARTS;
				default:
					break;
				}
				jme.sdfPastedMessage.innerString = "SMILES conversion provided by OpenChemLib";
			} catch (Exception e) {
				error = "SMILES parsing error:" + e.getMessage();
			}
			break;
		case OCLCODE:
			// try to parse OCL if not SMILES
			// ChemicalFormatDetector can not detect OCLcode
			try {
				convertedmolFile = jme.oclCodeToMOL(chemicalString);
				fileTypeRead = SupportedInputFileFormat.OCLCODE;
				error = null;
			} catch (Exception e) {
				error = "OCL parsing failed";
			}
			break;
		case CDX:
			try {
				convertedmolFile = jme.cdxToMOL(chemicalBytes);
				fileTypeRead = SupportedInputFileFormat.CDX;
				error = null;
			} catch (Exception e) {
				error = "CDX parsing failed";
			}
			break;
		case CDXML:
			try {
				convertedmolFile = jme.cdxmlToMOL(chemicalString);
				fileTypeRead = SupportedInputFileFormat.CDXML;
				error = null;
			} catch (Exception e) {
				error = "CDXML parsing failed";
				e.printStackTrace();
			}
			break;
		case CSRML:
		case InChI:
			try {
				convertedmolFile = jme.inchiToMOL(chemicalString);
				fileTypeRead = SupportedInputFileFormat.INCHI;
				error = null;
			} catch (Exception e) {
				error = "InChI parsing failed";
			}
			break;
		case InChIkey: 
			try {
				convertedmolFile = jme.inchikeyToMOL(chemicalString);
				fileTypeRead = SupportedInputFileFormat.INCHIKEY;
				error = null;
			} catch (Exception e) {
				error = "InChIKey parsing failed";
			}
			break;
		case JME:
		case RXN:
		case SDF:
		case SVG:
			// these are all already handled
			break;
		}
		if (convertedmolFile != null && error == null) {
			boolean success = false;
			try {
				success = jme.handleReadMolFileRXN(convertedmolFile, false);
			} catch (Exception e) {
			}
			if (!success)
				error = "Invalid molfile data";
		}

		jme.processFileRead(callback, fileTypeRead, error, repaint);
	}

	/**
	 * Use the openchemlib to convert a V3000 MOL to a V2000 molfile string
	 * 
	 * @param v3000
	 * @return
	 * @throws Exception
	 */
	private static String v3000toV2000MOL(String v3000Mol) throws Exception {
		return JME.getOclAdapter().v3000toV2000MOL(v3000Mol);
	}

	/**
	 * compare all my fields except the chemicalString
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || o.getClass() != getClass())
			return false;
		JMEReader r = (JMEReader) o;
		// field comparison not the input chemical string
		return equals(author, r.author) && equals(majorChemicalFormat, r.majorChemicalFormat)
				&& equals(minorChemicalFormat, r.minorChemicalFormat)
				&& equals(isReaction, r.isReaction)
				&& equals(embeddedChemicalFormat, r.embeddedChemicalFormat);
	}

	private static boolean equals(Object a, Object b) {
	        return (a == b) || (a != null && a.equals(b));
	    }

}
