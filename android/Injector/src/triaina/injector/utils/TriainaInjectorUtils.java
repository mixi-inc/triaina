package triaina.injector.utils;

import triaina.injector.TriainaInjector;
import triaina.injector.TriainaInjectorFactory;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

public final class TriainaInjectorUtils {
    private TriainaInjectorUtils() {
    }

    public static void injectMembersWithoutViews(Context context, Object obj) {
        TriainaInjector injector = TriainaInjectorFactory.getInjector(context);
        injector.injectMembersWithoutViews(obj);
    }

    public static void injectViewMembers(Context context, Activity activity) {
        TriainaInjector injector = TriainaInjectorFactory.getInjector(context);
        injector.injectViewMembers(activity);
    }

    public static void injectViewMembers(Context context, Fragment fragment) {
        TriainaInjector injector = TriainaInjectorFactory.getInjector(context);
        injector.injectViewMembers(fragment);
    }

    public static void injectMembers(Context context, Object obj) {
        TriainaInjector injector = TriainaInjectorFactory.getInjector(context);
        injector.injectMembers(obj);
    }
    
    public static void destroyInjector(Context context) {
        TriainaInjectorFactory.destroyInjector(context);
    }
}
