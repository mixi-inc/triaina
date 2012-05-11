package jp.mixi.triaina.test.webview;

import jp.mixi.triaina.webview.config.BridgeMethodConfig;
import jp.mixi.triaina.webview.config.BridgeObjectConfig;
import jp.mixi.triaina.webview.config.ConfigCache;
import jp.mixi.triaina.webview.config.LayoutConfig;
import junit.framework.TestCase;

public class ConfigCacheTest extends TestCase {
	public void testGetAndPutLayoutConfig() {
		ConfigCache cache = new ConfigCache();
		LayoutConfig config = cache.getLayoutConfig(getClass());
		
		assertNull(config);
		
		config = new LayoutConfig(1, 2);
		cache.putLayoutConfig(getClass(), config);
		
		config = cache.getLayoutConfig(getClass());
		assertNotNull(config);
	}

	public void testGetAndPutBridgeConfigSet() {
		ConfigCache cache = new ConfigCache();
		BridgeObjectConfig config = cache.getBridgeObjectConfig(getClass());
		
		assertNull(config);
		
		config = new BridgeObjectConfig();
		config.add(new BridgeMethodConfig(null, null));
		
		cache.putBridgeObjectConfig(getClass(), config);
		BridgeObjectConfig newObjectConfig = cache.getBridgeObjectConfig(getClass());
		
		assertEquals(config, newObjectConfig);
	}
}
