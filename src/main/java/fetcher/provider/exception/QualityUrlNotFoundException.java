package fetcher.provider.exception;

public class QualityUrlNotFoundException extends RuntimeException{

    public QualityUrlNotFoundException() {
        super();
    }

    public QualityUrlNotFoundException(String message) {
        super(message);
    }

    public QualityUrlNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public QualityUrlNotFoundException(Throwable cause) {
        super(cause);
    }

    protected QualityUrlNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
