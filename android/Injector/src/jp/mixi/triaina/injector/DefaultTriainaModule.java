package jp.mixi.triaina.injector;

import jp.mixi.triaina.injector.internal.DynamicListener;

import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

import android.app.Application;
import roboguice.config.DefaultRoboModule;
import roboguice.inject.ContextScope;
import roboguice.inject.ResourceListener;
import roboguice.inject.ViewListener;

public class DefaultTriainaModule extends DefaultRoboModule {
	private Application mApplication;
	
	public DefaultTriainaModule(Application application,
			ContextScope contextScope, ViewListener viewListener,
			ResourceListener resourceListener) {
		super(application, contextScope, viewListener, resourceListener);
		mApplication = application;
	}
	
	@Override
	protected void configure() {
		super.configure();
		bind(TriainaEnvironment.class).in(Singleton.class);
		bindListener(Matchers.any(), new DynamicListener(mApplication));		
	}
}
