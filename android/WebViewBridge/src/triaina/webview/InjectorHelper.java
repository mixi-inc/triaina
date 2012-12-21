package triaina.webview;

import triaina.injector.TriainaInjectorFactory;

import android.content.Context;

public class InjectorHelper {
    public <T> T inject(Context context, T obj) {
        TriainaInjectorFactory.getInjector(context).injectMembers(obj);
        return obj;
    }
}
