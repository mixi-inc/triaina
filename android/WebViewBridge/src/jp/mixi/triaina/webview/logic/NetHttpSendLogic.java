package jp.mixi.triaina.webview.logic;

import com.google.inject.Inject;

import jp.mixi.triaina.commons.workerservice.Job;
import jp.mixi.triaina.commons.workerservice.NetworkWorkerService;
import jp.mixi.triaina.webview.Callback;
import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.webview.entity.device.NetHttpSendParams;
import jp.mixi.triaina.webview.entity.device.NetHttpSendResult;
import jp.mixi.triaina.webview.jobs.HttpRequestJob;
import jp.mixi.triaina.webview.receiver.CallbackReceiver;
import android.content.Context;
import android.os.Handler;
import android.webkit.CookieManager;

public class NetHttpSendLogic {
	private Handler mHandler = new Handler();
	
	@Inject
	private Context mContext;
	
	public void invoke(WebViewBridge bridge, NetHttpSendParams params, Callback<NetHttpSendResult> callback) {
		HttpRequestJob job = createHttpRequestJob(params);
		invokeUsingNetworkWorkerService(bridge, job, callback);
	}
	
	public HttpRequestJob createHttpRequestJob(NetHttpSendParams params) {
		CookieManager manager = CookieManager.getInstance();
		HttpRequestJob job = new HttpRequestJob();
		job.setCookie(manager.getCookie(params.getUrl()));
		job.setParams(params);
		return job;
	}
	
	public void invokeUsingNetworkWorkerService(WebViewBridge bridge, Job job, Callback<NetHttpSendResult> callback) {
		NetworkWorkerService.invoke(mContext, job, new CallbackReceiver(
				bridge, callback, mHandler));
	}
}
