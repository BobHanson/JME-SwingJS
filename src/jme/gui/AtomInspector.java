package jme.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jme.event.ChangeAtomPropertyCallback;
import jme.event.InspectorEvent;

/**
 * Dialog box to set the atom map of the atom - later other properties
 * 
 * @author bruno
 *
 */
public class AtomInspector {
	JDialog modalDialog;
	JFrame window ;
	ActionListener actionListener;
	JTextField atomicMapField;
	ChangeAtomPropertyCallback change;
	public AtomInspector(ChangeAtomPropertyCallback change) {
		// Create an OK button
		final JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");
		atomicMapField = new JTextField("0", 4);
		this.change = change;

		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Hide dialog
				modalDialog.setVisible(false);
				if (e.getSource() == ok) {
					// make the changes
					// System.out.println("Here: " + atomicMapField.getText());
					String newInputValue = atomicMapField.getText().trim();
					// int oldValue = AtomInspector.this.change.getAtomValue();
					try {
						int newValue = Integer.parseInt(newInputValue);
						change.setAtomValue(newValue);

					} catch (NumberFormatException exception) {
						change.reportError(
								"invalid atom " + AtomInspector.this.change.actionType() + ": " + newInputValue);
					}
				}
			}

		};
		// Create a modal dialog


		modalDialog = new JDialog();
//		{
//			@Override
//			public boolean action(Event e, Object arg) {
//				this.setVisible(false);
//				if (e.target == ok) {
//					// make the changes
//					// System.out.println("Here: " + atomicMapField.getText());
//					String newInputValue = atomicMapField.getText().trim();
//					//int oldValue = AtomInspector.this.change.getAtomValue();
//					try {
//						int newValue = Integer.parseInt(newInputValue);
//						AtomInspector.this.change.setAtomValue(newValue);
//
//					} catch (NumberFormatException exception) {
//						 AtomInspector.this.change.reportError("invalid atom " + AtomInspector.this.change.actionType() + ": " +  newInputValue);
//					}
//				}
//				AtomInspector.this.change.finished();
//
//				return true;
//			}
//
//		};

		modalDialog.setModal(true);
		// Use a flow layout
		modalDialog.setLayout(new FlowLayout());

		ok.addActionListener(actionListener);
		cancel.addActionListener(actionListener);
		String label = "New atom " + change.actionType();
		modalDialog.add(new JLabel(label));
		modalDialog.add(ok);
		modalDialog.add(cancel);
		modalDialog.setLayout(new BorderLayout(2, 0)); // 2, 0 gaps

		JPanel p = new JPanel();

		p.add(new JLabel("new atom " + change.actionType(), JLabel.CENTER));
		p.add(atomicMapField);
		modalDialog.add("North", p);

		p = new JPanel();
		p.add(ok);
		p.add(cancel);

		modalDialog.add("South", p);

		// Add a window listener JDK 1.1 for closing the window using the X button of
		// the window
		modalDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				modalDialog.setVisible(false);
			}
		});

	}

	public void action(InspectorEvent event) {
		if (event.atomIndex > 0) {
			String title = "Change " + change.actionType() + " of atom  " + change.atomSymbol + " #" + event.atomIndex;
			modalDialog.setTitle(title);
			atomicMapField.setText("" + change.getAtomValue());
			// recompute the layount
			modalDialog.setMinimumSize(new Dimension(250, 100));
			modalDialog.pack();
			Point loc = event.jme.getLocationOnScreen();
			modalDialog.setLocation(loc.x + event.x + 30, loc.y + event.y);
			// Show dialog
			modalDialog.setVisible(true);
			atomicMapField.requestFocusInWindow();
			atomicMapField.select(0, 1000);
		}

	}

}