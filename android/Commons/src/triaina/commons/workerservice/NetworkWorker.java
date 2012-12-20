package triaina.commons.workerservice;

import android.content.Context;
import android.os.Handler;
import android.os.ResultReceiver;

public interface NetworkWorker<T extends Job> extends Worker<T> {
	
	public void onNetworkStatusChanged(boolean isConnected);
	
	@Override
	public boolean process(T job, int retry, int delayAmount, ResultReceiver receiver, Context context, Handler handler) throws Exception;
}
