package jp.mixi.triaina.webview.logic;

import java.util.List;

import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.webview.entity.device.SensorAccelerometerEnableParams;
import jp.mixi.triaina.webview.entity.web.AccelerometerParams;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.google.inject.Inject;

public class SensorAccelerometerLogic {
	@Inject
	private SensorManager mSensorManager;
	
	private AccelerometerListener mListener;
	
	private SensorAccelerometerEnableParams mParams;
	
	public void enable(WebViewBridge bridge, SensorAccelerometerEnableParams params) {
		synchronized (this) {
			if (mParams != null)
				return;
			
			enableInternal(bridge, params);
			mParams = params;	
		}
	}
	
	public void disable() {
		synchronized (this) {
			if (mParams != null) {
				disableInternal();
				mParams = null;
			}
		}
	}
	
	public void pause() {
		synchronized (this) {
			if (mParams != null)
				disableInternal();
		}
	}
	
	public void resume(WebViewBridge bridge) {
		synchronized (this) {
			if (mParams == null)
				return;
			
			enableInternal(bridge, mParams);	
		}
	}
	
	private void enableInternal(WebViewBridge bridge, SensorAccelerometerEnableParams params) {
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if(sensors.size() > 0) {
			Sensor sensor = sensors.get(0);
			mListener = new AccelerometerListener(bridge, params.getCallback());		
			mSensorManager.registerListener(mListener, sensor, params.getRate());
		}
	}
	
	private void disableInternal() {
		mSensorManager.unregisterListener(mListener);
		mListener = null;
	}
			
	protected static class AccelerometerListener implements SensorEventListener {
		private WebViewBridge mBridge;
		private String mDest;
		
		public AccelerometerListener(WebViewBridge bridge, String dest) {
			mBridge = bridge;
			mDest = dest;
		}
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
				mBridge.call(mDest, new AccelerometerParams((double)event.values[0], (double)event.values[1], (double)event.values[2]));
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}		
	}
}
