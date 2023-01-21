package jme;

import com.actelion.research.chem.AbstractDepictor;
import com.actelion.research.chem.IDCodeParser;
//OpenChemLib import
import com.actelion.research.chem.MolfileCreator;
import com.actelion.research.chem.MolfileParser;
import com.actelion.research.chem.SVGDepictor;
import com.actelion.research.chem.SmilesParser;
import com.actelion.research.chem.StereoMolecule;
import com.actelion.research.gui.generic.GenericRectangle;

public class OclParser implements Parser {

	public OclParser() {
	 // for reflection	
	}
	
	@Override
	public String getOclCode(String molFile) {
		// TODO : error handling
		String result = null;
		StereoMolecule mol = new StereoMolecule();
		if (new MolfileParser().parse(mol, molFile)) {

			result = mol.getIDCode();
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getOclSVG(String molFile) {
		double width = 400;
		double height = 300;
		// TODO : error handling
		StereoMolecule mol = new StereoMolecule();
		if (new MolfileParser().parse(mol, molFile)) {
			// recipe found in openchemlib-js
			SVGDepictor svgd = new SVGDepictorWithEmbeddedChemicalStructure(mol, molFile);
			svgd.validateView(null, new GenericRectangle(0, 0, width, height),
					AbstractDepictor.cModeInflateToHighResAVBL);
			svgd.paint(null);
			return svgd.toString();
		}
		return null;
	}

	@Override
	public String OclCodeToMOL(String oclCode) {
		StereoMolecule mol = new IDCodeParser().getCompactMolecule(oclCode.trim());
		MolfileCreator mfc = new MolfileCreator(mol);
		return mfc.getMolfile();

	}

	@Override
	public String SMILEStoMOL(String smiles) throws Exception {

		// OCLSmilesParser generates an exception if the SMILES is empty
		if (smiles == null || smiles.length() == 0 || smiles.trim().length() == 0)
			return new JMEmol().createMolFile(""); // empty mol

		StereoMolecule mol = new StereoMolecule();
		new SmilesParser().parse(mol, smiles.trim());

		MolfileCreator mfc = new MolfileCreator(mol);
		return mfc.getMolfile();
	}

	@Override
	public String v3000toV2000MOL(String v3000Mol) {
		StereoMolecule mol = new StereoMolecule();
		boolean success = new MolfileParser().parse(mol, v3000Mol);
		// error messages are sent to a TRACE function that cannot be redefined
		if (success) {
			MolfileCreator mfc = new MolfileCreator(mol);
			return mfc.getMolfile();
		}
		return null;
	}

}