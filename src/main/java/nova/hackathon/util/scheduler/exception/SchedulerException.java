package nova.hackathon.util.scheduler.exception;

import nova.hackathon.util.exception.BusinessBaseException;
import nova.hackathon.util.exception.ErrorCode;

public class SchedulerException extends BusinessBaseException {

    public SchedulerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SchedulerException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
