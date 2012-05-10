package jp.mixi.triaina.webview.logic;

import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.webview.entity.device.WiFiP2PEnableParams;
import jp.mixi.triaina.webview.receiver.WiFiDirectBroadcastReceiver;

import com.google.inject.Inject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

public class WiFiP2PLogic {
	private static final String TAG = "WiFiP2PLogic";
	@Inject
	private Context mContext;
	
	private Object mManager;
	private Object mChannel;
	private BroadcastReceiver mReceiver;
	
	private WiFiP2PEnableParams mEnableParams;
	
	public void doEnable(WebViewBridge bridge, WiFiP2PEnableParams params) {
		if (mEnableParams != null)
			return;
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		mEnableParams = params;
		mManager = mContext.getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = ((WifiP2pManager)mManager).initialize(mContext, mContext.getMainLooper(), null);
		mReceiver = new WiFiDirectBroadcastReceiver((WifiP2pManager)mManager, (Channel)mChannel, bridge, params.getDiscoverCallback());
		mContext.registerReceiver(mReceiver, intentFilter);
	}
	
	public void doDiscover(WebViewBridge bridge) {
		((WifiP2pManager)mManager).discoverPeers((Channel)mChannel, new WifiP2pManager.ActionListener() {
			@Override
			public void onSuccess() {
				Log.d(TAG, "Discovery Initiated");
			}

			@Override
			public void onFailure(int reasonCode) {
				Log.d(TAG, "Discovery Failed : " + reasonCode);
			}
		});
	}
	
	public void doDisable(WebViewBridge bridge) {
		disable();
	}
	
	public void destory() {
		disable();
	}

	private void disable() {
		if (mEnableParams == null)
			return;
		
		mContext.unregisterReceiver(mReceiver);
		mReceiver = null;
		mChannel = null;
		mManager = null;
		mEnableParams = null;
	}
}
