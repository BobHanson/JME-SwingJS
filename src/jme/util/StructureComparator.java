/* $RCSfile$
 * $Author: egonw $
 * $Date: 2005-11-10 09:52:44f -0600 (Thu, 10 Nov 2005) $
 * $Revision: 4255 $
 *
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright (C) 2003-2005  Miguel, Jmol Development, www.jmol.org
 *
 * Contact: jmol-developers@lists.sf.net
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package jme.util;

import java.util.Arrays;

/**
 * 
 * Methods to compare two structures or substructures using the closed-form
 * solution of Horn, 1987. These methods allow the alignment of 2D or 3D structures based
 * on a 1:1 mapping of coordinates either directly or based on a SMARTS or SMILES mapping.
 *
 * Code is derived from SwingJS javajs.util.MeasureD, with double arrays replacing javajs.util.*
 * x,y,z-based points, preserving just the few methods needed for these
 * operations.
 *
 * See Eigen4.java for details of this elegant quaternion analysis.
 * 
 * There are only three public methods here:
 * 
 * 
 * double[][] getCenterAndPoints(double[][] vPts)
 * 
 * adds a center point to the array of points to align
 * 
 * 
 * double[][] calculateQuaternionRotation(double[][][] centerAndPoints, double[]
 * retStddev, double[][] receiver)
 * 
 * carries out the alignment, returning the 4x4 rotation-translation matrix used.
 * 
 * 
 * double getRmsd(double[][][] centerAndPoints, double[][] mat4, double[][] receiver)
 * 
 * used by calculateQuaternionRotation but also can be used in any situation to transform 
 * points using any 4x4 rotation-translation matrix
 * 
 * 
 * 
 **/

public class StructureComparator {

	private StructureComparator() {
		// static methods only
	}
	
	/**
	 * From an array of [x, y, z] points, create an array for which the first
	 * element is the center of these points.
	 * 
	 * @param vPts [pt1, pt2, pt3...]
	 * @return [ center, pt1, pt2, pt3...]
	 */
	public static double[][] getCenterAndPoints(double[][] vPts) {
		int n = vPts.length;
		double[][] pts = new double[n + 1][3];
		pts[0] = new double[3];
		if (n > 0) {
			for (int i = 0; i < n; i++) {
				DPoint.add(pts[0], pts[i + 1] = vPts[i]);
			}
			DPoint.scale(pts[0], 1d / n);
		}
		return pts;
	}


