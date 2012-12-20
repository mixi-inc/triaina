package triaina.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import triaina.commons.exception.IORuntimeException;
import triaina.commons.exception.UnsupportedRuntimeException;


public final class DigestUtils {
    public static final String SHA256 = "SHA-256";
    public static final String SHA512 = "SHA-512";
    public static final String SHA = "SHA";

    private static final int BUFFER_SIZE = 1024;

    private DigestUtils() {
    }

    public static byte[] digest(String argo, InputStream in, long offset, long len) {
        try {
            MessageDigest md = MessageDigest.getInstance(argo);
            byte[] buffer = len >= 0 ? new byte[(int) Math.min(BUFFER_SIZE, len)] : new byte[BUFFER_SIZE];
            long readLen = 0;
            in.skip(offset);

            for (;;) {
                int r = len >= 0 ? in.read(buffer, 0, (int) Math.min(BUFFER_SIZE, len - readLen)) : in.read(buffer);
                if (r < 0)
                    break;

                md.update(buffer, 0, r);

                if (len >= 0) {
                    readLen += len;
                    if (readLen >= len)
                        break;
                }
            }
            return md.digest();
        } catch (NoSuchAlgorithmException exp) {
            throw new UnsupportedRuntimeException(exp);
        } catch (IOException exp) {
            throw new IORuntimeException(exp);
        }
    }

    public static byte[] digest(String argo, InputStream in) {
        return digest(argo, in, 0, -1);
    }
}
