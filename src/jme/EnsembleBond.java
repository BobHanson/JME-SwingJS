package jme;

import jme.core.Bond;

//kind of duplicated code with EnsembleAtom
class EnsembleBond {
	public int molIndex; // 0 based
	public JMEmol mol;
	public Bond bond;
	public int bondIndex;
	public int bondEnsembleIndex;

	public static EnsembleBond getEnsembleBond(JMEmolList molList, int molIndex,
			int bondIndex) {

		if (molIndex < 0 || bondIndex < 0) {
			JMEUtil.log("Invalid index for getEnsembleBond()");
			return null;
		}

		EnsembleBond result = new EnsembleBond();

		int cumulBondCount = 0;
		int molCount = 0;
		FOR_EACH_MOL: for (JMEmol mol: molList) {
			molCount++;

			result.molIndex = molCount;
			result.mol = mol;

			if (molIndex > 0) {
				if (molIndex == molCount) {
					result.bondEnsembleIndex = bondIndex + cumulBondCount;
					result.bondIndex = bondIndex;

					break FOR_EACH_MOL;
				}

			} else {
				// bondIndex is an ensemble index
				if (bondIndex <= cumulBondCount + mol.nBonds()) {
					result.bondEnsembleIndex = bondIndex;
					result.bondIndex = result.bondEnsembleIndex - cumulBondCount;

					break FOR_EACH_MOL;

				}

			}

			cumulBondCount += mol.nBonds();

		}

		if (result.mol != null) {
			result.bond = result.mol.getBond(result.bondIndex);
		}
		return result;

	}
}