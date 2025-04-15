package com.ganzi.notifier.infra.persistence;

import com.ganzi.notifier.domain.Content;
import com.ganzi.notifier.domain.NotificationStatus;
import com.ganzi.notifier.domain.NotificationType;
import com.ganzi.notifier.domain.Target;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;

    @Type(JsonType.class)
    @Column(name = "target", columnDefinition = "jsonb", nullable = false)
    @Convert(converter = JsonConverter.class)
    private Target targetInfo;

    @Type(JsonType.class)
    @Column(name = "content", columnDefinition = "jsonb", nullable = false)
    @Convert(converter = JsonConverter.class)
    private Content content;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;
}