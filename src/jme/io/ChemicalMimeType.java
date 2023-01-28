package jme.io;

public class ChemicalMimeType {
	static public String name = "chemical";
	static public String url = "http://www.ch.ic.ac.uk/chemime/";
	static public String molfile = "x-mdl-molfile";

	public static String molfile(String separator) {
		return name + separator + molfile;
	}

}