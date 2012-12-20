package triaina.commons.utils;

public final class ArrayUtils {
	
	private ArrayUtils() {}
	
	public static <T> T[] toArray(T... values) {
		return values;
	}
	
	public static Long[] convert(long[] arr) {
		Long[] ret = new Long[arr.length];
		for (int i = 0; i < arr.length; i++)
			ret[i] = arr[i];
		return ret;
	}
	
	public static long[] convert(Long[] arr) {
		long[] ret = new long[arr.length];
		for (int i = 0; i < arr.length; i++)
			ret[i] = arr[i];
		return ret;
	}
}
