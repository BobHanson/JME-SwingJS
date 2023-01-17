package jme;

public class MoleculeHandlingParameters {
	/**
	 * mainly handling of hydrogens removal
	 * @author bruno
	 *
	 */
	public class HydrogenHandlingParameters {
		public boolean keepStereoHs;
		public boolean keepMappedHs;
		public boolean keepIsotopicHs;
		
		public boolean removeHs;
		public boolean removeOnlyCHs;
		
		public boolean showHs;
		
		
		/**
		 * Initialize with default values
		 */
		public HydrogenHandlingParameters() {
	
			removeHs = false;
			removeOnlyCHs = false;
	
			keepStereoHs = true;
			keepMappedHs = true;
			keepIsotopicHs = true;
			
			showHs = true; // on heteroatom labels OH , NH2, ...
			
		}
	}

	public HydrogenHandlingParameters hydrogenHandlingParameters;
	public boolean computeValenceState;
	public boolean ignoreStereo;
	public boolean mark;
	public boolean number; // atom map
	
	
	public boolean showAtomMapNumberWithBackgroundColor; // new Jan 2020

	
	// these two options are related
	public boolean internalBondScalingForInput; // internal scale the molecules from input files - only JME needs this
	public boolean keepSameCoordinatesForOutput; // do not invert y or center x for output
	
	
	//public boolean markerMultiColor;
	//public int singleMarkerColorIndex;
	
	public MoleculeHandlingParameters() {
		hydrogenHandlingParameters = new HydrogenHandlingParameters();
		computeValenceState = true;
		ignoreStereo = false;
		mark = false;
		internalBondScalingForInput = false; 
		keepSameCoordinatesForOutput = true; // Interactive editor: must be false, testing suite: must be true
		//markerMultiColor = false;
		//singleMarkerColorIndex = -1;
	}
}
