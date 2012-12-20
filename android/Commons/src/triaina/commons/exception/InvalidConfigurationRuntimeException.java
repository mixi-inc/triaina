package triaina.commons.exception;

public class InvalidConfigurationRuntimeException extends CommonRuntimeException {
	private static final long serialVersionUID = -5991964542570870077L;

	public InvalidConfigurationRuntimeException() {
		super();
	}

	public InvalidConfigurationRuntimeException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public InvalidConfigurationRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public InvalidConfigurationRuntimeException(Throwable throwable) {
		super(throwable);
	}
}
