import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.Line2D;

// Swing Program Template
@SuppressWarnings("serial")
public class DT extends JFrame {
    // Name-constants
    public static final int CANVAS_WIDTH = 2000;
    public static final int CANVAS_HEIGHT = 1000;
    public static final String TITLE = "Test program.";
    public static Run run;
    public int xMax;
    public int yMax;
    public double scale;

    // ......

    // private variables of GUI components
    // ......

    /** Constructor to setup the GUI components */
    public DT(Run r) {
	this.run = r;
	grafen = new Graph();
	getContentPane().add(grafen, BorderLayout.CENTER);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	pack();
	setVisible(true);
	setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
	size = 600;
	margin = 50;
	scale = size * 1.0 / xMax;
    }

    Graph grafen;
    int size, margin;
    ScaleTools sc = new ScaleTools();
    class Graph extends JPanel {
	int size;

	Graph() {
	    setPreferredSize(new Dimension(CANVAS_WIDTH - 160,
		    CANVAS_HEIGHT - 160));
	}

	/** Custom painting codes on this JPanel */

	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    setBackground(Color.WHITE);
	    g.setColor(Color.green);
	    Graphics2D g2 = (Graphics2D) g;
	    
	    run.draw(g2);
	    

	    // Your custom painting codes
	    // ......
	}

    }

    class ScaleTools {

	float xFDraw(float x, int sub) {
	    return (float) (x * scale) + margin - sub;
	}

	float yFDraw(float y, int sub) {
	    return (float) ((yMax - y) * scale) + margin - sub;
	}
    }

    /** The entry main() method */
    public static void main(String[] args) {
	// Run GUI codes in the Event-Dispatching thread for thread safety
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		new DT(new KubiskTestProgram());
		// new DT(new Bezier());
		// new DT(new Polynom4th());
	    }
	});
    }
}
