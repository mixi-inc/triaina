package triaina.commons.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public final class BitmapUtils {
    private BitmapUtils() {}

    public static final int DEFAULT_QUALITY = 80;
    public static final CompressFormat DEFAULT_FORMAT = CompressFormat.JPEG;

    public static byte[] toByteArray (Bitmap bitmap, CompressFormat format, int quality) {
        ByteArrayOutputStream out = null;

        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, quality, out);
            return out.toByteArray();
        } finally {
            CloseableUtils.close(out);
        }
    }

    public static byte[] toByteArray (Bitmap bitmap) {
        return toByteArray(bitmap, DEFAULT_FORMAT, DEFAULT_QUALITY);
    }

    public static void recycle (Bitmap bitmap) {
        if (bitmap != null)
            bitmap.recycle();
    }
}
