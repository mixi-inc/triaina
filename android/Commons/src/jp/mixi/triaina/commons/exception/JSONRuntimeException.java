package jp.mixi.triaina.commons.exception;

public class JSONRuntimeException extends CommonRuntimeException {

	private static final long serialVersionUID = -7644664508945399067L;

	public JSONRuntimeException() {
		super();
	}

	public JSONRuntimeException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public JSONRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public JSONRuntimeException(Throwable throwable) {
		super(throwable);
	}
}
