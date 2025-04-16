package com.ganzi.notifier.infra.persistence;

import com.ganzi.notifier.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class NotificationPersistenceService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    public void save(Notification notification) {
        NotificationJpaEntity entity = mapper.toJpaEntity(notification);
        repository.save(entity);
    }

    public Optional<Notification> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomainEntity);
    }
}
