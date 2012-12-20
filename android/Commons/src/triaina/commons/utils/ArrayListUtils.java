package triaina.commons.utils;

import java.util.ArrayList;

public final class ArrayListUtils {
	private ArrayListUtils() {}
	
	public static <T> ArrayList<T> toArrayList(T... arr) {
		ArrayList<T> list = new ArrayList<T>(arr.length);
		for (T t : arr)
			list.add(t);
		return list;
	}
}
