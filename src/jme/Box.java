package jme;

//START JAVA_IMPORT
import java.awt.geom.Rectangle2D;


//END JAVA_IMPORT

//START GWT_IMPORT
//import ejava.awt.geom.Rectangle2D;


//END GWT_IMPORT


//END GWT_IMPORT


public class Box extends Rectangle2D.Double {
	
	public enum Axis {X, Y};
	public final static Axis Axes[] = {Axis.X, Axis.Y};
	
	public Box(double x, double y, double width, double height) {
		super(x,y,width,height); //why do we need to redefine the constructor?
	}
	
	public Box() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean isEmpty() {
		return this.width <= 0 || this.height <= 0;
	}
	double get(Axis xOrY) {
		return xOrY == Axis.X? getX(): getY();
	}
	double getDim(Axis xOrY) {
		return xOrY == Axis.X ? getWidth(): getHeight();
	}

	Box multiply(double factor) {
		x *= factor ;
		y *= factor;
		width *= factor;
		height *= factor;
		
		return  this;
	}
	/**
	 * Duplicated code with super class to avoid type casting error
	 * at run time
	 * @param r
	 * @return
	 */
    public Box createUnion(Box r) {
        Box dest = new Box();
        Box.union(this, r, dest);
        return dest;
    }

	int getRoundedWidth() {
		return (int)Math.round(width);
	}	
	int getRoundedHeight() {
		return (int)Math.round(height);
	}
	
	int getRoundedTotalWidth(double multiply) {
		return (int)Math.round((width  + x) * multiply);
	}	
	int getRoundedTotalHeight(double multiply) {
		return (int)Math.round((height + y) * multiply);
	}
	
	int getRoundedX() {
		return (int) Math.round(x);
	}
	
	int getRoundedY() {
		return (int) Math.round(y);
	}
}
