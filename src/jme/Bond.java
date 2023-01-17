package jme;

public class Bond extends AtomBondCommon{
	public int va;
	public int vb;
	public int bondType = JMEmol.SINGLE; //TODO constants should be defined in this class
	
	
	public int stereo;
	String btag;
	public double bondCenterX;
	public double bondCenterY;
	
	int partIndex;
	
	public Bond deepCopy() {
		Bond copy = new Bond();
		this.initOtherFromMe(copy);
		
		return copy;
	}
	
	public void initOtherFromMe(Bond otherBond) {
		super.initOtherFromMe(otherBond);
		
		otherBond.va = this.va;
		otherBond.vb = this.vb;
		otherBond.bondType = this.bondType;
		otherBond.stereo = this.stereo;
		otherBond.btag = this.btag; //should we deep copy the string?
		otherBond.bondCenterX = this.bondCenterX;
		otherBond.bondCenterY = this.bondCenterY;
		otherBond.partIndex = this.partIndex;
	}
	
	public static String convertBondType(int bondType) {
		String result = null ;
		switch(bondType) {
			case JMEmol.SINGLE :
				result = "single";
				break;
			case JMEmol.DOUBLE:
				result = "double";
				break;
			case JMEmol.TRIPLE:
				result = "triple";
				break;
			case JMEmol.AROMATIC:
				result = "aromatic";
				break;
			case JMEmol.COORDINATION:
				result = "coordination";
				break;
			case JMEmol.QUERY:
				result = "query";
				break;
		}
		
		return result;
	}
	public static String convertBondStereo(int stereo) {
		String result = null ;
		switch(stereo) {
		case JMEmol.DOWN :
		case JMEmol.XDOWN :
			result = "down";
			break;
		case JMEmol.UP:
		case JMEmol.XUP:
			result = "up";
			break;
		case JMEmol.EITHER:
		case JMEmol.XEITHER:
			result = "either";
			break;

		case JMEmol.EZ:
			result = "either";
			break;

		}

		return result;
	}
	
	public boolean isSingle() {
		return bondType==JMEmol.SINGLE;
	}
	public boolean isDouble() {
		return bondType==JMEmol.DOUBLE;
	}
	public boolean isTriple() {
		return bondType==JMEmol.TRIPLE;
	}
	
	
	void initBondCenter(Atom atoms[]) {
		Atom atom1 = atoms[va]; Atom atom2 = atoms[vb];
		
		bondCenterX = (atom1.x+ atom2.x)/2.;
		bondCenterY = (atom1.y+ atom2.y)/2.;

	}

	void toggleNormalCrossedDoubleBond() {
		assert(isDouble());
		if(isDouble()) {
			stereo = JMEmol.EZ- stereo;
		}
		assert(stereo== 0 || stereo == JMEmol.EZ);
	}

	public boolean isCoordination() {
		return this.bondType == JMEmol.COORDINATION;
	}
	public Bond setCoordination(Boolean yesOrNo) {
		this.bondType = yesOrNo?JMEmol.COORDINATION:JMEmol.SINGLE;
		return this;
	}
	public boolean isQuery() {
		return bondType==JMEmol.QUERY;
	}
	
	public Bond toggleCoordination() {
		return setCoordination(!isCoordination());
	}
	

	@Override
	public boolean resetObjectMark() {
		boolean hasChanged = this.backgroundColors[0] != NOT_MAPPED_OR_MARKED;
		this.resetBackgroundColors();;
		return hasChanged;
	}





/*	
	public int[] getBondProperties() {
		int[] bd = JMEUtil.createArray(4); 
		bd[0] = va; bd[1] = vb; bd[2] = bondType; bd[3] = stereo;
		return bd;
	}
	//TODO: change variable name - not clear
	public void setBondProperties(int bp0, int bp1, int bp2, int bp3) {
		// for actual bond, called after createBond() - so data for nbonds
		va = bp0; vb = bp1; bondType = bp2; stereo = bp3;
	}
*/
}
