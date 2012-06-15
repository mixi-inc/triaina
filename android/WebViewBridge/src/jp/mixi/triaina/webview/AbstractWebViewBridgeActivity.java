package jp.mixi.triaina.webview;

import java.io.File;

import com.google.inject.Inject;

import jp.mixi.triaina.commons.utils.SystemUtils;
import jp.mixi.triaina.injector.TriainaEnvironment;
import jp.mixi.triaina.injector.activity.TriainaActivity;
import jp.mixi.triaina.webview.annotation.Bridge;
import jp.mixi.triaina.webview.config.WebViewBridgeConfigurator;
import jp.mixi.triaina.webview.entity.Params;
import jp.mixi.triaina.webview.entity.device.EnvironmentSetParams;
import jp.mixi.triaina.webview.entity.device.FormPictureSelectParams;
import jp.mixi.triaina.webview.entity.device.FormPictureSelectResult;
import jp.mixi.triaina.webview.entity.device.NetBrowserOpenParams;
import jp.mixi.triaina.webview.entity.device.NewWindowParams;
import jp.mixi.triaina.webview.entity.device.NotificationParams;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.widget.Toast;

public abstract class AbstractWebViewBridgeActivity extends TriainaActivity {
    private static final String TAG = "AbsctactWebViewBridgeActivity";

    private static final String EXTRA_URL = "_url";

    private WebViewBridge mWebViewBridge;

    @Inject
    private TriainaEnvironment mEnvironment;

    @Inject
    private WebViewBridgeConfigurator mConfigurator;

    @Inject
    private ProgressManager mProgressManager;
    
    
    final public String[] getDomains() {
        return mWebViewBridge.getDomainConfig().getDomains();
    }

    public void call(String dest, Params params) {
        mWebViewBridge.call(dest, params);
    }

    public void call(String dest, Params params, Callback<?> callback) {
        mWebViewBridge.call(dest, params, callback);
    }

    final public WebViewBridge getWebViewBridge() {
        return mWebViewBridge;
    }

    final public WebViewBridgeConfigurator getConfigurator() {
        return mConfigurator;
    }

    @Bridge("system.environment.set")
    public void doEnvironmentSet(EnvironmentSetParams params) {
        mEnvironment.set(params.getName(), params.getValue());
    }

    @Bridge("system.notification.notify")
    public void doNotificationNotify(NotificationParams params) {
        Toast.makeText(this, params.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Bridge("system.form.picture.select")
    public void doFormPictureSelect(FormPictureSelectParams params, Callback<FormPictureSelectResult> callback) {
        // TODO need to implement Triaina Framework original logic
    }

    @Bridge("system.window.open")
    public void doOpenNewWindow(NewWindowParams params) {
        startActivityForResult(this, getClass(), params.getUrl());
    }
    
    @Bridge("system.net.browser.open")
    public void doNetBrowserOpen(NetBrowserOpenParams params) {
        SystemUtils.launchExternalBrowser(this, Uri.parse(params.getUrl()), getClass());
    }

    @Bridge("system.web.error")
    public void doWebError() {
        // ignore
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebViewBridge = mConfigurator.loadWebViewBridge(this);
        mConfigurator.configure(mWebViewBridge);

        WebSettings settings = getWebViewBridge().getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        File databasePath = new File(getCacheDir(), "webstorage");
        settings.setDatabasePath(databasePath.toString());

        // getWebViewBridge().clearCache(true);
        getWebViewBridge().setWebChromeClient(new TriainaWebChromeClient(mProgressManager));
        getWebViewBridge().setWebViewClient(new TriainaWebViewClient(mProgressManager));

        if (!restoreWebView(savedInstanceState)) {
            String url = getIntent().getStringExtra(EXTRA_URL);
            getWebViewBridge().loadUrl(url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && getWebViewBridge().canGoBack()) {
            getWebViewBridge().goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static final String EXTRA_WEBVIEW_PICTURE = "_picture";
    private static final String EXTRA_WEBVIEW_ID = "_webview_id";

    private static final String EXTRA_WEBVIEW_STATE = "_webview_state";
    private static final String WEBVIEW_TMP_FILE = "webview_tmp";

    @SuppressWarnings("deprecation")
    private boolean restoreWebView(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            return false;

        try {
            Bundle stateBundle = savedInstanceState.getBundle(EXTRA_WEBVIEW_STATE);
            if (stateBundle == null || stateBundle.size() == 0 || getWebViewBridge().restoreState(stateBundle) == null) {
                Log.w(TAG, "failed to restore webview state");
                return false;
            }
            if (Build.VERSION.SDK_INT < 11) {
                Bundle pictureBundle = savedInstanceState.getBundle(EXTRA_WEBVIEW_PICTURE);

                if (pictureBundle == null) {
                    Log.w(TAG, "failed to restore webview picture state.");
                    return false;
                }

                long id = savedInstanceState.getLong(EXTRA_WEBVIEW_ID);
                File tmp = new File(getCacheDir(), WEBVIEW_TMP_FILE + "." + id);
                if (!getWebViewBridge().restorePicture(pictureBundle, tmp)) {
                    Log.w(TAG, "failed to restore webview picture state");
                    return false;
                }
            }
        } catch (NullPointerException e) {
            // sometimes, especially repeating fast orientation
            // changes, WebView#restoreState() throws NPE.
            // Just ignore here.
            Log.w(TAG, "NullPointerException thrown during restoreState()");
            return false;
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle stateBundle = new Bundle();
        if (getWebViewBridge().saveState(stateBundle) == null) {
            Log.w(TAG, "failed to save webview state");
            return;
        }

        long id = getTaskId();
        Bundle webViewPictureBundle = new Bundle();
        File tmp = new File(getCacheDir(), WEBVIEW_TMP_FILE + "." + id);
        if (!getWebViewBridge().savePicture(webViewPictureBundle, tmp))
            Log.w(TAG, "failed to save webview picture");

        outState.putBundle(EXTRA_WEBVIEW_STATE, stateBundle);
        outState.putLong(EXTRA_WEBVIEW_ID, id);
        outState.putBundle(EXTRA_WEBVIEW_PICTURE, webViewPictureBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebViewBridge.resume();
    }

    @Override
    protected void onPause() {
        mWebViewBridge.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebViewBridge.destroy();
        super.onDestroy();
    }

    public static void startActivityForResult(Activity activity, Class<?> clazz, String url) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtra(EXTRA_URL, url);
        activity.startActivityForResult(intent, 0);
    }
}
