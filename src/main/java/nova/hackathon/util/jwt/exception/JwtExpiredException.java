package nova.hackathon.util.jwt.exception;

import nova.hackathon.util.exception.ErrorCode;

public class JwtExpiredException extends JwtException {

    public JwtExpiredException() {
        super(ErrorCode.TOKEN_EXPIRED);
    }

    public JwtExpiredException(String message) {
        super(message, ErrorCode.TOKEN_EXPIRED);
    }
}
