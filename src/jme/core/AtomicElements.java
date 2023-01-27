/**
 * 
 */
package jme.core;

import java.util.HashMap;

/**
 * @author bruno
 *
 */
public class AtomicElements {
	//gain: only 80 bytes when compressed - gzip is very smart
	public static HashMap<String, int[]> isotopes;
	
	public final static HashMap<String, int[]> getIsotopicMap() {
		return (isotopes == null? initIsotopMap() : isotopes);
	}
	//this is not up to date - new elements have been discovered
	static HashMap<String, int[]> initIsotopMap() {
		String dataStr = "H.1,2,3." +
				"He.3,4." +
				"Li.6,7." +
				"Be.9." +
				"B.10,11." +
				"C.12,13,14." +
				"N.14,15." +
				"O.16,17,18." +
				"F.19." +
				"Ne.20,21,22." +
				"Na.23." +
				"Mg.24,25,26." +
				"Al.27." +
				"Si.28,29,30." +
				"P.31." +
				"S.32,33,34,36." +
				"Cl.35,37." +
				"Ar.36,38,40." +
				"K.39,40,41." +
				"Ca.40,42,43,44,46,48." +
				"Sc.45." +
				"Ti.46,47,48,49,50." +
				"V.50,51." +
				"Cr.50,52,53,54." +
				"Mn.55." +
				"Fe.54,56,57,58." +
				"Co.59." +
				"Ni.58,60,61,62,64." +
				"Cu.63,65." +
				"Zn.64,66,67,68,70." +
				"Ga.69,71." +
				"Ge.70,72,73,74,76." +
				"As.75." +
				"Se.74,76,77,78,80,82." +
				"Br.79,81." +
				"Kr.78,80,82,83,84,86." +
				"Rb.85,87." +
				"Sr.84,86,87,88." +
				"Y.90." +
				"Zr.90,91,92,94,96." +
				"Nb.93." +
				"Mo.92,94,95,96,97,98,100." +
				"Tc.99." +
				"Ru.96,98,99,100,101,102,104." +
				"Rh.103." +
				"Pd.102,104,105,106,108,110." +
				"Ag.107,109." +
				"Cd.106,108,110,111,112,113,114,116." +
				"In.113,115." +
				"Sn.112,114,115,116,117,118,119,120,122,124." +
				"Sb.121,123." +
				"Te.120,122,123,124,125,126,128,130." +
				"I.127." +
				"Xe.124,126,128,129,130,131,132,134,136." +
				"Cs.133." +
				"Ba.130,132,134,135,136,137,138." +
				"La.138,139." +
				"Ce.136,138,140,142." +
				"Pr.141." +
				"Nd.142,143,144,145,146,148,150." +
				"Pm.145." +
				"Sm.144,147,148,149,150,152,154." +
				"Eu.151,153." +
				"Gd.152,154,155,156,157,158,160." +
				"Tb.159." +
				"Dy.156,158,160,161,162,163,164." +
				"Ho.165." +
				"Er.162,164,166,167,168,170." +
				"Tm.169." +
				"Yb.168,170,171,172,173,174,176." +
				"Lu.175,176." +
				"Hf.174,176,177,178,179,180." +
				"Ta.180,181." +
				"W.180,182,183,184,186." +
				"Re.185,187." +
				"Os.184,186,187,188,189,190,192." +
				"Ir.191,193." +
				"Pt.190,192,194,195,196,198." +
				"Au.197." +
				"Hg.197,198,199,200,201,202,204." +
				"Tl.203,205." +
				"Pb.204,206,207,208." +
				"Bi.209." +
				"Po.209." +
				"At.210." +
				"Rn.222." +
				"Fr.223." +
				"Ra.226." +
				"Ac.227." +
				"Th.232." +
				"Pa.231." +
				"U.234,235,238." +
				"Np.237." +
				"Pu.244." +
				"Am.243." +
				"Cm.247." +
				"Bk.247." +
				"Cf.251." +
				"Es.254." +
				"Fm.257." +
				"Md.258." +
				"No.255." +
				"Lr.256." +
				"Ku.261";

		String data[] = dataStr.split("\\.");
		isotopes = new HashMap<String, int[]>();		
		for(int i = 0 ; i < data.length - 1; i+= 2) {
			String symbol = data[i];
			String[] parts = data[i+1].split(",");
			int a[] = new int[parts.length];
			for(int j = 0; j < parts.length; j++) {
				a[j] = Integer.parseInt(parts[j]);
			}
			isotopes.put(symbol, a);
		}
		return isotopes;
	}

	/**
	 * return -1 for unknown symbol
	 * @param elementSymbol
	 * @param delta
	 * @return
	 */
	public static int getIsotopicMassOfElementDelta(String elementSymbol, int delta) {
		int m = getNaturalMass(elementSymbol);
		return (m>0 ? m + delta : -1);
	}
	
	public static int getDeltaIsotopicMassOfElement(String elementSymbol, int isotopMass) {
		int m = getNaturalMass(elementSymbol);
		return (m>0 ? isotopMass - m : -1);
	}
	/**
	 * return 12 for "C", 16 for "N", ... or -1 if the symbol is not valid
	 * @param elementSymbol
	 * @return
	 */
	public static int getNaturalMass(String elementSymbol) {
		int[] isotopes = AtomicElements.getIsotopicMap().get(elementSymbol);
		return (isotopes == null ? -1 : isotopes[0]);
	}
	/**
	 * Return true if isotop is one of the known isotopes of the given element symbol.
	 * Example: "C", 13 : return true
	 * @param elementSymbol
	 * @param isotop
	 * @return
	 */
	public static boolean isKnown(String elementSymbol, int isotop) {
		int[] isotopes = AtomicElements.getIsotopicMap().get(elementSymbol);
		if(isotopes != null) {
			for(int i = 0; i <isotopes.length; i++) {
				if(isotop == isotopes[i])
					return true;
			}
		}		
		return false;
	}
}
