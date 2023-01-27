package jme.js;

public abstract class JSME_RunAsyncCallback implements RunAsyncCallback {

	/**
	 * @j2sAlias onFailure
	 */
	@Override
	public void onFailure(Throwable reason) {
		// Window.alert("Loading JS code failed");

	}
}