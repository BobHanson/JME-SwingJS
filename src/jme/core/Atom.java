/**
 * 
 */
package jme.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jme.core.JMECore.Parameters;

/**
 * @author bruno
 *
 */
public class Atom implements AtomBondCommon {

	public static final int AN_H = 1;
	public static final int AN_B = 2;
	public static final int AN_C = 3;
	public static final int AN_N = 4;
	public static final int AN_O = 5;
	public static final int AN_SI = 6;
	public static final int AN_P = 7;
	public static final int AN_S = 8;
	public static final int AN_F = 9;
	public static final int AN_CL = 10;
	public static final int AN_BR = 11;
	public static final int AN_I = 12;
	public static final int AN_SE = 13;

	// BB
	// https://en.wikipedia.org/wiki/List_of_oxidation_states_of_the_elements
	public static final int AN_K = 14;
	public static final int AN_METAL1_START = AN_K;
	public static final int AN_Na = 15;
	public static final int AN_Li = 16;
	public static final int AN_Rb = 17;
	public static final int AN_Cs = 18;
	public static final int AN_Fr = 19;
	public static final int AN_Ag = 20;
	public static final int AN_METAL1_END = AN_Ag;

	public static final int AN_Mg = AN_METAL1_END + 1;
	public static final int AN_METAL2_START = AN_Mg;
	public static final int AN_Ca = AN_Mg + 1;
	public static final int AN_Ba = AN_Ca + 1;
	public static final int AN_Sr = AN_Ba + 1;;
	public static final int AN_Zn = AN_Sr + 1;
	public static final int AN_Ni = AN_Zn + 1;
	public static final int AN_Cu = AN_Ni + 1;
	public static final int AN_Cd = AN_Cu + 1;

	public static final int AN_METAL2_END = AN_Cd;

	public static final int AN_METAL3_START = AN_METAL2_END + 1;
	public static final int AN_Al = AN_METAL3_START;
	public static final int AN_Ga = AN_Al + 1;
	public static final int AN_Au = AN_Ga + 1;
	public static final int AN_METAL3_END = AN_Au;

	public static final int AN_X = AN_METAL3_END + 1;

	public static final int AN_R = AN_X + 1;
	// public static final int AN_R1 = 20;
	// public static final int AN_R2 = 21;
	// public static final int AN_R3 = 22;
	// added by BB
	public static final int AN_R_LAST = AN_R + 9; // keep the 9! 1 value for each R

	public static int chargedMetalType(int an) {
		if (an >= AN_METAL1_START && an <= AN_METAL1_END)
			return 1; // Na+
		if (an >= AN_METAL2_START && an <= AN_METAL2_END)
			return 2; // Ca++
		if (an >= AN_METAL3_START && an <= AN_METAL3_END)
			return 3; // Al+++
		return 0;
	}

	public static final String zlabel[] = new String[AN_R_LAST + 1];

	public static void atomicData() {
		zlabel[AN_X] = "X";
		zlabel[AN_H] = "H";
		zlabel[AN_B] = "B";
		zlabel[AN_C] = "C";
		zlabel[AN_N] = "N";
		zlabel[AN_O] = "O";
		zlabel[AN_F] = "F";
		zlabel[AN_CL] = "Cl";
		zlabel[AN_BR] = "Br";
		zlabel[AN_I] = "I";
		zlabel[AN_S] = "S";
		zlabel[AN_P] = "P";
		zlabel[AN_SI] = "Si";
		zlabel[AN_SE] = "Se";
		zlabel[AN_X] = "X";
		zlabel[AN_K] = "K";
		zlabel[AN_Li] = "Li";
		zlabel[AN_Na] = "Na";
		zlabel[AN_Rb] = "Rb";
		zlabel[AN_Cs] = "Cs";
		zlabel[AN_Fr] = "Fr";
		zlabel[AN_Ag] = "Ag";
		zlabel[AN_Mg] = "Mg";
		zlabel[AN_Ca] = "Ca";
		zlabel[AN_Sr] = "Sr";
		zlabel[AN_Ba] = "Ba";
		zlabel[AN_Zn] = "Zn";
		zlabel[AN_Ni] = "Ni";
		zlabel[AN_Cu] = "Cu";
		zlabel[AN_Cd] = "Cd";

		zlabel[AN_Al] = "Al";
		zlabel[AN_Ga] = "Ga";
		zlabel[AN_Au] = "Au";

		for (int i = AN_R; i <= AN_R_LAST; i++) {
			zlabel[i] = "R" + (i > AN_R ? (i - AN_R) : "");
		}
	}
	
	static {
		atomicData();
	}

	int map = NOT_MAPPED_OR_MARKED;
	int mark = NOT_MAPPED_OR_MARKED;

	/**
	 * indices of the colors defined in the current color palette
	 */
	public int backgroundColors[];

	public int iso; // BB isotope
	public double x;
	public double y;
	public double z;

	public int q = 0;

	public int partIndex; // ensemble index, if a ensemble is merged, this index gives molecule
	public boolean deleteFlag;

	public String label;
	
