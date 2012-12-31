package triaina.commons.test.utils;

import android.net.Uri;
import triaina.commons.utils.UriUtils;
import junit.framework.TestCase;

public class UriUtilsTest extends TestCase {

	public void testCompareDomain() {
		Uri uri = Uri.parse("http://aaa.example.com");
		assertEquals(true, UriUtils.compareDomain(uri, "example.com"));
		assertEquals(true, UriUtils.compareDomain(uri, "aaa.example.com"));
		
		uri = Uri.parse("http://example.com");
		assertEquals(false, UriUtils.compareDomain(uri, "aaa.example.com"));			
		assertEquals(false, UriUtils.compareDomain(uri, "exampl.com"));
		
		uri = Uri.parse("http://fake-example.com");
		assertEquals(false, UriUtils.compareDomain(uri, "example.com"));
	}

}
