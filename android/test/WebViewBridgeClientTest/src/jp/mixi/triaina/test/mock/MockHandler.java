package jp.mixi.triaina.test.mock;

import android.os.Handler;
import android.os.Message;

public class MockHandler extends Handler {
	
	@Override
	public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
		Runnable r = msg.getCallback();
		r.run();
		return true;
	}
}
