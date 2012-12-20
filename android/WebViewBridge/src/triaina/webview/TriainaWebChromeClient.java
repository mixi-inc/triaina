package triaina.webview;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebStorage.QuotaUpdater;

public class TriainaWebChromeClient extends WebChromeClient {
	private static final String TAG = "TriainaWebChromeClient";
	
	private Activity mActivity;

	public TriainaWebChromeClient(Activity activity) {
		mActivity = activity;
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		super.onProgressChanged(view, newProgress);
		Log.d(TAG, "onProgressChanged newProgress = " + newProgress);

		mActivity.setProgress(newProgress * 1000);
	}

	@Override
	public void onConsoleMessage(String message, int lineNumber, String sourceID) {
		Log.d(TAG, sourceID + ":" + lineNumber + " " + message);
	}

	@Override
	public void onExceededDatabaseQuota(String url, String databaseIdentifier,
			long currentQuota, long estimatedSize, long totalUsedQuota,
			QuotaUpdater quotaUpdater) {
		quotaUpdater.updateQuota(5 * 1024 * 1024);
	}
}
