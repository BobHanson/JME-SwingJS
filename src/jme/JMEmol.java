package jme;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.StringTokenizer;

import jme.core.Atom;
import jme.core.AtomBondCommon;
import jme.core.Bond;
import jme.core.Box;
import jme.core.JMECore;
import jme.core.JMESmiles;
import jme.gui.Actions;
import jme.gui.AtomDisplayLabel;
import jme.gui.GUI;
import jme.gui.GUI.RingInfo;
import jme.io.JMEReader;
import jme.io.JMEWriter;
import jme.ocl.OclAdapter;

// --------------------------------------------------------------------------
public class JMEmol extends JMECore implements Graphical2DObject {

// constructors:
//	public JMEmol()  
//	public JMEmol(Parameters pars)  
//	public JMEmol(JME jme, Parameters pars)  

//	public JMEmol(JME jme, JMEmol mols[])  

//	public JMEmol(JME jme, JMEmol m, int part)  
//	public JMEmol(JME jme, JMEmol m, int part, Object NOT_USED)  

//	public JMEmol(JME jme, JmolAdapterAtomIterator atomIterator, JmolAdapterBondIterator bondIterator, Parameters pars) throws Exception  
//	public JMEmol(JME jme, String molecule, boolean hasCoordinates, Parameters pars) throws Exception  
//	public JMEmol(JME jme, String molFile, Parameters options)  

	public static class ReactionRole {
		public final static int NOROLE = 0;
		public final static int REACTANT = 1;
		public final static int AGENT = 2;
		public final static int PRODUCT = 3;

		public final static int all[] = { REACTANT, AGENT, PRODUCT };
		public final static int maxRole = PRODUCT;
		public final static int ANY = -1;

	};

	final static boolean doTags = false; // compatibility with JMEPro

	public JME jme; // parent

	public int chain[] = new int[101];

	public int touchedAtom = 0; // nesmu byt static kvoli reaction (multi?)
	public int touchedBond = 0;
	public int touched_org = 0; // original v rubber banding
	public double xorg, yorg; // center of ring in free space, rubber banding
	public int nchain; // pomocna variable pre CHAIN (aktualna dlzka pri rubber)
	boolean stopChain = false;
	boolean needRecentering = false;
	boolean isQuery = false; // 2013.09

	Color uniColor = null;

	private int reactionRole = ReactionRole.NOROLE;

	protected boolean mixPastelBackGroundColors = true;
	private double centerx = Double.NaN, centery;
	private AtomDisplayLabel[] atomLabels;

	private RingInfo ringInfo;
	private boolean haveDoubleBonds;

	// used for junit testing
	public JMEmol() {
		this(null, (Parameters) null);
	}

	public JMEmol(Parameters pars) {
		this(null, pars);
	}

	/**
	 * The primary entry point; construct an empty molecule
	 * 
	 * @param jme
	 * @param pars desired parameters or null for defaults
	 */
	public JMEmol(JME jme, Parameters pars) {
		super(jme, pars);
		this.jme = jme;
	}

	/**
	 * Construct a deep copy of the given molecule.
	 * 
	 * @param m
	 */
	JMEmol(JMEmol m) {
		super((JMECore) m);
		jme = m.jme;
		reactionRole = m.reactionRole;
		ringInfo = m.ringInfo;
	}

	/**
	 * Construct a molecule by merging molecules.
	 * 
	 * @param jme
	 * @param mols
	 */
	public JMEmol(JME jme, JMEmol[] mols) {
		super(jme, mols);
		this.jme = jme;
		if (mols.length > 0)
			reactionRole = mols[0].reactionRole;
	}

	/**
	 * Construct a fragment molecule from a part of another molecule.
	 * 
	 * @param jme
	 * @param m
	 * @param part the fragment part to get
	 */
	public JMEmol(JME jme, JMEmol m, int part) {
		super(jme, m, part);
		this.jme = jme;
		reactionRole = m.reactionRole;
		ringInfo = m.ringInfo;
	}

	/**
	 * Duplicate of JMEmol(JME jme, JMEmol m, int part) , uses the internal
	 * partIndex instead of the this.a[] for the part information
	 * 
	 * @param m
	 * @param part
	 */
	public JMEmol(JME jme, JMEmol m, int part, Object NOT_USED) {
		this(jme, m.parameters);
		setPart(m, part);
	}

	// ----------------------------------------------------------------------------
	/**
	 * 
	 * Construct a JMEmol from a JME string
	 * 
	 * @param jme
	 * @param molecule jme string, for example
	 * @param type     JME, for example
	 * @param pars
	 * @throws Exception
	 */
	public JMEmol(JME jme, Object molecule, JME.SupportedFileFormat type, Parameters pars) throws Exception {
		this(jme, pars);
		if (molecule == null)
			return;
		switch (type) {
		case JME:
			createFromJMEString((String) molecule);
			break;
		case MOL: // V2000, actually
			createFromMOLString((String) molecule);
			break;
		case JMOL:
			createFromJmolAdapter((Object[]) molecule);
			break;
		default:
			throw new IllegalArgumentException("Unrecognized format");
		}
	}

	public String createJMEString(Box boundingBox) {
		return JMEWriter.createJMEString(this, false, boundingBox);
	}

	/**
	 * Process the JME string: natoms nbonds (atomic_symbol x y) (va vb bondType)
	 * 
	 * atomic symbols for smiles-non-standard atoms may be in smiles form i.e O-,
	 * Fe2+, NH3+
	 * 
	 * @param jmeString
	 */
	private void createFromJMEString(String jmeString) {
		JMEReader.createJMEFromString(this, jmeString);
	}

	/**
	 * Create a molecule from MOLfile V2000 data.
	 * 
	 * @param molData
	 */
	private void createFromMOLString(String molData) {
		JMEReader.createJMEFromMolData(this, molData);
	}

	/**
	 * From Jmol's SmarterJmolAdapter -- could be any one of dozens of kinds of
	 * file.
	 * 
	 * @param iterators [atomIterator, bondIterator]
	 * @throws Exception
	 */
	private void createFromJmolAdapter(Object[] iterators) throws Exception {
		JMEReader.createJMEFromJmolAdapter(this, iterators);
	}

	public static JMEmol mergeMols(ArrayList<JMEmol> mols) {
		return (mols.size() == 0 ? new JMEmol() : new JMEmol(mols.get(0).jme, mols.toArray(new JMEmol[mols.size()])));
	}

	/**
	 * Compute the average color for the atom or bond and set the color to the
	 * graphics use the .bacgroundColors array which is independent from the mark
	 * 
	 * @param og
	 * @param ab
	 * @param isAtom
	 * @return
	 */
	protected Color setPresetPastelBackGroundColor(PreciseGraphicsAWT og, int ab, boolean isAtom) {
		int colors[] = new int[1];
		boolean colorIsSet = false;

		boolean showMappingNumbers = this.parameters.number;
		boolean showColorMark = this.parameters.mark;
		boolean showAtomMapNumberWithBackgroundColor = this.parameters.showAtomMapNumberWithBackgroundColor;
		AtomBondCommon atomOrBond = isAtom ? this.atoms[ab] : this.bonds[ab];

		// an atom map can be mapped to the a given color of the palette, meaning that
		// visually
		// the reaction mapping could be easier to interpret than just the display of
		// numbers.
		if (showMappingNumbers && showAtomMapNumberWithBackgroundColor && isAtom) {
			Atom atom = this.atoms[ab];
			if (atom.isMapped()) {
				int map = atom.getMap();
				int max_map = jme.colorManager.numberOfBackgroundColors();
				// recycle the colors if too large atom map number
				while (map > max_map) {
					map -= max_map;
				}
				colors[0] = map;
				colorIsSet = true;
			}
		}

		if (!colorIsSet && showColorMark && atomOrBond.isMarked()) {
			colors[0] = atomOrBond.getMark(); // should we recycle the colors here?
			colorIsSet = true;
		}

		if (!colorIsSet) {
			// color mixing of background colors
			colors = isAtom ? this.getAtomBackgroundColors(ab) : this.getBondBackgroundColors(ab);
		}

		Color color = (colors != null && colors.length > 0 ? jme.colorManager.averageColor(colors) : null);
		if (color != null) {
			og.setColor(color);
		}
		return color;
	}

