package triaina.commons.exception;

public class NotFoundRuntimeException extends CommonRuntimeException {

	private static final long serialVersionUID = 5486025835429632952L;

	public NotFoundRuntimeException() {
		super();
	}

	public NotFoundRuntimeException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NotFoundRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public NotFoundRuntimeException(Throwable throwable) {
		super(throwable);
	}
}
