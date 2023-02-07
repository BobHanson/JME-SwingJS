package jme.util;

import java.util.Arrays;

public class DPoint {
	
	private DPoint() {
		// static methods only
	}

	/**
	 * Add b to a, returning a
	 * 
	 * @param a
	 * @param b
	 * @return a, with b added
	 */
	public static double[] add(double[] a, double[] b) {
		a[0] += b[0];
		a[1] += b[1];
		a[2] += b[2];
		return a;
	}

	/**
	 * subtract b from a and store into a given return array
	 * 
	 * @param a
	 * @param b
	 * @param ret receiver of a + b
	 */
	public static void sub2(double[] a, double[] b, double[] ret) {
		ret[0] = a[0] - b[0];
		ret[1] = a[1] - b[1];
		ret[2] = a[2] - b[2];
	}

	/**
	 * Calculate the squared distance of a to b
	 * 
	 * @param a
	 * @param b
	 * @return squared distance of a to b
	 */
	public static double distanceSquared(double[] a, double[] b) {
		return (a[0] - b[0]) * (a[0] - b[0]) + (a[1] - b[1]) * (a[1] - b[1]) + (a[2] - b[2]) * (a[2] - b[2]);
	}

	public static void scale(double[] a, double f) {
		a[0] *= f;
		a[1] *= f;
		a[2] *= f;
	}

	/**
	 * Transform p in place by matrix m.
	 * 
	 * @param m standard 4x4 matrix
	 * @param p point to transform
	 * @return transformed point p
	 */
	public static double[] transform2(double[][] m, double[] p) {
		if (m != null) {
			double d0 = m[0][0] * p[0] + m[0][1] * p[1] + m[0][2] * p[2];
			double d1 = m[1][0] * p[0] + m[1][1] * p[1] + m[1][2] * p[2];
			p[2] = m[2][0] * p[0] + m[2][1] * p[1] + m[2][2] * p[2];
			p[1] = d1;
			p[0] = d0;
		}
		return p;
	}

	/**
	 * Set a quaternion [w, x, y, z] from x, y, z, and w. The quaternion will be
	 * normalized such that q dot q = 1.
	 * 
	 * If all x, y, z, and w are 0, returns [1,0,0,0]
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @return normalized quaternion
	 */
	public static double[] setQ(double x, double y, double z, double w) {
		double[] q = new double[4];
		double factor = x * x + y * y + z * z + w * w;
		if (factor == 0) {
			q[0] = 1;
			return q;
		}
		q[0] = w / factor;
		q[1] = x / factor;
		q[2] = y / factor;
		q[3] = z / factor;
		return q;
	}

	/**
	 * Convert quaternion to matrix format.
	 * 
	 * @param q a quaternion as [w,x,y,z]
	 * @return matrix equivalent of q
	 */
	public static double[][] qToM4(double[] q) {
		double[][] mat = new double[3][3];
		// q0 = w, q1 = x, q2 = y, q3 = z
		mat[0][0] = q[0] * q[0] + q[1] * q[1] - q[2] * q[2] - q[3] * q[3];
		mat[0][1] = 2 * q[1] * q[2] - 2 * q[0] * q[3];
		mat[0][2] = 2 * q[1] * q[3] + 2 * q[0] * q[2];
		mat[1][0] = 2 * q[1] * q[2] + 2 * q[0] * q[3];
		mat[1][1] = q[0] * q[0] - q[1] * q[1] + q[2] * q[2] - q[3] * q[3];
		mat[1][2] = 2 * q[2] * q[3] - 2 * q[0] * q[1];
		mat[2][0] = 2 * q[1] * q[3] - 2 * q[0] * q[2];
		mat[2][1] = 2 * q[2] * q[3] + 2 * q[0] * q[1];
		mat[2][2] = q[0] * q[0] - q[1] * q[1] - q[2] * q[2] + q[3] * q[3];
		return mat;
	}

	public static String toString(double[][] m) {
		String s = "";
		for (int i = 0; i < m.length; i++)
			s += Arrays.toString(m[i]) + "\n";
		return s;
	}
}