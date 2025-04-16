package com.ganzi.notifier.infra.persistence;

import com.ganzi.notifier.domain.Notification;
import com.ganzi.notifier.domain.NotificationType;
import com.ganzi.notifier.domain.email.EmailContent;
import com.ganzi.notifier.domain.email.EmailNotification;
import com.ganzi.notifier.domain.email.EmailReceiver;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationJpaEntity toJpaEntity(Notification notification) {
        return NotificationJpaEntity.builder()
                .id(notification.getId())
                .type(notification.getType())
                .status(notification.getStatus())
                .retryCount(notification.getRetryCount())
                .content(notification.getContent())
                .targetInfo(notification.getTarget())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }

    public Notification toDomainEntity(NotificationJpaEntity entity) {
        if (entity.getType() == NotificationType.EMAIL) {
            return new EmailNotification(
                    entity.getId(),
                    entity.getStatus(),
                    entity.getRetryCount(),
                    (EmailReceiver) entity.getTargetInfo(),
                    (EmailContent) entity.getContent(),
                    entity.getCreatedAt(),
                    entity.getUpdatedAt()
            );
        }
        return null;
    }
}
