package triaina.webview;

import com.google.inject.Inject;

import triaina.commons.utils.SystemUtils;
import triaina.injector.TriainaEnvironment;
import triaina.injector.activity.TriainaActivity;
import triaina.webview.annotation.Bridge;
import triaina.webview.config.WebViewBridgeConfigurator;
import triaina.webview.entity.Params;
import triaina.webview.entity.device.EnvironmentSetParams;
import triaina.webview.entity.device.FormPictureSelectParams;
import triaina.webview.entity.device.FormPictureSelectResult;
import triaina.webview.entity.device.NetBrowserOpenParams;
import triaina.webview.entity.device.NewWindowParams;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public abstract class AbstractWebViewBridgeActivity extends TriainaActivity {
    private static final String TAG = AbstractWebViewBridgeActivity.class.getSimpleName();

    public static final String EXTRA_URL = "_url";

    private WebViewBridge mWebViewBridge;

    @Inject
    private WebViewBridgeConfigurator mConfigurator;
    
	@Inject
	private WebViewRestoreManager mRestoreManager;
    
    @Inject
    private TriainaEnvironment mEnvironment;

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
        SystemUtils.launchExternalBrowser(this, Uri.parse(params.getUrl()));
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
        configureSettings();
        configureClients();
        loadViews();
        loadUrl(savedInstanceState);
    }
    
    protected void configureSettings() {
        mConfigurator.configureSetting(mWebViewBridge);
    }

    protected void configureClients() {
        mWebViewBridge.setWebChromeClient(new TriainaWebChromeClient(this));
        mWebViewBridge.setWebViewClient(new TriainaWebViewClient());
    }

    protected void loadViews() {
    }

    protected void loadUrl(Bundle savedInstanceState) {
        if (mRestoreManager.restoreWebView(mWebViewBridge, savedInstanceState))
            return;
        String url = getIntent().getStringExtra(EXTRA_URL);
        getWebViewBridge().loadUrl(url);        
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && getWebViewBridge().canGoBack()) {
            getWebViewBridge().goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        storeWebView(outState);
    }
    
    protected void storeWebView(Bundle outState) {
        mRestoreManager.storeWebView(mWebViewBridge, outState);
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
        try {
            mWebViewBridge.destroy();
        } catch (Exception exp) {
            Log.w(TAG, exp.getMessage() + "", exp);
        }
        super.onDestroy();
    }

    public static void startActivityForResult(Activity activity, Class<?> clazz, String url) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtra(EXTRA_URL, url);
        activity.startActivityForResult(intent, 0);
    }
}
