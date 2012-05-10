package jp.mixi.triaina.commons.workerservice;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class IntentServiceHandler extends Handler {
	private static final int COUNT_MASK = 0x000FFFFF;
	private static final int AMOUNT_TIME_MASK = 0x7FF00000;
	private static final int AMOUNT_TIME_SHIFT = 20;
	private static final int LIMIT_EXPONENTIAL = 8;
	
	private IntentServiceHandlerListener mListener;
	
	public IntentServiceHandler(Looper looper, IntentServiceHandlerListener listener) {
		super(looper);
		mListener = listener;
	}

	@Override
	public void handleMessage(Message msg) {
		int retry = getRetryCount(msg);
		boolean isFinished = mListener.onHandleIntent((Intent)msg.obj, retry, getAmountTime(msg));
		
		if (!isFinished && retry < COUNT_MASK) {
			// backoff delay
			Message newMessage = new Message();
			newMessage.copyFrom(msg);
			int next = calcNextTime(newMessage);
			sendMessageDelayed(newMessage, next * 1000);
		} else {
			mListener.stopSelf(msg.arg1);
		}
	}

	/**
	 * exponential backoff
	 * @param msg
	 * @return
	 */
	private int calcNextTime(Message msg) {
		int amount = getAmountTime(msg);
		int retry = incRetryCount(msg);
		int n = retry < LIMIT_EXPONENTIAL ? retry : LIMIT_EXPONENTIAL;
		int max = (int)Math.pow(2, n);
		int next = (int)(Math.random() * max);
		setAmountTime(msg, amount + next);
		return next;
	}
	
	private int incRetryCount(Message msg) {
		return ++msg.arg2 & COUNT_MASK;
	}
	
	private int getRetryCount(Message msg) {
		return msg.arg2 & COUNT_MASK;
	}
	
	private int getAmountTime(Message msg) {
		return (msg.arg2 & AMOUNT_TIME_MASK) >> AMOUNT_TIME_SHIFT;
	}
	
	private void setAmountTime(Message msg, int t) {
		msg.arg2 &= ~AMOUNT_TIME_MASK;
		msg.arg2 |= t << AMOUNT_TIME_SHIFT;
	}

}
