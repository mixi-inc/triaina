package triaina.injector.content;

import triaina.injector.TriainaInjectorFactory;
import android.content.ContentProvider;

public abstract class TriainaContentProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        TriainaInjectorFactory.getInjector(getContext()).injectMembersWithoutViews(this);
        return true;
    }
}
