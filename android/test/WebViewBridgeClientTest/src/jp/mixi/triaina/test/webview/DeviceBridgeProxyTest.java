package jp.mixi.triaina.test.webview;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.os.Handler;
import android.test.AndroidTestCase;

import jp.mixi.triaina.commons.collection.ImmutableHashMap;
import jp.mixi.triaina.commons.exception.InvocationRuntimeException;
import jp.mixi.triaina.webview.BridgeConfig;
import jp.mixi.triaina.webview.Callback;
import jp.mixi.triaina.webview.DeviceBridgeProxy;
import jp.mixi.triaina.webview.DeviceBridgeProxy.DummyCallback;
import jp.mixi.triaina.webview.DeviceBridgeProxy.WebViewBridgeCallback;
import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.test.mock.MockHandler;
import jp.mixi.triaina.test.mock.MockParams;
import jp.mixi.triaina.test.mock.MockResult;

public class DeviceBridgeProxyTest extends AndroidTestCase {

	public void testDeviceBridgeProxy() {
		new DeviceBridgeProxy(mWebViewBridge,  new Object(), mConfig, new Handler());
	}
	
	public void testNotifyToDevice() throws Exception {
		DeviceBridgeProxy proxy = new DeviceBridgeProxy(mWebViewBridge, mBridge, mConfig, new MockHandler());
		proxy.notifyToDevice("{'bridge':'" + WebViewBridge.VERSION + "', 'dest':'aaa', 'params':{'aaa':'aaa', 'bbb': 'bbb'}}");
		assertEquals("aaa", mBridge.params.getAaa());
		assertEquals("bbb", mBridge.params.getBbb());
	}
	
	public void testNotifyToDeviceUseCallback() throws Exception {
		DeviceBridgeProxy proxy = new DeviceBridgeProxy(mWebViewBridge, mBridge, mConfig, new MockHandler());
		proxy.notifyToDevice("{'bridge':'" + WebViewBridge.VERSION + "', 'id': '1', 'dest':'bbb', 'params':{'aaa':'aaa', 'bbb': 'bbb'}}");
		assertEquals(WebViewBridgeCallback.class, mBridge.callback.getClass());
		assertEquals("aaa", mBridge.params.getAaa());
		assertEquals("bbb", mBridge.params.getBbb());		
	}
	
	public void testNotifyToDeviceUseDummyCallback() throws Exception {
		DeviceBridgeProxy proxy = new DeviceBridgeProxy(mWebViewBridge, mBridge, mConfig, new MockHandler());
		proxy.notifyToDevice("{'bridge':'" + WebViewBridge.VERSION + "', 'dest':'ccc', 'params':{'aaa':'aaa', 'bbb': 'bbb'}}");
		assertEquals(DummyCallback.class, mBridge.callback.getClass());
		assertEquals("aaa", mBridge.params.getAaa());
		assertEquals("bbb", mBridge.params.getBbb());
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

	static class DeviceBridge {
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
	
	static class MockDeviceBridgeProxy extends DeviceBridgeProxy {
		@Override
		public void validateParamsVersion(JSONObject json) {
			super.validateParamsVersion(json);
		}

		public MockDeviceBridgeProxy() {
			super(null, null, null, null);
		}
	}

	private WebViewBridge mWebViewBridge;
	private DeviceBridge mBridge;
	private BridgeConfig mConfig;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		mWebViewBridge = new WebViewBridge(getContext());
		mBridge = new DeviceBridge();
		Map<String, Method> map = new HashMap<String, Method>();
		
		Method met = mBridge.getClass().getMethod("aaa", new Class[]{MockParams.class});
		map.put("aaa", met);
		
		met = mBridge.getClass().getMethod("bbb", new Class[]{MockParams.class, Callback.class});
		map.put("bbb", met);
		
		met = mBridge.getClass().getMethod("ccc", new Class[]{MockParams.class, Callback.class});
		map.put("ccc", met);
		
		mConfig = new BridgeConfig(new String[]{"domain"}, new ImmutableHashMap<String, Method>(map));
	}
}
