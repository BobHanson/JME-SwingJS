/**
 * 
 */
package jme;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.BitSet;

import jme.core.Atom;

/**
 * @author bruno
 *
 */
public class AtomDisplayLabel {
	public double smallAtomWidthLabel = 0; // atom element only, e.g. N, Cl
	public double fullAtomWidthLabel; // atom element + extra e.g NH3+
	public int alignment;
	public boolean noLabelAtom;

	public Box box;
	public double labelX;
	public double labelY;

	public String str;

	int boundingBoxpadding = 2; // number of pixel of white space surrounding the atom labels

	public double atomMapY; // BB: move the map symbol higher
	public double atomMapX;
	public String mapString;

	private BitSet subscripts, superscripts;
	private BitSet bsSS;

	public static AtomDisplayLabel create(Atom atom, int alignment, FontMetrics fm, double h,
			boolean showHs, boolean showMap) {
		return new AtomDisplayLabel(atom, alignment, fm, h, showHs, showMap);
	}

	
	public AtomDisplayLabel(Atom a, int alignment, FontMetrics fm, double h, boolean showHs, boolean showMap) {
		int map = (showMap && a.hasBeenMapped() ? a.getMap() : -1);
		String z = a.getLabel();

		if (z == null || z.length() < 1) {
			z = "*";
			System.err.println("Z error!");
		}
		this.alignment = alignment;

		// boolean isCumuleneSP = (nv == 2 && sbo == 4);
		int padding = 2; // number of pixel of white space surrounding the atom labels
		noLabelAtom = (a.an == Atom.AN_C && a.q == 0 && a.iso == 0 && a.nv > 0 && (a.nv != 2 || a.sbo != 4));

		// BB better display for OH or o- : the bond will point at the atom symbol "O"
		// and not at the center of the "OH"
		String hydrogenSymbols = "";
		if (showHs && !this.noLabelAtom) {
			if (a.nh > 0) {
				hydrogenSymbols += "H";
				if (a.nh > 1) {
					hydrogenSymbols += a.nh;
				}
			}
		}

		String isoSymbol = (a.iso == 0 ? "" : "[" + a.iso + "]");

		String chargeSymbols = (a.q == 0 ? "" : (Math.abs(a.q) > 1 ? "" + Math.abs(a.q) : "") + (a.q > 0 ? "+" : "-"));

		String stringForWidth = z;
		if (alignment == JMEUtil.ALIGN_RIGHT) {
			z = chargeSymbols + hydrogenSymbols + isoSymbol + z; // H2N +H3N
		} else {
			z = isoSymbol + z + hydrogenSymbols + chargeSymbols; // NH2 , NH3+
		}
		this.str = z;

		if (alignment == JMEUtil.ALIGN_CENTER) {
			stringForWidth = z;
		}

		// place hydrogen count symbols subscript
		if (hydrogenSymbols.length() > 1) {
			int pos = z.indexOf(hydrogenSymbols);
			// H2 -> 2 subscript
			if (subscripts == null)
				subscripts = new BitSet();
			subscripts.set(pos + 1, pos + hydrogenSymbols.length());
		}

		// place charge symbols superscript
		if (chargeSymbols.length() > 0) {
			int pos = z.indexOf(chargeSymbols);
			if (superscripts == null)
				superscripts = new BitSet();
			superscripts.set(pos, pos + chargeSymbols.length());

		}

		// place isotope symbols superscript
		if (isoSymbol.length() > 0) {
			int pos = z.indexOf(isoSymbol);
			if (superscripts == null)
				superscripts = new BitSet();
			superscripts.set(pos, pos + isoSymbol.length());
		}

		int nsub = (subscripts == null ? 0 : subscripts.cardinality());
		int nsup = (superscripts == null ? 0 : superscripts.cardinality());
		double xAdj = 0, yAdj1 = 0, yAdj2 = 0;
		if (nsub != 0 || nsup != 0) {
			bsSS = new BitSet();
			if (nsub > 0) {
				bsSS.or(subscripts);
				yAdj1 = 1;
			}
			if (nsup > 0) {
				bsSS.or(superscripts);
				yAdj2 = 1;
			}
			xAdj = (bsSS.cardinality() * fm.charWidth('1') * (1-0.6));
		}
		
	
		// used to position / center the atom label
		double smallWidth = fm.stringWidth(stringForWidth);
		// used to compute the bounding box of the atom label
		double fullWidth = fm.stringWidth(z) - xAdj;

		this.smallAtomWidthLabel = smallWidth;
		this.fullAtomWidthLabel = fullWidth;

		int lineThickness = 1;

		double x = a.x;
		double y = a.y;

		// small width is used to compute the position xstart, that is the x position of
		// the label
		double xstart = x - smallWidth / 2.;
		if (alignment == JMEUtil.ALIGN_RIGHT) {
			xstart -= (fullWidth - smallWidth); // move the xstart further left
		}
		double ystart = y - h / 2; // o 1 vyssie

		// to take into account the line thickness
		xstart -= lineThickness;
		fullWidth += lineThickness;
		Box box = this.box = new Box(
				xstart - padding,
				ystart - padding - yAdj2 * h/3, 
				fullWidth + 2 * padding, 
				h + 2 * padding + (yAdj1 + yAdj2)*h/3);
		this.mapString = null;
		this.labelX = xstart + 1; // see
		this.labelY = ystart + h; // o 1 vyssie

		if (map < 0)
			return;

		// BH TODO: Haven't figured out the issue with 
		// to extend the size of the atomLabelBoundingBox

		this.mapString = " " + map;
		double superscriptMove = h * 0.3; // BB: move the map symbol higher
		
		
		if (noLabelAtom) {
			atomMapX = x + smallWidth / 4; // no atom symbol: put the map label closer, on the right
			atomMapY = y - (h + yAdj2*0.6) * 0.1; // BB: move the map symbol higher
		} else {
			double atomMapStringWidth = fm.stringWidth(mapString);
			if (alignment == JMEUtil.ALIGN_LEFT) {
				atomMapX = x - smallWidth / 2. + fullWidth;
			} else {
				box.x -= atomMapStringWidth;
				atomMapX = x + smallWidth / 2 - fullWidth - atomMapStringWidth;
			}

			// remember: y points down
			atomMapY = y - superscriptMove;
			box.y -= superscriptMove;
			box.height += superscriptMove;
			box.width += atomMapStringWidth;

		}
	}


