package triaina.commons.utils;


import org.json.JSONArray;
import org.json.JSONException;

import triaina.commons.exception.JSONRuntimeException;

public final class JSONArrayUtils {
	private JSONArrayUtils() {}
	
	public static Object get(JSONArray arr, int i) {
		try {
			return arr.get(i);
		} catch (JSONException exp) {
			throw new JSONRuntimeException(exp);
		}
	}
		
	public static String[] toStringArray(JSONArray arr) {
		String[] ss = new String[arr.length()];
		for (int i = 0; i < ss.length; i++) {
			ss[i] = arr.optString(i); 
		}
		return ss;
	}
}
