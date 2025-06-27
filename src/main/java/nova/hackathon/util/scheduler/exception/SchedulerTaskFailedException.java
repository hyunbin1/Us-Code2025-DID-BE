package nova.hackathon.util.scheduler.exception;

import nova.hackathon.util.exception.ErrorCode;

public class SchedulerTaskFailedException extends SchedulerException {

    public SchedulerTaskFailedException() {
        super(ErrorCode.SCHEDULER_TASK_FAILED);
    }

    public SchedulerTaskFailedException(ErrorCode errorCode) {
        super(errorCode);
    }
    public SchedulerTaskFailedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
