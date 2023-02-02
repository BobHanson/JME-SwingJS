package jme.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jme.JME;
import jme.JMEmol;
import jme.canvas.ColorManager;
import jme.canvas.ColorManager.ColorInfo;
import jme.canvas.PreciseGraphicsAWT;
import jme.canvas.PreciseImage;
import jme.core.Atom;
import jme.core.Bond;
import jme.core.JMECore;

public class GUI {

	@SuppressWarnings("serial")
	public static class Icon extends Rectangle2D.Double {

		/**
		 * the Graphics on which the icon is drawn
		 */
		protected PreciseGraphicsAWT pg;
		
		private Icon(PreciseGraphicsAWT pg) {
			this.pg = pg;
		}
		/**
		 * useful for event handling, e.g. mouse click did happen on top of the icon
		 */
		public boolean contains(int screenX, int screenY) {
			return contains(pg.screenToCoordX(screenX), pg.screenToCoordY(screenY));
		}
	}
	
	public static boolean isSwingJS = /** @j2sNative true || */
			false;
	
	// BB: true for touch device iPad, Android

	private static Boolean touchSupported;

	public static boolean isTouchSupported() {
		if (touchSupported == null) {
			boolean supported;
			/**
			 * In SwingJS, touch is taken to be the absence of a mouse, and mouse is known
			 * from detecting a mousemove. There is no way to know a priori if both mouse
			 * and touch are possible.
			 * 
			 * @j2sNative
			 * 
			 * 			supported = !J2S._haveMouse;
			 * 
			 */
			{
				supported = (System.getProperty("is_touch_supported") != null);
			}
			touchSupported = Boolean.valueOf(supported);
		}
		return touchSupported.booleanValue();
	}

	/**
	 * BB Compute the radius for human interaction around an atom or a bond. This
	 * radius should be larger for touch device than for pointer based system.
	 * 
	 * The drawback of a larger radius is that one has to be more precise when
	 * moving the structure.
	 * 
	 * @return a radius
	 */
	public static final int TOUCH_LIMIT = 50;

	final public static int fontSize = 13; // with a value != 12, the stringWidth errors are minimized in JS
	final public static int smallerFontSize = fontSize - 2; // OK for the Java VM

	public static final float atomMolecularDrawingAreaFontSize = fontSize; // BB

	public static final String defaultFontFamily = "Helvetica"; // looks good everywhere
	public final static Font copyRigthSmallTextFont = new Font(null, 0, 8);
	public final static Font menuCellFont = new Font(defaultFontFamily, Font.PLAIN, fontSize);

	/**
	 * for atom symbols
	 */
	public final static Font menuCellFontBold = new Font(defaultFontFamily, Font.BOLD, fontSize);

	public final static Font menuCellFontSmaller = new Font(defaultFontFamily, Font.PLAIN, smallerFontSize);

	public static FontMetrics menuCellFontMet;
	public static FontMetrics menuCellFontBoldMet;
	public static FontMetrics menuCellFontSmallerMet;

	public static final int maxFontSize = 100;

	public static final Font[] atomDrawingAreaFontCache = new Font[maxFontSize];
	public static final FontMetrics[] atomDrawingAreaFontMetCache = new FontMetrics[maxFontSize];

	public static final Font[] atomMapDrawingAreaFontCache = new Font[maxFontSize];
	public static final FontMetrics[] atomMapDrawingAreaFontMetCache = new FontMetrics[maxFontSize];

	public static final double rightBorderOldLook = 3; // older look needs more space because of it has a shadow
	public static final double rightBorderNewLook = 1.0;
	/**
	 * should be a multiple of 2 and 3, is set 0 in depict mode
	 */
	public static final double standardMenuCellSize = 24; // the original value

	private JME jme;

	private final Font atomDrawingAreaFont;
	private final FontMetrics atomDrawingAreaFontMet;

	public Font atomMapDrawingAreaFont;
	public FontMetrics atomMapDrawingAreaFontMet;

	public JPopupMenu functionalGroupPopumemu;
	public Point functionalGroupJPopupMenuPosition;
	public Point markerJPopupMenuPosition;
	public Point fixedCopyPasteJPopupMenuPosition;

	public boolean mustReDrawLeftMenu = true;
	public boolean mustReDrawTopMenu = true;
	public boolean mustReDrawMolecularArea = true;
	public boolean mustReDrawInfo = true;
	public boolean mustReDrawRightBorderImage = true;

	public double menuCellSize = standardMenuCellSize;

	public Icon dragAndDropIcon;
	public Icon fullScreenIcon;

	private int ioMargin;

	private double ioArrowWidth;

	static RingComparator ringComparator;

	JMEmol uniColorMolecule = null;

	// BB: the number of cells in the top menu - was ACTION_X
	public static final int TOP_ACTION_COUNT = Actions.ACTION_JME - 100; // assume that ACTION_JME is the last menu
																			// entry on
	// the top row

	public GUI(JME jme) {
		this.jme = jme;
		menuCellFontMet = jme.getFontMetrics(menuCellFont);
		menuCellFontBoldMet = jme.getFontMetrics(menuCellFontBold);
		menuCellFontSmallerMet = jme.getFontMetrics(menuCellFontSmaller);
		float realFs = atomMolecularDrawingAreaFontSize;
		int fs = Math.round(realFs);
		if (atomDrawingAreaFontCache[fs] == null) {
			atomDrawingAreaFontCache[fs] = new Font(defaultFontFamily, jme.options.boldAtomLabels ? Font.BOLD : Font.PLAIN, fs);
		}
		if (atomDrawingAreaFontMetCache[fs] == null) {
			atomDrawingAreaFontMetCache[fs] = jme.getFontMetrics(atomDrawingAreaFontCache[fs]);
		}

		atomDrawingAreaFont = atomDrawingAreaFontCache[fs];
		atomDrawingAreaFontMet = atomDrawingAreaFontMetCache[fs];

		fs = (int) Math.round(realFs * 0.8);
		if (atomMapDrawingAreaFontCache[fs] == null) {
			atomMapDrawingAreaFontCache[fs] = new Font(defaultFontFamily, Font.PLAIN, fs);
		}
		if (atomMapDrawingAreaFontMetCache[fs] == null) {
			atomMapDrawingAreaFontMetCache[fs] = jme.getFontMetrics(atomMapDrawingAreaFontCache[fs]);
		}

		atomMapDrawingAreaFont = atomMapDrawingAreaFontCache[fs];
		atomMapDrawingAreaFontMet = atomMapDrawingAreaFontMetCache[fs];

	}

	/**
	 * Provide the ideal height of a string consisting of usual upper case
	 * characters. Purpose: centering of String in the center of a box. Does not
	 * work for $ , y ; and others
	 */
	public static double stringHeight(FontMetrics fm) {
		return fm.getAscent() - fm.getDescent();
	}

	public static int getHumanInteractionTouchRadius() {
		// determined by trial and error on an iPad 4 by BB
		return TOUCH_LIMIT + (isTouchSupported() ? 300 : 120);
	}

