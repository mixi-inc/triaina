package triaina.commons.test.utils;

import java.util.HashSet;
import java.util.Set;

import triaina.commons.utils.SetUtils;
import junit.framework.TestCase;

public class SetUtilsTest extends TestCase {

	public void testToArrayAndAddAll() {
		Set<String> set = new HashSet<String>();
		set.add("a");
		set.add("b");
		set.add("c");
		
		String[] arr = SetUtils.toArray(set, String.class);
		assertEquals(3, arr.length);
	}

	public void testAddAll() {
		String[] arr = new String[]{"a", "b", "c"};
		Set<String> set = new HashSet<String>();
		SetUtils.addAll(set, arr);
		assertEquals(3, set.size());
	}
}
