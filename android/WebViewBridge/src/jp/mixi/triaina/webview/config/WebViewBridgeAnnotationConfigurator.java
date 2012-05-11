package jp.mixi.triaina.webview.config;

import java.lang.reflect.Method;

import com.google.inject.Inject;

import jp.mixi.triaina.commons.exception.InvalidConfigurationRuntimeException;
import jp.mixi.triaina.commons.utils.ClassUtils;
import jp.mixi.triaina.webview.Callback;
import jp.mixi.triaina.webview.InjectorHelper;
import jp.mixi.triaina.webview.WebViewBridge;
import jp.mixi.triaina.webview.annotation.Bridge;
import jp.mixi.triaina.webview.annotation.Domain;
import jp.mixi.triaina.webview.annotation.Layout;
import jp.mixi.triaina.webview.bridges.NetHttpSendBridge;
import jp.mixi.triaina.webview.bridges.SensorAccelerometerBridge;
import jp.mixi.triaina.webview.bridges.VibratorBridge;
import jp.mixi.triaina.webview.bridges.WebStatusBridge;
import jp.mixi.triaina.webview.bridges.WiFiBridge;
import jp.mixi.triaina.webview.bridges.WiFiP2PBridge;
import jp.mixi.triaina.webview.entity.Params;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;

public class WebViewBridgeAnnotationConfigurator implements WebViewBridgeConfigurator {
    private static final String TAG = "WebViewBridgeAnnotationConfigurator";

    @Inject
    private ConfigCache mConfigCache;

    @Inject
    private InjectorHelper mInjectorHelper;
    
    @Inject
    private Context mContext;

    public WebViewBridge loadWebViewBridge(Activity activity) {
        Class<?> clazz = activity.getClass();
        LayoutConfig config = mConfigCache.getLayoutConfig(clazz);

        if (config == null) {
            Log.d(TAG, "Miss cache layout config");

            Layout layoutAnn = (Layout) clazz.getAnnotation(Layout.class);
            if (layoutAnn == null) {
                throw new InvalidConfigurationRuntimeException("Must be defined as use layout");
            }

            jp.mixi.triaina.webview.annotation.WebViewBridge bridgeAnn = (jp.mixi.triaina.webview.annotation.WebViewBridge) clazz
                    .getAnnotation(jp.mixi.triaina.webview.annotation.WebViewBridge.class);
            if (bridgeAnn == null)
                throw new InvalidConfigurationRuntimeException("Must be defined as use layout");

            config = new LayoutConfig(layoutAnn.value(), bridgeAnn.value());
            mConfigCache.putLayoutConfig(clazz, config);
        } else
            Log.d(TAG, "Layout config from cache");

        activity.setContentView(config.getLayoutId());
        WebViewBridge webViewBridge = (WebViewBridge) activity.findViewById(config.getWebViewBridgeId());
        webViewBridge.setDomainConfig(createDomainConfig(clazz));

        return webViewBridge;
    }

    private DomainConfig createDomainConfig(Class<?> clazz) {
        DomainConfig config = mConfigCache.getDomainConfig(clazz);

        if (config == null) {
            Domain domainAnn = (Domain) clazz.getAnnotation(Domain.class);
            if (domainAnn == null)
                throw new InvalidConfigurationRuntimeException("Must be defined as access domain");

            config = new DomainConfig(domainAnn.value());
        } else
            Log.d(TAG, "Domain config from cache");

        return config;
    }

    public void configure(WebViewBridge bridge) {
        configure(bridge, mContext);
        configure(bridge, new WebStatusBridge(bridge));
        configure(bridge, new NetHttpSendBridge(bridge));
        configure(bridge, new WiFiBridge(bridge));
        configure(bridge, new VibratorBridge());
        configure(bridge, new SensorAccelerometerBridge(bridge));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            configure(bridge, new WiFiP2PBridge(bridge));
    }

    public void configure(WebViewBridge webViewBridge, Object bridgeObject) {
        bridgeObject = mInjectorHelper.inject(mContext, bridgeObject);
        
        Class<?> clazz = bridgeObject.getClass();
        BridgeObjectConfig config = mConfigCache.getBridgeObjectConfig(clazz);

        if (config == null) {
            Log.d(TAG, "Miss cache bridge config");

            config = createBridgeConfig(bridgeObject);
            mConfigCache.putBridgeObjectConfig(clazz, config);
        } else
            Log.d(TAG, "Bridge config from cache");

        webViewBridge.addBridgeObjectConfig(bridgeObject, config);
    }

    private BridgeObjectConfig createBridgeConfig(Object bridgeObject) {
        Class<?> clazz = bridgeObject.getClass();

        Method[] methods = clazz.getMethods();
        BridgeObjectConfig configSet = new BridgeObjectConfig();

        for (Method method : methods) {
            Bridge chAnn = method.getAnnotation(Bridge.class);
            if (chAnn != null) {
                if (validDestinationMethod(method)) {
                    Log.d(TAG, "dest:" + method.getName());
                    BridgeMethodConfig config = new BridgeMethodConfig(chAnn.value(), method);
                    configSet.add(config);
                }
            }
        }

        return configSet;
    }

    private boolean validDestinationMethod(Method method) {
        Class<?>[] argTypes = method.getParameterTypes();

        switch (argTypes.length) {
        case 2:
            if (!ClassUtils.isImplement(argTypes[1], Callback.class))
                return false;
        case 1:
            if (!ClassUtils.isImplement(argTypes[0], Params.class)
                    && !ClassUtils.isImplement(argTypes[0], Callback.class))
                return false;
        case 0:
            break;
        default:
            Log.w(TAG, "dest:" + method.getName() + " has illegal arguments");
            return false;
        }

        return true;
    }

    //for test
    public void setInjectorHelper(InjectorHelper injectorHelper) {
        mInjectorHelper = injectorHelper;
    }
    
    //for test
    public void setContext(Context context) {
        mContext = context;
    }
    
    //for test
    public void setConfigCache(ConfigCache configCache) {
        mConfigCache = configCache;
    }
}
