package triaina.commons.test.utils;

import java.util.ArrayList;

import triaina.commons.utils.ArrayListUtils;
import junit.framework.TestCase;

public class ArrayListUtilsTest extends TestCase {

	public void testToArrayList() {
		ArrayList<String> list = ArrayListUtils.toArrayList("AAA", "BBB");
		assertEquals("AAA", list.get(0));
		assertEquals("BBB", list.get(1));
	}
}
