package jp.mixi.triaina.commons.utils;

import java.io.IOException;

import jp.mixi.triaina.commons.base64.Base64;
import jp.mixi.triaina.commons.exception.IORuntimeException;

public final class Base64Utils {
    private Base64Utils() {
    }

    public static byte[] decode(String s) {
        try {
            return Base64.decode(s);
        } catch (IOException exp) {
            throw new IORuntimeException(exp);
        }
    }

    public static String encode(byte[] b) {
        return Base64.encodeBytes(b);
    }
}
