/**
 * 
 */
package jme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


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
public abstract class  Graphical2DObject {
	abstract void draw(PreciseGraphicsAWT og);
	
	protected abstract void moveXY(double movex, double movey);

	public abstract Box computeBoundingBoxWithAtomLabels(Box union);

	
	public abstract double centerX();

	public abstract double centerY();
	
	
	public abstract void XY(double x, double y);
	

	public void move(Box.Axis xOrY, double d) {
		if(xOrY == Box.Axis.X)
			moveXY(d, 0);
		else
			moveXY(0, d);

	}
	public double center(Box.Axis xOrY) {
		double result ;
		
		if(xOrY == Box.Axis.X)
			result = centerX();
		else
			result = centerY();
		
		
		return result;

	}
	
	
	void move(double movex, double movey, Rectangle2D.Double boundingBoxLimits) {

		Box bbox = computeBoundingBoxWithAtomLabels(null);
		double centerx=bbox.getCenterX(); double centery=bbox.getCenterY();

		//does not work correctly if moved to bottom right corner
		//is the
		if (
				(movex <0 && centerx < boundingBoxLimits.x) ||
				(movex >0 && centerx > boundingBoxLimits.width) ||

				(movey <0 && centery < boundingBoxLimits.y) ||
				(movey >0 && centery > boundingBoxLimits.height)
				)
			//don't move the object further if it is too far
			return;

		

		moveXY(movex, movey);


	}
	
	public void move(Point2D.Double shiftXY) {
		this.moveXY(shiftXY.x, shiftXY.y);

	}
	
	/** return a deep / safe copy of my bounding box
	 * 
	 * @return an empty box if empty
	 */
	public Box newBoundingBox() {
		return computeBoundingBoxWithAtomLabels(null);
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
	public static double closestDistancePointToLine_WRONG(double x0, double y0, double x1, double y1, double x2, double y2) {
		double result ;
		double dlX = x2 - x1;
		double dlY = y2 - y1;
		// if  dlY is 0, then parameter x0 is irrelevant => does not work
		double nominator = Math.abs(dlY*x0 - dlX*y0 + x2*y1 - y2*x1);
		double denominator = Math.sqrt(dlY * dlY + dlX * dlX);
		
		if (denominator > 0) {
			result = nominator / denominator;
		} else {
			result = Double.MAX_VALUE;
		}
		
		assert( result >= 0);
		return result;
	}
	

	
	/**
	 * https://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
	 * modified to handle case with a line length = 0
	 * x3, y3 is the point
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @return
	 */
	private static double shortestDistance(double x1, double y1, double x2, double y2, double x3, double y3) {
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
		//return closestDistancePointToLine(x0, y0, line.x1, line.y1, line.x2, line.y2);
		return shortestDistance(line.x1, line.y1, line.x2, line.y2, x0, y0);
	}

	abstract public double closestDistance(double x, double y);
	
	

}

class  Graphical2DObjectGroup<T extends Graphical2DObject> extends Graphical2DObject {

	ArrayList<T> group;
	
	public Graphical2DObjectGroup() {
		this.group = new ArrayList<T>();
	}
		
	public Graphical2DObjectGroup( ArrayList<T> initGroup) {
		this.group = new ArrayList<T>(initGroup);
	}
	
	@Override
	void draw(PreciseGraphicsAWT og) {
		for(T go : this.group) {
			go.draw(og);
		}
		
	}

	@Override
	protected void moveXY(double movex, double movey) {
		for(T go : this.group) {
			go.moveXY(movex, movey);
		}
		
	}

	@Override
	// duplicated code with JMEmolList
	public Box computeBoundingBoxWithAtomLabels(Box union) {
		for (T go : group) {
			union = go.computeBoundingBoxWithAtomLabels(union); 
		}
		return union;
	}

	@Override
	public double centerX() {
		double center = 0;
		for(T go : this.group) {
			center += go.centerX();
		}
		
		if ( this.group.size() > 0) {
			center /= group.size();
		}
		return center;
	}

