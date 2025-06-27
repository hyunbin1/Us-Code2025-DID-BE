package nova.hackathon.util.scheduler.exception;

import nova.hackathon.util.exception.ErrorCode;

public class SchedulerUnknownException extends SchedulerException {

    public SchedulerUnknownException() {
        super(ErrorCode.SCHEDULER_UNKNOWN_ERROR);
    }

    public SchedulerUnknownException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
