package jme;

import java.awt.FontMetrics;
import java.util.Calendar;
import java.util.StringTokenizer;

public abstract class JMEUtil {

	
	public static boolean isSwingJS = /** @j2sNative true || */false;

	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_CENTER = 1;
	public static final int ALIGN_RIGHT = 2;

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

	/**
	 * Used by JSME for code splitting
	 * 
	 * 
	 * 
	 */
	/**
	 * A callback meant to be used by
	 * {@link gwt_compat.google.gwt.core.client.GWT#runAsync(RunAsyncCallback) }.
	 */
	public interface RunAsyncCallback {
		/**
		 * Called when, for some reason, the necessary code cannot be loaded. For
		 * example, the web browser might no longer have network access.
		 * 
		 * @j2sAlias onFailure
		 */
		public void onFailure(Throwable reason);

		/**
		 * Called once the necessary code for it has been loaded.
		 * @j2sAlias onSuccess
		 */
		public void onSuccess();
	}

	public static abstract class JSME_RunAsyncCallback implements RunAsyncCallback {

		/**
		 * @j2sAlias onFailure
		 */
		@Override
		public void onFailure(Throwable reason) {
			// Window.alert("Loading JS code failed");

		}
	}

	public interface RunWhenDataReadyCallback {

		/**
		 * Called when, for some reason, the necessary code cannot be loaded. For
		 * example, the web browser might no longer have network access.
		 * 
		 * @j2sAlias onFailure
		 */
		void onFailure(Throwable reason);

		/**
		 * Called once the necessary code for it has been loaded.
		 * 
		 * @j2sAlias onSuccess
		 */
		void onSuccess(String data);

		/**
		 * 
		 * @j2sAlias onWarning
		 * 
		 * @param message
		 */
		void onWarning(String message);

	}

	public static void runAsync(RunAsyncCallback runAsyncCallback) {
	
		runAsyncCallback.onSuccess();
	
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

	public static double squareEuclideanDist(double x1, double y1, double x2, double y2) {
		double dx = x2-x1;
		double dy = y2-y1;
		return dx*dx+dy*dy;
	}

	public static double dotProduct(double x1, double y1, double x2, double y2) {
		return x1*x2 + y1*y2;
	}

	/**
	 * Compute the height of a triangle knowing the length of each side.
	 * Use Heron's formula.
	 * @param a
	 * @param b -base of the triangle
	 * @param c
	 * @return
	 */
	public static double triangleHeight(double a, double b, double c) {
		double s = (a+b+c)/2; //half the perimeter of the triangle
		double area = Math.sqrt( s * (s-a) * (s-b) * (s-c));
		double h = 0;
	
		if(b != 0) {
			h = area / b * 2;
		}
	
		return h;
	
	
	}

	// ----------------------------------------------------------------------------
	public static int compareAngles(double sina, double cosa, double sinb, double cosb) {
		// returns 1 if a < b (clockwise) -1 a > b, 0 ak a = b
		int qa = 0, qb = 0; // kvadrant
		if (sina >= 0. && cosa >= 0.)
			qa = 1;
		else if (sina >= 0. && cosa < 0.)
			qa = 2;
		else if (sina < 0. && cosa < 0.)
			qa = 3;
		else if (sina < 0. && cosa >= 0.)
			qa = 4;
		if (sinb >= 0. && cosb >= 0.)
			qb = 1;
		else if (sinb >= 0. && cosb < 0.)
			qb = 2;
		else if (sinb < 0. && cosb < 0.)
			qb = 3;
		else if (sinb < 0. && cosb >= 0.)
			qb = 4;
		if (qa < qb)
			return 1;
		else if (qa > qb)
			return -1;
		// su v rovnakom kvadrante
		switch (qa) {
		case 1:
		case 4:
			if (sina < sinb)
				return 1;
			else
				return -1;
		case 2:
		case 3:
			if (sina > sinb)
				return 1;
			else
				return -1;
		}
		System.err.println("stereowarning #31");
		return 0;
	}


	// ----------------------------------------------------------------------------
	/**
	 * See CTFile -- this line is NOT optional. It is critical in showing whether we
	 * have a 2D or 3D MOL file.
	 * 
	 * @param version
	 * @param is2d
	 * @return SDF header line 2 with no \n
	 */
	public static String getSDFDateLine(String version) {
		String mol = (version + "         ").substring(0, 10);
		int cMM, cDD, cYYYY, cHH, cmm;
		/**
		 * for convenience only, no need to invoke Calendar for this simple task.
		 * 
		 * @j2sNative
		 * 
		 * 			var c = new Date(); cMM = c.getMonth(); cDD = c.getDate(); cYYYY =
		 *            c.getFullYear(); cHH = c.getHours(); cmm = c.getMinutes();
		 */
		{
			Calendar c = Calendar.getInstance();
			cMM = c.get(Calendar.MONTH);
			cDD = c.get(Calendar.DAY_OF_MONTH);
			cYYYY = c.get(Calendar.YEAR);
			cHH = c.get(Calendar.HOUR_OF_DAY);
			cmm = c.get(Calendar.MINUTE);
		}
		mol += rightJustify("00", "" + (1 + cMM));
		mol += rightJustify("00", "" + cDD);
		mol += ("" + cYYYY).substring(2, 4);
		mol += rightJustify("00", "" + cHH);
		mol += rightJustify("00", "" + cmm);
		mol += "2D 1   1.00000     0.00000     0";
		// This line has the format:
		// IIPPPPPPPPMMDDYYHHmmddSSssssssssssEEEEEEEEEEEERRRRRR
		// A2<--A8--><---A10-->A2I2<--F10.5-><---F12.5--><-I6->
		return mol;
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

	/**
	 * Provide the ideal height of a string consisting of usual upper case
	 * characters. Purpose: centering of String in the center of a box. Does not
	 * work for $ , y ; and others
	 */
	public static double stringHeight(FontMetrics fm) {
		return fm.getAscent() - fm.getDescent();
	}

	public static void log(String string) {
		// TODO Auto-generated method stub
	
	}

}
