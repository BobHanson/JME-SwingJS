package jme.core;

public class Bond implements AtomBondCommon {

	public static final int SINGLE = 1, 
			DOUBLE = 2, 
			TRIPLE = 3, 
			AROMATIC = 5, 
			QUERY = 9, 
			COORDINATION = 0; // BH -- really? 0?
	
	public static final int STEREO_NONE = 0,
			STEREO_UP = 1, 
			STEREO_DOWN = 2, 
			STEREO_XUP = 3, 
			STEREO_XDOWN = 4, 
			STEREO_LAST_KNOWN = 4,
			STEREO_EITHER = 5, 
			STEREO_XEITHER = 6, 
			STEREO_EZ = 10;

	public static final int QB_ANY = 11, QB_AROMATIC = 12, QB_RING = 13, QB_NONRING = 14;
	
	public int va;
	public int vb;
	public int bondType = SINGLE;

	public int stereo;
	public String btag;
	public double centerX, centerY;
	/**
	 * for double bonds
	 */
	public double guideX = Double.NaN, guideY;

	public int partIndex;

	public int backgroundColors[];

	int mark = NOT_MAPPED_OR_MARKED;


	public Bond() {
		resetBackgroundColors();
	}

	@Override
	public Bond deepCopy() {
		return copyTo(new Bond());
	}

	public Bond copyTo(Bond b) {
		b.backgroundColors = AtomBondCommon.copyArray(this.backgroundColors);
		b.mark = this.mark;
		b.va = this.va;
		b.vb = this.vb;
		b.bondType = this.bondType;
		b.stereo = this.stereo;
		b.btag = this.btag; // should we deep copy the string?
		b.centerX = this.centerX;
		b.centerY = this.centerY;
		b.guideX = this.guideX;
		b.guideY = this.guideY;
		b.partIndex = this.partIndex;
		return b;
	}

	public static String convertBondType(int bondType) {
		String result = null;
		switch (bondType) {
		case SINGLE:
			result = "single";
			break;
		case DOUBLE:
			result = "double";
			break;
		case TRIPLE:
			result = "triple";
			break;
		case AROMATIC:
			result = "aromatic";
			break;
		case COORDINATION:
			result = "coordination";
			break;
		case QUERY:
			result = "query";
			break;
		}
		return result;
	}

	public static String convertBondStereo(int stereo) {
		String result = null;
		switch (stereo) {
		case STEREO_DOWN:
		case STEREO_XDOWN:
			result = "down";
			break;
		case STEREO_UP:
		case STEREO_XUP:
			result = "up";
			break;
		case STEREO_EITHER:
		case STEREO_XEITHER:
			result = "either";
			break;

		case STEREO_EZ:
			result = "either";
			break;

		}
		return result;
	}

	public boolean is(Bond b) {
		return (va == b.va && vb == b.vb || va == b.vb && vb == b.va);
	}
	
	public boolean isAB(int atom1, int atom2) {
		return (va == atom1 && vb == atom2 || va == atom2 && vb == atom1);
	}

	public boolean isSingle() {
		return bondType == SINGLE;
	}

	public boolean isDouble() {
		return bondType == DOUBLE;
	}

	public boolean isTriple() {
		return bondType == TRIPLE;
	}

	public void toggleNormalCrossedDoubleBond() {
		if (isDouble()) {
			stereo = STEREO_EZ - stereo;
		}
//		assert (stereo == 0 || stereo == STEREO_EZ);
	}

	public boolean isCoordination() {
		return this.bondType == COORDINATION;
	}

	public Bond setCoordination(Boolean yesOrNo) {
		this.bondType = yesOrNo ? COORDINATION : SINGLE;
		return this;
	}

	public boolean isQuery() {
		return bondType == QUERY;
	}

	public Bond toggleCoordination() {
		return setCoordination(!isCoordination());
	}

	@Override
	public boolean resetObjectMark() {
		boolean hasChanged = this.backgroundColors[0] != NOT_MAPPED_OR_MARKED;
		this.resetBackgroundColors();
		;
		return hasChanged;
	}

	/**
	 * Add a new background color unless it is not already present
	 * 
	 * @param c
	 */
	@Override
	public void addBackgroundColor(int c) {
		for (int i = 0; i < backgroundColors.length; i++) {
			if (backgroundColors[i] == c) {
				return;
			}
		}
		backgroundColors = AtomBondCommon.growArray(backgroundColors, backgroundColors.length + 1);
		backgroundColors[backgroundColors.length - 1] = c;
	}

	@Override
	public void resetBackgroundColors() {
		this.backgroundColors = new int[] { NOT_MAPPED_OR_MARKED };
	}

	@Override
	public int[] getBackgroundColors() {
		return backgroundColors;
	}

	/**
	 * @return true if changed
	 */
	@Override
	public boolean resetMark() {
		if (mark == NOT_MAPPED_OR_MARKED)
			return false;
		mark = NOT_MAPPED_OR_MARKED;
		return true;
	}

	@Override
	public int getMark() {
		return Math.max(mark, 0);
	}

	@Override
	public void setMark(int mark) {
		this.mark = mark;

	}

	@Override
	public boolean isMarked() {
		return mark > 0;
	}

}
