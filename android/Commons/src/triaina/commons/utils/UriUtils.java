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
        //we will need only host, so we only need scheme to parse url
        Uri domainUri = Uri.parse("http://" + domain);

        String originialDomain = uri.getHost();
        String targetDomain = domainUri.getHost();
        if (originialDomain == null || targetDomain == null) return false;

        int diff = originialDomain.length() - targetDomain.length();

        if (diff < 0) {  //hosts do not match or originialDomain and targetDomain subdomains does not match
            return false;
        } else if (diff > 0) { // hosts do not match, but this might be because originialDomain might have a subdomain
            if (hostHasMatchingSubdomain(diff, originialDomain))
                return false; // original domain does not have a subdomain or subdomains does not match
        }

        return targetDomain.equals(originialDomain.substring(diff));
    }


    private static boolean hostHasMatchingSubdomain(int diff, String originalDomain) {
        return originalDomain.substring(diff - 1).charAt(0) != '.';
    }

}
