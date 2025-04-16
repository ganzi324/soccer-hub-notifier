package com.ganzi.notifier.infra.messaging;

import com.ganzi.notifier.domain.NotificationStatus;
import com.ganzi.notifier.domain.email.EmailContent;
import com.ganzi.notifier.domain.email.EmailNotification;
import com.ganzi.notifier.domain.email.EmailReceiver;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class EmailNotificationMapper implements NotificationMessageMapper<EmailNotification, EmailNotificationMessage> {

    @Override
    public EmailNotification toDomain(EmailNotificationMessage message) {
        return new EmailNotification(
                message.getId(),
                NotificationStatus.PENDING,
                new EmailReceiver(message.getTarget().to(), message.getTarget().cc(), message.getTarget().bcc()),
                new EmailContent(message.getContent().subject(), message.getContent().body()),
                Instant.now(),
                Instant.now()
        );
    }
}
