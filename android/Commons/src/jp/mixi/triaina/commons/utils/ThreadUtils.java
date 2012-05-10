package jp.mixi.triaina.commons.utils;

import jp.mixi.triaina.commons.exception.CalledFromWrongThreadRuntimeException;
import android.os.Looper;

public final class ThreadUtils {
	private ThreadUtils() {}
	
	public static boolean isMainThread(Thread thread) {
		return Looper.getMainLooper().getThread() == thread;
	}
	
	public static boolean isMainThread() {
		return isMainThread(Thread.currentThread());
	}
	
	public static void checkMainThread(Thread thread) {
		if (!isMainThread(thread))
			throw new CalledFromWrongThreadRuntimeException("Don't touch without main thread!!"); 
	}
	
	public static void checkMainThread() {
		checkMainThread(Thread.currentThread());
	}
}