	@Override
	public double centerY() {
		double center = 0;
		for(T go : this.group) {
			center += go.centerY();
		}
		
		if ( this.group.size() > 0) {
			center /= group.size();
		}
		return center;	
	}

	@Override
	public void XY(double x, double y) {
		for(Graphical2DObject go : this.group) {
			go.moveXY(x, y);
		}

		
	}
	
	/**
	 * Align all my objects based on their respective centers
	 * @param xOrY
	 */
	public void alignCenter(Box.Axis xOrY) {
		double groupCenter = this.center(xOrY);
		
		for(Graphical2DObject go : this.group) {
			double delta = groupCenter - go.center(xOrY);
			go.move(xOrY, delta);
		}
		
	}

	
	
	@Override
	public double closestDistance(double x, double y) {
		double min = Double.MAX_VALUE;
		for(T go : this.group) {
			min = Math.min(min,  go.closestDistance(x, y));
		}
		
		return min;
	}

	public void add(T element) {
		this.group.add(element);
		
	}

	public void add(int pos, T element) {
		this.group.add(pos, element);
		
	}
	public int size() {
		return this.group.size();
	}

	public void distributePositions(final Box.Axis xOrY, double margin) {
		distributePositions(xOrY, margin, true);
	}

	
	/**
	 * keepXorYorder: should be true most of the time such that the object on the left stays on left,
	 * otherwise the first object on the left would be  the first object in my group
	 * @param xOrY
	 * @param margin
	 * @param keepXorYorder
	 */
	public void distributePositions(final Box.Axis xOrY, double margin, boolean keepXorYorder) {
		
		if (this.size() <= 1) {
			return;
		}
				
		Graphical2DObjectGroup<T> sorted = new Graphical2DObjectGroup<T>();
		sorted.addAll(this);
		sorted.removeNoSizeObjects(); //otherwise boundingBox is null
		if (sorted.size() <= 1) {
			return;
		}
		double beforeAlignCenter = sorted.center(xOrY);
		if (keepXorYorder) {
			Collections.sort(sorted.group, new Comparator<T>() {
				@Override
				public int compare(T m1, T m2) {
					// -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
					double x2 = m1.computeBoundingBoxWithAtomLabels(null).get(xOrY);
					double x1 = m2.computeBoundingBoxWithAtomLabels(null).get(xOrY);
					return x1 > x2 ? -1 : (x2 < x1) ? 1 : 0;
				}
			});
		}
		double sumMove = 0;
		for(T mol: sorted.group) {
			Box moleculeBox = mol.computeBoundingBoxWithAtomLabels(null);
			double move = sumMove - moleculeBox.get(xOrY);
			mol.move(xOrY, move);
			sumMove += moleculeBox.getDim(xOrY) + margin;
		}
		
		// the algorithm above move the objects.
		// apply correction
		double afterAlignCenter = sorted.center(xOrY);
		sorted.move(xOrY, beforeAlignCenter - afterAlignCenter);
		assert(Math.abs(beforeAlignCenter - sorted.center(xOrY)) < 0.001);

	}

	/**
	 * @return 
	 * 
	 */
	protected boolean removeNoSizeObjects() {
		
		Graphical2DObjectGroup<T> emptyList= new Graphical2DObjectGroup<T>();
		for(T mol :this.group) {
			Box moleculeBox = mol.computeBoundingBoxWithAtomLabels(null);
			if (moleculeBox == null || moleculeBox.isEmpty()) {
				emptyList.add(mol);
			}
		}
		
		for(T mol : emptyList.group) {
			this.group.remove(mol);
		}
			
		return emptyList.size() > 0;
		
	}

	public void addAll(Graphical2DObjectGroup<T> graphical2dObjectGroup) {
		this.addAll(graphical2dObjectGroup.group);
		
	}
	
	public void addAll(ArrayList<T> graphical2dObjectList) {
		this.group.addAll(graphical2dObjectList);
		
	}





}
