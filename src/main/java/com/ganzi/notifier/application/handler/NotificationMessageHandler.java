package com.ganzi.notifier.application.handler;

import com.ganzi.notifier.application.exception.NotificationSendFailException;
import com.ganzi.notifier.domain.Notification;

public interface NotificationMessageHandler<T extends Notification> {
    Class<? extends Notification> getSupportClass();
    void handle(T notification) throws NotificationSendFailException;
}