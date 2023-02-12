package jme.js;

/**
 * A callback meant to be used by
 * {@link gwt_compat.google.gwt.core.client.GWT#runAsync(RunAsyncCallback) }.
 */
public interface AsyncCallback {
	
	/**
	 * Called when, for some reason, the necessary code cannot be loaded. For
	 * example, the web browser might no longer have network access.
	 * 
	 */
	default public void onFailure(Throwable reason) {
		
	}

	/**
	 * Called once the necessary code for it has been loaded.
	 */
	default public void onSuccess() {
		
	}
	
	/**
	 * Called once the necessary code for it has been loaded.
	 */
	default public void onWarn() {
		
	}

}