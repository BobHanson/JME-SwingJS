/**
 * 
 */
package jme.ocl;

import com.actelion.research.chem.AbstractDepictor;
import com.actelion.research.chem.SVGDepictor;
import com.actelion.research.chem.StereoMolecule;

import jme.io.ChemicalMimeType;

/**
 * SVG contains a MDL molfile in its own XML tag
 *
 */
public class SVGDepictorWithEmbeddedChemicalStructure extends SVGDepictor {

	String mdlMOL;
	String tag;
	static String newLineReplacement = "\\\\n";

	public SVGDepictorWithEmbeddedChemicalStructure(StereoMolecule mol, String mdlMol) {
		super(mol, "");
		this.mdlMOL = mdlMol;
		int mode = AbstractDepictor.cDModeSuppressCIPParity | AbstractDepictor.cDModeSuppressESR
				| AbstractDepictor.cDModeSuppressChiralText;

		this.setDisplayMode(mode);

	}

	static String tag() {
		return ChemicalMimeType.molfile(":");
	}

//	@Override
	public String additionalMetaInfo(String indent) {
		String result = "";
		if (mdlMOL != null && mdlMOL.length() > 0) {
			String noNewLinemdlMOL = this.mdlMOL.replaceAll("(\\r|\\n|\\r\\n)", newLineReplacement);
			result = indent + "<" + tag() + ">" + noNewLinemdlMOL + "</" + tag() + ">\n";
		}
		return result;
	}

	// @Override
	public String additionalNameSpaces(String post) {
		return "xmlns:" + ChemicalMimeType.name + "=\"" + ChemicalMimeType.url + "\"" + post;
	}

	/*
	 * Return null if not found
	 */
	public static String extractEmbeddedChemicalString(String svg) {
		String result = null;
		String tag = tag();
		int tagStart = svg.indexOf(tag);

		if (tagStart > 0) {
			int molStart = svg.indexOf(">", tagStart) + 1;
			int molEnd = svg.indexOf("</" + tag, molStart);
			if (molEnd - molStart > 20) { // TODO: minimal length of a MOL
				result = svg.substring(molStart, molEnd);
			}
		}

		result = result.replaceAll(newLineReplacement, "\n");

		return result;
	}

}
