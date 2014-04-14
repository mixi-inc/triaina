package triaina.test.mock;

import triaina.webview.annotation.Bridge;

public class MockContext extends android.test.mock.MockContext {
	public MockParams params;
	
	@Bridge("notify")
	public void notifyFunc(MockParams params) {
		this.params = params;
	}
}
