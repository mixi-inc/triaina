package triaina.commons.http;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Method;

public class CommonHttpClient {
	private static final String TAG = "CommonHttpClient";
	private static Class<?> mProtoType = FixedHttpClient.class;

	public static OkHttpClient newInstance() {
		try {
			Method method = mProtoType.getMethod("newInstance");
			return (OkHttpClient)method.invoke(null);
		} catch (Exception exp) {
			Log.e(TAG, exp.getMessage() + "", exp);
		}
		return null;
	}
	
	public static void setProtoType(Class<?> protoType) {
		mProtoType = protoType;
	}
	
	public static Class<?> getProtoType() {
		return mProtoType;
	}
	
	public static void closeInstance(OkHttpClient instance) {
		// do nothing
	}
}
