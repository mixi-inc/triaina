package triaina.webview.exception;

import triaina.commons.exception.CommonRuntimeException;

public class SkipDomainCheckRuntimeException extends CommonRuntimeException {

	private static final long serialVersionUID = -7971976610462355519L;

	public SkipDomainCheckRuntimeException() {
		super();
	}

	public SkipDomainCheckRuntimeException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public SkipDomainCheckRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public SkipDomainCheckRuntimeException(Throwable throwable) {
		super(throwable);
	}
}