	public int[] v = new int[JMECore.MAX_BONDS_ON_ATOM + 1];

	public int nv;

	public int an = AN_C;
	public int nh = 0;
	public int sbo; // BB sum of bond order
	public String atag;

	public Atom() {
		resetBackgroundColors();
	}

	public Atom deepCopy() {
		return copyTo(new Atom());
	}
	
	public Atom copyTo(Atom a) {
		a.backgroundColors = AtomBondCommon.copyArray(backgroundColors);
		a.mark = this.mark;
		a.map = this.map;
		a.iso = this.iso;
		a.x = this.x;
		a.y = this.y;
		a.z = this.z;
		a.q = this.q;
		a.label = this.label;
		a.v = AtomBondCommon.copyArray(this.v);
		a.nv = this.nv;
		a.an = this.an;
		a.atag = this.atag;
		a.nh = this.nh;
		a.sbo = this.sbo;
		a.partIndex = this.partIndex;
		a.deleteFlag = this.deleteFlag;
		return a;
	}

	/**
	 * add another atom to my adjacency list
	 * 
	 * @param neighbor
	 */
	public void addNeighbor(int neighbor) {
		if (this.nv < JMECore.MAX_BONDS_ON_ATOM) {
			this.nv++;
			this.v[this.nv] = neighbor;
		}
	}

	public boolean hasBeenMapped() {
		return this.map != NOT_MAPPED_OR_MARKED;
	}

	public boolean resetObjectMark() {

		return this.resetMap();
	}

	public boolean resetMap() {
		boolean hasChanged = hasBeenMapped();
		this.map = NOT_MAPPED_OR_MARKED;

		return hasChanged;
	}

	public int getMap() {
		return hasBeenMapped() ? this.map : 0;
	}

	public int getMapOrMark(boolean isMap) {
		return isMap ? getMap() : getMark();

	}

	public void setMapOrMark(int m, boolean isMap) {
		if (isMap)
			setMap(m);
		else
			setMark(m);
	}

	public void setMap(int map) {
		this.map = map;
	}

	public boolean isMapped() {
		return getMap() != 0;
	}

	// used for template
	public boolean isMappedOrMarked() {
		return this.isMapped() || this.getMark() != 0;
	}

	public boolean isCumuleneSP() {
		return this.sbo >= 4 && this.nv == 2;
	}

	public void moveXY(double dx, double dy) {
		this.x += dx;
		this.y += dy;
	}

	public void scaleXY(double scale) {
		this.x *= scale;
		this.y *= scale;
	}

	public void XY(double x, double y) {
		this.x = x;
		this.y = y;

	}

	public int iso() {
		return this.iso;
	}

	public int q() {
		return this.q;
	}

	public void Q(int charge) {
		this.q = charge;
	}

	public void incrQ(int incr) {
		this.q += incr;
	}