	void forceUniColor(Color color) {
		this.uniColor = color;
	}

	void resetForceUniColor() {
		this.uniColor = null;
	}

	/**
	 * @return the reactionRole
	 */
	public int getReactionRole() {
		return reactionRole;
	}

	/**
	 * @param reactionRole the reactionRole to set
	 */
	public void setReactionRole(int reactionRole) {
		this.reactionRole = reactionRole;
	}

	public void center() {
		center(1.0);
	}

	/**
	 * centers molecule within the window xpix x ypix
	 * 
	 * @param factor
	 */
	public void center(double factor) {
		if (natoms == 0)
			return;

		Rectangle2D.Double widthAndHeight = this.jme.getMolecularAreaBoundingBoxCoordinate00();
		double xpix = widthAndHeight.width;
		double ypix = widthAndHeight.height;

		if (xpix <= 0 || ypix <= 0) { // does this ever happen?
			needRecentering = true;
			return;
		}

		Rectangle2D.Double cad = computeBoundingBoxWithAtomLabels(null);

		double shiftx = xpix / 2 - cad.getCenterX(); // . center[0];
		double shifty = ypix / 2 - cad.getCenterY(); // center[1];
		if (!jme.nocenter)
			moveXY(shiftx * factor, shifty * factor);
	}

	/**
	 * Need to be improved: should take the atom labels width and height into
	 * account
	 */
	@Override
	public double closestDistance(double x, double y) {
		return closestAtomDistance(x, y);
	}

