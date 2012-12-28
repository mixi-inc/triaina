package triaina.injector.service;

import triaina.injector.TriainaInjectorFactory;
import android.app.IntentService;

public abstract class TriainaIntentService extends IntentService {

    public TriainaIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TriainaInjectorFactory.getInjector(this).injectMembersWithoutViews(this);
    }

    @Override
    public void onDestroy() {
        try {
            TriainaInjectorFactory.destroyInjector(this);
        } finally {
            super.onDestroy();
        }
    }
}
