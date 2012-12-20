package triaina.commons.utils;

import android.net.Uri;

public final class UriUtils {
	private UriUtils() {}
	
	public static boolean compareDomain(Uri uri, String domain) {
		if (domain == null) {
			return false;
		}
		
		String host = uri.getHost();
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
