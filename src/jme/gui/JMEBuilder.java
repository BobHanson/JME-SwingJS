package jme.gui;

import jme.BondDirection;
import jme.JME;
import jme.JMEmol;
import jme.JME.SupportedFileFormat;
import jme.core.JMECore.Parameters;
import jme.core.Atom;
import jme.core.Bond;
import jme.core.JMECore;

public class JMEBuilder {

	//from JME: 
	public static final int TOUCH_LIMIT = GUI.TOUCH_LIMIT; // 50 pixels
    public static final int LA_FAILED = JME.LA_FAILED;
	
	/**
	 * spiroAdding set in JME, but only used here; alternatively, just MOUSE_SHIFT
	 * will do spiro ring addition.
	 */
	public boolean spiroAdding = false;

	private JME jme;
	private JMEmol mol;
	private Atom[] atoms;
	private Bond[] bonds;
	private int touchedAtom;
	private int touchedBond;
	private int action;
	private boolean spiroMode;
	private JMEmol templateMolecule;
	private String templateString;
	private boolean linearAdding;

	public JMEBuilder(JME jme) {
		this.jme = jme;
	}

	public JMEBuilder set(JMEmol mol, int action, boolean spiroMode) {
		if (mol != null) {
			// just the essentials
			this.mol = mol;
			this.atoms = mol.atoms;
			this.bonds = mol.bonds;
			this.touchedAtom = mol.touchedAtom;
			this.touchedBond = mol.touchedBond;
			this.action = action;
			this.spiroMode = spiroMode; // SHIFT_LEFT mouse
		}
		return this;
	}

	public void addBond() {
		mol.addBondToAtom(touchedAtom, 0, linearAdding || action == Actions.ACTION_BOND_TRIPLE, 
				action == Actions.ACTION_BOND_DOUBLE);
		bonds = mol.bonds;
	}