	public double squareDistance(Atom other) {
		return Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2);
	}

	public boolean hasCloseContactWith(Atom other, double minDistance) {
		double dist = this.squareDistance(other);
		return dist < Math.pow(minDistance, 2);
	}

	/*
	 * /** Parse an atomic symbol like 13C. Set the isotope at the atom index if
	 * found. Return the symbol with the isotopic part removed.
	 * 
	 * @param symbol
	 */
	final static Pattern atomicSymbolPattern = Pattern.compile("^(\\d+)([A-Z][a-z]?)(\\b.*)");

	String parseAtomSymbolIsotop(String symbol) {
		this.iso = 0;
		Matcher m = atomicSymbolPattern.matcher(symbol);
		if (m.find()) {
			int isomass = Integer.parseInt(m.group(1));
			String element = m.group(2);
			if (AtomicElements.isKnown(element, isomass)) {
				// iso[atomIndex] = isomass;
				this.iso = isomass;
				symbol = element + m.group(3); // add the rest of the match to the symbol

			}

		}
		return symbol;

	}

	// use (\\d+)? over (\\d*) because it gives a null group instead of an empty
	// string
	final static Pattern atomicSymbolPatternIsotopAndCharge = Pattern.compile("^(\\d+)?" // group 1: isotopic number
			+ "\\s*" + "([A-Z][a-z]?)" // group 2 : Atomic symbol - should we use 3 letters for recent elements?
			+ "\\s*" + "(H(\\d*))?" // group 3: H count

			+ "(?:" + "(?:([+-])(\\d*))" // group 4: a + or - followed by a number, e.g. +2, 1-1
			+ "|" + "((?:\\++)|(?:-+))" // group 5: charge, e.g +, ++ -, ----, +- does not match

			+ ")?" + "([^:+-]+?" // group 6 anything,
			+ "([,;#!])?" // group 7: check for a query symbol (SMARTS?)
			+ "[^:+-]+?)?" // group 8 anything,
			+ "(?::(\\d+))?$" // group 10: map number, e.g. :5

	);

	// will not match Na+-

	/*
	 * boolean isQuery = false; if (symbol.indexOf(",") > -1) isQuery = true; if
	 * (symbol.indexOf(";") > -1) isQuery = true; if (symbol.indexOf("#") > -1)
	 * isQuery = true; if (symbol.indexOf("!") > -1) isQuery = true;
	 */

	/**
	 * Parse strings like 13C to find isotope and charge. Check validity of the
	 * element symbol and isotope. If the element symbol, the isotope and the charge
	 * are valid, then set my charge and isotope accordingly, returns the element
	 * symbol extracted from the input string. If parsing fails or if the isotope is
	 * not correct, return the input string. Checking if the charge is valid for the
	 * the given element is not performed.
	 * 
	 * @param symbol
	 * @return element or symbol
	 */

	/**
	 * TODO: handling of query symbol TODO: handling of H count See setAtom()
	 * 
	 * 
	 */
	public String parseAtomicSymbolPatternIsotopMappAndCharge(String symbol,
			Parameters parameters) {

		Matcher m = atomicSymbolPatternIsotopAndCharge.matcher(symbol);
		if (m.find()) {
			String iso = m.group(1);
			String element = m.group(2);
			String hCount = m.group(3);
			String hCountNumber = m.group(4);
			String chargeNumber = m.group(6); // 2
			String chargeSign = m.group(5); // +
			String multiCharge = m.group(7); // ++
			String query = m.group(8);
			// String hasQuerySymbol = m.group(7);
			String atomMap = m.group(10); // :3 for an atom map
			int charge = 0;

			boolean isValid = true;

			if (AtomicElements.getNaturalMass(element) != -1) {
				if (iso != null && iso.length() > 0) { // the string length has to be tested because of the different
														// behavior of IE
					int isomass = Integer.parseInt(iso);

					if (AtomicElements.isKnown(element, isomass)) {
						this.iso = isomass;
					} else {
						isValid = false;
					}
				}

			} else {
				isValid = false;
			}

			// charge
			if (isValid) {

				boolean hasChargeSign = chargeSign != null && chargeSign.length() > 0;
				boolean hasChargeNumber = chargeNumber != null && chargeNumber.length() > 0;
				boolean hasMultiCharge = multiCharge != null && multiCharge.length() > 0;

				if (hasChargeSign || hasChargeNumber) {
					// design of the regexp: either (chargeNumber and chargeSign) or multiCharge
					charge = 1;
					if (hasChargeNumber) {
						charge = Integer.parseInt(chargeNumber);
					}
					charge *= chargeSign.equals("-") ? -1 : 1;

				} else if (hasMultiCharge) {
					charge = multiCharge.length();
					charge *= multiCharge.equals("-") ? -1 : 1;
				}

			}
			if (isValid) {
				symbol = element + (query != null ? query : "");
				this.q = charge;
			}

			if (atomMap != null && atomMap.length() > 0) {
				try {
					int map = Integer.parseInt(atomMap);
					if (map > 0) {
						if (parameters.mark) {
							this.setMark(map);
						}
						if (parameters.number) {
							this.setMap(map);
						}

					}
				} catch (Exception e) {
					// TODO: show error

				}

			}
			if (hCount != null) {
				this.nh = 1;
				if (hCountNumber != null && hCountNumber.length() > 0)
					this.nh = Integer.parseInt(hCountNumber);
			}

		}

		return symbol;
	}

	public String getLabel() {
		return (an == AN_X ? label : Atom.zlabel[an]);
	}

///   AtomBondCommon

	/**
	 * Add a new background color unless it is not already present
	 * 
	 * @param c
	 */
	public void addBackgroundColor(int c) {
		for (int i = 0; i < backgroundColors.length; i++) {
			if (backgroundColors[i] == c) {
				return;
			}
			;
		}
		backgroundColors = AtomBondCommon.growArray(backgroundColors, backgroundColors.length + 1);
		backgroundColors[backgroundColors.length - 1] = c;
	}

	public void resetBackgroundColors() {
		backgroundColors = new int[] { NOT_MAPPED_OR_MARKED };
	}

	/**
	 * Reset the mark to not marked.
	 * 
	 * @return true if changed
	 */
	public boolean resetMark() {
		if (mark == NOT_MAPPED_OR_MARKED)
			return false;
		mark = NOT_MAPPED_OR_MARKED;
		return true;
	}

	public int getMark() {
		return Math.max(mark, 0);
	}

	public void setMark(int markOrMap) {
		mark = markOrMap;
	}

	public boolean isMarked() {
		return mark > 0;
	}

	@Override
	public String toString() {
		return "[Atom " + getLabel() + " " + x + " " + y + "]";
	}

	@Override
	public int[] getBackgroundColors() {
		return  backgroundColors;
	}

	// ----------------------------------------------------------------------------
	/**
	 * Return the JME atoming number associated to the given symbol
	 * 
	 * @param s
	 * @return
	 */
	public static int checkAtomicSymbol(String s) {
	
		// BB simplification
		for (int an = 1; an < zlabel.length; an++) {
			if (s.equals(zlabel[an]))
				return an;
		}
		// there is a problem for R groups beyond R9: it will be interpreted as AN_X
		// see also protected int mapActionToAtomNumber(int action, int notFound) {
		return AN_X;
	}
	// ----------------------------------------------------------------------------
}