	void createSquare(PreciseGraphicsAWT g, int xpos, int ypos) {
		int square = ypos * 100 + xpos;
		double xstart = (xpos - 1) * (menuCellSize + menuCellBorder());
		double ystart = (ypos - 1) * (menuCellSize + menuCellBorder());
		if (xpos == 1 && ypos > 2)
			ystart -= (2 * menuCellSize); // relative coordinates in leftMenu
		g.setColor(jme.bgColor);
		if (jme.options.newLook) {
			if (square == jme.action) {
				g.setColor(jme.bgColor.darker());
				// System.out.println("xstart=" + xstart + " ystart=" + ypos);
			}

			g.fillRect(xstart, ystart, menuCellSize, menuCellSize);
			g.setColor(Color.darkGray);
			g.drawRect(xstart, ystart, menuCellSize - 1, menuCellSize - 1);
		} else {
			if (square == jme.action)
				g.fill3DRect(xstart + 1, ystart + 1, menuCellSize, menuCellSize, false); // color override does not work
																							// - always grey?
			else
				g.fill3DRect(xstart, ystart, menuCellSize, menuCellSize, true);
		}

		if (!jme.isActionEnabled(square)) {
			return;
		}

		// draws icon or text in the square
		double marginFromCellBorder = menuCellSize / 4; // space between cell border and inside icon
		if (ypos >= 3) {
			// top menu squares
			int dan = jme.mapActionToAtomNumberXorR(square);
			if (dan != -1) {
				String label = Atom.zlabel[dan];
				Color atomSymbolColor = jme.leftMenuAtomColor == null ? JME.color[dan] : jme.leftMenuAtomColor;

				squareTextBold(g, xstart, ystart, atomSymbolColor, label);
			}
			return;
		}
		g.setColor(Color.black);
		switch (square) {
		case Actions.ACTION_SMI: // smiley face
			if (true) {
				g.setColor(Color.yellow);
				g.fillOval(xstart + 3, ystart + 3, menuCellSize - 6, menuCellSize - 6); // head
				g.setColor(Color.black);
			}
			g.drawOval(xstart + 3, ystart + 3, menuCellSize - 6, menuCellSize - 6); // head
			g.drawArc(xstart + 6, ystart + 6, menuCellSize - 12, menuCellSize - 12, -35, -110); // mouth
			// oci
			g.fillRect(xstart + 9, ystart + 9, 2, 4);
			g.fillRect(xstart + menuCellSize - 10, ystart + 9, 2, 4);
			// jazyk
			if (Math.random() < 0.04) {
				g.setColor(Color.red);
				g.fillRect(xstart + 10, ystart + 18, 4, 4);
			}
			// blink
			if (Math.random() > 0.96) {
				g.setColor(Color.yellow);
				g.fillRect(xstart + menuCellSize - 10, ystart + 8, 2, 3);
			}
			break;
		case Actions.ACTION_SPIRO:
			// drawing spiro button
			double xFarLeft = xstart + marginFromCellBorder;
			double xFarRight = xstart + menuCellSize - marginFromCellBorder;
			double xMiddle = xstart + menuCellSize / 2;
			// bottom left - black line
			g.drawLine(xFarLeft, ystart + menuCellSize - marginFromCellBorder, xMiddle, ystart + menuCellSize / 2);
			g.drawLine(xstart + menuCellSize / 2, ystart + menuCellSize / 2, xFarRight,
					ystart + menuCellSize - marginFromCellBorder);

			double y = ystart + menuCellSize - marginFromCellBorder;
			double dotLength = menuCellSize / GUI.standardMenuCellSize;
			g.drawLine(xMiddle - dotLength, y, xMiddle - 2 * dotLength, y);
			g.drawLine(xMiddle + dotLength, y, xMiddle + 2 * dotLength, y);

			g.setColor(Color.magenta);
			// top left - magenta line
			g.drawLine(xFarLeft, ystart + marginFromCellBorder, xMiddle, ystart + menuCellSize / 2);
			g.drawLine(xstart + menuCellSize / 2, ystart + menuCellSize / 2, xFarRight, ystart + marginFromCellBorder);

			y = ystart + marginFromCellBorder;
			g.drawLine(xMiddle - dotLength, y, xMiddle - 2 * dotLength, y);
			g.drawLine(xMiddle + dotLength, y, xMiddle + 2 * dotLength, y);

			// restore default color
			g.setColor(Color.black);
			break;

		case Actions.ACTION_QRY:
			g.setColor(Color.orange);
			g.fillRect(xstart + 4, ystart + 4, menuCellSize - 8, menuCellSize - 8); // head
			g.setColor(Color.black);
			g.drawRect(xstart + 4, ystart + 4, menuCellSize - 8, menuCellSize - 8); // head
			g.drawArc(xstart + 6, ystart + 6, menuCellSize - 11, menuCellSize - 12, -35, -110); // mouth
			g.fillRect(xstart + 9, ystart + 9, 2, 4);
			g.fillRect(xstart + menuCellSize - 10, ystart + 9, 2, 4);
			break;
		case Actions.ACTION_CHARGE:
			// squareText(g, xstart, ystart, "+ / ");
			// squareText(g, xstart, ystart, "+ / -");
			// squareTextBold(g, xstart, ystart, Color.black, "+ -");
			// g.drawLine(xstart + 15, ystart + 13, xstart + 19, ystart + 13); // better
			// -
			double padding = (double) menuCellSize / 4;
			// the / line
			g.drawLine(xstart + padding, ystart + menuCellSize - padding, xstart + menuCellSize - padding,
					ystart + padding);

			double symbolSize = (double) menuCellSize / 2 - padding;

			// the minus - bottom right
			double minusY = ystart + menuCellSize * 2 / 3;
			double minusStartX = xstart + menuCellSize / 2;
			double minusEndX = minusStartX + symbolSize;

			g.drawLine(minusStartX, minusY, minusEndX, minusY);

			// the plus horizontal line - top left
			double hY = ystart + menuCellSize * 1 / 3;
			double hEndX = minusStartX;
			double hStartX = minusStartX - symbolSize;
			g.drawLine(hStartX, hY, hEndX, hY);

			// the plus vertical line - top left
			double vX = (hStartX + hEndX) / 2;
			double vStartY = hY - symbolSize / 2;
			double vEndY = vStartY + symbolSize;
			g.drawLine(vX, vStartY, vX, vEndY);

			break;

		case Actions.ACTION_MOVE_AT:

			if (jme.options.showAtomMoveJButton) {
				// Draw a blue rectangle centered in the cell
				double reduction = (double) marginFromCellBorder / 2;
				double squareSize = menuCellSize - 2 * marginFromCellBorder - 2 * reduction;
				double brx = xstart + reduction + marginFromCellBorder; // blue rectangle x
				double bry = ystart + (brx - xstart);

				g.setColor(Color.BLUE);
				g.drawRect(brx, bry, squareSize, squareSize);

				g.setColor(Color.BLACK);

				// four triangles symbolising arrows
				double middleX = xstart + menuCellSize / 2;
				double middleY = ystart + menuCellSize / 2;

				double arrowMarginFromCellBorder = reduction; // not enough space for using a complete margin
				double arrowHeight = reduction;
				double arrowWidth = squareSize; // must be the same as squareSize for small cells, if different, the
												// code has to be adapted
				assert arrowHeight > 0;

				// top triangle arrow
				double xLeft = brx;
				double xRight = brx + arrowWidth;
				double yTop = ystart + arrowMarginFromCellBorder;
				// int yBottom = bry - reduction;
				double yBottom = yTop + arrowHeight;

				g.drawLine(xLeft, yBottom, middleX, yTop);
				g.drawLine(middleX, yTop, xRight, yBottom);

				// bottom triangle arrow
				// x coordinates are the same
				yBottom = bry + squareSize + reduction;
				yTop = yBottom + arrowHeight;
				g.drawLine(xLeft, yBottom, middleX, yTop);
				g.drawLine(middleX, yTop, xRight, yBottom);

				// Left triangle
				xLeft = xstart + reduction;
				xRight = xLeft + arrowHeight;
				yTop = bry;
				yBottom = yTop + arrowWidth;

				g.drawLine(xRight, yTop, xLeft, middleY);
				g.drawLine(xLeft, middleY, xRight, yBottom);

				// right triangle
				// y coordinates are the same
				xLeft = brx + squareSize + reduction;
				xRight = xLeft + arrowHeight;

				g.drawLine(xLeft, yTop, xRight, middleY);
				g.drawLine(xRight, middleY, xLeft, yBottom);
			}

			break;

		case Actions.ACTION_UNDO:
			// g.drawArc(xstart+6,ystart+6,sd-12,sd-12,270,270); // head
			// g.drawArc(xstart + 6, ystart + 7, menuCellSize - 12, menuCellSize - 14, 270,
			// 270); // head

			drawUndoOrRedoArrowMenuCell(g, xstart, ystart, menuCellSize, true);

			// squareText(g,xstart,ystart,"UDO");
			break;

		case Actions.ACTION_REDO:
			drawUndoOrRedoArrowMenuCell(g, xstart, ystart, menuCellSize, false);
			break;

		case Actions.ACTION_IO:
			drawInputOutputArrowsMenuCell(g, xstart, ystart, menuCellSize);
			fixedCopyPasteJPopupMenuPosition = new Point((int) xstart, (int) ystart);
			break;

		case Actions.ACTION_REACP:
			g.drawLine(xstart + marginFromCellBorder, ystart + menuCellSize / 2,
					xstart + menuCellSize - marginFromCellBorder, ystart + menuCellSize / 2);
			g.drawLine(xstart + menuCellSize - marginFromCellBorder, ystart + menuCellSize / 2,
					xstart + menuCellSize - marginFromCellBorder * 3 / 2,
					ystart + menuCellSize / 2 + marginFromCellBorder / 2);
			g.drawLine(xstart + menuCellSize - marginFromCellBorder, ystart + menuCellSize / 2,
					xstart + menuCellSize - marginFromCellBorder * 3 / 2,
					ystart + menuCellSize / 2 - marginFromCellBorder / 2);
			break;
		case Actions.ACTION_CLEAR:
			g.setColor(Color.white);
			g.fillRect(xstart + 3, ystart + 5, menuCellSize - 7, menuCellSize - 11);
			g.setColor(Color.black);
			g.drawRect(xstart + 3, ystart + 5, menuCellSize - 7, menuCellSize - 11);
			// squareText(g,xstart,ystart,"CLR");
			break;
		case Actions.ACTION_NEW:
			// special handling (aby boli 2 stvorce on)
			g.setColor(jme.bgColor);
			if (jme.newMolecule)
				g.fill3DRect(xstart + 1, ystart + 1, menuCellSize, menuCellSize, false);
			g.setColor(Color.black);
			squareText(g, xstart, ystart, "NEW");
			break;
		case Actions.ACTION_DELGROUP:
			// squareText(g,xstart,ystart,"D-R");
			g.setColor(Color.red);
			g.drawLine(xstart + 7, ystart + 7, xstart + menuCellSize - 7, ystart + menuCellSize - 7);
			// g.drawLine(xstart + 8, ystart + 7, xstart + menuCellSize - 6, ystart +
			// menuCellSize
			// - 7);
			g.drawLine(xstart + 7, ystart + menuCellSize - 7, xstart + menuCellSize - 7, ystart + 7);
			// g.drawLine(xstart + 8, ystart + menuCellSize - 7, xstart + menuCellSize - 6,
			// ystart + 7);
			g.setColor(Color.black);
			g.drawLine(xstart + marginFromCellBorder, ystart + menuCellSize / 2, xstart + 12,
					ystart + menuCellSize / 2);
			squareText(g, xstart + 6, ystart, "R");
			break;
		case Actions.ACTION_DELETE:
			// squareText(g,xstart,ystart,"DEL");
			g.setColor(Color.red);
			// g.drawLine(xstart+m,ystart+m,xstart+sd-m,ystart+sd-m);
			// g.drawLine(xstart+m+1,ystart+m,xstart+sd-m+1,ystart+sd-m);
			// g.drawLine(xstart+m,ystart+sd -m,xstart+sd-m,ystart+m);
			// g.drawLine(xstart+m+1,ystart+sd-m,xstart+sd-m+1,ystart+m);
			g.drawLine(xstart + 7, ystart + 7, xstart + menuCellSize - 7, ystart + menuCellSize - 7);
			// g.drawLine(xstart + 8, ystart + 7, xstart + menuCellSize - 6, ystart +
			// menuCellSize
			// - 7);
			g.drawLine(xstart + 7, ystart + menuCellSize - 7, xstart + menuCellSize - 7, ystart + 7);
			// g.drawLine(xstart + 8, ystart + menuCellSize - 7, xstart + menuCellSize - 6,
			// ystart + 7);
			g.setColor(Color.black);
			break;
		case Actions.ACTION_MARK: // handle both color marking and setting of atom map

			if (!jme.options.starNothing) {
				if (jme.params.mark) {

					// May 2015replaced by a circle

					// 6 is same as for smiley
					double pseudoRadius = 9; // was 6, PE wants it smaller
					Color color = jme.colorManager.getColor(jme.activeMarkerColorIndex);
					if (color != null) {
						g.setColor(color);
						g.fillOval(xstart + pseudoRadius / 2, ystart + pseudoRadius / 2, menuCellSize - pseudoRadius,
								menuCellSize - pseudoRadius);
						g.setColor(Color.black);
					} else {
						jme.showInfo("invalid color index:" + jme.activeMarkerColorIndex);
						assert (false);
					}

				} else {
					squareText(g, xstart, ystart, "123");
				}
				markerJPopupMenuPosition = new Point((int) xstart, (int) ystart);
			}
			break;
		case Actions.ACTION_JME:
			// squareText(g,xstart,ystart,"JME");
			// g.drawImage(infoImage,xstart+2,ystart+2,this);
			g.setColor(Color.blue);
			double coloredRectSize = menuCellSize - 8;
			double coloredRectSizeX = xstart + (menuCellSize - coloredRectSize) / 2;
			double coloredRectSizeY = ystart + (menuCellSize - coloredRectSize) / 2;
			g.fillRect(coloredRectSizeX, coloredRectSizeY, coloredRectSize, coloredRectSize);
			g.setColor(Color.black);
			// g.drawRect(coloredRectSizeX, coloredRectSizeY, coloredRectSize,
			// coloredRectSize);
			// squareTextBold(g, xstart + 1, ystart - 1, Color.white, "i");
			squareTextBold(g, xstart, ystart, Color.white, "i");
			break;
		case Actions.ACTION_STEREO:
			g.drawLine(xstart + marginFromCellBorder, ystart + menuCellSize / 2,
					xstart + menuCellSize - marginFromCellBorder, ystart + menuCellSize / 2 + 2);
			g.drawLine(xstart + marginFromCellBorder, ystart + menuCellSize / 2,
					xstart + menuCellSize - marginFromCellBorder, ystart + menuCellSize / 2 - 2);
			g.drawLine(xstart + menuCellSize - marginFromCellBorder, ystart + menuCellSize / 2 + 2,
					xstart + menuCellSize - marginFromCellBorder, ystart + menuCellSize / 2 - 2);
			break;
		case Actions.ACTION_BOND_SINGLE:
			g.drawLine(xstart + marginFromCellBorder, ystart + menuCellSize / 2,
					xstart + menuCellSize - marginFromCellBorder, ystart + menuCellSize / 2);
			break;
		case Actions.ACTION_BOND_DOUBLE:
			g.drawLine(xstart + marginFromCellBorder, ystart + menuCellSize / 2 - 2,
					xstart + menuCellSize - marginFromCellBorder, ystart + menuCellSize / 2 - 2);
			g.drawLine(xstart + marginFromCellBorder, ystart + menuCellSize / 2 + 2,
					xstart + menuCellSize - marginFromCellBorder, ystart + menuCellSize / 2 + 2);
			break;
		case Actions.ACTION_BOND_TRIPLE:
			g.drawLine(xstart + marginFromCellBorder, ystart + menuCellSize / 2,
					xstart + menuCellSize - marginFromCellBorder, ystart + menuCellSize / 2);
			g.drawLine(xstart + marginFromCellBorder, ystart + menuCellSize / 2 - 3,
					xstart + menuCellSize - marginFromCellBorder, ystart + menuCellSize / 2 - 3);
			g.drawLine(xstart + marginFromCellBorder, ystart + menuCellSize / 2 + 3,
					xstart + menuCellSize - marginFromCellBorder, ystart + menuCellSize / 2 + 3);
			break;
		case Actions.ACTION_CHAIN:
			g.drawLine(xstart + marginFromCellBorder / 2, ystart + marginFromCellBorder * 2 + marginFromCellBorder / 3,
					xstart + marginFromCellBorder / 2 * 3,
					ystart + marginFromCellBorder * 2 - marginFromCellBorder / 3);
			g.drawLine(xstart + marginFromCellBorder / 2 * 3,
					ystart + marginFromCellBorder * 2 - marginFromCellBorder / 3, xstart + marginFromCellBorder / 2 * 5,
					ystart + marginFromCellBorder * 2 + marginFromCellBorder / 3);
			g.drawLine(xstart + marginFromCellBorder / 2 * 5,
					ystart + marginFromCellBorder * 2 + marginFromCellBorder / 3, xstart + marginFromCellBorder / 2 * 7,
					ystart + marginFromCellBorder * 2 - marginFromCellBorder / 3);
			break;
		case Actions.ACTION_RING_3: // klesnute o 2
			drawRingIcon(g, xstart, ystart + 2, 3);
			break;
		case Actions.ACTION_RING_4:
			drawRingIcon(g, xstart, ystart, 4);
			break;
		case Actions.ACTION_RING_5:
			drawRingIcon(g, xstart, ystart, 5);
			break;
		case Actions.ACTION_RING_PH:
			drawRingIcon(g, xstart, ystart, 1);
			break;
		case Actions.ACTION_RING_6:
			drawRingIcon(g, xstart, ystart, 6);
			break;
		case Actions.ACTION_RING_7:
			drawRingIcon(g, xstart, ystart, 7);
			break;
		case Actions.ACTION_RING_8:
			drawRingIcon(g, xstart, ystart, 8);
			break;

		case Actions.ACTION_FG:
			if (jme.options.fgMenuOption) {
				squareText(g, xstart, ystart, "FG");
				functionalGroupJPopupMenuPosition = new Point((int) xstart, (int) ystart);
			}
			break;

		}
	}

