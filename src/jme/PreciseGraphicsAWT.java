package jme;

//START JAVA_IMPORT

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints.Key;
import java.awt.geom.Rectangle2D;

//END JAVA_IMPORT

//START GWT_IMPORT

//import ejava.awt.BasicStroke;
//import ejava.awt.Color;
//import ejava.awt.Component;
//import ejava.awt.Font;
//import ejava.awt.FontMetrics;
//import ejava.awt.Graphics;
//import ejava.awt.Graphics2D;
//import ejava.awt.geom.Rectangle2D;
//import ejava.awt.Paint;
//import ejava.awt.RenderingHints.Key;
//import ejava.lang.System;

//END GWT_IMPORT




/**
 * A combined Font and FontMetrics class
 * compute string width and string height in floating point
 * Does not work correctly if getPrecisionFactor > 2, the string width and height are getting too small
 * with FF
 */





class PreciseFontMetrics {
	protected Font enlargeddFont ;
	protected FontMetrics enlargedFontMetrics;
	protected Font font;
	
	/**
	 * comp is needed to get the enlargedFontMetrics
	 * @param font
	 * @param comp
	 */
	public PreciseFontMetrics(Font font, Component comp) {
		this.font = font;
		this.enlargeddFont = font.deriveFont((float) (font.getSize() * this.getPrecisionFactor()));
		this.enlargedFontMetrics = comp.getFontMetrics(enlargeddFont);
	}
	
	public double stringWidth(String text) {
		return (double)(this.enlargedFontMetrics.stringWidth(text))/getPrecisionFactor();
	}

	double getPrecisionFactor() {
		return 1.0;
		//return PreciseGraphicsAWT.getPrecisionFactor();
	}

	public Font getFont() {
		return this.font;
	}

	/*
	 * Provide the ideal height of a string consisting of usual upper case characters.
	 * Purpose: centering of String in the center of a box.
	 * Does not work for $ , y ;  and others
	 */

	public double stringHeight() {
		double height = enlargedFontMetrics.getAscent() - enlargedFontMetrics.getDescent();

		return height / this.getPrecisionFactor();
	}


}


/**
 * TODO: handle the text width with automatic centering
 * drawCenteredString(.... )
 * SVG and VML subclass
 * 
 * @author bruno
 *
 */
public class PreciseGraphicsAWT {
	
	
	public class ExtendedGraphics2D {
	
		// BH 2023.01.18 inner class -- no need to duplicate references to original graphics object.
		// These generally should not be held as fields.
		
		public void drawStringWithStroke(String str, int x, int y, Color strokeColor, int strokeWidth) {
	
			// TBC for Java
	
			// ((StringGraphics)this.baseGraphics).drawString(str, x, y, strokeColor,
			// strokeWidth);
			// this.baseGraphics.dr
	
			baseGraphics.drawString(str, x, y);
	
		}
	
		/*
		 * 
		 */
		/**
		 * 
		 * @param str
		 * @param x
		 * @param y
		 * @param strokeColor
		 * @param strokeWidth
		 * @param             subscripts: sections of text to be subscript e.g. NH2, 2
		 *                    must be subscript, [[2,1]]
		 * @param superscript sections of text to be supescript, e.g Ca++ ++ must be
		 *                    superscript. [[2,2]], 13C -> [[0.2]]
		 */
		public void drawStringWithStrokeAndBaselineShifts(String str, int x, int y, Color strokeColor, int strokeWidth,
				int subscripts[][], int superscript[][]) {
	
			// TBC for Java
	
			// ((StringGraphics)this.baseGraphics).drawString(str, x, y, strokeColor,
			// strokeWidth, subscripts, superscript);
	
			baseGraphics.drawString(str, x, y);
	
		}
	}

	static BasicStroke defaultStroke = new BasicStroke(1.0f);
	Graphics2D baseGraphics;
	PreciseGraphicsAWT.ExtendedGraphics2D extendedBaseGraphics;
	
	protected double scale = 1.0; //for zooming in and out
	protected Font unscaledFont ;
	protected Font enlargeddFont ;
	protected BasicStroke unscaledStroke;
	protected BasicStroke enlargedStroke ;
	
	protected Color colorOverride = null;
	protected Color defaultBackGroundColor = null;
	protected Color savedColor = null;
	
	//used to convert an event X,y into my coordinate space

	Rectangle2D.Double screenArea = new Rectangle2D.Double(); //not scaled
	//actually needs only integer
	
	/**
	 * width to be used for drawing using real coordinates
	 * @return
	 */
	double getWidth() {
		return this.screenArea.width/this.scale;
	}
	
