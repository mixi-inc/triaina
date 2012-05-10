package jp.mixi.triaina.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jp.mixi.triaina.commons.exception.IORuntimeException;

public final class ZipUtils {
    public static final byte[] MAGIC_ENDSIG = new byte[] { 0x50, 0x4b, 0x05, 0x06 };

    private ZipUtils() {
    }

    /**
     * read comment whole of zip file
     * 
     * @param path
     * @return comment
     */
    public static String readComment(String path) {
        FileInputStream in = null;

        try {
            File file = new File(path);
            int fileLen = (int) file.length();
            byte[] buffer = new byte[Math.min(fileLen, 8192)];
            int len;

            in = new FileInputStream(file);
            in.skip(fileLen - buffer.length);

            String comment = null;
            if ((len = in.read(buffer)) > 0)
                comment = readComment(buffer, len);

            return comment;
        } catch (IOException exp) {
            throw new IORuntimeException(exp);
        } finally {
            CloseableUtils.close(in);
        }
    }

    private static String readComment(byte[] buffer, int len) {
        int buffLen = Math.min(buffer.length, len);
        int endSig = endSignatureIndex(buffer, buffLen);
        if (endSig < 0)
            return null;

        int commentLen = buffer[endSig + 20] + buffer[endSig + 21] * 256;
        int realLen = buffLen - endSig - 22;

        String comment = new String(buffer, endSig + 22, Math.min(commentLen, realLen));
        return comment;
    }

    /**
     * seek index of end signature
     * 
     * @param path
     * @return
     */
    public static int endSignatureIndex(String path) {
        FileInputStream in = null;

        try {
            File file = new File(path);
            int fileLen = (int) file.length();
            byte[] buffer = new byte[Math.min(fileLen, 8192)];
            int len;

            in = new FileInputStream(file);
            int skip = fileLen - buffer.length;
            in.skip(skip);

            if ((len = in.read(buffer)) < 1)
                return -1;

            return endSignatureIndex(buffer, len) + skip;
        } catch (IOException exp) {
            throw new IORuntimeException(exp);
        } finally {
            CloseableUtils.close(in);
        }

    }

    private static int endSignatureIndex(byte[] buffer, int len) {
        for (int i = len - MAGIC_ENDSIG.length - 21; i >= 0; i--) {
            int k = 0;
            for (; k < MAGIC_ENDSIG.length; k++) {
                if (buffer[i + k] != MAGIC_ENDSIG[k])
                    break;
            }

            if (k == MAGIC_ENDSIG.length)
                return i;
        }

        return -1;
    }
}
