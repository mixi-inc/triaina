package triaina.commons.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import triaina.commons.exception.IllegalAccessRuntimeException;
import triaina.commons.exception.IllegalArgumentRuntimeException;
import triaina.commons.exception.InstantiationRuntimeException;
import triaina.commons.exception.InvocationRuntimeException;
import triaina.commons.exception.NotFoundRuntimeException;
import triaina.commons.exception.SecurityRuntimeException;


public final class ClassUtils {

    private ClassUtils() {
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException exp) {
            throw new IllegalAccessRuntimeException(exp);
        } catch (InstantiationException exp) {
            throw new InstantiationRuntimeException(exp);
        }
    }

    public static <T> T newInstance(Class<T> clazz, Object... args) {
        Class<?>[] paramTypes = toClasses(args);
        try {
            return getConstructor(clazz, paramTypes).newInstance(args);
        } catch (IllegalAccessException exp) {
            throw new IllegalAccessRuntimeException(exp);
        } catch (InstantiationException exp) {
            throw new InstantiationRuntimeException(exp);
        } catch (IllegalArgumentException exp) {
            throw new IllegalArgumentRuntimeException(exp);
        } catch (InvocationTargetException exp) {
            throw new InvocationRuntimeException(exp);
        }
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz,
            Class<?>[] paramTypes) {
        try {
            return clazz.getConstructor(paramTypes);
        } catch (SecurityException exp) {
            throw new SecurityRuntimeException(exp);
        } catch (NoSuchMethodException exp) {
            throw new NotFoundRuntimeException(exp);
        }
    }

    public static Class<?>[] toClasses(Object... args) {
        List<Class<?>> list = new ArrayList<Class<?>>(args.length);
        for (Object obj : args)
            list.add(obj.getClass());
        return list.toArray(new Class[list.size()]);
    }

    public static boolean isImplement(Class<?> clazz, Class<?> interfaceClazz) {
        if (clazz.equals(interfaceClazz))
            return true;

        Class<?>[] ifs = clazz.getInterfaces();
        for (Class<?> i : ifs) {
            if (i.equals(interfaceClazz))
                return true;
        }

        Class<?> s = clazz.getSuperclass();
        if (s == null || clazz.equals(s.getClass()))
            return false;

        return isImplement(s, interfaceClazz);
    }

    public static Method[] getMethodsByName(Class<?> clazz, String name) {
        Method[] methods = clazz.getMethods();
        List<Method> list = new ArrayList<Method>();

        for (Method method : methods) {
            if (method.getName().equals(name))
                list.add(method);
        }

        return list.toArray(new Method[list.size()]);
    }

    public static Field getFiled(Class<?> clazz, String name) {
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException exp) {
            throw new NotFoundRuntimeException(exp);
        }
    }

    public static Field getDeclaredField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException exp) {
            throw new NotFoundRuntimeException(exp);
        }
    }

    public static Type[] getGenericType(Class<?> clazz, Class<?> interfaceClazz) {
        Type st = clazz.getGenericSuperclass();
        Type[] ret = getActualTypeArguments(interfaceClazz, st);

        if (ret != null)
            return ret;

        for (Type t : clazz.getGenericInterfaces()) {
            ret = getActualTypeArguments(interfaceClazz, t);
            if (ret != null)
                return ret;
        }

        Class<?> s = clazz.getSuperclass();
        if (s == null || clazz.equals(s.getClass()))
            return new Type[0];

        return getGenericType(s, interfaceClazz);
    }

    private static Type[] getActualTypeArguments(Class<?> type, Type t) {
        if (t != null && t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) t;
            if (pt.getRawType().equals(type))
                return ((ParameterizedType) t).getActualTypeArguments();
        }
        return null;
    }

}
