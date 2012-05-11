package jp.mixi.triaina.test.webview;

import org.json.JSONObject;

import android.os.Handler;
import android.test.AndroidTestCase;

import jp.mixi.triaina.commons.exception.InvocationRuntimeException;
import jp.mixi.triaina.webview.Callback;
import jp.mixi.triaina.webview.DeviceBridgeProxy;
import jp.mixi.triaina.webview.DeviceBridgeProxy.DummyCallback;
import jp.mixi.triaina.webview.DeviceBridgeProxy.WebViewBridgeCallback;
import jp.mixi.triaina.webview.config.BridgeMethodConfig;
import jp.mixi.triaina.webview.config.BridgeObjectConfig;
import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.test.mock.MockHandler;
import jp.mixi.triaina.test.mock.MockParams;
import jp.mixi.triaina.test.mock.MockResult;

public class DeviceBridgeProxyTest extends AndroidTestCase {

    private WebViewBridge mWebViewBridge;
    private BridgeObjectConfig mConfig1;
    private BridgeObjectConfig mConfig2;
    private DeviceBridge1 mBridge1;
    private DeviceBridge2 mBridge2;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        mWebViewBridge = new WebViewBridge(getContext());
        
        mConfig1 = new BridgeObjectConfig();
        mBridge1 = new DeviceBridge1();
        mConfig1.add(new BridgeMethodConfig("aaa",
                mBridge1.getClass().getMethod("aaa", new Class[]{MockParams.class})));
        mConfig1.add(new BridgeMethodConfig("bbb",
                mBridge1.getClass().getMethod("bbb", new Class[]{MockParams.class, Callback.class})));
        mConfig1.add(new BridgeMethodConfig("ccc",
                mBridge1.getClass().getMethod("ccc", new Class[]{MockParams.class, Callback.class})));
        
        mConfig2 = new BridgeObjectConfig();
        mBridge2 = new DeviceBridge2();
        mConfig2.add(new BridgeMethodConfig("ddd",
                mBridge2.getClass().getMethod("ddd", new Class[]{})));        
    }
    
	public void testDeviceBridgeProxy() {
		new DeviceBridgeProxy(mWebViewBridge, new Handler());
	}
	
	public void testNotifyToDevice() throws Exception {
		DeviceBridgeProxy proxy = new DeviceBridgeProxy(mWebViewBridge, new MockHandler());
		proxy.addBridgeObjectConfig(mBridge1, mConfig1);
		proxy.notifyToDevice("{'bridge':'" + WebViewBridge.VERSION + "', 'dest':'aaa', 'params':{'aaa':'aaa', 'bbb': 'bbb'}}");
		assertEquals("aaa", mBridge1.params.getAaa());
		assertEquals("bbb", mBridge1.params.getBbb());
	}
	
	public void testNotifyToDeviceUseCallback() throws Exception {
		DeviceBridgeProxy proxy = new DeviceBridgeProxy(mWebViewBridge, new MockHandler());
		proxy.addBridgeObjectConfig(mBridge1, mConfig1);
		proxy.notifyToDevice("{'bridge':'" + WebViewBridge.VERSION + "', 'id': '1', 'dest':'bbb', 'params':{'aaa':'aaa', 'bbb': 'bbb'}}");
		assertEquals(WebViewBridgeCallback.class, mBridge1.callback.getClass());
		assertEquals("aaa", mBridge1.params.getAaa());
		assertEquals("bbb", mBridge1.params.getBbb());		
	}
	
	public void testNotifyToDeviceUseDummyCallback() throws Exception {
		DeviceBridgeProxy proxy = new DeviceBridgeProxy(mWebViewBridge, new MockHandler());
		proxy.addBridgeObjectConfig(mBridge1, mConfig1);
		proxy.notifyToDevice("{'bridge':'" + WebViewBridge.VERSION + "', 'dest':'ccc', 'params':{'aaa':'aaa', 'bbb': 'bbb'}}");
		assertEquals(DummyCallback.class, mBridge1.callback.getClass());
		assertEquals("aaa", mBridge1.params.getAaa());
		assertEquals("bbb", mBridge1.params.getBbb());
	}
	
	public void testNotifyToDeviceNoArgument() {
        DeviceBridgeProxy proxy = new DeviceBridgeProxy(mWebViewBridge, new MockHandler());
        proxy.addBridgeObjectConfig(mBridge2, mConfig2);
        proxy.notifyToDevice("{'bridge':'" + WebViewBridge.VERSION + "', 'dest':'ddd', 'params':{}}");
        assertEquals(true, mBridge2.called);
    }
	
	public void testValidateParamsVersion() throws Exception {
		JSONObject json = new JSONObject();
		json.put("bridge", "1.1");
		MockDeviceBridgeProxy proxy = new MockDeviceBridgeProxy();
		proxy.validateParamsVersion(json);
	}
	
	public void testValidateParamsVersionOnException() throws Exception {
		JSONObject json = new JSONObject();
		json.put("bridge", "0.5");
		
		try {
			MockDeviceBridgeProxy proxy = new MockDeviceBridgeProxy();
			proxy.validateParamsVersion(json);
			fail();
		} catch (InvocationRuntimeException exp) {
		}
		
		json.put("bridge", "2.0");
		try {
			MockDeviceBridgeProxy proxy = new MockDeviceBridgeProxy();
			proxy.validateParamsVersion(json);
			fail();
		} catch (InvocationRuntimeException exp) {
		}
	}

	static class DeviceBridge1 {
		private MockParams params;
		private Callback<MockResult> callback;
		
		public void aaa(MockParams params) {
			this.params = params;
		}
		
		public void bbb(MockParams params, Callback<MockResult> callback) {
			this.params = params;
			this.callback = callback;
		}
		
		public void ccc(MockParams params, Callback<MockResult> callback) {
			this.params = params;
			this.callback = callback;
			callback.succeed(null, new MockResult());
		}
	}
	
	static class DeviceBridge2 {
	    private boolean called;
	    public void ddd() {
	        called = true;
	    }
	}
	
	static class MockDeviceBridgeProxy extends DeviceBridgeProxy {
		@Override
		public void validateParamsVersion(JSONObject json) {
			super.validateParamsVersion(json);
		}

		public MockDeviceBridgeProxy() {
			super(null, null);
		}
	}
}