	public void addRing() {
		// adding ring atoms
		// (bonds are added in completeRing)
		int atom1, atom2, atom3;
		double dx, dy, rx, sina, cosa, xx, yy;
		double diel, rc, uhol, xstart, ystart;
		int returnTouch = -1; // stopka pridavanie

		int nmembered = 6;
		switch (action) {
		case Actions.ACTION_RING_3:
			nmembered = 3;
			break;
		case Actions.ACTION_RING_4:
			nmembered = 4;
			break;
		case Actions.ACTION_RING_5:
		case Actions.ACTION_RING_FURANE:
		case Actions.ACTION_RING_3FURYL:
			nmembered = 5;
			break;
		case Actions.ACTION_RING_6:
		case Actions.ACTION_RING_PH:
			nmembered = 6;
			break;
		case Actions.ACTION_RING_7:
			nmembered = 7;
			break;
		case Actions.ACTION_RING_8:
			nmembered = 8;
			break;
		case Actions.ACTION_RING_9:
			nmembered = 9;
			break;
		}

		diel = Math.PI * 2. / nmembered;
		rc = Math.sqrt(JMECore.RBOND * JMECore.RBOND / 2. / (1. - Math.cos(diel)));
		Atom a = atoms[touchedAtom];
		Bond b = bonds[touchedBond];
		if (touchedAtom > 0) {
			// --- adding ring at the end of the bond
			if (a.nv < 2) {
				addRingToBond(nmembered, diel, rc);
			} else if (spiroMode || spiroAdding) {
				spiroAdding = false;
				// checking whether can do spiro
				if (action == Actions.ACTION_RING_PH || action == Actions.ACTION_RING_FURANE || action == Actions.ACTION_RING_3FURYL) {
					mol.info("ERROR - cannot add aromatic spiro ring !", LA_FAILED);
					return;
				}
				for (int i = 1; i <= a.nv; i++) {
					// int bo = bondType[bondIdentity(touchedAtom,v(touchedAtom)[i])];
					int bo = mol.getBond(touchedAtom, a.v[i]).bondType;
					if (i > 2 || bo != Bond.SINGLE) {
						mol.info("ERROR - spiro ring not possible here !", LA_FAILED);
						return;
					}
				}
				// --- adding spiro ring
				double[] newPoint = new double[2];
				mol.getNewPoint(touchedAtom, rc, newPoint);
				dx = a.x - newPoint[0];
				dy = a.y - newPoint[1];
				rx = Math.sqrt(dx * dx + dy * dy);
				if (rx < 0.001)
					rx = 0.001;
				sina = dy / rx;
				cosa = dx / rx;
				for (int i = 1; i <= nmembered; i++) {
					Atom newAtom = createAtom();
					uhol = diel * i + Math.PI * .5;
					JMECore.XY(newAtom, newPoint[0] + rc * (Math.sin(uhol) * cosa - Math.cos(uhol) * sina),
							newPoint[1] + rc * (Math.cos(uhol) * cosa + Math.sin(uhol) * sina));
				}
			} else {
				// adding bond and ring
				returnTouch = touchedAtom;
				jme.lastAction = JME.LA_BOND;
				addBond();
				touchedAtom = mol.natoms;
				addRingToBond(nmembered, diel, rc);
			}

		}

		// fusing ring
		else if (touchedBond > 0) {
			int revert;
			atom1 = b.va;
			atom2 = b.vb;
			Atom a1 = atoms[atom1];
			Atom a2 = atoms[atom2];
			// hlada ref. atom atom3
			atom3 = 0;
			if (a1.nv == 2) {
				if (a1.v[1] != atom2)
					atom3 = a1.v[1];
				else
					atom3 = a1.v[2];
			} else if (a2.nv == 2) {
				if (a2.v[1] != atom1)
					atom3 = a2.v[1];
				else
					atom3 = a2.v[2];
				revert = atom1;
				atom1 = atom2;
				atom2 = revert; // atom3 on atom1
			}
			if (atom3 == 0) // no clear reference atom
				if (a1.v[1] != atom2)
					atom3 = a1.v[1];
				else
					atom3 = a1.v[2];

			dx = a2.x - a1.x;
			dy = a2.y - a1.y;
			rx = Math.sqrt(dx * dx + dy * dy);
			if (rx < 0.001)
				rx = 0.001;
			sina = dy / rx;
			cosa = dx / rx;
			xx = rx / 2.;
			yy = rc * Math.sin((Math.PI - diel) * .5);
			revert = 1;
			Atom a3 = atoms[atom3];
			if (((a3.y - a1.y) * cosa - (a3.x - a1.x) * sina) > 0.) {
				yy = -yy;
				revert = 0;
			}
			xstart = a1.x + xx * cosa - yy * sina;
			ystart = a1.y + yy * cosa + xx * sina;
			for (int i = 1; i <= nmembered; i++) {
				Atom newAtom = createAtom();
				uhol = diel * (i + .5) + Math.PI * revert;
				// x[natoms]=xstart+rc*(Math.sin(uhol)*cosa-Math.cos(uhol)*sina);
				// y[natoms]=ystart+rc*(Math.cos(uhol)*cosa+Math.sin(uhol)*sina);
				JMECore.XY(newAtom, xstart + rc * (Math.sin(uhol) * cosa - Math.cos(uhol) * sina),
						ystart + rc * (Math.cos(uhol) * cosa + Math.sin(uhol) * sina));
				// next when fusing to the "long" bond
				if (revert == 1) {
					if (i == nmembered) {
						JMECore.XY(newAtom, a1.x, a1.y);
						/* x(natoms)=x; y(natoms)=y; */}
					if (i == nmembered - 1) {
						JMECore.XY(newAtom, a2.x, a2.y);
						/* x[natoms]=x(atom2); y[natoms]=y(atom2); */}
				} else {
					if (i == nmembered - 1) {
						JMECore.XY(newAtom, a1.x, a1.y);
						/* x[natoms]=x(atom1); y[natoms]=y(atom1); */}
					if (i == nmembered) {
						JMECore.XY(newAtom, a2.x, a2.y);
						/* x[natoms]=x(atom2); y[natoms]=y(atom2); */}
				}
			}
		}

		// new ring in free space
		else {
			double helpv = 0.5;
			if (nmembered == 6)
				helpv = 0.;
			for (int i = 1; i <= nmembered; i++) {
				Atom newAtom = createAtom();
				uhol = diel * (i - helpv);
				// x[natoms] = xorg + rc*Math.sin(uhol);
				// y[natoms] = yorg + rc*Math.cos(uhol);
				JMECore.XY(newAtom, mol.xorg + rc * Math.sin(uhol), mol.yorg + rc * Math.cos(uhol));
			}
		}

		completeRing(nmembered);
		// a aby to bolo uz po mouse down OK
		checkRing(nmembered);
		// po check ring (inak nerobi avoid), pri stopke pridavani
		if (returnTouch > -1)
			touchedAtom = returnTouch;
	}

// ----------------------------------------------------------------------------
	void addRingToBond(int nmembered, double diel, double rc) {
		double sina, cosa, dx, dy, rx, uhol;
		int atom1 = 0;
		Atom a = atoms[touchedAtom];
		if (a.nv == 0) {
			sina = 0.;
			cosa = 1.;
		} else {
			atom1 = a.v[1];
			dx = a.x - mol.x(atom1);
			dy = a.y - mol.y(atom1);
			rx = Math.sqrt(dx * dx + dy * dy);
			if (rx < 0.001)
				rx = 0.001;
			sina = dy / rx;
			cosa = dx / rx;
		}
		double xstart = a.x + rc * cosa;
		double ystart = a.y + rc * sina;
		for (int i = 1; i <= nmembered; i++) {
			Atom newAtom = createAtom();
			uhol = diel * i - Math.PI * .5;
			// x[natoms]=xstart+rc*(Math.sin(uhol)*cosa-Math.cos(uhol)*sina);
			// y[natoms]=ystart+rc*(Math.cos(uhol)*cosa+Math.sin(uhol)*sina);
			newAtom.XY(xstart + rc * (Math.sin(uhol) * cosa - Math.cos(uhol) * sina),
					ystart + rc * (Math.cos(uhol) * cosa + Math.sin(uhol) * sina));
		}
	}

// ----------------------------------------------------------------------------
	void completeRing(int nmembered) {
		// adding bonds between ring atoms
		int atom = 0, atom3;
		Bond b = null;
		for (int i = 1; i <= nmembered; i++) {
			b = createAndAddNewBond();
			// bondType[nbonds]=Bond.SINGLE; // is single is the deault of createBond()
			atom = mol.natoms - nmembered + i;
			mol.NV(atom, 2); // set number of neighbors to 2
			b.va = atom;
			b.vb = atom + 1; // setup the new bond between atom and atom + 1
		}
		b.vb = mol.natoms - nmembered + 1; // close the ring
		b = bonds[touchedBond];
		// alternating double bonds for phenyl and furane template
		// 2007.12 fixed problematic adding
		if (action == Actions.ACTION_RING_PH) {
			setBonds(Bond.DOUBLE, Bond.SINGLE, Bond.DOUBLE, Bond.SINGLE, Bond.DOUBLE);
			if (touchedBond > 0) {
				if (b.isSingle()) {
					// fancy stuff - fusing two phenyls by single bond
					atom3 = 0;
					if (mol.nv(b.va) > 1) {
						atom3 = mol.v(b.va)[1];
						atom = b.va;
						if (atom3 == b.vb)
							atom3 = mol.v(b.va)[2];
					}
					if (atom3 == 0 && mol.nv(b.vb) > 1) {
						atom3 = mol.v(b.vb)[1];
						atom = b.vb;
						if (atom3 == b.vb)
							atom3 = mol.v(b.vb)[2];
					}
					if (atom3 > 0) {
						// checking if bond atom3-atom is multiple
						for (int i = 1; i <= mol.nbonds; i++) {
							if (bonds[i].isAB(atom, atom3)) {
								if (!bonds[i].isSingle()) {
									setBonds(Bond.DOUBLE, Bond.SINGLE, Bond.DOUBLE, Bond.SINGLE, Bond.TRIPLE,
											Bond.SINGLE);
								}
								break;
							}
						}
					}
				} else {
					setBonds(Bond.DOUBLE, Bond.SINGLE, Bond.DOUBLE, Bond.SINGLE, Bond.DOUBLE, Bond.SINGLE);
				}
			}
		} else if (action == Actions.ACTION_RING_FURANE || action == Actions.ACTION_RING_3FURYL) {
			if (touchedBond > 0) {
				if (b.bondType == Bond.SINGLE) {
					Atom va = atoms[b.va];
					Atom vb = atoms[b.vb];
					// nned to check whether it is not =C-C= bond
					boolean isConjugated = false;
					for (int i = 1; i <= va.nv; i++) {
						if (mol.getBond(b.va, va.v[i]).bondType > Bond.SINGLE) {
							isConjugated = true;
							break;
						}
					}
					for (int i = 1; i <= mol.nv(b.vb); i++) {
						int ax = vb.v[i];
						if (mol.getBond(b.vb, ax).bondType > Bond.SINGLE) {
							isConjugated = true;
							break;
						}
					}
					if (!isConjugated)
						b.bondType = Bond.DOUBLE;
				}
				bonds[mol.nbonds - 4].bondType = Bond.DOUBLE;
				mol.AN(mol.natoms - 2, Atom.AN_O);
			} else if (touchedAtom > 0) {
				if (action == Actions.ACTION_RING_FURANE) {
					setBonds(Bond.SINGLE, Bond.DOUBLE, Bond.SINGLE, Bond.SINGLE, Bond.DOUBLE);
					mol.AN(mol.natoms - 1, Atom.AN_O);
				} else {
					setBonds(Bond.DOUBLE, Bond.SINGLE, Bond.SINGLE, Bond.DOUBLE, Bond.SINGLE);
					mol.AN(mol.natoms - 2, Atom.AN_O);
				}
			} else { // new furane ring
				setBonds(Bond.DOUBLE, Bond.SINGLE, Bond.SINGLE, Bond.DOUBLE, Bond.SINGLE);
				mol.AN(mol.natoms - 2, Atom.AN_O);
			}
		}

	}

