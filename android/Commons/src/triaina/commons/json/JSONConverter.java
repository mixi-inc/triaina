package triaina.commons.json;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import triaina.commons.collection.ImmutableHashMap;
import triaina.commons.exception.CommonRuntimeException;
import triaina.commons.exception.JSONConvertException;
import triaina.commons.exception.JSONRuntimeException;
import triaina.commons.json.annotation.Exclude;
import triaina.commons.utils.FieldUtils;
import triaina.commons.utils.JSONArrayUtils;
import triaina.commons.utils.JSONObjectUtils;
import triaina.commons.utils.NamingConventionUtils;

import android.os.Bundle;
import android.util.Log;

/**
 *  - Note
 *    The field convert name convention example is following. 
 *      mSomeField <-> some_field
 *      someField  <-> some_field
 *      some_field <-> some_field
 *      
 * @author hnakagawa
 * @author Guillaume Legrand
 *
 */
public final class JSONConverter {
	private static final String TAG = JSONConverter.class.getCanonicalName();
	
	private JSONConverter() {}
	
	private static ConcurrentMap<Class<?>, ImmutableHashMap<String, Field>> mBindCache =
				new ConcurrentHashMap<Class<?>, ImmutableHashMap<String,Field>>();
	
	private static ConcurrentMap<Class<?>, ImmutableHashMap<Field, String>> mPropCache =
			new ConcurrentHashMap<Class<?>, ImmutableHashMap<Field, String>>();

	/**
	 * Class to convert JSON data to or from Java "primitive" types (including Java primitive boxing types - e.g. Integer - and Strings).
	 * It basically converts everything that doesn't translate into a JSONObject or JSONArray or NULL.
	 * 
	 * This is not really necessary for types supported by the JSON library (String, Integer, Double...) especially thanks to autoboxing,
	 * however without this class other types like float wouldn't be supported as we can't cast unrelated classes like the different numerical types.
	 */
	private static abstract class PrimitiveConverter <T> {
		public    abstract T      retrieve (JSONObject json, String key)   throws JSONException;
		public    abstract T      retrieve (JSONArray  json, int    index) throws JSONException;
		/**
		 * Converts a T to an Object of a type which can be put() in a JSON object or array.
		 */
		protected Object convertToPutableObject (T value) {
			return value;  //default, override for types non-supported by JSON classes
		}

		public void put (final JSONObject json, final String key, final T value) throws JSONException {
			json.put(key, convertToPutableObject(value));
		}
		public void put (final JSONArray  json, final int index, final T value) throws JSONException {
			json.put(index, convertToPutableObject(value));
		}
		public void put (final JSONArray  json, final T value) throws JSONException {
			json.put(convertToPutableObject(value));
		}
	}