	// ----------------------------------------------------------------------------
	@Override
	public void draw(PreciseGraphicsAWT og) {
		int atom1, atom2;
		double xa, ya, xb, yb;
		double sin2, cos2;
		final double offset2 = 2, offset3 = 3;

		if (this.nAtoms() == 0)
			return; // bug fix github #25

		boolean markColorBackground = parameters.mark;
		Color atomTextStrokeColorArray[] = new Color[this.nAtoms() + 1];

		og.setDefaultBackGroundColor(jme.canvasBg);

		// ked padne, aby aspon ukazalo ramcek
		// this should not be done here
		if (jme.options.depictBorder) {
			og.setColor(Color.black);
			og.drawRect(0, 0, jme.dimension.width - 1, jme.dimension.height - 1);
		}

		if (this.uniColor != null) {
			og.overrideColor(this.uniColor);
		}

		// should not be done here
		if (needRecentering) {
			center();
			jme.alignMolecules(1, jme.moleculePartsList.size(), 0); // !!! nefunguje pre reakcion
			needRecentering = false;
			// System.err.println("DD recenter " + jme.dimension.width);
		}
		// TODO: no more reference to scalingIsPerformedByGraphicsEngine and
		// molecularAreaScale
		// atom + bond background coloring - done before drawing the atoms

		// bonds
		// color background for bonds (2 atoms must have a least one color in common)
		double rs = jme.options.bondBGrectRelativeSize;
		if (rs > 0) {
			for (int i = 1; i <= nbonds; i++) {
				// setPresetPastelBackGroundColor() returns null if no bacground colors have
				// been specified != mark
				if (setPresetPastelBackGroundColor(og, i, false) != null) {
					atom1 = bonds[i].va;
					atom2 = bonds[i].vb;
					setCosSin(atom1, atom2);
					cos2 = (offset3 * 3) * temp[0];
					sin2 = (offset3 * 3) * temp[1];

					sin2 *= rs;
					cos2 *= rs;

					double[] xr = new double[4], yr = new double[4];
					xr[0] = x(atom1) + sin2;
					yr[0] = y(atom1) - cos2;
					xr[1] = x(atom2) + sin2;
					yr[1] = y(atom2) - cos2;
					xr[2] = x(atom2) - sin2;
					yr[2] = y(atom2) + cos2;
					xr[3] = x(atom1) - sin2;
					yr[3] = y(atom1) + cos2;
					og.fillPolygon(xr, yr, 4);
				}
			}
		}

		// atom + bond background coloring - done before drawing the atoms
		// if (markColorBackground ||
		// this.parameters.showAtomMapNumberWithBackgroundColor) {
		double cs = offset2 * 12;

		// atoms : circle behind the atom position - does not cover the atom symbol
		// (will be done later)
		rs = jme.options.atomBGcircleRelativeSize;
		if (rs > 0) {
			for (int i = 1; i <= natoms; i++) {
				Color backgroundColor = setPresetPastelBackGroundColor(og, i, true);
				if (backgroundColor != null) {
					double scs = cs * rs;
					og.fillOval(x(i) - scs / 2., y(i) - scs / 2., scs, scs);

					// if we have a high resolution screen or a large zoom factor,
					// then add a thin stroke to the text to improve readability
					if (
					// JMEUtil.isHighDPI() ||
					og.currentZoomFactor() >= 2) { // or JMSE zoom factor > 100 %
						Color contrastColor = ColorManager.contrast(backgroundColor); // the stroke color is either
																						// white or black
						// depending on the darkness of the backgroundColor
						atomTextStrokeColorArray[i] = contrastColor;
					}

				}
			}

		}

		// BB
		// boolean[] isRingBond = JMEUtil.createBArray(nbonds+1);
		// this.findRingBonds(isRingBond);

		// draw bonds
		
		if (ringInfo == null && haveDoubleBonds) {
			setRingInfo();
		}
//		if (ringInfo != null) {
//			og.setColor(Color.red);
//			for (int i = 0; i < ringInfo.rings.size(); i++) {
//				Ring r = ringInfo.rings.get(i);
//				if (r.bondCount > 0)
//					og.drawString("" + i + "/" + r.bondCount, r.cx-5, r.cy-2);
//			}
//		}

		// BH 2023 atomLabels needed for drawing double bonds
		
		computeAtomLabels();
		og.setFont(jme.gui.getAtomDrawingFont());
		FontMetrics fm = jme.gui.getAtomDrawingFontMetrics();
		double h = GUI.stringHeight(fm);

		for (int i = 1; i <= nbonds; i++) {
			// og.setColor(Color.black);

			Bond bond = bonds[i];
			atom1 = bond.va;
			atom2 = bond.vb;

			og.setColor(bond.isCoordination() ? Color.LIGHT_GRAY : Color.BLACK);

			// new June 2017
			if (jme.action == Actions.ACTION_DELGROUP && touchedBond == i && this.isRotatableBond(i)) { // duplicated
																										// logic
				// with code below
				// for handling
				// ACTION_DELGROUP

				// og.setColor(Color.RED);
				continue; // do not draw the bond , that is more visible than red color
			}
			if (bond.stereo == Bond.STEREO_XUP 
					|| bond.stereo == Bond.STEREO_XDOWN
					|| bond.stereo == Bond.STEREO_XEITHER) // kvoli spicke vazby
			{
				int d = atom1;
				atom1 = atom2;
				atom2 = d;
			}

			xa = x(atom1);
			ya = y(atom1);
			xb = x(atom2);
			yb = y(atom2);

			if (!(bond.isSingle() || bond.isCoordination()) || bond.stereo != 0) {
				setCosSin(atom1, atom2); // BH??? test distance was 1, not .001?
			}
			switch (bond.bondType) {
			case Bond.DOUBLE:
				// BB crossed bond display: not magenta anymore
				// if (bond.stereo >= 10) og.setColor(Color.magenta); // E,Z je farebna
				cos2 = offset2 * temp[0];
				sin2 = offset2 * temp[1];
				if (bond.stereo != Bond.STEREO_EZ) {
					if (Double.isNaN(bond.guideX) && (
							Double.isNaN(bond.guideY) 
							|| !atomLabels[bond.va].noLabelAtom
							|| !atomLabels[bond.vb].noLabelAtom)) {
						og.drawLine(xa + sin2, ya - cos2, xb + sin2, yb - cos2);
						og.drawLine(xa - sin2, ya + cos2, xb - sin2, yb + cos2);
					} else {
						og.drawLine(xa, ya, xb, yb);
						drawSideLine(og, bond, xa, ya, xb, yb, cos2, sin2);
					}
				} else { // BB: crossed bond
					og.drawLine(xa + sin2, ya - cos2, xb - sin2, yb + cos2);
					og.drawLine(xa - sin2, ya + cos2, xb + sin2, yb - cos2);

				}
				og.setColor(Color.black);
				break;
			case Bond.TRIPLE:
				og.drawLine(xa, ya, xb, yb);
				double cos3 = offset3 * temp[0];
				double sin3 = offset3 * temp[1];
				og.drawLine(xa + sin3, ya - cos3, xb + sin3, yb - cos3);
				og.drawLine(xa - sin3, ya + cos3, xb - sin3, yb + cos3);
				break;
			case Bond.QUERY:// case 0: // dotted //BB : removed 0 because of coordination
				for (int k = 0; k < 10; k++) {
					double xax = xa - (xa - xb) / 10. * k;
					double yax = ya - (ya - yb) / 10. * k;
					og.drawLine(xax, yax, xax, yax);
				}
				// query bond text
				Object o = bond.btag;
				String z = "?";
				if (o != null)
					z = (String) o;
				double w = fm.stringWidth(z);
				double xstart = (xa + xb) / 2. - w / 2.;
				double ystart = (ya + yb) / 2. + h / 2 - 1; // o 1 vyssie
				og.setColor(Color.magenta);
				og.drawString(z, xstart, ystart);
				og.setColor(Color.black);
				break;
			default: // Bond.SINGLE, alebo stereo
				if (bond.stereo == Bond.STEREO_UP || bond.stereo == Bond.STEREO_XUP) {
					cos2 = offset3 * temp[0];
					sin2 = offset3 * temp[1];
					double[] px = new double[3];
					double[] py = new double[3];
					px[0] = xb + sin2;
					py[0] = yb - cos2;
					px[1] = xa;
					py[1] = ya;
					px[2] = xb - sin2;
					py[2] = yb + cos2;
					og.fillPolygon(px, py, 3);
				} else if (bond.stereo == Bond.STEREO_DOWN || bond.stereo == Bond.STEREO_XDOWN) {
					cos2 = offset3 * temp[0];
					sin2 = offset3 * temp[1];
					for (double k = 0; k < 10; k++) {
						double xax = xa - (xa - xb) / 10. * k;
						double yax = ya - (ya - yb) / 10. * k;
						double sc = k / 10.;
						og.drawLine(xax + sin2 * sc, yax - cos2 * sc, xax - sin2 * sc, yax + cos2 * sc);
					}
				} else if (bond.stereo == Bond.STEREO_EITHER || bond.stereo == Bond.STEREO_XEITHER) {
					double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
					cos2 = offset3 * temp[0];
					sin2 = offset3 * temp[1];
					double m = 8;
					for (double k = 0; k < m + 1; k += 1) {
						double xax = xa - (xa - xb) / m * k;
						double yax = ya - (ya - yb) / m * k;
						double sc = k / m; // thickness ?
						x1 = xax + sin2 * sc;
						y1 = yax - cos2 * sc;
						if (k > 0) {
							og.drawLine(x2, y2, x1, y1);
						}
						x2 = xax - sin2 * sc;
						y2 = yax + cos2 * sc;
						og.drawLine(x1, y1, x2, y2);

					}

				} else // normal single bonds
					og.drawLine(xa, ya, xb, yb);
				break;

			}
			// bond tags
			if (JMEmol.doTags) {
				String btag = bond.btag;
				if (btag != null && btag.length() > 0) {
					double w = fm.stringWidth(btag);
					double xstart = (xa + xb) / 2. - w / 2.;
					double ystart = (ya + yb) / 2. + h / 2 - 1; // o 1 vyssie
					og.setColor(Color.red);
					og.drawString(btag, xstart, ystart);
					og.setColor(Color.black);
				}
			}
		}

		// BB
		// try to improve the positioning and direction of the atom label when there are
		// either charges or implicit hydrogens
		// Direction example: -NH2 or H2N-
		// vertical positioning is currently not implemented
		// TODO: NH2, the 2 should subscript
		// TODO NH3+, the + should be superscript

		// draw atom background around the atom symbol if requested
		// draw atom symbol
		for (int i = 1; i <= natoms; i++) {
			if (atomLabels[i].noLabelAtom) {
				continue;
			}
			og.setBackGroundColor(); // set default background color
			setPresetPastelBackGroundColor(og, i, true);

			// surround the atom label with background color to mask the bonds around the
			// atom label
			atomLabels[i].fill(og);
			// color for the atom symbol
			og.setColor(JME.color[an(i)]);
			Color strokeColor = atomTextStrokeColorArray[i];
			atomLabels[i].draw(og, strokeColor, h, fm);
		}

		// diplay atom maps of atoms that have been marked
		// marked atoms - islo by to do predosleho loopu zapasovat ???

		// 10-2018: do not show maps if option star is set
		if (!markColorBackground) {
			og.setFont(jme.gui.atomMapDrawingAreaFont);

			for (int i = 1; i <= natoms; i++) {
				AtomDisplayLabel al = atomLabels[i];
				String mapString = al.mapString;

				if (mapString == null)
					continue;

				double atomMapX = al.atomMapX;
				double atomMapY = al.atomMapY;

				og.setColor(Color.magenta); // default color for atom map - could be an option

				// duplicated code
				Color strokeColor = atomTextStrokeColorArray[i];

				if (strokeColor == null) {
					og.drawString(mapString, atomMapX, atomMapY);
				} else {
					og.drawStringWithStroke(mapString, atomMapX, atomMapY, strokeColor, h / 20);
				}

			}
		}

		// two bugs:
		// doTags does not take leftToRight into account
		// if both atom maps and tags are presents , they will overlap

		// BB: I changed this part but could not test it
		// tags (povodne to bolo label)
		if (JMEmol.doTags) {
			for (int i = 1; i <= natoms; i++) {
				Atom a = atoms[i];
				if (a.atag == null || a.atag.equals(""))
					continue;
				// int w = jme.atomDrawingAreaFontMet.stringWidth(zz[i]);
				AtomDisplayLabel al = atomLabels[i];
				double smallWidth = al.smallAtomWidthLabel;
				double fullWidth = al.fullAtomWidthLabel;

				double xstart = a.x - smallWidth / 2.;
				double ystart = a.y + h / 2 - 1; // o 1 vyssie
				og.setColor(Color.red);
				og.drawString(" " + a.atag, xstart + fullWidth, ystart);
			}
		}

		// mark touched bond or atom, or atoms marked to delete
		if ((touchedAtom > 0 || touchedBond > 0) && !JME.webme) {

			og.setColor(jme.action == Actions.ACTION_DELETE ? Color.red :
			// just checking: Hmm...jme.mouseShift ? Color.cyan :
					Color.blue);

			if (touchedAtom > 0 && jme.action != Actions.ACTION_DELGROUP) {
				Rectangle2D.Double r = atomLabels[touchedAtom].drawBox;
				og.drawRect(r.x, r.y, r.width, r.height);
			}

			if (touchedBond > 0 && jme.action != Actions.ACTION_MOVE_AT) {
				// don't show a rectangle around the bond if the
				// action is to move the atom

				atom1 = bonds[touchedBond].va;
				atom2 = bonds[touchedBond].vb;
				setCosSin(atom1, atom2);
				cos2 = (offset3 + 1) * temp[0];
				sin2 = (offset3 + 1) * temp[1];
				double[] px = new double[5];
				double[] py = new double[5];
				px[0] = x(atom1) + sin2;
				px[1] = x(atom2) + sin2;
				py[0] = y(atom1) - cos2;
				py[1] = y(atom2) - cos2;
				px[3] = x(atom1) - sin2;
				px[2] = x(atom2) - sin2;
				py[3] = y(atom1) + cos2;
				py[2] = y(atom2) + cos2;
				px[4] = px[0];
				py[4] = py[0]; // bug in 1.01
				if (jme.action != Actions.ACTION_DELGROUP) // pri DELGROUP nekresli modro
					og.drawPolygon(px, py, 5);

				if (jme.action == Actions.ACTION_DELGROUP && isRotatableBond(touchedBond)) {
					// ACTION_DELGROUP is a specila way of deleting a groug of atoms icon: -X-R
					// two parts of the molecule, one to keep and one to delete
					// the smallest that will be deleted mut be shown in red
					// only possible if the selected bond is not a ring bond
					// marks atoms with unpleasent fate (suggested by Bernd Rohde)
					// the atoms selected for deleting are in this.a[]?

					int va = bonds[touchedBond].va;
					int vb = bonds[touchedBond].vb;
					this.computeMultiPartIndices(touchedBond);
					int partA = atoms[va].partIndex;
					int partB = atoms[vb].partIndex;
					int sizeA = 0;
					int sizeB = 0;

					for (int i = 1; i <= natoms; i++) {
						int pi = atoms[i].partIndex;
						if (pi == partA) {
							sizeA++;
						} else if (pi == partB) {
							sizeB++;
						}
					}
					// choose the smallest part to be deleted
					int partToDelete = sizeA > sizeB ? partB : partA;
					// framing atoms to delete in red
					og.setColor(Color.red);
					for (int i = 1; i <= natoms; i++) {
						this.atoms[i].deleteFlag = false;
						if (atoms[i].partIndex == partToDelete) {
							this.atoms[i].deleteFlag = true;
							Rectangle2D.Double r = atomLabels[i].drawBox;
							og.drawRect(r.x, r.y, r.width, r.height);
						}
					}
				}
			}
		}

		if (this.uniColor != null) {
			og.resetOverrideColor();
		}

	}

