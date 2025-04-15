package com.ganzi.notifier.infra.messaging;

import com.ganzi.notifier.domain.Notification;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificationMessageMapperFactory {

    private final Map<Class<? extends NotificationMessage>, NotificationMessageMapper<?, ?>> mapperMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public NotificationMessageMapperFactory(List<NotificationMessageMapper<?, ?>> mappers) {
        for (NotificationMessageMapper<?, ?> mapper : mappers) {
            Class<?>[] typeArgs = GenericTypeResolver.resolveTypeArguments(mapper.getClass(), NotificationMessageMapper.class);
            if (typeArgs != null && typeArgs.length == 2) {
                Class<? extends NotificationMessage> messageType = (Class<? extends NotificationMessage>) typeArgs[1];
                mapperMap.put(messageType, mapper);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Notification, E extends NotificationMessage> NotificationMessageMapper<T, E> getMapper(Class<E> messageClass) {
        NotificationMessageMapper<T, E> mapper = (NotificationMessageMapper<T, E>) mapperMap.get(messageClass);
        if (mapper == null) {
            throw new IllegalArgumentException("No mapper found for: " + messageClass);
        }
        return mapper;
    }
}
