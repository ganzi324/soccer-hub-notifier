package com.ganzi.notifier.infra.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class EmailNotificationMessage implements NotificationMessage {
    private UUID id;
    private EmailTargetMessage target;
    private EmailContentMessage content;
}
