package com.ganzi.notifier.domain;

import java.util.UUID;

public interface Notification {
    UUID getId();
    NotificationType getType();
    Target getTarget();
    Content getContent();
}
