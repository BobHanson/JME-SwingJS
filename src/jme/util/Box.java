package jme.util;

import java.awt.geom.Rectangle2D;

/**
 *
 * @author hansonr
 *
 */
public interface Box {

	enum Axis {X, Y}

	static double get(Rectangle2D.Double box, Axis xOrY) {
		return (xOrY == Axis.X? box.getX(): box.getY());
	}

	static double getDim(Rectangle2D.Double box, Axis xOrY) {
		return xOrY == Axis.X ? box.getWidth(): box.getHeight();
	}

	static Rectangle2D.Double createUnion(Rectangle2D.Double box, Rectangle2D.Double r, Rectangle2D.Double union) {
		if (union == null) {
			union = new Rectangle2D.Double();
			union.setFrame(box);
		} else {
			Rectangle2D.union(union, box, union);
		}
		if (r != null && r != union)
			Rectangle2D.union(union, r, union);
		return union;
	}

}