	private static void drawUndoOrRedoArrowMenuCell(PreciseGraphicsAWT g, double xstart, double ystart, double cellSize,
			boolean undo) {
		double arrowWidth = ((double) cellSize / 4.0); // 6
		double arrowHeight = arrowWidth;
		double margin = 2;
		ystart -= 1; // Nov 2016: better vertical centering

		// X values: we work with relative values because the absolute values will be
		// different for the Undo and Redo arrows
		double xStartArrowLine = margin;
		double xArrowTip = xStartArrowLine + arrowWidth / 2.0;
		double xEndArrowLine = xStartArrowLine + arrowWidth;

		// All Y values are absolute
		double yStartArrowLine = ystart + (10.0 * cellSize / 24.0);
		double yArrowTip = yStartArrowLine + arrowHeight;

		double xEnd = xstart + cellSize;

		double absoluteXArrowTip = 0;
		double absoluteXstartArrowLine = 0;
		double absoluteXEndArrowLine = 0;
		if (undo) {
			absoluteXstartArrowLine = xStartArrowLine + xstart;
			absoluteXArrowTip = xArrowTip + xstart;
			absoluteXEndArrowLine = xEndArrowLine + xstart;
		} else {
			// mirror image
			// Y values stay the same
			absoluteXArrowTip = xEnd - xArrowTip;
			absoluteXstartArrowLine = xEnd - xStartArrowLine;
			absoluteXEndArrowLine = xEnd - xEndArrowLine;
		}

		// draw the two lines of the arrow tip - two sides of the triangle
		g.drawLine(absoluteXstartArrowLine, yStartArrowLine, absoluteXArrowTip, yArrowTip);
		g.drawLine(absoluteXEndArrowLine, yStartArrowLine, absoluteXArrowTip, yArrowTip);

		// draw middle line of the arrow , but not as extended as the triangle lines -
		// it looks nicer so
		double yArrowCenterCorrection = arrowHeight / 3 - 0.5; // without the -0.5 , there is a gap visible when zooming
																// in

		g.drawLine(absoluteXArrowTip, yStartArrowLine + yArrowCenterCorrection, absoluteXArrowTip, yArrowTip);

		// The arc

		// define the coordinates and sizes of the bounding box that will contain the
		// arc
		double xStartArcBoxTopLeft = xArrowTip;
		double yStartArcBoxTopLeft = ystart + xStartArcBoxTopLeft;
		double arcBoxWidth = cellSize - xStartArcBoxTopLeft - 2 * margin;
		double arcBoxHeight = cellSize - 2 * margin;

		// The arc has to be moved slightly up in order to align with the start of the
		// line of the center of the arrow (see above)
		yStartArcBoxTopLeft -= yArrowCenterCorrection;
		arcBoxHeight -= yArrowCenterCorrection;

		arcBoxHeight -= 1; // looks better when the cell size is small

		double arcSpan = 270;
		double startAngle = 0;
		double absoluteXxtartArcBoxTopLeft = 0;
		if (undo) {
			absoluteXxtartArcBoxTopLeft = xstart + xStartArcBoxTopLeft;
			startAngle = 270;
		} else {
			absoluteXxtartArcBoxTopLeft = xEnd - arcBoxWidth - xStartArcBoxTopLeft;
			arcSpan *= -1;
			startAngle = -90;
		}
		// draw an arc inside the box
		g.drawArc(absoluteXxtartArcBoxTopLeft, yStartArcBoxTopLeft, arcBoxWidth, arcBoxHeight, startAngle, arcSpan);
	}

