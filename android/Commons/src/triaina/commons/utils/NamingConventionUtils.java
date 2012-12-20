package triaina.commons.utils;

import android.text.TextUtils;
import android.util.Log;

public final class NamingConventionUtils {
	private static final String TAG = NamingConventionUtils.class.getCanonicalName();
	
	private NamingConventionUtils() {}
	

	public static String fromJavaFieldNameToJSONName(String name) {
		if (TextUtils.isEmpty(name)) {
			Log.w(TAG, name + " is empty or null");
			return name;
		}
		
		if (name.length() < 2) {
			return name;
		}
		
		if (name.charAt(0) == 'm') {
			name = name.substring(1);
		}
		
		return convertDelimited(name, '_');
	}
	
	public static String fromJavaClassNameToDotDelimited(String name) {
		if (TextUtils.isEmpty(name)) {
			Log.w(TAG, name + " is empty or null");
			return name;
		}
		
		return convertDelimited(name, '.');
	}
	
	/**
	 * Convert the name of an enum as given in a JSON string to its Java identifier name, as per JSON and Java conventions.
	 * (e.g. "some_enum" to "SOME_ENUM")
	 * @param enumJsonName the enum name as given in the JSON string
	 * @return the same enum converted as a proper Java identifier name
	 * @throws NullPointerException if {@link enumJsonName} is null
	 */
	public static String fromJsonNameToJavaEnumName (final String jsonName) {
		return jsonName.toUpperCase();
	}

	private static String convertDelimited(String name, char delimit) {
		
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			int c = name.charAt(i);
			if (Character.isUpperCase(c)) {
				if (i != 0) {
					builder.append(delimit);
				}
				builder.append((char)Character.toLowerCase(c));
			} else {
				builder.append((char)c);
			}
		}
		
		return builder.toString();
	}
}
