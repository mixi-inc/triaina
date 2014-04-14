package triaina.webview;

import android.os.Bundle;
import android.util.Log;

public class WebViewRestoreManager {
	private static final String TAG = WebViewRestoreManager.class.getSimpleName();

	private static final String EXTRA_WEBVIEW_STATE = "_webview_state";

	@SuppressWarnings("deprecation")
	public boolean restoreWebView(WebViewBridge webViewBridge,
			Bundle savedInstanceState) {
		if (savedInstanceState == null)
			return false;

		try {

			Bundle stateBundle = savedInstanceState.getBundle(EXTRA_WEBVIEW_STATE);
			if (stateBundle == null || stateBundle.size() == 0
					|| webViewBridge.restoreState(stateBundle) == null) {
				Log.w(TAG, "failed to restore webview state");
				return false;
			}
		} catch (NullPointerException e) {
			// sometimes, especially repeating fast orientation
			// changes, WebView#restoreState() throws NPE.
			// Just ignore here.
			Log.w(TAG, "NullPointerException thrown during restoreState()");
			return false;
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	public void storeWebView(WebViewBridge webViewBridge, Bundle outState) {
		Bundle stateBundle = new Bundle();
		if (webViewBridge.saveState(stateBundle) == null) {
			Log.w(TAG, "failed to save webview state");
			return;
		}

		outState.putBundle(EXTRA_WEBVIEW_STATE, stateBundle);
	}
}
