package jp.mixi.triaina.commons.exception;

public class CommonRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -5412111776425419939L;

	public CommonRuntimeException() {
		super();
	}

	public CommonRuntimeException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public CommonRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public CommonRuntimeException(Throwable throwable) {
		super(throwable);
	}
}
