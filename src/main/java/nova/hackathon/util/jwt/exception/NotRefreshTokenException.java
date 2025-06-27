package nova.hackathon.util.jwt.exception;

import nova.hackathon.util.exception.ErrorCode;

public class NotRefreshTokenException extends JwtException {

    public NotRefreshTokenException() {
        super(ErrorCode.NOT_REFRESH_TOKEN);
    }

    public NotRefreshTokenException(String message) {
        super(message, ErrorCode.NOT_REFRESH_TOKEN);
    }
}
