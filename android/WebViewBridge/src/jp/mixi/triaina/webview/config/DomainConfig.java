package jp.mixi.triaina.webview.config;

public class DomainConfig {
    private String[] mDomains;
 
    public DomainConfig(String[] domains) {
        mDomains = domains;
    }
    
    public String[] getDomains() {
        return mDomains;
    }
}
