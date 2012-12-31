package triaina.webview.bridges;

import java.util.Random;

import javax.inject.Inject;

import triaina.commons.workerservice.Job;
import triaina.commons.workerservice.NetworkWorkerService;
import triaina.webview.Callback;
import triaina.webview.WebViewBridge;
import triaina.webview.annotation.Bridge;
import triaina.webview.entity.device.NetHttpSendParams;
import triaina.webview.entity.device.NetHttpSendResult;
import triaina.webview.jobs.HttpRequestJob;
import triaina.webview.receiver.CallbackReceiver;
import android.content.Context;
import android.os.Handler;
import android.webkit.CookieManager;

public class NetHttpSendBridge implements BridgeLifecyclable {
	@Inject
    private Handler mHandler;

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
        if (params.getNotification() != null)
        	job.setNotificationId(new Random().nextInt());
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
