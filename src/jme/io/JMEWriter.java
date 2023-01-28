package jme.io;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import jme.JME;
import jme.JMEUtil;
import jme.JMEmol;
import jme.core.Atom;
import jme.core.AtomicElements;
import jme.core.Bond;
import jme.core.Box;
import jme.core.JMECore;

public class JMEWriter extends JMECore {

		public static String createJME(JMECore mol, boolean addHydrogens, Box box) {
			return new JMEWriter(mol).createJME(addHydrogens, box);
		}

		public static String createMolFile(JMECore mol, String header, boolean stampDate, Box box) {
			return new JMEWriter(mol).createMolFile(header, stampDate, box);
		}
		
		public static String createExtendedMolFile(JMEmol mol, String header, boolean stampDate, Box box) {
			return new JMEWriter(mol).createExtendedMolFile(header, stampDate, box);
		}

		public JMEWriter(JMECore mol) {
			super(mol);
		}

		/**
		 * Center and scale back the bond length to about 1.4 because JSME has rescaled
		 * the bond to RBOND (internalBonsScaling) When reading a MOL, the y coordinated
		 * were inverted, thus invert back y coordinate to match ISISDraw coord system
		 * 
		 * if the argument is null, then the boundingBox of the molecule will be
		 * computed. Using this argument might be useful for a JMEmolList for which each
		 * molecules are recentered globally, for the ensemble
		 */
		public void transformAtomCoordinatesForOutput(Box boundingBox) {

			// atoms
			double scale = 1.4 / JMECore.RBOND; // ??? co je standard scale ???
			// where is this coming from?
			
			if (boundingBox == null)
				boundingBox = computeCoordinate2DboundingBox();

			if (boundingBox == null) { // h appens when they are no atoms
				return;
			}

			double ymax = boundingBox.y + boundingBox.height;
			double xmin = boundingBox.x;

			// this is used for the test suite or non GUI applications
			if (this.parameters.keepSameCoordinatesForOutput) {
				xmin = 0.0;
				ymax = 0.0;
				scale = 1.0;
			}

			for (int i = 1; i <= natoms; i++) {
				Atom atom = atoms[i];
				atom.x = (atom.x - xmin) * scale; // center and scale
				atom.y = (ymax - atom.y) * scale; // inverts y coordinate to match ISISDraw coord system
			}

		}

		// ----------------------------------------------------------------------------
		/**
		 * Crate a representation in JME format
		 * 
		 * @return
		 */
		public String createJME(boolean addHydrogens, Box box) {
			String s = "" + natoms + " " + nbonds;

			this.transformAtomCoordinatesForOutput(box);

			for (int i = 1; i <= natoms; i++) {
				Atom atom = this.atoms[i];
				int iso = atom.iso;
				String sa = "";
				if (iso != 0) {
					sa += iso;
				}
				sa += atom.getLabel();
				if ((addHydrogens || getValenceForMolOutput(i) > 0) && atom.nh > 0) { 
					// aj pre C
					sa += "H";
					if (atom.nh > 1)
						sa += atom.nh;
				}
				if (atom.q != 0) {
					if (atom.q > 0)
						sa += "+";
					else
						sa += "-";
					if (Math.abs(atom.q) > 1)
						sa += Math.abs(atom.q);
				}
				int map = findAtomMapForOutput(i);
				if (map != 0)
					sa += ":" + map;
				s += " " + sa + " " + JMEUtil.fformat(atom.x, 0, 2) + " " + JMEUtil.fformat(atom.y, 0, 2);
			}
			int d;
			for (int i = 1; i <= nbonds; i++) {
				int a1 = bonds[i].va, a2 = bonds[i].vb, nas = bonds[i].bondType;
				int stereo = bonds[i].stereo;
				if (bonds[i].bondType == Bond.QUERY) {
					nas = stereo;
				} else {
					switch (stereo) {
					case Bond.STEREO_UP:
						nas = -1;
						break;
					case Bond.STEREO_DOWN:
						nas = -2;
						break;
					case Bond.STEREO_XUP:
					case Bond.STEREO_XDOWN:
						nas = (stereo == Bond.STEREO_XUP ? -1 : -2);
						d = a1;
						a1 = a2;
						a2 = d;
						break;
					case Bond.STEREO_EZ:
						nas = -5;
						break;
					}
				}
				s += " " + a1 + " " + a2 + " " + nas;
			}
			return s;
		}



