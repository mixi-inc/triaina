package jp.mixi.triaina.commons.workerservice;

import android.content.Context;
import android.os.Handler;
import android.os.ResultReceiver;

public interface Worker <T extends Job> {
	
	public boolean process(T job, int retry, int delayAmount, ResultReceiver receiver, Context context, Handler handler) throws Exception;
}
