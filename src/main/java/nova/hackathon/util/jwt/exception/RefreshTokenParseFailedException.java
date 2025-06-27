package nova.hackathon.util.jwt.exception;

import nova.hackathon.util.exception.ErrorCode;

public class RefreshTokenParseFailedException extends JwtException{

    public RefreshTokenParseFailedException() {
        super(ErrorCode.REFRESH_TOKEN_PARSE_FAILED);
    }

    public RefreshTokenParseFailedException(String message) {
        super(message, ErrorCode.REFRESH_TOKEN_PARSE_FAILED);
    }
}
