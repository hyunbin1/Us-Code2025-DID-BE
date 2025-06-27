package nova.hackathon.util.jwt.exception;

import nova.hackathon.util.exception.ErrorCode;

public class JwtSignatureInvalidException extends JwtException{

    public JwtSignatureInvalidException() {
        super(ErrorCode.TOKEN_SIGNATURE_INVALID);
    }

    public JwtSignatureInvalidException(String message) {
        super(message, ErrorCode.TOKEN_SIGNATURE_INVALID);
    }
}
