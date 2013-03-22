package triaina.webview;

import java.io.File;

import com.google.inject.Inject;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class WebViewRestoreManager {
	private static final String TAG = WebViewRestoreManager.class.getSimpleName();

	private static final String EXTRA_WEBVIEW_PICTURE = "_picture";
	private static final String EXTRA_WEBVIEW_ID = "_webview_id";

	private static final String EXTRA_WEBVIEW_STATE = "_webview_state";
	private static final String WEBVIEW_TMP_FILE = "webview_tmp";

	@Inject
	private Context mContext;

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
			if (Build.VERSION.SDK_INT <= 10) {
				Bundle pictureBundle = savedInstanceState
						.getBundle(EXTRA_WEBVIEW_PICTURE);

				if (pictureBundle == null) {
					Log.w(TAG, "failed to restore webview picture state.");
					return false;
				}

				long id = savedInstanceState.getLong(EXTRA_WEBVIEW_ID);
				File tmp = new File(mContext.getCacheDir(), WEBVIEW_TMP_FILE
						+ "." + id);
				if (!webViewBridge.restorePicture(pictureBundle, tmp)) {
					Log.w(TAG, "failed to restore webview picture state");
					return false;
				}
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
	public void storeWebView(WebViewBridge webViewBridge, Bundle outState,
			long id) {
		Bundle stateBundle = new Bundle();
		if (webViewBridge.saveState(stateBundle) == null) {
			Log.w(TAG, "failed to save webview state");
			return;
		}

		outState.putBundle(EXTRA_WEBVIEW_STATE, stateBundle);
		
		if (Build.VERSION.SDK_INT <= 10) {
			Bundle webViewPictureBundle = new Bundle();
			File tmp = new File(mContext.getCacheDir(), WEBVIEW_TMP_FILE + "."
					+ id);
			if (!webViewBridge.savePicture(webViewPictureBundle, tmp))
				Log.w(TAG, "failed to save webview picture");
			outState.putBundle(EXTRA_WEBVIEW_PICTURE, webViewPictureBundle);
			outState.putLong(EXTRA_WEBVIEW_ID, id);
		}
	}
}
