package com.ganzi.notifier.domain.email;

import com.ganzi.notifier.domain.Notification;
import com.ganzi.notifier.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class EmailNotification implements Notification {
    private UUID id;
    private EmailReceiver target;
    private EmailContent content;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
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
