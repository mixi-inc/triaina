package triaina.commons.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import triaina.commons.exception.JSONRuntimeException;

import android.net.Uri;
import android.os.Bundle;

public final class JSONObjectUtils {
	private JSONObjectUtils() {}
	
	public static JSONObject parse(String jsonText) throws JSONRuntimeException {
		if (jsonText == null) {
			throw new JSONRuntimeException("cannot parse null");
		}
		
		try {
			return new JSONObject(jsonText);
		} catch (JSONException exp) {
			throw new JSONRuntimeException(exp);
		}
	}
	
	public static <T> void put(JSONObject json, String key, T value) {
		try {
			json.put(key, (T)value);
		} catch (JSONException exp) {
			throw new JSONRuntimeException(exp);
		}
	}
	
	public static Object get(JSONObject json, String key) {
		try {
			return json.get(key);
		} catch (JSONException exp) {
			throw new JSONRuntimeException(exp);
		}
	}
	
	public static String getString(JSONObject json, String key) {
		try {
			return json.getString(key);
		} catch (JSONException exp) {
			throw new JSONRuntimeException(exp);
		}
	}
	
	public static double getDouble(JSONObject json, String key) {
		try {
			return json.getDouble(key);
		} catch (JSONException exp) {
			throw new JSONRuntimeException(exp);
		}
	}
	
	public static int getInt(JSONObject json, String key) {
		try {
			return json.getInt(key);
		} catch (JSONException exp) {
			throw new JSONRuntimeException(exp);
		}
	}
	
	public static boolean getBoolean(JSONObject json, String key) {
		try {
			return json.getBoolean(key);
		} catch (JSONException exp) {
			throw new JSONRuntimeException(exp);
		}
	}
	
	public static JSONObject getJSONObject(JSONObject json, String key) {
		try {
			return json.getJSONObject(key);
		} catch (JSONException exp) {
			throw new JSONRuntimeException(exp);
		}
	}
	
	public static JSONArray getJSONArray(JSONObject json, String key) {
		try {
			return json.getJSONArray(key);
		} catch (JSONException exp) {
			throw new JSONRuntimeException(exp);
		}
	}
	
	public static Map<String, String> getMap(JSONObject json, String key) {
		JSONObject obj = JSONObjectUtils.getJSONObject(json, key);
		return JSONObjectUtils.toMap(obj);
	}
	
	public static Bundle getBundle(JSONObject json, String key) {
		JSONObject obj = JSONObjectUtils.getJSONObject(json, key);
		return JSONObjectUtils.toBundle(obj);
	}
	
	public static String toUriQueryParameter(JSONObject json) {
		StringBuilder builder = new StringBuilder();
		
		builder.append('?');
		for (@SuppressWarnings("unchecked")
		Iterator<String> itr = json.keys(); itr.hasNext();) {
			String key = itr.next();
			builder.append(key);
			builder.append('=');
			builder.append(Uri.encode(json.optString(key)));
			
			if (itr.hasNext()) {
				builder.append('&');
			}
		}
		
		return builder.toString();
	}
	
	public static Map<String, String> toMap(JSONObject json) {
		Map<String, String> map = new HashMap<String, String>();
		
		for (@SuppressWarnings("unchecked")
				Iterator<String> itr = json.keys(); itr.hasNext();) {
			String key = itr.next();
			map.put(key, json.optString(key));
		}
		
		return map;
	}
	
	public static Bundle toBundle(JSONObject json) {
		Bundle bundle = new Bundle();
		
		for (@SuppressWarnings("unchecked")
				Iterator<String> itr = json.keys(); itr.hasNext();) {
			String key = itr.next();
			bundle.putString(key, json.optString(key));
		}
		
		return bundle;
	}
	
	/**
	 * Compares 2 JSON objects.
	 * It compares the actual JSON data, and considers a NULL or non-specified value for a key equivalent to an absence of key (i.e. "{key:null}" is considered equivalent to "{}").
	 * Not that values are not coerced for comparison, so "{key:10}" isn't equivalent to "{key:'10'}".
	 * @param left The first JSON data to compare
	 * @param right The JSON data to compare to {@link left}
	 * @return true if the 2 JSON data are equivalent per the above description, false otherwise
	 */
	public static boolean areDataEquivalent (final JSONObject left, final JSONObject right) {
		if ((left == null) || (right == null)) {
			return (left == right);  //whether both null or not
		}
		
		//special simple case
		if (left.toString().equals(right.toString()))
			return true;
		
		//else, perform a full comparison :
		final String[] keysToCompare = new String[left.length() + right.length()];  //will contain all keys to compare
		@SuppressWarnings("unchecked")
		final Iterator<String> leftKeysIt = left.keys();
		//copy keys to a common array (O(n)) :
		int i = 0;
		for (; leftKeysIt.hasNext(); ++i)
			keysToCompare[i] = leftKeysIt.next();
		@SuppressWarnings("unchecked")
		final Iterator<String> rightKeysIt = right.keys();
		for (; rightKeysIt.hasNext(); ++i)
			keysToCompare[i] = rightKeysIt.next();
		//sort keys (O(n logn)) :
		Arrays.sort(keysToCompare);
		//compare left and right for each key (O(n)) :
		for (i = 0; i < keysToCompare.length; ++i) {
			if ((i > 0) && (keysToCompare[i].equals(keysToCompare[i-1])))
				//already checked this key
				continue;
			
			final String key = keysToCompare[i];
			Object leftObj;
			try {
				leftObj = (left.has(key)) ? left.get(key) : JSONObject.NULL;
			} catch (final JSONException jE) {
				throw new JSONRuntimeException(jE);  //shouldn't happen as we check for has() before calling get()
			}
			Object rightObj;
			try {
				rightObj = (right.has(key)) ? right.get(key) : JSONObject.NULL;
			} catch (final JSONException jE) {
				throw new JSONRuntimeException(jE);  //shouldn't happen as we check for has() before calling get()
			}
			
			//compare sub-objects :
			if ((leftObj instanceof JSONObject) && (rightObj instanceof JSONObject)) {
				if (!(areDataEquivalent((JSONObject)leftObj, (JSONObject)rightObj)))
					return false;
			} else {
				if (!leftObj.equals(rightObj))
					return false;  //they're not equivalent
			}
			//Android's JSONArray overrides equals() so no need for special case for it
		}
		
		//didn't return false yet, objects are equal :
		return true;
	}
}
