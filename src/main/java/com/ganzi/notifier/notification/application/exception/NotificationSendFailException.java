package com.ganzi.notifier.notification.application.exception;

public class NotificationSendFailException extends RuntimeException {
    public NotificationSendFailException(String message) {
        super(message);
    }

    public NotificationSendFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
