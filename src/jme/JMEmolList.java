/**
 * 
 */
package jme;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import jme.canvas.Graphical2DObject;
import jme.canvas.Graphical2DObjectGroup;
import jme.core.Atom;
import jme.core.Bond;
import jme.core.JMECore;
import jme.util.Box;
import jme.util.JMEUtil;

/**
 * Class that parses input string that contains multiple molecules formats,
 * writes out molecules, and manage a collection of JMEmol. Molecules can have a
 * reaction role if the input type is reaction. 
 * 
 * Does not need any JME instance.
 * 
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
	
		public EnsembleAtom(JMEmolList moleculeParts, int molIndex, int atomIndex) {
			assert atomIndex >= 0 && molIndex >= 0;
			int cumulAtomCount = 0;
			int molCount = 0;
			for (JMEmol mol : moleculeParts) {
				molCount++; // Feb 2020: was missing
				molIndex = molCount;
				mol = mol;
				if (atomIndex > cumulAtomCount + mol.nAtoms()) {
					// atomIndex is an ensemble index
					atomEnsembleIndex = atomIndex;
					atomIndex = atomEnsembleIndex - cumulAtomCount;
					break;
				}
				if (molIndex > 0 && molIndex == molCount) {
					atomEnsembleIndex = atomIndex + cumulAtomCount;
					atomIndex = atomIndex;
					break;
				}
				cumulAtomCount += mol.nAtoms();
			}
			if (mol != null) {
				atom = mol.getAtom(atomIndex);
				if (atom == null)
					System.out.println("DETLETED >>>");
			}
		}		
	}
	
	public static class EnsembleBond {
		public int molIndex; // 0 based
		public JMEmol mol;
		public Bond bond;
		public int bondIndex;
		public int bondEnsembleIndex;
			
		public EnsembleBond(JMEmolList molList, int molIndex, int bondIndex) {
			int cumulBondCount = 0;
			int molCount = 0;
			for (JMEmol mol : molList) {
				molCount++;
				molIndex = molCount;
				mol = mol;
				if (bondIndex > cumulBondCount + mol.nBonds()) {
					// bondIndex is an ensemble index
					bondEnsembleIndex = bondIndex;
					bondIndex = bondEnsembleIndex - cumulBondCount;
					break;
				}
				if (molIndex > 0 && molIndex == molCount) {
					bondEnsembleIndex = bondIndex + cumulBondCount;
					bondIndex = bondIndex;
					break;
				}
				cumulBondCount += mol.nBonds();
			}
			if (mol != null) {
				bond = mol.getBond(bondIndex);
			}
		}
	}

	public boolean isReaction = false ;
	private String errorMsg = null;
	String warnng = null;
	public Exception error = null;

	public void removeAll() {
		super.clear();
	}
	
	/**
	 * 
	 * @return null if there are no errors
	 */
	public String getErrorMessage() {
		
		if(errorMsg !=null)
			return errorMsg;
		if(error != null)
			return JME.makeErrorMessage(error);
		
		return null;
	}
	
	public JMEmolList setErrorMsg(String msg) {
		errorMsg = msg;
		removeAll();
		return this;
	}
	
	public boolean isReaction() {
		return isReaction;
	}
	
	public JMEmolList.EnsembleAtom getEnsembleAtom( int mol, int atom) {
		if (mol < 0 || atom < 0) {
			JMEUtil.log("Invalid index for getEnsembleAtom()");
			return null;
		}
		return new JMEmolList.EnsembleAtom(this, mol, atom);
		
	}
	public EnsembleBond getEnsembleBond( int mol, int bond) {
		
		if (mol < 0 || bond < 0) {
			JMEUtil.log("Invalid index for getEnsembleBond()");
			return null;
		}
		return new EnsembleBond(this, mol, bond);
		
	}

	public int computeAtomEnsembleIndex( int mol, int atom) {
		return getEnsembleAtom( mol, atom).atomEnsembleIndex;
	}
	
	public int computeBondEnsembleIndex( int mol, int bond) {
		return getEnsembleBond(mol, bond).bondEnsembleIndex;
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
			//addAll(fragmentList); //very complicated implementation at least in Java
			for(JMEmol each: fragmentList) {
				add(each);
			}	
		}
		return changed;


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
		JMEmolList reactionParts = new JMEmolList();
		for (JMEmol mol : this) {
			if (mol.nAtoms() > 0 && mol.getReactionRole() == reactionRole)
				reactionParts.add(mol);
		}
		if (mergeMolecules && reactionParts.size() > 1) {
			JMEmol merged = JMEmol.mergeMols(reactionParts);
			merged.setReactionRole(reactionRole);
			reactionParts = new JMEmolList();
			reactionParts.add(merged);
		}
		return reactionParts;
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
		double closeContactDist = JMECore.RBOND/10;
		
		int results[] = new int[size() * (size()-1)];
		int rpos = 0;
		
		for(int m1=0; m1< size() - 1; m1++) {
			JMEmol mol1 = get(m1);
		
			for(int m2=m1+1; m2< size() - 1; m1++) {
				JMEmol mol2 = get(m1);
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
				
		for(int m1=0; m1< size() - 1; m1++) {
			JMEmol mol1 = get(m1);
		
			for(int m2=m1+1; m2< size() - 1; m2++) {
				JMEmol mol2 = get(m2);
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
			remove(mol);
		}
			
		return emptyList.size() > 0;
	}
	
	/**
	 *
	 * @return null if no atoms
	 */
	public Rectangle2D.Double computeCoordinate2DboundingBox() {
		
		Rectangle2D.Double boundingBox = null;
		
		//loop through all molecules, extend the bounding box with each molecule
		// correctedBoundingBox uses atom labels
		for(JMEmol mol :this) {			
			//create a new instance each time this method is called
			Rectangle2D.Double moleculeBox = mol.computeCoordinate2DboundingBox(); 
			if(moleculeBox != null) //if no atoms
				boundingBox = (boundingBox == null ? moleculeBox : Box.createUnion(boundingBox, moleculeBox, boundingBox));
		}
		return boundingBox;
	}

	/**
	 * uses atom labels
	 * 
	 * @return null if no atoms
	 */
	public Rectangle2D.Double computeBoundingBoxWithAtomLabels() {
		Rectangle2D.Double boundingBox = null;
		for (JMEmol mol : this) {
			boundingBox = mol.computeBoundingBoxWithAtomLabels(boundingBox);
		}
		return boundingBox;
	}
	/**
	 * 
	 * @return an empty box if no atoms
	 */
	public Rectangle2D.Double safeComputeBoundingBoxWithAtomLabels(double minWidth, double minHeight) {
		
		Rectangle2D.Double boundingBox = computeBoundingBoxWithAtomLabels();
		if (boundingBox == null) {
			boundingBox = new Rectangle2D.Double();
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
		int index = indexOf(mol);
		
		if(index >= 0)
			set(index, newMol);
		
		return index;
	}
	/**
	 * Return a base 1 index
	 * @param mol
	 * @return
	 */
	public int jmeIndex(JMEmol mol) {
		return indexOf(mol) + 1;
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
				int m = mol.getMaxAtomMap();
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
			Graphical2DObject.move(mol, xOrY, dist);
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
				sumlen += mol.bondDistance(i);
			}
			bondCount += n;
		}
		if (bondCount > 0) {
			sumlen = sumlen / bondCount;
			// scale = RBOND / sumlen;
			scale = JMECore.RBOND / sumlen; // BB

		} else {
			for (JMEmol mol : this) {
				if (mol.nAtoms() > 1) {
					scale = 3. * JMECore.RBOND / mol.distance(1, 2); // BB
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
