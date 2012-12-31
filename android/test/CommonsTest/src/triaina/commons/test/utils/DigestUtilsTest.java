package triaina.commons.test.utils;

import java.io.ByteArrayInputStream;

import triaina.commons.utils.DigestUtils;
import junit.framework.TestCase;

public class DigestUtilsTest extends TestCase {
	
	public void testDigestStringInputStream() {
		byte[] digest = DigestUtils.digest(DigestUtils.SHA256, new ByteArrayInputStream("aaa".getBytes()));
		assertEquals(32, digest.length);
		
		StringBuilder builder = new StringBuilder();
		for (byte b : digest) {
	        builder.append(String.format("%02x", b));
        }
		assertEquals("9834876dcfb05cb167a5c24953eba58c4ac89b1adf57f28f2f9d09af107ee8f0", builder.toString());
	}
}
