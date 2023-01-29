package jme;

import jme.core.Atom;

/**
 * 
 * @author bruno
 *
 */
public class BondDirection {

	public double sin;
	public double cos;
	public double x, y;

	public BondDirection() {
	};

	public void init(JMEmol mol, int sourceAtom, int destAtom) {
		x = mol.x(destAtom);
		y = mol.y(destAtom);
		double dx = mol.x(sourceAtom) - x;
		double dy = mol.y(sourceAtom) - y;

		double r = Math.sqrt(dx * dx + dy * dy); // eucldean dist between new atom and source (anchor)

		this.sin = dy / r;
		this.cos = dx / r;

	}

	public boolean initBondCreate(JMEmol mol, int sourceAtom, int addBondArgument) {
		int nh = mol.atoms[sourceAtom].nh;
		int nv = mol.atoms[sourceAtom].nv;
		int q = mol.atoms[sourceAtom].q;
		boolean hasTwoPossibleAddAngle = mol.addBondToAtom(sourceAtom, addBondArgument, false, false);	
		int destAtom = mol.natoms; // index of the new added atom
		init(mol, sourceAtom, destAtom);
		mol.deleteAtom(destAtom); // delete the added bond and added atom
		// deleteAtom has side effects on the source atom
		mol.atoms[sourceAtom].nh = nh;
		mol.atoms[sourceAtom].nv = nv;
		mol.atoms[sourceAtom].q = q;
		return hasTwoPossibleAddAngle;
	}

	/**
	 * 
	 * @param mol      : the mol that already contains the added fragment
	 * @param ffirst   : fisrst atom index of the fragment
	 * @param flast    : last atom index of the fragment
	 * @param sourceAt : source atom index in the mol to which the fragment will be
	 *                 attached
	 * @param sourceBD : the bonddirection of the source atom
	 */
	public void moveAndRotateFragment(JMEmol mol, int ffirst, int flast, int sourceAt, BondDirection sourceBD) {

		for (int i = ffirst; i <= flast; i++) {
			Atom atom = mol.atoms[i];
			atom.moveXY(-this.x, -this.y);

			// rotation to be parallel with x axis
			double xx = atom.x * this.cos + atom.y * this.sin;
			double yy = atom.y * this.cos - atom.x * this.sin;

			atom.XY(xx, yy);

			// rotating parallel with connecting bond
			// -cos - opposite direction
			xx = -atom.x * sourceBD.cos + atom.y * sourceBD.sin;
			yy = -atom.y * sourceBD.cos - atom.x * sourceBD.sin;
			atom.XY(xx, yy);

			// move towards point1
			atom.moveXY(mol.atoms[sourceAt].x, mol.atoms[sourceAt].y);

		}
	}
}