	private void drawSideLine(PreciseGraphicsAWT og, Bond bond, double xa, double ya, double xb, double yb, double cos2,
			double sin2) {
		double cx = bond.centerX;
		double cy = bond.centerY;
		double ox = cx + sin2 * 2;
		double oy = cy - cos2 * 2;
		double gx = bond.guideX;
		double gy = bond.guideY;
		double min = 3;

//		og.setColor(Color.red);
//		og.drawString("x", cx-2, cy+3);
//		og.drawString("o", gx - 2, gy +3);

		// check for trans.
		if ((cx - gx) * (cx - gx) + (cy - gy) * (cy - gy) < min) {
			gx = Double.NaN;
		}

		// check for offset wrong side
		double f = (Double.isNaN(gx) ? -2f
				: (ox - gx) * (ox - gx) + (oy - gy) * (oy - gy) > (cx - gx) * (cx - gx) + (cy - gy) * (cy - gy) ? -2
						: 2);

		double g = 0.1;
		double dx = xb - xa;
		double dy = yb - ya;
		xa += dx * g;
		xb -= dx * g;
		ya += dy * g;
		yb -= dy * g;

		double x1 = xa + sin2 * f;
		double y1 = ya - cos2 * f;
		double x2 = xb + sin2 * f;
		double y2 = yb - cos2 * f;

		og.drawLine(x1, y1, x2, y2);
	}

	/**
	 * ///////////////////////////////////////////// // atom labels
	 * 
	 * //orientation of the atom labels with H or charges //either right to left
	 * (OH) or left to right (HO) // based on the relative position of the neighbors
	 * // bug fix November 2019: atom map
	 * 
	 */
	void computeAtomLabels() {
		boolean showHs = parameters.hydrogenParams.showHs;
		boolean showMap = (!parameters.mark || parameters.showAtomMapNumberWithBackgroundColor);
		FontMetrics fm = jme.gui.getAtomDrawingFontMetrics();
		double rb = RBOND;
		atomLabels = AtomDisplayLabel.createLabels(this, rb, fm, showHs, showMap, atomLabels);
	}

	@Override
	public Box computeBoundingBoxWithAtomLabels(Box union) {
		if (natoms == 0)
			return union;
		computeAtomLabels();
		for (int i = 1; i <= natoms; i++)
			union = this.atomLabels[i].drawBox.createUnion(union, union);
		return union;
	}

