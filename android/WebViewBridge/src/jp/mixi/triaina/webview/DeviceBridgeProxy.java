package jp.mixi.triaina.webview;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;

import jp.mixi.triaina.commons.exception.InvocationRuntimeException;
import jp.mixi.triaina.commons.exception.JSONConvertException;
import jp.mixi.triaina.commons.exception.NotFoundRuntimeException;
import jp.mixi.triaina.commons.json.JSONConverter;
import jp.mixi.triaina.commons.utils.ClassUtils;
import jp.mixi.triaina.commons.utils.FloatUtils;
import jp.mixi.triaina.commons.utils.JSONObjectUtils;
import jp.mixi.triaina.webview.entity.Error;
import jp.mixi.triaina.webview.entity.Params;
import jp.mixi.triaina.webview.entity.Result;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

public class DeviceBridgeProxy {
	private static final String TAG = "DeviceBridgeProxy";
	
	private CallbackHelper mCallbackHelper = new CallbackHelper();
	
	private WebViewBridge mWebViewBridge;
	private Object mDeviceBridge;
	private BridgeConfig mConfig;
	private Handler mHandler;
	
	public DeviceBridgeProxy(WebViewBridge webViewBridge, Object deviceBridge,
			BridgeConfig config, Handler handler) {
		mWebViewBridge = webViewBridge;
		mDeviceBridge = deviceBridge;
		mConfig = config;
		mHandler = handler;
	}
		
