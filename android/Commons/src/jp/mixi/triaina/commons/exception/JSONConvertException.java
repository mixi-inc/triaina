package jp.mixi.triaina.commons.exception;

public class JSONConvertException extends CommonException {

	private static final long serialVersionUID = -4144211665410970530L;

	public JSONConvertException() {
		super();
	}

	public JSONConvertException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public JSONConvertException(String detailMessage) {
		super(detailMessage);
	}

	public JSONConvertException(Throwable throwable) {
		super(throwable);
	}
}
