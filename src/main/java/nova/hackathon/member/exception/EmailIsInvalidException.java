package nova.hackathon.member.exception;

import nova.hackathon.util.exception.ErrorCode;

public class EmailIsInvalidException extends MemberException{

    public EmailIsInvalidException() {super(ErrorCode.EMAIL_IS_INVALID);}
}
