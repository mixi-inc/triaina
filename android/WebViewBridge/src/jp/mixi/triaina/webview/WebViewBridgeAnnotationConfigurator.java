package jp.mixi.triaina.webview;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import jp.mixi.triaina.commons.collection.ImmutableHashMap;
import jp.mixi.triaina.commons.exception.InvalidConfigurationRuntimeException;
import jp.mixi.triaina.commons.utils.ClassUtils;
import jp.mixi.triaina.webview.annotation.Bridge;
import jp.mixi.triaina.webview.annotation.Domain;
import jp.mixi.triaina.webview.annotation.Layout;
import jp.mixi.triaina.webview.entity.Params;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;

public class WebViewBridgeAnnotationConfigurator implements WebViewBridgeConfigurator {
	private static final String TAG = "WebViewBridgeAnnotationConfigurator";
	
	@Inject
	private ConfigCache mConfigCache;
	
	public WebViewBridge loadWebViewBridge(Activity activity) {
		Class<?> clazz = activity.getClass();
	
		LayoutConfig config = mConfigCache.getLayoutConfig(clazz);
		
		if (config == null) {
			Log.d(TAG, "Miss cache layout config");
		
			Layout layoutAnn = (Layout) clazz.getAnnotation(Layout.class);
			if (layoutAnn == null) {
				throw new InvalidConfigurationRuntimeException("Must be defined as use layout");
			}
		
			jp.mixi.triaina.webview.annotation.WebViewBridge bridgeAnn =
				(jp.mixi.triaina.webview.annotation.WebViewBridge) clazz.getAnnotation(jp.mixi.triaina.webview.annotation.WebViewBridge.class);
			if (bridgeAnn == null) {
				throw new InvalidConfigurationRuntimeException("Must be defined as use layout");
			}
			
			config = new LayoutConfig(layoutAnn.value(), bridgeAnn.value());
			mConfigCache.putLayoutConfig(clazz,  config);
		} else {
			Log.d(TAG, "Layout config from cache");
		}
		
		activity.setContentView(config.getLayoutId());
		WebViewBridge webViewBridge = (WebViewBridge)activity.findViewById(config.getWebViewBridgeId());
		
		return webViewBridge;
	}
	
	public void configureBridge(WebViewBridge webViewBridge, Object bridgeObject) {
		Class<?> clazz = bridgeObject.getClass();
		
		BridgeConfig config = mConfigCache.getBridgeConfig(clazz);
		
		if (config == null) {
			Log.d(TAG, "Miss cache bridge config");
			
			config = createBridgeConfig(bridgeObject);
			mConfigCache.putBridgeConfig(clazz, config);
		} else {
			Log.d(TAG, "Bridge config from cache");
		}

		webViewBridge.setDeviceBridge(bridgeObject, config, new Handler());
	}
	
	private BridgeConfig createBridgeConfig(Object bridgeObject) {
		Class<?> clazz = bridgeObject.getClass();
		Domain domainAnn = (Domain) clazz.getAnnotation(Domain.class);
		if (domainAnn == null)
			throw new InvalidConfigurationRuntimeException("Must be defined as access domain");

		Method[] methods = clazz.getMethods();
		Map<String, Method> map = new HashMap<String, Method>();
		
		for (Method method : methods) {
			Bridge chAnn = method.getAnnotation(Bridge.class);
			if (chAnn != null) {
				if (validDestinationMethod(method)) {
					Log.d(TAG, "dest:" + method.getName());
					map.put(chAnn.value(), method);	
				}
			}
		}
		
		ImmutableHashMap<String, Method> methodMap =
				new ImmutableHashMap<String, Method>(map);	
		return new BridgeConfig(domainAnn.value(), methodMap);
	}
	
	private boolean validDestinationMethod(Method method) {
		Class<?>[] argTypes = method.getParameterTypes();
				
		switch (argTypes.length) {
		case 2:
			if (!ClassUtils.isImplement(argTypes[1], Callback.class))
				return false;
		case 1:
			if (!ClassUtils.isImplement(argTypes[0], Params.class) &&
					!ClassUtils.isImplement(argTypes[0], Callback.class))
				return false;
		case 0:
			break;
		default:
			Log.w(TAG, "dest:" + method.getName() + " has illegal arguments");
			return false;
		}
		
		return true;
	}
	
	public void setConfigCache(ConfigCache configCache) {
		mConfigCache = configCache;
	}
}
