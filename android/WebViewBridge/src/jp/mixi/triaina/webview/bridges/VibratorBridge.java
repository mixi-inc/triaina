package jp.mixi.triaina.webview.bridges;

import com.google.inject.Inject;

import android.os.Vibrator;
import jp.mixi.triaina.commons.utils.ArrayUtils;
import jp.mixi.triaina.webview.annotation.Bridge;
import jp.mixi.triaina.webview.entity.device.VibratorVibrateParams;

public class VibratorBridge implements BridgeObject {
	@Inject
	private Vibrator mVibrator;

	@Bridge("system.vibrator.vibrate")
	public void doVibrate(VibratorVibrateParams params) {
		Integer r = params.getRepeat();
		if (r == null)
			mVibrator.vibrate(params.getMsec());
		else
			mVibrator.vibrate(ArrayUtils.convert(params.getPattern()), r == null ? -1 : r.intValue());
	}

	@Bridge("system.vibrator.cancel")
	public void doCancel() {
		mVibrator.cancel();
	}

	@Override
	public void onResume() {
	}

	@Override
	public void onPause() {
		doCancel();
	}

	@Override
	public void onDestroy() {
	}
}
