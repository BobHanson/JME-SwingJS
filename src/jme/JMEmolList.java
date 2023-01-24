/**
 * 
 */
package jme;

import java.util.ArrayList;
//START JAVA_IMPORT
import java.util.StringTokenizer;

import com.actelion.research.chem.AromaticityResolver;
import com.actelion.research.chem.MolfileCreator;
import com.actelion.research.chem.MolfileParser;
import com.actelion.research.chem.StereoMolecule;
import com.actelion.research.chem.coords.CoordinateInventor; // to compute 2D coordinates

import jme.JME.SupportedFileFormat;
import jme.JMESmiles.SmilesCreationParameters;
import jme.JMEUtil.GWT;

//END JAVA_IMPORT

//START GWT_IMPORT
//import ejava.util.StringTokenizer;
//END GWT_IMPORT









/**
 * Class that parses input string that contains multiple molecules formats
 * ,
 * writes out molecules,  and  manage a collection of JMEmol.
 * Molecules can have a reaction role if the input type is reaction.
 * Does not need any JME instance!!!!
 * @author bruno
 *
 */
@SuppressWarnings("serial")
public class JMEmolList extends ArrayList<JMEmol> {

	public static class EnsembleAtom {
		public int molIndex; // 0 based
		public JMEmol mol;
		public Atom atom;
		public int atomIndex;
		public int atomEnsembleIndex;
	
		public  EnsembleAtom (JMEmolList moleculeParts,  int molIndex,
				int atomIndex) {
	
			assert  atomIndex >= 0 && molIndex >= 0;
			
	
			int cumulAtomCount = 0;
			int molCount = 0;
			FOR_EACH_MOL: for (JMEmol mol: moleculeParts) {
				molCount++; //Feb 2020: was missing
	
				this.molIndex = molCount;
				this.mol = mol;
	
				if (molIndex > 0) {
					if (molIndex == molCount) {
						this.atomEnsembleIndex = atomIndex + cumulAtomCount;
						this.atomIndex = atomIndex;
	
						break FOR_EACH_MOL;
					}
	
				} else {
					// atomIndex is an ensemble index
					if (atomIndex <= cumulAtomCount + mol.nAtoms()) {
						this.atomEnsembleIndex = atomIndex;
						this.atomIndex = this.atomEnsembleIndex - cumulAtomCount;
	
						break FOR_EACH_MOL;
	
					}
	
				}
	
				cumulAtomCount += mol.nAtoms();
	
			}
	
			if (this.mol != null) {
				this.atom = this.mol.getAtom(this.atomIndex);
			}
		}
	
		
	}

	
	// kind of duplicated code with EnsembleAtom
	public static class EnsembleBond {
		public int molIndex; // 0 based
		public JMEmol mol;
		public Bond bond;
		public int bondIndex;
		public int bondEnsembleIndex;
	
		
		public EnsembleBond(JMEmolList molList, int molIndex,
				int bondIndex) {
	
	//		if (molIndex < 0 || bondIndex < 0) {
	//			GWT.log("Invalid index for getEnsembleBond()");
	//			return null;
	//		}
	
	
			int cumulBondCount = 0;
			int molCount = 0;
			FOR_EACH_MOL: for (JMEmol mol: molList) {
				molCount++;
	
				this.molIndex = molCount;
				this.mol = mol;
	
				if (molIndex > 0) {
					if (molIndex == molCount) {
						this.bondEnsembleIndex = bondIndex + cumulBondCount;
						this.bondIndex = bondIndex;
	
						break FOR_EACH_MOL;
					}
	
				} else {
					// bondIndex is an ensemble index
					if (bondIndex <= cumulBondCount + mol.nBonds()) {
						this.bondEnsembleIndex = bondIndex;
						this.bondIndex = this.bondEnsembleIndex - cumulBondCount;
	
						break FOR_EACH_MOL;
	
					}
	
				}
	
				cumulBondCount += mol.nBonds();
	
			}
	
			if (this.mol != null) {
				this.bond = this.mol.getBond(this.bondIndex);
			}
	
		}
	}

