package jp.mixi.triaina.webview;

import roboguice.inject.ContextSingleton;
import jp.mixi.triaina.injector.AbstractTriainaModule;
import jp.mixi.triaina.webview.config.ConfigCache;
import jp.mixi.triaina.webview.config.WebViewBridgeAnnotationConfigurator;
import jp.mixi.triaina.webview.config.WebViewBridgeConfigurator;
import com.google.inject.Singleton;

public class WebViewBridgeModule extends AbstractTriainaModule {

	@Override
	protected void configure() {
		bind(WebViewBridgeConfigurator.class).to(WebViewBridgeAnnotationConfigurator.class).in(ContextSingleton.class);
		bind(ConfigCache.class).in(Singleton.class);
		bind(InjectorHelper.class).in(ContextSingleton.class);
	}
}
