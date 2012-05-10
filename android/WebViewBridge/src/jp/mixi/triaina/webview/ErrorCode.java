package jp.mixi.triaina.webview;

public enum ErrorCode {
	NOT_FOUND_BRIDGE_ERROR("404"),
	JSON_PARSE_ERROR("405"),
	INVOCATION_BRIDGE_ERROR("406"),
	
	UNKNOWN_ERROR("500"),
	NETWORK_ERROR("501"),
	RESOURCE_NOT_FOUND_ERROR("502"),
	NOT_SUPPORT_ERROR("503"),
	SECURITY_ERROR("504");
	
	private String mCode;

	private ErrorCode(String code) {
		mCode = code;
	}
	
	public String getCode() {
		return mCode;
	}
}