		/**
		 * Create a MOL or RXN. The parameter header is usually the SMILES stampdate is
		 * set to false for the tests
		 * 
		 * @param header
		 * @param box 
		 * @return
		 */
		public String createMolFile(String header, boolean stampDate, Box box) {
			// LP complained about this - v300 does not do it
			// if (natoms == 0) return ""; // 2008.12

			String s = mdlHeaderLines(header, stampDate, false);
			// this.parameters;

			// atoms
			// where is this coming from?

			this.transformAtomCoordinatesForOutput(box);

			for (int i = 1; i <= natoms; i++) {
				Atom atom = atoms[i];
				s += JMEUtil.fformat(atom.x, 10, 4) + JMEUtil.fformat(atom.y, 10, 4) + JMEUtil.fformat(0.0, 10, 4);
				String z = getAtomLabel(i);

				if (z.length() == 1)
					z += "  ";
				else if (z.length() == 2)
					z += " ";
				else if (z.length() > 3)
					z = "Q  "; // query ???
				s += " " + z;

				// isotope, charge
				int charge = 0;
				if (q(i) > 0 && q(i) < 4)
					charge = 4 - q(i);
				else if (q(i) < 0 && q(i) > -4)
					charge = 4 - q(i);
				// BB
				int deltaIsotop = 0;
				if (atom.iso != 0) {
					int delta = AtomicElements.getDeltaIsotopicMassOfElement(getAtomLabel(i), this.atoms[i].iso);
					if (delta >= -3 && delta <= 4) {
						deltaIsotop = delta;
					}
				}

				// BB: valence field vv
				int vv = 0; // no markings

				// if(atom.nh>0) {
				// vv = atom.nh+atom.nv; //ChemWriterr OK
				// } else {
				// //metal like [Al]
				// if(q(i) == 0 && JME.chargedMetalType(atom.an) !=0) {
				// // Datawarrior does not recognise it correctly
				// //ChemWriter shows an error
				// //Marvin JS, ChemDoodle ignores this field
				// vv = 15; //explicit 0 valence
				// }
				// }
				vv = getValenceForMolOutput(i);

				z = JMEUtil.iformat(deltaIsotop, 2) + JMEUtil.iformat(charge, 3) + "  0" + JMEUtil.iformat(vv, 3) + "  0  0  0  0  0";
				int map = this.findAtomMapForOutput(i);
				z += JMEUtil.iformat(map, 3);
				s += z + "  0  0" + "\n";

			}
			// bonds
			for (int i = 1; i <= nbonds; i++) {
				s += getMOLStereoBond(this.bonds[i], false) + "\n";
				;
			}

			// charges on standard atoms
			for (int i = 1; i <= natoms; i++) {
				if (q(i) != 0) {
					s += "M  CHG  1" + JMEUtil.iformat(i, 4) + JMEUtil.iformat(q(i), 4) + "\n";
				}

				// ISO BB
				if (this.atoms[i].iso != 0) {
					s += "M  ISO  1" + JMEUtil.iformat(i, 4) + JMEUtil.iformat(this.atoms[i].iso, 4) + "\n";
				}
			}

			// radical (X atoms)
			/*
			 * for (int i=1;i<=nradicals;i++) { if (an[i] == Atom.AN_X) { s += "M  RAD  1" +
			 * iformat(radical[i],4) + iformat(2,4) + "\n"; } }
			 */

			s += "M  END" + "\n";
			return s;
		}

		int getValenceForMolOutput(int at) {
			// Provide valence count only for metals that are handled yet by JME

			int val = 0;
			Atom atom = atoms[at];

			if (atom.nh > 0 && Atom.chargedMetalType(atom.an) != 0) {
				val = atom.nh + atom.nv;
			}

			return val;
		}

		// ----------------------------------------------------------------------------
		String createExtendedMolFile2(String smiles) {
			return createExtendedMolFile(smiles, true, null);
		}

