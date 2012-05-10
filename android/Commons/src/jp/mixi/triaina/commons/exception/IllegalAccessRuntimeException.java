package jp.mixi.triaina.commons.exception;

public class IllegalAccessRuntimeException extends CommonRuntimeException {

	private static final long serialVersionUID = -7193385130846231432L;

	public IllegalAccessRuntimeException() {
		super();
	}

	public IllegalAccessRuntimeException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public IllegalAccessRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public IllegalAccessRuntimeException(Throwable throwable) {
		super(throwable);
	}
}
