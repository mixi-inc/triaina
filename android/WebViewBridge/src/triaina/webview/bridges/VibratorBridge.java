package triaina.webview.bridges;

import javax.inject.Inject;

import android.content.Context;
import android.os.Vibrator;
import triaina.commons.utils.ArrayUtils;
import triaina.webview.annotation.Bridge;
import triaina.webview.entity.device.VibrateParams;

public class VibratorBridge implements BridgeLifecyclable {
    @Inject
    private Context mContext;

    private boolean mIsEnable;

    @Bridge("system.vibrator.vibrate")
    public void vibrate(VibrateParams params) {
        Vibrator vibrator = getVibrator();
        if (vibrator == null)
            return;

        Integer r = params.getRepeat();
        try {
            if (r == null)
                vibrator.vibrate(params.getMsec());
            else
                vibrator.vibrate(ArrayUtils.convert(params.getPattern()), r == null ? -1 : r.intValue());
        } finally {
            mIsEnable = true;
        }
    }

    @Bridge("system.vibrator.cancel")
    public void cancel() {
        if (!mIsEnable)
            return;
        try {
            getVibrator().cancel();
        } finally {
            mIsEnable = false;
        }
    }

    protected Vibrator getVibrator() {
        return (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
        cancel();
    }

    @Override
    public void onDestroy() {
    }
}
