package jme.event;

abstract public class ChangeAtomPropertyCallback  {
	public abstract  String actionType();
	public abstract  int getAtomValue();
	public abstract void setAtomValue(int newValue);
	public abstract void reportError(String errorMessage);
	public String atomSymbol;
	public abstract void finished();


	
}