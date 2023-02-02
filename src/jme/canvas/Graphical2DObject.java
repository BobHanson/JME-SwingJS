/**
 * 
 */
package jme.canvas;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import jme.util.Box;

/**
 * @author bruno
 *
 */
public interface Graphical2DObject {

	public boolean isEmpty();

	public void draw(PreciseGraphicsAWT og);

	public void moveXY(double movex, double movey);

	public Rectangle2D.Double computeBoundingBoxWithAtomLabels(Rectangle2D.Double union);

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
		Rectangle2D.Double bbox = o.computeBoundingBoxWithAtomLabels(null);
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
	public static Rectangle2D.Double newBoundingBox(Graphical2DObject o) {
		Rectangle2D.Double box = o.computeBoundingBoxWithAtomLabels(null);
		return (box == null ? new Rectangle2D.Double() : box);
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
