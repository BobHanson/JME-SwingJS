package jme.js;

public interface RunWhenDataReadyCallback {

	/**
	 * Called when, for some reason, the necessary code cannot be loaded. For
	 * example, the web browser might no longer have network access.
	 * 
	 */
	void onFailure(Throwable reason);

	/**
	 * Called once the necessary code for it has been loaded.
	 * 
	 */
	void onSuccess(String data);

	/**
	 * 
	 * @param message
	 */
	void onWarning(String message);

}