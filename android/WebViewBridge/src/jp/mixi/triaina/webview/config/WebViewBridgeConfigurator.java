package jp.mixi.triaina.webview.config;

import jp.mixi.triaina.webview.WebViewBridge;
import android.app.Activity;

public interface WebViewBridgeConfigurator {
	
	public WebViewBridge loadWebViewBridge(Activity activity);
	
	public void configure(WebViewBridge bridge);
	
	public void configure(WebViewBridge webViewBridge, Object bridgeObject);
}
