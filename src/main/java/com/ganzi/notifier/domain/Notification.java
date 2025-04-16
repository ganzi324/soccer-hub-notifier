package com.ganzi.notifier.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@Getter
public abstract class Notification {

    private static final int MAX_RETRIES = 2;

    private UUID id;
    private NotificationStatus status;
    private int retryCount;
    private Instant createdAt;
    private Instant updatedAt;

    public Notification(UUID id, NotificationStatus status, int retryCount, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.status = status;
        this.retryCount = retryCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Notification(UUID id, NotificationStatus status) {
        this.id = id;
        this.status = status;
        this.retryCount = 0;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void markAsSuccess() {
        this.status = NotificationStatus.SUCCESS;
    }

    public void markAsFailed() {
        this.status = NotificationStatus.FAILED;
    }

    public void markAsPending() {
        this.status = NotificationStatus.PENDING;
    }

    public void retry() {
        if (retryCount >= MAX_RETRIES) {
            this.status = NotificationStatus.FAILED;
        } else {
            this.retryCount++;
            this.status = NotificationStatus.RETRY;
        }
    }

    public abstract NotificationType getType();
    public abstract Target getTarget();
    public abstract Content getContent();
}
