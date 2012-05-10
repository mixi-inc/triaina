package jp.mixi.triaina.injector;

import com.google.inject.Stage;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class TriainaApplication extends Application {
	private static final String TAG = "TriainaApplication";
	
	@Override
	public void onCreate() {
		super.onCreate();
		try {
			TriainaInjectorFactory.setBaseApplicationInjector(this, Stage.PRODUCTION);
		} catch (Exception exp) {
			Log.e(TAG, exp.getMessage() + "", exp);
		}
	}
	
	public TriainaInjector getInjector(Context context) {
		return TriainaInjectorFactory.getInjector(context);
	}
	
	public void destroyInjector(Context context) {
		TriainaInjectorFactory.destroyInjector(context);
	}
}
