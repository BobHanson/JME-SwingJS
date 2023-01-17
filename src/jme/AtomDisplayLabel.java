/**
 * 
 */
package jme;


/**
 * @author bruno
 *
 */
public class AtomDisplayLabel {
	public double smallAtomWidthLabel = 0; // atom element only, e.g. N, Cl
	public double fullAtomWidthLabel;  // atom element + extra e.g NH3+
	public boolean leftToRight;
	public boolean noLabelAtom;
	
	public Box atomLabelBoundingBox;
	public double labelX;
	public double labelY;
	
	public String zz;

	int boundingBoxpadding = 2; // number of pixel of white space surrounding the atom labels
	
	
	public double atomMapY ; // BB: move the map symbol higher
	public double atomMapX;
	public String mapString;


	
	private String z;
	
	double neighborXaverage;
	
	public int subscripts[][];
	public int superscripts[][];

	/**
	 * 
	 */
	public AtomDisplayLabel(String label) {
		this.z = label;
	}
	
	public void computeLabel( AtomBondCommon at)  {
		
	}

}
