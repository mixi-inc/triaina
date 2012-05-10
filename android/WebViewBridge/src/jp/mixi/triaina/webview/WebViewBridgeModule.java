package jp.mixi.triaina.webview;

import roboguice.inject.ContextSingleton;
import jp.mixi.triaina.injector.AbstractTriainaModule;
import jp.mixi.triaina.webview.logic.SensorAccelerometerLogic;
import jp.mixi.triaina.webview.logic.VibratorLogic;
import jp.mixi.triaina.webview.logic.WebStatusLogic;
import jp.mixi.triaina.webview.logic.WiFiLogic;
import jp.mixi.triaina.webview.logic.WiFiP2PLogic;

import android.os.Build;

import com.google.inject.Singleton;

public class WebViewBridgeModule extends AbstractTriainaModule {

	@Override
	protected void configure() {
		bind(WebViewBridgeConfigurator.class).to(WebViewBridgeAnnotationConfigurator.class).in(ContextSingleton.class);
		bind(ConfigCache.class).in(Singleton.class);
		bind(WebStatusLogic.class).in(ContextSingleton.class);
		bind(SensorAccelerometerLogic.class).in(ContextSingleton.class);
		bind(VibratorLogic.class).in(ContextSingleton.class);
		bind(WiFiLogic.class).in(ContextSingleton.class);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			bind(WiFiP2PLogic.class).in(ContextSingleton.class);
	}
}