	/**
	 * Calculate the best transformation of a set of N points to match another set
	 * of N points using the closed-form solution of Horn, 1987.
	 * 
	 * see Berthold K. P. Horn, "Closed-form solution of absolute orientation using
	 * unit quaternions" J. Opt. Soc. Amer. A, 1987, Vol. 4, pp. 629-642
	 * http://www.opticsinfobase.org/viewmedia.cfm?uri=josaa-4-4-629&seq=0
	 * https://doi.org/10.1364/JOSAA.4.000629
	 * 
	 * @param centerAndPoints [ [center, pta1, pta2, pta3,...], [center, pta1, pta2,
	 *                        pta3,...] ]
	 * @param receiver        optional receiver of transformed [center, pta1',
	 *                        pta2', pta3'...]; if present must be double[length N +
	 *                        1][]
	 * @param retStddev       return [start,end] values of start and end rmsd; start
	 *                        is after aligning centers; end is after carrying out
	 *                        the rotation
	 * 
	 * @return 4x4 matrix from Horn closed-form solution
	 */
	public static double[][] calculateQuaternionRotation(double[][][] centerAndPoints, double[] retStddev,
			double[][] receiver) {
		retStddev[1] = Double.NaN;
		double[][] ptsA = centerAndPoints[0];
		double[][] ptsB = centerAndPoints[1];
		int nPts = ptsA.length - 1;
		if (nPts < 2 || ptsA.length != ptsB.length)
			return null;
		double Sxx = 0, Sxy = 0, Sxz = 0, Syx = 0, Syy = 0, Syz = 0, Szx = 0, Szy = 0, Szz = 0;
		double[] ptA = new double[3];
		double[] ptB = new double[3];
		double[] ptA0 = ptsA[0];
		double[] ptB0 = ptsB[0];
		for (int i = nPts + 1; --i >= 1;) {
			DPoint.sub2(ptsA[i], ptA0, ptA);
			DPoint.sub2(ptsB[i], ptB0, ptB);
			Sxx += ptA[0] * ptB[0];
			Sxy += ptA[0] * ptB[1];
			Sxz += ptA[0] * ptB[2];
			Syx += ptA[1] * ptB[0];
			Syy += ptA[1] * ptB[1];
			Syz += ptA[1] * ptB[2];
			Szx += ptA[2] * ptB[0];
			Szy += ptA[2] * ptB[1];
			Szz += ptA[2] * ptB[2];
		}
		retStddev[0] = getRmsd(centerAndPoints, null, centerAndPoints.length == 2 ? null : centerAndPoints[2]);
		double[][] N = new double[4][4];
		N[0][0] = Sxx + Syy + Szz;
		N[0][1] = N[1][0] = Syz - Szy;
		N[0][2] = N[2][0] = Szx - Sxz;
		N[0][3] = N[3][0] = Sxy - Syx;

		N[1][1] = Sxx - Syy - Szz;
		N[1][2] = N[2][1] = Sxy + Syx;
		N[1][3] = N[3][1] = Szx + Sxz;

		N[2][2] = -Sxx + Syy - Szz;
		N[2][3] = N[3][2] = Syz + Szy;

		N[3][3] = -Sxx - Syy + Szz;

		double[] v = new Eigen4().setM(N).getLargestEigenvector();
		double[] q = DPoint.setQ(v[1], v[2], v[3], v[0]);
		double[][] mat4 = DPoint.qToM4(q);
		retStddev[1] = getRmsd(centerAndPoints, mat4, receiver);
		return mat4;
	}

	
	/**
	 * Determine the root mean square deviation of two sets of points after
	 * transformation of the first set by a 4x4 rotation-translation matrix.
	 * 
	 * Optionally return the transformed coordinates with their center.
	 * 
	 * @param centerAndPoints [ [center, pta1, pta2, pta3,...], [center, pta1, pta2,
	 *                        pta3,...] ]
	 * @param mat4            a standard 4x4 rotation-translation matrix; if null,
	 *                        then no rotation is carried out
	 * @param receiver        optional receiver of transformed [center, pta1',
	 *                        pta2', pta3'...]
	 * @return
	 */
	public static double getRmsd(double[][][] centerAndPoints, double[][] mat4, double[][] receiver) {
		double sum2 = 0;
		double[][] ptsA = centerAndPoints[0];
		double[][] ptsB = centerAndPoints[1];
		double[] cA = ptsA[0];
		double[] cB = ptsB[0];
		if (receiver != null) {
			receiver[0] = cB;
		}
		int n = ptsA.length - 1;
		double[] ptAnew = new double[3];
		for (int i = n + 1; --i >= 1;) {
			DPoint.sub2(ptsA[i], cA, ptAnew);
			DPoint.add(DPoint.transform2(mat4, ptAnew), cB);
			sum2 += DPoint.distanceSquared(ptAnew, ptsB[i]);
			if (receiver != null) {
				receiver[i] = ptAnew;
				ptAnew = new double[3];
			}
		}
		return Math.sqrt(sum2 / n);
	}

	
	public static void main(String[] args) {
		test();
	}
	
	public static void test() {
		double[][] a = new double[][] { { 0, 0, 0 }, { 1, 0, 0 }, { 0, 2, 0 } };
		double[][] b = new double[][] { { 0, 0, 0 }, { 0, 1, 0 }, { -2, 0, 0 } };

		double[][] ca = getCenterAndPoints(a);
		double[][] cb = getCenterAndPoints(b);
		double[][] receiver = new double[ca.length][];
		double[] rmsd = new double[2];
		double[][] m = calculateQuaternionRotation(new double[][][] { ca, cb }, rmsd, receiver);
		System.out.println(Arrays.toString(rmsd));
		System.out.println(DPoint.toString(m));
	}

}