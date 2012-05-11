package jp.mixi.triaina.webview.receiver;

import java.util.Collection;

import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.webview.entity.web.WiFiP2PDeviceParams;
import jp.mixi.triaina.webview.entity.web.WiFiP2PDiscoverParams;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver implements
        PeerListListener {
	@SuppressWarnings("unused")
    private static final String TAG = "WiFiDirectBroadcastReceiver";
	private WifiP2pManager mManager;
	private Channel mChannel;
	private WebViewBridge mBridge;
	private String mDiscoverDest;

	public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
	        WebViewBridge bridge, String discoverDest) {
		super();
		mManager = manager;
		mChannel = channel;
		mBridge = bridge;
		mDiscoverDest = discoverDest;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
			mManager.requestPeers(mChannel, this);
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peers) {
		Collection<WifiP2pDevice> c = peers.getDeviceList();
		WiFiP2PDeviceParams[] devices = new WiFiP2PDeviceParams[c.size()];
		WiFiP2PDiscoverParams params = new WiFiP2PDiscoverParams();

		int i = 0;
		for (WifiP2pDevice d : c) {
			devices[i] = new WiFiP2PDeviceParams();
			devices[i].setDeviceName(d.deviceName);
			devices[i].setDeviceAddress(d.deviceAddress);
			i++;
		}

		params.setDevices(devices);
		mBridge.call(mDiscoverDest, params);
	}
}
