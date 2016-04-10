import java.util.Arrays;

public class CubicSplineWikiExample {
    final float[] x;
    final float[] y;
    public float[] hx;
    public float[] hy;
    public float[] aa;
    public float[] bb;
    public float[] b;
    public float[] k;
    float[][] m;
    private final int n;
    private float[][] cofactorMatrix;

    public CubicSplineWikiExample(float[] x, float[] y) {
	this.x = x;
	this.y = y;
	n = x.length;
	precalculations();
	initiateMatrix();
	// System.out.println("n = " + nDimension);
	cofactorMatrix = coFactorMatrix(m, n);
	k = solveSystemOfEquation(transposeAndInverse(cofactorMatrix), b, n);
	debugPrintMatrix(m);
	calculateUnknownsBasedOnK();
	printUnknowns();
	
    }

    public void initiateMatrix() {
	m = new float[n][n];
	for (int i = 0; i < n; i++)
	    Arrays.fill(m[i], 0);
	int scewMatrix = 0;
	float prev;
	m[0][0] = 2 / (hx[0]);
	m[0][1] = prev = 1 / (hx[0]);
	for (int i = 1; i < n - 1; i++) {
	    m[i][scewMatrix] = prev;
	    m[i][scewMatrix + 1] = 2 * (1 / hx[i - 1] + 1 / hx[i]);
	    m[i][scewMatrix + 2] = prev = 1 / hx[i];
	    scewMatrix++;
	}
	m[n - 1][n - 1] = 2 / hx[n - 2];
	m[n - 1][n - 2] = 1 / hx[n - 2];
    }

    public void printUnknowns() {
	for (int i = 0; i < n; i++) {
	    System.out.println("k[" + i + "] = " + k[i]);
	}
	for(int i = 0; i < n; i++){
	    System.out.println("b["+i+"] = " + b[i]);
	}
	for(int i = 0; i < n-1; i++){
	    System.out.println("aa and bb of : "+i+ " is respectively : "+aa[i] + ", " + bb[i]);
	}
    }
    public void calculateUnknownsBasedOnK(){
	aa = new float[n-1];
	bb = new float[n-1];
	for(int i = 0; i < n-1; i++){
	    aa[i] = k[i]*hx[i] - hy[i];
	    bb[i] = -k[i+1]*hx[i] + hy[i];
	}
    }
    public float interpolate(int i, float x){
	//System.out.println(x + " " +(x - this.x[i-1]));
	float t = (x - this.x[i-1]) / (this.x[i]- this.x[i-1]);
	//System.out.println(t);
	return ( (1-t)*y[i-1]) + (t*y[i]) + (t *(1-t) * (aa[i-1]*(1-t) + (bb[i-1]*t)));
    }

    public void precalculations() {
	// hx is difference matrix between x+1 and x.
	hx = new float[n - 1];
	// hy is difference matrix between y+1 and y
	hy = new float[n - 1];
	b = new float[n];
	for (int i = 0; i < n - 1; i++) {
	    hx[i] = x[i + 1] - x[i];
	    hy[i] = y[i + 1] - y[i];
	}
	float prev = (float) (hy[0] / Math.pow(hx[0], 2));
	float next;
	b[0] = 3 * prev;
	for (int i = 1; i < n - 1; i++) {
	    next = (float) (hy[i] / Math.pow(hx[i], 2));
	    b[i] = 3 * (prev + next);
	    prev = next;
	}
	b[n - 1] = 3 * prev;
    }

