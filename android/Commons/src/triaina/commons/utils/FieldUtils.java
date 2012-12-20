package triaina.commons.utils;

import java.lang.reflect.Field;

import triaina.commons.exception.CommonRuntimeException;
import triaina.commons.exception.IllegalAccessRuntimeException;
import triaina.commons.exception.IllegalArgumentRuntimeException;

import android.util.Log;


public final class FieldUtils {
    private static final String TAG = FieldUtils.class.getCanonicalName();

    private FieldUtils() {
    }

    public static void setNoException(Object obj, Field field, Object value) {
        try {
            set(obj, field, value);
        } catch (CommonRuntimeException exp) {
            Log.w(TAG, exp.getMessage() + "", exp);
        }
    }

    public static void set(Object obj, Field field, Object value) {
        try {
            if (!field.isAccessible())
                field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalArgumentException exp) {
            throw new IllegalArgumentRuntimeException(exp);
        } catch (IllegalAccessException exp) {
            throw new IllegalAccessRuntimeException(exp);
        }
    }

    public static Object get(Object obj, Field field) {
        try {
            if (!field.isAccessible())
                field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalArgumentException exp) {
            throw new IllegalArgumentRuntimeException(exp);
        } catch (IllegalAccessException exp) {
            throw new IllegalAccessRuntimeException(exp);
        }
    }
}
