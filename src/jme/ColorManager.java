package jme;


//START JAVA_IMPORT
import java.awt.Color;


/**
 * Manage set of colors
 * @author bruno
 *
 */
public class ColorManager {
	public final static String colorHashPrefix = "COLOR_HASH";
	
	

	public class ColorInfo {
		Color color;
		String name;
		String hash;
		int index;
		
		ColorInfo(Color color, String name, int index) {
			this.color = color;
			this.name = name;
			this.hash = colorHashPrefix + "\t" +  index;
			this.index = index;
		}
		
		public String getName() {
			return name;
		}
	}

	// colors used for atom background colors , highlighting some atoms
	// static Color[] psColor;
	protected ColorInfo[] psColor;
	//protected String[] labels; // each color can have a label
	
	public ColorManager() {
		// TODO Auto-generated constructor stub
	}
	

	public void initDefaultBackGroundColorPaletteIfNeeded() {
		if(psColor == null ) {
			psColor = new ColorInfo[8]; // first color cannot be changed
			
			//psColor[0] = new ColorInfo(Color.gray,  "0", 0); //this color is never used. SHould it be null
			psColor[0] = new ColorInfo(Color.gray,  null, 0); //this color is never used. SHould it be null April 2021
			
			psColor[1] = new ColorInfo(Color.cyan, "1", 1);
			psColor[2] = new ColorInfo(new Color(255, 204, 102),"2", 2);
			//psColor[3] = new ColorInfo(new Color(255, 255, 153), "3", 3); // (tto) light yellow
			psColor[3] = new ColorInfo(new Color(255, 255, 0), "3", 3); //full yellow
			psColor[4] = new ColorInfo(new Color(255, 153, 153), "4", 4);// pastel red
			psColor[5] = new ColorInfo(new Color(51, 204, 255), "5", 5);
			psColor[6] = new ColorInfo(new Color(255, 153, 255), "6", 6);
			psColor[7] = new ColorInfo(new Color(102, 255, 102), "7", 7); // light green Feb 2022
		}

	}
	
	public int numberOfBackgroundColors() {
		this.initDefaultBackGroundColorPaletteIfNeeded();
		
		return this.psColor.length - 1;
	}

	
	/**
	 * Return an array of RGB (0-255) values
	 * @return int[n][3]
	 */
	
	// this is not used anymore?
	public int[][] getBackGroundColorPaletteAsInteger() {
		initDefaultBackGroundColorPaletteIfNeeded();
		
		int result[][] = new int[psColor.length-1][3];
		
		// the first value is ignored
		for(int i = 1 ; i < psColor.length; i++) {
			Color c = getColor(i);
			result[i-1][0] = c.getRed(); // Why i-1? The first color is grey
			result[i-1][1] = c.getGreen();
			result[i-1][2] = c.getBlue();
		}
		
		return result;
	}
	
	public String[] getBackGroundColorPalette() {
		initDefaultBackGroundColorPaletteIfNeeded();
		String result[] = new String[psColor.length - 1];
		for(int i = 1 ; i < psColor.length; i++) {
			result[i - 1] = makeHexColor(psColor[i].color);
		}
	
		
		return result;
	}
	
	public ColorInfo getColorInfo(int index) {
		this.initDefaultBackGroundColorPaletteIfNeeded();
		if ( index >= 0 && index < this.psColor.length) {
			return this.psColor[index];
		}
		
		return null;
	}
	public ColorInfo getColorInfoOfColorHash(String hash) {
		this.initDefaultBackGroundColorPaletteIfNeeded(); 
		for( ColorInfo each : psColor) {
			if (each.hash == hash) {
				return each;
			}
		}
		
		return null;
	}
	
	public Color getColor(int index) {
		this.initDefaultBackGroundColorPaletteIfNeeded(); //DUP
		if ( index >= 0 && index < this.psColor.length) {
			return this.psColor[index].color;
		}
		
		return null;
	}
	public String getColorAssociatedLabel(int index) {
		this.initDefaultBackGroundColorPaletteIfNeeded(); //DUP
		if ( index >= 0 && index < this.psColor.length) {
			return this.psColor[index].name;
		}
		
		return null;
	}	
	
