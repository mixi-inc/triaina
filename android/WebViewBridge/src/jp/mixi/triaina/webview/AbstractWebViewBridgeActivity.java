package jp.mixi.triaina.webview;

import com.google.inject.Inject;

import jp.mixi.triaina.commons.utils.SystemUtils;
import jp.mixi.triaina.injector.TriainaEnvironment;
import jp.mixi.triaina.injector.activity.TriainaActivity;
import jp.mixi.triaina.webview.annotation.Bridge;
import jp.mixi.triaina.webview.annotation.NetBrowserOpenParams;
import jp.mixi.triaina.webview.entity.Params;
import jp.mixi.triaina.webview.entity.device.EnvironmentSetParams;
import jp.mixi.triaina.webview.entity.device.FormPictureSelectParams;
import jp.mixi.triaina.webview.entity.device.FormPictureSelectResult;
import jp.mixi.triaina.webview.entity.device.NetHttpSendParams;
import jp.mixi.triaina.webview.entity.device.NetHttpSendResult;
import jp.mixi.triaina.webview.entity.device.NotificationParams;
import jp.mixi.triaina.webview.entity.device.NotifyStatusResult;
import jp.mixi.triaina.webview.entity.device.SensorAccelerometerEnableParams;
import jp.mixi.triaina.webview.entity.device.VibratorVibrateParams;
import jp.mixi.triaina.webview.entity.device.WebStatusParams;
import jp.mixi.triaina.webview.entity.device.WiFiGetDeviceAddressResult;
import jp.mixi.triaina.webview.entity.device.WiFiP2PEnableParams;
import jp.mixi.triaina.webview.logic.NetHttpSendLogic;
import jp.mixi.triaina.webview.logic.SensorAccelerometerLogic;
import jp.mixi.triaina.webview.logic.VibratorLogic;
import jp.mixi.triaina.webview.logic.WebStatusLogic;
import jp.mixi.triaina.webview.logic.WiFiLogic;
import jp.mixi.triaina.webview.logic.WiFiP2PLogic;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public abstract class AbstractWebViewBridgeActivity extends TriainaActivity {
	@SuppressWarnings("unused")
	private static final String TAG = "AbsctactWebViewBridgeActivity";
	
	private WebViewBridge mWebViewBridge;
	
	@Inject
	private WebViewBridgeConfigurator mConfigurator;
	
	@Inject
	private WebStatusLogic mWebStatusLogic;
	
	@Inject
	private TriainaEnvironment mEnvironment;
	
	@Inject
	private NetHttpSendLogic mNetHttpSendLogic;
	
	@Inject
	private SensorAccelerometerLogic mAccelerometerLogic;
	
	@Inject
	private VibratorLogic mVibratorLogic;
	
	@Inject
	private WiFiLogic mWiFiLogic;
	
	@Inject
	private WiFiP2PLogic mWiFiP2PLogic;
	
	final public String[] getDomains() {
		return mWebViewBridge.getDomains();
	}
	
	public void call(String dest, Params params) {
		mWebViewBridge.call(dest, params);
	}
	
	public void call(String dest, Params params, Callback<?> callback) {
		mWebViewBridge.call(dest, params, callback);
	}
	
	final public WebViewBridge getWebViewBridge() {
		return mWebViewBridge;
	}

	@Bridge("system.web.status")
	public void doWebStatus(WebStatusParams params, Callback<NotifyStatusResult> callback) {
		mWebStatusLogic.invoke(this, getWebViewBridge(), params, callback);
	}
	
	@Bridge("system.environment.set")
	public void doEnvironmentSet(EnvironmentSetParams params) {
		mEnvironment.set(params.getName(), params.getValue());
	}

	@Bridge("system.notification.notify")
	public void doNotificationNotify(NotificationParams params) {
		Toast.makeText(this, params.getMessage(), Toast.LENGTH_SHORT).show();
	}
	
	@Bridge("system.form.picture.select")
	public void doFormPictureSelect(FormPictureSelectParams params, Callback<FormPictureSelectResult> callback) {
		//TODO need to implement Triaina Framework original logic
	}
	
	@Bridge("system.net.http.send")
	public void doNetHttpSend(NetHttpSendParams params, Callback<NetHttpSendResult> callback) {
		mNetHttpSendLogic.invoke(getWebViewBridge(), params, callback);
	}
	
	@Bridge("system.net.browser.open")
	public void doNetBrowserOpen(NetBrowserOpenParams params) {
		SystemUtils.launchExternalBrowser(this, Uri.parse(params.getUrl()), getClass());
	}
	
	@Bridge("system.sensor.accelerometer.enable")
	public void doSensorAccelerometerEnable(SensorAccelerometerEnableParams params) {
		mAccelerometerLogic.enable(getWebViewBridge(), params);
	}
	
	@Bridge("system.sensor.accelerometer.disable")
	public void doSensorAccelerometerDisable() {
		mAccelerometerLogic.disable();
	}
	
	@Bridge("system.vibrator.vibrate")
	public void doVibratorVibrate(VibratorVibrateParams params) {
		mVibratorLogic.invoke(params);
		//mVibratorLogic.cancel();
	}
	
	@Bridge("system.wifi.enable")
	public void doWiFiEnable() {
		mWiFiLogic.doEnable();
	}
	
	@Bridge("system.wifi.disable")
	public void doWiFiDisable() {
		mWiFiLogic.doDisable();
	}
	
	@Bridge("system.wifi.getDeviceAddress")
	public void doGetDeviceAddress(Callback<WiFiGetDeviceAddressResult> callback) {
		mWiFiLogic.doGetDeviceAddress(getWebViewBridge(), callback);
	}
	
	@Bridge("system.wifip2p.enable")
	public void doWiFiP2PEnable(WiFiP2PEnableParams params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			mWiFiP2PLogic.doEnable(mWebViewBridge, params);
	}
	
	@Bridge("system.wifip2p.discover")
	public void doWiFiP2PDiscover() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			mWiFiP2PLogic.doDiscover(mWebViewBridge);
	}
	
	@Bridge("system.wifip2p.disable")
	public void doWiFiP2PEnable() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			mWiFiP2PLogic.doDisable(mWebViewBridge);
	}
	
	@Bridge("system.web.error")
	public void doWebError() {
		//ignore
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWebViewBridge = mConfigurator.loadWebViewBridge(this);
		mConfigurator.configureBridge(mWebViewBridge, this);
	}
	
	@Override
    protected void onResume() {
	    super.onResume();
	    mAccelerometerLogic.resume(mWebViewBridge);
    }

	@Override
    protected void onPause() {
		mVibratorLogic.cancel();
		mAccelerometerLogic.pause();
		super.onPause();
    }
	
	@Override
    protected void onDestroy() {
		mWiFiP2PLogic.destory();
		mWebViewBridge.destroy();
		super.onDestroy();
    }
}
