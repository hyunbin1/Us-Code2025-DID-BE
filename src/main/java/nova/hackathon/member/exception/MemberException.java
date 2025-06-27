package nova.hackathon.member.exception;

import nova.hackathon.util.exception.BusinessBaseException;
import nova.hackathon.util.exception.ErrorCode;

public class MemberException extends BusinessBaseException {

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
    public MemberException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}