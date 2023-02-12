package jme.io;

import jme.util.JMEUtil;

public class ChemicalMimeType {
	static public String typeName = "chemical";
	static public String url = "http://www.ch.ic.ac.uk/chemime/";
	static public String molfile = "x-mdl-molfile";

	private ChemicalMimeType() {}
	
	public static String molfile(String separator) {
		return typeName + separator + molfile;
	}

	public static String chemicalMimeTag() {
		return molfile(":");
	}

	/*
	 * Return null if not found
	 */
	public static String extractEmbeddedChemicalString(String svg) {
		String result = null;
		String tag = chemicalMimeTag();
		int tagStart = svg.indexOf(tag);
	
		if (tagStart > 0) {
			int molStart = svg.indexOf(">", tagStart) + 1;
			int molEnd = svg.indexOf("</" + tag, molStart);
			if (molEnd - molStart > 20) { // TODO: minimal length of a MOL
				result = svg.substring(molStart, molEnd);
			}
		}
		if (result == null)
			return null;
		if (result.indexOf('\n') < 0)
			result = result.replaceAll("\\\n", "\n");
		return JMEUtil.unwrapCData(result);
	}

	public static String additionalNameSpaces(String post) {
		return "xmlns:" + typeName + "=\"" + url + "\"" + post;
	}

}