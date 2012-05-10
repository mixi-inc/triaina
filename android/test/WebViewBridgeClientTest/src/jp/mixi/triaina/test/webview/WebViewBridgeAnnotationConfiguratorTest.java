package jp.mixi.triaina.test.webview;

import java.lang.reflect.Method;

import jp.mixi.triaina.commons.collection.ImmutableHashMap;
import jp.mixi.triaina.commons.exception.CommonRuntimeException;
import jp.mixi.triaina.webview.R;
import jp.mixi.triaina.webview.BridgeConfig;
import jp.mixi.triaina.webview.ConfigCache;
import jp.mixi.triaina.webview.WebViewBridgeAnnotationConfigurator;
import jp.mixi.triaina.webview.annotation.Bridge;
import jp.mixi.triaina.webview.annotation.Domain;
import jp.mixi.triaina.webview.annotation.Layout;
import jp.mixi.triaina.webview.annotation.WebViewBridge;
import jp.mixi.triaina.test.mock.MockParams;
import android.app.Activity;
import android.content.Context;
import android.test.AndroidTestCase;
import android.view.View;

public class WebViewBridgeAnnotationConfiguratorTest extends AndroidTestCase {
	private WebViewBridgeAnnotationConfigurator mConfigurator;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mConfigurator = new WebViewBridgeAnnotationConfigurator();
		mConfigurator.setConfigCache(new ConfigCache());
	}
	
	public void testLoadWebViewBridge() {
		Activity activity = new MockActivity(getContext());
		jp.mixi.triaina.webview.WebViewBridge bridge = mConfigurator.loadWebViewBridge(activity);
		assertNotNull(bridge);
	}

	public void testConfigureBridge() {
		MockActivity mock = new MockActivity(getContext());
		jp.mixi.triaina.webview.WebViewBridge webViewBridge = new jp.mixi.triaina.webview.WebViewBridge(getContext());
		mConfigurator.configureBridge(webViewBridge, mock);
		
		BridgeConfig config = webViewBridge.getBridgeConfig();
		ImmutableHashMap<String, Method> map = config.getMethodMap();
		
		Method aaa = map.get("aaa");
		Method bbb = map.get("bbb");

		assertEquals("example.com", webViewBridge.getDomains()[0]);
		assertEquals(mock, webViewBridge.getDeviceBridge());
		assertEquals("aaa", aaa.getName());
		assertEquals("bbb", bbb.getName());
	}
	
	@Layout(R.layout.main)
	@WebViewBridge(R.id.webview)
	@Domain("example.com")
	static class MockActivity extends Activity {
		Context context;
		int layoutId;
		
		public MockActivity(Context context) {
			this.context = context;
		}
		
		public void setContentView(int layoutResID) {
			this.layoutId = layoutResID;
		}
		
		public View findViewById(int id) {
			if (id != R.id.webview) {
				throw new CommonRuntimeException();
			}
			return new jp.mixi.triaina.webview.WebViewBridge(context);
		}
		
		@Bridge("aaa")
		public void aaa(MockParams notify) {
		}
		
		@Bridge("bbb")
		public void bbb(MockParams notify) {
		}
	}
}
