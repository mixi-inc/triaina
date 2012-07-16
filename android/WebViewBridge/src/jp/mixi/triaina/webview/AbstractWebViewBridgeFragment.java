package jp.mixi.triaina.webview;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.inject.Inject;

import jp.mixi.triaina.commons.utils.SystemUtils;
import jp.mixi.triaina.injector.TriainaEnvironment;
import jp.mixi.triaina.injector.fragment.AbstractTriainaFragment;
import jp.mixi.triaina.webview.annotation.Bridge;
import jp.mixi.triaina.webview.config.WebViewBridgeConfigurator;
import jp.mixi.triaina.webview.entity.Params;
import jp.mixi.triaina.webview.entity.device.EnvironmentSetParams;
import jp.mixi.triaina.webview.entity.device.FormPictureSelectParams;
import jp.mixi.triaina.webview.entity.device.FormPictureSelectResult;
import jp.mixi.triaina.webview.entity.device.NetBrowserOpenParams;
import jp.mixi.triaina.webview.entity.device.NotificationParams;

public abstract class AbstractWebViewBridgeFragment extends
		AbstractTriainaFragment {
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

	@Bridge("system.notification.notify")
	public void doNotificationNotify(NotificationParams params) {
		Toast.makeText(getActivity(), params.getMessage(), Toast.LENGTH_SHORT).show();
	}

	@Bridge("system.form.picture.select")
	public void doFormPictureSelect(FormPictureSelectParams params,
			Callback<FormPictureSelectResult> callback) {
		// TODO need to implement Triaina Framework original logic
	}

	@Bridge("system.net.browser.open")
	public void doNetBrowserOpen(NetBrowserOpenParams params) {
		SystemUtils.launchExternalBrowser(getActivity(),
				Uri.parse(params.getUrl()), getClass());
	}

	@Bridge("system.web.error")
	public void doWebError() {
		// ignore
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflatedView =
				mConfigurator.loadInflatedView(this, inflater, container);
		mWebViewBridge = mConfigurator.loadWebViewBridge(this, inflatedView);
		mConfigurator.configure(mWebViewBridge);
		mConfigurator.configureSetting(mWebViewBridge);
		setClients();
		return inflatedView;
	}
	
	protected void setClients() {
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
		//XXX
		if (mWebViewBridge != null)
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
		super.onDestroyView();
	}
	
	public boolean isRestored() {
		return mIsRestored;
	}
}
