package jp.mixi.triaina.commons.utils;

import java.lang.reflect.Array;
import java.util.Set;

public final class SetUtils {
	private SetUtils() {}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(Set<T> set, Class<?> clazz) {
		return set.toArray((T[]) Array.newInstance(clazz, set.size()));
	}
	
	public static <T> void addAll(Set<T> set, T[] arr) {
		for (T t : arr) {
			set.add(t);
		}
	}
}
