package jp.mixi.triaina.commons.exception;

public class SecurityRuntimeException extends CommonRuntimeException {

	private static final long serialVersionUID = 6244026068714272574L;

	public SecurityRuntimeException() {
		super();
	}

	public SecurityRuntimeException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public SecurityRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public SecurityRuntimeException(Throwable throwable) {
		super(throwable);
	}
}