	boolean isReaction = false ;
	protected String errorMsg = null;
	String warning = null;
	protected Exception error = null;

	
	
	public void reset() {
		removeAll();
		errorMsg = null;
		error = null;
		isReaction =false;
	}
	
	
	public void removeAll() {
		super.clear();
	}
	
	protected  String makeErrorMessage(Exception e) {
		
		return JME.makeErrorMessage(e);

	}

	/**
	 * 
	 * @return null if there are no errors
	 */
	public String getErrorMessage() {
		
		if(errorMsg !=null)
			return errorMsg;
		if(error != null)
			return makeErrorMessage(error);
		
		return null;
	}
	
	public boolean isReaction() {
		return isReaction;
	}
	
	public JMEmolList.EnsembleAtom getEnsembleAtom( int mol, int atom) {
		// log("computeAtomEnsembleIndex");
		if (mol < 0 || atom < 0) {
			GWT.log("Invalid index for getEnsembleAtom()");
			return null;
		}

		return new JMEmolList.EnsembleAtom(this, mol, atom);
		
	}
	public EnsembleBond getEnsembleBond( int mol, int bond) {
		
		if (mol < 0 || bond < 0) {
			GWT.log("Invalid index for getEnsembleBond()");
			return null;
		}

		return new EnsembleBond(this, mol, bond);
		
	}
	
	
	 // duplicated with 
	// 		// Sept 2019
	// EnsembleAtom ea = this.getEnsembleAtom(molIndex, atomIndex);

	//TODO: move to JME or to mollist object
	// mol : 0 based
	public int computeAtomEnsembleIndex( int mol, int atom) {
		// log("computeAtomEnsembleIndex");
		return this.getEnsembleAtom( mol, atom).atomEnsembleIndex;
		
	}
	
	
	
	// mol : 0 based
	public int computeBondEnsembleIndex( int mol, int bond) {


		return this.getEnsembleBond(mol, bond).bondEnsembleIndex;
		
//		int bondCount = 0;
//		int bondE = 0;
//		
//		for(int m = 1; m <= numberofMoleculeParts; m++) {
//			if(m == mol) {
//				bondE = bond + bondCount;
//				break;
//			}
//			bondCount += moleculeParts[m].nBonds();
//
//		}
//		
//		return bondE;
	}

	/*
	 * parse and read the argument and put the molecules into a vector
	 *
	 */
	//TODO: handle input with 3D or no coordinates
	public boolean readJMEstringInput(String molecule, MoleculeHandlingParameters pars) {
		
		//| is molecular separator
		//> is reaction separator
		
		this.reset();
		StringTokenizer st = new StringTokenizer(molecule, "|>", true);
		isReaction = (molecule.indexOf(">") > -1); //false means it is a molecule
		int nt = st.countTokens();
		int roleIndex = 0;
		
		
		for (int i = 1; i <= nt; i++) {
			String s = st.nextToken();
			s.trim();
			if (s.equals("|"))
				continue;
			if (s.equals(">")) {
				roleIndex++;
				continue;
			}
			
			if(roleIndex > 3) {
				errorMsg = "too many \">\"";
				return false;
			}
			
			JMEmol mol = null;
			
			try {
				mol = new JMEmol(null, s, JME.SupportedFileFormat.JME, pars);
				if (mol.natoms == 0) {
					//this.showError("problems in reading/processing molecule !"); //old original error message
					errorMsg = "0 atoms found in \"" + s + "\"";
					return  false;
				
				}
		
			} catch(Exception e) {
				this.errorMsg = makeErrorMessage(e) + ": " + s;
				return false;
			}
			
			

			this.add(mol);
			if(isReaction) {
				mol.setReactionRole(JMEmol.ReactionRole.all[roleIndex]);
			}

		}

		return true;
	}

