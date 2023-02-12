package jme.ocl;

import com.actelion.research.chem.AbstractDepictor;
import com.actelion.research.chem.AromaticityResolver;
import com.actelion.research.chem.IDCodeParser;
import com.actelion.research.chem.Molecule;
import com.actelion.research.chem.MolfileCreator;
import com.actelion.research.chem.MolfileParser;
import com.actelion.research.chem.RingCollection;
import com.actelion.research.chem.SVGDepictor;
import com.actelion.research.chem.SmilesParser;
import com.actelion.research.chem.StereoMolecule;
import com.actelion.research.chem.coords.CoordinateInventor;
import com.actelion.research.gui.generic.GenericRectangle;

import jme.JMEmol;
import jme.core.Atom;
import jme.core.Bond;
import jme.core.JMECore;
import jme.gui.GUI.Ring;
import jme.gui.GUI.RingInfo;
import jme.io.JMEReader;
import jme.io.JMESVGWriter;

public class OclAdapter {

	
	public String getOclCode(String molFile) {
		// TODO : error handling
		String result = null;
		StereoMolecule mol = new StereoMolecule();
		if (new MolfileParser().parse(mol, molFile)) {

			result = mol.getIDCode();
		}

		return result;
	}

	
	public String getOclSVG(String molFile) {
		double width = 400;
		double height = 300;
		// TODO : error handling
		StereoMolecule mol = new StereoMolecule();		
		
		if (new MolfileParser().parse(mol, molFile)) {			
			SVGDepictor svgd = new JMESVGWriter(mol, molFile);
			svgd.setLegacyMode(false); // include font information
			svgd.validateView(null, new GenericRectangle(0, 0, width, height),
					AbstractDepictor.cModeInflateToHighResAVBL);
			svgd.paint(null);
			return svgd.toString();
		}
		return null;
	}

	
	public String OclCodeToMOL(String oclCode) {
		StereoMolecule mol = new IDCodeParser().getCompactMolecule(oclCode.trim());
		MolfileCreator mfc = new MolfileCreator(mol);
		return mfc.getMolfile();

	}

	
	public String SMILEStoMOL(String smiles) throws Exception {

		// OCLSmilesParser generates an exception if the SMILES is empty
		if (smiles == null || smiles.length() == 0 || smiles.trim().length() == 0)
			return new JMEmol().createMolFile(""); // empty mol

		StereoMolecule mol = new StereoMolecule();
		new SmilesParser().parse(mol, smiles.trim());

		MolfileCreator mfc = new MolfileCreator(mol);
		return mfc.getMolfile();
	}

	
	public String v3000toV2000MOL(String v3000Mol) {
		StereoMolecule mol = new StereoMolecule();
		boolean success = new MolfileParser().parse(mol, v3000Mol);
		// error messages are sent to a TRACE function that cannot be redefined
		if (success) {
			MolfileCreator mfc = new MolfileCreator(mol);
			return mfc.getMolfile();
		}
		return null;
	}
	

	/**
	 * In most cases, the returned molecule is the same as the input one. If OCL lib
	 * changfed the number of atoms, then another molecule is returned. Return null
	 * if 2D fails.
	 * 
	 * @param mol
	 * @return
	 */
	public static JMEmol compute2Dcoordinates(final JMEmol mol) {
		JMEmol result = mol;
		JMEmol molCopy = mol;

		// OenCHemLib issue: the hydrogens are placed at the end of the CT, thus atom
		// arder is changed
		boolean hasExplicitHydrogens = mol.hasHydrogen();
		if (hasExplicitHydrogens) {
			molCopy = mol.deepCopy();
			for (int i = 1; i <= molCopy.natoms; i++) {
				Atom atom = molCopy.getAtom(i);
				if (atom.an == Atom.AN_H) { // replace hydrogen
					atom.an = Atom.AN_X;
					atom.iso = 0;
					atom.label = "A" + i;
				}
			}
		}

		// create a molfile
		String molFile = molCopy.createMolFile("");

		// create a OCL molecule
		StereoMolecule oclMol = new StereoMolecule();
		if (new MolfileParser().parse(oclMol, molFile)) {
			boolean computed2D = true;
			// generate 2D - can fail
			try {
				// argument: do not remove explicit H
				new CoordinateInventor(0).invent(oclMol);
			} catch (Exception e) {
				computed2D = false;
				result = null;
			}

			if (computed2D) {
				// assume that the order of the atoms is the same
				if (oclMol.getAllAtoms() == mol.nAtoms()) {
					for (int i = 0; i < oclMol.getAllAtoms(); i++) {
						mol.XY(i + 1, oclMol.getAtomX(i), oclMol.getAtomY(i));
					}
					mol.internalBondLengthScaling();
				} else {
					// TODO: test case:
					MolfileCreator mfc = new MolfileCreator(oclMol);
					try {
						result = new JMEmol(mol.jme, mfc.getMolfile(), JMEReader.SupportedInputFileFormat.MOL,
								mol.parameters);
					} catch (Exception e) {
						e.printStackTrace();
					}
					result.chiralFlag = mol.chiralFlag; // is this needed? TODO
					result.internalBondLengthScaling();

				}
			}

		}

		return result;

	}

