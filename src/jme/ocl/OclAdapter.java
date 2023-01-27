package jme.ocl;

import com.actelion.research.chem.AbstractDepictor;
import com.actelion.research.chem.AromaticityResolver;
import com.actelion.research.chem.IDCodeParser;
//OpenChemLib import
import com.actelion.research.chem.MolfileCreator;
import com.actelion.research.chem.MolfileParser;
import com.actelion.research.chem.SVGDepictor;
import com.actelion.research.chem.SmilesParser;
import com.actelion.research.chem.StereoMolecule;
import com.actelion.research.chem.coords.CoordinateInventor;
import com.actelion.research.gui.generic.GenericRectangle;

import jme.JME;
import jme.JMEmol;
import jme.Parser;
import jme.core.Atom;
import jme.core.Bond;

public class OclAdapter implements Parser {

	public OclAdapter() {
	 // for reflection	
	}
	
	@Override
	public String getOclCode(String molFile) {
		// TODO : error handling
		String result = null;
		StereoMolecule mol = new StereoMolecule();
		if (new MolfileParser().parse(mol, molFile)) {

			result = mol.getIDCode();
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getOclSVG(String molFile) {
		double width = 400;
		double height = 300;
		// TODO : error handling
		StereoMolecule mol = new StereoMolecule();
		if (new MolfileParser().parse(mol, molFile)) {
			// recipe found in openchemlib-js
			SVGDepictor svgd = new SVGDepictorWithEmbeddedChemicalStructure(mol, molFile);
			svgd.setLegacyMode(false); // include font information
			svgd.validateView(null, new GenericRectangle(0, 0, width, height),
					AbstractDepictor.cModeInflateToHighResAVBL);
			svgd.paint(null);
			return svgd.toString();
		}
		return null;
	}

	@Override
	public String OclCodeToMOL(String oclCode) {
		StereoMolecule mol = new IDCodeParser().getCompactMolecule(oclCode.trim());
		MolfileCreator mfc = new MolfileCreator(mol);
		return mfc.getMolfile();

	}

	@Override
	public String SMILEStoMOL(String smiles) throws Exception {

		// OCLSmilesParser generates an exception if the SMILES is empty
		if (smiles == null || smiles.length() == 0 || smiles.trim().length() == 0)
			return new JMEmol().createMolFile(""); // empty mol

		StereoMolecule mol = new StereoMolecule();
		new SmilesParser().parse(mol, smiles.trim());

		MolfileCreator mfc = new MolfileCreator(mol);
		return mfc.getMolfile();
	}

	@Override
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
						result = new JMEmol(mol.jme, mfc.getMolfile(), JME.SupportedFileFormat.MOL,
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



}