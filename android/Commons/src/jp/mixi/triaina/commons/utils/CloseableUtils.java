package jp.mixi.triaina.commons.utils;

import java.io.Closeable;
import java.io.IOException;

import android.util.Log;

public final class CloseableUtils {
    private static final String TAG = "jp.mixi.android.util.CloseableUtils";

    private CloseableUtils() {}

    public static void close (Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException exp) {
                Log.w(TAG, exp.getMessage());
            }
        }
    }
}
