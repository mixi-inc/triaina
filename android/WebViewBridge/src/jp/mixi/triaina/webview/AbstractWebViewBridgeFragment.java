package jp.mixi.triaina.webview;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.inject.Inject;

import jp.mixi.triaina.commons.utils.SystemUtils;
import jp.mixi.triaina.injector.TriainaEnvironment;
import jp.mixi.triaina.injector.fragment.AbstractTriainaFragment;
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
import jp.mixi.triaina.webview.logic.NetHttpSendLogic;
import jp.mixi.triaina.webview.logic.SensorAccelerometerLogic;
import jp.mixi.triaina.webview.logic.VibratorLogic;
import jp.mixi.triaina.webview.logic.WebStatusLogic;
import jp.mixi.triaina.webview.logic.WiFiLogic;

public abstract class AbstractWebViewBridgeFragment extends AbstractTriainaFragment {
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
	private VibratorLogic mVibratorLogic;
	
	@Inject
	private SensorAccelerometerLogic mAccelerometerLogic;
	
	@Inject
	private WiFiLogic mWiFiLogic;
	
	final public String[] getDomains() {
		return mWebViewBridge.getDomains();
	}
	
	public void call(String channel, Params params) {
		mWebViewBridge.call(channel, params);
	}
	
	public void call(String channel, Params params, Callback<?> callback) {
		mWebViewBridge.call(channel, params, callback);
	}
	
	final public WebViewBridge getWebViewBridge() {
		return mWebViewBridge;
	}
	
	@Bridge("system.web.status")
	public void doWebStatus(WebStatusParams params, Callback<NotifyStatusResult> callback) {
		mWebStatusLogic.invoke(getActivity(), getWebViewBridge(), params, callback);
	}

	@Bridge("system.environment.set")
	public void doEnvironmentSet(EnvironmentSetParams params) {
		mEnvironment.set(params.getName(), params.getValue());
	}
	
	@Bridge("system.notification.notify")
	public void doNotificationNotify(NotificationParams params) {
		Toast.makeText(getActivity(), params.getMessage(), Toast.LENGTH_SHORT).show();
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
		SystemUtils.launchExternalBrowser(getActivity(), Uri.parse(params.getUrl()), getClass());
	}

	@Bridge("system.vibrator.vibrate")
	public void doVibratorVibrate(VibratorVibrateParams params) {
		mVibratorLogic.invoke(params);
	}
	
	@Bridge("system.sensor.accelerometer.enable")
	public void doSensorAccelerometerEnable(SensorAccelerometerEnableParams params) {
		mAccelerometerLogic.enable(getWebViewBridge(), params);
	}
	
	@Bridge("system.sensor.accelerometer.disable")
	public void doSensorAccelerometerDisable() {
		mAccelerometerLogic.disable();
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
	
	@Bridge("system.web.error")
	public void doWebError() {
		//ignore
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mWebViewBridge = new WebViewBridge(getActivity());
		mConfigurator.configureBridge(mWebViewBridge, this);
		return mWebViewBridge;
	}
	
	@Override
    public void onResume() {
	    super.onResume();
	    mAccelerometerLogic.resume(mWebViewBridge);
    }
	
	@Override
    public void onPause() {
		mVibratorLogic.cancel();
		mAccelerometerLogic.pause();
		super.onPause();
    }
	
	@Override
    public void onDestroyView() {
		mWebViewBridge.destroy();
		super.onDestroyView();
    }

}
