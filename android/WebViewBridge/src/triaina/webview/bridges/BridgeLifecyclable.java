package triaina.webview.bridges;

public interface BridgeLifecyclable {
    public void onResume();
    
    public void onPause();
    
    public void onDestroy();
}
