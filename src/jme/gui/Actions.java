package jme.gui;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import jme.JME;
import jme.core.Atom;

/**
 * A class that gathers all the GUI-related keyboard and mouse actions. 
 * 
 * @author hansonr
 *
 */
public class Actions {
	
	/**
	 * A flag for '-' to extend the left-menu options to add a bond
	 */
	public static final int ACTION_UNCHANGED = -1;
	
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

	public static final int ACTION_TEMPLATE = 2030;

	public static final int ACTION_GROUP_MIN = 2033; // BB first entry in the substituents (FG)
	public static final int ACTION_GROUP_TBU = 2033;
	public static final int ACTION_GROUP_NITRO = 2034;
	public static final int ACTION_GROUP_COO = 2035;
	public static final int ACTION_GROUP_CF3 = 2036;
	public static final int ACTION_GROUP_CCL3 = 2037;
	public static final int ACTION_GROUP_CC = 2038;
	public static final int ACTION_GROUP_SULFO = 2039;
	public static final int ACTION_GROUP_COOME = 2040;
	public static final int ACTION_GROUP_OCOME = 2041;
	public static final int ACTION_GROUP_CYANO = 2042;
	public static final int ACTION_GROUP_NME2 = 2043;
	public static final int ACTION_GROUP_NHSO2ME = 2044;
	public static final int ACTION_GROUP_CCC = 2045;
	public static final int ACTION_GROUP_C2 = 2046;
	public static final int ACTION_GROUP_C3 = 2047;
	public static final int ACTION_GROUP_C4 = 2048;
	public static final int ACTION_GROUP_COH = 2049;
	public static final int ACTION_GROUP_dO = 2050; // =O
	public static final int ACTION_GROUP_PO3H2 = 2051;
	public static final int ACTION_GROUP_SO2NH2 = 2052;
	public static final int ACTION_GROUP_TEMPLATE = 2053;
	public static final int ACTION_GROUP_CF = 2054;
	public static final int ACTION_GROUP_CL = 2055;
	public static final int ACTION_GROUP_CB = 2056;
	public static final int ACTION_GROUP_CI = 2057;
	public static final int ACTION_GROUP_CN = 2058;
	public static final int ACTION_GROUP_CO = 2059;
	public static final int ACTION_GROUP_CON = 2060; // BB
	public static final int ACTION_GROUP_NCO = 2061; // BB
	public static final int ACTION_GROUP_MAX = 2062; // last+1 len na < test


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

	private JME jme;

	public Actions(JME jme) {
		this.jme = jme;
		setActions();
	}


