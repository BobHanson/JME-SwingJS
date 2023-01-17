package jme;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class SDFstack extends ArrayList<String> {

	protected int currentIndex = -1;
	
	/**
	 * 
	 * @return the current index starting counting at 1
	 */
	public int getCurrentDisplayIndex() {
		return currentIndex + 1;
	}

	public int addEntries(String sdfInput) {
		if(sdfInput == null) return 0;
		String inputSDFentries[]  = sdfInput.split("\\$\\$\\$\\$\r?\n?");
		//String inputSDFentries[]  = sdfInput.split("a", 10);
		
		if(inputSDFentries.length <= 1) {
			return 0;
		}
		for(String sdf : inputSDFentries) {
			this.add(sdf);
		}
			
		return inputSDFentries.length;
	}
	
	public String next() {
		if(currentIndex < this.size() - 1) {
			currentIndex ++ ;
			return this.get(currentIndex);
		} else {
			return null;
		}
	}

	public String previous() {
		if(currentIndex > 0) {
			currentIndex -- ;
			return this.get(currentIndex);
		} else {
			return null;
		}
	}
	
	public String first() {
		currentIndex = -1;
		return this.next();
	}
	
	public String last() {
		currentIndex = this.size();

		return this.previous();
	}
	/**
	 * Removes all of the elements from this list. The list will be empty after this call returns.
	 */
	@Override
	public void clear() {
		this.currentIndex = -1;
		super.clear();
	}
	
	/**
	 * JME accepts a SDF with multiple records. This method returns the list of the  SDF records that were input 
	 * @return a string array, which could be empty
	 */
	public String[] getMultiSDFstack() {
		
		return  this.toArray(new String[this.size()]);
	}

}
