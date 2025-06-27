package nova.hackathon.util.jwt.exception;

import nova.hackathon.util.exception.ErrorCode;

public class JwtInvalidException extends JwtException{

    public JwtInvalidException() {
        super(ErrorCode.TOKEN_INVALID);
    }

    public JwtInvalidException(String message) {
        super(message, ErrorCode.TOKEN_INVALID);
    }
}
