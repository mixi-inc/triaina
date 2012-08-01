package jp.mixi.triaina.webview.bridges;

import java.util.concurrent.atomic.AtomicInteger;

import com.google.inject.Inject;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import jp.mixi.triaina.webview.Callback;
import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.webview.annotation.Bridge;
import jp.mixi.triaina.webview.entity.device.NotificationParams;
import jp.mixi.triaina.webview.entity.device.NotificationResult;

public class NotificationBridge implements BridgeObject {
	@Inject
	private Context mContext;

	@Inject
	private NotificationManager mNotificationManager;
	
	private AtomicInteger mIdSeq = new AtomicInteger();
	
	private WebViewBridge mBridge;

	public NotificationBridge(WebViewBridge bridge) {
		mBridge = bridge;
	}
	
	@Bridge("system.notification.notify")
	public void doNofity(NotificationParams params, Callback<NotificationResult> callback) {
		Notification notification = 
				new NotificationCompat.Builder(mContext)
				.setContentTitle(params.getTitle())
				.setContentText(params.getText()).getNotification();
		
		int id = mIdSeq.incrementAndGet();
		mNotificationManager.notify(id, notification);
		callback.succeed(mBridge, new NotificationResult(id));
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
