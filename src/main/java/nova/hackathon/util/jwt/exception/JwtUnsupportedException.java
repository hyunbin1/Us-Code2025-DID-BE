package nova.hackathon.util.jwt.exception;

import nova.hackathon.util.exception.ErrorCode;

public class JwtUnsupportedException extends JwtException{

    public JwtUnsupportedException() {
        super(ErrorCode.TOKEN_UNSUPPORTED);
    }

    public JwtUnsupportedException(String message) {
        super(message, ErrorCode.TOKEN_UNSUPPORTED);
    }
}