	/**
	 * Draw with offsets and smaller fonts for subscripts and superscripts. 
	 * The bitsets tell us when to start and stop subscripts and superscripts.
	 * 
	 * @param og
	 * @param strokeColor
	 * @param h
	 * @param fm
	 */
	public void draw(PreciseGraphicsAWT og, Color strokeColor, double h, FontMetrics fm) {
		double strokeWidth = h / 20;
		if (bsSS == null) {
			og.drawStringWithStroke(str, labelX, labelY, strokeColor, strokeWidth);
			return;
		}
		Font normalFont = og.baseGraphics.getFont();
		Font subFont = normalFont.deriveFont((float)(h * 0.8));
		double x = labelX;
		double y = labelY;
		double subOffset = h / 3;
		double superOffset = 2 * subOffset;
		double yoff;
		for (int i = 0; i < str.length();) {
			int pt0 = i; 
			int pt1 = bsSS.nextSetBit(i);
			if (pt1 == pt0) {
				if (subscripts.get(i)) {
					pt1 = subscripts.nextClearBit(i + 1);
					yoff = subOffset;
				} else {
					pt1 = superscripts.nextClearBit(i + 1);
					yoff = superOffset;
				} 
			} else {
				if (pt1 < 0)
					pt1 = str.length();
				yoff = 0;
			}
			String s = str.substring(pt0, pt1);
			if (yoff != 0)	
				og.setFont(subFont);
			og.drawStringWithStroke(s, x,  y + yoff,  strokeColor,  strokeWidth);
			if (yoff != 0)	
				og.setFont(normalFont);
			x += fm.stringWidth(s) * (yoff == 0 ? 1 : 0.6);
			i = pt1;
		}
	}


}
