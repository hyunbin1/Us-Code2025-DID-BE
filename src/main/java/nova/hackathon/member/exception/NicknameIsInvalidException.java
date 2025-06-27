package nova.hackathon.member.exception;

import nova.hackathon.util.exception.ErrorCode;

public class NicknameIsInvalidException extends MemberException{

    public NicknameIsInvalidException() {super(ErrorCode.NICKNAME_IS_INVALID);}
}