	/**
	 * height to be used for drawing using real coordinates
	 * @return
	 */
	double getHeight() {
		return this.screenArea.height/this.scale;
	}
	void setDrawOnScreenCoordinates(int x, int y) {
		screenArea.x = x;
		screenArea.y = y;
	}
	void setDrawOnScreenCoordinates(Rectangle2D.Double screenArea) {
		this.screenArea.x = screenArea.x;
		this.screenArea.y = screenArea.y;
		this.screenArea.height = screenArea.height;
		this.screenArea.width = screenArea.width;
		
	}
	void setDrawOnScreenCoordinates(int x, int y, int width, int height) {
		this.screenArea.x = x;
		this.screenArea.y = y;
		this.screenArea.height = height;
		this.screenArea.width = width;
		
	}
	
	/**
	 * The pixel coordinate of the drawing area
	 * @return
	 */
	public int screenX() {
		return (int)screenArea.x;
		
	}
	/**
	 * The pixel coordinate of the drawing area
	 * @return
	 */
	public int screenY() {
		return (int)screenArea.y;
	}
	
	double screenToCoord(int pixel) {
		return (double)pixel/this.scale;
	}
	int coordToScreen(double coord) {
		return (int)(coord * this.scale +0.5);
	}
	
	double screenToCoordX(int x) {
		return screenToCoord(x- (int)this.screenArea.x) ;
	}
	double screenToCoordY(int y) {
		return screenToCoord(y - (int)this.screenArea.y) ;
	}
	
	
	public double currentZoomFactor() {
		return scale;
		
	}

	public PreciseGraphicsAWT(Graphics graphics) {
		baseGraphics = (Graphics2D) graphics;
		if(JME.precision > 1.0) {
			setStroke(defaultStroke);
		}
		extendedBaseGraphics = new PreciseGraphicsAWT.ExtendedGraphics2D();
	}

	public void initPrecisionScale() {
		this.initPrecisionScale(1.0);

	}
	
	public void initPrecisionScale(double absoluteScale) {
		
		//Concatenates the current Graphics2D Transform with a scaling transformation Subsequent rendering is resized according to the specified scaling factors relative to the previous scaling.
		
		
//		if(false) {
			//Java implementation FIXME why is it diferent?
			if(absoluteScale != scale) {
				double transformScale = absoluteScale/scale;
				this.baseGraphics.scale(transformScale/JME.precision, transformScale/JME.precision);
				scale = absoluteScale;
			}
//		} else { //JavaScript
//			scale = absoluteScale;
//			this.baseGraphics.(scale/JME.precision, scale/JME.precision);
//		}
	}

	public Graphics2D getGraphics() {
		return this.baseGraphics;
	}

	public Color getDefaultBackGroundColor() {
		return defaultBackGroundColor;
	}
	public void setDefaultBackGroundColor(Color backGroundColor) {
		this.defaultBackGroundColor = backGroundColor;
	}
	public void setBackGroundColor() {
		this.setColor(this.defaultBackGroundColor);
	}

	public void overrideColor(Color color) {
		this.colorOverride = color;
		this.savedColor = this.baseGraphics.getColor();
		this.baseGraphics.setColor(color);
	}
	
	public void resetOverrideColor() {
		this.colorOverride = null;
		this.baseGraphics.setColor(this.savedColor);
	}
	public void setColor(Color color) {
		// if override color is used, than only one color is actually used for the foreground
		// still need a background color e.g. for text on top of a line
		if( this.colorOverride == null)
			this.baseGraphics.setColor(color);
		else {
			if(color == this.defaultBackGroundColor || color.equals(this.defaultBackGroundColor)) {
				this.baseGraphics.setColor(this.defaultBackGroundColor);
			} else {
				this.baseGraphics.setColor(this.colorOverride);
			}
		}
	}

	public void fillRect(double x, double y, double width, double height) {
		this.baseGraphics.fillRect(r(x), r(y), r(width), r(height));
	}
	public void fillRoundRect(double x, double y, double width, double height,  double arcWidth, double arcHeight) {
		this.baseGraphics.fillRoundRect(r(x), r(y), r(width), r(height), r(arcWidth), r(arcHeight));
	}
	
	


	/**
	 * Reimplementation needed because the original AWT uses a linewidth equal to 1, which is too small
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param raised
	 */
	public void fill3DRect(double x, double y, double width, double height, boolean raised) {
		//this.baseGraphics.fill3DRect(r(x), r(y), r(width), r(height), raised);

        // According to the spec, color should be used instead of paint,
        // so Graphics.fill3DRect resets paint and
        // it should be restored after the call
        Paint savedPaint = this.baseGraphics.getPaint();
        Color color = this.baseGraphics.getColor();
        Color colorUp, colorDown;
        
        
        if (raised) {
            colorUp = color.brighter();
            colorDown = color.darker();
            this.baseGraphics.setColor(color);
        } else {
            colorUp = color.darker();
            colorDown = color.brighter();
            this.baseGraphics.setColor(colorUp);
        }
        int border = r(1.0);
        int widthInt = r(width);
        int heightInt = r(height);
        int xInt = r(x);
        int yInt = r(y);
        
        
        widthInt -= border;
        heightInt -= border;
        this.baseGraphics.fillRect(xInt+border, yInt+border, widthInt-border, heightInt-border);

        this.baseGraphics.setColor(colorUp);
        this.baseGraphics.fillRect(xInt, yInt, widthInt, border);
        this.baseGraphics.fillRect(xInt, yInt+border, border, heightInt);

        this.baseGraphics.setColor(colorDown);
       // this.baseGraphics.fillRect(xInt+widthInt, yInt, border, heightInt);
        this.baseGraphics.fillRect(xInt+widthInt, yInt, border, heightInt+border); //BB added + border, looks better
        this.baseGraphics.fillRect(xInt+1, yInt+heightInt, widthInt, border);
        
        
        this.baseGraphics.setPaint(savedPaint);

	}

