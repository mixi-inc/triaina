package triaina.commons.utils;

import android.database.Cursor;

public final class CursorUtils {
    private CursorUtils() {}
    
    public static void close(Cursor c) {
        if (c != null)
            c.close();
    }
}
