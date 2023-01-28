package jme;

import jme.core.Atom;

class EnsembleAtom {
	public int molIndex; // 0 based
	public JMEmol mol;
	public Atom atom;
	public int atomIndex;
	public int atomEnsembleIndex;

	public static EnsembleAtom getEnsembleAtom(JMEmolList moleculeParts,  int molIndex,
			int atomIndex) {

		if (molIndex < 0 || atomIndex < 0) {
			JMEUtil.log("Invalid index for getEnsembleAtom()");
			return null;
		}

		EnsembleAtom result = new EnsembleAtom();

		int cumulAtomCount = 0;
		int molCount = 0;
		FOR_EACH_MOL: for (JMEmol mol: moleculeParts) {
			molCount++; //Feb 2020: was missing

			result.molIndex = molCount;
			result.mol = mol;

			if (molIndex > 0) {
				if (molIndex == molCount) {
					result.atomEnsembleIndex = atomIndex + cumulAtomCount;
					result.atomIndex = atomIndex;

					break FOR_EACH_MOL;
				}

			} else {
				// atomIndex is an ensemble index
				if (atomIndex <= cumulAtomCount + mol.nAtoms()) {
					result.atomEnsembleIndex = atomIndex;
					result.atomIndex = result.atomEnsembleIndex - cumulAtomCount;

					break FOR_EACH_MOL;

				}

			}

			cumulAtomCount += mol.nAtoms();

		}

		if (result.mol != null) {
			result.atom = result.mol.getAtom(result.atomIndex);
		}
		return result;

	}
}