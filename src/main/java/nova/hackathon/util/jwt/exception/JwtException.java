package nova.hackathon.util.jwt.exception;

import nova.hackathon.util.exception.BusinessBaseException;
import nova.hackathon.util.exception.ErrorCode;

public class JwtException extends BusinessBaseException {

    public JwtException(ErrorCode errorCode) {
        super(errorCode);
    }

    public JwtException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
