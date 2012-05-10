package jp.mixi.triaina.test.webview;

import java.lang.reflect.Method;
import java.util.HashMap;

import jp.mixi.triaina.commons.collection.ImmutableHashMap;
import jp.mixi.triaina.webview.BridgeConfig;
import jp.mixi.triaina.webview.ConfigCache;
import jp.mixi.triaina.webview.LayoutConfig;
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

	public void testGetAndPutBridgeConfig() {
		ConfigCache cache = new ConfigCache();
		BridgeConfig config = cache.getBridgeConfig(getClass());
		
		assertNull(config);
		
		config = new BridgeConfig(new String[]{"example.com"}, new ImmutableHashMap<String, Method>(new HashMap<String, Method>()));
		cache.putBridgeConfig(getClass(), config);
		
		config = cache.getBridgeConfig(getClass());
		assertEquals("example.com", config.getDomains()[0]);
		assertNotNull(config);
		assertNotNull(cache.getBridgeConfig(getClass()));
	}
}
