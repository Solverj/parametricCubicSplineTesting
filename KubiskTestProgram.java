import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D.Float;
import java.awt.geom.Point2D;

public class KubiskTestProgram implements Run {

    int size = 600;
    int margin = 50;
    int xMax = 15;
    int yMax = 15;
    double scale = size / xMax;

    @Override
    public void draw() {
	// TODO Auto-generated method stub

    }

    float xFDraw(float x, int sub) {
	return (float) (x * scale) + margin - sub;
    }

    float yFDraw(float y, int sub) {
	return (float) ((yMax - y) * scale) + margin - sub;
    }

    @Override
    public void draw(Graphics2D g2) {
	// TODO Auto-generated method stub
	float[] wX = { -1, 0, 3, 0 };
	float[] wY = { 0.5f, 0, 3, 5 };
	float[] x = { 1, 2, 3, 6, 9, 12 };
	float[] y = { 1, 3, 4, 6, 9, 14 };
	// float[] x2 = { 0.9f, 1.3f, 1.9f, 2.1f, 3.0f};
	// float[] y2 = { 1.3f, 1.5f, 1.85f, 2.1f, 3.0f};
	float[] x2 = { 0, 1, 2, 4, 10, 14, 16, 17, 18, 5 };
	float[] y2 = { 0, 1, 2, 3, 3, 3, 3, 2, 2, 10 };
	// float[] x2 = {3.0f, 2.1f, 1.9f, 1.3f, 0.9f};
	// float[] y2 = {3.0f, 2.1f, 1.85f, 1.5f, 1.3f};
	float[] xCircle = { 1, 2, 3, 4, 5, 5 };
	float[] yCircle = { 1, 3, 4, 5, 6, 10 };
	float[] compareXcircle = { 2, 3, 4, 5, 6, 7 };
	float[] compareYcircle = { 1, 3, 4, 5, 6, 10 };
	// float xP0 = 1.0f;
	// float yP0 = 1.0f;
	//
	// float xP1 = 2.0f;
	// float yP1 = 0.0f;
	//
	// float xP2 = 5.0f;
	// float yP2 = 3.0f;
	Graphics g = (Graphics2D) g2;
	g2.setColor(Color.black);
	for (int i = 0; i < wX.length; i++) {
	    g2.draw(new Ellipse2D.Float(xFDraw(x2[i], -500), yFDraw(y2[i], 0),
		    3, 3));
	}
	float[][] testMatrix = { { 1, 0, 0, 1 }, { 0, 2, 1, 2 },
		{ 2, 1, 0, 1 }, { 2, 0, 1, 4 } };

	straightLines(g2, x2, y2, g);
	CubicSplineWikiExample cbw = new CubicSplineWikiExample(x2, y2);
	NaturalCubicSplineExample ncs = new NaturalCubicSplineExample(x2, y2);
	CubicSplineWikiExample cbv = new CubicSplineWikiExample(y2, x2);
	g.setColor(Color.black);
	drawWikiCubics(g2, cbw, cbv);
	drawCubics(g2, ncs);

    }

    private void drawWikiCubics(Graphics2D g2, CubicSplineWikiExample cbw,
	    CubicSplineWikiExample cbv) {
	float[] x = cbw.x;
	float[] y = cbw.y;
	float fromX, fromY;
	float toX, toY;
	fromX = cbw.interpolate(1, 0);
	fromY = cbv.interpolate(1, 0);
	float step = 0.1f;
	for (int i = 1; i < x.length; i++) {
	    for (float it = 0; it < 20; it += step) {
		float yy = cbw.interpolate(i, it);
		float xx = cbv.interpolate(i, it);
		toY = yy;
		toX = xx;
		g2.draw(new Line2D.Float(xFDraw(fromX, -500), yFDraw(fromY, 0),
			xFDraw(toX, -500), yFDraw(toY, 0)));
		fromX = toX;
		fromY = toY;
	    }
	}
	/*
	 * System.out.println(x.length); for (int i = 1; i < x.length; i++) { if
	 * (x[i-1] < x[i]) { for (float it = x[i-1]; it < x[i]; it += step) {
	 * float yy = cbw.interpolate(i, it); float xx = cbv.interpolate(i, it);
	 * toY = yy; toX = it; g2.draw(new Line2D.Float(xFDraw(fromX, -500),
	 * yFDraw(fromY, 0), xFDraw(toX, -500), yFDraw(toY, 0))); fromX = toX;
	 * fromY = toY; } // g2.draw(new Line2D.Float(xFDraw(fromX, -500), //
	 * yFDraw(fromY, 0), xFDraw(x[i+1], -500), yFDraw(y[i+1], 0))); } else {
	 * 
	 * for (float it = x[i]; it <= x[i-1]; it += step) { float yy =
	 * cbw.interpolate(i, it); float xx = cbv.interpolate(i, it); toY = yy;
	 * toX = it; g2.draw(new Line2D.Float(xFDraw(fromX, -500), yFDraw(fromY,
	 * 0), xFDraw(toX, -500), yFDraw(toY, 0))); fromX = toX; fromY = toY; }
	 * } }
	 */

    }

