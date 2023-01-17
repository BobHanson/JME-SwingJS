package jme;

/**
 * Storage of the state of the chemical structures of restoring for by the undo
 * manager
 * 
 * @author bruno
 *
 */
class SavedState {
	JMEmolList moleculePartsList = null; // when multipart, nealokuje !!
	//int numberofMoleculeParts = 0;
	//int actualMoleculePartIndex = 0;
	JMEmol activeMol = null;
	boolean reaction;
	boolean multipart;
	double depictScale = 1.0;
	public int lastAction;
}