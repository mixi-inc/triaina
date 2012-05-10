package jp.mixi.triaina.test.webview;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import jp.mixi.triaina.commons.collection.ImmutableHashMap;
import jp.mixi.triaina.webview.BridgeConfig;
import jp.mixi.triaina.webview.Callback;
import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.webview.entity.Result;
import jp.mixi.triaina.test.mock.MockContext;
import jp.mixi.triaina.test.mock.MockHandler;
import jp.mixi.triaina.test.mock.MockParams;
import android.os.Handler;
import android.os.Parcel;
import android.test.AndroidTestCase;

public class WebViewBridgeTest extends AndroidTestCase {
	private MockContext mockContext;
	private BridgeConfig mConfig;
	
	protected void setUp() throws Exception {
		super.setUp();
		mockContext = new MockContext();
		
		HashMap<String, Method> map = new HashMap<String, Method>();
		map.put("notify", mockContext.getClass().getMethod("notifyFunc", new Class[]{MockParams.class}));
		
		ImmutableHashMap<String, Method> methodMap = new ImmutableHashMap<String, Method>(map);
		mConfig = new BridgeConfig(new String[]{"example.com"}, methodMap);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testWebViewBridgeWebViewHandlerContext() {
		WebViewBridge bridge = new WebViewBridge(getContext());
		bridge.setDeviceBridge(new MockContext(), mConfig, new MockHandler());
		Map<String, Method> map = bridge.getBridgeConfig().getMethodMap();
		assertNotNull(map.get("notify"));
	}

	public void testGetContext() {
		WebViewBridge bridge = new WebViewBridge(getContext());
		bridge.setDeviceBridge(new MockContext(), mConfig, new Handler());
		assertNotNull(bridge.getDeviceBridge());
	}

	public void testCall() throws Exception {
		WebViewBridge bridge = new WebViewBridge(getContext());
		MockParams notify = new MockParams();
		notify.setAaa("aaa");
		notify.setBbb("bbb");
		String res = bridge.call("notify", notify, new Callback<Result>() {
			@Override
			public void succeed(WebViewBridge bridge, Result result) {				
			}
			@Override
			public void fail(WebViewBridge bridge, String code, String msg) {
			}
			
			@Override
			public void writeToParcel(Parcel dest, int flags) {	
			}
			
			@Override
			public int describeContents() {
				return 0;
			}
		});
		
		//assertEquals("javascript: WebBridge.notifyToWeb('{\"id\":\"1\",\"dest\":\"notify\",\"params\":{\"bbb\":\"bbb\",\"aaa\":\"aaa\"},\"bridge\":\"" + WebViewBridge.VERSION + "\"}');", res);
	}
	
	public void testNotifyToDevice() throws Exception {
		MockContext context = new MockContext();
		WebViewBridge bridge = new WebViewBridge(getContext());
		bridge.setDeviceBridge(context, mConfig, new MockHandler());
		bridge.getDeviceBridgeProxy().notifyToDevice("{'bridge': '" + WebViewBridge.VERSION + "', 'dest': 'notify', 'params' : {'aaa':'aaa'}}");
		assertEquals("aaa", context.params.getAaa());
	}
	
	public void testNotifyToDeviceOnException() throws Exception {
		MockContext context = new MockContext();
		WebViewBridge bridge = new WebViewBridge(getContext());
		bridge.setDeviceBridge(context, mConfig, new MockHandler());
		bridge.getDeviceBridgeProxy().notifyToDevice("{'bridge': '" + WebViewBridge.VERSION + "', 'dest': 'notfound', 'data' : {'aaa':'aaa'}}");
	}
}
