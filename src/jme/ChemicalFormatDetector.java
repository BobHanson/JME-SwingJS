/**
 * 
 */
package jme;

import java.util.Objects;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bruno
 *
 */
public class ChemicalFormatDetector {
	/**
	 * 
	 */
	public ChemicalFormatDetector() {
	}

	public ChemicalFormatDetector(String chemicalString) {
		detectFormat(chemicalString);
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
	public ChemicalFormatDetector embeddedChemicalFormat;

	protected int numberOfLines = 0;

	protected boolean isReaction;
	public boolean isInputEmpty = false;

	public boolean hasSpace;

	public String chemicalString;

	static protected ChemicalFormatDetector Empty = new ChemicalFormatDetector();

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

	
	// from Objects.equals, needed for unittests
	public static boolean equals(Object a, Object b) {
	        return (a == b) || (a != null && a.equals(b));
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
				this.majorChemicalFormat = ChemicalFormatDetector.MajorChemicalFormat.CSRML;
				this.author = ChemicalFormatDetector.Author.MolecularNetworks;

				break;
			}

			if (numberOfLines > 4) {
				if (chemicalString.startsWith("<")) {
					if (chemicalString.toLowerCase().startsWith("<svg")) {
						// Extract the embedded chemical within the SVG
						String mol = SVGDepictorWithEmbeddedChemicalStructure
								.extractEmbeddedChemicalString(chemicalString);
						if (mol != null) {
							this.embeddedChemicalFormat = new ChemicalFormatDetector(mol);
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
									this.majorChemicalFormat = ChemicalFormatDetector.MajorChemicalFormat.SMIRKS;
									this.isReaction = isReaction;
								}

								// Note: a smiles is a valid smarts, there is no way to detect the intention
							} else 
							if (isSmarts || canBeExtendedSmarts) {
								this.majorChemicalFormat = ChemicalFormatDetector.MajorChemicalFormat.SMARTS;
								if (canBeExtendedSmarts) {
									this.minorChemicalFormat = ChemicalFormatDetector.MinorChemicalFormat.extended;
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

	public void init(ChemicalFormatDetector other) {
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
	public ChemicalFormatDetector initAsV2000MOL() {
		reset();
		this.author = Author.MDL;
		this.majorChemicalFormat = MajorChemicalFormat.MOL;
		this.minorChemicalFormat = MinorChemicalFormat.V2000;
		this.isReaction = false;
		this.embeddedChemicalFormat = null;

		return this;
	}

	// initialize my self as a MDL MOL v3000 format
	public ChemicalFormatDetector initAsV3000MOL() {
		initAsV2000MOL();
		this.minorChemicalFormat = MinorChemicalFormat.V3000;

		return this;
	}

	public ChemicalFormatDetector initAsOClcode() {
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
		ChemicalFormatDetector cfd = (ChemicalFormatDetector) o;

		// field comparison not the input chemical string
		return ChemicalFormatDetector.equals(author, cfd.author) && ChemicalFormatDetector.equals(majorChemicalFormat, cfd.majorChemicalFormat)
				&& ChemicalFormatDetector.equals(minorChemicalFormat, cfd.minorChemicalFormat)
				&& ChemicalFormatDetector.equals(isReaction, cfd.isReaction)
				&& ChemicalFormatDetector.equals(embeddedChemicalFormat, cfd.embeddedChemicalFormat);
		
		
	}

}