    public float[][] coFactorMatrix(float[][] matrix, int n) {
	boolean positive = true;
	float[][] coFactor = new float[n][n];
	if (n == 2) {
	    coFactor[0][0] = matrix[1][1];
	    coFactor[1][1] = matrix[0][0];
	    coFactor[0][1] = -matrix[0][1];
	    coFactor[1][0] = -matrix[1][0];
	} else {
	    for (int i = 0; i < n; i++) {
		if (i % 2 == 0)
		    positive = true;
		else
		    positive = false;
		for (int j = 0; j < n; j++) {
		    if (positive) {
			coFactor[i][j] = determinant(degenerateMatrix(matrix,
				i, j));
			positive = false;
		    } else {
			float non_minus_zero = -determinant(degenerateMatrix(
				matrix, i, j));
			if (non_minus_zero == -0)
			    non_minus_zero = 0;
			coFactor[i][j] = non_minus_zero;
			positive = true;
		    }
		}
	    }
	}
	return coFactor;
    }

    // after this step, take the transposed matrix and multiply with 1/2 to get
    // inverse matrix
    public float[][] transposeAndInverse(float[][] matrix) {
	float det = 1 / (determinant(m));
	float[][] transposedMatrix = new float[matrix.length][matrix.length];
	for (int i = 0; i < matrix.length; i++) {
	    for (int j = 0; j < matrix.length; j++) {
		transposedMatrix[i][j] = matrix[j][i] * det;
	    }
	}
	return transposedMatrix;
    }

    // works
    public float[][] degenerateMatrix(float[][] matrix, int row, int column) {
	int degRow = 0, degCol = 0, n = matrix.length - 1;
	float[][] degedMatrix = new float[n][n];
	for (int i = 0; i < matrix.length; i++) {
	    for (int j = 0; j < matrix.length; j++) {
		if (i != row && j != column) {
		    degedMatrix[degRow][degCol] = matrix[i][j];
		    degCol++;
		    if (degCol == n) {
			degCol = 0;
			degRow++;
		    }
		}
	    }
	}
	return degedMatrix;
    }

    private float[] solveSystemOfEquation(float[][] inverseMatrix,
	    float[] unknowns, int n) {
	float[] result = new float[n];
	int resultCounter = 0;
	for (int i = 0; i < inverseMatrix.length; i++) {
	    int unknownCounter = 0;
	    for (int j = 0; j < inverseMatrix[i].length; j++) {
		result[resultCounter] += (unknowns[unknownCounter++] * inverseMatrix[i][j]);
	    }
	    resultCounter++;
	}

	return result;
    }

    /*
     * method takes in a cofactor matrix and the determinant of the original
     * matrix and multiplies returns the inverted -1 of "matrix"
     */

    private void debugPrintMatrix(float[][] matrix) {
	for (int i = 0; i < matrix.length; i++) {
	    for (int j = 0; j < matrix[i].length; j++) {
		System.out.print(" " + matrix[i][j]);
	    }
	    System.out.println();
	}
    }

    public float determinant(float[][] matrix) { // method sig. takes a matrix
						 // (two dimensional array),
						 // returns determinant.
	float sum = 0.0f;
	int s;
	if (matrix.length == 1) { // bottom case of recursion. size 1 matrix
				  // determinant is itself.
	    return (matrix[0][0]);
	}
	for (int i = 0; i < matrix.length; i++) { // finds determinant using
						  // row-by-row expansion
	    float[][] smaller = new float[matrix.length - 1][matrix.length - 1]; // creates
										 // smaller
										 // matrix-
										 // values
										 // not
										 // in
										 // same
										 // row,
										 // column
	    for (int a = 1; a < matrix.length; a++) {
		for (int b = 0; b < matrix.length; b++) {
		    if (b < i) {
			smaller[a - 1][b] = matrix[a][b];
		    } else if (b > i) {
			smaller[a - 1][b - 1] = matrix[a][b];
		    }
		}
	    }
	    if (i % 2 == 0) { // sign changes based on i
		s = 1;
	    } else {
		s = -1;
	    }
	    sum += s * matrix[0][i] * (determinant(smaller)); // recursive step:
							      // determinant of
							      // larger
							      // determined by
							      // smaller.
	}
	return (sum); // returns determinant value. once stack is finished,
		      // returns final determinant.
    }

}
