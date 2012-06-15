package jp.mixi.triaina.webview;

import com.google.inject.Inject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class ProgressManager {
    private ProgressDialog mProgressDialog;

    private boolean mIsProgress;
    
    @Inject
    private Context mContext;
    
    @Inject
    private Handler mHandler;
    
    public void showProgress(int newProgress) {
        if (mIsProgress)
            return;

        mIsProgress = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog == null)
                    mProgressDialog = ProgressDialog.show(mContext, "", "Loading. Please wait...", true);
            }
        });
    }
    
    public void hideProgress() {
        if (!mIsProgress)
            return;

        mIsProgress = false;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) {
                    mProgressDialog.hide();
                    mProgressDialog = null;
                }
            }
        });
    }
    
    public boolean isProgress() {
        return mIsProgress;
    }
}
