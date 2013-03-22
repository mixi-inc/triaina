package triaina.webview;

import roboguice.inject.ContextSingleton;
import triaina.injector.AbstractTriainaModule;
import triaina.webview.config.ConfigCache;
import triaina.webview.config.WebViewBridgeAnnotationConfigurator;
import triaina.webview.config.WebViewBridgeConfigurator;
import com.google.inject.Singleton;

public class WebViewBridgeModule extends AbstractTriainaModule {
	@Override
	protected void configure() {
		bind(WebViewBridgeConfigurator.class).to(WebViewBridgeAnnotationConfigurator.class).in(ContextSingleton.class);
		bind(ConfigCache.class).in(Singleton.class);
		bind(InjectorHelper.class).in(ContextSingleton.class);
		bind(WebViewRestoreManager.class).in(ContextSingleton.class);
	}
}
