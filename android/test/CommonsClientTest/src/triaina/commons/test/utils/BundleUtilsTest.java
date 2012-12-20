package triaina.commons.test.utils;

import android.os.Bundle;
import triaina.commons.utils.BundleUtils;
import junit.framework.TestCase;

public class BundleUtilsTest extends TestCase {

	public void testGetStringByCaseInsensitive() {
		Bundle bundle = new Bundle();
		bundle.putString("AAA", "BBB");
		assertEquals("BBB", BundleUtils.getStringByCaseInsensitive(bundle, "aaa"));
		assertEquals("BBB", BundleUtils.getStringByCaseInsensitive(bundle, "AAA"));
	}
}
