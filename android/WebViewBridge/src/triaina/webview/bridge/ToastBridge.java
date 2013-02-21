package triaina.webview.bridge;

import javax.inject.Inject;

import android.content.Context;
import android.widget.Toast;
import triaina.webview.annotation.Bridge;
import triaina.webview.entity.device.ToastParams;

public class ToastBridge implements BridgeLifecyclable {
    @Inject
    private Context mContext;

    private Toast mLastToast;

    @Bridge("system.toast.show")
    public void showToast(ToastParams params) {
        Integer d = params.getDuration();
        if (d == null)
            d = Toast.LENGTH_SHORT;

        mLastToast = Toast.makeText(mContext, params.getText(), d);
        mLastToast.show();
    }

    @Bridge("system.toast.cancel")
    public void cancel() {
        if (mLastToast == null)
            return;

        mLastToast.cancel();
        mLastToast = null;
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
        cancel();
    }
}
