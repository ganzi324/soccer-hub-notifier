package com.ganzi.notifier.common.data;

import com.ganzi.notifier.domain.NotificationStatus;
import com.ganzi.notifier.domain.email.EmailContent;
import com.ganzi.notifier.domain.email.EmailNotification;
import com.ganzi.notifier.domain.email.EmailReceiver;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class EmailNotificationTestData {

    public static EmailNotification createTestNotification() {
        UUID id = UUID.randomUUID();
        NotificationStatus status = NotificationStatus.PENDING;
        Instant now = Instant.now();

        EmailReceiver receiver = new EmailReceiver(
                List.of("to@example.com"),
                List.of("cc@example.com"),
                List.of("bcc@example.com")
        );

        EmailContent content = new EmailContent(
                "Test Email Subject",
                "This is a test email body."
        );

        return new EmailNotification(id, status, receiver, content, now, now);
    }
}
