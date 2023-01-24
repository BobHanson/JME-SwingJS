package jme;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

//****************************************************************************
// show an alert box centered near the center of the source
//mimic the Javascript alert()( function
@SuppressWarnings("serial")
class AlertBox extends FrameWithLocation {

	// the source can be the applet
	public AlertBox(String msg, Component source, Color backgroundColor) {
		super();

		setResizable(false);

		this.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel label = new JLabel(msg, JLabel.CENTER);
		this.add(label);

		// an extra JPanel is needed, otherwise the button will be as wide as the frame
		JPanel p = new JPanel();
		p.add(this.closeJButton);
		this.add(p);

		if (source != null) {
			// center the window on top of the source

			this.lastLocation = source.getLocationOnScreen();
			this.pack(); // the translation must be done after the pack() otherwise the size of the
							// window (myself) is not known (=0)

			this.safeTranslate(this.lastLocation, source.getWidth() / 2 - this.getWidth() / 2,
					source.getHeight() / 2 - this.getHeight() / 2);
		}
		if (backgroundColor != null) {
			setBackground(backgroundColor);
		}

	}

	@Override
	public String closeJButtonJLabel() {
		return "OK";
	}

}