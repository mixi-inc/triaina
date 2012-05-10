package jp.mixi.triaina.webview;

import java.lang.reflect.Method;

import jp.mixi.triaina.commons.collection.ImmutableHashMap;

public class BridgeConfig {
	private String[] mDomains;
	private ImmutableHashMap<String, Method> mMethodMap;

	public BridgeConfig(String[] domains, ImmutableHashMap<String, Method> methodMap) {
		mDomains = domains;
		mMethodMap = methodMap;
	}
	
	public ImmutableHashMap<String, Method> getMethodMap() {
		return mMethodMap;
	}
	
	public String[] getDomains() {
		return mDomains;
	}
}
