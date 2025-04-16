package com.ganzi.notifier.application.handler;

import com.ganzi.notifier.application.exception.NotificationSendFailException;
import com.ganzi.notifier.application.service.EmailService;
import com.ganzi.notifier.domain.email.EmailNotification;
import com.ganzi.notifier.infra.messaging.EmailNotificationMessage;
import com.ganzi.notifier.infra.messaging.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailMessageHandler implements NotificationMessageHandler<EmailNotification> {

    private final EmailService emailService;

    @Override
    public Class<? extends NotificationMessage> getSupportClass() {
        return EmailNotificationMessage.class;
    }

    @Override
    public void handle(EmailNotification emailNotification) throws NotificationSendFailException {
        emailService.send(emailNotification.getTarget().to().getFirst(), emailNotification.getContent().subject(), emailNotification.getContent().body());
    }
}