	public void notifyToDevice(String data) {
		final String jsonText = decode(data);
		logging("notified", jsonText);
		
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				String id = null;
				String dest = null;
				try {
					final JSONObject json = JSONObjectUtils.parse(jsonText);
					validateParamsVersion(json);
					
					if (!json.isNull("params")) {
						if (!json.isNull("id"))
							id = json.optString("id");
						dest = JSONObjectUtils.getString(json, "dest");
						invoke(id, dest, JSONObjectUtils.getJSONObject(json, "params"));	
					} else {
						id = JSONObjectUtils.getString(json, "id");
						
						@SuppressWarnings("unchecked")				
						Callback<Result> callback = (Callback<Result>)mWebViewBridge.getCallback(id);
						if (callback == null)
							return;
						
						mWebViewBridge.removeCallback(id);
						
						if (!json.isNull("result"))
							mCallbackHelper.invokeSucceed(mWebViewBridge, callback, JSONObjectUtils.getJSONObject(json, "result"));
						else if (!json.isNull("error"))
							mCallbackHelper.invokeFail(mWebViewBridge, callback, JSONObjectUtils.getJSONObject(json, "error"));
					}
				} catch (NotFoundRuntimeException exp) {
					Log.w(TAG, exp.getMessage() + "", exp);
					mWebViewBridge.returnToWeb(id, dest, new Error(ErrorCode.NOT_FOUND_BRIDGE_ERROR.getCode(), exp.getMessage() + "", dest));
				} catch (JSONConvertException exp) {
					Log.e(TAG, exp.getMessage() + "", exp);
					mWebViewBridge.returnToWeb(id, dest, new Error(ErrorCode.JSON_PARSE_ERROR.getCode(), exp.getMessage() + "", dest));
				} catch (InvocationTargetException exp) {
					Log.e(TAG, exp.getMessage() + "", exp);
					mWebViewBridge.returnToWeb(id, dest, new Error(ErrorCode.INVOCATION_BRIDGE_ERROR.getCode(), exp.getMessage() + "", dest));
				}  catch (Exception exp) {
					Log.e(TAG, exp.getMessage() + "", exp);
					mWebViewBridge.returnToWeb(id, dest, new Error(ErrorCode.UNKNOWN_ERROR.getCode(), exp.getMessage() + "", dest));
				}
			}
		});
	}
	
	private String decode(String data) {
		try {
			return URLDecoder.decode(data, "UTF-8");
		} catch (UnsupportedEncodingException exp) {
			Log.e(TAG, exp.getMessage() + "", exp);
		}
		return null;
	}
	
	private void invoke(String id, String dest, JSONObject json)
			throws InvocationTargetException, IllegalArgumentException,
			IllegalAccessException, JSONConvertException {
		
		final Method method = findMethod(dest);
		final Class<?>[] argTypes= method.getParameterTypes();
		Params params = null;
		Callback<?> callback = null;
		
		if (argTypes.length > 0 && ClassUtils.isImplement(argTypes[0], Params.class))
			params = (Params)JSONConverter.toObject(json, argTypes[0]);
		
		if (TextUtils.isEmpty(id))
			callback = new DummyCallback(dest);
		else
			callback = new WebViewBridgeCallback(id, dest);
		
		if (argTypes.length == 2)
			method.invoke(mDeviceBridge, params, callback);
		else if (argTypes.length == 1) {
			if (Callback.class.equals(argTypes[0]))
				method.invoke(mDeviceBridge, callback);
			else {
				method.invoke(mDeviceBridge, params);
				callback.succeed(mWebViewBridge, null);
			}
		} else
			method.invoke(mDeviceBridge);
	}
	
	public BridgeConfig getBridgeConfig() {
		return mConfig;
	}
	
	protected void validateParamsVersion(JSONObject json) {
		double version = Math.floor(FloatUtils.parse(JSONObjectUtils.getString(json, "bridge")));
		if (version != WebViewBridge.COMPATIBLE_VERSION) {
			throw new InvocationRuntimeException("Need WebViewBridge newer than " + (WebViewBridge.COMPATIBLE_VERSION + 1));
		}
	}
	
	protected void logging(String event, String jsonText) {
		String arg = jsonText == null ? "null" : jsonText;
		Log.d(TAG, event + " from Web with " + arg);
	}
	
	private Method findMethod(String dest) {
		Method method = mConfig.getMethodMap().get(dest);
		if (method == null)
			throw new NotFoundRuntimeException("cannot find " + dest + " bridge method");
		return method;
	}
	
	public static class WebViewBridgeCallback implements Callback<Result> {
		private String mId;
		private String mDest;
		
		public WebViewBridgeCallback(String id, String dest) {
			mId = id;
			mDest = dest;
		}
		
		public WebViewBridgeCallback(Parcel source) {
			mId = source.readString();
			mDest = source.readString();
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(mId);
			dest.writeString(mDest);
		}
		
		@Override
		public void succeed(final WebViewBridge bridge, final Result result) {
			bridge.returnToWeb(mId, mDest, result);
		}
		
		@Override
		public void fail(final WebViewBridge bridge, final String code, final String msg) {
			bridge.returnToWeb(mId, mDest, new Error(code, msg, mDest));
		}

		@Override
		public int describeContents() {
			return 0;
		}
		
		public static final Parcelable.Creator<WebViewBridgeCallback> CREATOR = new Parcelable.Creator<WebViewBridgeCallback>() {
	        @Override
	        public WebViewBridgeCallback createFromParcel(Parcel source) {
	            return new WebViewBridgeCallback(source);
	        }
	        @Override
	        public WebViewBridgeCallback[] newArray(int size) {
	            return new WebViewBridgeCallback[size];
	        }
	    };
	}
	
	public static class DummyCallback implements Callback<Result> {
		private static final String TAG = "DummyCallback";
		
		private String mDest;
		
		public DummyCallback(String dest) {
			mDest = dest;
		}
		
		public DummyCallback(Parcel source) {
			mDest = source.readString();
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(mDest);
		}
		
		@Override
		public void succeed(WebViewBridge bridge, Result result) {
			Log.w(TAG, "unnecessary callback is called with " + mDest);
		}

		@Override
		public void fail(WebViewBridge bridge, String code, String msg) {
			Log.w(TAG, "unnecessary callback is called with " + mDest);
		}

		@Override
		public int describeContents() {
			return 0;
		}
		
		public static final Parcelable.Creator<DummyCallback> CREATOR = new Parcelable.Creator<DummyCallback>() {
	        @Override
	        public DummyCallback createFromParcel(Parcel source) {
	            return new DummyCallback(source);
	        }
	        @Override
	        public DummyCallback[] newArray(int size) {
	            return new DummyCallback[size];
	        }
	    };
	}
}
