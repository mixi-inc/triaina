
package triaina.commons.utils;

import android.net.Uri;

public final class UriUtils {
    private UriUtils() {
    }

    public static boolean compareDomain(Uri uri, String domain) {
        if (domain == null) {
            return false;
        }

        //in case domain string contains port values we need to make sure we are getting right value
        // we will need only host, so we only need scheme to parse url
        Uri domainUri = Uri.parse("http://" + domain);

        String host = uri.getHost();
        domain = domainUri.getHost();

        int d = host.length() - domain.length();
        if (d < 0) {
            return false;
        } else if (d > 0) {
            if (host.substring(d - 1).charAt(0) != '.')
                return false;
        }

        return domain.equals(host.substring(d));
    }
}
