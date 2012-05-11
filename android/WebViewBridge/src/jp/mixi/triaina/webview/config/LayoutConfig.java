package jp.mixi.triaina.webview.config;

public class LayoutConfig {
	private int mLayoutId;
	private int mWebViewBridgeId;
	
	public LayoutConfig(int layoutId, int webViewBridgeId) {
		mLayoutId = layoutId;
		mWebViewBridgeId = webViewBridgeId;
	}
	
	public int getLayoutId() {
		return mLayoutId;
	}
	
	public int getWebViewBridgeId() {
		return mWebViewBridgeId;
	}
}
