package triaina.webview.bridges;

import java.util.Random;

import javax.inject.Inject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.SparseIntArray;
import triaina.webview.Callback;
import triaina.webview.WebViewBridge;
import triaina.webview.annotation.Bridge;
import triaina.webview.entity.device.NotificationClearParams;
import triaina.webview.entity.device.NotificationNotifyParams;
import triaina.webview.entity.device.NotificationResult;

public class NotificationBridge implements BridgeLifecyclable {
	private static final int DEFAULT_CODE = 200;

	private static final String VALUE_KEY = "value";

	@Inject
	private Context mContext;

	@Inject
	private NotificationManager mNotificationManager;

	private WebViewBridge mBridge;

	private static final SparseIntArray ICON_MAP;
	static {
		ICON_MAP = new SparseIntArray();
		ICON_MAP.append("add".hashCode(), android.R.drawable.ic_menu_add);
		ICON_MAP.append("agenda".hashCode(), android.R.drawable.ic_menu_agenda);
		ICON_MAP.append("call".hashCode(), android.R.drawable.ic_menu_call);
		ICON_MAP.append("camera".hashCode(), android.R.drawable.ic_menu_camera);
		ICON_MAP.append("close".hashCode(),
				android.R.drawable.ic_menu_close_clear_cancel);
		ICON_MAP.append("compass".hashCode(),
				android.R.drawable.ic_menu_compass);
		ICON_MAP.append("crop".hashCode(), android.R.drawable.ic_menu_crop);
		ICON_MAP.append("delete".hashCode(), android.R.drawable.ic_menu_delete);
		ICON_MAP.append("directions".hashCode(),
				android.R.drawable.ic_menu_directions);
		ICON_MAP.append("edit".hashCode(), android.R.drawable.ic_menu_edit);
		ICON_MAP.append("gallery".hashCode(),
				android.R.drawable.ic_menu_gallery);
		ICON_MAP.append("help".hashCode(), android.R.drawable.ic_menu_help);
		ICON_MAP.append("info".hashCode(),
				android.R.drawable.ic_menu_info_details);
		ICON_MAP.append("manage".hashCode(), android.R.drawable.ic_menu_manage);
		ICON_MAP.append("map".hashCode(), android.R.drawable.ic_menu_mapmode);
		ICON_MAP.append("more".hashCode(), android.R.drawable.ic_menu_more);
		ICON_MAP.append("calendar".hashCode(),
				android.R.drawable.ic_menu_my_calendar);
		ICON_MAP.append("location".hashCode(),
				android.R.drawable.ic_menu_mylocation);
		ICON_MAP.append("places".hashCode(),
				android.R.drawable.ic_menu_myplaces);
		ICON_MAP.append("preferences".hashCode(),
				android.R.drawable.ic_menu_preferences);
		ICON_MAP.append("history".hashCode(),
				android.R.drawable.ic_menu_recent_history);
		ICON_MAP.append("image".hashCode(),
				android.R.drawable.ic_menu_report_image);
		ICON_MAP.append("revert".hashCode(), android.R.drawable.ic_menu_revert);
		ICON_MAP.append("rotate".hashCode(), android.R.drawable.ic_menu_rotate);
		ICON_MAP.append("save".hashCode(), android.R.drawable.ic_menu_save);
		ICON_MAP.append("search".hashCode(), android.R.drawable.ic_menu_search);
		ICON_MAP.append("send".hashCode(), android.R.drawable.ic_menu_send);
		ICON_MAP.append("set_as".hashCode(), android.R.drawable.ic_menu_set_as);
		ICON_MAP.append("share".hashCode(), android.R.drawable.ic_menu_share);
		ICON_MAP.append("slideshow".hashCode(),
				android.R.drawable.ic_menu_slideshow);
		ICON_MAP.append("upload".hashCode(), android.R.drawable.ic_menu_upload);
		ICON_MAP.append("view".hashCode(), android.R.drawable.ic_menu_view);
		ICON_MAP.append("zoom".hashCode(), android.R.drawable.ic_menu_zoom);
	}

	public NotificationBridge(WebViewBridge bridge) {
		mBridge = bridge;
	}

	@Bridge("system.notification.notify")
	public void nofity(NotificationNotifyParams params,
			Callback<NotificationResult> callback) {
		Intent intent = new Intent();
		intent.setClassName(mContext.getPackageName(),
				mContext.getPackageName() + "." + params.getView());
		intent.putExtra(VALUE_KEY, params.getValue());

		PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
				DEFAULT_CODE, intent, Intent.FLAG_ACTIVITY_NEW_TASK);

		Notification notification = new NotificationCompat.Builder(mContext)
				.setContentTitle(params.getTitle())
				.setContentText(params.getText())
				.setSmallIcon(ICON_MAP.get(params.getIcon().hashCode()))
				.setContentIntent(pendingIntent).getNotification();

		int id = new Random().nextInt();
		mNotificationManager.notify(id, notification);
		callback.succeed(mBridge, new NotificationResult(id + ""));
	}

	@Bridge("system.notification.clear")
	public void clear(NotificationClearParams params) {
		mNotificationManager.cancel(Integer.parseInt(params.getId()));
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
