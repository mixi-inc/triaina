package triaina.commons.workerservice;

import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public abstract class AbstractIntentService extends Service implements IntentServiceHandlerListener {
	private volatile Looper mServiceLooper;
	private volatile IntentServiceHandler mServiceHandler;
	private String mName;
	private boolean mRedelivery;

	/**
	 * Creates an IntentService.  Invoked by your subclass's constructor.
	 *
	 * @param name Used to name the worker thread, important only for debugging.
	 */
	public AbstractIntentService(String name) {
		super();
		mName = name;
	}

	/**
	 * Sets intent redelivery preferences.  Usually called from the constructor
	 * with your preferred semantics.
	 *
	 * <p>If enabled is true,
	 * {@link #onStartCommand(Intent, int, int)} will return
	 * {@link Service#START_REDELIVER_INTENT}, so if this process dies before
	 * {@link #onHandleIntent(Intent)} returns, the process will be restarted
	 * and the intent redelivered.  If multiple Intents have been sent, only
	 * the most recent one is guaranteed to be redelivered.
	 *
	 * <p>If enabled is false (the default),
	 * {@link #onStartCommand(Intent, int, int)} will return
	 * {@link Service#START_NOT_STICKY}, and if the process dies, the Intent
	 * dies along with it.
	 */
	public void setIntentRedelivery(boolean enabled) {
		mRedelivery = enabled;
	}

	@Override
	public void onCreate() {
		// TODO: It would be nice to have an option to hold a partial wakelock
		// during processing, and to have a static startService(Context, Intent)
		// method that would launch the service & hand off a wakelock.
		super.onCreate();
		HandlerThread thread = new HandlerThread("IntentService[" + mName + "]");
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new IntentServiceHandler(mServiceLooper, this);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		msg.arg2 = 0;
		msg.obj = intent;
		mServiceHandler.sendMessage(msg);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		onStart(intent, startId);
		return mRedelivery ? START_REDELIVER_INTENT : START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		mServiceLooper.quit();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * This method is invoked on the worker thread with a request to process.
	 * Only one Intent is processed at a time, but the processing happens on a
	 * worker thread that runs independently from other application logic.
	 * So, if this code takes a long time, it will hold up other requests to
	 * the same IntentService, but it will not hold up anything else.
	 *
	 * @param intent The value passed to {@link
	 *               android.content.Context#startService(Intent)}.
	 * @param delayAmount If this call isn't the first time,
	 *  delayed time in milliseconds will be passed.
	 * @return Return true if the process finished.
	 *  If your process need to be retried, return false then it will be
	 *  called again after a back-off delay
	 */
	public abstract boolean onHandleIntent(Intent intent, int retry, int delayAmount);
}
