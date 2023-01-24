package jme;

/**
 * 
 * @author bruno
 *
 */
class BondDirection {

	double sin;
	double cos;
	double x, y;

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

		Atom savedAtom = mol.atoms[sourceAtom].deepCopy();

		boolean hasTwoPossibleAddAngle = mol.addBondToAtom(sourceAtom, addBondArgument);
		// create a new atom and a
		// new bond with the
		// right orientation
		// relative to the
		// sourceAtom

		int destAtom = mol.natoms; // index of the new added atom

		this.init(mol, sourceAtom, destAtom);

		mol.deleteAtom(destAtom); // delete the added bond and added atom

		// deleteAtom has side effects on the source atom
		mol.atoms[sourceAtom].nh = savedAtom.nh;
		mol.atoms[sourceAtom].nv = savedAtom.nv;
		mol.atoms[sourceAtom].q = savedAtom.q;

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