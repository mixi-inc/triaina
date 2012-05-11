package jp.mixi.triaina.webview;

import jp.mixi.triaina.webview.AbstractWebViewBridgeActivity;
import jp.mixi.triaina.webview.annotation.Domain;
import jp.mixi.triaina.webview.annotation.Layout;
import jp.mixi.triaina.webview.annotation.WebViewBridge;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//@Domain("mixi.jp")
@Domain("10.192.82.8")
@Layout(R.layout.main)
@WebViewBridge(R.id.webview)
public class TriainaWebViewBridgeClientActivity extends AbstractWebViewBridgeActivity {
	private static final String TAG = "MixiWebViewBridgeClientActivity";
	
	private boolean mReceivedTitle = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	getWebViewBridge().clearCache(true);
    	
    	//getWebView().loadUrl("http://dvm160.lo.mixi.jp:8801/");
    	getWebViewBridge().loadUrl("http://10.192.82.8:3000/webview");
    	getWebViewBridge().requestFocus();
    	getWebViewBridge().setWebChromeClient(new WebChromeClient() {
    		/*
    		@Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mReceivedTitle = true;
                Log.d(TAG, "onReceivedTitle!!");
            }*/
    	});
    	getWebViewBridge().setWebViewClient(new WebViewClient() {
    		private int mStarted;
    		private boolean mFinished;
    		
    		private boolean isRedirect() {
    			return mStarted > 1 || mFinished;
    		}
    		
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				Log.d(TAG, "started!!");
				++mStarted;
				mReceivedTitle = false;				
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				Log.d(TAG, "finished!!");
				--mStarted;
				mFinished = true;
			}
    		
    	});
    	getWebViewBridge().getSettings().setJavaScriptEnabled(true);
    }

}