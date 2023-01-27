package jme;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import jme.core.Atom;

//****************************************************************************
@SuppressWarnings("serial")
class MultiBox extends FrameWithLocation {

	final static int BOX_ABOUT = 0;
	final static int BOX_SMILES = 1;
	final static int BOX_ATOMX = 2;

	// static Point aboutBoxPoint = new Point(500, 10);
	// static Point smilesBoxPoint = new Point(200, 50);
	// static Point atomxBoxPoint = new Point(150, 420);
	Point aboutBoxPoint;
	Point smilesBoxPoint;
	Point atomxBoxPoint;
	static JTextField atomicSymbol = new JTextField("H"); // pouziva sa v JME
	JButton helpJButton = new JButton("Help");
	JButton homeJButton = new JButton("JSME home");

	JTextField smilesText;
	JME jme; // parent of MultiBox
	// ----------------------------------------------------------------------------

	/**
	 * 
	 * @param box : 1 is for smiles , 2 for X and 0 for about
	 * @param jme
	 */
	MultiBox(int box, JME jme) {
		super();
		this.jme = jme;

		// BB It is not necesssary to specify a font, the default one looks good - not
		// true - Helevetica looks beter because it is correclty centered
		if (jme.dialogFont != null) {
			setFont(jme.dialogFont);
		}
		setBackground(jme.bgColor);
		setResizable(false);

		// setup the first position of dialog box close to the applet
		Point jmeLocation = jme.getLocationOnScreen();
		// System.out.println(jmeLocation);

		switch (box) {
		case BOX_SMILES:
			// BB
			if (smilesBoxPoint == null) {
				smilesBoxPoint = new Point(jmeLocation);
				safeTranslate(smilesBoxPoint, -30, 0); // above the applet,close to the smiley
			}
			this.lastLocation = smilesBoxPoint;
			initSmilesBox(jme.getSmiles());
			break;
		case BOX_ATOMX:
			// BB
			if (atomxBoxPoint == null) {
				// BB
				atomxBoxPoint = new Point(jmeLocation);
				safeTranslate(atomxBoxPoint, -50, (int) jme.menuCellSize * 13 - 80); // left side of the atom X
																							// menu cell
			}
			this.lastLocation = atomxBoxPoint;
			initAtomxBox();
			break;
		case BOX_ABOUT:
			// BB
			if (aboutBoxPoint == null) {
				aboutBoxPoint = new Point(jmeLocation);
				safeTranslate(aboutBoxPoint, (int) jme.menuCellSize * 5, 0); // right side next to the info menu
																					// cell
			}
			initAboutBox();
			this.lastLocation = aboutBoxPoint;
			break;
		}

		pack();
		setVisible(true);
	}

