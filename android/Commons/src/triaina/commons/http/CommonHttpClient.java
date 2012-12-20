package triaina.commons.http;

import java.lang.reflect.Method;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;

import android.net.http.AndroidHttpClient;
import android.util.Log;

public class CommonHttpClient {
	private static final String TAG = "CommonHttpClient";
	private static Class<?> mProtoType = FixedHttpClient.class;

	public static HttpClient newInstance() {
		try {
			Method method = mProtoType.getMethod("newInstance");
			return (HttpClient)method.invoke(null);
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
	
	public static void closeInstance(HttpClient instance) {
		if (instance == null)
			return;
		
		if (instance instanceof AndroidHttpClient) {
			AndroidHttpClient client = (AndroidHttpClient) instance;
			client.close();
		} else {
			ClientConnectionManager mgr = instance.getConnectionManager();
			if (mgr != null)
				mgr.shutdown();
		}
	}
}
