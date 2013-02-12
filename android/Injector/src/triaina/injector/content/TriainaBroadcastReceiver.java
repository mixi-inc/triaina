package triaina.injector.content;

import triaina.injector.TriainaInjector;
import triaina.injector.TriainaInjectorFactory;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Injection-enabled broadcast receiver.
 * Override {@link TriainaBroadcastReceiver#handleReceive}
 * to ensure proper context scope usage.
 * @author keishin.yokomaku
 */
public abstract class TriainaBroadcastReceiver extends BroadcastReceiver {

    @Override
    public final void onReceive(Context context, Intent intent) {
        TriainaInjector injector = TriainaInjectorFactory.getInjector(context);
        try {
            injector.injectMembersWithoutViews(this);
            handleReceive(context, intent);
        } finally {
        }
    }

    protected abstract void handleReceive(Context context, Intent intent);
}
