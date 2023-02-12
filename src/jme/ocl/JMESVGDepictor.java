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
public class JMESVGDepictor extends SVGDepictor {

	String mdlMOL;
	String tag;
	static String newLineReplacement = "\\\\n";

	public JMESVGDepictor(StereoMolecule mol, String mdlMol) {
		super(mol, "");
		if (mdlMol != null && mdlMol.length() > 0)
			this.mdlMOL = mdlMol;
		int mode = AbstractDepictor.cDModeSuppressCIPParity | AbstractDepictor.cDModeSuppressESR
				| AbstractDepictor.cDModeSuppressChiralText;
		this.setDisplayMode(mode);
	}

	static String tag() {
		return ChemicalMimeType.molfile(":");
	}

	public String additionalMetaInfo(String indent) {
		String result = "";
		if (mdlMOL != null) {
			String molData 
			//= this.mdlMOL.replaceAll("(\\r|\\n|\\r\\n)", newLineReplacement);
			= wrapCDATA(mdlMOL);
			result = indent + "<" + tag() + ">" + molData + "</" + tag() + ">\n";
		}
		return result;
	}

	private static String wrapCDATA(String data) {
		return "<![CDATA[" + data + "]]>";
	}

	private static String unwrapCData(String result) {
		int pt = result.indexOf("<![CDATA[");
		int pt1 = result.indexOf("]]>");
		return (pt >= 0 && pt1 > pt ? result.substring(pt + 9, pt1) : result);			
	}

	public static String additionalNameSpaces(String post) {
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
		if (result.indexOf('\n') < 0)
			result = result.replaceAll(newLineReplacement, "\n");
		return unwrapCData(result);
	}
	
	@Override 
	public String toString() {
		String svg = super.toString();
		if (mdlMOL != null) {
			int pt = svg.lastIndexOf("</");
			if (pt > 0) {
				svg = svg.substring(0, pt) + additionalMetaInfo("") + svg.substring(pt);
			}
			pt = svg.indexOf("xmlns");
			if (pt > 0) {
				svg = svg.substring(0, pt) + additionalNameSpaces(" ") + svg.substring(pt);
			}
		}
		return svg;
	}

}
