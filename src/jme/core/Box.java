package jme.core;

import java.awt.geom.Rectangle2D;

/**
 *
 * @author hansonr
 *
 */
public interface Box {
	
	public enum Axis {X, Y};

	public static double get(Rectangle2D.Double box, Axis xOrY) {
		return (xOrY == Axis.X? box.getX(): box.getY());
	}

	public static double getDim(Rectangle2D.Double box, Axis xOrY) {
		return xOrY == Axis.X ? box.getWidth(): box.getHeight();
	}

	public static Rectangle2D.Double createUnion(Rectangle2D.Double box, Rectangle2D.Double r, Rectangle2D.Double union) {
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
