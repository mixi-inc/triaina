package jp.mixi.triaina.commons.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

public final class SystemUtils {

    private SystemUtils() {
    }

    public static void launchExternalBrowser(final Context context,
            final Uri uri, final Class<?> compClass) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setWebViewActivityEnabled(false);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                setWebViewActivityEnabled(true);
            }

            private void setWebViewActivityEnabled(boolean isEnabled) {
                context.getPackageManager()
                        .setComponentEnabledSetting(
                                new ComponentName(context, compClass),
                                isEnabled ? PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP);
            }
        }).start();
    }

}
