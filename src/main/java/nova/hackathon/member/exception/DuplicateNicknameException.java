package nova.hackathon.member.exception;

import nova.hackathon.util.exception.ErrorCode;

public class DuplicateNicknameException extends MemberException{

    public DuplicateNicknameException() {super(ErrorCode.DUPLICATE_NICKNAME_EXCEPTION);}
}
