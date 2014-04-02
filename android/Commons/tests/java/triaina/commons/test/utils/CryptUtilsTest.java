package triaina.commons.test.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import triaina.commons.utils.CryptUtils;
import junit.framework.TestCase;

public class CryptUtilsTest extends TestCase {

	public void testDecryptAndEncrypt() throws Exception {
		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
		keygen.initialize(1024);

		KeyPair keyPair = keygen.generateKeyPair();

		byte[] c = CryptUtils.encrypt(CryptUtils.RSA_ECB_PKCS1_MODE, "aaa".getBytes(), keyPair.getPrivate());
		c = CryptUtils.decrypt(CryptUtils.RSA_ECB_PKCS1_MODE, c, keyPair.getPublic());
		assertEquals("aaa", new String(c));
	}
}