		// ----------------------------------------------------------------------------
		// Molfile V3000 - 2006.09
		public String createExtendedMolFile(String header, boolean stampDate, Box box) {

			// int nradicals = 0;
			// int[] radical = new int[natoms+1);
			// finding whether molecule is chiral

			String s = mdlHeaderLines(header, stampDate, true);

			// int chiral = 0;
			// for (int i=1;i<=nbonds;i++) if (bonds[i].stereo != 0) {chiral = 1; break;}

			String mv30 = "M  V30 ";
			// header
			s += mv30 + "BEGIN CTAB" + "\n";
			// at the end should be chiral flag 1 or 0
			s += mv30 + "COUNTS " + natoms + " " + nbonds + " 0 0 " + mdlChiralFlag() + "\n";
			s += mv30 + "BEGIN ATOM" + "\n";
			// atoms
			this.transformAtomCoordinatesForOutput(box);

			for (int i = 1; i <= natoms; i++) {
				Atom atom = atoms[i];
				s += mv30;
				// inverts y coordinate to match ISISDraw coord system
				s += i + " " + getAtomLabel(i);
				int m = this.findAtomMapForOutput(i);
				s += " " + JMEUtil.fformat(atom.x, 0, 4) + " " + JMEUtil.fformat(atom.y, 0, 4) + " " + JMEUtil.fformat(0.0, 0, 4) + " " + m;
				if (atom.q != 0)
					s += " CHG=" + atom.q;
				// TODO JME currently ignoring isotopes & radicals
				if (atom.iso > 0) { // MAy 2020
					s += " MASS=" + atom.iso;
				}

				int val = getValenceForMolOutput(i);
				if (val != 0)
					s += " VAL=" + val;

				s += "\n";

				// TODO: isotopoe handling?
			}
			s += mv30 + "END ATOM" + "\n";
			s += mv30 + "BEGIN BOND" + "\n";
			// bonds
			for (int i = 1; i <= nbonds; i++) {
				s += mv30 + i + " " + getMOLStereoBond(this.bonds[i], true) + "\n";
			}
			s += mv30 + "END BOND" + "\n";

			// stereo collections
			// MDLV30/STEABS /STERACn /STERELn
			ArrayList<Integer> abs = new ArrayList<Integer>();
			ArrayList<ArrayList<Integer>> orlists = new ArrayList<ArrayList<Integer>>();
			ArrayList<ArrayList<Integer>> mixlists = new ArrayList<ArrayList<Integer>>();
			for (int i = 0; i < 10; i++) {
				orlists.add(null);
				mixlists.add(null);
			}
			for (int i = 1; i <= natoms; i++) {
				String atag = atoms[i].atag;
				if (atag == null || atag.length() == 0)
					continue;
				if (atag.equals("abs"))
					abs.add(new Integer(i));
				else if (atag.startsWith("mix")) {
					int n = Integer.parseInt(atag.substring(3));
					ArrayList<Integer> o = (mixlists.size() > n ? mixlists.get(n) : null);
					ArrayList<Integer> l = (o == null ? new ArrayList<Integer>() : o);
					l.add(new Integer(i));
					mixlists.set(n, l);
				} else if (atag.startsWith("or")) {
					int n = Integer.parseInt(atag.substring(2));
					ArrayList<Integer> o = (orlists.size() > n ? orlists.get(n) : null);
					ArrayList<Integer> l = (o == null ? new ArrayList<Integer>() : o);
					l.add(new Integer(i));
					orlists.set(n, l);
				}
			}
			s += appendMOLCollection("MDLV30/STEABS", abs, mv30);
			if (orlists.size() > 0)
				for (int i = 1; i < orlists.size(); i++)
					s += appendMOLCollection("MDLV30/STEREL" + i, orlists.get(i), mv30);
			if (mixlists.size() > 0)
				for (int i = 1; i < mixlists.size(); i++)
					s += appendMOLCollection("MDLV30/STERAC" + i, mixlists.get(i), mv30);

			s += mv30 + "END CTAB" + "\n";
			s += "M  END" + "\n";
			return s;
		}

		static String appendMOLCollection(String name, ArrayList<Integer> list, String mv30) {
			if (list == null || list.size() == 0)
				return "";
			String s = "";
			s += mv30 + "BEGIN COLLECTION" + "\n";
			s += mv30 + name + " [ATOMS=(" + list.size();
			for (Iterator<Integer> i = list.iterator(); i.hasNext();)
				s += " " + i.next();
			s += ")]" + "\n";
			s += mv30 + "END COLLECTION" + "\n";
			return s;
		}

		/**
		 * 
		 * @param header
		 * @param stampDate ignored; assumed true -- we WILL put the MDL date stamp in
		 * @param isV3000
		 * @return
		 */
		protected String mdlHeaderLines(String header, boolean stampDate, boolean isV3000) {
			int writeChiralFlag = isV3000 ? 0 : mdlChiralFlag(); // for V3000, the chiral flag is set in the COUNTS line

			// BH 2023.15 not allowing first line to be blank or spaces only, as that can be
			// clipped improperly
			String s = (header != null && (header = header.trim()).length() > 0 ? header : "_");
			if (s.length() > 79)
				s = s.substring(0, 76) + "...";
			s += "\n";
			// since 2006.01 added one space to header line (two newlines causes problems in
			// tokenizer)

			String versiondate = "JME" + JME.version + " " + new Date();

			// the 2D/3D dimension is critical and required
			// if(stampDate) {
			s += getSDFDateLine("JME" + JME.version) + "\n";
//			} else {
//				s += "\n";
//			}
			s += versiondate + "\n";
			// counts line
			s += JMEUtil.iformat(isV3000 ? 0 : natoms, 3) + JMEUtil.iformat(isV3000 ? 0 : nbonds, 3);
			s += "  0  0" + JMEUtil.iformat(writeChiralFlag, 3) + "  0  0  0  0  0999 " + (isV3000 ? "V3000" : "V2000")
					+ "\n";

			return s;

		}

