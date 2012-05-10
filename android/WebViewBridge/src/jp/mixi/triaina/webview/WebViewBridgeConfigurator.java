package jp.mixi.triaina.webview;

import android.app.Activity;

public interface WebViewBridgeConfigurator {
	
	public WebViewBridge loadWebViewBridge(Activity activity);
	
	public void configureBridge(WebViewBridge webViewBridge, Object bridgeObject);
}