	private Bond createAndAddNewBond(int a, int b, int bondType) {
		Bond bond = mol.createAndAddNewBond(a, b, bondType);
		bonds = mol.bonds;
		return bond;
	}

	private Bond createAndAddNewBond() {
		Bond b = mol.createAndAddBondFromOther(null);
		bonds = mol.bonds;
		return b;
	}

	private Atom createAtom() {
		Atom a = mol.createAtom();
		atoms = mol.atoms;
		return a;
	}

	// ----------------------------------------------------------------------------
	void checkRing(int nmembered) {
		// checks if newly created ring doesn't touch with some atoms +compute the v and
		// bondCenter

		int parent[] = new int[mol.natoms + 1];

		// complete the adjacency list of the newly created bonds
		// * should have been done when the new bonds had been created TODO
		for (int i = 1; i <= nmembered; i++) {
			int ratom = mol.natoms - nmembered + i;
			int rbond = mol.nbonds - nmembered + i;
			atoms[ratom].v[1] = ratom - 1;
			atoms[ratom].v[2] = ratom + 1;
			mol.setBondCenter(bonds[rbond]);
		}
		// close ring
		int n = mol.natoms;
		mol.v(n - nmembered + 1)[1] = n;
		mol.v(n)[2] = n - nmembered + 1;

		// zistuje, ci sa nejake nove atomy dotykaju so starymi
		for (int i = mol.natoms - nmembered + 1; i <= mol.natoms; i++) { // loop over new ring atoms
			parent[i] = 0;
			Atom a = atoms[i];
			double min = TOUCH_LIMIT + 1;
			int tooCloseAtom = 0;
			for (int j = 1; j <= mol.natoms - nmembered; j++) { // loop over older atoms
				Atom b = atoms[j];
				double dx = a.x - b.x;
				double dy = a.y - b.y;
				double rx = dx * dx + dy * dy;
				if (rx < TOUCH_LIMIT)
					if (rx < min) {
						min = rx;
						tooCloseAtom = j;
					} // BB break missing?
				// TODO can we have more than one too close atom? No because
				// it finds the also the closest atom
			}
			if (tooCloseAtom > 0) // dotyk noveho atomu i so starym atomom atom
				if (touchedAtom == 0 || tooCloseAtom == touchedAtom) // ked stopka len ten 1
					parent[i] = tooCloseAtom;
		}
		// parent[i] and i must be merged?
		// robi nove vazby
		int noldbonds = mol.nbonds - nmembered;
		bloop: for (int i = noldbonds + 1; i <= noldbonds + nmembered; i++) {
			int atom1 = bonds[i].va;
			int atom2 = bonds[i].vb;
			int p1 = parent[atom1];
			int p2 = parent[atom2];
			if (p1 > 0 && p2 > 0) { 
				// in case of a touched bond?
				// ak parenty nie su viazane urobi medzi nimi novu vazbu
				for (int k = 1; k <= noldbonds; k++) {
					if (bonds[k].isAB(p1, p2))
						continue bloop;
				}
				// BB create bond between parent[atom1] and parent[atom2]?
				createAndAddNewBond(p1, p2, bonds[i].bondType);
			} else if (p1 > 0) {
				// BB create bond between parent[atom1] and atom2?
				createAndAddNewBond(p1, atom2, bonds[i].bondType);
			} else if (p2 > 0) {
				// BB create bond between parent[atom2] and atom1?
				createAndAddNewBond(p2, atom1, bonds[i].bondType);
			}
		}

		// nakoniec vyhodi atomy, co maju parentov
		int noldatoms = mol.natoms - nmembered;
		for (int i = mol.natoms; i > noldatoms; i--) {
			int pi = parent[i];
			if (pi > 0) {
				mol.deleteAtom(i);
				// 2007.12 checking 5-nasobnost u C
				if (atoms[pi].an == Atom.AN_C) {
					int sum = 0;
					for (int j = 1; j <= atoms[pi].nv; j++) {
						int aj = atoms[pi].v[j];
						for (int k = 1; k <= mol.nbonds; k++) {
							Bond b = bonds[k];
							if (b.isAB(pi, aj))
								sum += b.bondType;
						}
					}
					if (sum > 4) {
						// zmeni nove vazby na single
						// more intelligent and keep double bond ???
						for (int k = noldbonds + 1; k <= noldbonds + nmembered; k++)
							bonds[k].bondType = Bond.SINGLE;
					}
				}
			}
		}

		// if stopka avoid
		if (touchedAtom > 0)
			mol.avoidTouch(nmembered);

	}

