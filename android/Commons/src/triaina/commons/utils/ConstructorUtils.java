package triaina.commons.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import triaina.commons.exception.IllegalAccessRuntimeException;
import triaina.commons.exception.IllegalArgumentRuntimeException;
import triaina.commons.exception.InstantiationRuntimeException;
import triaina.commons.exception.InvocationRuntimeException;


public final class ConstructorUtils {
	private ConstructorUtils() {}
	
	public static <T> T newInstance(Constructor<T> cons, Object...args) {
		try {
			return cons.newInstance(args);
		} catch (IllegalArgumentException exp) {
			throw new IllegalArgumentRuntimeException(exp);
		} catch (InstantiationException exp) {
			throw new InstantiationRuntimeException(exp);
		} catch (IllegalAccessException exp) {
			throw new IllegalAccessRuntimeException(exp);
		} catch (InvocationTargetException exp) {
			throw new InvocationRuntimeException(exp);
		}
	}
}
