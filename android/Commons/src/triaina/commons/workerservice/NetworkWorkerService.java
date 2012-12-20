package triaina.commons.workerservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.ResultReceiver;
import android.util.Log;

public class NetworkWorkerService extends WorkerService {
	private static final String TAG = NetworkWorkerService.class.getCanonicalName();
		
	private boolean mIsConnected;
	
	private BroadcastReceiver mNetworkStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean lastConnected = mIsConnected;
			updateNetworkConnectionStatus();
            
			Log.v(TAG, "network status changed: " + lastConnected + " -> " + mIsConnected);
			if (lastConnected != mIsConnected) {
				NetworkWorker<?> worker = (NetworkWorker<?>)getCurrentWorker();
				if (worker != null) {
					worker.onNetworkStatusChanged(mIsConnected);
				}
			}
		}
	};
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean onHandleIntent(Intent intent, int retry, int delayAmount) {
		Job job = getJob(intent);
		
		NetworkWorker worker;
		try {
			worker = (NetworkWorker)getWorker(job);
		} catch (Exception exp) {
			Log.e(TAG, exp.getMessage() + "", exp);
			return true;
		}
		
		try {
			setCurrentWorker(worker);
			worker.onNetworkStatusChanged(mIsConnected);
			return worker.process(job, retry, delayAmount, (ResultReceiver)intent.getParcelableExtra(EXTRA_RECEIVER), this, getHandler());
		} catch (Exception exp) {
			Log.e(TAG, exp.getMessage() + "", exp);
			return true;
		} finally {
			setCurrentWorker(null);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		setupNetworkStatusReceiver();
	}

	@Override
	public void onDestroy() {
		teardownNetworkStatusReceiver();
		super.onDestroy();
	}
	
	public static void invoke(Context context, Job job) {
		invoke(NetworkWorkerService.class, context, job, null);
	}
	
	public static void invoke(Context context, Job job, ResultReceiver receiver) {
		invoke(NetworkWorkerService.class, context, job, receiver);
	}
	
	public static void buildIntent(Intent intent, Context context, Job job, ResultReceiver receiver) {
		buildIntent(NetworkWorkerService.class, intent, context, job, receiver);
	}
	
	private void setupNetworkStatusReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkStateReceiver, filter);
        updateNetworkConnectionStatus();
    }
	
	 private void teardownNetworkStatusReceiver() {
		 unregisterReceiver(mNetworkStateReceiver);
	 }
	
	private void updateNetworkConnectionStatus() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            mIsConnected = info.isConnected();
        } else {
            mIsConnected = false;
        }
    }
}
