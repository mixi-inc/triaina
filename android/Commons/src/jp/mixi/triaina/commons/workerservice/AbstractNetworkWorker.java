package jp.mixi.triaina.commons.workerservice;

import java.util.concurrent.CountDownLatch;

import android.content.Context;
import android.os.Handler;
import android.os.ResultReceiver;

public abstract class AbstractNetworkWorker<T extends Job> implements NetworkWorker<T> {
	private volatile boolean mIsConnected;
	
	private volatile CountDownLatch mNetworkWaitLatch;

	@Override
	abstract public boolean process(T job, int retry, int delayAmount, ResultReceiver receiver, Context context, Handler handler) throws Exception ;
	
	@Override
	public void onNetworkStatusChanged(boolean isConnected) {
		mIsConnected = isConnected;	
		if (mIsConnected && mNetworkWaitLatch != null)
				mNetworkWaitLatch.countDown();
	}
	
	public boolean isConnected() {
		return mIsConnected;
	}
	
	public boolean waitForNetworkAvailable() {
		CountDownLatch networkWaitLatch = null;
		
		if (!mIsConnected)
			networkWaitLatch = new CountDownLatch(1); 
		
		if (!mIsConnected && networkWaitLatch != null) {
			try {
				networkWaitLatch.await();
			} catch (InterruptedException exp) {
				return true;
			}
		}
		
		return false;
	}
}