	// To ease debugging this method, the menuCellSize can be set to a higher value
	// , e.g 120
	/**
	 * draw two vertical blue arrows to symbolize output / input, arrows are
	 * simplified as triangles
	 * 
	 * @param g
	 * @param xstart
	 * @param ystart
	 * @param cellSize
	 * @param undo
	 */
	void drawInputOutputArrowsMenuCell(PreciseGraphicsAWT g, double xstart, double ystart, double cellSize) {

		// the IO triangle/arrows

		// arrows are simplified as triangle
		double arrowWidth = ioArrowWidth;
		double arrowHeight = arrowWidth;
		double margin = ioMargin;
		double xStartArrowLine = margin + xstart;
		double xArrowTip = xStartArrowLine + arrowWidth / 2.0;
		double xEndArrowLine = xStartArrowLine + arrowWidth;

		double yStartArrowLine = ystart + margin;
		double yArrowTip = yStartArrowLine + arrowHeight;

		g.setColor(Color.BLUE);

		g.fillPolygon(new double[] { xStartArrowLine, xArrowTip, xEndArrowLine },
				new double[] { yStartArrowLine, yArrowTip, yStartArrowLine }, 3);

		/* 2nd triangle */
		xStartArrowLine = xArrowTip;
		xArrowTip = xStartArrowLine + arrowWidth / 2.0;
		xEndArrowLine = xStartArrowLine + arrowWidth;

		yArrowTip = yStartArrowLine + arrowHeight / 2.0;
		yStartArrowLine = yArrowTip + arrowHeight;

		g.fillPolygon(new double[] { xStartArrowLine, xArrowTip, xEndArrowLine },
				new double[] { yStartArrowLine, yArrowTip, yStartArrowLine }, 3);

	}

	/**
	 * Draw the drag and drop symbol at the bottom right of the JSME container
	 * 
	 * @param g                       : either the infor bar or the molecular area
	 * @param graphicsContainerWidth
	 * @param graphicsContainerHeight
	 */
	public void drawDragAndDropIcon(PreciseGraphicsAWT g, double iconScale) {
		// arrows are simplified as triangle

		double graphicsContainerWidth = g.getWidth();
		double graphicsContainerHeight = g.getHeight();

		// Use the same size as I/O arrows

		double margin = ioMargin * iconScale;
		double arrowWidth = ioArrowWidth * iconScale;
		double arrowHeight = arrowWidth;

		if (dragAndDropIcon == null)
			dragAndDropIcon = new Icon(g);
		else
			dragAndDropIcon.pg = g;

		if (jme.isDepict()) {
			margin = 0; // put the arrow at the extreme left and bottom without any margin
		}

		double xStartArrowLine = graphicsContainerWidth - margin - arrowWidth;
		double xArrowTip = xStartArrowLine + arrowWidth;

		double yArrowBottom;

		double yArrowTop;
		double yArrowMiddle;

		if (!jme.isDepict()) { // non depict mode: the arrow is Y centered in the middle of the info bar
			yArrowMiddle = graphicsContainerHeight / 2;
			yArrowBottom = yArrowMiddle + arrowHeight / 2;
			yArrowTop = yArrowMiddle - arrowHeight / 2;
		} else { // arrow is placed at the bottom right
			yArrowBottom = graphicsContainerHeight;

			yArrowTop = yArrowBottom - arrowHeight;
			yArrowMiddle = (yArrowTop + yArrowBottom) / 2;
		}

		g.setColor(Color.BLUE);

		g.fillPolygon(new double[] { xStartArrowLine, xArrowTip, xStartArrowLine },
				new double[] { yArrowTop, yArrowMiddle, yArrowBottom }, 3);

		dragAndDropIcon.setRect(xStartArrowLine, yArrowTop, arrowWidth, arrowHeight);

	}

