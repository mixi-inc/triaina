package jp.mixi.triaina.webview.config;

import jp.mixi.triaina.webview.WebViewBridge;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface WebViewBridgeConfigurator {
	
	public WebViewBridge loadWebViewBridge(Activity activity);
	
	public View loadInflatedView(Fragment fragment, LayoutInflater inflater, ViewGroup container);
	
	public WebViewBridge loadWebViewBridge(Fragment fragment, View inflatedView);
	
	public void configure(WebViewBridge webViewBridge);
	
	public void configure(WebViewBridge webViewBridge, Object bridgeObject);
	
	public void configureSetting(WebViewBridge webViewBridge);
}