	/**
	 * Adding template store in jme.tmol to clicked atom anchor atom in the template
	 * is marked by :1 emptyCanvas indicates that (artificial) touchedAtom should be
	 * deleted. 
	 * 
	 * BH: How about using ChemDraw NicKName files? 
	 * 
	 */
	int addGroupTemplate(boolean emptyCanvas) {
		// finding mark:1 in template

		JMEmol tmol = templateMolecule;
		if (tmol == null || tmol.natoms == 0)
			return 0;

		int mark1 = 0;

		// find the atom that is marked in the template: this is the joining atom

		mark1 = tmol.findFirstMappdedOrMarkedAtom(); // bug fix 2022-02

		if (mark1 == 0) {
			mark1 = 1; // the template has not marked atoms
		}

		// //assert(mark1 > 0) ;

		int nn = mol.natoms;

		// getting dummy point in original molecule
		int source = touchedAtom;

		BondDirection bd = new BondDirection();

		boolean hasTwoPossibleAddAngle = bd.initBondCreate(mol, source, 1);

		BondDirection alternativeBD = null;
		if (hasTwoPossibleAddAngle) {
			alternativeBD = new BondDirection();
			alternativeBD.initBondCreate(mol, source, -1);
		}

		BondDirection templateBD = new BondDirection();
		templateBD.initBondCreate(tmol, mark1, 0);

		// add the template atoms to myself no binding yet
		// do not yet move or rotate the template part
		mol.addOtherMolToMe(tmol);
		mol.complete(mol.parameters.computeValenceState);

		// remove the map coming from the template
		this.atoms[nn + mark1].resetMap();

		if (!emptyCanvas) {

			templateBD.moveAndRotateFragment(mol, nn + 1, mol.natoms, source, bd);

			if (hasTwoPossibleAddAngle) {

				// count the number of touched atoms for the first bond direction
				double closeContactFactor = mol.sumAtomTooCloseContactsOfAddedFragment(nn + 1, mol.natoms);
				// if there is a close contact atom
				// may be the alternative bond direction has less touch atom

				// count the number of touched atoms for the alterantive bond direction

				// first restore the template coordinates inside my self
				for (int ta = 1; ta <= tmol.natoms; ta++) {
					// this.x[nn+ta] = tmol.x(ta);
					// this.y[nn+ta] = tmol.y(ta);
					mol.XY(nn + ta, tmol.x(ta), tmol.y(ta));
				}

				templateBD.moveAndRotateFragment(mol, nn + 1, mol.natoms, source, alternativeBD);
				double alternativecloseContactFactor = mol.sumAtomTooCloseContactsOfAddedFragment(nn + 1, mol.natoms);

				if (alternativecloseContactFactor <= closeContactFactor) {
					// chose the alternative BD which is already set

				} else {
					// chose the first BD

					// first restore the template coordinates inside my self
					for (int ta = 1; ta <= tmol.natoms; ta++) {
						// this.x[nn+ta] = tmol.x(ta);
						// this.y[nn+ta] = tmol.y(ta);
						mol.XY(nn + ta, tmol.x(ta), tmol.y(ta));
					}

					templateBD.moveAndRotateFragment(mol, nn + 1, mol.natoms, source, bd);
					// restore the rotation and
					// translation using the first
					// bd
				}

			}
		}

		// adding connecting bond
		createAndAddNewBond();
		bonds[mol.nbonds].va = source;
		bonds[mol.nbonds].vb = mark1 + nn;

		// for (int i=1;i<=natoms;i++)
		// System.out.println(i+" "+an[i]);
		// for (int i=1;i<=nbonds;i++)
		// System.out.println(i+" "+va[i]+" "+vb[i]+" "+bondType[i]);

		// cleanup the atom map //
		if (emptyCanvas) {
			mol.deleteAtom(source);
			mol.center();
		}
		mol.complete(mol.parameters.computeValenceState);

		return tmol.natoms; // BB needed later by avoidTouch
	}

