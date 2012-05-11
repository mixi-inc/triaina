package jp.mixi.triaina.webview.bridges;

import com.google.inject.Inject;

import jp.mixi.triaina.commons.workerservice.Job;
import jp.mixi.triaina.commons.workerservice.NetworkWorkerService;
import jp.mixi.triaina.webview.Callback;
import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.webview.annotation.Bridge;
import jp.mixi.triaina.webview.entity.device.NetHttpSendParams;
import jp.mixi.triaina.webview.entity.device.NetHttpSendResult;
import jp.mixi.triaina.webview.jobs.HttpRequestJob;
import jp.mixi.triaina.webview.receiver.CallbackReceiver;
import android.content.Context;
import android.os.Handler;
import android.webkit.CookieManager;

public class NetHttpSendBridge implements BridgeObject {
    private Handler mHandler = new Handler();

    @Inject
    private Context mContext;

    private WebViewBridge mBridge;

    public NetHttpSendBridge(WebViewBridge bridge) {
        mBridge = bridge;
    }

    @Bridge("system.net.http.send")
    public void doHttpSend(NetHttpSendParams params, Callback<NetHttpSendResult> callback) {
        HttpRequestJob job = createHttpRequestJob(params);
        invokeUsingNetworkWorkerService(job, callback);
    }

    public HttpRequestJob createHttpRequestJob(NetHttpSendParams params) {
        CookieManager manager = CookieManager.getInstance();
        HttpRequestJob job = new HttpRequestJob();
        job.setCookie(manager.getCookie(params.getUrl()));
        job.setParams(params);
        return job;
    }

    public void invokeUsingNetworkWorkerService(Job job, Callback<NetHttpSendResult> callback) {
        NetworkWorkerService.invoke(mContext, job, new CallbackReceiver(mBridge, callback, mHandler));
    }

    public WebViewBridge getWebViewBridge() {
        return mBridge;
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
