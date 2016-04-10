import java.lang.Math;
import java.util.Arrays;

public class HermiteCubicSplineExample {

    public final float[] x;
    public final float[] y;
    public float[] k;
    public float[] a;
    public float[] b;
    float[] m;
    private final int n;

    private HermiteCubicSplineExample(float[] x, float[] y) {
	this.x = x;
	this.y = y;
	n = x.length;
	initiate();
	// initiateConstants();
	// System.out.println("k0 = " + k0 + " , k1 = " + k1 + " , k2 = " + k2);
	// System.out.print("a1 = " + a1 + "\n" + "b1 = " + b1 + "\n" + "a2 = "
	// + a2 + "\n" + "b2 = " + b2 + "\n");
    }

    public void initiate() {
	m = new float[n];
	float[] d = new float[n - 1];
	// zero it all out
	//finite difference
	for (int i = 0; i < n - 1; i++) {
	    float h = x[i + 1] - x[i];
	    d[i] = (y[i + 1] - y[i]) / h;
	}
	m[0] = d[0];
	for (int i = 1; i < n - 1; i++) {
	    m[i] = (d[i - 1] + d[i]) * 0.5f;
	}
	m[n - 1] = d[n - 2];

	for (int i = 0; i < n - 1; i++) {
	    float a = m[i] / d[i];
	    float b = m[i + 1] / d[i];
	    float h = (float) Math.hypot(a, b);
	    if (h > 9f) {
		float t = 3f / h;
		m[i] = t * a * d[i];
		m[i + 1] = t * b * d[i];
	    }
	}

    }

    //
    // private void initiateConstants() {
    // a1 = k0 * (xP1 - xP0) - (yP1 - yP0);
    // b1 = -k1 * (xP1 - xP0) + (yP1 - yP0);
    // a2 = k1 * (xP2 - xP1) - (yP2 - yP1);
    // b2 = -k2 * (xP2 - xP1) + (yP2 - yP1);
    // }

    public static HermiteCubicSplineExample generateCubic(float x[], float y[]) {
	if (x.length == y.length)
	    return new HermiteCubicSplineExample(x, y);
	else
	    System.out.println("y and x lists aren't equally large");
	return null;
    }

    public float interpolate(float xx, int i) {
	float h = x[i+1] - x[i];
	float t = (xx - x[i]) / h;
	return (y[i] * (1 + 2 * t) + h * m[i] * t) * (1 - t) * (1 - t)
                + (y[i + 1] * (3 - 2 * t) + h * m[i + 1] * (t - 1)) * t * t;
	// float a11 = 2 / (xP1 - xP0);
	// float a12 = 1 / (xP1 - xP0);
	// float a13 = 0;
	// float a21 = a12;
	// float a22 = 2 * (1 / (xP1 - xP0) + 1 / (xP2 - xP1));
	// float a23 = 1 / (xP2 - xP1);
	// float a31 = 0;
	// float a32 = a23;
	// float a33 = 2 / (xP2 - xP1);
	//
	// float x2 = (float) Math.pow(xP2 - xP1, 2);
	// float x1 = (float) Math.pow(xP1 - xP0, 2);
	// float y2 = yP2 - yP1;
	// float y1 = yP1 - yP0;
	// float b1 = 3 * (y1 / x1);
	// float b3 = 3 * (y2 / x2);
	// float b2 = 3 * ((b1 / 3) + (b3 / 3));
	// /*
	// * the original matrix
	// */
	// float[][] p = { { a11, a12, 0 }, // =bb1
	// { a21, a22, a23 }, // =bb2
	// { 0, a32, a33 } // =bb3
	// };
	//
	// float c11, c12, c13, c21, c22, c23, c31, c32, c33;
	//
	// /*
	// * cofactors of p
	// */
	// c11 = (a22 * a33) - (a32 * a23);
	// c12 = -((a21 * a33) - (a31 * a23));
	// c13 = (a21 * a32) - (a31 * a22);
	// c21 = -((a12 * a33) - (a32 * a13));
	// c22 = (a11 * a33) - (a31 * a13);
	// c23 = -((a11 * a32) - (a31 * a12));
	// c31 = (a12 * a23) - (a22 * a13);
	// c32 = -((a11 * a23) - (a21 * a13));
	// c33 = ((a11 * a22) - (a21 * a12));
	//
	// /*
	// * cofactorMatrix of p, also called adjunct of P
	// */
	// float[][] cofactorOfP = { { c11, c21, c31 }, { c12, c22, c32 },
	// { c13, c23, c33 } };
	//
	// float[][] inverseOfP = solveInverseMatrix(p, cofactorOfP);
	// float[] unknowns = { b1, b2, b3 };
	// float[] knowns = solveSystemOfEquation(inverseOfP, unknowns,
	// unknowns.length);
	// this.k0 = knowns[0];
	// this.k1 = knowns[1];
	// this.k2 = knowns[2];
    }

    

    // float splineFunction1(float x, float y_one, float y_two, float x_one,
    // float x_two) {
    //
    // float a = k0 * (x_two - x_one) - (y_two - y_one);
    // float b = -((k1) * (x_two - x_one) - (y_two - y_one));
    //
    // // q_i = (1-t) y_{i-1} + ty_i + t(1-t)(a_i (1-t) + b_i t)
    // float t = ((x - x_one) / (x_two - x_one));
    // return (1 - t) * y_one + (t * y_two) + (t * (1 - t))
    // * (a * (1 - t) + b * t);
    // }
    //
    // float splineFunction2(CubicSplineExample cs, float x, float y_one,
    // float y_two, float x_one, float x_two) {
    //
    // float a = k1 * (x_two - x_one) - (y_two - y_one);
    // float b = -((k2) * (x_two - x_one) - (y_two - y_one));
    //
    // // q_i = (1-t) y_{i-1} + ty_i + t(1-t)(a_i (1-t) + b_i t)
    // float t = ((x - x_one) / (x_two - x_one));
    // return (1 - t) * y_one + (t * y_two) + (t * (1 - t))
    // * (a * (1 - t) + b * t);
    // }

}