	public void addGroup(boolean emptyCanvas) {
		//
		mol.touched_org = touchedAtom;
		int nadded = 0;
		switch (action) {
		case Actions.ACTION_GROUP_TBU:
		case Actions.ACTION_GROUP_CCL3:
		case Actions.ACTION_GROUP_CF3:
		case Actions.ACTION_GROUP_SULFO:
		case Actions.ACTION_GROUP_PO3H2:
		case Actions.ACTION_GROUP_SO2NH2:
			addBonds(touchedAtom, LINEAR_ON, 0, LINEAR_OFF, -1, -2);
			switch (action) {
			case Actions.ACTION_GROUP_CCL3:
				setAtoms(Atom.AN_CL, Atom.AN_CL, Atom.AN_CL);
				break;
			case Actions.ACTION_GROUP_CF3:
				setAtoms(Atom.AN_F, Atom.AN_F, Atom.AN_F);
				break;
			case Actions.ACTION_GROUP_SULFO:
				setAtoms(Atom.AN_S, Atom.AN_O, Atom.AN_O, Atom.AN_O);
				setBonds(Bond.SINGLE, Bond.SINGLE, Bond.DOUBLE, Bond.DOUBLE);
				break;
			case Actions.ACTION_GROUP_SO2NH2:
				setAtoms(Atom.AN_S, Atom.AN_N, Atom.AN_O, Atom.AN_O);
				setBonds(Bond.SINGLE, Bond.SINGLE, Bond.DOUBLE, Bond.DOUBLE);
				break;
			case Actions.ACTION_GROUP_PO3H2:
				setAtoms(Atom.AN_P, Atom.AN_O, Atom.AN_O, Atom.AN_O);
				setBonds(Bond.SINGLE, Bond.SINGLE, Bond.SINGLE, Bond.DOUBLE);
				break;
			}
			nadded = 4;
			break;
		case Actions.ACTION_GROUP_NHSO2ME:
			addBonds(touchedAtom, 0, LINEAR_ON, 0, LINEAR_OFF, -1, -2);
			nadded = setAtoms(Atom.AN_N, Atom.AN_S, Atom.AN_C, Atom.AN_O, Atom.AN_O);
			setBonds(Bond.SINGLE, Bond.SINGLE, Bond.SINGLE, Bond.DOUBLE, Bond.DOUBLE);
			break;
		case Actions.ACTION_GROUP_NITRO:
			addBonds(touchedAtom, 0, -1);
			nadded = setAtoms(Atom.AN_N, Atom.AN_O, Atom.AN_O);
			setBonds(Bond.SINGLE, Bond.DOUBLE, jme.options.polarnitro ? Bond.SINGLE : Bond.DOUBLE);
			if (jme.options.polarnitro) {
				setCharges(-1);
			}
			break;
		case Actions.ACTION_GROUP_COO:
			addBonds(touchedAtom, 0, -1);
			nadded = setAtoms(Atom.AN_C, Atom.AN_O, Atom.AN_O);
			setBonds(Bond.SINGLE, Bond.SINGLE, Bond.DOUBLE);
			break;
		case Actions.ACTION_GROUP_COOME:
			addBonds(touchedAtom, 0, 0, -2);
			nadded = setAtoms(Atom.AN_C, Atom.AN_O, Atom.AN_C, Atom.AN_O);
			setBonds(Bond.DOUBLE);
			break;
		case Actions.ACTION_GROUP_CON:
			addBonds(touchedAtom, 0, -1);
			nadded = setAtoms(Atom.AN_C, Atom.AN_N, Atom.AN_O);
			setBonds(Bond.DOUBLE);
			break;
		case Actions.ACTION_GROUP_NCO:
			addBonds(touchedAtom, 0, 0);
			nadded = setAtoms(Atom.AN_N, Atom.AN_C, Atom.AN_O);
			setBonds(Bond.DOUBLE);
			break;
		case Actions.ACTION_GROUP_OCOME:
			addBonds(touchedAtom, 0, 0, -1);
			nadded = setAtoms(Atom.AN_O, Atom.AN_C, Atom.AN_C, Atom.AN_O);
			setBonds(Bond.DOUBLE);
			break;
		case Actions.ACTION_GROUP_NME2:
			addBonds(touchedAtom, 0, -1);
			nadded = setAtoms(Atom.AN_N, Atom.AN_C, Atom.AN_C);
			break;
		case Actions.ACTION_GROUP_CC:
			addBonds(touchedAtom, LINEAR_ON, 0, LINEAR_OFF);
			setBonds(Bond.TRIPLE);
			nadded = 2;
			break;
		case Actions.ACTION_GROUP_COH:
			addBonds(touchedAtom, 0);
			nadded = setAtoms(Atom.AN_C, Atom.AN_O);
			setBonds(Bond.DOUBLE);
			break;
		case Actions.ACTION_GROUP_dO:
			addBonds(touchedAtom);
			nadded = setAtoms(Atom.AN_O);
			setBonds(Bond.DOUBLE);
			break;
		case Actions.ACTION_GROUP_CCC:
			addBonds(touchedAtom, LINEAR_ON, 0, 0, LINEAR_OFF);
			nadded = setAtoms(Atom.AN_C, Atom.AN_C, Atom.AN_C);
			setBonds(Bond.TRIPLE, Bond.SINGLE);
			break;
		case Actions.ACTION_GROUP_CYANO:
			addBonds(touchedAtom, LINEAR_ON, 0, LINEAR_OFF);
			nadded = setAtoms(Atom.AN_C, Atom.AN_N);
			setBonds(Bond.TRIPLE);
			break;
		case Actions.ACTION_GROUP_CF:
			addBonds(touchedAtom);
			nadded = setAtoms(Atom.AN_F);
			break;
		case Actions.ACTION_GROUP_CL:
			addBonds(touchedAtom);
			nadded = setAtoms(Atom.AN_CL);
			break;
		case Actions.ACTION_GROUP_CB:
			addBonds(touchedAtom);
			nadded = setAtoms(Atom.AN_BR);
			break;
		case Actions.ACTION_GROUP_CI:
			addBonds(touchedAtom);
			nadded = setAtoms(Atom.AN_I);
			break;
		case Actions.ACTION_GROUP_CN:
			addBonds(touchedAtom);
			nadded = setAtoms(Atom.AN_N);
			break;
		case Actions.ACTION_GROUP_CO:
			addBonds(touchedAtom);
			nadded = setAtoms(Atom.AN_O);
			break;
		case Actions.ACTION_GROUP_C2:
			addBonds(touchedAtom, 0);
			break;
		case Actions.ACTION_GROUP_C3:
			addBonds(touchedAtom, 0, 0);
			break;
		case Actions.ACTION_GROUP_C4:
			addBonds(touchedAtom, 0, 0, 0);
			break;
		case Actions.ACTION_GROUP_TEMPLATE:
			nadded = addGroupTemplate(emptyCanvas);
			break;
		}

		mol.avoidTouch(nadded); // 2009.2, predtym 4

		if (emptyCanvas)
			mol.touchedAtom = 0;
	}

