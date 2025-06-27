package nova.hackathon.member.exception;

import nova.hackathon.util.exception.ErrorCode;

public class DuplicateEmailException extends MemberException{

    public DuplicateEmailException() {super(ErrorCode.DUPLICATE_EMAIL_EXCEPTION);}
    public DuplicateEmailException(ErrorCode errorCode) {
        super(errorCode);
    }
    public DuplicateEmailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
