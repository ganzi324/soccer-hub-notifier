package com.ganzi.notifier.notification.domain;

public interface Notification {
    NotificationType getType();
    Target getTarget();
    Content getContent();
}
