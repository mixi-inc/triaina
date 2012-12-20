package triaina.commons.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public final class ToastUtils {
	public static void showOnMainThread(final Context context, final int resId, final int duration, Handler handler) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, resId, duration).show();
			}
		});
	}
	
	public static void showOnMainThread(final Context context, final String text, final int duration, Handler handler) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, text, duration).show();
			}
		});
	}
}
