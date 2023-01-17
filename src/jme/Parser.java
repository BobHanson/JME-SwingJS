package jme;

public interface Parser {

	String getOclCode(String molFile);

	String getOclSVG(String molFile);

	String OclCodeToMOL(String oclCode);

	String SMILEStoMOL(String smiles) throws Exception;

	String v3000toV2000MOL(String v3000Mol);

}
