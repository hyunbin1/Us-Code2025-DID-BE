package nova.hackathon.member.exception;

import nova.hackathon.util.exception.ErrorCode;

public class PasswordIsInvalidException extends MemberException{

    public PasswordIsInvalidException() {super(ErrorCode.PASSWORD_IS_INVALID);}
    public PasswordIsInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
    public PasswordIsInvalidException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
