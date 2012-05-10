package jp.mixi.triaina.commons.exception;

public class UnsupportedRuntimeException extends CommonRuntimeException {

    private static final long serialVersionUID = -490517655999458168L;

    public UnsupportedRuntimeException() {
        super();
    }

    public UnsupportedRuntimeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnsupportedRuntimeException(String detailMessage) {
        super(detailMessage);
    }

    public UnsupportedRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
