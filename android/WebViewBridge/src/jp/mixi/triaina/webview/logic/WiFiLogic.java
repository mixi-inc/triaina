package jp.mixi.triaina.webview.logic;

import jp.mixi.triaina.webview.Callback;
import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.webview.entity.device.WiFiGetDeviceAddressResult;

import com.google.inject.Inject;

import android.net.wifi.WifiManager;

public class WiFiLogic {
	@Inject
	private WifiManager mWifiManager;

	public void doGetDeviceAddress(WebViewBridge bridge, Callback<WiFiGetDeviceAddressResult> callback) {
		String addr = mWifiManager.getConnectionInfo().getMacAddress();
		WiFiGetDeviceAddressResult result = new WiFiGetDeviceAddressResult();
		result.setDeviceAddress(addr);
		callback.succeed(bridge, result);
	}
	
	public void doEnable() {
		mWifiManager.setWifiEnabled(true);
	}
	
	public void doDisable() {
		mWifiManager.setWifiEnabled(false);
	}
}
