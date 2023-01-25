package jme;

/**
 * Common variables and functionalities for atoms and bonds
 * @author bruno
 *
 */
public interface AtomBondCommon {

	public final static int NOT_MAPPED_OR_MARKED = -99199;

	public AtomBondCommon deepCopy();
	
	/**
	 * Add a new background color unless it is not already present
	 * @param c
	 */
	public void addBackgroundColor(int c);
	
	public void resetBackgroundColors();

	public int[] getBackgroundColors();
	
	
	/**
	 * @return true if changed
	 */
	public boolean resetMark();
	
	/**
	 * @return true if changed
	 */
	public boolean resetObjectMark();


	public int getMark();
	
	public void setMark(int map);
	
	public boolean isMarked();
	
	public static int[] growArray(int[] array, int newSize) {
		int newArray[] = new int[newSize];
		System.arraycopy(array, 0, newArray, 0, array.length);
		return newArray;
	}

	/* shallow copy of the array */
	public static int[] copyArray(int[] array) {
		int copy[] = new int[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}



}
