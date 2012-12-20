package triaina.test.mock;

import triaina.commons.workerservice.NetworkWorker;
import android.content.Context;
import android.os.Handler;
import android.os.ResultReceiver;

public class MockNetworkWorker implements NetworkWorker<MockNetworkJob> {

	@Override
	public boolean process(MockNetworkJob job, int retry, int delayAmount, ResultReceiver receiver, Context context,
			Handler handler) {
		return true;
	}

	@Override
	public void onNetworkStatusChanged(boolean isConnected) {

	}
}
