package jp.mixi.triaina.commons.exception;

public class CommonException extends Exception {

	private static final long serialVersionUID = 1898354321722507010L;

	public CommonException() {
		super();
	}

	public CommonException(String detailMessage) {
		super(detailMessage);
	}

	public CommonException(Throwable throwable) {
		super(throwable);
	}

	public CommonException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
}
