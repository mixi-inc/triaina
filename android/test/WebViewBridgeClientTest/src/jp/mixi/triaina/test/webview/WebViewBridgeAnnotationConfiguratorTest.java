package jp.mixi.triaina.test.webview;

import jp.mixi.triaina.commons.exception.CommonRuntimeException;
import jp.mixi.triaina.webview.InjectorHelper;
import jp.mixi.triaina.webview.R;
import jp.mixi.triaina.webview.annotation.Bridge;
import jp.mixi.triaina.webview.annotation.Domain;
import jp.mixi.triaina.webview.annotation.Layout;
import jp.mixi.triaina.webview.annotation.WebViewBridge;
import jp.mixi.triaina.webview.config.BridgeMethodConfig;
import jp.mixi.triaina.webview.config.BridgeObjectConfig;
import jp.mixi.triaina.webview.config.ConfigCache;
import jp.mixi.triaina.webview.config.WebViewBridgeAnnotationConfigurator;
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
		mConfigurator.setContext(getContext());
		mConfigurator.setInjectorHelper(new MockInjectorHenler());
	}
	
	public void testLoadWebViewBridge() {
		Activity activity = new MockActivity(getContext());
		jp.mixi.triaina.webview.WebViewBridge bridge = mConfigurator.loadWebViewBridge(activity);
		assertNotNull(bridge);
		assertEquals(1, bridge.getDomainConfig().getDomains().length);
		assertEquals("example.com", bridge.getDomainConfig().getDomains()[0]);
	}

	public void testConfigureBridge() {
		MockActivity mock = new MockActivity(getContext());
		jp.mixi.triaina.webview.WebViewBridge webViewBridge = new jp.mixi.triaina.webview.WebViewBridge(getContext());
		mConfigurator.configure(webViewBridge, mock);
		
		BridgeObjectConfig objectConfig = webViewBridge.getBridgeConfigSet();
		BridgeMethodConfig methodConfig = objectConfig.get("aaa");
		assertEquals("aaa", methodConfig.getMethod().getName());
		
		methodConfig = objectConfig.get("bbb");
		assertEquals("bbb", methodConfig.getMethod().getName());
	}
	
	static class MockInjectorHenler extends InjectorHelper {
        @Override
        public <T> T inject(Context context, T obj) {
            return obj;
        }
	}
	
	@WebViewBridge(R.id.webview)
	@Layout(R.layout.main)
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
