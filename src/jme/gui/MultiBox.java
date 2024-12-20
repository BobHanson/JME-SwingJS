package jme.gui;

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

import jme.JME;

//****************************************************************************
@SuppressWarnings("serial")
public class MultiBox extends FrameWithLocation {

	public final static int BOX_ABOUT = 0;
	public final static int BOX_SMILES = 1;
	public final static int BOX_ATOMX = 2;

	// static Point aboutBoxPoint = new Point(500, 10);
	// static Point smilesBoxPoint = new Point(200, 50);
	// static Point atomxBoxPoint = new Point(150, 420);
	Point aboutBoxPoint;
	Point smilesBoxPoint;
	Point atomxBoxPoint;
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
	public MultiBox(int box, JME jme) {
		super();
		this.jme = jme;

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
				safeTranslate(atomxBoxPoint, -50, (int) jme.gui.menuCellSize * 13 - 80); // left side of the atom X
																							// menu cell
			}
			this.lastLocation = atomxBoxPoint;
			initAtomxBox();
			break;
		case BOX_ABOUT:
			// BB
			if (aboutBoxPoint == null) {
				aboutBoxPoint = new Point(jmeLocation);
				safeTranslate(aboutBoxPoint, (int) jme.gui.menuCellSize * 5, 0); // right side next to the info menu
																					// cell
			}
			initAboutBox();
			this.lastLocation = aboutBoxPoint;
			break;
		}

		pack();
		setVisible(true);
		
		// BH 2023.01.28 after visible, select text
		switch (box) {
		case BOX_SMILES:
			smilesText.select(0, 1000);
			break;
		case BOX_ATOMX:
			jme.atomicSymbol.select(0, 1000);
			break;
		case BOX_ABOUT:
			break;
		}
		
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
			l.setFont(GUI.copyRigthSmallTextFont);
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
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					submitSmiles(smilesText.getText());
				}
				
			});
		}
		add("South", p);

		// this.setSmiles(smilesText.getText().trim()); //BB : to size the SMILES box
		// correctly - does not work
		// this.setMaximumSize(new Dimension(330, 200)); //BB does not work
		smilesText.setText(smilesText.getText().trim()); // odstrani " "
		setResizable(true);
	}

	protected void submitSmiles(String smiles) {
		try {
			jme.readMolFile(jme.SMILEStoMOL(smiles));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------------------
	// sets smiles in smiles box a aj upravi dlzku
	// BB: resize does not work
	void setSmiles(String smiles) {
		Dimension d = getSize();
		int l = GUI.menuCellFontSmallerMet.stringWidth(smiles) + 50;
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
		add("Center", jme.atomicSymbol);
		p = new JPanel();
		p.add(this.closeJButton);
		add("South", p);
	}

	// ----------------------------------------------------------------------------
	public boolean keyDown(KeyEvent e, int key) {
		jme.dialogActionX();
		return false;
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