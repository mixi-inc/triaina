package triaina.test.webview;

import triaina.commons.exception.CommonRuntimeException;
import triaina.webview.InjectorHelper;
import triaina.webview.R;
import triaina.webview.annotation.Bridge;
import triaina.webview.annotation.Domain;
import triaina.webview.annotation.Layout;
import triaina.webview.annotation.WebViewBridgeResouce;
import triaina.webview.config.BridgeMethodConfig;
import triaina.webview.config.BridgeObjectConfig;
import triaina.webview.config.ConfigCache;
import triaina.webview.config.WebViewBridgeAnnotationConfigurator;
import triaina.test.mock.MockParams;
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
		triaina.webview.WebViewBridge bridge = mConfigurator.loadWebViewBridge(activity);
		assertNotNull(bridge);
		assertEquals(1, bridge.getDomainConfig().getDomains().length);
		assertEquals("example.com", bridge.getDomainConfig().getDomains()[0]);
	}

	public void testRegisterBridge() {
		MockActivity mock = new MockActivity(getContext());
		triaina.webview.WebViewBridge webViewBridge = new triaina.webview.WebViewBridge(getContext());
		mConfigurator.registerBridge(webViewBridge, mock);

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

	@WebViewBridgeResouce(R.id.webview)
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
			return new triaina.webview.WebViewBridge(context);
		}

		@Bridge("aaa")
		public void aaa(MockParams notify) {
		}

		@Bridge("bbb")
		public void bbb(MockParams notify) {
		}
	}
}
