package jp.mixi.triaina.injector.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import jp.mixi.triaina.injector.TriainaInjectorFactory;
import jp.mixi.triaina.injector.annotation.DynamicInject;

import android.app.Application;

import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class DynamicListener implements TypeListener {
	private Application mApplication;
	private Injector mInjector;
	
	public DynamicListener(Application application) {
		mApplication = application;
	}
	
	public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
		for(Class<?> c = typeLiteral.getRawType(); c != Object.class; c = c.getSuperclass()) {
			for (Field field : c.getDeclaredFields()) {
				if (field.isAnnotationPresent(DynamicInject.class)) {
					if(Modifier.isStatic(field.getModifiers()))
        				throw new UnsupportedOperationException("Dynamic instance may not be statically injected");
        			else {
        				if (mInjector == null) {
        					synchronized (this) {
        						if (mInjector == null)
        							mInjector = TriainaInjectorFactory.getBaseApplicationInjector(mApplication);
        					}
        				}
        				
        				typeEncounter.register(new DynamicMembersInjector<I>(field, mInjector));
        			}
        		}
            }
        }
    }
}