		/**
		 * Generate a string for the bond. For v3000 the M 30 and the bond index must be
		 * added
		 * 
		 * @param bond
		 * @param isV3000
		 * @return
		 */
		protected static String getMOLStereoBond(Bond bond, boolean isV3000) {

			int bondType = bond.bondType;
			int a1 = bond.va;
			int a2 = bond.vb;
			// int nas = bondType; //whatis nas?
			// if (isSingle(i)) nas=1; else if (isDouble(i)) nas=2;
			int stereo = 0;

			/*
			 * V2000 Single bonds: 0 = not stereo, 1 = Up, 4 = Either, 6 = Down, Double
			 * bonds: 0 = Use x-, y-, z-coords from atom block to determine cis or trans, 3
			 * = Cis or trans (either) double bond
			 * 
			 */
			if (bondType == Bond.SINGLE || bondType == Bond.COORDINATION) {
				switch (bond.stereo) {
				case Bond.STEREO_UP:
				case Bond.STEREO_XUP:
					stereo = 1;
					break;
				case Bond.STEREO_DOWN:
				case Bond.STEREO_XDOWN:
					stereo = 6;
					break;
				case Bond.STEREO_EITHER:
				case Bond.STEREO_XEITHER:
					stereo = 4;
				}
				// swap atom labels if needed
				switch (bond.stereo) {
				case Bond.STEREO_XUP:
				case Bond.STEREO_XDOWN:
				case Bond.STEREO_XEITHER:
					int t = a1;
					a1 = a2;
					a2 = t;

				}
			}
			/*
			 * V3000 0 = none (default) 1 = up 2 = either 3 = down
			 * 
			 */
			if (bondType == Bond.DOUBLE && bond.stereo == Bond.STEREO_EZ) {
				stereo = 3; // 3 = Cis or trans (either) double bond
			}

			// convert v2000 to v3000 conventions
			if (isV3000) {
				switch (stereo) {
				case 6:
					stereo = 3;
					break;
				case 4:
				case 3:
					stereo = 2;

				}
			}

			if (bondType == Bond.COORDINATION) {
				bondType = 8; // MDL CT does not support coordination, convention used here is the same as in
								// other software, officialy 8 means any
			} else if (bondType == Bond.QUERY || bondType == Bond.AROMATIC) { // Not sure about this convention, see test case
																	// testReadRXNwithTwoAgents
				bondType = 4;
			}
			String bonds;
			if (isV3000) {
				bonds = bondType + " " + a1 + " " + a2;
				if (stereo != 0)
					bonds += " CFG=" + stereo;
			} else {
				bonds = JMEUtil.iformat(a1, 3) + JMEUtil.iformat(a2, 3) + JMEUtil.iformat(bondType, 3) + JMEUtil.iformat(stereo, 3) + "  0  0  0";
			}

			return bonds;
		}

		protected int mdlChiralFlag() {
			return getChiralFlag() && canBeChiral() ? 1 : 0;
		}

		// ----------------------------------------------------------------------------
		/**
		 * See CTFile -- this line is NOT optional. It is critical in showing whether we
		 * have a 2D or 3D MOL file.
		 * 
		 * @param version
		 * @param is2d
		 * @return SDF header line 2 with no \n
		 */
		public static String getSDFDateLine(String version) {
			String mol = (version + "         ").substring(0, 10);
			int cMM, cDD, cYYYY, cHH, cmm;
			/**
			 * for convenience only, no need to invoke Calendar for this simple task.
			 * 
			 * @j2sNative
			 * 
			 * 			var c = new Date(); cMM = c.getMonth(); cDD = c.getDate(); cYYYY =
			 *            c.getFullYear(); cHH = c.getHours(); cmm = c.getMinutes();
			 */
			{
				Calendar c = Calendar.getInstance();
				cMM = c.get(Calendar.MONTH);
				cDD = c.get(Calendar.DAY_OF_MONTH);
				cYYYY = c.get(Calendar.YEAR);
				cHH = c.get(Calendar.HOUR_OF_DAY);
				cmm = c.get(Calendar.MINUTE);
			}
			mol += JMEUtil.rightJustify("00", "" + (1 + cMM));
			mol += JMEUtil.rightJustify("00", "" + cDD);
			mol += ("" + cYYYY).substring(2, 4);
			mol += JMEUtil.rightJustify("00", "" + cHH);
			mol += JMEUtil.rightJustify("00", "" + cmm);
			mol += "2D 1   1.00000     0.00000     0";
			// This line has the format:
			// IIPPPPPPPPMMDDYYHHmmddSSssssssssssEEEEEEEEEEEERRRRRR
			// A2<--A8--><---A10-->A2I2<--F10.5-><---F12.5--><-I6->
			return mol;
		}

	}