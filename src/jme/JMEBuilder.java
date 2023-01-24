package jme;

import java.util.Arrays;

class JMEBuilder {

	private JME jme;
	private JMEmol mol;
	private Atom[] atoms;
	private Bond[] bonds;
	private int touchedAtom;
	private int touchedBond;
	private int action;

	JMEBuilder(JME jme) {
		this.jme = jme;
	}

	public JMEBuilder set(JMEmol mol, int action) {
		this.mol = mol;
		this.atoms = mol.atoms;
		this.bonds = mol.bonds;
		this.touchedAtom = mol.touchedAtom;
		this.touchedBond = mol.touchedBond;
		this.action = action;
		return this;
	}

	public void addBond() {
		mol.addBondToAtom(touchedAtom, 0);
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
		case JME.ACTION_RING_3:
			nmembered = 3;
			break;
		case JME.ACTION_RING_4:
			nmembered = 4;
			break;
		case JME.ACTION_RING_5:
		case JME.ACTION_RING_FURANE:
		case JME.ACTION_RING_3FURYL:
			nmembered = 5;
			break;
		case JME.ACTION_RING_6:
		case JME.ACTION_RING_PH:
			nmembered = 6;
			break;
		case JME.ACTION_RING_7:
			nmembered = 7;
			break;
		case JME.ACTION_RING_8:
			nmembered = 8;
			break;
		case JME.ACTION_RING_9:
			nmembered = 9;
			break;
		}

