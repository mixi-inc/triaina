package jp.mixi.triaina.webview;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TriainaWebViewClient extends WebViewClient {
    private static final String TAG = "TriainaWebViewClient";
    
    private ProgressManager mProgressManager;
    
    public TriainaWebViewClient(ProgressManager progressManager) {
        mProgressManager = progressManager;
    }
    
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        mProgressManager.hideProgress();
        Log.d(TAG, "onPageFinished");
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        mProgressManager.hideProgress();
        Log.d(TAG, "onReceivedError");
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
