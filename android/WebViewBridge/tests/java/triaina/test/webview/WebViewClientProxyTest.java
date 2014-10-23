package triaina.test.webview;

import android.webkit.WebViewClient;

import junit.framework.TestCase;

import triaina.commons.exception.SecurityRuntimeException;
import triaina.webview.WebViewBridge.SecurityRuntimeExceptionResolver;
import triaina.webview.WebViewBridge.WebViewClientProxy;
import triaina.webview.config.DomainConfig;

public class WebViewClientProxyTest extends TestCase {
    private DomainConfig mConfig;

    private static final String WRONG_URL = "http://mix.jp";

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

    public void testOriginalOnPageStartedOnException() {
        try {
            WebViewClient client = new WebViewClientProxy(new WebViewClient(), mConfig);
            client.onPageStarted(null, WRONG_URL, null);
            fail();
        } catch (SecurityRuntimeException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    public void testOnPageStartedOnExceptionWithoutExceptionResolver() {
        try {
            WebViewClient client = new WebViewClientProxy(new WebViewClient(), mConfig, null);
            client.onPageStarted(null, WRONG_URL, null);
            fail();
        } catch (SecurityRuntimeException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    public void testOnPageStartedOnExceptionHandlingWithExceptionResolver() {
        try {
            SecurityRuntimeExceptionResolver resolver = new SecurityRuntimeExceptionResolver() {
                @Override
                public void resolve(SecurityRuntimeException e) {
                    assertEquals(WRONG_URL, e.getUrl());
                }
            };
            WebViewClient client = new WebViewClientProxy(new WebViewClient(), mConfig, resolver);
            client.onPageStarted(null, WRONG_URL, null);
        } catch (SecurityRuntimeException e) {
            fail();
        } catch (Exception e) {
            fail();
        }
    }
}
