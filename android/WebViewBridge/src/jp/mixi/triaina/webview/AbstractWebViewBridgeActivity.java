package jp.mixi.triaina.webview;

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
import jp.mixi.triaina.webview.entity.device.NotificationParams;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public abstract class AbstractWebViewBridgeActivity extends TriainaActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AbsctactWebViewBridgeActivity";

    private WebViewBridge mWebViewBridge;

    @Inject
    private TriainaEnvironment mEnvironment;

    @Inject
    private WebViewBridgeConfigurator mConfigurator;

    
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
}
