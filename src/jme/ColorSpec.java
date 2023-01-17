package jme;

/**
 * Used to exchange data with JS objects
 * @author bruno
 *
 */
public  class ColorSpec {
	String color; // hexadecimal
	String label;
	// comment, ...
	
	public ColorSpec(String hexColor, String label) {
		this.color = hexColor;
		this.label = label;
	}
	
}