	void drawRightBorderImage(Graphics g) {
		if (!mustReDrawRightBorderImage)
			return;

		Rectangle2D.Double screenArea = new Rectangle2D.Double(jme.dimension.width - jme.rightBorder(),
				jme.topMenuHeight(), jme.rightBorder(), jme.molecularArea.height);
		PreciseGraphicsAWT og = GUI.getScaledGraphicsOfPreciseImage(jme.rightBorderImage, jme.menuScale, screenArea);

		double imgWidth = jme.rightBorder(1);
		// double imgHeight = (double)jme.molecularAreaHeight/jme.depictScale;
		double imgHeight = screenArea.height / jme.menuScale;
		if (jme.options.newLook) {
			og.setColor(Color.darkGray);
			// og.drawLine(imgWidth - 1, 0, imgWidth - 1, imgHeight - 1);//right line
			// og.drawLine(0, 0, 0, imgHeight);//right line
			og.fillRect(0, 0, imgWidth, imgHeight);
			// og.fillRect(0, 0, imgWidth, imgHeight/2);
		} else {
			// vonkajsi okraj na pravej strane
			og.setColor(jme.bgColor.darker());
			og.drawLine(imgWidth - 1, 0, imgWidth - 1, imgHeight);
			// predel vo farbe backgroundu
			og.setColor(jme.bgColor);
			og.drawLine(imgWidth - 2, 0, imgWidth - 2, imgHeight);
			// svetly okraj dovnutra
			og.setColor(jme.brightColor);
			og.drawLine(imgWidth - 3, 0, imgWidth - 3, imgHeight);
		}

		g.drawImage(jme.rightBorderImage.getImage(), (int) screenArea.x, (int) screenArea.y, jme);

	}

	// ----------------------------------------------------------------------------
	public void drawTopMenu(Graphics g) {

//Swing will handle this differently
//		if (!jme.mustReDrawTopMenu)
//			return;

		// BH 2023 topMenuImage can be null even in Java if this is happening on the
		// main thread.
		if (jme.topMenuImage == null)
			return;

		int action = jme.action;

		Rectangle2D.Double screenArea = new Rectangle2D.Double(0, 0, jme.dimension.width, jme.topMenuHeight());
		PreciseGraphicsAWT og = GUI.getScaledGraphicsOfPreciseImage(jme.topMenuImage, jme.menuScale, screenArea);

		double imgWidth = jme.dimension.width / jme.menuScale;
		double imgHeight = jme.topMenuHeight(1.0);
		og.setColor(jme.bgColor);
		og.fillRect(0, 0, imgWidth, imgHeight);

		if (jme.options.newLook) {
			// og.setColor(Color.darkGray);
			og.setColor(jme.bgColor.darker());
			double s = (menuCellSize + menuCellBorder()) * GUI.TOP_ACTION_COUNT;
			// og.drawRect(s,0,imgWidth-s-1,menuCellSize*2 + menuCellBorder()-1);
			og.drawRect(s, 0, imgWidth - s - 1, imgHeight - 1);
		} else {
			og.setColor(jme.bgColor.darker());
			og.drawLine(imgWidth - 1, 0, imgWidth - 1, imgHeight - 1); // right
			og.drawLine(0, imgHeight - 1, imgWidth - 1, imgHeight - 1); // bottom

			og.setColor(jme.brightColor);
			og.drawLine(0, 0, imgWidth - 1, 0); // top
		}

		// og.drawLine(TOP_MENU_NUMBER_OF_CELLS * menuCellSize, 0,
		// TOP_MENU_NUMBER_OF_CELLS * menuCellSize, imgHeight - 1); // predel

		// BB: redraw the FG menu cell if a substituent had been selected
		int savedAction = action;
		if (Actions.ACTION_GROUP_MIN <= action && action <= Actions.ACTION_GROUP_MAX) {
			action = Actions.ACTION_FG;
		}
		for (int i = 1; i <= GUI.TOP_ACTION_COUNT; i++) {
			createSquare(og, i, 1); // icon cell
			createSquare(og, i, 2);
		}

		// restore the action value in case it had been changed for redrawing above
		action = savedAction;

		g.drawImage(jme.topMenuImage.getImage(), 0, 0, jme);
	}

	// ----------------------------------------------------------------------------
	void drawLeftMenu(Graphics g) {
		// Swing will handle this differently
//		if (!jme.mustReDrawLeftMenu)
//			return;

		Rectangle2D.Double screenArea = new Rectangle2D.Double(0, jme.topMenuHeight(), jme.leftMenuWidth(),
				jme.dimension.height - jme.topMenuHeight());
		PreciseGraphicsAWT og = getScaledGraphicsOfPreciseImage(jme.leftMenuImage, jme.menuScale, screenArea);
		double imgWidth = jme.leftMenuWidth(1);
		double imgHeight = (jme.dimension.height - jme.topMenuHeight()) / jme.menuScale;
		og.setColor(jme.bgColor);
		og.fillRect(0, 0, imgWidth, imgHeight);
		double yInfoArea = imgHeight - jme.infoAreaHeight(1);
		int leftMenuCellCount = jme.getLeftMenuCellCount();
		if (jme.options.newLook) {
			og.setColor(Color.darkGray);
			double y = leftMenuCellCount * (menuCellSize + menuCellBorder()) + 3;
			if (yInfoArea > y) {
				og.drawLine(0, y, menuCellSize - 1, y); // top horizontal line
				og.drawLine(0, y, 0, imgHeight - 1); // left

				og.drawLine(menuCellSize - 1, y, menuCellSize - 1, yInfoArea); // right down to info area
				og.drawLine(menuCellSize - 1, yInfoArea, imgWidth, yInfoArea); // small horizontal line to info area
			}
			// frame at the bottom
			og.drawLine(0, imgHeight - 1, imgWidth, imgHeight - 1); // bottom

		} else {
			og.setColor(jme.brightColor);
			og.drawLine(0, 0, 0, imgHeight - 1); // left
			og.drawLine(0, leftMenuCellCount * menuCellSize, imgHeight - 1, leftMenuCellCount * menuCellSize); // predel

			og.setColor(jme.bgColor.darker());
			// og.drawLine(imgWidth - 1, 0, imgWidth - 1, imgHeight - 1 - menuCellSize); //
			// right down to info area
			og.drawLine(imgWidth - 1, 0, imgWidth - 1, yInfoArea + 1); // right down to info area
			og.drawLine(0, imgHeight - 1, imgWidth - 0, imgHeight - 1); // bottom
		}

		// the actions for the left menu matche the squar numbers
		for (int i = 3; i <= leftMenuCellCount + 2; i++) {
			createSquare(og, 1, i);
		}

		g.drawImage(jme.leftMenuImage.getImage(), (int) screenArea.x, (int) screenArea.y, jme);
	}

