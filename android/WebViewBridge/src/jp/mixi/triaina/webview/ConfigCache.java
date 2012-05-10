package jp.mixi.triaina.webview;

import java.util.WeakHashMap;


/**
 * Note
 *  This is _NOT_ thread-safe. 
 */
public class ConfigCache {
	private WeakHashMap<Class<?>, LayoutConfig> mLayoutConfigCache =
			new WeakHashMap<Class<?>, LayoutConfig>();
	private WeakHashMap<Class<?>, BridgeConfig> mBridgeConfigCache =
			new WeakHashMap<Class<?>, BridgeConfig>();
	
	public LayoutConfig getLayoutConfig(Class<?> clazz) {
		return mLayoutConfigCache.get(clazz);
	}
	
	public void putLayoutConfig(Class<?> clazz, LayoutConfig config) {
		mLayoutConfigCache.put(clazz, config);
	}
	
	public BridgeConfig getBridgeConfig(Class<?> clazz) {
		return mBridgeConfigCache.get(clazz);
	}
	
	public void putBridgeConfig(Class<?> clazz, BridgeConfig config) {
		mBridgeConfigCache.put(clazz, config);
	}
}
