package jme.gui;

//START JAVA_IMPORT

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import jme.canvas.PreciseGraphicsAWT;

//END JAVA_IMPORT





//START GWT_IMPORT
//import ejava.awt.Rectangle;
//import ejava.awt.geom.Rectangle2D;


//END GWT_IMPORT

/**
 * New class intended to provide an alternative to the simplistic 
 * event handling of AWT: it is not possible to attach a mouse event
 * to a graphical element
 * 
 * the frame of the icon is scaled based on the graphics - is coordinate
 * @author bruno
 *
 */
@SuppressWarnings("serial")
public class Icon extends Rectangle2D.Double {

	//the Graphics on which the icon is drawn

	PreciseGraphicsAWT pg;
	
	public Icon(PreciseGraphicsAWT pg) {
		this.pg = pg;
	}
	/*
	 * useful for event handling, e.g. mouse click did happen on top of the icon
	 */
	public boolean contains(int screenX, int screenY) {
		boolean result = this.contains(pg.screenToCoordX(screenX), pg.screenToCoordY(screenY));
		return result;
	}
	/**
	 * The position and dimension of the icon relative to the top left of the applet
	 * as screen pixels.
	 * This method can be used to provide the frame of the DnD icon to an AppletViewer
	 * that can handle DnD (JSME)
	 * @return
	 */
	public Rectangle onScreenFrame() {
		Rectangle frame = new Rectangle();
		frame.x = pg.screenX() + pg.coordToScreen(this.x);
		frame.y = pg.screenY() + pg.coordToScreen(this.y);
		frame.width = pg.coordToScreen(this.width);
		frame.height = pg.coordToScreen(this.height);
		
		return frame;
		
	}

}