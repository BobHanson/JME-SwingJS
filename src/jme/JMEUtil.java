package jme;

import java.awt.FontMetrics;
import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.StringTokenizer;

public abstract class JMEUtil {

	
	public static boolean isSwingJS = /** @j2sNative true || */false;

	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_CENTER = 1;
	public static final int ALIGN_RIGHT = 2;

	public static int[] growArray(int[] array, int newSize) {
		int newArray[] = new int[newSize];
		System.arraycopy(array, 0, newArray, 0, array.length);
		return newArray;
	}

	/* shallow copy of the array */
	public static int[] copyArray(int[] array) {
		int copy[] = new int[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

// Cloning

	/**
	 * Copies the specified array, truncating or padding with nulls (if necessary)
	 * so the copy has the specified length. For all indices that are valid in both
	 * the original array and the copy, the two arrays will contain identical
	 * values. For any indices that are valid in the copy but not the original, the
	 * copy will contain <tt>null</tt>. Such indices will exist if and only if the
	 * specified length is greater than that of the original array. The resulting
	 * array is of exactly the same class as the original array.
	 *
	 * @param           <T> the class of the objects in the array
	 * @param original  the array to be copied
	 * @param newLength the length of the copy to be returned
	 * @return a copy of the original array, truncated or padded with nulls to
	 *         obtain the specified length
	 * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
	 * @throws NullPointerException       if <tt>original</tt> is null
	 * @since 1.6
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] copyOf(T[] original, int newLength) {
		return (T[]) copyOf(original, newLength, original.getClass());
	}

	/**
	 * Copies the specified array, truncating or padding with nulls (if necessary)
	 * so the copy has the specified length. For all indices that are valid in both
	 * the original array and the copy, the two arrays will contain identical
	 * values. For any indices that are valid in the copy but not the original, the
	 * copy will contain <tt>null</tt>. Such indices will exist if and only if the
	 * specified length is greater than that of the original array. The resulting
	 * array is of the class <tt>newType</tt>.
	 *
	 * @param           <U> the class of the objects in the original array
	 * @param           <T> the class of the objects in the returned array
	 * @param original  the array to be copied
	 * @param newLength the length of the copy to be returned
	 * @param newType   the class of the copy to be returned
	 * @return a copy of the original array, truncated or padded with nulls to
	 *         obtain the specified length
	 * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
	 * @throws NullPointerException       if <tt>original</tt> is null
	 * @throws ArrayStoreException        if an element copied from
	 *                                    <tt>original</tt> is not of a runtime type
	 *                                    that can be stored in an array of class
	 *                                    <tt>newType</tt>
	 * @since 1.6
	 */
	public static <T, U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
		@SuppressWarnings("unchecked")
		T[] copy = ((Object) newType == (Object) Object[].class) ? (T[]) new Object[newLength]
				: (T[]) Array.newInstance(newType.getComponentType(), newLength);
		System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
		return copy;
	}

	public static String[] growArray(String[] array, int newSize) {
		String newArray[] = new String[newSize];
		System.arraycopy(array, 0, newArray, 0, array.length);

		return newArray;
	}

	public static double[] growArray(double[] array, int newSize) {
		double newArray[] = new double[newSize];
		System.arraycopy(array, 0, newArray, 0, array.length);

		return newArray;
	}

	public static int[][] growArray(int[][] array, int newSize) {
		int secondarySize = array[0].length;
		int newArray[][] = new int[newSize][secondarySize]; // new int[newSize][secondarySize];
		System.arraycopy(array, 0, newArray, 0, array.length);

		return newArray;
	}

 	public static boolean equals(int[] a1, int[] a2) {
		if (a1.length == a2.length) {
			for (int i = 0; i < a1.length; i++) {
				if (a1[i] != a2[i]) {
					return false;
				}
			}
			return true;
		}

		return false;
	}

	public static int[] intersection(int[] array1, int[] array2) {
		int common[] = new int[0];
		for (int v1 : array1) {
			if (contains(array2, v1)) {
				common = growArray(common, common.length + 1);
				common[common.length - 1] = v1;
			}
		}

		return common;

	}

	public static boolean contains(int[] array, int v) {
		for (int each : array) {
			if (each == v) {
				return true;
			}
		}

		return false;
	}

	public static <T> void swap(T[] array, int i, int j) {
		T temp = array[j];
		array[j] = array[i];
		array[i] = temp;

	}

	/* shallow copy of the array */
	public static int[] copyArray(int[] array, int n) {
		int copy[] = new int[array.length];
		System.arraycopy(array, 0, copy, 0, n);

		return copy;

	}

	/* shallow copy of the array */
	public static String[] copyArray(String[] array) {
		String copy[] = new String[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);

		return copy;

	};

	/* shallow copy of the array */
	public static double[] copyArray(double[] array) {
		double copy[] = new double[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);

		return copy;

	};

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
	 * Do nothing , support for JSME code splitting.
	 */
	public static class GWT {

		public static void log(String string) {
			// TODO Auto-generated method stub

		}

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
	
		return dx*dx+dy*dy; //equal to dot product
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

}
