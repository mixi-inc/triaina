package jp.mixi.triaina.webview.bridges;

import jp.mixi.triaina.webview.Callback;
import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.webview.annotation.Bridge;
import jp.mixi.triaina.webview.entity.device.WiFiGetDeviceAddressResult;

import com.google.inject.Inject;

import android.net.wifi.WifiManager;

public class WiFiBridge implements BridgeObject {
	@Inject
	private WifiManager mWifiManager;
	
	public WebViewBridge mBridge;

	public WiFiBridge(WebViewBridge bridge) {
	    mBridge = bridge;
	}
	
	@Bridge("system.wifi.getDeviceAddress")
	public void doGetDeviceAddress(Callback<WiFiGetDeviceAddressResult> callback) {
		String addr = mWifiManager.getConnectionInfo().getMacAddress();
		WiFiGetDeviceAddressResult result = new WiFiGetDeviceAddressResult();
		result.setDeviceAddress(addr);
		callback.succeed(mBridge, result);
	}
	
	@Bridge("system.wifi.enable")
	public void doEnable() {
		mWifiManager.setWifiEnabled(true);
	}
	
	@Bridge("system.wifi.disable")
	public void doDisable() {
		mWifiManager.setWifiEnabled(false);
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
