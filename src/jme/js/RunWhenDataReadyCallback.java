package jme.js;

public interface RunWhenDataReadyCallback {

	/**
	 * Called when, for some reason, the necessary code cannot be loaded. For
	 * example, the web browser might no longer have network access.
	 * 
	 * @j2sAlias onFailure
	 */
	void onFailure(Throwable reason);

	/**
	 * Called once the necessary code for it has been loaded.
	 * 
	 * @j2sAlias onSuccess
	 */
	void onSuccess(String data);

	/**
	 * 
	 * @j2sAlias onWarning
	 * 
	 * @param message
	 */
	void onWarning(String message);

}