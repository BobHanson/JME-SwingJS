/**
 * 
 */
package jme.io;

import com.actelion.research.chem.AbstractDepictor;
import com.actelion.research.chem.SVGDepictor;
import com.actelion.research.chem.StereoMolecule;

import jme.util.JMEUtil;

/**
 * SVG contains a MDL molfile in its own XML tag
 *
 */
public class JMESVGWriter extends SVGDepictor {

	String mdlMOL;
	String tag;
//	static String newLineReplacement = "\\\\n";

	public JMESVGWriter(StereoMolecule mol, String mdlMol) {
		super(mol, "");
		if (mdlMol != null && mdlMol.length() > 0)
			this.mdlMOL = mdlMol;
		int mode = AbstractDepictor.cDModeSuppressCIPParity | AbstractDepictor.cDModeSuppressESR
				| AbstractDepictor.cDModeSuppressChiralText;
		this.setDisplayMode(mode);
	}

	public String additionalMetaInfo(String indent) {
		String result = "";
		if (mdlMOL != null) {
			String molData 
			//= this.mdlMOL.replaceAll("(\\r|\\n|\\r\\n)", "\\\n");
			= JMEUtil.wrapCDATA(mdlMOL);
			result = indent + "<" + ChemicalMimeType.chemicalMimeTag() + ">" + molData + "</" + ChemicalMimeType.chemicalMimeTag() + ">\n";
		}
		return result;
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
				svg = svg.substring(0, pt) + ChemicalMimeType.additionalNameSpaces(" ") + svg.substring(pt);
			}
		}
		return svg;
	}

}