	/**
	 * Addition a new bond in mouseDrag mode: move the bond around with the mouse
	 * position.
	 */
	void rubberBanding(double xnew, double ynew) {
		// len pre vazby
		// povodny touchedAtom je ulozeny v touched_org (urobene v mouse_down)

		// last atom is the atom at the end of the new bond

		touchedAtom = 0;

		XY(0, xnew, ynew); // gives atom 0 the coordinates of the mouse pointer
		if (jme.action != Actions.ACTION_CHAIN) { // pri chaine to blblo
			int atom = checkTouch(0, true); // in order to find a close enough atom
			if (atom > 0) {
				touchedAtom = atom;
				if (atom == touched_org) {
					XY(natoms, xorg, yorg);// move the new atom to the coordinate of the origin atom "snap"
				} else {
					XY(natoms, x(atom), y(atom)); // move the new atom to the coordinate of the closest touched atom
				}
			} else {
				// bond width normal length follows mouse pointer
				setCosSin(touched_org, 0);
				XY(natoms, x(touched_org) + JMECore.RBOND * temp[0], y(touched_org) + JMECore.RBOND * temp[1]);
			}
			return;
		}
		// chain processing
		// first atom (chain=1) was added in mouseDown
		// ma 4 moznosti: back, flip, add1, add2
		// chain[0] is origin, chain[1] moze len flipnut, nie deletnut
		// miesto add sa moze aj napojit na existing atom

		touchedBond = 0; // 2005.02 (no marked bond)
		// action according to the mouse position
		int last = chain[nchain]; // last atom
		int parent = chain[nchain - 1];
		double dx = x(last) - x(parent);
		double dy = y(last) - y(parent);
		double rx = Math.sqrt(dx * dx + dy * dy);
		if (rx < 1.0)
			rx = 1.0;
		double sina = dy / rx;
		double cosa = dx / rx;
		double vv = rx / 2. / Math.tan(Math.PI / 6.);
		// moving mouse pos
		double xx = xnew - x(parent);
		double yy = ynew - y(parent);
		double xm = -rx / 2. + xx * cosa + yy * sina; // relativ to "0"
		double ym = yy * cosa - xx * sina; // hore / dolu
		// zistuje poziciu mouse point relativne k trojuholniku
		if (xm < 0.) { // delete this atom
			// special treatment per 1. atom (inak sa vzdy vymaze)
			if (nchain > 1) { // !!!
				deleteAtom(natoms);
				// this.jme.recordAtomEvent("deleteAtom", natoms);
				nchain--;
				stopChain = false;
			} else if (natoms == 2) { // first 2 atoms / \ flip (4 positions)
				// up down flip
				if (y(2) - y(1) < 0 && ynew - y(1) > 0)
					atoms[2].y = y(1) + rx / 2.;
				else if (y(2) - y(1) > 0 && ynew - y(1) < 0)
					atoms[2].y = y(1) - rx / 2.;
				// left right flip
				if (x(2) - x(1) < 0 && xnew - x(1) > 0)
					atoms[2].x = x(1) + rx * .866;
				else if (x(2) - x(1) > 0 && xnew - x(1) < 0)
					atoms[2].x = x(1) - rx * .866;
			} else { // skusa flipnut 1. atom (x je vzdy -RBOND !) okolo chain[0]
				if (nv(chain[0]) == 2) { // i.e. moze flipnut
					int ref = v(chain[0])[1];
					if (ref == chain[1])
						ref = v(chain[0])[2];
					// flipne len ked mouse na opacnej strane ref---chain[0] ako ch[1]
					dx = x(chain[0]) - x(ref);
					dy = y(chain[0]) - y(ref);
					rx = Math.sqrt(dx * dx + dy * dy);
					if (rx < 1.0)
						rx = 1.0;
					sina = dy / rx;
					cosa = dx / rx;
					// moving mouse pos
					xx = xnew - x(ref);
					yy = ynew - y(ref);
					double ymm = yy * cosa - xx * sina; // hore / dolu
					// moving chain[1]
					xx = x(chain[1]) - x(ref);
					yy = y(chain[1]) - y(ref);
					double yc1 = yy * cosa - xx * sina; // hore / dolu
					if (ymm > 0. && yc1 < 0. || ymm < 0. && yc1 > 0.) { // su opacne
						int bd = nbonds;
						addBondToAtom(0, chain[0], 0, false); // adds new bond
						deleteBond(bd, true); // delets old bond
						if (checkTouch(natoms, true) > 0)
							stopChain = true;
					}
				}
			}
		} else {
			if (stopChain)
				return;
			// calculates triangle height at this position
			double th = -1.; // mouse too far right
			if (xm < rx * 1.5)
				th = (rx * 1.5 - xm) * vv / (rx * 1.5);
			if (Math.abs(ym) > th) { // mouse above/below trinagle border
				nchain++;
				if (nchain > 100) {
					// info("You are too focused on chains, enough of it for now !");
					jme.showInfo("You are too focused on chains, enough of it for now !");
					nchain--;
					return;
				}
				addBondToAtom(Bond.SINGLE, natoms, (int) Math.round(ym), false);
				// this.jme.recordBondEvent("addBond"); // wait until finished
				jme.willPostSave(false); // do not store undo state
				chain[nchain] = natoms;
				if (checkTouch(natoms, true) > 0)
					stopChain = true;
			}
		}
		touchedAtom = 0;
	}

	// ----------------------------------------------------------------------------
	void checkChain() {
		// called from mouseUp after finishing chain
		if (stopChain) { // if overlap, then last added atom
			int n = checkTouch(natoms, false);
			// adding bond natoms - natoms-1 to n
			if (nv(n) < MAX_BONDS_ON_ATOM) { // if ==, no error message
				// making bond n - nchain-1
				int parent = chain[nchain - 1];
				createAndAddNewBond(n, parent, Bond.SINGLE);
			}
			deleteAtom(natoms); // delete the last atom from the chain because of the closing with anonther atom
		}
		stopChain = false;
	}

	/**
	 * checking touch of atom with my other atoms
	 * 
	 * @param atom
	 * @return the atom index that is the closest among the too close ones
	 */
	int checkTouch(int atom, boolean onlyOne) {
		return checkTouchToAtom(atom, 1, natoms, GUI.TOUCH_LIMIT, onlyOne);
	}

	protected int countNumberOverlapAtomOfAddedFragment(int fragmentFirstAtom, int fragmentLastAtom) {
		int result = 0;
		for (int at = 1; at <= natoms; at++) {
			if (at < fragmentFirstAtom && at > fragmentLastAtom
					&& checkTouchToAtom(at, fragmentFirstAtom, fragmentLastAtom, GUI.TOUCH_LIMIT, true) != 0) {
				result++;
			}
		}
		return result;
	}

	// ----------------------------------------------------------------------------
	public void avoidTouch(int from) {
		// checking atom overlap and moving atoms away from each other
		// moving always atom with the higher number
		// called after GROUP or CHAIN

		// BB: it is not the whole group that is moved, usually it a single atom of an
		// added ring
		// it could be more than one atom if there is more than one touch

		// should we have a loop that move atoms until there are no more overlap?

		// double dx,dy,rx;
		// double min=TOUCH_LIMIT+1;
		if (from == 0)
			from = natoms; // checks last from atoms

		for (int i = natoms; i > natoms - from; i--) {
			if (checkTouch(i, true) != 0)
				moveXY(i, 6, 6);
		}
	}

	// ----------------------------------------------------------------------------
	/**
	 * A new bond between the last atom and the touched atom has been created. if
	 * the two atoms are too close, then increase the bond order between the two
	 * atoms instead of creating a new bond
	 */
	void checkBond() {
		// check ci sa novo pridany atom neprekryva s nejakym starym
		// natoms bol posledne pridany atom, bol pridany k touchedAtom

		/*
		 * google translation: whether the newly added atom overlap with some old natoms
		 * was recently added atom , was added to the Touched
		 * 
		 */
		// check for touch of end of new bond with some atom
		int atom = checkTouch(natoms, false); // the atom index that is the closest among the too close ones
		if (atom == 0)
			return;

		// skutocne sa dotyka atomu atom = actually touches the Atom
		natoms--;

		int i = getBondIndex(atom, touched_org);
		if (i > 0) {
			nbonds--; // delete the just created new bond
			incrNV(touched_org, -1);
			// and increase the bond order between the two atom that were already bonded,
			// unless triple
			if (bonds[i].bondType < Bond.TRIPLE) {
				bonds[i].bondType++;
				bonds[i].stereo = 0;
			} // stereo zrusi
			else
				info("Maximum allowed bond order is 3 !");
			return;
		}
		if (nv(atom) == MAX_BONDS_ON_ATOM) {
			nbonds--; // delete the just created new bond
			incrNV(this.touched_org, -1);
			info("Not possible connection !");
			return;
		}

		// zmeni vazbove data na touched_org a atom
		bonds[nbonds].vb = atom;
		incrNV(this.touched_org, -1); // the new atom was added last, thus just need to decrease the nv
		this.addBothNeighbors(atom, touched_org);
		setBondCenter(bonds[nbonds]);
	}

