package jp.mixi.triaina.commons.utils;

import java.util.Set;

import android.os.Bundle;

public final class BundleUtils {
	private BundleUtils() {}
	
	public static String getStringByCaseInsensitive(Bundle bundle, String key) {
		Set<String> keys = bundle.keySet();
		String lower = key.toLowerCase();
		for (String k : keys) {
			if (k.toLowerCase().equals(lower))
				return bundle.getString(k);
		}
		
		return null;
	}
}
