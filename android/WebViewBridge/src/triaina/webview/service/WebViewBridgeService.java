package triaina.webview.service;

import android.app.IntentService;
import android.content.Intent;

public class WebViewBridgeService extends IntentService {
	private static final String TAG = "WebViewBridgeService";
	
	public WebViewBridgeService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}
}
