package triaina.webview.bridges;

import java.util.HashMap;

import java.util.Map;

import triaina.commons.collection.ImmutableHashMap;
import triaina.webview.Callback;
import triaina.webview.WebViewBridge;
import triaina.webview.annotation.Bridge;
import triaina.webview.entity.device.NotifyStatusResult;
import triaina.webview.entity.device.WebStatusParams;
import android.annotation.SuppressLint;
import android.content.res.Configuration;

@SuppressLint("UseSparseArrays")
@SuppressWarnings("deprecation")
public class WebStatusBridge implements BridgeObject {
    public static final Map<Integer, String> ORIENTATION_MAP;
    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(Configuration.ORIENTATION_UNDEFINED, "undefined");
        map.put(Configuration.ORIENTATION_LANDSCAPE, "landscape");
        map.put(Configuration.ORIENTATION_PORTRAIT, "portrait");
        map.put(Configuration.ORIENTATION_SQUARE, "square");
        ORIENTATION_MAP = new ImmutableHashMap<Integer, String>(map);
    }

    private WebViewBridge mBridge;

    public WebStatusBridge(WebViewBridge bridge) {
        mBridge = bridge;
    }

    @Bridge("system.web.status")
    public void doWebStatus(WebStatusParams params, Callback<NotifyStatusResult> callback) {
        NotifyStatusResult result = new NotifyStatusResult();
        result.setOrientation(ORIENTATION_MAP.get(mBridge.getContext().getResources().getConfiguration().orientation));
        callback.succeed(mBridge, result);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
    }
}
