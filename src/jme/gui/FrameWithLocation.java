package jme.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

@SuppressWarnings("serial")
class FrameWithLocation extends JFrame {
	protected Point lastLocation;
	protected JButton closeJButton;

	public FrameWithLocation(String title) {
		super(title);
		initialize();
	}

	public FrameWithLocation() {
		super();
		initialize();
	}

	public String closeJButtonJLabel() {
		return "Close";
	}

	public void initialize() {
		closeJButton = new JButton(this.closeJButtonJLabel());
		
		closeJButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		

		// Add a window listener JDK 1.1 for closing the window using the X button of
		// the window
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				setVisible(false);
			}
		});

	}

	// BH -- Swing recursive setvisible -> hide in JavaScript
	@Override
	public void setVisible(boolean tf) {
		if (tf) {
			this.setLocation(this.lastLocation);
		} else {
			if (this.isShowing()) {
				Point currentLocation = this.getLocationOnScreen();
				lastLocation.setLocation(currentLocation);
			}
			// Idea: record the delta position relative to the applet instead of the
			// absolute position
		}
		super.setVisible(tf);
	}

	public void disposeIfShowing() {
		if (this.isShowing()) {
			this.dispose(); // will call hide()
		}
	}

//	@Override
//	public void show() {
//		this.setLocation(this.lastLocation);
//		super.setVisible(true);
//	}

//	// ----------------------------------------------------------------------------
//	public boolean action(Event e, Object arg) {
//		if (e.target == this.closeJButton) {
//			this.setVisible(false);
//			return true;
//		}
//		return this.customAction(e, arg); // for the queryBox
//
//		/*
//		 * else if ("Submit".equals(arg)) { jme.readSmiles(smilesText.getText()); }
//		 */
//	}

//	public boolean customAction(Event e, Object arg) {
//		return false;
//	}

	// avoid a x < 0 or y <
	public static void safeTranslate(Point location, int deltaX, int deltaY) {
		location.translate(deltaX, deltaY);
		location.x = location.x < 0 ? 0 : location.x;
		location.y = location.y < 0 ? 0 : location.y;

	}

}