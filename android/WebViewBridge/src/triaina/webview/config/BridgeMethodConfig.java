package triaina.webview.config;

import java.lang.reflect.Method;

public class BridgeMethodConfig {
    private String mDest;
    private Method mMethod;
    
    public BridgeMethodConfig(String dest, Method method) {
        mDest = dest;
        mMethod = method;
    }
    
    public String getDest() {
        return mDest;
    }
    
    public Method getMethod() {
        return mMethod;
    }
}