	public void drawRect(double x, double y, double width, double height) {
		this.baseGraphics.drawRect(r(x), r(y), r(width), r(height));
	}

	public void drawLine(double x1, double y1, double x2, double y2) {
		this.baseGraphics.drawLine(r(x1), r(y1), r(x2), r(y2));
	}

	public void setFont(Font font) {
		this.unscaledFont = font;
		this.enlargeddFont = font.deriveFont((float) (font.getSize() * JME.precision));
		this.baseGraphics.setFont(this.enlargeddFont);
	}

	
	public void drawString(String str, double x, double y) {
		this.baseGraphics.drawString(str, r(x), r(y));
	}
	
	/**
	 * Used for text with a colored background to make the text more readable. Works only in HiDPI screen
	 * or large zoom (200%)
	 * @param str
	 * @param x
	 * @param y
	 * @param strokeColor
	 * @param strokeWidth
	 */
	public void drawStringWithStroke(String str, double x, double y, Color strokeColor, double strokeWidth ) {
		this.extendedBaseGraphics.drawStringWithStroke(str, r(x), r(y), strokeColor, r(strokeWidth));
	}
	
	
	/**
	 * 
	 * @param str
	 * @param x
	 * @param y
	 * @param strokeColor
	 * @param strokeWidth
	 * @param subscripts: sections of text to be subscript e.g. NH2, 2 must be subscript, [[2,1]]
	 * @param superscript sections of text to be supescript, e.g Ca++ ++ must be superscript. [[2,2]], 13C -> [[0.2]]
	 */
	public void drawStringWithStrokeAndBaselineShifts(String str, double x, double y, Color strokeColor, double strokeWidth,
			int subscripts[][], int superscript[][]) {
		this.extendedBaseGraphics.drawStringWithStrokeAndBaselineShifts(str, r(x), r(y), strokeColor, r(strokeWidth), subscripts, superscript);
	}
	/*
	 * Convert a coordinate to a pixel position
	 */
	protected int r(double v) {
		return  (int) Math.round(v * JME.precision);
	}

	protected double ir(int v) {
		return  (double)v / JME.precision;
	}

	
	public void fillOval(double x, double y, double width, double height) {
		this.baseGraphics.fillOval(r(x), r(y), r(width), r(height));
	}
	public void drawOval(double x, double y, double width, double height) {
		this.baseGraphics.drawOval(r(x), r(y), r(width), r(height));
	}

	
	
	public void drawArc(double x, double y, double width, double height, double startAngle, double arcAngle) {
		this.baseGraphics.drawArc(r(x), r(y), r(width), r(height), (int)Math.round(startAngle), (int)Math.round(arcAngle));
	}

	public void translate(double x, double y) {
	
		baseGraphics.translate(r(x),  r(y));
	
	}


	public void drawPolygon(double xPoints[], double yPoints[], int nPoints) {
		int xPointsInt[] = new int[nPoints];
		int yPointsInt[] = new int[nPoints];
		
		for(int i = 0; i < nPoints; i++) {
			xPointsInt[i] = r(xPoints[i]);
			yPointsInt[i] = r(yPoints[i]);
		}
		
		this.baseGraphics.drawPolygon(xPointsInt, yPointsInt, nPoints);
	}

	public void fillPolygon(double xPoints[], double yPoints[], int nPoints) {
		int xPointsInt[] = new int[nPoints];
		int yPointsInt[] = new int[nPoints];
		
		//duplicated code with drawPolygon
		for(int i = 0; i < nPoints; i++) {
			xPointsInt[i] = r(xPoints[i]);
			yPointsInt[i] = r(yPoints[i]);
		}
		
		this.baseGraphics.fillPolygon(xPointsInt, yPointsInt, nPoints);
	}

    public void setRenderingHint(Key hintKey, Object hintValue) {
		this.baseGraphics.setRenderingHint(hintKey,hintValue );
		
	}

    /**
     * Only the stroke width is handled
     * @param basicStroke
     */
	public void setStroke(BasicStroke basicStroke) {
		this.unscaledStroke = basicStroke;
		this.enlargedStroke = new BasicStroke((float) (JME.precision * basicStroke.getLineWidth()));		
		this.baseGraphics.setStroke(this.enlargedStroke);
		
	}

}