	// ----------------------------------------------------------------------------
	protected void drawInfo(Graphics g) {
		// BH 2023.01.18 Swing will handle this differently
//		if (!jme.mustReDrawInfo)
//			return;
		String text = (jme.infoText == null ? "" : jme.infoText);

		int textYPosition = 15;
		// screen position of the info bar at the bottom of the applet
		Rectangle2D.Double screenArea = new Rectangle2D.Double(jme.leftMenuWidth(),
				jme.dimension.height - jme.infoAreaHeight(), jme.dimension.width - jme.leftMenuWidth(),
				jme.infoAreaHeight());
		PreciseGraphicsAWT og = getScaledGraphicsOfPreciseImage(jme.infoAreaImage, jme.menuScale, screenArea);

		double imgWidth = screenArea.width / jme.menuScale; // the width is reduced if scale > 1
		double imgHeight = jme.infoAreaHeight(1); // unscaled because og is scaled
		og.setColor(jme.bgColor);
		og.fillRect(0, 0, imgWidth, imgHeight);

		if (jme.options.newLook) {
			og.setColor(Color.darkGray);
			og.drawRect(-10, 0, imgWidth - 1 + 10, imgHeight - 1); // -10: used for masking the left border
		} else {
			og.setColor(jme.brightColor);
			// og.setColor(Color.red);
			// og.drawLine(0, 0, imgWidth - 1 - 2, 0); // top
			og.drawLine(0, 0, imgWidth - jme.rightBorder(1) + 1, 0); // top
			og.setColor(jme.bgColor.darker());
			og.drawLine(0, imgHeight - 1, imgWidth - 1, imgHeight - 1); // bottom
			og.drawLine(imgWidth - 1, 0, imgWidth - 1, imgHeight - 1); // right
		}
		og.setFont(GUI.menuCellFontSmaller);
		og.setColor(Color.black);

		if (text.toLowerCase().contains("error"))
			og.setColor(Color.red);
		og.drawString(text, 10, textYPosition);

		if (!jme.isDepict()) { /*
								 * in depict mode, another graphics must be used because the info bar is not
								 * present
								 */
			// TODO: the java implemenrtation does not support drag and drop
			drawDragAndDropIcon(og, 1.0);
			if (jme.options.fullScreenIconOption && JME.isFullScreenSupported())
				drawFullScreenIcon(og, 1.0, dragAndDropIcon);
			else
				fullScreenIcon = null;
		}

		if (imgWidth > 100 && jme.doDrawChiralText()) {
			String chiralText = "Chiral";
			og.setColor(Color.black);
			og.drawString(chiralText, imgWidth - 100, textYPosition);
		}

		g.drawImage(jme.infoAreaImage.getImage(), (int) screenArea.x, (int) screenArea.y, jme);

	}

	public void draw(Graphics g2d) {
		ioMargin = 3;
		ioArrowWidth = (menuCellSize - 2 * ioMargin) / 1.5;
		drawInfo(g2d);
		drawTopMenu(g2d);
		drawLeftMenu(g2d);
		drawRightBorderImage(g2d);
	}

	// --------------------------------------------------------------------------
	void squareText(PreciseGraphicsAWT g, double xstart, double ystart, String text) {

		// Smaller font is needed to display NEW and 123

		// g.setFont(menuCellFontSmaller);
		// int hSmall = menuCellFontSmallerMet.getBoxUppercaseHeight(); // vyska fontu
		// int w = menuCellFontSmallerMet.stringWidth(text);

		FontMetrics fm = GUI.menuCellFontMet;
		int w = fm.stringWidth(text);

		// Smaller font is needed to display NEW and 123
		// If the text is too wide for the cell, then use a smaller font
		if (w >= menuCellSize - 1) {
			int size = fm.getFont().getSize();
			// TODO: font cache does not work here
			// decrease font size until the text fits in the cell
			while (w >= menuCellSize - 1 && size > 1) {
				size--;
				Font smallerFont = new Font(fm.getFont().getName(), fm.getFont().getStyle(), size);
				fm = jme.getFontMetrics(smallerFont);
				w = fm.stringWidth(text);
				g.setFont(smallerFont);

			}
		} else {
			g.setFont(GUI.menuCellFont);
		}
		double h = GUI.stringHeight(fm); // vyska fontu

		g.drawString(text, xstart + (menuCellSize - w) / 2, ystart + (menuCellSize - h) / 2 + h);

	}

	// --------------------------------------------------------------------------
	void squareTextBold(PreciseGraphicsAWT g, double xstart, double ystart, Color col, String text) {
		// Used for the atom symbols on the left side menu
		double h = GUI.stringHeight(GUI.menuCellFontBoldMet); // vyska fontu
		double w = GUI.menuCellFontBoldMet.stringWidth(text);
		g.setFont(GUI.menuCellFontBold);
		g.setColor(col);
//		if (bwMode)
//			g.setColor(Color.black);
		g.drawString(text, xstart + (menuCellSize - w) / 2, ystart + (menuCellSize - h) / 2 + h);
		// poor man's BOLD
		// g.drawString(text,xstart+(sd-w)/2+1,ystart+(sd-h)/2+h);
	}

	// --------------------------------------------------------------------------
	void drawRingIcon(PreciseGraphicsAWT g, double xstart, double d, int n) {
		double m = menuCellSize / 4; // margin
		boolean ph = false;
		double xp[] = new double[9];
		double yp[] = new double[9]; // polygon coordinates
		double xcenter = xstart + menuCellSize / 2;
		double ycenter = d + menuCellSize / 2;
		double rc = menuCellSize / 2 - m / 2;
		if (n == 1) {
			n = 6;
			ph = true;
		}
		for (int i = 0; i <= n; i++) {
			double uhol = Math.PI * 2. / n * (i - .5);
			xp[i] = xcenter + rc * Math.sin(uhol);
			yp[i] = ycenter + rc * Math.cos(uhol);
		}
		g.drawPolygon(xp, yp, n + 1);
		if (ph) { // double bonds in Ph icon
			for (int i = 0; i <= n; i++) {
				double uhol = Math.PI * 2. / n * (i - .5);
				xp[i] = xcenter + (rc - 3) * Math.sin(uhol);
				yp[i] = ycenter + (rc - 3) * Math.cos(uhol);
			}
			g.drawLine(xp[0], yp[0], xp[1], yp[1]);
			g.drawLine(xp[2], yp[2], xp[3], yp[3]);
			g.drawLine(xp[4], yp[4], xp[5], yp[5]);
		}
	}

	/**
	 * Draw the icon for toggling between normal and fullscreen
	 * 
	 * @param g
	 * @param graphicsContainerWidth
	 * @param graphicsContainerHeight
	 * @param iconScale
	 * @param expand
	 */
	void drawFullScreenIcon(PreciseGraphicsAWT g, double iconScale, Icon rightIcon) {

		boolean expand = !jme.isFullScreen();

		// the icon is needed for event handling
		if (this.fullScreenIcon == null) {
			this.fullScreenIcon = new Icon(g);
		} else {
			this.fullScreenIcon.pg = g;
		}

		double margin = ioMargin * iconScale;
		double iconHeight = ioArrowWidth * iconScale; // same height as the DnD icon

		// the icon will placed at the bottom right
		double rightX = g.getWidth();
		double graphicsContainerHeight = g.getHeight();
		if (rightIcon != null) {
			rightX = rightIcon.x;// put the icon to left side of the DnD icon if present
			iconHeight = rightIcon.height; // same height as the DnD icon
			// move further to the left of the DnD icon
			rightX -= 2 * margin;
		}

		// icon will look like a monitor display with 16:9 ratio
		double rectangleWidth = iconHeight * 16 / 9;

		double startSize = 1.0;
		double endSize = 0.3;
		double startColor = 0;
		double endColor = 1.0;
		int steps = 20;
		boolean firstLoop = true;

		// create a gradient blue to white (or vice versa) by stacking rectangles of
		// decreasing sizes
		for (double relativeSize = startSize; relativeSize >= endSize; relativeSize -= (startSize - endSize) / steps) {
			float c = (float) ((startSize - relativeSize) * (endColor - startColor) / (startSize - endSize));

			// reverse the icon color scheme when the applet is in full screen mode
			if (!expand) {
				c = (float) endColor - c;
			}
			Color color = new Color(c, c, 1.0f);
			g.setColor(color);

			double h = iconHeight * relativeSize;
			double w = h / iconHeight * rectangleWidth;
			double x = rightX - rectangleWidth + (rectangleWidth - w) / 2;
			double y;
			if (!jme.isDepict()) { // non depict mode: the icon is Y centered in the middle of the info bar
				y = graphicsContainerHeight / 2 - h / 2;
			} else {
				y = graphicsContainerHeight - iconHeight / 2 - h / 2;
			}
			g.fillRect(x, y, w, h);

			if (firstLoop) {
				// save the position of the icon on the graphics - will be used for event
				// handling
				firstLoop = false;
				fullScreenIcon.setRect(x, y, w, h); // for event handling
			}

		}

	}

