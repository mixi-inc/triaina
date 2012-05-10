package jp.mixi.triaina.webview.logic;

import com.google.inject.Inject;

import android.os.Vibrator;
import jp.mixi.triaina.commons.utils.ArrayUtils;
import jp.mixi.triaina.webview.entity.device.VibratorVibrateParams;

public class VibratorLogic {
	@Inject
	private Vibrator mVibrator;
	
	public void invoke(VibratorVibrateParams params) {
		Integer r = params.getRepeat();
		mVibrator.vibrate(ArrayUtils.convert(params.getPattern()), r == null ? -1 : r.intValue());
	}
	
	public void cancel() {
		mVibrator.cancel();
	}
}
