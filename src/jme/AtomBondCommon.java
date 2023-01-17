package jme;

/**
 * Common variables and functionalities for atoms and bonds
 * @author bruno
 *
 */
public  abstract class AtomBondCommon {

	public static int NOT_MAPPED_OR_MARKED = -99199;

	// there are two ways to color the atoms
	
	// indices of the  colors defined in the current color palette
	public int backgroundColors[];
	
	int mark = NOT_MAPPED_OR_MARKED;
	
	//2) Using the current color palette - single color
	//protected int backgroundColorIndex = NOT_MAPPED_OR_MARKED; // valid color index if >= 0
	
	
	public abstract AtomBondCommon deepCopy(); // implementation must call initOtherFromMe()
	

	public void initOtherFromMe(AtomBondCommon other) {
		other.backgroundColors = JMEUtil.copyArray(this.backgroundColors);
		other.mark = this.mark;		
	}

	public AtomBondCommon() {
		this.resetBackgroundColors();
	}

	/**
	 * Add a new background color unless it is not already present
	 * @param c
	 */
	public void addBackgroundColor(int c) {
		for(int i = 0; i < backgroundColors.length; i++) {
			if(backgroundColors[i] == c) {
				return;
			};
		}
		// this is not valid anymore
	//	if(backgroundColors.length == 1 && backgroundColors[0] == 0) {
	//		backgroundColors[0] = c;
	//	} else {
			backgroundColors = JMEUtil.growArray(backgroundColors, backgroundColors.length + 1);
			backgroundColors[backgroundColors.length-1] = c;
	//	}
	
	}

	public void resetBackgroundColors() {
		this.backgroundColors = JMEUtil.createArray(1);
		this.backgroundColors[0] = NOT_MAPPED_OR_MARKED;
		
	}

	
	/**
	 * @return true if changed
	 */
	public boolean resetMark() {
		//this.resetBackgroundColors();
		//this.backgroundColorIndex = NOT_MAPPED_OR_MARKED;
		//return this.resetObjectMark();
		boolean result = this.mark != NOT_MAPPED_OR_MARKED;
		this.mark = NOT_MAPPED_OR_MARKED;
		
		return result;
	}
	
	/**
	 * @return true if changed
	 */
	protected abstract boolean resetObjectMark();


	public int getMark() {
		
		//int mark = backgroundColors[0];
		int mark = this.mark;
		
		return Math.max(mark, 0);
		
	}

	public void setMark(int map) {
		//backgroundColors[0] = map;
		this.mark = map;
		
	}
	public boolean isMarked() {
		//return this.backgroundColors[0] > 0;
		return this.getMark() > 0;
	}
	
}