	/**
	 * the menu cell border differs in new and old look
	 * 
	 * @return
	 */
	public int menuCellBorder() {
		return (jme.options.newLook ? 1 : 0);
	}

	public JPopupMenu getFunctionalGroupPopumemu() {
		if (functionalGroupPopumemu == null) {
			functionalGroupPopumemu = createFunctionalGroupPopumemu();
		}
		return functionalGroupPopumemu;
	}

	public JPopupMenu createFunctionalGroupPopumemu() {

		JPopupMenu popup = new JPopupMenu();

		for (String eachFG : jme.functionalGroups) {
			JMenuItem mi = new JMenuItem(eachFG);
			popup.add(mi);
			mi.setActionCommand(eachFG);
			mi.addActionListener(jme);
		}
		jme.add(popup);
		return popup;
	}

	public JPopupMenu createFBackgroundColorPopumemu() {

		JPopupMenu popup = new JPopupMenu();

		for (int i = 1; i < jme.colorManager.psColor.length; i++) {
			ColorInfo ci = jme.colorManager.getColorInfo(i);
			Color color = ci.color;
			String label = ci.name;
			String colorrHash = ci.hash;
			;
			JMenuItem mi = new JMenuItem(label + "\t" + ColorManager.makeHexColor(color));
			// JSapplet awt
			// JMenuItem will create
			// SVG with color circle
			popup.add(mi);
			mi.setActionCommand(colorrHash);
			mi.addActionListener(jme);

		}

		jme.add(popup);
		return popup;
	}

	public boolean mustRedrawNSomething() {
		return mustReDrawLeftMenu || mustReDrawTopMenu || mustReDrawMolecularArea || mustReDrawInfo
				|| mustReDrawRightBorderImage;

	}

	public static PreciseGraphicsAWT getScaledGraphicsOfPreciseImage(PreciseImage pi, double scale,
			Rectangle2D.Double screenArea) {
		PreciseGraphicsAWT og = pi.getGraphics(JME.scalingIsPerformedByGraphicsEngine ? scale : 1);
		og.setDrawOnScreenCoordinates(screenArea);
		return og;
	}

	public static class RingComparator implements Comparator<Ring> {

		public int phase = 0;
		
		@Override
		public int compare(Ring a, Ring b) {
			// 1) aromatic over not aromatic, any size
			if (a.isAromatic != b.isAromatic) {
				return (a.isAromatic ? -1 : 1);
			}
			// 2) more bonds wins
			if (a.bondCount != b.bondCount) {
				return (a.bondCount > b.bondCount ? -1 : 1);
			}
			// 3) larger ring wins
			if (a.size != b.size) {
				return (a.size > b.size ? -1 : 1);
			}
			// 4) hetero loses
			if (a.isHetero != b.isHetero) {
				return (a.isHetero ? 1 : -1);
			}
			return 0;
		}

	}

	public static class Ring {
		public BitSet bsBonds = new BitSet();
		public BitSet bsAtoms = new BitSet();
		public boolean isAromatic;
		public boolean isHetero;
		public int bondCount;
		public int size;
		public double cx;
		public double cy;
	}

	public static class RingInfo {
		// all bitsets are 1-based, as in JME
		final public BitSet bsAromaticRings = new BitSet();
		final public BitSet bsAromaticBonds = new BitSet();
		final public List<Ring> rings = new ArrayList<>();
		final public BitSet bsRingBonds = new BitSet();
		final public BitSet bsRingAtoms = new BitSet();
		final public BitSet bsAromaticAtoms = new BitSet();

		public RingInfo(JMECore mol) {
			BitSet bsDouble = new BitSet();
			for (int i = 1; i <= mol.nbonds; i++) {
				Bond b = mol.bonds[i];
				if (b.bondType == Bond.DOUBLE) {
					bsDouble.set(i);
				}
				b.guideX = Double.NaN;
			}
			JME.getParser().getRingInfo(this, mol);
			if (rings.size() > 0) {
				// delete duplicate bonds and set guide points
				for (int i = 0, n = rings.size(); i < n; i++) {
					Ring r = rings.get(i);
					r.bsBonds.and(bsDouble);
					r.bondCount = r.bsBonds.cardinality();
				}

				// sort these, primarily to separate different kinds of
				// nonaromatic rings.

				if (ringComparator == null)
					ringComparator = new RingComparator();
				rings.sort(ringComparator);

				BitSet bsBonds = new BitSet();
				BitSet bsToDo = new BitSet();
				bsToDo.set(0, rings.size());
				removeDuplicates(bsToDo, 3, bsBonds, true);
				removeDuplicates(bsToDo, 2, bsBonds, true);
				removeDuplicates(bsToDo, 1, bsBonds, true);
				removeDuplicates(bsToDo, 3, bsBonds, false);
				removeDuplicates(bsToDo, 2, bsBonds, false);
				removeDuplicates(bsToDo, 1, bsBonds, false);
				for (int i = 0, n = rings.size(); i < n; i++) {
					Ring r = rings.get(i);
					r.bondCount = r.bsBonds.cardinality();
				}

				for (int i = 0, n = rings.size(); i < n; i++) {
					Ring r = rings.get(i);
					// System.out.println(i + " " + r.isAromatic + " " + r.bsAtoms + " " +
					// r.bondCount);
					double cx = 0;
					double cy = 0;
					for (int j = r.bsAtoms.nextSetBit(0); j >= 0; j = r.bsAtoms.nextSetBit(j + 1)) {
						cx += mol.atoms[j].x;
						cy += mol.atoms[j].y;
					}
					cx /= r.size;
					cy /= r.size;
					r.cx = cx;
					r.cy = cy;
					for (int j = r.bsBonds.nextSetBit(0); j >= 0; j = r.bsBonds.nextSetBit(j + 1)) {
						mol.bonds[j].guideX = cx;
						mol.bonds[j].guideY = cy;
					}
				}
			}
			
			// a nice improvement on this would be to check for conjugation, 
			// weighting the assignment based on that. This only affects
			// fused ring systems with enone enes for the fusion.
			
			// set non-ring bond guides if we can,
			// based on substituent directions
			// also remove EZ stereochemistry from small-ring double bonds
			
			for (int i = 1; i <= mol.nbonds; i++) {
				Bond b = mol.bonds[i];
				int type = b.bondType;
				if 	(bsRingBonds.get(i)) {
					b.smallRing = true;
					if (type == Bond.TRIPLE)
						b.bondType = Bond.SINGLE;
				} else {
					b.smallRing = false;
				}
				if (type == Bond.DOUBLE) {

					
					if (!Double.isNaN(b.guideX)) {
						continue; // already done
					}
					Atom a1 = mol.atoms[b.va];
					Atom a2 = mol.atoms[b.vb];
					// check for 1,1,2,2
					if (a1.nv == 3 && a2.nv == 3) {
						continue; // just set by direction
					}
					if (a1.an != Atom.AN_C || a2.an != Atom.AN_C) {
						continue; // just set by direction
					}
					// check for 1,1 or 2,2
					if (a1.nv == 3 && a2.nv == 1 || a1.nv == 1 && a2.nv == 3) {
						b.guideY = Double.NaN;
					}
					int ia1s1 = mol.getSp2Other(b.va, b.vb, true);
					int ia2s1 = mol.getSp2Other(b.vb, b.va, true);
					int ia1s2 = (a1.nv == 2 ? 0 : mol.getSp2Other(b.va, b.vb, false));
					int ia2s2 = (a2.nv == 2 ? 0 : mol.getSp2Other(b.vb, b.va, false));
					if (ia1s1 == 0 || ia2s1 == 0) {
						// Just H atoms ?
						continue;
					}
					// just the average of substituent directions. Very simple!
					int n1 = (ia1s2 == 0 ? 1 : 2);
					int n2 = (ia2s2 == 0 ? 1 : 2);
					double gx = mol.atoms[ia1s1].x + mol.atoms[ia2s1].x + (n1 == 2 ? mol.atoms[ia1s2].x : 0)
							+ (n2 == 2 ? mol.atoms[ia2s2].x : 0);
					double gy = mol.atoms[ia1s1].y + mol.atoms[ia2s1].y + (n1 == 2 ? mol.atoms[ia1s2].y : 0)
							+ (n2 == 2 ? mol.atoms[ia2s2].y : 0);
					b.guideX = gx / (n1 + n2);
					b.guideY = gy / (n1 + n2);
				}
			}
		}

