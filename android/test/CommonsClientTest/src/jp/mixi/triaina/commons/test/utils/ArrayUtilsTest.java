package jp.mixi.triaina.commons.test.utils;

import jp.mixi.triaina.commons.utils.ArrayUtils;
import junit.framework.TestCase;

public class ArrayUtilsTest extends TestCase {

	public void testToArray() {
		String[] arr = ArrayUtils.toArray("a", "b");
		assertEquals(2, arr.length);
		assertEquals("a", arr[0]);
		assertEquals("b", arr[1]);
	}
	
	public void convertToLongObjectArray() throws Exception {
		long[] arr = ArrayUtils.convert(new Long[]{1L, 2L});
		assertEquals(2, arr.length);
		assertEquals(1, arr[0]);
		assertEquals(2, arr[1]);
	}
	
	public void convertToLongArray() throws Exception {
		Long[] arr = ArrayUtils.convert(new long[]{1L, 2L});
		assertEquals(2, arr.length);
		assertEquals(new Long(1), arr[0]);
		assertEquals(new Long(2), arr[1]);
	}
}