	public String getColorAssociatedHash(int index) {
		this.initDefaultBackGroundColorPaletteIfNeeded(); //DUP
		if ( index >= 0 && index < this.psColor.length) {
			return this.psColor[index].hash;
		}
		
		return null;
	}
	
	public void  setBackGroundColorPalette(String[] palette) {
		
		int n = palette.length;
		
		ColorSpec[] colorSpecPalette = new ColorSpec[n];
		
		for(int i = 0 ; i < n; i++) {
			String color = palette[i];
			String label = "" + (i+1); // default name
			colorSpecPalette[i] = new ColorSpec(color, label);
		}
		
		this.setMarkerMenuBackGroundColorPalette(colorSpecPalette);
	}
	
	/*
	 * 
	 * [color, name]
	 */
	public void  setMarkerMenuBackGroundColorPalette(ColorSpec[] palette) {
		// DUP code
		this.initDefaultBackGroundColorPaletteIfNeeded();
		ColorInfo firstColor = psColor[0];
		psColor = new ColorInfo[palette.length + 1];
		psColor[0] = firstColor;
		
		for(int i = 0 ; i < palette.length; i++) {
			psColor[i + 1] = new ColorInfo(parseHexColor(palette[i].color), palette[i].label, i + 1);
		}		


	}

	/**
	 * Compute an average color from a list of color
	 * @param colorCodes - indices of my color palette
	 * @return
	 */
	public Color averageColor(int [] colorCodes) {
		initDefaultBackGroundColorPaletteIfNeeded();
		int singleColorIndex = -1;
		if(colorCodes != null && colorCodes.length >= 1) {
			// mix colors
			int red=0, green=0, blue=0;
			int count = 0;
			for( int colorIndex : colorCodes) {
				if(colorIndex>=0 && colorIndex < psColor.length) {
					Color color = psColor[colorIndex].color;
					red += color.getRed();
					green += color.getGreen();
					blue += color.getBlue();
					count++;
					singleColorIndex = colorIndex;

				}
			}
			
			if(count > 0) {
				if(count == 1) {
					if (singleColorIndex >= 0) {
						return psColor[singleColorIndex].color; //May 31: was 0
					}
				} else {
					//mix color
					Color color = new Color(red/count, green/count, blue/count);
					
					return color;
					
				}
			}
		}
		
		return null;
		
	}
	
	/**
	 * Convert a hexcolor representation into a Color object
	 * @param hex #FF00DD
	 * @return
	 */
	public static Color parseHexColor(String hex) {
		Color c = Color.white;
		try {
			if (!hex.startsWith("#"))
				throw new Exception("bad hex encoding");
			int r = Integer.parseInt(hex.substring(1, 3), 16);
			int g = Integer.parseInt(hex.substring(3, 5), 16);
			int b = Integer.parseInt(hex.substring(5, 7), 16);
			c = new Color(r, g, b);
			return c;
		} catch (Exception e) {
			System.err.println("Problems in parsing background color " + hex);
			return c;
		}
	}
	
	public static String makeHexColor(Color c) {
		String hex = "#" + byteToHexString(c.getRed()) + byteToHexString(c.getGreen())
							+ byteToHexString(c.getBlue());
		
		return hex;
		
	}
	
	// need "00" for 0 and not "0"
	protected static String byteToHexString(int c) {
		String hex =  Integer.toHexString(c);
		if (hex.length() == 1) {
			hex = "0" + hex;
		}
		
		return hex;
	}



	public static double brightness(Color c) {
		double result = c.getRed() * 299.0 + c.getGreen() * 587.0 + c.getBlue() * 114;
		result /= 1000.0;
		
		return result;
	}

	public static Color contrast(Color c) {
		Color result = brightness(c) > 123 ? Color.BLACK : Color.WHITE;
		
		return result;
	}



}
