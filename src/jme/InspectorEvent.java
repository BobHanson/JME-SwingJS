package jme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jme.JMEUtil.JSME_RunAsyncCallback;

class InspectorEvent implements ActionListener {
	JMEmol mol;
	int atomIndex;
	int bondIndex;
	int molIndex; // 0 based
	int x;
	int y;
	JME jme;

	

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

		JMEUtil.runAsync(new JMEUtil.JSME_RunAsyncCallback() {

			@Override
			public void onSuccess() {
				ChangeAtomPropertyCallback change = null;
				if (e.getActionCommand() == JME.changeAtomMapAction || e.getActionCommand() == JME.changeAtomMarkAction) {

					final String actionType = e.getActionCommand() == JME.changeAtomMapAction ? "map": "mark" ;
					change = new ChangeAtomPropertyCallback() {
						
						@Override
						public void setAtomValue(int newValue) {
							InspectorEvent.this.changeAtomMap(newValue);
						}
						
						@Override
						public int getAtomValue() {
							return InspectorEvent.this.getAtomMap();
						}

						@Override
						public String actionType() {
							return actionType;
						}

						@Override
						public void reportError(String errorMessage) {
							InspectorEvent.this.reportError(errorMessage);
						}

						@Override
						protected void finished() {
							InspectorEvent.this.jme.setAtomToHighLight(molIndex, 0);
							InspectorEvent.this.jme.requestFocusInWindow();
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
							InspectorEvent.this.reportError(errorMessage);
						}

						@Override
						protected void finished() {
							InspectorEvent.this.jme.setAtomToHighLight(molIndex, 0);
							InspectorEvent.this.jme.requestFocusInWindow();
							
						}
							
						
						
					};

				}
				
				if (change != null) {
					change.atomSymbol = mol.getAtomLabel(atomIndex);
					AtomInspector atomInspector = new AtomInspector(change);
					InspectorEvent.this.jme.setAtomToHighLight(molIndex, atomIndex);
					atomInspector.action(InspectorEvent.this);
					
				}

			}

		});

	}

	protected void changeAtomMap(int newMap) {
		if (newMap >=0) {
			this.jme.changeAtomMap(this.mol, this.atomIndex, newMap);
		} else {
			this.reportError("Atom map or mark should be positive");
		}
	}

	protected void changeAtomCharge(int newCharge) {
		this.jme.changeAtomCharge(this.mol, this.atomIndex, newCharge);
	}
	public void reportError(String message) {
		this.jme.showError(message);

	}

}