	public static int mapActionToAtomNumberX(int action) {
		if (action >= Actions.ACTION_AN_R && action <= ACTION_AN_R_LAST) {
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


	
	private Map<Object, AbstractAction> actions = new HashMap<>();
	private AbstractAction actionAtomBond;


	/**
	 * Create a KeyStroke from a character or character code
	 * 
	 * @param key
	 * @param modifiers
	 * @return
	 */
	public KeyStroke getKeyStroke(int key, int modifiers) {
		if (modifiers == InputEvent.META_MASK) {
			modifiers = InputEvent.CTRL_MASK;
		}	
		if (key < 127)
			return KeyStroke.getKeyStroke(Character.valueOf((char) key), modifiers);
		return KeyStroke.getKeyStroke(key, modifiers);
	}

	
	private class WrappedAction extends AbstractAction {

			/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
			int id;
			AbstractAction a;
			private String name;
			
		WrappedAction(String name, int id, AbstractAction a) {
			super(name);
			this.id = id;
			this.name = name;
			this.a = a;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int id = e.getID();
			if (this.id != 0) {
				id = this.id;
				e = new ActionEvent(e.getSource(), id, name);
			}
			a.actionPerformed(e);
		}
			
	}
	
	protected void addAction(String name, int id, int key, int modifiers, AbstractAction a) {
		if (id == 0)
			id = key;
		if (id != 0) {
			a = new WrappedAction(name, id, a);
		}
		actions.put(name, a);
		if (key != 0) {
			KeyStroke shortcut = getKeyStroke(key, modifiers);
			a.putValue("shortcut", shortcut);
			actions.put(shortcut, a);		
		}
	}

	
	public boolean doAction(Object key, int id) {
			
		AbstractAction a = actions.get(key);
		if (a == null)
			return false;
		String actionCommand = key.toString();
		if (a instanceof WrappedAction) {
			if (id == 0)
				id = ((WrappedAction)a).id;
			a = ((WrappedAction)a).a;
		}
		jme.startKeyboardAction();
		a.actionPerformed(new ActionEvent(a, id, actionCommand));
		jme.endKeyboardAction();
		return true;
	}

	@SuppressWarnings("serial")
	public void setActions() {
		addAction("copy", 0, KeyEvent.VK_C, InputEvent.CTRL_MASK, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jme.copyFileToClipboard();
			}
		});
		addAction("cut", 0, KeyEvent.VK_X, InputEvent.CTRL_MASK, new AbstractAction() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				jme.cutSelectedMoleculeForSystemClipBoard();
			}
		});
		addAction("paste", 0, KeyEvent.VK_V, InputEvent.CTRL_MASK, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jme.options.paste)
					jme.pasteMolFileFromClipboard();
			}			
		});
		
		AbstractAction a = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jme.doUndoRedo(e.getID());
			}			
		};

		addAction("redo", 1, 'Y', InputEvent.CTRL_MASK, a);
		addAction("undo", -1, 'U', 0, a);
		addAction("undo", -1, 'Z', InputEvent.CTRL_MASK, a);
		
		a = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jme.doNavigate(e.getID());
			}
		};

		// key will be used as an id here
		addAction("navigate", 0, KeyEvent.VK_UP, 0, a); 
		addAction("navigate", 0, KeyEvent.VK_DOWN, 0, a); 
		addAction("navigate", 0, KeyEvent.VK_LEFT, 0, a); 
		addAction("navigate", 0, KeyEvent.VK_RIGHT, 0, a); 
		a = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				jme.doAtomG();
			}
		};
		addAction("atomG", 0, KeyEvent.VK_MULTIPLY, 0, a);
		addAction("atomG", 0, KeyEvent.VK_ASTERISK, 0, a);
		addAction("atomG", 0, KeyEvent.VK_G, 0, a);
		a = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				jme.doAtomX();
			}
		};
		addAction("atomX", 0, KeyEvent.VK_X, 0, a);		
		a = actionAtomBond = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jme.doAtomBond(e.getID());
			}			
		};
		addAction("atombond", Actions.ACTION_DELETE, KeyEvent.VK_D, 0, a);		
		addAction("atombond", Actions.ACTION_DELETE, KeyEvent.VK_BACK_SPACE, 0, a);		
		addAction("atombond", Actions.ACTION_DELETE, KeyEvent.VK_DELETE, 0, a);		

		addAction("atombond", Actions.ACTION_BOND_SINGLE, KeyEvent.VK_ESCAPE, 0, a);

		// BH IDEA -- SHIFT for Ph
		addAction("atombond", Actions.ACTION_RING_PH, 'P', KeyEvent.SHIFT_MASK, a);

		addAction("atombond", Actions.ACTION_GROUP_NITRO, 'Y', 0, a);
		addAction("atombond", Actions.ACTION_GROUP_SULFO, 'Z', 0, a);
		addAction("atombond", Actions.ACTION_GROUP_COO, 'A', 0, a);
		addAction("atombond", Actions.ACTION_GROUP_CC, 'E', 0, a);
		addAction("atombond", Actions.ACTION_GROUP_CN, 'Q', 0, a);
		addAction("atombond", Actions.ACTION_RING_3FURYL, '0', InputEvent.ALT_MASK, a);
		addAction("atombond", Actions.ACTION_RING_3FURYL, '0', InputEvent.SHIFT_MASK, a);
		addAction("atombond", Actions.ACTION_CHAIN, ' ', 0, a);
		
		addAction("atombond", Actions.ACTION_BOND_TRIPLE, '#', 0, a);

		a = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jme.doPage(e.getID());
			}
		};

		addAction("page", Actions.ACTION_PGUP, 'M', 0, a);
		addAction("page", Actions.ACTION_PGUP, KeyEvent.VK_PAGE_UP, 0, a);
		addAction("page", Actions.ACTION_PGDN, 'W', 0, a);
		addAction("page", Actions.ACTION_PGDN, KeyEvent.VK_PAGE_DOWN, 0, a);
		addAction("page", Actions.ACTION_HOME, KeyEvent.VK_HOME, 0, a);
		addAction("page", Actions.ACTION_END, KeyEvent.VK_END, 0, a);

	}

	private int actions0_9 = 0;
	private int actionsBond = 0;

	public void setBondVariableAction(boolean isBond) {
		int newActions = (isBond ? Actions.ACTION_AN_C : Actions.ACTION_BOND_SINGLE);
		AbstractAction a = actionAtomBond;
		if (actionsBond != newActions) {
			actionsBond = newActions;
			if (newActions == Actions.ACTION_BOND_SINGLE) {
				// BH++ previously not defined for bonds
				addAction("atombond", Actions.ACTION_BOND_SINGLE, '-', 0, a);
				addAction("atombond", Actions.ACTION_STEREO, '+', 0, a);
				addAction("atombond", Actions.ACTION_STEREO, KeyEvent.VK_ADD, 0, a);
			} else {
				addAction("atombond", Actions.ACTION_CHARGE, '+', 0, a);
				addAction("atombond", Actions.ACTION_CHARGE, KeyEvent.VK_ADD, 0, a);
				addAction("atombond", Actions.ACTION_UNCHANGED, '-', 0, a);
			}
		}
				
	}
	
	public void setAtomVariableAction(boolean isR, int action) {

		setBondVariableAction(false);
		AbstractAction a = actionAtomBond;

		// 'T' depends upon choice of atom on the left
		switch (action) {
		case Actions.ACTION_AN_F:
			addAction("atombond", Actions.ACTION_GROUP_CF3, 'T', 0, a);
			break;
		case Actions.ACTION_AN_CL:
			addAction("atombond", Actions.ACTION_GROUP_CCL3, 'T', 0, a);
			break;
		default:
			addAction("atombond", Actions.ACTION_GROUP_TBU, 'T', 0, a);
			break;
		}

		int newActions = (isR ? Actions.ACTION_AN_R : Actions.ACTION_GROUP_TEMPLATE);
		if (action == Actions.ACTION_MARK && actions0_9 != Actions.ACTION_MARK) {
			actions0_9 = Actions.ACTION_MARK;
			for (int i = 0; i <= 9;)
				addAction("atombond", Actions.ACTION_MARK + 0, '0', 0, a);
			return;
		}

		if (actions0_9 == newActions) {
			return;
		}
		actions0_9 = newActions;
		if (isR) {
			for (int i = 0; i <= 9;)
				addAction("atombond", Actions.ACTION_AN_R + i, (char) (48 + i), 0, a);
		} else {
			addAction("atombond", Actions.ACTION_RING_FURANE, '0', 0, a);
			addAction("atombond", Actions.ACTION_BOND_SINGLE, '1', 0, a);
			// BH++ suggests SHIFT-P for Phenyl, not 1 
			//addAction("atombond", Actions.ACTION_RING_PH, '1', 0, a);
			
			int id = (action == Actions.ACTION_AN_O ? Actions.ACTION_GROUP_dO : Actions.ACTION_BOND_DOUBLE);
			addAction("atombond", id, '2', 0, a);
			addAction("atombond", id, '=', 0, a);
			// BH suggestions SHIFT-3 for 3-membered ring to allow 3 to be for triple bond
			addAction("atombond", Actions.ACTION_RING_3, '3', InputEvent.SHIFT_MASK, a);
			addAction("atombond", Actions.ACTION_BOND_TRIPLE, '3', 0, a);
			
			addAction("atombond", Actions.ACTION_RING_4, '4', 0, a);
			addAction("atombond", Actions.ACTION_RING_5, '5', 0, a);
			addAction("atombond", Actions.ACTION_RING_6, '6', 0, a);
			addAction("atombond", Actions.ACTION_RING_7, '7', 0, a);
			addAction("atombond", Actions.ACTION_RING_8, '8', 0, a);
			addAction("atombond", Actions.ACTION_RING_9, '9', 0, a);
		}
	}


	public void dispose() {
		jme = null;
		actions = null;
	}

	
}