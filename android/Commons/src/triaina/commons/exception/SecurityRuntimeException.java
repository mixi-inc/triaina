package triaina.commons.exception;

public class SecurityRuntimeException extends CommonRuntimeException {

	private static final long serialVersionUID = 6244026068714272574L;

	private String mUrl;

	public SecurityRuntimeException() {
		super();
	}

	public SecurityRuntimeException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public SecurityRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public SecurityRuntimeException(String detailMessage, String url) {
		this(detailMessage);
		mUrl = url;
	}

	public SecurityRuntimeException(Throwable throwable) {
		super(throwable);
	}

	public SecurityRuntimeException(Throwable throwable, String url) {
		this(throwable);
		mUrl = url;
	}

	public String getUrl(){
		return mUrl;
	}
}
