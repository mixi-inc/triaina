package triaina.commons.exception;

public class InvocationRuntimeException extends CommonRuntimeException {

	private static final long serialVersionUID = 591237050719624525L;

	public InvocationRuntimeException() {
		super();
	}

	public InvocationRuntimeException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public InvocationRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public InvocationRuntimeException(Throwable throwable) {
		super(throwable);
	}
}
