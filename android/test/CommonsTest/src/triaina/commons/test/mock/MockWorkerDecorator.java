package triaina.commons.test.mock;

import android.content.Context;
import android.os.Handler;
import android.os.ResultReceiver;
import triaina.commons.workerservice.WorkerDecorator;

public class MockWorkerDecorator implements WorkerDecorator<MockJob> {
	private MockWorker mWorker;
	
	public MockWorkerDecorator(MockWorker worker) {
		mWorker = worker;
	}
	
	@Override
	public boolean process(MockJob job, int retry, int delayAmount, ResultReceiver receiver,
			Context context, Handler handler) throws Exception {
		mWorker.process(job, retry, delayAmount, receiver, context, handler);
		return true;
	}
	
	public MockWorker getMockWorker() {
		return mWorker;
	}
}
