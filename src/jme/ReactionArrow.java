package jme;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

class ReactionArrow implements Graphical2DObject {

	double arrowWidth = 24 * 2;
	
	double centerX = 0;
	double centerY = 0;
	
	public boolean hasBeenPlaced = false;
	
	Rectangle2D.Double boundingBox;
	
	
	Line2D.Double horizontalLine = new Line2D.Double();
	Line2D.Double topTipLine = new Line2D.Double();
	Line2D.Double bottomTipLine = new Line2D.Double();
	
	Line2D.Double[]  lines = new Line2D.Double[] {horizontalLine, topTipLine, bottomTipLine};


	// TODO: use a base length propotiional to JMEmol RBond
	public ReactionArrow(double size) {
		this.boundingBox = new Rectangle2D.Double();
		this.arrowWidth = size;
	}
	
	
	public double width() {
		return this.arrowWidth;
	}
	
	@Override
	public void draw(PreciseGraphicsAWT og) {
				
		double m = arrowHeigth() / 2; // hrot sipky
		og.setColor(Color.magenta);
		double xLeft = centerX - arrowWidth / 2;
		double xRight = centerX + arrowWidth / 2;
		
		horizontalLine.setLine(xLeft, centerY, xRight, centerY);
		topTipLine.setLine(xRight, centerY, xRight - m, centerY + m);
		bottomTipLine.setLine(xRight, centerY, xRight - m, centerY - m);
		

		for(Line2D.Double eachLine : this.lines ) {
			og.drawLine(eachLine.x1, eachLine.y1, eachLine.x2, eachLine.y2);
		}
		
		
	}
	
	protected double arrowHeigth() {
		return arrowWidth / 4 ;
	}

	
	/**
	 * Absolute positioning
	 */
	public void XY(double x, double y) {
		hasBeenPlaced = true;
		centerX = x;
		centerY = y;
	}
	@Override
	public void moveXY(double moveX, double moveY) {
		hasBeenPlaced = true;
		centerX += moveX;
		centerY += moveY;
		//System.out.println("Moved arrow: " + centerX + ", " + centerY);
		
	}

	public Rectangle2D.Double updateBoundingBox() {
		boundingBox.setRect(centerX - arrowWidth / 2, centerY() + arrowHeigth() / 2, arrowWidth, arrowHeigth());
		return boundingBox;
	}
	
	@Override
	public Rectangle2D.Double computeBoundingBoxWithAtomLabels(Rectangle2D.Double union) {
		updateBoundingBox();
		if (union != null)
			return JMEUtil.createUnion(boundingBox, union, union);
		union = new Rectangle2D.Double();
		union.setFrame(boundingBox);
		return union;
	}

	@Override
	public double centerX() {
		return centerX;
	}

	@Override
	public double centerY() {
		return centerY;
	}


	@Override
	public double closestDistance(double x, double y) {
		double min = Double.MAX_VALUE;		
		for(Line2D.Double eachLine : this.lines ) {
			double d = Graphical2DObject.closestDistancePointToLine(x, y, eachLine);
			min = Math.min(d, min);
		}
		return min;
	}


	@Override
	public boolean isEmpty() {
		return false;
	}


}