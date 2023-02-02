package jme.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jme.JME;
import jme.JMEmol;
import jme.gui.AtomInspector;
import jme.js.JSME_RunAsyncCallback;
import jme.util.JMEUtil;

public class InspectorEvent implements ActionListener {
	public JME jme;
	public JMEmol mol;
	public int atomIndex;
	public int bondIndex;
	public int molIndex; // 0 based
	public int x;
	public int y;

	

	public InspectorEvent(JME jme) {
		this.jme = jme;
	}

	public void reset() {
		mol = null;
		molIndex = atomIndex = bondIndex = x = y = 0;
	}

	protected int getAtomMap() {
		return mol.findAtomMapForOutput(this.atomIndex);
	}

	protected int getAtomCharge() {
		return mol.findAtomChargeForOutput(this.atomIndex);
	}
	@Override
	public void actionPerformed(final ActionEvent e) {

		JMEUtil.runAsync(new JSME_RunAsyncCallback() {

			/**
			 * @j2sAlias onSuccess
			 */
			@Override
			public void onSuccess() {
				ChangeAtomPropertyCallback change = null;
				if (e.getActionCommand() == JME.changeAtomMapAction || e.getActionCommand() == JME.changeAtomMarkAction) {

					final String actionType = e.getActionCommand() == JME.changeAtomMapAction ? "map": "mark" ;
					change = new ChangeAtomPropertyCallback() {
						
						@Override
						public void setAtomValue(int newValue) {
							changeAtomMap(newValue);
						}
						
						@Override
						public int getAtomValue() {
							return getAtomMap();
						}

						@Override
						public String actionType() {
							return actionType;
						}

						@Override
						public void reportError(String errorMessage) {
							showError(errorMessage);
						}

						@Override
						public void finished() {
							jme.setAtomToHighLight(molIndex, 0);
							jme.requestFocusInWindow();
						}
						
					};

				}

				else if (e.getActionCommand() == JME.changeAtomChargeAction) {
					final String actionType = "charge";
					
					change = new ChangeAtomPropertyCallback() {
						
						@Override
						public void setAtomValue(int newValue) {
							InspectorEvent.this.changeAtomCharge(newValue);
						}
						
						@Override
						public int getAtomValue() {
							return InspectorEvent.this.getAtomCharge();
						}

						@Override
						public String actionType() {
							// TODO Auto-generated method stub
							return actionType;
						}

						@Override
						public void reportError(String errorMessage) {
							showError(errorMessage);
						}

						@Override
						public void finished() {
							jme.setAtomToHighLight(molIndex, 0);
							jme.requestFocusInWindow();
							
						}
							
						
						
					};

				}
				
				if (change != null) {
					change.atomSymbol = mol.getAtomLabel(atomIndex);
					AtomInspector atomInspector = new AtomInspector(change);
					jme.setAtomToHighLight(molIndex, atomIndex);
					atomInspector.action(InspectorEvent.this);
					
				}

			}

		});

	}

	protected void changeAtomMap(int newMap) {
		if (newMap >=0) {
			jme.changeAtomMap(this.mol, this.atomIndex, newMap);
		} else {
			showError("Atom map or mark should be positive");
		}
	}

	protected void changeAtomCharge(int newCharge) {
		jme.changeAtomCharge(this.mol, this.atomIndex, newCharge);
	}
	public void showError(String message) {
		jme.showError(message);

	}

}