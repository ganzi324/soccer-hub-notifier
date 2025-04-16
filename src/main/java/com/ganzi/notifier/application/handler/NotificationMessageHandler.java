package com.ganzi.notifier.application.handler;

import com.ganzi.notifier.application.exception.NotificationSendFailException;
import com.ganzi.notifier.domain.Notification;
import com.ganzi.notifier.infra.messaging.NotificationMessage;

public interface NotificationMessageHandler<T extends Notification> {
    Class<? extends NotificationMessage> getSupportClass();
    void handle(T notification) throws NotificationSendFailException;
}