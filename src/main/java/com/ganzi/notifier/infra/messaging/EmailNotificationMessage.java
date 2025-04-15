package com.ganzi.notifier.infra.messaging;

import lombok.Getter;

import java.util.UUID;

@Getter
public class EmailNotificationMessage implements NotificationMessage {
    private UUID id;
    private EmailTargetMessage target;
    private EmailContentMessage content;
}
