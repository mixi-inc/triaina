package jp.mixi.triaina.webview.logic;

import java.util.HashMap;
import java.util.Map;

import jp.mixi.triaina.commons.collection.ImmutableHashMap;
import jp.mixi.triaina.webview.Callback;
import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.webview.entity.device.NotifyStatusResult;
import jp.mixi.triaina.webview.entity.device.WebStatusParams;
import android.content.Context;
import android.content.res.Configuration;

public class WebStatusLogic {
	public static final Map<Integer,String> ORIENTATION_MAP;
	static {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(Configuration.ORIENTATION_UNDEFINED, "undefined");
		map.put(Configuration.ORIENTATION_LANDSCAPE, "landscape");
		map.put(Configuration.ORIENTATION_PORTRAIT, "portrait");
		map.put(Configuration.ORIENTATION_SQUARE, "square");
		ORIENTATION_MAP = new ImmutableHashMap<Integer, String>(map);
	}
	
	public void invoke(Context context, WebViewBridge bridge, WebStatusParams params, Callback<NotifyStatusResult> callback) {
		NotifyStatusResult result = new NotifyStatusResult();
		result.setOrientation(ORIENTATION_MAP.get(context.getResources().getConfiguration().orientation));
		callback.succeed(bridge, result);
	}
}
