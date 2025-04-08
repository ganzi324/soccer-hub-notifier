package com.ganzi.notifier.notification.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class EmailNotification implements Notification {
    private EmailReceiver target;
    private EmailContent content;

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
