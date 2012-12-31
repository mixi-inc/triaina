package triaina.injector.service;

import triaina.injector.TriainaInjectorFactory;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class TriainaService extends Service {
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
