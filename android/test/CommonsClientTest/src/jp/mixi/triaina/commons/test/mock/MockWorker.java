package jp.mixi.triaina.commons.test.mock;

import android.content.Context;
import android.os.Handler;
import android.os.ResultReceiver;
import jp.mixi.triaina.commons.workerservice.Job;
import jp.mixi.triaina.commons.workerservice.Worker;

public class MockWorker implements Worker<MockJob> {
	private Job job;
	private int retry;
	private int delayAmount;
	private ResultReceiver receiver;
	private Context context;
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	private Handler handler;
	
	@Override
	public boolean process(MockJob job, int retry, int delayAmount, ResultReceiver receiver, Context context,
			Handler handler) {

		this.job = job;
		this.retry = retry;
		this.delayAmount = delayAmount;
		this.receiver = receiver;
		this.context = context;
		this.handler = handler;
		
		return true;
	}
	
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public ResultReceiver getResultReceiver() {
		return receiver;
	}
	
	public void setResultReceiver(ResultReceiver receiver) {
		this.receiver = receiver;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public int getDelayAmount() {
		return delayAmount;
	}

	public void setDelayAmount(int delayAmount) {
		this.delayAmount = delayAmount;
	}
}
