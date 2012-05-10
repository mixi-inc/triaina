package jp.mixi.triaina.test.mock;

import android.content.Context;
import android.webkit.WebView;

public class MockWebView extends WebView {
	private String loadedUrl;

	public MockWebView(Context context) {
		super(context);
	}

	@Override
	public void loadUrl(String url) {
		this.loadedUrl = url;
	}

	public String getLoadedUrl() {
		return loadedUrl;
	}
}
