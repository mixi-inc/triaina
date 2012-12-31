package triaina.webview;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;

import triaina.commons.utils.SystemUtils;
import triaina.injector.TriainaEnvironment;
import triaina.injector.fragment.TriainaFragment;
import triaina.webview.annotation.Bridge;
import triaina.webview.config.WebViewBridgeConfigurator;
import triaina.webview.entity.Params;
import triaina.webview.entity.device.EnvironmentSetParams;
import triaina.webview.entity.device.FormPictureSelectParams;
import triaina.webview.entity.device.FormPictureSelectResult;
import triaina.webview.entity.device.NetBrowserOpenParams;

public abstract class AbstractWebViewBridgeFragment extends TriainaFragment {
    private static final String TAG = AbstractWebViewBridgeFragment.class.getSimpleName();

    private WebViewBridge mWebViewBridge;

    @Inject
    private WebViewBridgeConfigurator mConfigurator;

    @Inject
    private WebViewRestoreManager mRestoreManager;

    @Inject
    private TriainaEnvironment mEnvironment;

    private boolean mIsRestored;

    final public String[] getDomains() {
        return mWebViewBridge.getDomainConfig().getDomains();
    }

    public void call(String channel, Params params) {
        mWebViewBridge.call(channel, params);
    }

    public void call(String channel, Params params, Callback<?> callback) {
        mWebViewBridge.call(channel, params, callback);
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

    @Bridge("system.net.browser.open")
    public void doNetBrowserOpen(NetBrowserOpenParams params) {
        SystemUtils.launchExternalBrowser(getActivity(), Uri.parse(params.getUrl()));
    }

    @Bridge("system.web.error")
    public void doWebError() {
        // ignore
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = mConfigurator.loadInflatedView(this, inflater, container);
        mWebViewBridge = mConfigurator.loadWebViewBridge(this, inflatedView);
        mConfigurator.configure(mWebViewBridge);
        mConfigurator.configure(mWebViewBridge, this);
        configureSettings();
        configureClients();
        return inflatedView;
    }

    protected void configureSettings() {
        mConfigurator.configureSetting(mWebViewBridge);
    }

    protected void configureClients() {
        mWebViewBridge.setWebChromeClient(new TriainaWebChromeClient(getActivity()));
        mWebViewBridge.setWebViewClient(new TriainaWebViewClient());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsRestored = mRestoreManager.restoreWebView(mWebViewBridge, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // XXX
        if (mWebViewBridge != null)
            storeWebView(outState);
    }

    protected void storeWebView(Bundle outState) {
        mRestoreManager.storeWebView(mWebViewBridge, outState, getActivity().getTaskId());
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebViewBridge.resume();
    }

    @Override
    public void onPause() {
        mWebViewBridge.pause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        try {
            mWebViewBridge.destroy();
        } catch (Exception exp) {
            Log.w(TAG, exp.getMessage() + "", exp);
        }
        super.onDestroyView();
    }

    public boolean isRestored() {
        return mIsRestored;
    }
}