	private final static int LINEAR_ON = Integer.MAX_VALUE;
	private final static int LINEAR_OFF = Integer.MIN_VALUE;

	/**
	 * Process a sequence of bond creation instructions.
	 * 
	 * This does NOT increment mol.touchedBond or mol.touchedAtom.
	 * 
	 * @param b an array of directives, including b[i] == Integer.MAX_VALUE
	 *          (linearAdding start), Integer.MIN_VALUE (linearAdding stop), b[i] >
	 *          0 target atom, b[i] < 0 counting from the end of the atom set,
	 *          mol.natoms
	 */
	private void addBonds(int... b) {
		for (int i = 0; i < b.length; i++) {
			int mode = b[i];
			switch (mode) {
			case LINEAR_ON:
				linearAdding = true;
				break;
			case LINEAR_OFF:
				linearAdding = false;
				break;
			default:
				mol.addBondToAtom(mode > 0 ? mode : mol.natoms + mode, 0, linearAdding, false);
				bonds = mol.bonds;
				break;
			}
		}
	}

	/**
	 * Set the bond types of a sequence of newly created bonds, from first- to
	 * last-created. Pad with Bond.SINGLE as necesssary.
	 * 
	 * 
	 * @param bo
	 */
	private void setBonds(int... bo) {
		for (int i = 0, pt = mol.nbonds - bo.length + 1; i < bo.length; i++) {
			mol.bonds[pt++].bondType = bo[i];
		}
	}