	// necessary to add "smaller" bonds in scaled molecule bt WebME
//	double RBOND() {
//		return //(
//				//JME.scalingIsPerformedByGraphicsEngine ? 
//				JMECore.RBOND;
//				//			: JMECore.RBOND * jme.molecularAreaScalePixelsPerCoord);
//	}

	/**
	 * Add all other molecule (fragment) atoms and bonds to my self. Do not create a
	 * new bond or change the coordinates. all atomic and bond proipoerties are
	 * copied. The argument is not changed.
	 * 
	 * @param otherMol
	 */
	public void addOtherMolToMe(JMEmol otherMol) {
		int nn = natoms;
		for (int i = 1; i <= otherMol.natoms; i++) {
			createAtomFromOther(otherMol.atoms[i]);
			AN(natoms, otherMol.an(i));
		}
		for (int i = 1; i <= otherMol.nbonds; i++) {
			createAndAddBondFromOther(otherMol.bonds[i]); // create new bond and place it at the end: bonds[nbonds]
			bonds[nbonds].va = otherMol.bonds[i].va + nn;
			bonds[nbonds].vb = otherMol.bonds[i].vb + nn;
		}
	}

	// BB note: generic would not work with int or double
	/**
	 * 
	 * @param array
	 * @param newSize
	 * @return
	 */

	@Override
	protected Atom createAtomFromOther(Atom atomToDuplicate) {
		atomLabels = null;
		return super.createAtomFromOther(atomToDuplicate);
	}

	/**
	 * 
	 * @param s      CSV string with atom (or bond) and color indices
	 * @param        delta: ensemble vs mol correction, increment of atom/bon index
	 *               to move to another fragment
	 * @param isAtom
	 */
	public void setAtomOrBondColors(String s, int delta, boolean isAtom) {

		StringTokenizer st = new StringTokenizer(s, ",");
		try {
			while (st.hasMoreTokens()) {
				int atomOrBond = Integer.valueOf(st.nextToken()).intValue() - delta;
				int color = Integer.valueOf(st.nextToken()).intValue();
				if (isAtom)
					addAtomColor(atomOrBond, color);
				else {
					addBondColor(atomOrBond, color); // Sept 2019
				}
			}
		} catch (Exception e) {
			System.err.println("Error in atom coloring");
			JMEUtil.log("Error in atom coloring");
			// e.printStackTrace();
		}
	}

	public void setAtomColors(String s, int delta) {
		setAtomOrBondColors(s, delta, true);

	}

	public void setBondColors(String s, int delta) {
		setAtomOrBondColors(s, delta, false);
	}

	public void addAtomColor(int at, int c) {
		if (at > 0 && at <= natoms) {
			atoms[at].addBackgroundColor(c);
		}

	}

	public int[] getAtomBackgroundColors(int at) {
		return (at > 0 && at <= natoms ? atoms[at].getBackgroundColors() : null);
	}

	public void addBondColor(int b, int c) {
		if (b > 0 && b <= this.nbonds) {
			bonds[b].addBackgroundColor(c);
		}
	}

	public int[] getBondBackgroundColors(int b) {
		return (b > 0 && b <= nbonds ? bonds[b].getBackgroundColors() : null);
	}

	/**
	 * Create a copy of my self. This code uses PE original code from the save()
	 * method
	 * 
	 * @param src
	 * @return
	 */
	public JMEmol deepCopy() {
		return new JMEmol(this);
	}

	boolean isRotatableBond(int a1, int a2) {
		return minimumRingSize(a1, a2) == 0;
	}

	boolean isRotatableBond(int b) {
		return isRotatableBond(bonds[b].va, bonds[b].vb);
	}

	// ----------------------------------------------------------------------------

//	/**
//	 * SIDE EFFECT: set this.a[] !!!!!!!!!!!!!!!!!! what is this variable good for?
//	 * 
//	 * @param removeSmall
//	 * @return the number of fragments? * this code is not used anymore
//	 * 
//	 */
//	public int checkMultipart(boolean removeSmall) {
//		// group prislusnost da do a[]
//		int nparts = 0;
//		boolean ok = false;
//		a = new int[natoms + 1); // a is used by other
//
//		while (true) {
//			for (int j = 1; j <= natoms; j++)
//				if (a[j] == 0) {
//					a[j] = ++nparts;
//					ok = true;
//					break;
//				}
//			if (!ok)
//				break;
//			while (ok) {
//				ok = false;
//				for (int j = 1; j <= nbonds; j++) {
//					int atom1 = bonds[j].va;
//					int atom2 = bonds[j].vb;
//					if (a[atom1] > 0 && a[atom2] == 0) {
//						a[atom2] = nparts;
//						ok = true;
//					} else if (a[atom2] > 0 && a[atom1] == 0) {
//						a[atom1] = nparts;
//						ok = true;
//					}
//				}
//			}
//		}
//		if (nparts < 2 || !removeSmall)
//			return nparts;
//
//		// najde najvacsiu
//		int size[] = new int[nparts + 1);
//		for (int i = 1; i <= natoms; i++)
//			size[a[i]]++;
//		int max = 0, largest = 1;
//		for (int i = 1; i <= nparts; i++)
//			if (size[i] > max) {
//				max = size[i];
//				largest = i;
//			}
//		// removing smaller part(s)
//		for (int i = natoms; i >= 1; i--)
//			if (a[i] != largest)
//				deleteAtom(i);
//
//		center(); // aby sa nedostalo do trap za okraj
//		info("Smaller part(s) removed !");
//		return 1;
//	}

	// used in the tests, should not be used in JME
	public String createSmiles() {
		return createSmiles(parameters);
	}

	/**
	 * BB Create a smiles, does not affect my self, unlike the original
	 * implementation which needed the nocanonize option
	 * 
	 * @return smiles
	 */
	public String createSmiles(Parameters pars) {
		return JMESmiles.getSmiles(deepCopy(), pars, isQuery);
	}

	public int findAtomChargeForOutput(int atomIndex) {
		int charge = 0;
		if (atomIndex > 0 && atomIndex <= this.nAtoms()) {
			Atom atom = this.atoms[atomIndex];
			charge = atom.q();
		}
		return charge;
	}

	public boolean hasMarkedAtom() {
		for (int at = 1; at <= natoms; at++) {
			if (findAtomMapForOutput(at) > 0) {
				return true;
			}
		}

		return false;
	}

	// --------------------------------------------------------------------------
	/**
	 * Create a MOL or RXN. The paprameter header is usually the SMILES
	 * 
	 * @param header
	 * @return
	 */
	public String createMolFile(String header) {
		return JMEWriter.createMolFile((JMECore) this, header, true, computeCoordinate2DboundingBox());
	}

