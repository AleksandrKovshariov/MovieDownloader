package downloader.exception;

import java.io.IOError;
import java.io.IOException;

public class ResponseStatusDontMatchException extends IOException {
    private int statusCode;

    public ResponseStatusDontMatchException(String message) {
        super(message);
    }

    public ResponseStatusDontMatchException(String message, int statusCode){
        super(message);
        this.statusCode = statusCode;
    }

    public ResponseStatusDontMatchException(int statusCode){
        super();
        this.statusCode = statusCode;
    }
}
