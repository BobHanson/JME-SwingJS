package jme;

import jme.core.JMECore;
import jme.gui.GUI.RingInfo;

public interface Parser {

	void getRingInfo(RingInfo info, JMECore core);
	String getOclCode(String molFile);

	String getOclSVG(String molFile);

	String OclCodeToMOL(String oclCode);

	String SMILEStoMOL(String smiles) throws Exception;

	String v3000toV2000MOL(String v3000Mol);

}
