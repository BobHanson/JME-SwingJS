package jme;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;

/**
 * Trying to find all varaibles and functions that JSMEMol instance needs from JME
 * INCOMPLETED!
 * @author bruno
 *
 */
public abstract class JMEConf {

	public static final Object ACTION_CHAIN = null;
	public static final Object ACTION_BOND_TRIPLE = null;
	public static final Object ACTION_BOND_DOUBLE = null;
	public static final Object LA_FAILED = null;
	public static final Object ACTION_STEREO = null;
	public static final Object ACTION_GROUP_TBU = null;
	public static final Object ACTION_GROUP_CCL3 = null;
	public static final Object ACTION_GROUP_CF3 = null;
	public static final Object ACTION_GROUP_SULFO = null;
	public static final Object ACTION_GROUP_PO3H2 = null;
	public static final Object ACTION_GROUP_SO2NH2 = null;
	public static final int AN_CL = 0;
	public static final int AN_F = 0;
	public static final int AN_O = 0;
	public static final int AN_S = 0;
	public static final int AN_N = 0;
	public static final int AN_P = 0;
	public static final Object ACTION_GROUP_NHSO2ME = null;
	public static final Object ACTION_GROUP_NITRO = null;
	boolean isTouchSupported = false;
	boolean nocenter = false;
	int numberofMoleculeParts =0;
	boolean scalingIsPerformedByGraphicsEngine = false;
	double depictScale = 0;
	double atomMolecularDrawingAreaFontSize = 0;
	boolean depictBorder = false;
	Rectangle dimension = null;
	Color[] psColor = null;
	Font atomDrawingAreaFont = null;
	FontMetrics atomDrawingAreaFontMet = null;
	boolean doTags = false;
	int AN_C = 0;
	boolean showHydrogens = false;
	Color canvasBg = null;
	Color[] color = null;
	boolean bwMode = false;
	Object action = null;
	boolean webme = false;
	Object ACTION_DELETE = null;
	Object ACTION_DELGROUP = null;
	int[] apointx = null;
	int[] apointy = null;
	int[] bpointx = null;
	int[] bpointy = null;
	public double menuCellSize;
	public boolean revertStereo;
	public Object lastAction;
	abstract Rectangle getMolecularAreaSizeWithScalingForDepiction();
	abstract void alignMolecules(int i, int numberofmoleculeparts2, int j);
	abstract void initatomDrawingAreaFont(double atommoleculardrawingareafontsize2);
	abstract int stringHeight(FontMetrics atomdrawingareafontmet2);
	public void recordAtomEvent(String string, int natoms) {
		// TODO Auto-generated method stub
		
	}
	public void info(String string) {
		// TODO Auto-generated method stub
		
	}
	public void recordBondEvent(String string) {
		// TODO Auto-generated method stub
		
	}
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
