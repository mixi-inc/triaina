package triaina.commons.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;

public final class SystemUtils {

    private SystemUtils() {
    }

    public static void launchExternalBrowser(final Context context,
            final Uri uri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }).start();
    }

    public static int getTargetSdkVersion(Context context){
        if(context == null) return -1;

        ContextWrapper contextWrapper = new ContextWrapper(context);
        ApplicationInfo applicationInfo = contextWrapper.getApplicationInfo();
        return applicationInfo.targetSdkVersion;
    }
}