	/**
	 * Use OCLib to recompute aromatic or query bond orders (e.g input file with
	 * bond order 4) Return null if an error occurred . If no changes, return the
	 * same molecule object otherwise a copy
	 * 
	 * @param mol
	 * @return null if no change
	 */
	public static JMEmol reComputeBondOrderIfAromaticBondType(JMEmol mol) {
		if (!mol.hasAromaticBondType()) {
			return mol; // no changes
		}
		// create a molfile
		String molFile = mol.createMolFile("");
		JMEmol result = mol.deepCopy();

		// create a OCL molecule
		StereoMolecule oclMol = new StereoMolecule();
		if (!new MolfileParser().parse(oclMol, molFile)) {
			return mol;
		}

		if (!(oclMol.getAllAtoms() == mol.nAtoms() && oclMol.getAllBonds() == mol.nBonds())) {
			return null;
		}

		boolean computeBondOrder = true;

		try {
			AromaticityResolver bondFixer = new AromaticityResolver(oclMol);
			bondFixer.locateDelocalizedDoubleBonds(null);
		} catch (Exception e) {
			computeBondOrder = false;
			result = null;
		}

		if (computeBondOrder) {
			// assume that the order of the atoms is the same

			for (int b = 0; b < oclMol.getAllBonds(); b++) {
				int bo = oclMol.getBondOrder(b);
				int at1 = oclMol.getBondAtom(0, b);
				int at2 = oclMol.getBondAtom(1, b);
				int charge1 = oclMol.getAtomCharge(at1++);
				int charge2 = oclMol.getAtomCharge(at2++);
				if (mol.q(at1) != charge1 || mol.q(at2) != charge2) {
					return null;
				}

				Bond bond = result.bonds[b + 1];
				if ((at1 == bond.va && at2 == bond.vb) || (at2 == bond.va && at1 == bond.vb)) {
					bond.bondType = bo;
				} else {
					return null;
				}
			}

		}

		return result;
	}

	/**
	 * Retrieve ring information from OCL, including bonds and atoms in small rings, as well as aromaticity.  
	 * 
	 * See GUI.getRingInfo()
	 * 
	 * @author Bob Hanson
	 */
	
	public void getRingInfo(RingInfo info, JMECore mol) {		
		StereoMolecule m = new StereoMolecule();
		for (int i = 1; i <= mol.natoms; i++) {
			Atom a = mol.atoms[i];
			int an = 6; // set this to 3 to not do any aromatic calc
			switch (a.an) {
			case Atom.AN_B:
				an = 5;
				break;
			case Atom.AN_N:
				an = 7;
				break;
			case Atom.AN_O:
				an = 8;
			case Atom.AN_P:
				an = 15;
				break;
			case Atom.AN_S:
				an = 16;
			case Atom.AN_SE:
				an = 34;
				break;
			case Atom.AN_SI:
				an = 14;
				break;
			case Atom.AN_F:
			case Atom.AN_CL:
			case Atom.AN_BR:
			case Atom.AN_I:
				an = 9; // don't care!
				break;
			case Atom.AN_H:
				an = 1;
				break;
			}
			m.setAtomicNo(m.addAtom(a.x, a.y), an);
		}
		for (int i = 1; i <= mol.nbonds; i++) {
			Bond b = mol.bonds[i];
			int type = b.bondType;
			switch (b.bondType) {
			case Bond.SINGLE:
			case Bond.DOUBLE:
				type = b.bondType;
				break;
			case Bond.TRIPLE:
				type = Molecule.cBondTypeTriple;
				break;
			default:
				type = 1;
				break;
			}
			m.setBondType(m.addBond(b.va - 1, b.vb - 1), type);
		}

		try {
			RingCollection sys = m.getRingSet();
			for (int i = sys.getSize(); --i >= 0;) {
				boolean isAromatic = sys.isAromatic(i);
				if (isAromatic)
					info.bsAromaticRings.set(i);
				Ring r = new Ring();
				info.rings.add(r);
				int[] bonds = sys.getRingBonds(i);
				//System.out.println(i + " " + Arrays.toString(bonds));
				for (int j = bonds.length; --j >= 0;) {
					int pt = bonds[j] + 1;
					r.bsBonds.set(pt);
					int a1 = mol.bonds[pt].va;
					int a2 = mol.bonds[pt].vb;
					r.bsAtoms.set(a1);
					r.bsAtoms.set(a2);
					r.isAromatic = isAromatic;
					if (mol.atoms[a1].an != Atom.AN_C)
						r.isHetero = true;
				}
				r.size = r.bsAtoms.cardinality();
				r.bondCount = r.bsBonds.cardinality();
				info.bsRingAtoms.or(r.bsAtoms);
				info.bsRingBonds.or(r.bsBonds);
				if (isAromatic)
					info.bsAromaticAtoms.or(r.bsAtoms);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}