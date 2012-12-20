package triaina.commons.utils;

import java.security.GeneralSecurityException;
import java.security.Key;

import javax.crypto.Cipher;

import triaina.commons.exception.SecurityRuntimeException;


public final class CryptUtils {
    public static final String RSA_ECB_PKCS1_MODE = "RSA/ECB/PKCS1PADDING";

    private CryptUtils() {
    }

    public static byte[] decrypt(String mode, byte[] soruce, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(mode);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(soruce);
        } catch (GeneralSecurityException exp) {
            throw new SecurityRuntimeException(exp);
        }
    }

    public static byte[] encrypt(String mode, byte[] soruce, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(mode);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(soruce);
        } catch (GeneralSecurityException exp) {
            throw new SecurityRuntimeException(exp);
        }
    }
}