	// ----------------------------------------------------------------------------
	void initAboutBox() {
		setTitle("About " + JME.programName);
		setBackground(this.jme.bgColor);
		Container pane = getContentPane();
		javax.swing.Box b = new javax.swing.Box(BoxLayout.Y_AXIS);
		b.setBorder(new EmptyBorder(10, 10, 10, 10));
		b.add(new JLabel(JME.programName + " Molecular Editor" + " v" + JME.version));
		b.add(new JLabel("Peter Ertl, Bruno Bienfait"));
		b.add(new JLabel("and Robert Hanson"));
		b.add(javax.swing.Box.createRigidArea(new Dimension(10,10)));
		for (String cl : JME.copyright) {
			JLabel l = new JLabel(cl);
			l.setFont(JME.copyRigthSmallTextFont);
			b.add(l);
		}
		for (int i = 0; i < b.getComponentCount(); i++)
			((JComponent)b.getComponent(i)).setAlignmentX(Component.CENTER_ALIGNMENT);
		
		pane.add("Center", b);
		
		JPanel p = new JPanel();
		p.add(helpJButton);
		p.add(homeJButton);
		helpJButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("help");
				try {
					URL u = new URL(JME.helpUrl);
					showURL(u);
				} catch (Exception urlException) {
					System.out.println(urlException.getMessage());
					MultiBox.this.jme.showError(urlException.getMessage());
				}
			}
		});
		homeJButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("help");
				try {
					URL u = new URL(JME.websiteUrl);
					showURL(u);
				} catch (Exception urlException) {
					System.out.println(urlException.getMessage());
					MultiBox.this.jme.showError(urlException.getMessage());
				}
			}
		});
		p.add(this.closeJButton);
		pane.add("South", p);
		// setLocation(aboutBoxPoint);
	}

	// ----------------------------------------------------------------------------
	void initSmilesBox(String smiles) {
		setTitle("SMILES");
		setLayout(new BorderLayout(2, 0)); // 2, 0 gaps
		smilesText = new JTextField(smiles + "     ");
		if (!jme.options.runsmi) {
			// the JTextField cannot be copied in JS when the setEditable is set to false
			// smilesText.setEditable(false);
		}
		add("Center", smilesText);
		JPanel p = new JPanel();
		p.add(this.closeJButton);
		if (jme.options.runsmi) {
			JButton b = new JButton("Submit");
			p.add(b);
		}
		add("South", p);

		// this.setSmiles(smilesText.getText().trim()); //BB : to size the SMILES box
		// correctly - does not work
		// this.setMaximumSize(new Dimension(330, 200)); //BB does not work
		smilesText.setText(smilesText.getText().trim()); // odstrani " "
		setResizable(true);
	}

	// ----------------------------------------------------------------------------
	// sets smiles in smiles box a aj upravi dlzku
	// BB: resize does not work
	void setSmiles(String smiles) {
		Dimension d = getSize();
		int l = jme.menuCellFontSmallerMet.stringWidth(smiles) + 50;
		if (l < 150)
			l = 150;
		// BB : avoid huge dialog box that does not fit the screen
		if (l > 400)
			l = 400;

		validate();
		setSize(l, d.height);
		smilesText.setText(smiles);
	}

	// ----------------------------------------------------------------------------
	void initAtomxBox() {
		setTitle("Nonstandard atom");
		setLayout(new BorderLayout(2, 0)); // 2, 0 gaps
		JPanel p = new JPanel();
		p.add(new JLabel("atomic SMILES", JLabel.CENTER));
		add("North", p);
		// 2007.01 fixed bug - frozen xbutton
		String as = "H";
		if (atomicSymbol != null)
			as = atomicSymbol.getText();
		atomicSymbol = new JTextField(as, 8);
		add("Center", atomicSymbol);
		p = new JPanel();
		p.add(this.closeJButton);
		add("South", p);
	}

	// ----------------------------------------------------------------------------
	public boolean keyDown(KeyEvent e, int key) {
		// v JME menu nastavi na X (ak bolo medzitym ine) ak tukane zo atomxBox
		if (atomicSymbol == null)
			return false; // nie null iba v atomxBox
		// vracia false, lebo potom by sa nedalo pisat napr v smilesBox
		if (jme.action != JME.ACTION_AN_X) {
			jme.action = JME.ACTION_AN_X;
			jme.active_an = Atom.AN_X; // treba
		}
		// JME.repaint(); //can't make static reference ... kvoli ocierneniu X
		return false; // inak sa nedaju pisat pismena do text boxu
	}

//	// ----------------------------------------------------------------------------
//	@Override
//	public boolean customAction(Event e, Object arg) {
//		String html = (e.target == this.helpJButton ? MultiBox.this.jme.helpUrl
//				: e.target == this.homeJButton ? MultiBox.this.jme.websiteUrl : null);
//		if (html == null)
//			return false;
//		try {
//			URL u = new URL(html);
//			showURL(u);
//		} catch (Exception urlException) {
//			// System.out.println(urlException.getMessage());
//			MultiBox.this.jme.showError(urlException.getMessage());
//		}
//		return true;
//	
//	}

	private static void showURL(URL url) throws IOException, URISyntaxException {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			desktop.browse(url.toURI());
		}
	}

}