package triaina.commons.exception;

public class IORuntimeException extends CommonRuntimeException {
	private static final long serialVersionUID = -8000893106100281337L;

	public IORuntimeException() {
	}

	public IORuntimeException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public IORuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public IORuntimeException(Throwable throwable) {
		super(throwable);
	}
}
