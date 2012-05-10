package jp.mixi.triaina.test.webview;

import org.json.JSONObject;

import android.os.Parcel;

import jp.mixi.triaina.test.mock.MockResult;
import jp.mixi.triaina.webview.Callback;
import jp.mixi.triaina.webview.CallbackHelper;
import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.webview.entity.Result;
import junit.framework.TestCase;

public class CallbackHelperTest extends TestCase {

	@SuppressWarnings("unchecked")
	public void testInvokeSucceed() throws Exception {
		Callback<?> callback = new Callback<MockResult>() {
			@Override
			public void succeed(WebViewBridge bridge, MockResult result) {
				assertEquals("AAA", result.getAaa());
			}
			@Override
			public void fail(WebViewBridge bridge, String code, String msg) {				
			}
			
			@Override
			public void writeToParcel(Parcel dest, int flags) {	
			}
			
			@Override
			public int describeContents() {
				return 0;
			}
		};
		
		mHelper.invokeSucceed(null, (Callback<Result>)callback, new JSONObject("{\"aaa\":\"AAA\"}"));
	}

	@SuppressWarnings("unchecked")
	public void testInvokeError() throws Exception {
		Callback<?> callback = new Callback<MockResult>() {
			@Override
			public void succeed(WebViewBridge bridge, MockResult result) {
			}
			@Override
			public void fail(WebViewBridge bridge, String code, String msg) {	
				assertEquals("1", code);
				assertEquals("aaa", msg);
			}
			
			@Override
			public void writeToParcel(Parcel dest, int flags) {
			}
			
			@Override
			public int describeContents() {
				return 0;
			}
		};
		
		mHelper.invokeFail(null, (Callback<Result>)callback, new JSONObject("{\"code\":\"1\",\"message\":\"aaa\"}"));
	}

	public void testGetResultClass() throws Exception {
		Callback<MockResult> callback = new Callback<MockResult>() {
			@Override
			public void succeed(WebViewBridge bridge, MockResult result) {
			}
			
			@SuppressWarnings("unused")
			public void succeed(Object result) {
			}
			
			@SuppressWarnings("unused")
			public void succeed() {
			}

			@Override
			public void fail(WebViewBridge bridge, String code, String msg) {				
			}
			
			@Override
			public void writeToParcel(Parcel dest, int flags) {	
			}
			
			@Override
			public int describeContents() {
				return 0;
			}
		};
		assertEquals(MockResult.class, mHelper.getResultClass(callback));
	}

	private CallbackHelper mHelper;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mHelper = new CallbackHelper();
	}
}
