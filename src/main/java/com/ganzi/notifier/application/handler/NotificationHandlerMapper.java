package com.ganzi.notifier.application.handler;

import com.ganzi.notifier.domain.Notification;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Component
public class NotificationHandlerMapper {

    private final Map<Class<? extends Notification>, NotificationMessageHandler<?>> handlerMap;

    public NotificationHandlerMapper(List<NotificationMessageHandler<?>> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(NotificationMessageHandler::getSupportClass, Function.identity()));
    }
}
