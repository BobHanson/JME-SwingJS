package jme.gui;

import jme.core.Atom;

public class Actions {
	
	public static final int ACTION_SMI      = 101;
	public static final int ACTION_CLEAR    = 102;
	public static final int ACTION_NEW      = 103; // using newMolecule
	public static final int ACTION_DELETE   = 104;
	public static final int ACTION_MARK     = 105;
	public static final int ACTION_DELGROUP = 106;
	public static final int ACTION_QRY      = 107;
	public static final int ACTION_CHARGE   = 108;
	public static final int ACTION_REACP    = 109;
	public static final int ACTION_UNDO     = 110;
	public static final int ACTION_REDO     = 111;
	public static final int ACTION_SPIRO    = 112;
	public static final int ACTION_MOVE_AT  = 113;
	public static final int ACTION_JME      = 114;

	public static final int ACTION_PGUP = 151;
	public static final int ACTION_PGDN = 152;
	public static final int ACTION_HOME = 153;
	public static final int ACTION_END  = 154;

//	public static final int ACTION_ROT90 = 156; // webme
//	public static final int ACTION_CHARGE_PLUS = 157; // webme
//	public static final int ACTION_CHARGE_MINUS = 158; // webme
//
	
	public static final int ACTION_STEREO = 201;
	public static final int ACTION_BOND_SINGLE = 202;
	public static final int ACTION_BOND_DOUBLE = 203;
	public static final int ACTION_BOND_TRIPLE = 204;
	public static final int ACTION_CHAIN = 205;

	public static final int ACTION_RING_3 = 206;
	public static final int ACTION_RING_4 = 207;
	public static final int ACTION_RING_5 = 208;
	public static final int ACTION_RING_PH = 209;
	public static final int ACTION_RING_6 = 210;
	public static final int ACTION_RING_7 = 211;
	public static final int ACTION_RING_8 = 212;
	public static final int ACTION_FG     = 213; // BB: button for a popup menu with functional groups
	public static final int ACTION_IO     = 214; // BB replace ACTION_EMPTY_CELL by I/O icon below info icon

	public static final int ACTION_RING_FURANE = 221; // nema button
	public static final int ACTION_RING_3FURYL = 223; // Alt 0
	public static final int ACTION_RING_9 = 229; // nema button
	public static final int ACTION_TEMPLATE = 230;

	public static final int ACTION_GROUP_MIN = 233; // BB first entry in the substituents (FG)
	public static final int ACTION_GROUP_TBU = 233;
	public static final int ACTION_GROUP_NITRO = 234;
	public static final int ACTION_GROUP_COO = 235;
	public static final int ACTION_GROUP_CF3 = 236;
	public static final int ACTION_GROUP_CCL3 = 237;
	public static final int ACTION_GROUP_CC = 238;
	public static final int ACTION_GROUP_SULFO = 239;
	public static final int ACTION_GROUP_COOME = 240;
	public static final int ACTION_GROUP_OCOME = 241;
	public static final int ACTION_GROUP_CYANO = 242;
	public static final int ACTION_GROUP_NME2 = 243;
	public static final int ACTION_GROUP_NHSO2ME = 244;
	public static final int ACTION_GROUP_CCC = 245;
	public static final int ACTION_GROUP_C2 = 246;
	public static final int ACTION_GROUP_C3 = 247;
	public static final int ACTION_GROUP_C4 = 248;
	public static final int ACTION_GROUP_COH = 249;
	public static final int ACTION_GROUP_dO = 250; // =O
	public static final int ACTION_GROUP_PO3H2 = 251;
	public static final int ACTION_GROUP_SO2NH2 = 252;
	public static final int ACTION_GROUP_TEMPLATE = 253;
	public static final int ACTION_GROUP_CF = 254;
	public static final int ACTION_GROUP_CL = 255;
	public static final int ACTION_GROUP_CB = 256;
	public static final int ACTION_GROUP_CI = 257;
	public static final int ACTION_GROUP_CN = 258;
	public static final int ACTION_GROUP_CO = 259;
	public static final int ACTION_GROUP_CON = 260; // BB
	public static final int ACTION_GROUP_NCO = 261; // BB
	public static final int ACTION_GROUP_MAX = 262; // last+1 len na < test


	
	public static final int ACTION_AN_C = 301;
	public static final int ACTION_AN_N = 401;
	public static final int ACTION_AN_O = 501;
	public static final int ACTION_AN_S = 601;
	public static final int ACTION_AN_F = 701;
	public static final int ACTION_AN_CL = 801;
	public static final int ACTION_AN_BR = 901;
	public static final int ACTION_AN_I = 1001;
	public static final int ACTION_AN_P = 1101;
	public static final int ACTION_AN_X = 1201;

	public static final int ACTION_AN_H = 1300; // does not match a square posiiton on left menu
	// added by BB
	// public static final int ACTION_AN_STAR = 1301;

	public static final int ACTION_AN_R = 1301; // must be 1301 because it corresponds to the left menu square position ???
	public static final int ACTION_AN_R1 = 1302;
	public static final int ACTION_AN_R2 = 1303;
	public static final int ACTION_AN_R3 = 1304;
	// added by BB
	public static final int ACTION_AN_R4 = 1305;
	public static final int ACTION_AN_R5 = 1306;
	public static final int ACTION_AN_R6 = 1307;
	public static final int ACTION_AN_R7 = 1308;
	public static final int ACTION_AN_R8 = 1309;
	public static final int ACTION_AN_R9 = 1310;

	public static final int ACTION_AN_R_LAST = 1310;
	// end added by BB

	public static final int actionToAtomNumberArray[] = { 
			ACTION_AN_C, Atom.AN_C, 
			ACTION_AN_N, Atom.AN_N, 
			ACTION_AN_O, Atom.AN_O, 
			ACTION_AN_F, Atom.AN_F, 
			ACTION_AN_CL, Atom.AN_CL, 
			ACTION_AN_BR, Atom.AN_BR, 
			ACTION_AN_I, Atom.AN_I, 
			ACTION_AN_S, Atom.AN_S, 
			ACTION_AN_P, Atom.AN_P,
			// next three are not on menu except as X or R
			ACTION_AN_H, Atom.AN_H, 
			ACTION_AN_X, Atom.AN_X, 
			ACTION_AN_R, Atom.AN_R, 
	};

	public static final int LEFT_MENU_ELEMENT_COUNT = 9;




	public static int mapActionToAtomNumberX(int action) {
		if (action >= Actions.ACTION_AN_R) {
			int delta = action - Actions.ACTION_AN_R;
			return Atom.AN_R + delta;
		}
		for (int i = 0; i < Actions.actionToAtomNumberArray.length; i += 2) {
			if (Actions.actionToAtomNumberArray[i] == action) {
				return Actions.actionToAtomNumberArray[i + 1];
			}
		}
		return -1;
	}

	
}