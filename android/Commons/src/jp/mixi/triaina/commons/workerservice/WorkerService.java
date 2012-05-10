package jp.mixi.triaina.commons.workerservice;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicReference;

import jp.mixi.triaina.commons.exception.NotFoundRuntimeException;
import jp.mixi.triaina.commons.utils.ClassUtils;
import jp.mixi.triaina.commons.utils.ConstructorUtils;
import jp.mixi.triaina.commons.workerservice.annotation.Assign;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class WorkerService extends AbstractIntentService {
	private static final String TAG = "WorkerService";
	
	public static final String EXTRA_JOB = "job";
	public static final String EXTRA_RECEIVER = "receiver";
	
	public static final String EXTRA_CONFIRMED = "confirmed";
	public static final String ACTION_CANCEL_TASK = "jp.mixi.android.commons.workerservice.WorkerService.cancelTask";
    
	private AtomicReference<Worker<?>> mCurrentWorker = new AtomicReference<Worker<?>>();
	
	private volatile Handler mHandler;
	
    
	private BroadcastReceiver mCancelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	Log.v(TAG, "Cancel Broadcast Received");
            
        	try {
        		Abortable abortable = (Abortable)getCurrentWorker();
        		boolean confirmed = intent.getBooleanExtra(EXTRA_CONFIRMED, false);
        		if (!confirmed)
        			abortable.confirm(WorkerService.this, mHandler);
        		else
        			abortable.onAbort(WorkerService.this, mHandler);
        	} catch (ClassCastException exp) {
        		//ignore
        	}
        }
	};
    
	public WorkerService() {
		super(TAG);
	}
    
	@Override
	public void onCreate() {
		super.onCreate();
		mHandler = new Handler();
		registerReceiver(mCancelReceiver, new IntentFilter(ACTION_CANCEL_TASK));	
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(mCancelReceiver);
		super.onDestroy();
	}
	
	protected Handler getHandler() {
		return mHandler;
	}
	
	@SuppressWarnings("unchecked")
	protected Worker<?> getWorker(Job job) {
		Assign assign = job.getClass().getAnnotation(Assign.class);
		if (assign == null) {
			Log.w(TAG, "Worker is not assigned to " + job.toString());
			throw new NotFoundRuntimeException("Not found assigned worker");
		}
		
		Worker<?> worker = ClassUtils.newInstance(assign.worker());
		@SuppressWarnings("rawtypes")
		Class[] clazzs = assign.decorators();
		if (clazzs.length == 0)
			return worker;
		
		int i = clazzs.length - 1;
		Worker<?> decorator;
		
		Worker<?> arg = worker;
		do {
			@SuppressWarnings("rawtypes")
			Constructor cons = getConstructor(clazzs[i], arg);
			decorator = (Worker<?>)ConstructorUtils.newInstance(cons, arg);
			arg = decorator;
		} while(--i >= 0);
		
		return decorator;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Constructor getConstructor(Class clazz, Worker<?> worker) {
		try {
			return ClassUtils.getConstructor(clazz, new Class[]{worker.getClass()});
		} catch (Exception ignore) {
		}
		
		try {
			return ClassUtils.getConstructor(clazz, new Class[]{NetworkWorker.class});
		} catch (Exception ignore) {
		}
		
		return ClassUtils.getConstructor(clazz, new Class[]{Worker.class});
	}
	
	protected void setCurrentWorker(Worker<?> worker) {
		mCurrentWorker.set(worker);
	}
	
	protected Worker<?> getCurrentWorker() {
		return mCurrentWorker.get();
	}
	
	protected Job getJob(Intent intent) {
		return intent.getParcelableExtra(EXTRA_JOB);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onHandleIntent(Intent intent, int retry, int delayAmount) {
		Job job = getJob(intent);
		if (job == null) {
			Log.w(TAG, "Received job is empty");
			return true;
		}
		
		try {
			@SuppressWarnings("rawtypes")
			Worker worker = getWorker(job);
			setCurrentWorker(worker);
			return worker.process(job, retry, delayAmount, (ResultReceiver)intent.getParcelableExtra(EXTRA_RECEIVER), this, getHandler());
		} catch (Exception exp) {
			Log.e(TAG, exp.getMessage() + "", exp);
			return true;
		} finally {
			setCurrentWorker(null);
		}
	}
	
	public static void invoke(Context context, Job job) {
		invoke(WorkerService.class, context, job, null);
	}
		
	public static void invoke(Context context, Job job, ResultReceiver receiver) {
		invoke(WorkerService.class, context, job, receiver);
	}
	
	protected static void invoke(Class<? extends WorkerService> clazz, Context context, Job job, ResultReceiver receiver) {
		Intent intent = new Intent();
		buildIntent(clazz, intent, context, job, receiver);
		context.startService(intent);
	}
	
	public static void buildIntent(Intent intent, Context context, Job job, ResultReceiver receiver) {
		buildIntent(WorkerService.class, intent, context, job, receiver);
	}
	
	protected static void buildIntent(Class<? extends WorkerService> clazz, Intent intent, Context context, Job job, ResultReceiver receiver) {
		intent.setClass(context, clazz);
		intent.putExtra(EXTRA_JOB, job);
		if (receiver != null)
			intent.putExtra(EXTRA_RECEIVER, receiver);
	}
}
