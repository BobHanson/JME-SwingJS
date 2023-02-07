package jme.util;

import java.util.Arrays;

/**
 * 
 * 
 * Eigenvalues and eigenvectors of a symmetric 3x3 matrix.
 * 
 * Adapted specifically for 4D by Bob Hanson from
 * http://math.nist.gov/javanumerics/jama/ (public domain); adding quaternion
 * superimposition capability; removing nonsymmetric reduction to Hessenberg
 * form, which we do not need in Jmol.
 * 
 * Output returned as double[4][4], with double[4]) values.
 * 
 * Eigenvalues and eigenvectors are sorted from smallest to largest eigenvalue.
 * 
 * A = V*D*V' where the eigenvalue matrix D is diagonal and the eigenvector
 * matrix V is orthogonal. I.e. A = V.times(D.times(V.transpose())) and
 * V.times(V.transpose()) equals the identity matrix.
 * 
 * @author hansonr
 **/

public class Eigen4 {

	/**
	 * Arrays for internal storage of eigenvalues.
	 * 
	 * @serial internal storage of eigenvalues.
	 */
	private double[] d, e;

	/**
	 * Array for internal storage of eigenvectors.
	 * 
	 * @serial internal storage of eigenvectors.
	 */
	private double[][] V;

	public Eigen4() {
	}

	public Eigen4 set() {
		V = new double[4][4];
		d = new double[4];
		e = new double[4];
		return this;
	}

	public Eigen4 setM(double[][] m) {
		set();
		calc(m);
		return this;
	}

	/**
	 * return values sorted from smallest to largest value.
	 */
	public double[] getEigenvalues() {
		return d;
	}

	/**
	 * Return the real parts of the eigenvalues e *
	 * 
	 * @return real(diag(D))
	 */

	public double[] getRealEigenvalues() {
		return d;
	}

	/**
	 * Return the imaginary parts of the eigenvalues
	 * 
	 * @return imag(diag(D))
	 */

	public double[] getImagEigenvalues() {
		return e;
	}

	/**
	 * Specifically for 3x3 systems, returns eigenvectors as V3[3] and values as
	 * float[3]; sorted from smallest to largest value.
	 * 
	 * @param eigenvectors returned vectors
	 * @param eigenvalues  returned values
	 * 
	 */
	public double[] getLargestEigenvector() {
		double[] e = new double[4];
		e[0] = V[0][3];
		e[1] = V[1][3];
		e[2] = V[2][3];
		e[3] = V[3][3];
		return e;
	}

	/**
	 * Construct the eigenvalue decomposition
	 * 
	 * @param A Square matrix
	 */

