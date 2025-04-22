package com.ganzi.notifier.common.data;

import com.ganzi.notifier.infra.messaging.EmailContentMessage;
import com.ganzi.notifier.infra.messaging.EmailNotificationMessage;
import com.ganzi.notifier.infra.messaging.EmailTargetMessage;

import java.util.List;
import java.util.UUID;

public class EmailNotificationMessageTestData {

    public static EmailNotificationMessage createTestMessage() {
        UUID id = UUID.randomUUID();

        EmailTargetMessage target = new EmailTargetMessage(
                List.of("to1@example.com", "to2@example.com"),
                List.of("cc1@example.com"),
                List.of("bcc1@example.com", "bcc2@example.com")
        );

        EmailContentMessage content = new EmailContentMessage(
                "📬 테스트 메일 제목",
                "이것은 테스트 메일 본문입니다. 시스템 동작을 확인해주세요."
        );

        return new EmailNotificationMessage(id, target, content);
    }
}
