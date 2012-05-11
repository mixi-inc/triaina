package jp.mixi.triaina.webview.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BridgeObjectConfig {
    private Map<String, BridgeMethodConfig> mMap = new HashMap<String, BridgeMethodConfig>();
    
    public void add(BridgeMethodConfig config) {
        mMap.put(config.getDest(), config);
    }
    
    public void add(BridgeObjectConfig config) {
        mMap.putAll(config.mMap);
    }
    
    public BridgeMethodConfig get(String name) {
        return mMap.get(name);
    }
    
    public Collection<BridgeMethodConfig> getMethodConfigs() {
        return mMap.values();
    }
}
