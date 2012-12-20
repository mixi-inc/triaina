package triaina.webview.bridges;

import triaina.webview.WebViewBridge;
import triaina.webview.annotation.Bridge;
import triaina.webview.entity.device.WiFiP2PEnableParams;
import triaina.webview.receiver.WiFiDirectBroadcastReceiver;

import com.google.inject.Inject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Build;
import android.util.Log;

public class WiFiP2PBridge implements BridgeObject {
    private static final String TAG = "WiFiP2PLogic";
    @Inject
    private Context mContext;

    private Object mManager;
    private Object mChannel;
    private BroadcastReceiver mReceiver;

    private WiFiP2PEnableParams mEnableParams;

    private WebViewBridge mBridge;
    
    public WiFiP2PBridge(WebViewBridge bridge) {
        mBridge = bridge;
    }
    
    @Bridge("system.wifip2p.enable")
    public void doEnable(WiFiP2PEnableParams params) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            return;

        if (mEnableParams != null)
            return;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mEnableParams = params;
        mManager = mContext.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = ((WifiP2pManager) mManager).initialize(mContext, mContext.getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver((WifiP2pManager) mManager, (Channel) mChannel, mBridge,
                params.getDiscoverCallback());
        mContext.registerReceiver(mReceiver, intentFilter);
    }

    @Bridge("system.wifip2p.discover")
    public void doDiscover() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            return;

        ((WifiP2pManager) mManager).discoverPeers((Channel) mChannel, new WifiP2pManager.ActionListener() {
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

    @Bridge("system.wifip2p.disable")
    public void doDisable() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            return;

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

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
        disable();
    }
}
