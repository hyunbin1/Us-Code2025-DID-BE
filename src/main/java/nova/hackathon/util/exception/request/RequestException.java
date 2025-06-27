package nova.hackathon.util.exception.request;

import nova.hackathon.util.exception.BusinessBaseException;
import nova.hackathon.util.exception.ErrorCode;

public class RequestException extends BusinessBaseException {

    public RequestException(ErrorCode errorCode) {
        super(errorCode);
    }
    public RequestException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}