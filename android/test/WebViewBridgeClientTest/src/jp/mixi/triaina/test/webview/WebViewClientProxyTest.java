package jp.mixi.triaina.test.webview;
import android.webkit.WebViewClient;

import jp.mixi.triaina.webview.WebViewBridge.WebViewClientProxy;
import junit.framework.TestCase;

public class WebViewClientProxyTest extends TestCase {

	public void testOnPageStarted() {
		WebViewClient client = new WebViewClientProxy(new WebViewClient(), new String[]{"mixi.jp", "mixi.co.jp"});
		client.onPageStarted(null, "http://mixi.jp", null);
		client.onPageStarted(null, "http://mixi.co.jp", null);
		client.onPageStarted(null, "http://t.mixi.jp", null);
		client.onPageStarted(null, "http://t.mixi.co.jp", null);
		
	}
	
	public void testOnPageStartedOnException() {
		try {
			WebViewClient client = new WebViewClientProxy(new WebViewClient(), new String[]{"mixi.jp", "mixi.co.jp"});
			client.onPageStarted(null, "http://mix.jp", null);
			fail();
		} catch (Exception exp) {
		}
	}
}
