package nova.hackathon.util.jwt.exception;

import nova.hackathon.util.exception.ErrorCode;

public class JwtMalformedException extends JwtException{

    public JwtMalformedException() {
        super(ErrorCode.TOKEN_MALFORMED);
    }

    public JwtMalformedException(String message) {
        super(message, ErrorCode.TOKEN_MALFORMED);
    }
}
