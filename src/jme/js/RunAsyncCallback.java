package jme.js;

/**
 * Used by JSME for code splitting
 * 
 * 
 * 
 */
/**
 * A callback meant to be used by
 * {@link gwt_compat.google.gwt.core.client.GWT#runAsync(RunAsyncCallback) }.
 */
public interface RunAsyncCallback {
	/**
	 * Called when, for some reason, the necessary code cannot be loaded. For
	 * example, the web browser might no longer have network access.
	 * 
	 * @j2sAlias onFailure
	 */
	public void onFailure(Throwable reason);

	/**
	 * Called once the necessary code for it has been loaded.
	 * @j2sAlias onSuccess
	 */
	public void onSuccess();
}