	// ----------------------------------------------------------------------------
	/**
	 * Change charge/Hydrogen count
	 * 
	 * @param atom
	 * @param type is always 0 in JME: means toggle the charge/hydrogen count
	 * @return
	 */
	public boolean changeCharge(int atom, int type) {
		// click with +/- on atom
		// 2002.05 --- pridana moznost C+
		// 2005.03 --- pridany type, moze byt 0 1 -1
		String np = "Charge change not possible on ";

		// for webme
		if (type == 1) {
			incrQ(atom, 1);
			return true;
		} else if (type == -1) {
			incrQ(atom, -1);
			return true;
		}

		int startCharge = q(atom);
		int startNH = atoms[atom].nh;
		// standard jme behaviour
		// int sbo = sumBondOrders(atom);
		int sbo = getSumOfBondOrder(atom);
		if (sbo == -1) { // query
			if (type == 0) {
				if (q(atom) == 0)
					Q(atom, 1);
				else if (q(atom) == 1)
					Q(atom, -1);
				else if (q(atom) == -1)
					Q(atom, 0);
			}
		}
		switch (an(atom)) {
		case Atom.AN_H:
			// Sept 2016: change charge cycle: 0 -> 1 -> -1 -> 0
			if (sbo == 0) {
				if (q(atom) == 0)
					Q(atom, 1);
				else if (q(atom) == 1)
					Q(atom, -1);
				else {
					Q(atom, 0);
				}
			}

			break;

		case Atom.AN_B:
			if (sbo > 2)
				info(np + "this boron !");
			if (q(atom) == 0)
				Q(atom, 1);
			else if (q(atom) == 1)
				Q(atom, 0);
			break;
		case Atom.AN_C:
			// case Atom.AN_SI:
			if (sbo > 3)
				info(np + "this carbon !");
			else if (sbo < 4) {
				if (q(atom) == 0)
					Q(atom, -1);
				else if (q(atom) == -1)
					Q(atom, 1);
				else if (q(atom) == 1)
					Q(atom, 0);
			}
			break;
		case Atom.AN_N:
		case Atom.AN_P:
			if (sbo > 3)
				info(np + "multibonded N or P !");
			else if (sbo == 3 && q(atom) == 0)
				Q(atom, 1);
			else if (sbo == 3 && q(atom) == 1)
				Q(atom, 0);
			else if (sbo < 3 && q(atom) == 0)
				Q(atom, 1);
			else if (sbo < 3 && q(atom) == 1)
				Q(atom, -1);
			else if (sbo < 3 && q(atom) == -1)
				Q(atom, 0);
			break;
		case Atom.AN_O: // -[O-] -O- =O -[O+]< >[O2+]< ...
		case Atom.AN_S: // asi sa na multivalent vykaslat
		case Atom.AN_SE:
			if (sbo > 2)
				info(np + "multibonded " + Atom.zlabel[an(atom)] + " !");
			else if (sbo == 2 && q(atom) == 0)
				Q(atom, 1);
			else if (sbo == 2 && q(atom) == 1)
				Q(atom, 0);
			else if (sbo < 2 && q(atom) == 0)
				Q(atom, -1);
			else if (sbo < 2 && q(atom) == -1)
				Q(atom, 1);
			else if (sbo < 2 && q(atom) == 1)
				Q(atom, 0);
			break;
		case Atom.AN_F:
		case Atom.AN_CL:
		case Atom.AN_BR:
		case Atom.AN_I:
			if (sbo == 0 && q(atom) == 0)
				Q(atom, -1);
			else if (sbo == 0 && q(atom) == -1)
				Q(atom, 0);
			else
				info(np + "the halogen !");
			break;
		case Atom.AN_X:
			info("Use X button to change charge on the X atom !");
			break;
		}

		if (Atom.chargedMetalType(an(atom)) > 0)
			if (!this.toggleChargeAndHydrogenCountOfMetalAtom(atoms[atom], sbo))
				info(np + Atom.zlabel[an(atom)]);

		return startCharge != q(atom) || startNH != atoms[atom].nh;
	}

	/**
	 * Change the charge and hydrogen count of a metal atom e.g.Na => Na+, Ca =>
	 * Ca++ works only for atom with only two oxydation states: 0 and >0 like Na
	 * 
	 * @param atom
	 * @param sbo  : sum of bond order of the atom
	 * @return
	 */
	boolean toggleChargeAndHydrogenCountOfMetalAtom(Atom atom, int sbo) {
		/*
		 * See also: atomDelete increases H count valenceState can decrease the charge
		 * and H count
		 */
		boolean changed = false;
		int maxMetalCharge = Atom.chargedMetalType(atom.an); // 1 for Na, 2 for Ca, 3 for Al
		if (maxMetalCharge > 0) {
			int maxChargeIncrease = maxMetalCharge - sbo;
			if (maxChargeIncrease > 0) {
				int q = atom.q;
				int nh = atom.nh;

				if (q + nh < maxChargeIncrease) { // Na=>Na+, Ca+ => Ca++, Al+ => Al3+,
					q += maxChargeIncrease - nh;
				} else if (q + nh == maxChargeIncrease) {
					if (q == maxChargeIncrease) {
						q = 0;
						nh = maxChargeIncrease;
					} // Na+ => NaH , Ca++ => CaH2, Ca+H => CaH2
					else { // nh == maxChargeIncrease
						q = 0;
						nh = 0; // NaH => Na, CaH2 = > Ca
						if (sbo == 0)
							info("Metallic " + Atom.zlabel[atom.an]);
					}

				}
				changed = (atom.q != q || atom.nh != nh);
				atom.q = q;
				atom.nh = nh;

			}
		}
		return changed;
	}

	public boolean markAtom(int newMark) {
		// Mark the touched atom

		// star marking of a part of a molecules
		if (parameters.mark) {
			if (jme.options.markOnly1) { // only one atom at a time can be marked
				for (int at = 1; at <= natoms; at++) {
					if (at != touchedAtom) {
						this.atoms[at].resetMark(); // if not done, the atoms with maps will still stay blue
					}
				}
			}
			if (newMark != this.atoms[touchedAtom].getMark()) {
				this.atoms[touchedAtom].setMark(newMark);
				return true;
			} else { // same color requested twice: toggle
				this.atoms[touchedAtom].resetMark();
				return false;
			}
		}

		boolean hasBeenMarked;

		// normal number marking
		// checking whether this atom is marked
		Atom touchedAtomObject = this.atoms[touchedAtom];
		if (newMark <= 0) {
			// removing this marking
			touchedAtomObject.resetMap();
			hasBeenMarked = false;
		} else {
			hasBeenMarked = newMark != touchedAtomObject.getMap(); // BB new 2020
			touchedAtomObject.setMap(newMark);
			hasBeenMarked = true;
		}
		return hasBeenMarked;

	}

	public boolean markBond(int newMark) {
		// duplicated logic with markAtom

		// star marking of a part of a molecules
		if (parameters.mark) {
			// doColoring = -1;
			// //assert(activeMarker == this.parameters.markerMultiColor);
			if (jme.options.markOnly1) { // only one atom at a time can be marked
				for (int at = 1; at <= nbonds; at++) {
					if (at != touchedBond) {
						this.bonds[at].resetMark(); // if not done, the atoms with maps will still stay blue
					}

				}
			}

			if (newMark != this.bonds[touchedBond].getMark()) {
				this.bonds[touchedBond].setMark(newMark);

				return true;
			} else { // same color requested twice: toggle
				this.bonds[touchedBond].resetMark();
				return false;
			}

		}

		return false;
	}

