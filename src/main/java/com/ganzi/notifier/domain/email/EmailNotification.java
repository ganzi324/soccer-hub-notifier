package com.ganzi.notifier.domain.email;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.ganzi.notifier.domain.Notification;
import com.ganzi.notifier.domain.NotificationStatus;
import com.ganzi.notifier.domain.NotificationType;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
public class EmailNotification extends Notification {

    private EmailReceiver target;
    private EmailContent content;

    public EmailNotification(UUID id,
                             NotificationStatus status,
                             int retryCount,
                             EmailReceiver target,
                             EmailContent content,
                             Instant createdAt,
                             Instant updatedAt
    ) {
        super(id, status, retryCount, createdAt, updatedAt);
        this.target = target;
        this.content = content;
    }

    public EmailNotification(UUID id,
                             NotificationStatus status,
                             EmailReceiver target,
                             EmailContent content,
                             Instant createdAt,
                             Instant updatedAt
    ) {
        super(id, status, 0, createdAt, updatedAt);
        this.target = target;
        this.content = content;
    }

    public NotificationType getType() {
        return NotificationType.EMAIL;
    }

    @Override
    public EmailReceiver getTarget() {
        return target;
    }

    @Override
    public EmailContent getContent() {
        return content;
    }
}
