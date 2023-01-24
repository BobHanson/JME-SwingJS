/**
 * 
 */
package jme;

//START JAVA_IMPORT
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

//END JAVA_IMPORT

//START GWT_IMPORT
//import ejava.awt.geom.Line2D;
//import ejava.awt.geom.Point2D;
//import ejava.awt.geom.Rectangle2D;

//END GWT_IMPORT

/**
 * @author bruno
 *
 */
public interface Graphical2DObject {

	public boolean isEmpty();

	public void draw(PreciseGraphicsAWT og);

	public void moveXY(double movex, double movey);

	public Box computeBoundingBoxWithAtomLabels(Box union);

	public double centerX();

	public double centerY();

	public static void move(Graphical2DObject o, Box.Axis xOrY, double d) {
		if (xOrY == Box.Axis.X)
			o.moveXY(d, 0);
		else
			o.moveXY(0, d);
	}

	public static double center(Graphical2DObject o, Box.Axis xOrY) {
		return (xOrY == Box.Axis.X ? o.centerX() : o.centerY());
	}

	public static void move(Graphical2DObject o, double movex, double movey, Rectangle2D.Double boundingBoxLimits) {
		if (o.isEmpty())
			return;
		Box bbox = o.computeBoundingBoxWithAtomLabels(null);
		double centerx = bbox.getCenterX();
		double centery = bbox.getCenterY();
		if ((movex < 0 && centerx < boundingBoxLimits.x) || (movex > 0 && centerx > boundingBoxLimits.width)
				|| (movey < 0 && centery < boundingBoxLimits.y) || (movey > 0 && centery > boundingBoxLimits.height))
			return;
		o.moveXY(movex, movey);
	}

	public static void move(Graphical2DObject o, Point2D.Double shiftXY) {
		o.moveXY(shiftXY.x, shiftXY.y);
	}

	/**
	 * return a deep / safe copy of my bounding box
	 * 
	 * @return an empty box if empty
	 */
	public static Box newBoundingBox(Graphical2DObject o) {
		return o.computeBoundingBoxWithAtomLabels(null);
	}

	/**
	 * https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_two_points
	 * 
	 * @param x0 point
	 * @param y0 point
	 * @param x1 line start
	 * @param y1 line start
	 * @param x2 line end
	 * @param y2 line end
	 * @return
	 */
	public static double closestDistancePointToLine_WRONG(double x0, double y0, double x1, double y1, double x2,
			double y2) {
		double result;
		double dlX = x2 - x1;
		double dlY = y2 - y1;
		// if dlY is 0, then parameter x0 is irrelevant => does not work
		double nominator = Math.abs(dlY * x0 - dlX * y0 + x2 * y1 - y2 * x1);
		double denominator = Math.sqrt(dlY * dlY + dlX * dlX);

		if (denominator > 0) {
			result = nominator / denominator;
		} else {
			result = Double.MAX_VALUE;
		}

		assert (result >= 0);
		return result;
	}

	/**
	 * https://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
	 * modified to handle case with a line length = 0 x3, y3 is the point
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @return
	 */
	public static double shortestDistance(double x1, double y1, double x2, double y2, double x3, double y3) {
		double px = x2 - x1;
		double py = y2 - y1;
		double temp = (px * px) + (py * py);
		double dist;
		double dx;
		double dy;

		// if line length == 0
		if (temp == 0) {
			dx = x2 - x3;
			dy = y2 - y3;
		} else {

			double u = ((x3 - x1) * px + (y3 - y1) * py) / (temp);
			if (u > 1) {
				u = 1;
			} else if (u < 0) {
				u = 0;
			}
			double x = x1 + u * px;
			double y = y1 + u * py;

			dx = x - x3;
			dy = y - y3;
		}
		dist = Math.sqrt(dx * dx + dy * dy);
		return dist;
	}

	public static double closestDistancePointToLine(double x0, double y0, Line2D.Double line) {
		// return closestDistancePointToLine(x0, y0, line.x1, line.y1, line.x2,
		// line.y2);
		return shortestDistance(line.x1, line.y1, line.x2, line.y2, x0, y0);
	}

	public double closestDistance(double x, double y);

}
