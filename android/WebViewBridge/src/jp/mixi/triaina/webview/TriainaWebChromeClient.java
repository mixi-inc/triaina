package jp.mixi.triaina.webview;

import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebStorage.QuotaUpdater;

public class TriainaWebChromeClient extends WebChromeClient {
    private static final String TAG = "TriainaWebChromeClient";

    private ProgressManager mProgressManager;
    
    public TriainaWebChromeClient(ProgressManager progressManager) {
        mProgressManager = progressManager;
    }
    
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        Log.d(TAG, "onProgressChanged newProgress = " + newProgress);
        if (!mProgressManager.isProgress() && newProgress < 30)
            mProgressManager.showProgress(newProgress);
        else if (mProgressManager.isProgress() && newProgress == 100)
            mProgressManager.hideProgress();
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        Log.d(TAG, sourceID + ":" + lineNumber + " " + message);
    }

    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota,
            long estimatedSize, long totalUsedQuota, QuotaUpdater quotaUpdater) {
        quotaUpdater.updateQuota(5 * 1024 * 1024);
    }
}
