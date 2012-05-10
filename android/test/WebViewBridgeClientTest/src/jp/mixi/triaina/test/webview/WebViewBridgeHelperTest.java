package jp.mixi.triaina.test.webview;

import android.test.AndroidTestCase;

import jp.mixi.triaina.webview.WebViewBridgeHelper;

public class WebViewBridgeHelperTest extends AndroidTestCase {
	private WebViewBridgeHelper mBridgeHelper;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mBridgeHelper = new WebViewBridgeHelper();
	}
	
	public void testLoadJavaScript() {
		assertEquals("javascript: func();", mBridgeHelper.makeJavaScript("func"));
		assertEquals("javascript: func('arg1');", mBridgeHelper.makeJavaScript("func", "arg1"));
		assertEquals("javascript: func('arg1','arg2');", mBridgeHelper.makeJavaScript("func", "arg1", "arg2"));
	}
}
