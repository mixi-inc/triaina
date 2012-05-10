package jp.mixi.triaina.commons.exception;

public class CalledFromWrongThreadRuntimeException extends CommonRuntimeException {
	private static final long serialVersionUID = -7769875177893783855L;

	
	public CalledFromWrongThreadRuntimeException() {
		super();
	}

	public CalledFromWrongThreadRuntimeException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public CalledFromWrongThreadRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public CalledFromWrongThreadRuntimeException(Throwable throwable) {
		super(throwable);
	}
}