	private static final ImmutableHashMap<Class<?>, PrimitiveConverter<?>> PRIMITIVE_CONVERTERS;
	static {
		HashMap<Class<?>, PrimitiveConverter<?>> map = new HashMap<Class<?>, PrimitiveConverter<?>>();
		final PrimitiveConverter<String> jsonStringRetriever = new PrimitiveConverter<String> () {
			@Override
			public String retrieve (final JSONObject json, final String key) throws JSONException { return json.getString(key); }
			@Override
			public String retrieve(final JSONArray json, final int index) throws JSONException { return json.getString(index); }
		};
		final PrimitiveConverter<Boolean> jsonBooleanRetriever = new PrimitiveConverter<Boolean> () {
			@Override
			public Boolean retrieve (final JSONObject json, final String key) throws JSONException { return json.getBoolean(key); }
			@Override
			public Boolean retrieve(final JSONArray json, final int index) throws JSONException { return json.getBoolean(index); }
		};
		final PrimitiveConverter<Integer> jsonIntegerRetriever = new PrimitiveConverter<Integer> () {
			@Override
			public Integer retrieve (final JSONObject json, final String key) throws JSONException { return json.getInt(key); }
			@Override
			public Integer retrieve(final JSONArray json, final int index) throws JSONException { return json.getInt(index); }
		};
		final PrimitiveConverter<Long> jsonLongRetriever = new PrimitiveConverter<Long> () {
			@Override
			public Long retrieve (final JSONObject json, final String key) throws JSONException { return json.getLong(key); }
			@Override
			public Long retrieve(final JSONArray json, final int index) throws JSONException { return json.getLong(index); }
		};
		final PrimitiveConverter<Double> jsonDoubleRetriever = new PrimitiveConverter<Double> () {
			@Override
			public Double retrieve (final JSONObject json, final String key) throws JSONException { return json.getDouble(key); }
			@Override
			public Double retrieve(final JSONArray json, final int index) throws JSONException { return json.getDouble(index); }
		};
		final PrimitiveConverter<Float> jsonFloatRetriever = new PrimitiveConverter<Float> () {
			@Override
			public Float retrieve (final JSONObject json, final String key) throws JSONException { return ((Double)json.getDouble(key)).floatValue(); }
			@Override
			public Float retrieve(final JSONArray json, final int index) throws JSONException { return ((Double)json.getDouble(index)).floatValue(); }
			@Override
			protected Double convertToPutableObject (final Float value) { return value.doubleValue(); }
		};
		final PrimitiveConverter<Short> jsonShortRetriever = new PrimitiveConverter<Short> () {
			@Override
			public Short retrieve (final JSONObject json, final String key) throws JSONException { return ((Integer)json.getInt(key)).shortValue(); }
			@Override
			public Short retrieve (final JSONArray json, final int index) throws JSONException { return ((Integer)json.getInt(index)).shortValue(); }
			@Override
			protected Integer convertToPutableObject (final Short value) { return value.intValue(); }
		};
		final PrimitiveConverter<Byte> jsonByteRetriever = new PrimitiveConverter<Byte> () {
			@Override
			public Byte retrieve (final JSONObject json, final String key) throws JSONException { return ((Integer)json.getInt(key)).byteValue(); }
			@Override
			public Byte retrieve (final JSONArray json, final int index) throws JSONException { return ((Integer)json.getInt(index)).byteValue(); }
			@Override
			protected Integer convertToPutableObject (final Byte value) { return value.intValue(); }
		};
		final PrimitiveConverter<Character> jsonCharRetriever = new PrimitiveConverter<Character> () {
			@Override
			public Character retrieve (final JSONObject json, final String key) throws JSONException { return (json.getString(key)).charAt(0); }
			@Override
			public Character retrieve (final JSONArray json, final int index) throws JSONException { return (json.getString(index)).charAt(0); }
			@Override
			protected String convertToPutableObject (final Character value) { return value.toString(); }
		};

		map.put(String.class   , jsonStringRetriever);
		map.put(Boolean.class  , jsonBooleanRetriever);
		map.put(boolean.class  , jsonBooleanRetriever);
		map.put(Integer.class  , jsonIntegerRetriever);
		map.put(int.class      , jsonIntegerRetriever);
		map.put(Long.class     , jsonLongRetriever);
		map.put(long.class     , jsonLongRetriever);
		map.put(Double.class   , jsonDoubleRetriever);
		map.put(double.class   , jsonDoubleRetriever);
		map.put(Float.class    , jsonFloatRetriever);
		map.put(float.class    , jsonFloatRetriever);
		map.put(Short.class    , jsonShortRetriever);
		map.put(short.class    , jsonShortRetriever);
		map.put(Byte.class     , jsonByteRetriever);
		map.put(byte.class     , jsonByteRetriever);
		map.put(Character.class, jsonCharRetriever);
		map.put(char.class     , jsonCharRetriever);

		PRIMITIVE_CONVERTERS = new ImmutableHashMap<Class<?>, PrimitiveConverter<?>>(map);
	}

	private static PrimitiveConverter<?> getPrimitiveConverter (final Class<?> typeToConvertTo) {
		return PRIMITIVE_CONVERTERS.get(typeToConvertTo);
	}

	private static boolean isPrimitivelyConvertible (final Class<?> type) {
		return PRIMITIVE_CONVERTERS.containsKey(type);
	}
	
