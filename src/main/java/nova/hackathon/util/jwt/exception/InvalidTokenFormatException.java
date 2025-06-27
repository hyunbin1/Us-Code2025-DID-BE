package nova.hackathon.util.jwt.exception;

import nova.hackathon.util.exception.ErrorCode;

public class InvalidTokenFormatException extends JwtException{

    public InvalidTokenFormatException() {
        super(ErrorCode.INVALID_TOKEN_FORMAT);
    }

    public InvalidTokenFormatException(String message) {
        super(message, ErrorCode.INVALID_TOKEN_FORMAT);
    }
}