	/**
	 * compute 2D coordinates using openchem lib
	 *  Return null if 2D fails.
	 * @param mol
	 * 
	 * HACK!!!: does not use GWT.async !!!!
	 */
	public JMEmol compute2DcoordinatesIfMissing(JMEmol mol)  {
		if (! mol.has2Dcoordinates()) {
			return this.compute2Dcoordinates(mol);
		} else {
			return mol;
		}
		
		
	}

	/**
	 * In most cases, the returned molecule is the same as the input one. If OCL lib
	 * changfed the number of atoms, then another molecule is returned. Return null
	 * if 2D fails.
	 * 
	 * @param mol
	 * @return
	 */
	public JMEmol compute2Dcoordinates(final JMEmol mol) {

		if (mol.nAtoms() <= 1) {
			return mol;
		}

		JMEmol result = mol;
		JMEmol molCopy = mol;

		// OenCHemLib issue: the hydrogens are placed at the end of the CT, thus atom
		// arder is changed
		boolean hasExplicitHydrogens = mol.hasHydrogen();
		if (hasExplicitHydrogens) {
			molCopy = mol.deepCopy();
			for (int i = 1; i <= molCopy.natoms; i++) {
				Atom atom = molCopy.getAtom(i);
				if (atom.an == JME.AN_H) { // replace hydrogen
					atom.an = JME.AN_X;
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
								mol.moleculeHandlingParameters);
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

	public boolean hasAromaticBondType(JMEmol mol) {
		if (mol.nBonds() < 1 ) {
			return false;
		}
		
		for(int b = 1; b <= mol.nBonds(); b++) {
			Bond bond = mol.bonds[b];
			if( bond.bondType == 4 || bond.bondType == JMEmol.AROMATIC || bond.isQuery()) { //TODO: molfile reader should use AROMATIC?
				return true;
			}
		}
		
		return false;

	}
	
	/**
	 * Use OCLib to recompute aromatic or query bond orders (e.g input file with bond order 4)
	 * Return null if an error occurred . If no changes, return the same molecule object otherwise a copy 
	 * @param mol
	 * @return
	 */
	public JMEmol reComputeBondOrderIfAromaticBondType(JMEmol mol) {
		if (! hasAromaticBondType(mol)) {
			return mol; // no changes
		}
		// create a molfile
		String molFile = mol.createMolFile("");
		JMEmol result = mol.deepCopy();
		
		// create a OCL molecule
		StereoMolecule oclMol = new StereoMolecule();
		if (new MolfileParser().parse( oclMol,  molFile) ) {
			
			if (!(oclMol.getAllAtoms() == mol.nAtoms() && oclMol.getAllBonds() == mol.nBonds())) {
				return null;
			}
			
			boolean computeBondOrder = true;
			
			try {
				AromaticityResolver bondFixer = new AromaticityResolver(oclMol);
				bondFixer.locateDelocalizedDoubleBonds(null);
			} catch(Exception e) {
				computeBondOrder = false;
				result = null;
			}
			
			if (computeBondOrder) {
				// assume that the order of the atoms is the same
					
				for(int b = 0; b < oclMol.getAllBonds(); b++) {
					int bo = oclMol.getBondOrder(b);
					int at1 = oclMol.getBondAtom(0, b);
					int at2 = oclMol.getBondAtom(1, b);
					int charge1 = oclMol.getAtomCharge(at1);
					int charge2 = oclMol.getAtomCharge(at2);
					
					
					
					at1++;
					at2++;
					
					if (mol.q(at1) != charge1 || mol.q(at2) != charge2) {
						return null;
					}

					Bond bond = result.bonds[b+1];
					if ((at1 == bond.va && at2 == bond.vb)|| (at2 == bond.va && at1 == bond.vb)) {
						bond.bondType = bo;
					} else {
						return null;
					}
					//System.out.println(bo);  // 1 or 2
				}


			}
			
		}
		
		return result;		
		
		

	}

	// can raise exception
	public boolean readMDLstringInput(String s, MoleculeHandlingParameters pars) {

		this.reset();
		isReaction = s.startsWith("$RXN");
		if (!isReaction) {
			return this.readSingleMOL(s, pars);
		}
		String separator = JMEUtil.findLineSeparator(s);
		StringTokenizer st = new StringTokenizer(s, separator, true);
		String line = "";
		for (int i = 1; i <= 5; i++) {
			line = JMEUtil.nextData(st, separator);
		}
		// TODO: exception handling
		int nr = Integer.valueOf(line.substring(0, 3).trim()).intValue();
		int np = Integer.valueOf(line.substring(3, 6).trim()).intValue();
		// support of agents, this is not standard, same convention as in Marvin JS
		int na = 0;
		if (line.length() >= 9) {
			na = Integer.valueOf(line.substring(6, 9).trim()).intValue();
		}

		JMEUtil.nextData(st, separator); // 1. $MOL
		for (int p = 1; p <= nr + np + na; p++) {
			String m = "";
			while (true) {
				String ns = JMEUtil.nextData(st, separator);
				if (ns == null || ns.equals("$MOL"))
					break;
				else
					m += ns + separator;
			}
			// System.err.print("MOLS"+p+separator+m);
			if (!this.readSingleMOL(m, pars)) {
				return false;
			}
			JMEmol mol = this.last();
			if (p <= nr) {
				mol.setReactionRole(JMEmol.ReactionRole.REACTANT);
			} else if (p > nr && p <= nr + np) {
				mol.setReactionRole(JMEmol.ReactionRole.PRODUCT);
			} else {
				mol.setReactionRole(JMEmol.ReactionRole.AGENT);
			}
		}
		return true;
	}

	public boolean readJmolAdaptorInput(Object[] iterators, MoleculeHandlingParameters params) {
		try {
			add(new JMEmol(null, iterators, SupportedFileFormat.JMOL, params));
			return true;
		} catch (Exception e) {
			this.error = e;
			return false;
		}
	}

	/*
	 * MDL MOL
	 */
	private boolean readSingleMOL(String s, MoleculeHandlingParameters pars) {
		try {
			add(new JMEmol(null, s, JME.SupportedFileFormat.MOL, pars));
			return true;
		} catch (Exception e) {
			this.error = e;
			return false;
		}
	}

	/**
	 * Needed after jme.showhydrogens has changed
	 */
	public void reComputeAtomLabels() {
		for( JMEmol mol : this) {
			mol.computeAtomLabels();
		}
	}
	/**
	 * Split  my molecules that are fragmented
	 * @param removeEmpty: remove empty molecules from my list
	 * @return true if there was any change
	 */
	public boolean splitFragments(boolean removeEmpty) {
		JMEmolList fragmentList = new JMEmolList();
		boolean changed = false;
		
		
		for(JMEmol each: this) {
			int nparts = each.computeMultiPartIndices();
			JME jme = each.jme;
			if(nparts == 1) {
				fragmentList.add(each);
			} else if(nparts == 0) {
				if(removeEmpty) {
					changed = true; //skip empty molecules
				} else
					fragmentList.add(each);
				
			} else {
				for (int p = 1; p <= nparts; p++) {
					//extract each part and append it to the fragmentList
					JMEmol newFragment = new JMEmol(jme, each, p, null);
					newFragment.setReactionRole(each.getReactionRole());
					fragmentList.add(newFragment); //new implementation that uses the internal partIndex variable
				}
				changed = true;
						
			}
		}
		
		if(changed) {
			removeAll();
			//this.addAll(fragmentList); //very complicated implementation at least in Java
			for(JMEmol each: fragmentList) {
				this.add(each);
			}
			
		}
		
		return changed;


	}
	
	/**
	 * First is reactant, then agent, then product
	 * @return
	 */
//	public JMEmolList[] groupByReactionRole()  {
//		JMEmolList groups[] = new JMEmolList[3];
//		groups[0] = new JMEmolList();
//		groups[1] = new JMEmolList();
//		groups[2] = new JMEmolList();
//		
//		for(JMEmol each: this) {
//			int role = each.getReactionRole();
//			switch(role) {
//			case ReactionRole.REACTANT:
//				groups[0].add(each);
//				break;
//			case ReactionRole.AGENT:
//				groups[1].add(each);
//				break;
//			case ReactionRole.PRODUCT:
//				groups[2].add(each);
//				break;
//			}
//			
//		}
//
//		return groups;
//	}
	
	/**
	 * Arguments for generateMolFileOrRxn()
	 * TODO: should be merged in MolecularHandlingParameters
	 * @author bruno
	 *
	 */
	public static class MolFileOrRxnParameters {
		public String header = "";
		public boolean stampDate = false;
		public boolean isV3000 = false;
		public boolean mergeReationComponents = false;
		public boolean debugDoNotUpdateReactionRole = false;
	}




	/**
	 * 
	 * @param showImplicitHydrogens : atom symbols like CH3
	 * @return
	 */
	public String generateJMEstring(boolean showImplicitHydrogens, Box boundingBox) {
		String result = "";
		
		if (isReaction) {
			
			result = reactionParts(JMEmol.ReactionRole.REACTANT).generateJMEstring(showImplicitHydrogens, boundingBox) + ">" + 
					 reactionParts(JMEmol.ReactionRole.AGENT).generateJMEstring(showImplicitHydrogens, boundingBox)    + ">" +
					 reactionParts(JMEmol.ReactionRole.PRODUCT).generateJMEstring(showImplicitHydrogens, boundingBox);
		
		} else {
			for(JMEmol mol : this) {
				String jme = mol.createJME(showImplicitHydrogens, boundingBox);
				if (jme.length() > 0) {
					if (result.length() > 0)
						result += "|";
					result += jme; // ta molekula moze byt empty
				}
			}
			
		}
		return result;
	}
	
	/**
	 * 
	 * @param reactionRole
	 * @return a list of molecules with the given reaction role
	 */
	public JMEmolList reactionParts(int reactionRole) {
		return reactionParts(reactionRole, false);
	}
	
	public JMEmolList reactionParts(int reactionRole, boolean mergeMolecules) {
		JMEmolList reactionParts =  new JMEmolList();
		
		for(JMEmol mol : this) {
			if(mol.nAtoms() > 0 && mol.getReactionRole()==reactionRole )
				reactionParts.add(mol);
		}
		
		if(mergeMolecules && reactionParts.size() > 1 ) {
			JMEmol merged = JMEmol.mergeMols(reactionParts);
			merged.setReactionRole(reactionRole);
			reactionParts = new JMEmolList();
			reactionParts.add(merged);
		}
		
		return reactionParts;
	

	}

	
	//duplicated code with generateJmeFile
	public String generateSmilesOrSmirks(SmilesCreationParameters pars) {
		String result = "";
		
		if (isReaction) {
			
			result = reactionParts(JMEmol.ReactionRole.REACTANT).generateSmilesOrSmirks(pars) + ">" + 
					 reactionParts(JMEmol.ReactionRole.AGENT).generateSmilesOrSmirks(pars)    + ">" +
					 reactionParts(JMEmol.ReactionRole.PRODUCT).generateSmilesOrSmirks(pars);
		
		} else {
			for(JMEmol mol : this) {
				String smiles = mol.createSmiles(pars);
				if (smiles.length() > 0) {
					if (result.length() > 0)
						result += ".";
					result += smiles; // ta molekula moze byt empty
				}
			}
			
		}
		return result;
	}

	
	/**
	 * Generate  MOL file, or RXN reaction file
	 * @param MolFileOrRxnParameters instance
	 * @return
	 */
	public String generateMolFileOrRxn(MolFileOrRxnParameters pars) {
		
		
		
		String s = "";
		String newLine = "\n";
		
		if (isReaction) {
			int roles[];
		
			
			//suppress the agents if none because writing out agents is not standard
			if(reactionParts(JMEmol.ReactionRole.AGENT).size() > 0) {
				roles = new int[] {JMEmol.ReactionRole.REACTANT, JMEmol.ReactionRole.PRODUCT, JMEmol.ReactionRole.AGENT};
			} else {
				roles = new int[] {JMEmol.ReactionRole.REACTANT, JMEmol.ReactionRole.PRODUCT};
				
			}

			s += "$RXN" + newLine + newLine + newLine
					+ "JME Molecular Editor" + newLine;

			//write the number of molecules for each role
			for(int r : roles) {
				int n =  pars.mergeReationComponents ? 1 :reactionParts(r).size();
				s += JMEUtil.iformat(n, 3);
			}

			s += newLine;
			
			//reactants products, agents
			for(int r : roles) {
				ArrayList<JMEmol> mols =  reactionParts(r, pars.mergeReationComponents);
				
				for(JMEmol mol: mols) {
					s += "$MOL" + newLine;
					s += this.createMolfile(mol, pars);
				}
			}
				
		} else { // viac molekul do 1 mol file
			//we could create a SDF
			JMEmol mol ;
			if(size() > 1) {
				mol = JMEmol.mergeMols(this);
			} else {
				mol = get(0);
			}
			s = this.createMolfile(mol, pars);

		}
		return s;
	}

	
	protected String createMolfile(JMEmol mol, MolFileOrRxnParameters arg) {
		
		String s;
		
		if(! arg.isV3000) 
			s = mol.createMolFile(arg.header, arg.stampDate);
		else //BB
			s = mol.createExtendedMolFile(arg.header, arg.stampDate);
		
		return s;
	}
	
	public JMEmol last() {
		return get(size() - 1);
	}

	public JMEmol first() {
		return get(0);
	}
	
	
	// NOT TESTED
	/**
	 * Compute the 2D overlap between each pair of molecules
	 * @return
	 */
	public int[] overlap() {
		double closeContactDist = JMEmol.RBOND/10;
		
		int results[] = new int[this.size() * (this.size()-1)];
		int rpos = 0;
		
		for(int m1=0; m1< this.size() - 1; m1++) {
			JMEmol mol1 = this.get(m1);
		
			for(int m2=m1+1; m2< this.size() - 1; m1++) {
				JMEmol mol2 = this.get(m1);
				if(mol1.hasCloseContactWith(mol2, closeContactDist)) {
					results[rpos++] = m1;
					results[rpos++] = m2;
				}
			}
		}
		
		return results;
	}

	//not tested
	public boolean hasCloseContact(double closeContactDist) {
				
		for(int m1=0; m1< this.size() - 1; m1++) {
			JMEmol mol1 = this.get(m1);
		
			for(int m2=m1+1; m2< this.size() - 1; m2++) {
				JMEmol mol2 = this.get(m2);
				if(mol1.hasCloseContactWith(mol2, closeContactDist)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	//not tested
	public boolean removeEmptyMolecules() {
		JMEmolList emptyList= new JMEmolList();
		for(JMEmol mol :this) {
			if(mol.nAtoms() == 0) {
				emptyList.add(mol);
			}
		}
		
		for(JMEmol mol : emptyList) {
			this.remove(mol);
		}
			
		return emptyList.size() > 0;
	}
	
	/**
	 *
	 * @return null if no atoms
	 */
	public Box computeCoordinate2DboundingBox() {
		
		Box boundingBox = null;
		
		//loop through all molecules, extend the bounding box with each molecule
		// correctedBoundingBox uses atom labels
		for(JMEmol mol :this) {			
			//create a new instance each time this method is called
			Box moleculeBox = mol.computeCoordinate2DboundingBox(); 
			if(moleculeBox != null) //if no atoms
				boundingBox = (boundingBox == null ? moleculeBox : boundingBox.createUnion(moleculeBox, boundingBox));
		}
		return boundingBox;
	}
		/**
		 * uses atom labels
		 * @return null if no atoms
		 */
	public Box computeBoundingBoxWithAtomLabels() {			
			Box boundingBox = null;
			for(JMEmol mol : this) {
				boundingBox = mol.computeBoundingBoxWithAtomLabels(boundingBox);
			}
			return boundingBox;
	}
	/**
	 * 
	 * @return an empty box if no atoms
	 */
	public Box safeComputeBoundingBoxWithAtomLabels(double minWidth, double minHeight) {
		
		Box boundingBox = this.computeBoundingBoxWithAtomLabels();
		if (boundingBox == null) {
			boundingBox = new Box();
		}
		
		
		boundingBox.width = Math.max(boundingBox.width, minWidth);
		boundingBox.height = Math.max(boundingBox.height, minHeight);
		
		return boundingBox;
	}	
	//ell is evenly distributed across the width of the cell, with flush right and left margins.
	/**
	 * 
	 */
	public void distributeAndCenterPositionsHorizontally(double margin) {
		distributePositions(Box.Axis.X, margin);
		alignCenter(Box.Axis.Y);
	}
	public void distributeAndCenterPositionsVertically(double margin) {
		distributePositions(Box.Axis.Y, margin);
		alignCenter(Box.Axis.X);
	}
	
	/**
	 * Might look more distanced than the provided margin because boundingBoxpadding is used to compute size of the 
	 * molecule bounding box
	 * @param xOrY
	 * @param margin
	 */
	public void distributePositions(final Box.Axis xOrY, double margin) {
		
		Graphical2DObjectGroup<JMEmol> distributer = new Graphical2DObjectGroup<JMEmol>();
		distributer.addAll(this);
		distributer.distributePositions(xOrY, margin);
	}
	
	public void alignCenterY() {
		alignCenter(Box.Axis.Y);
	}
	
	/**
	 * Align all my molecules along the given axis
	 * @param xOrY
	 */
	public void alignCenter(Box.Axis xOrY) {
		
		Graphical2DObjectGroup<JMEmol> aligner = new Graphical2DObjectGroup<JMEmol>(this);
		aligner.alignCenter(xOrY);
		
	}
	/**
	 * Replace the first occurrence of the given mol by a new one
	 * @param mol
	 * @param newMol
	 * @return the position of the new molecule in the list
	 */
	public int replace(JMEmol mol, JMEmol newMol) {
		int index = this.indexOf(mol);
		
		if(index >= 0)
			this.set(index, newMol);
		
		return index;
	}
	/**
	 * Return a base 1 index
	 * @param mol
	 * @return
	 */
	public int jmeIndex(JMEmol mol) {
		return this.indexOf(mol) + 1;
	}
	

	/**
	 * Find the largest atom map among my molecules having the given reaction role
	 * @param reactionRole
	 * @return
	 */
	public int findMaxAtomMap(int reactionRole) {
		
		int max = Integer.MIN_VALUE;
		
		for(JMEmol mol : this) {
			if(reactionRole == JMEmol.ReactionRole.ANY || mol.getReactionRole() == reactionRole) {
				int m = mol.geMaxAtomMap();
				if(m>max)
					max = m;
			}
		}

		return max;
	}
	
	/**
	 * Find the largest atom map among my molecules.
	 * @return
	 */
	public int findMaxAtomMap() {
		return findMaxAtomMap(JMEmol.ReactionRole.ANY);
	}


	public boolean hasMarkedAtom() {
		for(JMEmol mol : this) {
			if (mol.hasMarkedAtom()) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean hasOneMoleculeWithChiralFlag() {
		for(JMEmol mol : this) {
			if (mol.getChiralFlag()) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public JMEmolList deepCopy() {
		
		JMEmolList copy = new JMEmolList();
		
		for(JMEmol mol : this) {
			copy.add(mol.deepCopy());
		}
		return copy;
	}
	
	/**
	 * 
	 * @return false if at least one of my molecule is not empty
	 */
	public boolean isReallyEmpty() {
	
		for(JMEmol mol: this) {
			if(mol.nAtoms() > 0) {
				return false;
			}
		}
		
		return true;

	}

	/**
	 * 
	 * @return the reference bond length that was used for scaling
	 */
	
	public void scaleXY(double scale) {
		for(JMEmol mol: this) {
			mol.scaleXY(scale);
		}
	}
	public void moveXY(double dx, double dy) {
		for(JMEmol mol: this) {
			mol.moveXY(dx, dy);
		}
	}
	public void move(Box.Axis xOrY, double dist) {
		for(JMEmol mol: this) {
			mol.move(xOrY, dist);
		}
	}

	public JMEmolList setAtomBackGroundColors(String s) {
		if(s != null)
			for(JMEmol mol : this) {
				mol.setAtomColors(s, 0);
			}
		
		return this;
	}



	
	//FIXME: what if different references were used? FOr instance, one molecule has no 2D coordinates.
	// eack molecule is scaled indepently - this is better for SMIRKS input
	public double internalBondLengthScaling() {
		double referenceBondLength = 0;
		
		for(JMEmol mol: this) {
			referenceBondLength = mol.internalBondLengthScaling();
		}
		
		return referenceBondLength;
	}
	
	
	
	// October 2019 dirty fix instead of using the new JMEmolList class
	// Duplicated code with scaling()
	public void scaleInternalBondMolList() {
		double sumlen = 0, scale = 0;
		int bondCount = 0;
		for (JMEmol mol : this) {
			int n = mol.nbonds;
			for (int i = 1; i <= n; i++) {
				Bond b = mol.bonds[i];
				double dx = mol.x(b.va) - mol.x(b.vb);
				double dy = mol.y(b.va) - mol.y(b.vb);
				sumlen += Math.sqrt(dx * dx + dy * dy);
			}
			bondCount += n;
		}
		if (bondCount > 0) {
			sumlen = sumlen / bondCount;
			// scale = RBOND / sumlen;
			scale = this.first().RBOND() / sumlen; // BB

		} else {
			for (JMEmol mol : this) {
				;
				if (mol.nAtoms() > 1) {
					scale = 3. * mol.RBOND() / Math.sqrt((mol.x(1) - mol.x(2)) * (mol.x(1) - mol.x(2))
							+ (mol.y(1) - mol.y(2)) * (mol.y(1) - mol.y(2))); // BB

					break;
				}

			}

		}
		if (scale > 0) {
			for (JMEmol mol : this) {
				mol.scaleXY(scale);
			}
		}

	}

	public int totalNumberOfAtoms() {
		int cumulAtomCount = 0;
		for(JMEmol mol: this) {
			cumulAtomCount += mol.nAtoms();
		}

		return cumulAtomCount;
	}

	public int totalNumberOfBonds() {
		int cumulBOndCount = 0;
		for(JMEmol mol: this) {
			cumulBOndCount += mol.nBonds();
		}

		return cumulBOndCount;
	}
	
	public void resetTouchedAtomAndBond() {
		for(JMEmol mol: this) {
			mol.touchedAtom = mol.touchedBond = 0;
		}
	}
	
	public Graphical2DObjectGroup<Graphical2DObject> asGroup() {
		Graphical2DObjectGroup<Graphical2DObject> newGroup = new Graphical2DObjectGroup<Graphical2DObject>();
		
		for( JMEmol mol : this) {
			newGroup.add(mol);
		}
		return newGroup;
		
	}


}
