package triaina.commons.test.utils;

import android.net.Uri;

import junit.framework.TestCase;

import triaina.commons.utils.UriUtils;

public class UriUtilsTest extends TestCase {

    public void testCompareDomain() {
        Uri uri = Uri.parse("http://aaa.example.com");
        assertEquals(true, UriUtils.compareDomain(uri, "example.com"));
        assertEquals(true, UriUtils.compareDomain(uri, "aaa.example.com"));
        assertEquals(true, UriUtils.compareDomain(uri, "example.com:10000"));
        assertEquals(true, UriUtils.compareDomain(uri, "aaa.example.com:10000"));

        uri = Uri.parse("http://example.com");
        assertEquals(false, UriUtils.compareDomain(uri, "aaa.example.com"));
        assertEquals(false, UriUtils.compareDomain(uri, "exampl.com"));

        uri = Uri.parse("http://fake-example.com");
        assertEquals(false, UriUtils.compareDomain(uri, "example.com"));
    }

    public void testCompareDomainDomainNull() {
        Uri uri = Uri.parse("mailto:?subject=hoge");
        assertEquals(false, UriUtils.compareDomain(uri, "example.com"));
    }

}