	@SuppressWarnings("unchecked")
	private static Object retrieve (final Class<?> type, final JSONObject json, final String key, final Object enclosingObject) throws JSONConvertException {
		if (json.isNull(key))
			return null;

		try {
			if (isPrimitivelyConvertible(type)) {
				return getPrimitiveConverter(type).retrieve(json, key);
			} else if (type.isArray()) {
				return toObjectArray(json.getJSONArray(key), type.getComponentType(), enclosingObject);
            } else if (type.isEnum()) {
            	@SuppressWarnings("rawtypes")
            	final Class<? extends Enum> enumType = type.asSubclass(Enum.class);
            	final String enumValue = NamingConventionUtils.fromJsonNameToJavaEnumName(json.getString(key));
            	try {
            		return Enum.valueOf(enumType, enumValue);
            	} catch (final IllegalArgumentException exp) {
            		throw new JSONConvertException(exp);
            	}
 			} else if (Bundle.class.equals(type)) {
				final Bundle bundle = new Bundle();
				bind(json.getJSONObject(key), bundle);
				return bundle;
			} else {
				return toObject(json.getJSONObject(key), type, enclosingObject);
			}
		}catch (final JSONException exp) {
			throw new JSONConvertException(exp);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Object retrieve (final Class<?> type, final JSONArray json, final int index, final Object enclosingObject) throws JSONConvertException {
		if (json.isNull(index))
			return null;

		try {
			if (isPrimitivelyConvertible(type)) {
				return getPrimitiveConverter(type).retrieve(json, index);
			} else if (type.isArray()) {
				return toObjectArray(json.getJSONArray(index), type.getComponentType(), enclosingObject);
            } else if (type.isEnum()) {
            	@SuppressWarnings("rawtypes")
            	final Class<? extends Enum> enumType = type.asSubclass(Enum.class);
            	final String enumValue = NamingConventionUtils.fromJsonNameToJavaEnumName(json.getString(index));
            	try {
            		return Enum.valueOf(enumType, enumValue);
            	} catch (final IllegalArgumentException exp) {
            		throw new JSONConvertException(exp);
            	}
			} else if (Bundle.class.equals(type)) {
				final Bundle bundle = new Bundle();
				bind(json.getJSONObject(index), bundle);
				return bundle;
			} else {
				return toObject(json.getJSONObject(index), type, enclosingObject);
			}
		}catch (final JSONException exp) {
			throw new JSONConvertException(exp);
		}
	}

	@SuppressWarnings("unchecked")  //cf. comment below
	private static void put (final JSONObject json, final String key, final Object value) throws JSONConvertException {
		if (value == null) {
			//just put it in the destination array :
			try {
				json.put(key, value);
			} catch (final JSONException exp) {
				throw new JSONConvertException(exp);
			}
		} else {
			final Class<?> type = value.getClass();
			try {
				if (isPrimitivelyConvertible(type)) {
					@SuppressWarnings("rawtypes")  //this is on purpose ; we need to call one of converter's generic-parameterized method, so as we don't know the type of 'value' at compile time, we need to explicitly call its bridge method
					final PrimitiveConverter converter = getPrimitiveConverter(type);
					converter.put(json, key, value);
				} else {
					final Object valueToPut;
					if (type.isArray()) {
						valueToPut = toJSONArray((Object[])value);
					}
					//TODO Enum
					else if (Bundle.class.equals(type)) {
						valueToPut = toJSON((Bundle)value);
					} else {
						valueToPut = toJSON(value);
					}
					json.put(key, valueToPut);
				}
			} catch (final JSONException exp) {
				throw new JSONConvertException(exp);
			}
		}
	}
	@SuppressWarnings({ "unchecked", "unused" })  //cf. comment below for unchecked  //not currently used but keep as utility method
	private static void put (final JSONArray json, final int index, final Object value) throws JSONConvertException {
		if (value == null) {
			//just put it in the destination array :
			try {
				json.put(index, value);
			} catch (final JSONException exp) {
				throw new JSONConvertException(exp);
			}
		} else {
			final Class<?> type = value.getClass();
			try {
				if (isPrimitivelyConvertible(type)) {
					@SuppressWarnings("rawtypes")  //this is on purpose ; we need to call one of converter's generic-parameterized method, so as we don't know the type of 'value' at compile time, we need to explicitly call its bridge method
					final PrimitiveConverter converter = getPrimitiveConverter(type);
					converter.put(json, index, value);
				} else {
					final Object valueToPut;
					if (type.isArray()) {
						valueToPut = toJSONArray((Object[])value);
					}
					//TODO enum
					else if (Bundle.class.equals(type)) {
						valueToPut = toJSON((Bundle)value);
					} else {
						valueToPut = toJSON(value);
					}
					json.put(index, valueToPut);
				}
			} catch (final JSONException exp) {
				throw new JSONConvertException(exp);
			}
		}
	}
	@SuppressWarnings("unchecked")  //cf. comment below
	private static void put (final JSONArray json, final Object value) throws JSONConvertException {
		if (value == null) {
			//just put it in the destination array :
			json.put(value);
		} else {
			final Class<?> type = value.getClass();
			try {
				if (isPrimitivelyConvertible(type)) {
					@SuppressWarnings("rawtypes")  //this is on purpose ; we need to call one of converter's generic-parameterized method, so as we don't know the type of 'value' at compile time, we need to explicitly call its bridge method
					final PrimitiveConverter converter = getPrimitiveConverter(type);
					converter.put(json, value);
				} else {
					final Object valueToPut;
					if (type.isArray()) {
						valueToPut = toJSONArray((Object[])value);
					}
					//TODO enum
					else if (Bundle.class.equals(type)) {
						valueToPut = toJSON((Bundle)value);
					} else {
						valueToPut = toJSON(value);
					}
					json.put(valueToPut);
				}
			} catch (final JSONException exp) {
				throw new JSONConvertException(exp);
			}
		}
	}

	public static <T> T toObjectNoException(String jsonText, Class<T> clazz) {
		try {
			return toObjectNoException(JSONObjectUtils.parse(jsonText), clazz);
		} catch (JSONRuntimeException exp) {
			Log.e(TAG, exp.getMessage() + "", exp);
		}
		return null;
	}
	
	public static <T> T toObject(String jsonText, Class<T> clazz) throws JSONConvertException {
		try {
			return toObject(new JSONObject(jsonText), clazz);
		} catch (JSONException exp) {
			throw new JSONConvertException(exp);
		}
	}

	public static <T> T toObjectNoException(JSONObject json, Class<T> clazz) {
		try {
			return JSONConverter.toObject(json, clazz);
		} catch (JSONConvertException exp) {
			Log.e(TAG, exp.getMessage() + "", exp);
		}
		//if an exception occurred :
		return null;
	}
	
	public static <T> T toObject (final JSONObject json, final Class<T> type) throws JSONConvertException {
		return toObject(json, type, null);
	}
	
	private static <T> T toObject (final JSONObject json, final Class<T> type, final Object enclosingInstance) throws JSONConvertException {
		try {
			final T obj;
			if (type.isMemberClass() && ((type.getModifiers() & Modifier.STATIC) != Modifier.STATIC)) {  //if non-static member (aka inner) class (not provided by reflection API)
				//need to supply the enclosing instance
				final Constructor<T> defaultConstructor = type.getConstructor(type.getEnclosingClass());
				obj = defaultConstructor.newInstance(enclosingInstance);
			} else {
				obj = type.newInstance();
			}

			bind(json, obj);
			return obj;
		} catch (CommonRuntimeException exp) {
			throw new JSONConvertException(exp);
		} catch (IllegalAccessException exp) {
			throw new JSONConvertException(exp);
		} catch (InstantiationException exp) {
			throw new JSONConvertException(exp);
		} catch (final NoSuchMethodException exp) {
			throw new JSONConvertException(exp);
		} catch (final InvocationTargetException exp) {
			throw new JSONConvertException(exp);
		}
	}
	
	public static <T> T[] toObjectArrayNoException (final JSONArray json, Class<T> clazz) {
		try {
			return toObjectArray(json, clazz);
		} catch (final JSONConvertException exp) {
			Log.e(TAG, exp.getMessage() + "", exp);
		}
		//if an exception occurred :
		return null;
	}
	
	public static <T> T[] toObjectArray (final JSONArray json, Class<T> clazz) throws JSONConvertException {
		return toObjectArray(json, clazz, null);
	}
	
	private static <T> T[] toObjectArray (final JSONArray json, Class<T> clazz, Object enclosingObject) throws JSONConvertException {
		@SuppressWarnings("unchecked")
		final T[] objectArray = (T[])Array.newInstance(clazz, json.length());
		try {
			bind(json, objectArray, enclosingObject);
		} catch (final IllegalAccessException exp) {
			throw new JSONConvertException(exp);
		} catch (final InstantiationException exp) {
			throw new JSONConvertException(exp);
		}
		return objectArray;
	}
	
	private static void bind(JSONObject json, Bundle bundle) {
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = json.keys(); iterator.hasNext();) {
			String key = (String) iterator.next();
				
			JSONObject child = json.optJSONObject(key);
			if (child != null) {
				Bundle value = new Bundle();
				bind(child, value);
				bundle.putBundle(key, value);
				continue;
			}
				
			JSONArray arr = json.optJSONArray(key);
			if (arr != null) {
				String[] values = JSONArrayUtils.toStringArray(arr);
				bundle.putStringArray(key, values);
				continue;
			}
				
			String value = json.optString(key);
			bundle.putString(key, value);
		}
	}
	
	private static void bind (final JSONObject json, final Object obj) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, JSONConvertException {
		final ImmutableHashMap<String, Field> fieldMap = loadBindMap(obj.getClass());

		final Set<String> keySet = fieldMap.keySet();
		for (String key : keySet) {
			final Field field = fieldMap.get(key);
			final Class<?> type = field.getType();

			final Object value = retrieve(type, json, key, obj);
			if (value != null)  //if null, we just let the Java object to its default value (or the one set in the constructor)
				FieldUtils.setNoException(obj, field, value);
		}
	}

	private static void bind (final JSONArray jsonArr, final Object[] objs, final Object enclosingObject) throws IllegalAccessException, InstantiationException, JSONConvertException {
		final Class<?> type = objs.getClass();
		final Class<?> compType = type.getComponentType();
		for (int i = 0; i < objs.length; i++) {
			final Object obj = retrieve(compType, jsonArr, i, enclosingObject);
			if (obj != null)  //if null, leave to default value
				objs[i] = obj;
		}
	}
	
	public static JSONObject toJSONNoException(Object obj) {
		try {
			return toJSON(obj);
		} catch (Exception exp) {
			Log.e(TAG, exp.getMessage() + "", exp);
		}
		return null;
	}
	
	public static JSONObject toJSON(Bundle bundle) throws JSONConvertException {
		if (bundle == null) {
			return null;
		}
		
		JSONObject json = new JSONObject();
		Set<String> keySet = bundle.keySet();
		for (String key : keySet) {
			put(json, key, bundle.get(key));
		}
		
		return json;
	}

	public static JSONObject toJSON (final Object obj) throws JSONConvertException {
		if (obj == null)
			return null;

		final JSONObject json = new JSONObject();
		final ImmutableHashMap<Field, String> propMap = loadPropMap(obj.getClass());	
		final Set<Field> fieldSet = propMap.keySet();

		for (final Field field : fieldSet) {
			put(json, propMap.get(field), FieldUtils.get(obj, field));
		}
		
		return json;
	}

	public static JSONArray toJSONArray (final Object[] objs) throws JSONConvertException {
		JSONArray jsonArr = null;

		if (objs != null) {
			jsonArr = new JSONArray();
			for (Object obj : objs) {
				put(jsonArr, obj);
			}
		}
		
		return jsonArr;
	}
	
	private static ImmutableHashMap<String, Field> loadBindMap(Class<?> clazz) {
		ImmutableHashMap<String, Field> bindMap = mBindCache.get(clazz);
		if (bindMap != null) {
			return bindMap;
		}
		
		bindMap = createBindMap(clazz);
		mBindCache.put(clazz, bindMap);
		
		return bindMap;
	}
	
	private static ImmutableHashMap<String, Field> createBindMap(Class<?> clazz) {
		HashMap<String, Field> bindMap = new HashMap<String, Field>();
		Field[] fields = clazz.getDeclaredFields();
		
		for (Field field : fields) {
			Exclude exc = field.getAnnotation(Exclude.class);
			if (exc == null) {
				field.setAccessible(true);
				String name = NamingConventionUtils.fromJavaFieldNameToJSONName(field.getName());
				bindMap.put(name, field);
			}
		}
		
		return new ImmutableHashMap<String, Field>(bindMap);
	}
	
	private static ImmutableHashMap<Field, String> loadPropMap(Class<?> clazz) {
		ImmutableHashMap<Field, String> propMap = mPropCache.get(clazz);
		if (propMap != null) {
			return propMap;
		}
		
		propMap = createPropMap(clazz);
		mPropCache.put(clazz, propMap);
		
		return propMap;
	}
	
	private static ImmutableHashMap<Field, String> createPropMap(Class<?> clazz) {
		HashMap<Field, String> propMap = new HashMap<Field, String>();
		Field[] fields = clazz.getDeclaredFields();
		
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers()) && !field.isAnnotationPresent(Exclude.class) && !field.isSynthetic()) {  //ignore Exclude annotated fields and synthetic fields
				field.setAccessible(true);
				String name = NamingConventionUtils.fromJavaFieldNameToJSONName(field.getName());
				propMap.put(field, name);
			}
		}
		
		return new ImmutableHashMap<Field, String>(propMap);
	}
	
}
