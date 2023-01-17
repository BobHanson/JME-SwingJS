package jme;

import java.awt.Color;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

// ****************************************************************************
@SuppressWarnings("serial")
class QueryBox extends FrameWithLocation {
	// static Point point = new Point(20, 200);
	Point myLocation;
	JTextField text;
	static JButton c, n, o, s, p, f, cl, br, i, any, anyec, halogen, aromatic, nonaromatic, ring, nonring;
	static JButton anyBond, aromaticBond, ringBond, nonringBond, sdBond;
	
	static JComboBox choiced, choiceh;
	Color bgc;
	boolean isBondQuery = false;
	JME jme; // reference to parent
	private JButton resetButton;
	// --- buttony etc su definovane ako static aby sa zachovala ich hodnota po
	// novom stlacenie QRY (aby sa window dostalo hore)
	// ----------------------------------------------------------------------------

	QueryBox(JME jme) {
		super("Atom/Bond Query");
		this.jme = jme;
		bgc = jme.bgColor;
		// BB
		if (myLocation == null) {
			// setup the first position of dialog box close to the applet
			Point jmeLocation = jme.getLocationOnScreen();
			myLocation = new Point(jmeLocation);
			this.safeTranslate(myLocation, -150, 10);

		}
		this.lastLocation = myLocation;

		setLayout(new GridLayout(0, 1));

		// BB It is not necesssary to specify a font, the default one looks good
		if (jme.dialogFont != null) {
			setFont(jme.dialogFont);
		}
		setBackground(bgc);

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 1));
		p1.add(new JLabel("Atom type :"));

		// boolean first = (any == null); // caused problems
		boolean first = true;

		if (first) {
			any = newJButton("Any");
			anyec = newJButton("Any except C");
			halogen = newJButton("Halogen");
		}
		p1.add(any);
		p1.add(anyec);
		p1.add(halogen);
		add(p1);

		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 1));
		p2.add(new JLabel("Or select one or more from the list :", JLabel.LEFT));
		add(p2);

		JPanel p3 = new JPanel();
		// p3.setLayout(new GridLayout(1,0));
		p3.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 1));
		if (first) {
			c = newJButton("C");
			n = newJButton("N");
			o = newJButton("O");
			s = newJButton("S");
			p = newJButton("P");
			f = newJButton("F");
			cl = newJButton("Cl");
			br = newJButton("Br");
			i = newJButton("I");
		}
		p3.add(c);
		p3.add(n);
		p3.add(o);
		p3.add(s);
		p3.add(p);
		p3.add(f);
		p3.add(cl);
		p3.add(br);
		p3.add(i);
		add(p3);

		JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 1));
		if (first) {
			choiceh = new JComboBox();
			choiceh.addItem("Any");
			choiceh.addItem("0");
			choiceh.addItem("1");
			choiceh.addItem("2");
			choiceh.addItem("3");
			choiceh.addActionListener(customAction);
		}
		p4.add(new JLabel("Number of hydrogens :  "));
		p4.add(choiceh);
		add(p4);

		JPanel p5 = new JPanel();
		p5.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 1));
		if (first) {
			choiced = new JComboBox();
			choiced.addItem("Any");
			choiced.addItem("0");
			choiced.addItem("1");
			choiced.addItem("2");
			choiced.addItem("3");
			choiced.addItem("4");
			choiced.addItem("5");
			choiced.addItem("6");
			choiced.addActionListener(customAction);
		}

		p5.add(new JLabel("Number of connections :", JLabel.LEFT));
		p5.add(choiced);
		p5.add(new JLabel(" (H's don't count.)", JLabel.LEFT));
		add(p5);

		JPanel p6 = new JPanel();
		p6.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 1));
		p6.add(new JLabel("Atom is :"));
		if (first)
			aromatic = newJButton("Aromatic");
		p6.add(aromatic);
		if (first)
			nonaromatic = newJButton("Nonaromatic");
		p6.add(nonaromatic);
		if (first)
			ring = newJButton("Ring");
		p6.add(ring);
		if (first)
			nonring = newJButton("Nonring");
		p6.add(nonring);
		add(p6);

		JPanel p9 = new JPanel();
		p9.setBackground(getBackground().darker());
		p9.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 1));
		p9.add(new JLabel("Bond is :"));
		if (first)
			anyBond = newJButton("Any");
		p9.add(anyBond);
		if (first)
			aromaticBond = newJButton("Aromatic");
		p9.add(aromaticBond);
		// if (first) sdBond = newJButton("- or ="); p9.add(sdBond);
		if (first)
			ringBond = newJButton("Ring");
		p9.add(ringBond);
		if (first)
			nonringBond = newJButton("Nonring");
		p9.add(nonringBond);
		add(p9);

		JPanel p8 = new JPanel();
		p8.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 1));
		// p8.add(new JLabel("Query :"));
		if (first)
			text = new JTextField("*", 20);
		p8.add(text);
		resetButton = newJButton("Reset");
		p8.add(resetButton);
		resetButton.addActionListener(customAction);
		p8.add(this.closeJButton);
		add(p8);
		setResizable(false);

		if (first) { // musi sa to explicitne, inak nemaju vsetky bgc
			resetAtomList();
			resetAtomType();
			resetBondType();
			aromatic.setBackground(bgc);
			nonaromatic.setBackground(bgc);
			ring.setBackground(bgc);
			nonring.setBackground(bgc);
			choiceh.setBackground(bgc);
			choiced.setBackground(bgc);
			changeColor(any);
		}

		pack();
		setVisible(true);
	}

	private JButton newJButton(String text) {
		JButton b = new JButton(text);
		b.addActionListener(customAction);
		return b;
	}

	// ----------------------------------------------------------------------------
	private ActionListener customAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			 Object b = e.getSource();
			if (b == resetButton) {
				resetAll();
				changeColor(any); // Any on
				doSmarts();
			} else if (b instanceof JButton) {
				resetBondType(); // set to any ???
				if (b == any) {
					resetAtomList();
					resetAtomType();
				} else if (b == anyec) {
					resetAtomList();
					resetAtomType();
				} else if (b == halogen) {
					resetAtomList();
					resetAtomType();
				} else if (b == ring) {
					nonring.setBackground(bgc);
				} else if (b == nonring) {
					ring.setBackground(bgc);
					aromatic.setBackground(bgc);
				} else if (b == aromatic) {
					nonaromatic.setBackground(bgc);
					nonring.setBackground(bgc);
				} else if (b == nonaromatic) {
					aromatic.setBackground(bgc);
				} else if (b == anyBond || b == aromaticBond || b == ringBond
						|| b == nonringBond) {
					resetAll();
					isBondQuery = true;
				} else { // atom z listu pressed (moze by aj posledny vynulovany
					resetAtomType();
				}
				changeColor((JButton) (b));
				doSmarts();
			} else if (b instanceof JComboBox) {
				resetBondType();
				JComboBox choice = (JComboBox) (b);
				if (choice.getSelectedIndex() == 0)
					choice.setBackground(bgc);
				else
					choice.setBackground(Color.orange);
				doSmarts();
			}

			// v JME menu nastavi na query (ak bolo medzitym ine)
			if (jme.action != JME.ACTION_QRY) {
				jme.action = JME.ACTION_QRY;
				jme.repaint();
			}

		}
		
	};

	// ----------------------------------------------------------------------------
	private void resetAll() {
		resetAtomList();
		resetAtomType();
		choiceh.setSelectedIndex(0);
		choiced.setSelectedIndex(0);
		aromatic.setBackground(bgc);
		nonaromatic.setBackground(bgc);
		ring.setBackground(bgc);
		nonring.setBackground(bgc);
		choiceh.setBackground(bgc);
		choiced.setBackground(bgc);
		resetBondType(); // also sets isBondQuery to false

	}

	// ----------------------------------------------------------------------------
	private void resetAtomList() {
		c.setBackground(bgc);
		n.setBackground(bgc);
		o.setBackground(bgc);
		s.setBackground(bgc);
		p.setBackground(bgc);
		f.setBackground(bgc);
		cl.setBackground(bgc);
		br.setBackground(bgc);
		i.setBackground(bgc);
	}

	// ----------------------------------------------------------------------------
	private void resetAtomType() {
		any.setBackground(bgc);
		anyec.setBackground(bgc);
		halogen.setBackground(bgc);
	}

	// ----------------------------------------------------------------------------
	private void resetBondType() {
		anyBond.setBackground(bgc);
		aromaticBond.setBackground(bgc);
		// sdBond.setBackground(bgc);
		ringBond.setBackground(bgc);
		nonringBond.setBackground(bgc);
		isBondQuery = false;
	}

	// ----------------------------------------------------------------------------
	private void changeColor(JButton b) {
		if (b.getBackground() == bgc)
			b.setBackground(Color.orange);
		else
			b.setBackground(bgc);
	}

	// ----------------------------------------------------------------------------
	private void doSmarts() {
		String smarts = "";
		boolean showaA = false;

		// basic atom type
		if (any.getBackground() != bgc) {
			smarts = "*";
			showaA = true;
		} else if (anyec.getBackground() != bgc) {
			smarts = "!#6";
			showaA = true;
		} else if (halogen.getBackground() != bgc) {
			f.setBackground(Color.orange);
			cl.setBackground(Color.orange);
			br.setBackground(Color.orange);
			i.setBackground(Color.orange);
			smarts = "F,Cl,Br,I";
		} else {
			boolean ar = aromatic.getBackground() != bgc;
			boolean nar = nonaromatic.getBackground() != bgc;
			if (c.getBackground() != bgc) {
				if (ar)
					smarts += "c,";
				else if (nar)
					smarts += "C,";
				else
					smarts += "#6,";
			}
			if (n.getBackground() != bgc) {
				if (ar)
					smarts += "n,";
				else if (nar)
					smarts += "N,";
				else
					smarts += "#7,";
			}
			if (o.getBackground() != bgc) {
				if (ar)
					smarts += "o,";
				else if (nar)
					smarts += "O,";
				else
					smarts += "#8,";
			}
			if (s.getBackground() != bgc) {
				if (ar)
					smarts += "s,";
				else if (nar)
					smarts += "S,";
				else
					smarts += "#16,";
			}
			if (p.getBackground() != bgc) {
				if (ar)
					smarts += "p,";
				else if (nar)
					smarts += "P,";
				else
					smarts += "#15,";
			}
			if (f.getBackground() != bgc)
				smarts += "F,";
			if (cl.getBackground() != bgc)
				smarts += "Cl,";
			if (br.getBackground() != bgc)
				smarts += "Br,";
			if (i.getBackground() != bgc)
				smarts += "I,";
			// if (h.getBackground() != bgc) smarts += "H,";
			if (smarts.endsWith(","))
				smarts = smarts.substring(0, smarts.length() - 1);
			if (smarts.length() < 1 && !isBondQuery) { // napr pri vynulovani
				// listu
				if (ar)
					smarts = "a";
				else if (nar)
					smarts = "A";
				else {
					any.setBackground(Color.orange);
					smarts = "*";
				}
			}
		}

		// atomic properties
		String ap = "";
		if (showaA && aromatic.getBackground() != bgc)
			ap += ";a";
		if (showaA && nonaromatic.getBackground() != bgc)
			ap += ";A";
		if (ring.getBackground() != bgc)
			ap += ";R";
		if (nonring.getBackground() != bgc)
			ap += ";!R";
		// zjednodusenie (mieso *;r len r ...)
		if (any.getBackground() != bgc && ap.length() > 0)
			smarts = ap.substring(1, ap.length());
		else
			smarts += ap;

		// hydrogens and number of bonds
		int nh = choiceh.getSelectedIndex();
		if (nh > 0) {
			nh--;
			smarts += ";H" + nh;
		}
		int nd = choiced.getSelectedIndex();
		if (nd > 0) {
			nd--;
			smarts += ";D" + nd;
		}

		// bond type
		if (anyBond.getBackground() != bgc)
			smarts = "~";
		if (aromaticBond.getBackground() != bgc)
			smarts = ":";
		// if (sdBond.getBackground() != bgc) smarts = "-,=";
		if (ringBond.getBackground() != bgc)
			smarts = "@";
		if (nonringBond.getBackground() != bgc)
			smarts = "!@";

		text.setText(smarts);
	}

	// --------------------------------------------------------------------------
	boolean isBondQuery() {
		return isBondQuery;
	}

	// --------------------------------------------------------------------------
	String getSmarts() {
		return text.getText();
	}
	// --------------------------------------------------------------------------

}
// ****************************************************************************