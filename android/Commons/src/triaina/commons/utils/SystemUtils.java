package triaina.commons.utils;

import android.content.Context;
import android.content.Intent;
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

}
