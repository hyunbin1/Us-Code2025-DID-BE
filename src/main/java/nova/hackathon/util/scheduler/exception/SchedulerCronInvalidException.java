package nova.hackathon.util.scheduler.exception;

import nova.hackathon.util.exception.ErrorCode;

public class SchedulerCronInvalidException extends SchedulerException {

    public SchedulerCronInvalidException() {
        super(ErrorCode.SCHEDULER_CRON_INVALID);
    }

    public SchedulerCronInvalidException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