    private void printMatrix(String input, float m[][], float[] b) {
	System.out.println(input);
	for (int i = 0; i < m.length; i++) {
	    System.out.println();
	    for (int j = 0; j < m.length; j++) {
		System.out.print(" " + m[i][j]);
	    }
	    if (b != null)
		System.out.print(" " + b[i + 1]);
	}
	System.out.println("\nend");
    }

    private void straightLines(Graphics2D g2, float[] x, float[] y, Graphics g) {
	for (int i = 0; i < x.length - 1; i++) {
	    if (i % 2 == 0)
		g.setColor(Color.red);
	    else
		g.setColor(Color.green);
	    g2.draw(new Line2D.Float(xFDraw(x[i], -500), yFDraw(y[i], 0),
		    xFDraw(x[i + 1], -500), yFDraw(y[i + 1], 0)));
	}
    }

    public void drawHermites(Graphics2D g2, HermiteCubicSplineExample cs) {
	float fromX, fromY;
	float toX, toY;
	fromX = cs.x[0];
	fromY = cs.y[0];
	for (int i = 0; i < cs.x.length - 1; i++) {
	    if (cs.x[i] < cs.x[i + 1]) {
		for (float p = cs.x[i]; p < cs.x[i + 1]; p += 0.5f) {
		    toY = cs.interpolate(p, i);
		    toX = p;
		    g2.draw(new Line2D.Float(xFDraw(fromX, 0),
			    yFDraw(fromY, 0), xFDraw(toX, 0), yFDraw(toY, 0)));
		    fromX = toY;
		    fromY = toY;
		}
	    } else {
		for (float p = cs.x[i + 1]; p < cs.x[i]; p += 0.5f) {
		    toY = cs.interpolate(p, i);
		    toX = p;
		    g2.draw(new Line2D.Float(xFDraw(fromX, 0),
			    yFDraw(fromY, 0), xFDraw(toX, 0), yFDraw(toY, 0)));
		    fromX = toY;
		    fromY = toY;
		}
	    }
	}
    }

    public void drawCubics(Graphics2D g2, NaturalCubicSplineExample ncs) {
	float[] x = ncs.x;
	float[] y = ncs.y;
	float fromX, fromY;
	float toX, toY;
	fromX = x[0];
	fromY = y[0];
	float step = 0.1f;
	for (int i = 0; i < x.length - 1; i++) {
	    if (x[i] < x[i + 1]) {
		for (float it = x[i]; it <= x[i + 1]; it += step) {
		    float yy = ncs.interpolate(it, i);
		    toY = yy;
		    toX = it;
		    g2.draw(new Line2D.Float(xFDraw(fromX, 0),
			    yFDraw(fromY, 0), xFDraw(toX, 0), yFDraw(toY, 0)));
		    fromX = toX;
		    fromY = toY;
		}
		g2.draw(new Line2D.Float(xFDraw(fromX, 0), yFDraw(fromY, 0),
			xFDraw(x[i + 1], 0), yFDraw(y[i + 1], 0)));
	    } else {

		for (float it = x[i + 1]; it <= x[i]; it += step) {
		    float yy = ncs.interpolate(it, i);
		    toY = yy;
		    toX = it;
		    g2.draw(new Line2D.Float(xFDraw(fromX, 0),
			    yFDraw(fromY, 0), xFDraw(toX, 0), yFDraw(toY, 0)));
		    fromX = toX;
		    fromY = toY;
		}
		g2.draw(new Line2D.Float(xFDraw(fromX, 0), yFDraw(fromY, 0),
			xFDraw(x[i], 0), yFDraw(y[i], 0)));
	    }
	}
    }

    public void iterate(int loww, int highh, int index,
	    NaturalCubicSplineExample ce, float[] x, float[] y, Graphics2D g2) {
	int high = highh;
	int low = loww;
	if (x[loww] > x[highh]) {
	    low = highh;
	    high = loww;
	}
	float toX, toY;
	float fromX, fromY;
	fromX = x[low];
	fromY = ce.interpolate(x[low], index);
	for (float i = x[low] + 0.2f; i <= x[high]; i += 0.2f) {
	    toX = i;
	    toY = ce.interpolate(i, index);
	    g2.draw(new Line2D.Float(xFDraw(fromX, 0), yFDraw(fromY, 0),
		    xFDraw(toX, 0), yFDraw(toY, 0)));
	    System.out.println("x  : " + i + " y : " + toY);
	    fromX = toX;
	    fromY = toY;
	}
	g2.draw(new Line2D.Float(xFDraw(fromX, 0), yFDraw(fromY, 0), xFDraw(
		x[high], 0), yFDraw(y[high], 0)));

    }

}
