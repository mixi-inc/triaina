package jp.mixi.triaina.commons.exception;

public class IllegalArgumentRuntimeException extends CommonRuntimeException {

	private static final long serialVersionUID = 2739273151772907542L;

	public IllegalArgumentRuntimeException() {
		super();
	}

	public IllegalArgumentRuntimeException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public IllegalArgumentRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public IllegalArgumentRuntimeException(Throwable throwable) {
		super(throwable);
	}
}
