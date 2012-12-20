package triaina.commons.test.utils;

import java.io.ByteArrayInputStream;

import triaina.commons.utils.InputStreamUtils;


import android.test.AndroidTestCase;

public class InputStreamUtilsTest extends AndroidTestCase {

	public void testToByteArray() throws Exception {
		String test = "test";
		byte[] arr = InputStreamUtils.toByteArray(new ByteArrayInputStream(test.getBytes()));
		assertEquals(test, new String(arr));
	}
}
