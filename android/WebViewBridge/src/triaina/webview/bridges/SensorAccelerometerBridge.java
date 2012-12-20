package triaina.webview.bridges;

import java.util.List;

import triaina.webview.WebViewBridge;
import triaina.webview.annotation.Bridge;
import triaina.webview.entity.device.SensorAccelerometerEnableParams;
import triaina.webview.entity.web.AccelerometerParams;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.google.inject.Inject;

public class SensorAccelerometerBridge implements BridgeObject {
    @Inject
    private SensorManager mSensorManager;

    private AccelerometerListener mListener;

    private SensorAccelerometerEnableParams mParams;

    private WebViewBridge mBridge;

    public SensorAccelerometerBridge(WebViewBridge bridge) {
        mBridge = bridge;
    }

    @Bridge("system.sensor.accelerometer.enable")
    public void doEnable(SensorAccelerometerEnableParams params) {
        synchronized (this) {
            if (mParams != null)
                return;

            enableInternal(mBridge, params);
            mParams = params;
        }
    }

    @Bridge("system.sensor.accelerometer.disable")
    public void doDisable() {
        synchronized (this) {
            if (mParams != null) {
                disableInternal();
                mParams = null;
            }
        }
    }

    private void enableInternal(WebViewBridge bridge, SensorAccelerometerEnableParams params) {
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
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
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mBridge.call(mDest, new AccelerometerParams((double) event.values[0], (double) event.values[1],
                        (double) event.values[2]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    @Override
    public void onResume() {
        synchronized (this) {
            if (mParams == null)
                return;

            enableInternal(mBridge, mParams);
        }
    }

    @Override
    public void onPause() {
        synchronized (this) {
            if (mParams != null)
                disableInternal();
        }
    }

    @Override
    public void onDestroy() {
    }
}
