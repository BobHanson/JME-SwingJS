package jme;

class TouchedMol {
	JMEmol mol;
	int atomIndex;
	int bondIndex;
	double distance;

	public boolean equals(TouchedMol other) {
		return this.mol == other.mol && this.atomIndex == other.atomIndex && this.bondIndex == other.bondIndex;
		
	}
	
	public void reset() {
		mol = null;
		atomIndex = 0;
		bondIndex = 0;
	}
	
	public void initMyselfWith(TouchedMol other) {
		this.mol = other.mol;
		this.atomIndex = other.atomIndex;
		this.bondIndex = other.bondIndex;
		
	}
	public boolean isTouched() {
		return this.mol != null &&(this.atomIndex >0 || this.bondIndex >0);
	}
}