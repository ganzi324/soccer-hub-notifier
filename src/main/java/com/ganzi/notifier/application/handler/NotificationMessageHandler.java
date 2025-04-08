package com.ganzi.notifier.application.handler;

import com.ganzi.notifier.notification.domain.Notification;

public interface NotificationMessageHandler<T extends Notification> {
    Class<? extends Notification> getSupportClass();
    void handle(T notification);
}