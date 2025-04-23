package com.ganzi.notifier.infra.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotificationMessage implements NotificationMessage {
    private UUID id;
    private EmailTargetMessage target;
    private EmailContentMessage content;
}
