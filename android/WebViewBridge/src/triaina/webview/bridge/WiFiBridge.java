package triaina.webview.bridge;

import javax.inject.Inject;

import triaina.webview.Callback;
import triaina.webview.WebViewBridge;
import triaina.webview.annotation.Bridge;
import triaina.webview.entity.device.WiFiGetMacAddressResult;

import android.net.wifi.WifiManager;

public class WiFiBridge implements BridgeLifecyclable {
	@Inject
	private WifiManager mWifiManager;
	
	public WebViewBridge mBridge;

	public WiFiBridge(WebViewBridge bridge) {
	    mBridge = bridge;
	}
	
	@Bridge("system.wifi.mac.get")
	public void getMacAddress(Callback<WiFiGetMacAddressResult> callback) {
		String addr = mWifiManager.getConnectionInfo().getMacAddress();
		WiFiGetMacAddressResult result = new WiFiGetMacAddressResult();
		result.setMacAddress(addr);
		callback.succeed(mBridge, result);
	}
	
	@Bridge("system.wifi.enable")
	public void enable() {
		mWifiManager.setWifiEnabled(true);
	}
	
	@Bridge("system.wifi.disable")
	public void disable() {
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
