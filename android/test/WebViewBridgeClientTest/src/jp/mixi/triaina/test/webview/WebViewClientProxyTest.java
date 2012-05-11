package jp.mixi.triaina.test.webview;
import jp.mixi.triaina.webview.WebViewBridge.WebViewClientProxy;
import jp.mixi.triaina.webview.config.DomainConfig;
import junit.framework.TestCase;
import android.webkit.WebViewClient;

public class WebViewClientProxyTest extends TestCase {
    private DomainConfig mConfig;
    
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        mConfig = new DomainConfig(new String[]{"mixi.jp", "mixi.co.jp"});
    }

    public void testOnPageStarted() {
		WebViewClient client = new WebViewClientProxy(new WebViewClient(), mConfig);
		client.onPageStarted(null, "http://mixi.jp", null);
		client.onPageStarted(null, "http://mixi.co.jp", null);
		client.onPageStarted(null, "http://t.mixi.jp", null);
		client.onPageStarted(null, "http://t.mixi.co.jp", null);
		
	}
	
	public void testOnPageStartedOnException() {
		try {
			WebViewClient client = new WebViewClientProxy(new WebViewClient(), mConfig);
			client.onPageStarted(null, "http://mix.jp", null);
			fail();
		} catch (Exception exp) {
		}
	}
}