	/**
	 * Set the atomic number of recently added atoms, from first-created to
	 * last-created. Pad with AN_C as necessary.
	 * 
	 * Note that this does NOT increment mol.touchedAtom.
	 * 
	 * @param an
	 * @return
	 */
	private int setAtoms(int... an) {
		int n = an.length;
		for (int i = 0, pt = mol.natoms - n + 1; i < n; i++) {
			mol.atoms[pt++].an = an[i];
		}
		return n;
	}

	private void setCharges(int... ch) {
		for (int i = 0, pt = mol.natoms - ch.length + 1; i < ch.length; i++) {
			mol.atoms[pt++].q = ch[i];
		}
	}

	public String setTemplate(String t) throws Exception {
		templateString = t;
		Parameters pars = new Parameters();
		pars.mark = true; // needed otherwise the atom map will be ignored
		templateMolecule = new JMEmol(jme, t, SupportedFileFormat.JME, pars);
		templateMolecule.internalBondLengthScaling();		
		if (!templateMolecule.hasMappedOrMarkedAtom()) {
			// console warning
			return "template molecule has no mapped atom";
			// June 2020: JMEmol does not do it automatically. 
		}
		return null;
	}

	public String getTemplateString() {
		return templateString;
	}

	public String checkBondAction() {
		String event = null;
		boolean cleanPolar = false;
		if (action == Actions.ACTION_DELETE) {
			deleteAtomOrBond();
			jme.updatePartsList(); // record the event as well
		} else if (action == Actions.ACTION_DELGROUP) {
			mol.deleteAtomGroup();
			cleanPolar = true;
			mol.touchedBond = 0;
			event = JME.DEL_BOND_GROUP;
		} else if (action == Actions.ACTION_STEREO) {
			mol.toggleBondStereo(mol.touchedBond);
			event = JME.SET_BOND_STEREO;
		} else if (action == Actions.ACTION_BOND_SINGLE || action == Actions.ACTION_CHAIN) { // Actions.ACTION_CHAIN should be removed?
			if (mol.bonds[mol.touchedBond].bondType == Bond.SINGLE && mol.bonds[mol.touchedBond].stereo == 0) {
				mol.bonds[mol.touchedBond].bondType = Bond.DOUBLE;
				event = JME.SET_BOND_DOUBLE;
			} else {
				mol.bonds[mol.touchedBond].bondType = Bond.SINGLE;
				mol.bonds[mol.touchedBond].stereo = 0;
				event = JME.SET_BOND_SINGLE;
			}
			mol.bonds[mol.touchedBond].stereo = 0; // zrusi stereo
		} else if (action == Actions.ACTION_BOND_DOUBLE) {
			boolean differentBondOrder = mol.bonds[mol.touchedBond].bondType != Bond.DOUBLE;
			mol.bonds[mol.touchedBond].bondType = Bond.DOUBLE;
			if (!differentBondOrder) {
				mol.bonds[mol.touchedBond].toggleNormalCrossedDoubleBond();
			} else {
				mol.bonds[mol.touchedBond].stereo = 0; // zrusi stereo
			}
			cleanPolar = true;
			event = JME.SET_BOND_DOUBLE;
		} else if (action == Actions.ACTION_BOND_TRIPLE) {
			mol.bonds[mol.touchedBond].bondType = Bond.TRIPLE;
			mol.bonds[mol.touchedBond].stereo = 0; // zrusi stereo
			cleanPolar = true;
			event = JME.SET_BOND_TRIPLE;
		} else if (action >= Actions.ACTION_RING_3 && action <= Actions.ACTION_RING_9) {
			// fusing ring to bond
			jme.lastAction = JME.LA_RING; // in addRing may be set to 0
			addRing();
			cleanPolar = true;
			event = JME.ADD_RING_BOND;
		}
		if (cleanPolar)
			mol.cleanAfterChanged(jme.options.polarnitro); // FIXME: add to addRing			
		return event;
	}

