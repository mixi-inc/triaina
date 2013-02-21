package triaina.webview.bridge;

public interface BridgeLifecyclable {
    public void onResume();
    
    public void onPause();
    
    public void onDestroy();
}
