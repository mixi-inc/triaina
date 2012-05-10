package jp.mixi.triaina.commons.test.workerservice;

import jp.mixi.triaina.commons.workerservice.IntentServiceHandler;
import jp.mixi.triaina.commons.workerservice.IntentServiceHandlerListener;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.test.AndroidTestCase;
import android.util.Log;

public class IntentServiceHandlerTest extends AndroidTestCase {

	public void testIntentServiceHandler() {
		new IntentServiceHandler(getContext().getMainLooper(), new MockListener());
	}

	public void testHandleMessageMessage() {
		MockListener listener = new MockListener();
		MockIntentServiceHandler handler = new MockIntentServiceHandler(getContext().getMainLooper(), listener);
		
		Message msg = new Message();
		msg.obj = new Intent();
		
		//test for exponential backoff
		int amount = -1;
		for (int i = 0; i < 10; i++) {
			handler.handleMessage(msg);
		
			assertEquals(msg.obj, listener.mIntent);
			assertEquals(i, listener.mRetry);
			assertTrue(amount <= listener.mDelayAmount);
			
			Log.w("TEST", listener.mDelayAmount + "");
			Log.w("TEST", handler.mUptimeMillis + "");
		
			assertEquals(msg.obj, handler.mMsg.obj);
			msg = handler.mMsg;
		}
	}

	
	public static class MockIntentServiceHandler extends IntentServiceHandler {
		private Message mMsg;
		private long mUptimeMillis;
		
		@Override
		public void dispatchMessage(Message msg) {
		}

		@Override
		public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
			mMsg = msg;
			mUptimeMillis = uptimeMillis;
			return true;
		}
		
		public MockIntentServiceHandler(Looper looper,
				IntentServiceHandlerListener listener) {
			super(looper, listener);
		}
		
	}
	
	public static class MockListener implements IntentServiceHandlerListener {
		private Intent mIntent;
		private int mRetry;
		private int mDelayAmount;
		
		@Override
		public boolean onHandleIntent(Intent intent, int retry, int delayAmount) {
			mIntent = intent;
			mRetry = retry;
			mDelayAmount = delayAmount;
			return false;
		}

		@Override
		public void stopSelf(int startId) {
		}
	}
}
