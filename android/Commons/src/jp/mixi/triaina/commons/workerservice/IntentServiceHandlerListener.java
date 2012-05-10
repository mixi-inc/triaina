package jp.mixi.triaina.commons.workerservice;

import android.content.Intent;

public interface IntentServiceHandlerListener {
	
	public boolean onHandleIntent(Intent intent, int retry, int delayAmount);
	
	public void stopSelf(int startId); 

}
