package com.ganzi.notifier.infra.messaging;

import com.ganzi.notifier.domain.Notification;

public interface NotificationMessageMapper<T extends Notification, E extends NotificationMessage> {
    T toDomain(E notificationMessage);
}
