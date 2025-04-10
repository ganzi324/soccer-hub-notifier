package com.ganzi.notifier.notification.application.exception;

public class EmailSendFailException extends NotificationSendFailException {
    public EmailSendFailException(String message)  {
        super(message);
    }

    public EmailSendFailException(String message, Throwable cause)  {
        super(message, cause);
    }
}