	public void info(String msg, int laFailed) {
		info(msg);
		jme.lastAction = laFailed;
	}

	public JMEmol compute2DcoordinatesIfMissing() {
		return (has2Dcoordinates() ? null : OclAdapter.compute2Dcoordinates(this));
	}

	public void clearRotation() {
		centerx = centery = Double.NaN;
	}

	public void rotate(double movex) {
		if (Double.isNaN(centerx)) {
			Box bbox = computeBoundingBoxWithAtomLabels(null);
			centerx = bbox.getCenterX();
			centery = bbox.getCenterY();
		}
		rotate(movex, centerx, centery);
	}

	public JMEmol reComputeBondOrderIfAromaticBondType() {
		return OclAdapter.reComputeBondOrderIfAromaticBondType(this);
	}

	/**
	 * JMEmol version translates action to bond type and adds GUI.TOUCH_LIMIT.
	 * @param action
	 * @param ia
	 * @param up
	 * @param forceLinear
	 * 
	 * @return true if up parameter was used
	 */
	public boolean addBondToAtom(int action, int ia, int up, boolean forceLinear) {
		int bondType = (action == Actions.ACTION_BOND_TRIPLE ? Bond.TRIPLE
				: action == Actions.ACTION_BOND_DOUBLE ? Bond.DOUBLE : Bond.SINGLE);
		Boolean b = super.addBondToAtom(bondType, ia, up, forceLinear, GUI.TOUCH_LIMIT);
		if (b == null)
			return false;
		// creating new bond with stereo tool
		if (action == Actions.ACTION_STEREO)
			toggleBondStereo(nbonds);
		xorg = x(natoms);
		yorg = y(natoms); // used after moving, when moving !OK
		return b.booleanValue();
	}

	public void cleanAfterChanged(boolean polarNitro) {
		setValenceState();
		cleanPolarBonds(polarNitro); // TODO: need parameter polarnitro
	}


	/// The following methods COULD Be in JMECore, but are not, because they are just the results of GUI actions. 
	
	/*
	 * Delete all atoms that have been marked for deletion (atom.deleteFlag == true)
	 */
	public void deleteAtomGroup() {
		while (true) {
			int atd = 0;
			for (int i = natoms; i >= 1; i--)
				if (atoms[i].deleteFlag && i > atd) {
					atd = i;
				}
			if (atd == 0)
				break;
			deleteAtom(atd);
			atoms[atd].deleteFlag = false;
		}
	}

	/**
	 * remove all coordination bonds. returns the number of bonds that have been
	 * removed Application: SMILES cannot represent them
	 */
	public int deleteCoordinationBonds() {
		int cbCount = 0;
		// BH 2023.01.29 delete from top down for no need for outer while loop
		for (int b = nbonds + 1; --b >= 1;) {
			if (bonds[b].bondType == Bond.COORDINATION) {
				deleteBond(b, false);
				cbCount++;
			}
		}
		return cbCount;
	}

	/**
	 * toggle the stereo status of the selected bond
	 * 
	 * @param bondIndex
	 */
	public void toggleBondStereo(int bondIndex) {
		// alebo vola z drawingArea.mouseDown s (touchBond) a bondType je rozna,
		// alebo z completeBond, vtedy je bondType vzdy 1
		// robi to inteligente, presmykuje medzi 4, len kde je to mozne
		// v stereob je uschovane aj querytype ked ide o Bond.QUERY bond

		Bond bond = this.bonds[bondIndex];
		if (bond.isSingle() || bond.isCoordination()) { // accept coordination bond with stereo
			// Bond.UP a Bond.DOWN daju hrot na va[], Bond.XUP, Bond.XDOWN na vb[]
			int atom1 = bonds[bondIndex].va;
			int atom2 = bonds[bondIndex].vb;
			if (nv(atom1) < 2 && nv(atom2) < 2) { // <=2 nemoze byt kvoli allenu
				bond.stereo = 0;
				info("Stereomarking meaningless on this bond !");
				return;
			}
			// atom1 - stary, atom2 - novy atom
//			if (JME.webme) {
//				// handling webme (up / down templates)
//				// just switching up/xup and down/xdown
//				if (!jme.revertStereo) {
//					if (bond.stereo == Bond.STEREO_UP)
//						bond.stereo = Bond.STEREO_XUP;
//					else if (bond.stereo == Bond.STEREO_XUP)
//						bond.stereo = Bond.STEREO_UP;
//					else {
//						if (nv(atom2) <= nv(atom1))
//							bond.stereo = Bond.STEREO_UP;
//						else
//							bond.stereo = Bond.STEREO_XUP;
//					}
//				} else {
//					if (bond.stereo == Bond.STEREO_DOWN)
//						bond.stereo = Bond.STEREO_XDOWN;
//					else if (bond.stereo == Bond.STEREO_XDOWN)
//						bond.stereo = Bond.STEREO_DOWN;
//					else {
//						if (nv(atom2) <= nv(atom1))
//							bond.stereo = Bond.STEREO_DOWN;
//						else
//							bond.stereo = Bond.STEREO_XDOWN;
//					}
//				}
//			}
//
			// standard editor stuff
			switch (bond.stereo) {
			case 0: // aby bol hrot spravne (nie na nerozvetvenom)
				// Bond.UP dava normalne hrot na va[]
				if (nv(atom2) <= nv(atom1))
					bond.stereo = Bond.STEREO_UP;
				else
					bond.stereo = Bond.STEREO_XUP;
				break;
			case Bond.STEREO_UP:
				bond.stereo = Bond.STEREO_DOWN;
				break;
			case Bond.STEREO_DOWN:
				bond.stereo = Bond.STEREO_EITHER;
				break;
			case Bond.STEREO_EITHER:
				if (nv(atom2) > 2)
					bond.stereo = Bond.STEREO_XUP;
				else
					bond.stereo = Bond.STEREO_UP;
				break;
			case Bond.STEREO_XUP:
				bond.stereo = Bond.STEREO_XDOWN;
				break;
			case Bond.STEREO_XDOWN:
				bond.stereo = Bond.STEREO_XEITHER;
				break;
			case Bond.STEREO_XEITHER:
				if (nv(atom1) > 2)
					bond.stereo = Bond.STEREO_UP;
				else
					bond.stereo = Bond.STEREO_XUP;
				break;

			}
		} else if (bond.bondType == Bond.DOUBLE) {
			bond.toggleNormalCrossedDoubleBond();
			// if (bond.stereo == Bond.EZ) bond.stereo = 0; else bond.stereo = Bond.EZ;
		} else {
			info("Stereomarking allowed only on single and double bonds!");
		}
	}

	/**
	 * Warning: side effect: the order of the atoms is changed turned off
	 */
	public void numberAtomsSequentially() {
		for (int i = 1; i <= natoms; i++) {
			this.atoms[i].setMap(i);
		}
	}


	private void setRingInfo() {
		ringInfo = new GUI.RingInfo(this);
	}
	
	@Override
	public void setBondCenters() {
		ringInfo = null;
		haveDoubleBonds = false;
		super.setBondCenters();
	}

	@Override
	public void setBondCenter(Bond b) {
		super.setBondCenter(b);
		if (b.bondType == Bond.DOUBLE)
			haveDoubleBonds = true;
	}



}
