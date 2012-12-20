package triaina.commons.exception;

public class InstantiationRuntimeException extends CommonRuntimeException {

	private static final long serialVersionUID = 1524336611876835L;

	public InstantiationRuntimeException() {
		super();
	}

	public InstantiationRuntimeException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public InstantiationRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public InstantiationRuntimeException(Throwable throwable) {
		super(throwable);
	}
}