	public void calc(double[][] A) {

		// symmetric assumed
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				V[i][j] = A[i][j];
			}
		}

		// Tridiagonalize.
		tred2();

		// Diagonalize.
		tql2();
	}

	/*
	 * Symmetric Householder reduction to tridiagonal form.
	 */

	private void tred2() {

		// This is derived from the Algol procedures tred2 by
		// Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
		// Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
		// Fortran subroutine in EISPACK.

		for (int j = 0; j < 4; j++) {
			d[j] = V[4 - 1][j];
		}

		// Householder reduction to tridiagonal form.

		for (int i = 4 - 1; i > 0; i--) {

			// Scale to avoid under/overflow.

			double scale = 0.0;
			double h = 0.0;
			for (int k = 0; k < i; k++) {
				scale = scale + Math.abs(d[k]);
			}
			if (scale == 0.0) {
				e[i] = d[i - 1];
				for (int j = 0; j < i; j++) {
					d[j] = V[i - 1][j];
					V[i][j] = 0.0;
					V[j][i] = 0.0;
				}
			} else {

				// Generate Householder vector.

				for (int k = 0; k < i; k++) {
					d[k] /= scale;
					h += d[k] * d[k];
				}
				double f = d[i - 1];
				double g = Math.sqrt(h);
				if (f > 0) {
					g = -g;
				}
				e[i] = scale * g;
				h = h - f * g;
				d[i - 1] = f - g;
				for (int j = 0; j < i; j++) {
					e[j] = 0.0;
				}

				// Apply similarity transformation to remaining columns.

				for (int j = 0; j < i; j++) {
					f = d[j];
					V[j][i] = f;
					g = e[j] + V[j][j] * f;
					for (int k = j + 1; k <= i - 1; k++) {
						g += V[k][j] * d[k];
						e[k] += V[k][j] * f;
					}
					e[j] = g;
				}
				f = 0.0;
				for (int j = 0; j < i; j++) {
					e[j] /= h;
					f += e[j] * d[j];
				}
				double hh = f / (h + h);
				for (int j = 0; j < i; j++) {
					e[j] -= hh * d[j];
				}
				for (int j = 0; j < i; j++) {
					f = d[j];
					g = e[j];
					for (int k = j; k <= i - 1; k++) {
						V[k][j] -= (f * e[k] + g * d[k]);
					}
					d[j] = V[i - 1][j];
					V[i][j] = 0.0;
				}
			}
			d[i] = h;
		}

		// Accumulate transformations.

		for (int i = 0; i < 4 - 1; i++) {
			V[4 - 1][i] = V[i][i];
			V[i][i] = 1.0;
			double h = d[i + 1];
			if (h != 0.0) {
				for (int k = 0; k <= i; k++) {
					d[k] = V[k][i + 1] / h;
				}
				for (int j = 0; j <= i; j++) {
					double g = 0.0;
					for (int k = 0; k <= i; k++) {
						g += V[k][i + 1] * V[k][j];
					}
					for (int k = 0; k <= i; k++) {
						V[k][j] -= g * d[k];
					}
				}
			}
			for (int k = 0; k <= i; k++) {
				V[k][i + 1] = 0.0;
			}
		}
		for (int j = 0; j < 4; j++) {
			d[j] = V[4 - 1][j];
			V[4 - 1][j] = 0.0;
		}
		V[4 - 1][4 - 1] = 1.0;
		e[0] = 0.0;
	}

	// Symmetric tridiagonal QL algorithm.

	private void tql2() {

		// This is derived from the Algol procedures tql2, by
		// Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
		// Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
		// Fortran subroutine in EISPACK.

		for (int i = 1; i < 4; i++) {
			e[i - 1] = e[i];
		}
		e[4 - 1] = 0.0;

		double f = 0.0;
		double tst1 = 0.0;
		double eps = Math.pow(2.0, -52.0);
		for (int l = 0; l < 4; l++) {

			// Find small subdiagonal element

			tst1 = Math.max(tst1, Math.abs(d[l]) + Math.abs(e[l]));
			int m = l;
			while (m < 4) {
				if (Math.abs(e[m]) <= eps * tst1) {
					break;
				}
				m++;
			}

			// If m == l, d[l] is an eigenvalue,
			// otherwise, iterate.

			if (m > l) {
				int iter = 0;
				do {
					iter = iter + 1; // (Could check iteration count here.)

					// Compute implicit shift

					double g = d[l];
					double p = (d[l + 1] - g) / (2.0 * e[l]);
					double r = hypot(p, 1.0);
					if (p < 0) {
						r = -r;
					}
					d[l] = e[l] / (p + r);
					d[l + 1] = e[l] * (p + r);
					double dl1 = d[l + 1];
					double h = g - d[l];
					for (int i = l + 2; i < 4; i++) {
						d[i] -= h;
					}
					f = f + h;

					// Implicit QL transformation.

					p = d[m];
					double c = 1.0;
					double c2 = c;
					double c3 = c;
					double el1 = e[l + 1];
					double s = 0.0;
					double s2 = 0.0;
					for (int i = m - 1; i >= l; i--) {
						c3 = c2;
						c2 = c;
						s2 = s;
						g = c * e[i];
						h = c * p;
						r = hypot(p, e[i]);
						e[i + 1] = s * r;
						s = e[i] / r;
						c = p / r;
						p = c * d[i] - s * g;
						d[i + 1] = h + s * (c * g + s * d[i]);

						// Accumulate transformation.

						for (int k = 0; k < 4; k++) {
							h = V[k][i + 1];
							V[k][i + 1] = s * V[k][i] + c * h;
							V[k][i] = c * V[k][i] - s * h;
						}
					}
					p = -s * s2 * c3 * el1 * e[l] / dl1;
					e[l] = s * p;
					d[l] = c * p;

					// Check for convergence.

				} while (Math.abs(e[l]) > eps * tst1);
			}
			d[l] = d[l] + f;
			e[l] = 0.0;
		}

		// Sort eigenvalues and corresponding vectors.

		for (int i = 0; i < 4 - 1; i++) {
			int k = i;
			double p = d[i];
			for (int j = i + 1; j < 4; j++) {
				if (d[j] < p) {
					k = j;
					p = d[j];
				}
			}
			if (k != i) {
				d[k] = d[i];
				d[i] = p;
				for (int j = 0; j < 4; j++) {
					p = V[j][i];
					V[j][i] = V[j][k];
					V[j][k] = p;
				}
			}
		}
	}

	private static double hypot(double a, double b) {

		// sqrt(a^2 + b^2) without under/overflow.

		double r;
		if (Math.abs(a) > Math.abs(b)) {
			r = b / a;
			r = Math.abs(a) * Math.sqrt(1 + r * r);
		} else if (b != 0) {
			r = a / b;
			r = Math.abs(b) * Math.sqrt(1 + r * r);
		} else {
			r = 0.0;
		}
		return r;
	}

}