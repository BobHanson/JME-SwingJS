package jme;

////START JAVA_IMPORT

//import gwt_compat.JavaScriptObject;

public class JMEevent {
	// toString is needed thus, enum cannot be used in Java
	// public enum Origin {API, PASTE, DROP, GUI};
	static public class Origin {
		static String API = "API";
		static String PASTE = "PASTE";
		static String DROP = "DROP";
		static String GUI = "GUI";
		static String INIT = "INIT";
	}

	public int atom;
	public int bond;
	public int bondAtoms[];
	public int atomBackgroundColorIndex;

	// atom and indexing for the ensemble
	// can differ if there are more than one fragment
	public int atomE;
	public int bondE;
	public int bondAtomsE[];

	public int molecule;
	public String action;
	public String argument;
	public Object argumentJSO;

	public String origin;

	public int stackLevel = 0;

	public JMEevent() {
		reset();
	}

	/**
	 * useful to keep only one instance and thus avoid create and delete
	 * 
	 * @return
	 */
	public JMEevent reset() {
		atom = 0;
		bond = 0;
		molecule = 0;
		atomBackgroundColorIndex = -1;

		bondAtoms = new int[] { 0, 0 };

		atomE = 0;
		bondE = 0;
		bondAtomsE = new int[] { 0, 0 };

		action = null;
		argument = null;
		origin = null;

		stackLevel = 0;
		return this;
	}

	public int getAtom() {
		return atom;
	}

	public void log(String string) {
		consoleLog(string); // use this for debugging only, not for production
	}

	private native void consoleLog(String msg) /*-{
												//console tested on Chrome with success
												//On IE: the console is not available unless debugger is on: causes an exception
												try {
												$wnd.console.log(msg);
												//$wnd.alert("Warning: the global function \"jsmeOnLoad\" is not defined.\n If you have defined it, there might be a syntax error in your javascript code.");
												} catch(err) {
												}
												
												}-*/;

	// mol: 0 based
	public JMEevent setAtomAndMol(JMEmolList moleculeParts, int atom, int mol) {
		this.atom = atom;
		molecule = mol + 1;

		atomE = moleculeParts.computeAtomEnsembleIndex(mol, atom);

		JMEmolList.EnsembleAtom eAtom = moleculeParts.getEnsembleAtom(mol, atom);

		if (eAtom != null) {
			AtomBondCommon atomObj = eAtom.atom;
			if (atomObj == null) {
				assert (false);
			}
			atomBackgroundColorIndex = atomObj.getMark();

		}

		return this;
	}

	public int getBond() {
		return bond;
	}

	// mol: 0 based
	public JMEevent setBondAndMol(JMEmolList moleculeParts, int bond, int mol) {
		this.bond = bond;

		if (bond > 0 && mol >= 0) {
			molecule = mol;
			JMEmol jmeMol = moleculeParts.get(mol);

			bondAtoms[0] = jmeMol.bonds[bond].va;
			bondAtoms[1] = jmeMol.bonds[bond].vb;

			bondE = moleculeParts.computeBondEnsembleIndex(mol, bond);

			bondAtomsE[0] = moleculeParts.computeAtomEnsembleIndex(mol, bondAtoms[0]);
			bondAtomsE[1] = moleculeParts.computeAtomEnsembleIndex(mol, bondAtoms[1]);
		}

		return this;
	}

	public String getAction() {
		return action;
	}

	public JMEevent setAction(String action) {
		this.action = action;
		return this;
	}

	public JMEevent setAtomAndBondAndMol(JMEmolList moleculeParts, int atom, int bond, int mol) {
		this.setBondAndMol(moleculeParts, bond, mol);
		return this.setAtomAndMol(moleculeParts, atom, mol);

	}

	public JMEevent setArgument(String arg) {
		this.argument = arg;
		return this;
	}

//	public JMEevent setArgument(JavaScriptObject arg) {
//		this.argumentJSO = arg;
//		return this;
//	}

	public void setOrigin_API() {
		this.setOriginIfNotAlreadySet(Origin.API);

	}

	public void setOrigin_GUI() {
		this.setOriginIfNotAlreadySet(Origin.GUI);

	}

	public void setOrigin_DROP() {
		this.setOriginIfNotAlreadySet(Origin.DROP);

	}

	public void setOrigin_INIT() {
		this.setOriginIfNotAlreadySet(Origin.INIT);
	}

	public void setOrigin_PASTE() {
		this.setOriginIfNotAlreadySet(Origin.PASTE);
	}

	private void setOriginIfNotAlreadySet(String newOrigin) {
		if (this.origin == null) {
			this.origin = newOrigin;
		}

	}

	public boolean isOrigin_API() {

		return this.origin == Origin.API;
	}

	public String toString() {
		return "[JMEEvent " + action 
				+ (origin == null ? "" : " origin=" + origin)
				+ (atom > 0 ? " atom=" + atom : "")
				+ (bond > 0 ? " bond=" + bond + "(" + bondAtoms[0] + "-" + bondAtoms[1] + ")" : "") 
				+ "]";
	}
}
