package jp.mixi.triaina.webview.receiver;

import jp.mixi.triaina.webview.Callback;
import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.webview.entity.Result;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class CallbackReceiver extends ResultReceiver {
	public static final String RESULT = "result";
	public static final String MESSAGE = "message";
	public static final int SUCCESS_CODE = 0;
	
	public static final int TIMEOUT_ERROR_CODE = 1;
	
	
	private WebViewBridge mBridge;
	@SuppressWarnings("rawtypes")
	private Callback mCallback;
	
	public CallbackReceiver(WebViewBridge bridge, @SuppressWarnings("rawtypes") Callback callback, Handler handler) {
		super(handler);
		mBridge = bridge;
		mCallback = callback;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData) {
		if (resultCode == SUCCESS_CODE) {
			resultData.setClassLoader(CallbackReceiver.class.getClassLoader());
			Result result = resultData.getParcelable(RESULT);
			mCallback.succeed(mBridge, result);
		} else {
			String msg = null;
			if (resultData != null)
				msg = resultData.getString(MESSAGE);
			mCallback.fail(mBridge, resultCode + "", msg + "");
		}
	}
}
