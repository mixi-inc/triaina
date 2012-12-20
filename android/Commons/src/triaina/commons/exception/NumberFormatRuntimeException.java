package triaina.commons.exception;

public class NumberFormatRuntimeException extends CommonRuntimeException {

	private static final long serialVersionUID = 6079998109440925976L;

	public NumberFormatRuntimeException() {
		super();
	}

	public NumberFormatRuntimeException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NumberFormatRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public NumberFormatRuntimeException(Throwable throwable) {
		super(throwable);
	}
}
