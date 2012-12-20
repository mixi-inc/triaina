package triaina.commons.utils;

import triaina.commons.exception.NumberFormatRuntimeException;

public final class FloatUtils {
	private FloatUtils() {}
	
	public static float parse(String s) {
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException exp) {
			throw new NumberFormatRuntimeException(exp);
		}
	}
}