		/**
		 * Starting with a set of rings indicating all double bonds, 
		 * selectively pull out rings of different sizes, "claiming" the
		 * double bonds for that ring so that we can (later) set their 
		 * guide point to the side of the bond within this ring. 
		 *  
		 * @param bsToDo
		 * @param nBonds
		 * @param bsBonds
		 * @param checkIntersect
		 */
		private void removeDuplicates(BitSet bsToDo, int nBonds, BitSet bsBonds, boolean checkIntersect) {
			for (int i = bsToDo.nextSetBit(0); i >= 0; i = bsToDo.nextSetBit(i + 1)) {
				Ring r = rings.get(i);
				if (r.bondCount < nBonds)
					continue;
				if (checkIntersect && r.bsBonds.intersects(bsBonds))
					continue;
				bsToDo.clear(i);
//				System.out.println(i + " " + r.bsBonds + " " + bsBonds);
				r.bsBonds.andNot(bsBonds);
				r.bondCount = r.bsBonds.cardinality();
				bsBonds.or(r.bsBonds);
			}
		}
	}

	public Font getAtomDrawingFont() {
		return atomDrawingAreaFont;
	}

	public FontMetrics getAtomDrawingFontMetrics() {
		return atomDrawingAreaFontMet;
	}

	/**
	 * BB
	 * 
	 * Determine button-action matches the mouse coordinates.
	 * 
	 * Counting off cells in x and y coordinates, returning a row/column key.
	 * 
	 * @param x
	 * @param y
	 * @return  100 * row + column or 0 for none found
	 */
	public int determineMenuAction(double x, double y, boolean ignoreDisabledActions) {
		int action = 0;

		// convert the x,y event coordinate to the menu scale
		x = (int) Math.round(x / jme.menuScale);
		y = (int) Math.round(y / jme.menuScale);

		if (x < jme.leftMenuWidth(1.0) || y < jme.topMenuHeight(1.0)) { // --- inside the menu area

			int xbutton = 0;
			for (int i = 1; i <= GUI.TOP_ACTION_COUNT; i++)
				if (x < i * (menuCellSize + menuCellBorder())) {
					xbutton = i;
					break;
				}
			int ybutton = 0;
			int n = jme.getLeftMenuCellCount();
			double h = menuCellSize + menuCellBorder();
			for (int i = 1; i <= n + 2; i++) {
				if (y < i * h) {
					ybutton = i;
					break;
				}
			}
			if (xbutton > 0 && ybutton > 0) {
				action = ybutton * 100 + xbutton;
			}
		}

		// TODO: filter out all actions that are disabled
		if (ignoreDisabledActions) {
			switch (action) {
			case Actions.ACTION_REACP:
				if (!jme.options.reaction)
					action = 0;
				break;
			case Actions.ACTION_FG:
				if (!jme.options.fgMenuOption)
					action = 0;
				break;

			case Actions.ACTION_MARK:
				if (jme.options.starNothing) {
					action = 0;
				}
				break;
			}

		}
		return action;
	}

	/**
	 * mouse cursor move over the button square: special action
	 * 
	 * @param action
	 * @return
	 */
	public boolean handleMouseEnterActionMenu(int action, JMEmol mol) {

		// idea: show info for each button
		// each action used here must also be declared in handleMouseLeaveActionMenu()
		String note = null;
		switch (action) {
		case Actions.ACTION_NEW:
			note = "Add new molecule";
			break;
		case Actions.ACTION_MOVE_AT:
			note = "Move atom";
			break;
		case Actions.ACTION_FG:
			note = "Add a functional group"; // BH added
			break;
		case Actions.ACTION_SPIRO:
			note = "Activate spiro ring";
			break;
		case Actions.ACTION_STEREO:
			note = "Stereo bond single or double";
			break;
		case Actions.ACTION_CHAIN:
			note = "Create alkyl chain";
			break;
		case Actions.ACTION_DELETE:
			note = "Delete atom or bond";
			break;
		case Actions.ACTION_DELGROUP:
			note = "Click bond to delete smallest fragment";
			break;
		case Actions.ACTION_SMI:
			note = "Show SMILES or SMIRKS";
			break;
		case Actions.ACTION_QRY:
			note = "Open query box for SMARTS";
			break;
		case Actions.ACTION_AN_X:
			note = "Select other atom type (" + jme.getAtomSymbolForX() + ")";
			break;
		case Actions.ACTION_AN_R:
			note = "Select R group";
			break;
		}
		if (note != null)
			jme.info(note);
		// if there is no atoms, then there is nothing to highlight
		if (mol == null || mol.natoms == 0) {
			return false;
		}
		switch (action) {
		case Actions.ACTION_CLEAR:
			note = (jme.moleculePartsList.size() > 1 ? "Delete selected molecule (red)" : "Clear canvas");
			mol.forceUniColor(Color.RED);
			uniColorMolecule = mol;
			break;
		case Actions.ACTION_REACP:
			note = "Copy selected (blue) molecule to the other side of the reaction";
			mol.forceUniColor(Color.BLUE);
			uniColorMolecule = mol;
		}
		if (note == null) {
			jme.setMustRedrawMolecularArea(false); // new oct 2016
			mustReDrawTopMenu = false;
		} else {
			jme.info(note);
			jme.setMustRedrawMolecularArea(true); // new oct 2016
			mustReDrawTopMenu = true;
		}
		return note != null; // || mustReDrawMolecularArea || mustReDrawTopMenu;
	}

	/**
	 * 
	 * @param action
	 * @return true if repaint needed
	 */
	public boolean handleMouseLeaveActionMenu(int action) {

		switch (action) {
		case Actions.ACTION_FG:
		case Actions.ACTION_NEW:
		case Actions.ACTION_MOVE_AT:
		case Actions.ACTION_SPIRO:
		case Actions.ACTION_SMI:
		case Actions.ACTION_QRY:
		case Actions.ACTION_AN_X:
		case Actions.ACTION_AN_R:
		case Actions.ACTION_STEREO:
		case Actions.ACTION_CHAIN:
		case Actions.ACTION_DELGROUP:
		case Actions.ACTION_DELETE:
			jme.clearInfo();
			return true;
		}
		if (uniColorMolecule != null
				&& (action == Actions.ACTION_CLEAR 
				|| (jme.options.reaction && action == Actions.ACTION_REACP))) {

			uniColorMolecule.resetForceUniColor();
			uniColorMolecule = null;
			for (JMEmol mol : jme.moleculePartsList) {
				mol.resetForceUniColor();
			}
			jme.clearInfo();
			jme.setMustRedrawMolecularArea(true);
			mustReDrawTopMenu = true;
		} else {
			jme.setMustRedrawMolecularArea(false); // new october2016
			mustReDrawTopMenu = false;
		}
		return mustReDrawMolecularArea;// || mustReDrawTopMenu;
	}

	public void dispose() {
		jme = null;
	}


	
}