		diel = Math.PI * 2. / nmembered;
		rc = Math.sqrt(JMEmol.RBOND * JMEmol.RBOND / 2. / (1. - Math.cos(diel)));
		Atom a = atoms[touchedAtom];
		Bond b = bonds[touchedBond];
		if (touchedAtom > 0) {
			// --- adding ring at the end of the bond
			if (a.nv < 2) {
				addRingToBond(nmembered, diel, rc);
			} else {
				if (!jme.mouseShift && !jme.spiroAdding) {
					// adding bond and ring
					returnTouch = touchedAtom;
					addBond();
					touchedAtom = mol.natoms;
					addRingToBond(nmembered, diel, rc);
				} else {
					// checking whether cad do spiro
					jme.spiroAdding = false;
					if (action == JME.ACTION_RING_PH || action == JME.ACTION_RING_FURANE
							|| action == JME.ACTION_RING_3FURYL) {
						mol.info("ERROR - cannot add aromatic spiro ring !", JME.LA_FAILED);
						return;
					}
					for (int i = 1; i <= a.nv; i++) {
						// int bo = bondType[bondIdentity(touchedAtom,v(touchedAtom)[i])];
						int bo = mol.bondIdentityBond(touchedAtom, a.v[i]).bondType;
						if (i > 2 || bo != Bond.SINGLE) {
							mol.info("ERROR - spiro ring not possible here !", JME.LA_FAILED);
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
						// x[natoms]=newPoint[0]+rc*(Math.sin(uhol)*cosa-Math.cos(uhol)*sina);
						// y[natoms]=newPoint[1]+rc*(Math.cos(uhol)*cosa+Math.sin(uhol)*sina);
						mol.XY(newAtom, newPoint[0] + rc * (Math.sin(uhol) * cosa - Math.cos(uhol) * sina),
								newPoint[1] + rc * (Math.cos(uhol) * cosa + Math.sin(uhol) * sina));
					}
				}
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
				mol.XY(newAtom, xstart + rc * (Math.sin(uhol) * cosa - Math.cos(uhol) * sina),
						ystart + rc * (Math.cos(uhol) * cosa + Math.sin(uhol) * sina));
				// next when fusing to the "long" bond
				if (revert == 1) {
					if (i == nmembered) {
						mol.XY(newAtom, a1.x, a1.y);
						/* x(natoms)=x; y(natoms)=y; */}
					if (i == nmembered - 1) {
						mol.XY(newAtom, a2.x, a2.y);
						/* x[natoms]=x(atom2); y[natoms]=y(atom2); */}
				} else {
					if (i == nmembered - 1) {
						mol.XY(newAtom, a1.x, a1.y);
						/* x[natoms]=x(atom1); y[natoms]=y(atom1); */}
					if (i == nmembered) {
						mol.XY(newAtom, a2.x, a2.y);
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
				mol.XY(newAtom, mol.xorg + rc * Math.sin(uhol), mol.yorg + rc * Math.cos(uhol));
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
		if (action == JME.ACTION_RING_PH) {
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
					if (atom3 > 0)
						// checking if bond atom3-atom is multiple
						for (int i = 1; i <= mol.nbonds; i++)
							if ((bonds[i].va == atom3 && bonds[i].vb == atom)
									|| (bonds[i].va == atom && bonds[i].vb == atom3)) {
								if (!mol.isSingle(i)) {
									setBonds(Bond.DOUBLE, Bond.SINGLE, Bond.DOUBLE, Bond.SINGLE, Bond.TRIPLE, Bond.SINGLE);
								}
								break;
							}
				} else {
					setBonds(Bond.DOUBLE, Bond.SINGLE, Bond.DOUBLE, Bond.SINGLE, Bond.DOUBLE, Bond.SINGLE);
				}
			}
		} else if (action == JME.ACTION_RING_FURANE || action == JME.ACTION_RING_3FURYL) {
			if (touchedBond > 0) {
				if (b.bondType == Bond.SINGLE) {
					Atom va = atoms[b.va];
					Atom vb = atoms[b.vb];
					// nned to check whether it is not =C-C= bond
					boolean isConjugated = false;
					for (int i = 1; i <= va.nv; i++) {
						if (mol.bondIdentityBond(b.va, va.v[i]).bondType > Bond.SINGLE) {
							isConjugated = true;
							break;
						}
					}
					for (int i = 1; i <= mol.nv(b.vb); i++) {
						int ax = vb.v[i];
						if (mol.bondIdentityBond(b.vb, ax).bondType > Bond.SINGLE) {
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
				if (action == JME.ACTION_RING_FURANE) {
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
		Bond b = mol.createAndAddNewBond();
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
			bonds[rbond].initBondCenter(atoms);
		}
		// close ring
		int n = mol.natoms;
		mol.v(n - nmembered + 1)[1] = n;
		mol.v(n)[2] = n - nmembered + 1;

		// zistuje, ci sa nejake nove atomy dotykaju so starymi
		for (int i = mol.natoms - nmembered + 1; i <= mol.natoms; i++) { // loop over new ring atoms
			parent[i] = 0;
			double min = JME.TOUCH_LIMIT + 1;
			int tooCloseAtom = 0;
			for (int j = 1; j <= mol.natoms - nmembered; j++) { // loop over older atoms
				double dx = mol.x(i) - mol.x(j);
				double dy = mol.y(i) - mol.y(j);
				double rx = dx * dx + dy * dy;
				if (rx < JME.TOUCH_LIMIT)
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
			if (parent[atom1] > 0 && parent[atom2] > 0) { // in case of a touched bond?
				// ak parenty nie su viazane urobi medzi nimi novu vazbu
				for (int k = 1; k <= noldbonds; k++) {
					if ((bonds[k].va == parent[atom1] && bonds[k].vb == parent[atom2])
							|| (bonds[k].vb == parent[atom1] && bonds[k].va == parent[atom2]))
						continue bloop;
				}
				// BB create bond between parent[atom1] and parent[atom2]?
				createAndAddNewBond(parent[atom1], parent[atom2], bonds[i].bondType);
			} else if (parent[atom1] > 0) {
				// BB create bond between parent[atom1] and atom2?
				createAndAddNewBond(parent[atom1], atom2, bonds[i].bondType);
			} else if (parent[atom2] > 0) {
				// BB create bond between parent[atom2] and atom1?
				createAndAddNewBond(parent[atom2], atom1, bonds[i].bondType);
			}
		}

		// nakoniec vyhodi atomy, co maju parentov
		int noldatoms = mol.natoms - nmembered;
		for (int i = mol.natoms; i > noldatoms; i--) {
			if (parent[i] > 0) {
				mol.deleteAtom(i);
				// 2007.12 checking 5-nasobnost u C
				if (mol.an(parent[i]) == Atom.AN_C) {
					int sum = 0;
					for (int j = 1; j <= mol.nv(parent[i]); j++) {
						int a2 = mol.v(parent[i])[j];
						for (int k = 1; k <= mol.nbonds; k++) {
							if ((bonds[k].va == parent[i] && bonds[k].vb == a2)
									|| (bonds[k].va == a2 && bonds[k].vb == parent[i]))
								sum += bonds[k].bondType;
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

	// adding template store in jme.tmol to clicked atom
// anchor atom in the template is marked by :1
// emptyCanvas indicates that (artificial) touchedAtom should be deleted
	int addGroupTemplate(boolean emptyCanvas) {
		// finding mark:1 in template
		// qw

		JMEmol tmol = jme.templateMolecule;

		// BB: without the next two lines, the GWT optimizer fails (one can still
		// compile with the option -draftCompile and -XenableClosureCompiler)
		if (tmol == null || tmol.natoms == 0)
			return 0;

		int mark1 = 0;

		// find the atom that is marked in the template: this is the joining atom
		// for (int k=1;k<=tmol.nmarked;k++) {
		// int atom = tmol.mark[k][0];
		// if (tmol.mark[k][1] == 1) mark1 = atom;
		// }

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

		BondDirection alternativeBD = new BondDirection();
		if (hasTwoPossibleAddAngle) {
			alternativeBD.initBondCreate(mol, source, -1);
		}

		BondDirection templateBD = new BondDirection();
		templateBD.initBondCreate(tmol, mark1, 0);

		// add the template atoms to myself no binding yet
		// do not yet move or rotate the template part
		mol.addOtherMolToMe(tmol);
		mol.complete(mol.moleculeHandlingParameters.computeValenceState);

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
		mol.complete(mol.moleculeHandlingParameters.computeValenceState);

		return tmol.natoms; // BB needed later by avoidTouch
	}

	void addGroup(boolean emptyCanvas) {
		//
		mol.touched_org = touchedAtom;
		int nadded = 0;
		switch (action) {
		case JME.ACTION_GROUP_TBU:
		case JME.ACTION_GROUP_CCL3:
		case JME.ACTION_GROUP_CF3:
		case JME.ACTION_GROUP_SULFO:
		case JME.ACTION_GROUP_PO3H2:
		case JME.ACTION_GROUP_SO2NH2:
			addBonds(touchedAtom, LINEAR_ON, 0, LINEAR_OFF, -1, -2);
			switch (action) {
			case JME.ACTION_GROUP_CCL3:
				setAtoms(Atom.AN_CL, Atom.AN_CL, Atom.AN_CL);
				break;
			case JME.ACTION_GROUP_CF3:
				setAtoms(Atom.AN_F, Atom.AN_F, Atom.AN_F);
				break;
			case JME.ACTION_GROUP_SULFO:
				setAtoms(Atom.AN_S, Atom.AN_O, Atom.AN_O, Atom.AN_O);
				setBonds(Bond.SINGLE, Bond.SINGLE, Bond.DOUBLE, Bond.DOUBLE);
				break;
			case JME.ACTION_GROUP_SO2NH2:
				setAtoms(Atom.AN_S, Atom.AN_N, Atom.AN_O, Atom.AN_O);
				setBonds(Bond.SINGLE, Bond.SINGLE, Bond.DOUBLE, Bond.DOUBLE);
				break;
			case JME.ACTION_GROUP_PO3H2:
				setAtoms(Atom.AN_P, Atom.AN_O, Atom.AN_O, Atom.AN_O);
				setBonds(Bond.SINGLE, Bond.SINGLE, Bond.SINGLE, Bond.DOUBLE);
				break;
			}
			nadded = 4;
			break;
		case JME.ACTION_GROUP_NHSO2ME:
			addBonds(touchedAtom, 0, LINEAR_ON, 0, LINEAR_OFF, -1, -2);
			nadded = setAtoms(Atom.AN_N, Atom.AN_S, Atom.AN_C, Atom.AN_O, Atom.AN_O);
			setBonds(Bond.SINGLE, Bond.SINGLE, Bond.SINGLE, Bond.DOUBLE, Bond.DOUBLE);
			break;
		case JME.ACTION_GROUP_NITRO:
			addBonds(touchedAtom, 0, -1);
			nadded = setAtoms(Atom.AN_N, Atom.AN_O, Atom.AN_O);
			setBonds(Bond.SINGLE, Bond.DOUBLE, jme.options.polarnitro ? Bond.SINGLE : Bond.DOUBLE);
			if (jme.options.polarnitro) {
				mol.changeCharge(mol.natoms - 2, 1);
				mol.changeCharge(mol.natoms, -1);
			}
			break;
		case JME.ACTION_GROUP_COO:
			addBonds(touchedAtom, 0, -1);
			nadded = setAtoms(Atom.AN_C, Atom.AN_O, Atom.AN_O);
			setBonds(Bond.SINGLE, Bond.SINGLE, Bond.DOUBLE);
			break;
		case JME.ACTION_GROUP_COOME:
			addBonds(touchedAtom, 0, 0, -2);
			nadded = setAtoms(Atom.AN_C, Atom.AN_O, Atom.AN_C, Atom.AN_O);
			setBonds(Bond.DOUBLE);
			break;
		case JME.ACTION_GROUP_CON:
			addBonds(touchedAtom, 0, -1);
			nadded = setAtoms(Atom.AN_C, Atom.AN_N, Atom.AN_O);
			setBonds(Bond.DOUBLE);
			break;
		case JME.ACTION_GROUP_NCO:
			addBonds(touchedAtom, 0, 0);
			nadded = setAtoms(Atom.AN_N, Atom.AN_C, Atom.AN_O);
			setBonds(Bond.DOUBLE);
			break;
		case JME.ACTION_GROUP_OCOME:
			addBonds(touchedAtom, 0, 0, -1);
			nadded = setAtoms(Atom.AN_O, Atom.AN_C, Atom.AN_C, Atom.AN_O);
			setBonds(Bond.DOUBLE);
			break;
		case JME.ACTION_GROUP_NME2:
			addBonds(touchedAtom, 0, -1);
			nadded = setAtoms(Atom.AN_N, Atom.AN_C, Atom.AN_C);
			break;
		case JME.ACTION_GROUP_CC:
			addBonds(touchedAtom, LINEAR_ON, 0, LINEAR_OFF);
			setBonds(Bond.TRIPLE);
			nadded = 2;
			break;
		case JME.ACTION_GROUP_COH:
			addBonds(touchedAtom, 0);
			nadded = setAtoms(Atom.AN_C, Atom.AN_O);
			setBonds(Bond.DOUBLE);
			break;
		case JME.ACTION_GROUP_dO:
			addBonds(touchedAtom);
			nadded = setAtoms(Atom.AN_O);
			setBonds(Bond.DOUBLE);
			break;
		case JME.ACTION_GROUP_CCC:
			addBonds(touchedAtom, LINEAR_ON, 0, 0, LINEAR_OFF);
			nadded = setAtoms(Atom.AN_C, Atom.AN_C, Atom.AN_C);
			setBonds(Bond.TRIPLE, Bond.SINGLE);
			break;
		case JME.ACTION_GROUP_CYANO:
			addBonds(touchedAtom, LINEAR_ON, 0, LINEAR_OFF);
			nadded = setAtoms(Atom.AN_C, Atom.AN_N);
			setBonds(Bond.TRIPLE);
			break;
		case JME.ACTION_GROUP_CF:
			addBonds(touchedAtom);
			nadded = setAtoms(Atom.AN_F);
			break;
		case JME.ACTION_GROUP_CL:
			addBonds(touchedAtom);
			nadded = setAtoms(Atom.AN_CL);
			break;
		case JME.ACTION_GROUP_CB:
			addBonds(touchedAtom);
			nadded = setAtoms(Atom.AN_BR);
			break;
		case JME.ACTION_GROUP_CI:
			addBonds(touchedAtom);
			nadded = setAtoms(Atom.AN_I);
			break;
		case JME.ACTION_GROUP_CN:
			addBonds(touchedAtom);
			nadded = setAtoms(Atom.AN_N);
			break;
		case JME.ACTION_GROUP_CO:
			addBonds(touchedAtom);
			nadded = setAtoms(Atom.AN_O);
			break;
		case JME.ACTION_GROUP_C2:
			addBonds(touchedAtom, 0);
			break;
		case JME.ACTION_GROUP_C3:
			addBonds(touchedAtom, 0, 0);
			break;
		case JME.ACTION_GROUP_C4:
			addBonds(touchedAtom, 0, 0, 0);
			break;
		case JME.ACTION_GROUP_TEMPLATE:
			nadded = addGroupTemplate(emptyCanvas);
			break;
		}

		mol.avoidTouch(nadded); // 2009.2, predtym 4

		if (emptyCanvas)
			mol.touchedAtom = 0;
	}

	private final static int LINEAR_ON = Integer.MAX_VALUE;
	private final static int LINEAR_OFF = Integer.MIN_VALUE;
	
	private void addBonds(int...b) {
		System.out.println("addBonds " + Arrays.toString(b));
		
		for (int i = 0; i < b.length; i++) {
			int mode = b[i];
			switch (mode) {
			case LINEAR_ON:
				mol.linearAdding = true;
				break;
			case LINEAR_OFF:
				mol.linearAdding = false;
				break;
			default:
				if (mode > 0) {
					mol.addBondToAtom(touchedAtom, 0);
				} else {
					mol.addBondToAtom(mol.natoms + mode, 0);
				}
				bonds = mol.bonds;
				break;
			}
		}
	}

	private void setBonds(int... bo) {
		System.out.println("setBonds " + Arrays.toString(bo));
		int pt = mol.nbonds - bo.length + 1;
		for (int i = 0; i < bo.length; i++) {
			mol.bonds[pt++].bondType = bo[i];
		}
	}

	private int setAtoms(int... an) {
		int n = an.length;
		int pt = mol.natoms - n + 1;
		for (int i = 0; i < n; i++) {
			mol.atoms[pt++].an = an[i];
		}
		return n;
	}


}