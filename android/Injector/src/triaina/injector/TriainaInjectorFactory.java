package triaina.injector;

import java.util.ArrayList;
import java.util.WeakHashMap;

import triaina.commons.exception.CommonRuntimeException;
import triaina.injector.internal.TriainaInjectorImpl;

import roboguice.event.EventManager;
import roboguice.inject.ContextScope;
import roboguice.inject.ContextScopedRoboInjector;
import roboguice.inject.ResourceListener;
import roboguice.inject.ViewListener;

import android.app.Application;
import android.content.Context;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.spi.DefaultElementVisitor;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;
import com.google.inject.spi.StaticInjectionRequest;

public class TriainaInjectorFactory {
	public static final Stage DEFAULT_STAGE = Stage.PRODUCTION;
	
	protected static WeakHashMap<Application,Injector> sInjectors = new WeakHashMap<Application,Injector>();
	protected static WeakHashMap<Application,ResourceListener> sResourceListeners = new WeakHashMap<Application, ResourceListener>();
	protected static WeakHashMap<Application,ViewListener> sViewListeners = new WeakHashMap<Application, ViewListener>();

	private TriainaInjectorFactory() {}

	public static Injector getBaseApplicationInjector(Application application) {
		Injector injector = sInjectors.get(application);
		if(injector != null )
			return injector;

		synchronized (TriainaInjectorFactory.class) {
            injector = sInjectors.get(application);
            if(injector != null)
            	return injector;
            
            return setBaseApplicationInjector(application, DEFAULT_STAGE);
        }
    }

	public static Injector setBaseApplicationInjector(final Application application, Stage stage, Module... modules) {
		for(Element element : Elements.getElements(modules)) {
			element.acceptVisitor(new DefaultElementVisitor<Void>() {
				@Override
				public Void visit(StaticInjectionRequest element) {
					getResourceListener(application).requestStaticInjection(element.getType());
					return null;
				}
			});
		}

        synchronized (TriainaInjectorFactory.class) {
            final Injector injector = Guice.createInjector(stage, modules);
            sInjectors.put(application, injector);
            return injector;
        }
    }

    /**
     * Return the cached Injector instance for this application, or create a new one if necessary.
     */
    public static Injector setBaseApplicationInjector(Application application, Stage stage) {
    	synchronized (TriainaInjectorFactory.class) {
    		final int id = application.getResources().getIdentifier("triaina_modules", "array", application.getPackageName());
    		final String[] moduleNames = id > 0 ? application.getResources().getStringArray(id) : new String[]{};
    		final ArrayList<Module> modules = new ArrayList<Module>();
    		final DefaultTriainaModule defaultRoboModule = newDefaultRoboModule(application);

    		modules.add(defaultRoboModule);

    		try {
    			for (String name : moduleNames) {
                    final Class<? extends Module> clazz = Class.forName(name).asSubclass(Module.class);
                    try {
                        modules.add(clazz.getDeclaredConstructor(Context.class).newInstance(application));
                    } catch(final NoSuchMethodException noActivityConstructorException) {
                        modules.add(clazz.newInstance());
                    }
    			}
    		} catch (Exception exp) {
    			throw new CommonRuntimeException(exp);
    		}

    		final Injector injector = setBaseApplicationInjector(application, stage, modules.toArray(new Module[modules.size()]));
    		sInjectors.put(application, injector);
    		return injector;
        }
    }


    public static TriainaInjector getInjector(Context context) {
        final Application application = (Application)context.getApplicationContext();
        return new TriainaInjectorImpl(new ContextScopedRoboInjector(context, getBaseApplicationInjector(application), getViewListener(application)));
    }

    public static <T> T injectMembers(Context context, T t) {
        getInjector(context).injectMembers(t);
        return t;
    }
    
    public static DefaultTriainaModule newDefaultRoboModule(final Application application) {
        return new DefaultTriainaModule(application, new ContextScope(), getViewListener(application), getResourceListener(application));
    }

    protected static ResourceListener getResourceListener(Application application ) {
    	ResourceListener resourceListener = sResourceListeners.get(application);
        if(resourceListener == null) {
            synchronized (TriainaInjectorFactory.class) {
                if(resourceListener == null) {
                    resourceListener = new ResourceListener(application);
                    sResourceListeners.put(application,resourceListener);
                }
            }
        }
        return resourceListener;
    }

    protected static ViewListener getViewListener(final Application application) {
        ViewListener viewListener = sViewListeners.get(application);
        if(viewListener == null) {
            synchronized (TriainaInjectorFactory.class) {
                if(viewListener==null) {
                    viewListener = new ViewListener();
                    sViewListeners.put(application,viewListener);
                }
            }
        }
        
        return viewListener;
    }

    public static void destroyInjector(Context context) {
        final TriainaInjector injector = getInjector(context);
        injector.getInstance(EventManager.class).destroy();
        injector.getInstance(ContextScope.class).destroy(context);
        sInjectors.remove(context);
    }
}
