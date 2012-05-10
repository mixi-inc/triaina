package jp.mixi.triaina.webview;

import android.os.Parcelable;
import jp.mixi.triaina.webview.entity.Result;

public interface Callback<T extends Result> extends Parcelable {
	public void succeed(WebViewBridge bridge, T result);
	
	public void fail(WebViewBridge bridge, String code, String msg);
}
