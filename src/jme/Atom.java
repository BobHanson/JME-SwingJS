/**
 * 
 */
package jme;

import java.awt.FontMetrics;
//START JAVA_IMPORT
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//END GWT_IMPORT

/**
 * @author bruno
 *
 */
public class Atom extends AtomBondCommon {
	protected int map = NOT_MAPPED_OR_MARKED;

	int iso; //BB isotope
	double x;
	double y;
	public double z;
	
	// coordinates used for output (MOL, JSME)
	public double xo;
	public double yo;

	int q = 0;

	int partIndex; // ensemble index, if a ensemble is merged, this index gives molecule
	boolean deleteFlag;

	public String label;
	int[] v = new int[JMEmol.MAX_BONDS_ON_ATOM+1];

	int nv;

	int an = JME.AN_C;
	int nh = 0;
	int sbo; //BB sum of bond order
	String atag;
	
	AtomDisplayLabel al;

	public Atom deepCopy() {
		Atom copy = new Atom();
		
		this.initOtherFromMe(copy);

		return copy;
	}

	
	public void initOtherFromMe(Atom otherAtom) {
		super.initOtherFromMe(otherAtom);
		
		otherAtom.map = this.map;
		otherAtom.iso = this.iso;

		otherAtom.x = this.x;
		otherAtom.y = this.y;
		otherAtom.z = this.z;

		otherAtom.xo = this.xo;
		otherAtom.yo = this.yo;

		otherAtom.q = this.q;
		otherAtom.label = this.label;

		otherAtom.v = JMEUtil.copyArray(this.v);
		otherAtom.nv = this.nv;


		otherAtom.an = this.an;
		otherAtom.atag = this.atag;

		otherAtom.nh = this.nh;
		otherAtom.sbo = this.sbo;

		otherAtom.partIndex = this.partIndex;
		otherAtom.deleteFlag = this.deleteFlag;
		
		//otherAtom.backgroundColorIndex = this.backgroundColorIndex;

	}
	/**
	 * add another atom to my adjacency list
	 * @param neighbor
	 */
	public void addNeighbor(int neighbor) {
		if(this.nv < JMEmol.MAX_BONDS_ON_ATOM) {
			this.nv ++;
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
		boolean hasChanged =  hasBeenMapped();
		this.map = NOT_MAPPED_OR_MARKED;

		return hasChanged;
	}

	public int getMap() {
		return hasBeenMapped()? this.map:0;
	}

	
	
//	public int getMarkerAsMap(boolean markerMultiColor) {
//		if (markerMultiColor && this.backgroundColorIndex >= 0) { // active marker was enabled in JME
//			return this.backgroundColorIndex + 1;
//		}
//		if (backgroundColors.length > 0 && backgroundColors[0] > 0)  {
//			return 1; // any color
//		}
//		return 0;
//	}

	

	
	public int getMapOrMark(boolean isMap) {
		return isMap ? getMap() : getMark();
		
	}
	
	public void setMapOrMark(int m, boolean isMap) {
		if(isMap)
			setMap(m);
		else
			setMark(m);
		
	}
//	public void setMarkedMap(int map) {
//		if (markerMultiColor) {
//			this.backgroundColorIndex = map -1;
//		} else {
//			this.setMap(map);
//		}
//		
//	}
	void setMap(int map) {
		this.map = map;
	}

	
	boolean isMapped() {
		return this.getMap() != 0;
	}

	// used for template
	boolean isMappedOrMarked() {
		return this.isMapped() || this.getMark() != 0;
	}
	
	public	boolean isCumuleneSP() {
		return this.sbo >= 4 && this.nv==2;
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
		this.x = x; this.y=y;

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
		return Math.pow(x-other.x, 2)+ Math.pow(y-other.y, 2);
	}
	
	public boolean hasCloseContactWith(Atom other, double minDistance) {
		double dist = this.squareDistance(other);
		return dist < Math.pow(minDistance,  2);
	}
	/*
	/**
	 * Parse an atomic symbol like 13C. Set the isotope at the atom index if found.
	 * Return the symbol with the isotopic part removed.
	 * @param symbol
	 */
	final static Pattern atomicSymbolPattern = Pattern.compile("^(\\d+)([A-Z][a-z]?)(\\b.*)");

	String parseAtomSymbolIsotop(String symbol) {
		this.iso = 0;
		Matcher m = atomicSymbolPattern.matcher(symbol);
		if(m.find()) {
			int isomass = Integer.parseInt(m.group(1));
			String element =  m.group(2);
			if(AtomicElements.isKnown(element, isomass)) {
				//iso[atomIndex] = isomass;
				this.iso = isomass;
				symbol = element + m.group(3); //add the rest of the match to the symbol

			}

		}
		return symbol;

	}
	//use (\\d+)? over (\\d*) because it gives a null group instead of an empty string
	final static Pattern atomicSymbolPatternIsotopAndCharge = Pattern.compile(
			"^(\\d+)?" //group 1: isotopic number
			+"\\s*"
			+ "([A-Z][a-z]?)" //group 2 : Atomic symbol - should we use 3 letters for recent elements?
			+"\\s*"
			+"(H(\\d*))?" //group 3: H count

			+ "(?:"
			+	"(?:([+-])(\\d*))" //group 4: a + or -  followed by a number, e.g. +2, 1-1
			+		"|"
			+ 	"((?:\\++)|(?:-+))" // group 5: charge, e.g +, ++ -, ----,  +- does not match
			
			+ ")?" 
			+ "([^:+-]+?" //group 6 anything,
			+	"([,;#!])?" //group 7: check for a query symbol (SMARTS?)
			+ "[^:+-]+?)?" //group 8 anything,
			+ "(?::(\\d+))?$" // group 10: map number, e.g. :5
			
			
	);


	//will not match Na+-

	/*		boolean isQuery = false;
			if (symbol.indexOf(",") > -1) isQuery = true;
			if (symbol.indexOf(";") > -1) isQuery = true;
			if (symbol.indexOf("#") > -1) isQuery = true;
			if (symbol.indexOf("!") > -1) isQuery = true;
	 */

	/**
	 * Parse strings like 13C to find isotope and charge. Check validity of the element symbol and isotope.
	 * If the element symbol, the isotope and the charge are valid, then
	 * set my charge and isotope accordingly, returns the element symbol extracted from the input string.
	 * If parsing fails or if the isotope is not correct, return the input string.
	 * Checking if the charge is valid for the the given element is not performed.
	 * 
	 * @param symbol
	 * @return element or symbol
	 */


	/**
	 * TODO: handling of query symbol
	 * TODO: handling of H count
	 * See setAtom()
	 * 
	 * 
	 */
	String parseAtomicSymbolPatternIsotopMappAndCharge(String symbol, MoleculeHandlingParameters moleculeHandlingParameters) {
		
		Matcher m = atomicSymbolPatternIsotopAndCharge.matcher(symbol);
		if(m.find()) {
			String iso = m.group(1);
			String element =  m.group(2);
			String hCount = m.group(3);
			String hCountNumber = m.group(4);
			String chargeNumber = m.group(6); //2
			String chargeSign = m.group(5); //+ 
			String multiCharge = m.group(7); //++
			String query = m.group(8);
			//String hasQuerySymbol = m.group(7);
			String atomMap = m.group(10); //:3 for an atom map
			int charge = 0;
			
			boolean isValid = true;
			
			if( AtomicElements.getNaturalMass(element) != -1) {
				if(iso != null && iso.length() > 0) { //the string length has to be tested because of the different behavior of IE
					int isomass = Integer.parseInt(iso);
					
					if(AtomicElements.isKnown(element, isomass)) {
						this.iso = isomass;
					} else {
						isValid = false;
					}
				}
				
			} else {
				isValid = false;
			}
			
			// charge
			if(isValid) {
				
				boolean hasChargeSign = chargeSign != null && chargeSign.length() > 0;
				boolean hasChargeNumber = chargeNumber != null && chargeNumber.length() > 0;
				boolean hasMultiCharge = multiCharge != null && multiCharge.length() > 0;

				if(hasChargeSign || hasChargeNumber) {
					//design of the regexp: either (chargeNumber and chargeSign) or multiCharge
					charge = 1;
					if (hasChargeNumber) {
						charge = Integer.parseInt(chargeNumber);
					}
					charge *= chargeSign.equals("-")? -1 : 1;
					
				} else if(hasMultiCharge){
					charge = multiCharge.length();
					charge *= multiCharge.equals("-")? -1 : 1;
				}
				
			}
			if(isValid) {
				symbol = element + (query!=null?query:"");
				this.q = charge;
			}
			
			if(atomMap != null && atomMap.length() > 0) {
				try {
					int map = Integer.parseInt(atomMap);
					if (map > 0 ) {
						if (moleculeHandlingParameters.mark) {
							this.setMark(map);
						} 
						if (moleculeHandlingParameters.number) {
							this.setMap(map);
						}

						
					}
				} catch(Exception e) {
					//TODO: show error
					
				}
				
			}
			if(hCount != null) {
				this.nh = 1;
				if(hCountNumber != null && hCountNumber.length() > 0)
					this.nh = Integer.parseInt(hCountNumber);
			}

		}
		
		return symbol;
	}

	public void setDisplay(int alignment, boolean showHs, boolean showMap, FontMetrics fm, double h) {

		al = new AtomDisplayLabel(x, y, getLabel(), an, nv, sbo, nh, q, iso, showMap && hasBeenMapped() ? getMap() : -1, alignment, fm, h, showHs);
		
	}

	public String getLabel() {
		return (an == JME.AN_X ? label : JME.zlabel[an]);
	}


	@Override
	public String toString() {
		return "[Atom " + getLabel() + " " + x + " " + y + "]";
	}
}

