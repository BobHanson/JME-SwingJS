package jme.util;

import java.util.StringTokenizer;

import javax.swing.SwingUtilities;

import jme.js.AsyncCallback;

public interface JMEUtil {

	/**
	 * Check if the applet is showing in highDPI or not. In a web browser, this can
	 * change with the zoom factor, thus this function should be called before each
	 * drawing
	 * 
	 * @return
	 */

	public static boolean isHighDPI() {
		return false;
	}

	public static String nextData(StringTokenizer st, String separator) {
		// dost tricky, musi uvazit aj bez \n aj sa \n |\n za sebou ...
		// musi osetrit aj lines with zero length (2 x po sebe \n alebo |)
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			if (s.equals(separator)) return " ";
			else {
				if (!st.nextToken().equals(separator)) { // ukoncujuci separator
					System.err.println("mol file line separator problem!");
				}
				// musi vyhodit z konca pripadne | (napr v appletviewer)
				while(true) {
					char c =  s.charAt(s.length()-1);
					if (c=='|' || c =='\n' || c=='\r') {
						s = s.substring(0,s.length()-1);
						if (s.length()==0) return " "; // v textboox \r\n ??
					}  
					else break;
				}
				return s;
			}
		}
		return null;
	}

	// ----------------------------------------------------------------------------
	public static String findLineSeparator(String molFile) {
		//if (c=='\t' || c=='\n' || c=='\r' || c=='\f' || c=='|') 
		//  molFile = " " + molFile;
		//StringTokenizer st = new StringTokenizer(molFile,"\t\n\r\f|",true);
		// osetrene aj separator 2x tesne za sebou  
		StringTokenizer st = new StringTokenizer(molFile,"\n",true);
		if (st.countTokens() > 4)
			return "\n";
		else {  
			st = new StringTokenizer(molFile,"|",true);
			if (st.countTokens() > 4)
				return "|";
			else 	
				System.err.println("Cannot process mol file, use | as line separator !");
		}
		return null;
	}

	// ----------------------------------------------------------------------------
	/**
	 * right-justify using spaces
	 * 
	 * @param number with no more than len digits
	 * @param len max 8
	 * @return
	 */
	public static String iformat(int number, int len) {
		return rightJustify("        ".substring(0, len), "" + number);
	}
 
	public static String rightJustify(String s1, String s2) {
		int n = s1.length() - s2.length();
		return (n == 0 ? s2 : n > 0 ? s1.substring(0, n) + s2 : s1.substring(0, s1.length() - 1) + "?");
	}
	
	/**
	 * Truncate to dec digits after the decimal place and left-pad to length len.
	 * 
	 * @param number
	 * @param len guaranteed length of string to return
	 * @param dec the number of decimal places or 0 for integer rounding down
	 * @return the formatted number or right-justified "?" 
	 */
	public static String fformat(double number, int len, int dec) {
		// este pridat zmensovanie dec, ked dlzka nestaci
		if (dec == 0)
			return iformat((int) number, len);
		if (Math.abs(number) < 0.0009)
			number = 0.; // 2012 fix 1.0E-4
		double m = Math.pow(10, dec);
		number = (int) Math.round(number * m) / m;
		String s = new Double(number).toString(); // this sometimes return 1.0E-4
		int dotpos = s.indexOf('.');
		if (dotpos < 0) {
			s += ".";
			dotpos = s.indexOf('.');
		}
		int slen = s.length();
		for (int i = 1; i <= dec - slen + dotpos + 1; i++)
			s += "0";
		return (len == 0 ? s : rightJustify("          ".substring(0, len), s));
	}

	public static void log(String s) {
		//System.out.println(s);	
	}

	public static boolean isMacintosh() {
		// TODO Auto-generated method stub
		return false;
	}

	public static String wrapCDATA(String data) {
		return "<![CDATA[" + data + "]]>";
	}

	public static String unwrapCData(String result) {
		int pt = result.indexOf("<![CDATA[");
		int pt1 = result.indexOf("]]>");
		return (pt >= 0 && pt1 > pt ? result.substring(pt + 9, pt1) : result);			
	}

}
