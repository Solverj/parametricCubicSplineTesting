import java.util.Arrays;

public class NaturalCubicSplineExample {
    final float[] x;
    final float[] y;

    public float[] a;
    public float[] b;
    public float[] v;
    public float[] u;
    public float[] z;
    public float[] h;
    float[][] m;
    private final int nDimension;
    private float[][] cofactorMatrix;

    public NaturalCubicSplineExample(float[] x, float[] y) {
	this.x = x;
	this.y = y;
	nDimension = x.length;
	precalculations();
	initiateMatrix();
	//System.out.println("n = " + nDimension);
        cofactorMatrix = coFactorMatrix(m, nDimension - 2);
        z = solveSystemOfEquation(transposeAndInverse(cofactorMatrix), u, nDimension);
        printUnknowns();
    }
    public float interpolate(float xx, int ii){
	int i = ii;
	float x = xx;
	float x1 = this.x[i + 1];
	float x0 = this.x[i];
	if(x == this.x[i]) return y[i];
	if(x > this.x[i+1]){
	    x0 = this.x[i+1];
	    x1 = this.x[i];
	}
	System.out.println("i : " + i + "  x = "+ x + " z[i] = " + z[i] + " h[i] = " + h[i] + " z[i+1] = " + z[i+1]);
	float slutt = (float)Math.pow((x1 - x),3);
	float start = (float)Math.pow((x - x0), 3);
	float forsteLedd =  z[i+1]/(6*h[i]);
	forsteLedd *= start;
	float forsteLedd2 = z[i] / (6*h[i]);
	forsteLedd2 *= slutt;
	System.out.println("førsteLedd = " + forsteLedd);
	forsteLedd += forsteLedd2;
	float andreLedd = y[i+1]/h[i] - (z[i+1]/6 * h[i]);
	start = x - this.x[i];
	andreLedd *= start;
	float tredjeLedd = y[i]/h[i] - (h[i]/6*z[i]);
	tredjeLedd *= x1 - x;
	System.out.println("førsteLedd : " + forsteLedd + " andre ledd = " + andreLedd + " tredje ledd = " + tredjeLedd);
	float result = forsteLedd + andreLedd + tredjeLedd;
	return result;
	
    }
    private void printUnknowns(){
	System.out.println("z length is: " + z.length + " is : " + nDimension);
	for(int i = 0; i < z.length; i++){
	    //System.out.println("unknown z[" + i + "] is : " + z[i]);
	    System.out.println("z["+i + "] is : " + z[i] );
	}
	System.out.println("u's length is " + u.length );
	for(int i = 0; i < u.length; i++){
	    System.out.println("u["+i + "] is  : " + u[i]);
	}
    }

    private void precalculations() {
	h = new float[nDimension];
	b = new float[nDimension];
	v = new float[nDimension];
	u = new float[nDimension];
	z = new float[nDimension];
	// initiate knot points
	for (int i = 0; i < nDimension - 1; i++) {
	    h[i] = x[i + 1] - x[i];
	}
	// initiate values for u's. (b's)

	for (int i = 0; i < nDimension - 1; i++) {
	    b[i] = (1 / h[i]) * (y[i + 1] - y[i]);

	}
	// initiate mid-tridiagonal elements (v's)
	for (int i = 1; i < nDimension - 1; i++) {
	    v[i] = 2 * (h[i - 1] + h[i]);
	}
	// initiate u's
	for (int i = 1; i < nDimension - 1; i++) {
	    u[i] = 6 * (b[i] - b[i - 1]);
	}
	z[0] = z[nDimension - 1] = 0; // first knot and last knot is zero.

    }

    private void initiateMatrix() {
					
	cofactorMatrix = new float[nDimension - 2][nDimension - 2];
	m = new float[nDimension - 2][nDimension - 2];
	for (int i = 0; i < nDimension - 2; i++) {
	    Arrays.fill(m[i], 0);
	}
	int vCounter = 0;
	int hF = 0;
	int hN = 1;
	float hPrev = h[1];
	for (int i = 0; i < nDimension - 2; i++) {
	    if (i > 0) {
		m[i][hF++] = hPrev;
	    }
	    if (i < nDimension - 3) {
		m[i][hN++] = hPrev = h[i];
	    }
	    m[i][vCounter++] = v[i + 1];
	}
    }

    public float[][] coFactorMatrix(float[][] matrix, int n) {
	System.out.println("n dimension inside cofactorMatrix method is : " + n);
	boolean positive = true;
	float[][] coFactor = new float[n][n];
	if (n == 2) {
	    coFactor[0][0] = matrix[1][1];
	    coFactor[1][1] = matrix[0][0];
	    coFactor[0][1] = -matrix[0][1];
	    coFactor[1][0] = -matrix[1][0];
	} else {
	    for (int i = 0; i < n; i++) {
		if(i%2 == 0) positive = true;
		else positive = false;
		for (int j = 0; j < n; j++) {
		    if(positive){
			coFactor[i][j] = determinant(degenerateMatrix(matrix,
				i, j));
			positive = false;
		    }else{
			float non_minus_zero = -determinant(degenerateMatrix(matrix,
				i, j));
			if(non_minus_zero == - 0) non_minus_zero = 0;
			coFactor[i][j] = non_minus_zero;
			positive = true;
		    }
		}
	    }
	}
	return coFactor;
    }
    //after this step, take the transposed matrix and multiply with 1/2 to get inverse matrix
    public float[][] transposeAndInverse(float[][] matrix){
	float det = 1/(determinant(m));
	float[][] transposedMatrix = new float[matrix.length][matrix.length];
	for(int i = 0; i < matrix.length; i++){
	    for(int j = 0; j < matrix.length; j++){
		transposedMatrix[i][j] = matrix[j][i] * det;
	    }
	}
	return transposedMatrix;
    }

    // works
    public float[][] degenerateMatrix(float[][] matrix, int row, int column) {
	int degRow = 0, degCol = 0, n = matrix.length-1;
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
	int resultCounter = 1;
	for (int i = 0; i < inverseMatrix.length; i++) {
	    int unknownCounter = 1;
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