	public String checkAtomAction() {
		String event = null;
		if (action == Actions.ACTION_DELETE) {
			deleteAtomOrBond();
			jme.updatePartsList();
		} else if (action == Actions.ACTION_DELGROUP) {
			return "TRUE"; // do nothing
		} else if (action == Actions.ACTION_CHARGE) {
			if (mol.changeCharge(mol.touchedAtom, 0))
				event = JME.CHARGE_ATOM0;
		} else if (action == Actions.ACTION_BOND_SINGLE || action == Actions.ACTION_BOND_DOUBLE
				|| action == Actions.ACTION_BOND_TRIPLE || action == Actions.ACTION_STEREO
				|| action == Actions.ACTION_CHAIN) {
			jme.lastAction = JME.LA_BOND; // allows for snap drag
			addBond();
			mol.touched_org = mol.touchedAtom;
			if (action == Actions.ACTION_CHAIN) {
				mol.nchain = 1; // pre CHAIN rubberbanding
				mol.chain[1] = mol.natoms;
				mol.chain[0] = mol.touchedAtom;
				mol.touchedBond = 0; // 2005.02
				jme.willPostSave(false); 
				// for the CHAIN, save the state at mouseUp event
			} else {
				jme.recordBondEvent(JME.ADD_BOND);
				event = "";
			}
		} else if (action >= Actions.ACTION_RING_3 && action <= Actions.ACTION_RING_9) {
			jme.lastAction = JME.LA_RING; // in addRing may be set to 0
			addRing();
			event = JME.ADD_RING;
		} else if (action == Actions.ACTION_TEMPLATE) {
			// BH Not implemented??
			// mol.addTemplate(template);
			jme.lastAction = JME.LA_GROUP;
			event = JME.ADD_TEMPLATE;
		} else if (action >= Actions.ACTION_GROUP_MIN && action < Actions.ACTION_GROUP_MAX) {
			addGroup(false);
			event = JME.ADD_GROUP;
			jme.lastAction = JME.LA_GROUP; // may be set to 0
		} else if (action > 300) { // atoms
			if (jme.active_an != mol.an(mol.touchedAtom) || jme.active_an == Atom.AN_X) {
				mol.AN(mol.touchedAtom, jme.active_an);
				mol.Q(mol.touchedAtom, 0); // resetne naboj
				mol.atoms[mol.touchedAtom].iso = 0; // BB: reset isotop
				mol.atoms[mol.touchedAtom].nh = 0;

				// special processing pre AN_X, osetrene, ze moze byt aj
				// ""
				if (jme.active_an == Atom.AN_X) {
					mol.setAtom(mol.touchedAtom, jme.getAtomSymbolForX());
				}
				// jme.recordAtomEvent(SET_ATOM + active_an); 
				// active_an is an arbitrary
				// number, should be
				// changed to the string of the atom type
				event = JME.SET_ATOM;
			}
		}
		return event;
	}

	public boolean deleteAtomOrBond() {
		if (mol.touchedAtom == 0 && mol.touchedBond == 0)
			return false;
		if (mol.touchedAtom > 0) {
			mol.deleteAtom(mol.touchedAtom);
			jme.recordAtomEvent(JME.DEL_ATOM);
			mol.touchedAtom = 0;
		} else {
			mol.deleteBond(mol.touchedBond);
			jme.recordBondEvent(JME.DEL_BOND); // BH was recordAtomEvent
			mol.touchedBond = 0;
		}
		mol.cleanAfterChanged(jme.options.polarnitro); // to add Hs
		return true;
	}

}