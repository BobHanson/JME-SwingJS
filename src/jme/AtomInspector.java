package jme;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Dialog box to set the atom map of the atom - later other properties
 * 
 * @author bruno
 *
 */
class AtomInspector {
	JDialog modalDialog;
	JFrame window ;
	ActionListener actionListener;
	JTextField atomicMapField;
	ChangeAtomPropertyCallback change;


	
	@SuppressWarnings("serial")
	public AtomInspector(ChangeAtomPropertyCallback change) {
		// Create an OK button
		final JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");
		atomicMapField = new JTextField("0", 4);
		this.change = change;

//		actionListener = new  ActionListener()
//		{
//			public void actionPerformed( ActionEvent e )
//			{
//				// Hide dialog
//				modalDialog.setVisible(false);
//				if(e.getSource() == ok) {
//					//make the changes
//					//System.out.println("Here: " + atomicMapField.getText());
//					try {
//						int newMap = Integer.parseInt(atomicMapField.getText());
//						inspectorEvent.changeAtomMap(newMap);
//
//					} catch (NumberFormatException exception) {
//						inspectorEvent.reportError("invalid atom map");
//					}
//				}
//			}
//		};
		// Create a modal dialog

		// parent : should be the applet window
		this.window = new JFrame();

		modalDialog = new JDialog(this.window) {
			@Override
			public boolean action(Event e, Object arg) {
				this.setVisible(false);
				if (e.target == ok) {
					// make the changes
					// System.out.println("Here: " + atomicMapField.getText());
					String newInputValue = atomicMapField.getText().trim();
					//int oldValue = AtomInspector.this.change.getAtomValue();
					try {
						int newValue = Integer.parseInt(newInputValue);
						AtomInspector.this.change.setAtomValue(newValue);

					} catch (NumberFormatException exception) {
						 AtomInspector.this.change.reportError("invalid atom " + AtomInspector.this.change.actionType() + ": " +  newInputValue);
					}
				}
				AtomInspector.this.change.finished();

				return true;
			}

		};
		
		
		modalDialog.setModal(true);
		// Use a flow layout
		modalDialog.setLayout(new FlowLayout());

		// action listener is not implemented in JSapplet
//		ok.addActionListener( actionListener );
//		
//		cancel.addActionListener( actionListener );
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
			public void windowClosing(WindowEvent evt) {
				modalDialog.setVisible(false);
			}
		});

	}

	public void action(InspectorEvent event) {
		if (event.atomIndex > 0) {
			String title = "Change " + change.actionType() +  " of atom  "  + change.atomSymbol + " #"  + event.atomIndex;
			modalDialog.setTitle(title);
			atomicMapField.setText( ""  + change.getAtomValue());



			// recompute the layount
			modalDialog.pack();
			modalDialog.setLocation(event.x + 30, event.y);
			// Show dialog
			modalDialog.setVisible(true);
			
			//atomicMapField.focus();
			// This will only work when the widget is attached to the document and not
			//atomicMapField.selectAll(); // the dialog is not visible yet, thus there is no effect

			//modalDialog.setVisible(true);

		}

	}

}