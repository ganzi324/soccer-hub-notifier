package com.ganzi.notifier.application.handler;

import com.ganzi.notifier.application.service.EmailService;
import com.ganzi.notifier.application.exception.NotificationSendFailException;
import com.ganzi.notifier.domain.email.EmailNotification;
import com.ganzi.notifier.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailMessageHandler implements NotificationMessageHandler<EmailNotification> {

    private final EmailService emailService;

    @Override
    public Class<? extends Notification> getSupportClass() {
        return EmailNotification.class;
    }

    @Override
    public void handle(EmailNotification notification) throws NotificationSendFailException {
        emailService.send(notification.getTarget().to().getFirst(), notification.getContent().subject(), notification.getContent().body());
    }
}