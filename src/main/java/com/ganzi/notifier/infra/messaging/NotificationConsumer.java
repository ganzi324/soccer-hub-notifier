package com.ganzi.notifier.infra.messaging;

import com.ganzi.notifier.application.handler.NotificationHandlerMapper;
import com.ganzi.notifier.application.handler.NotificationMessageHandler;
import com.ganzi.notifier.common.config.RabbitMQConfig;
import com.ganzi.notifier.notification.domain.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class NotificationConsumer {

    private final Map<Class<? extends Notification>, NotificationMessageHandler<?>> handlers;

    public NotificationConsumer(NotificationHandlerMapper handlerMapper) {
        this.handlers = handlerMapper.getHandlerMap();
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void receive(Notification notification) {
        try {
            Class<? extends Notification> clazz = notification.getClass();
            NotificationMessageHandler<?> handler = handlers.get(clazz);

            if (handler == null) {
                throw new IllegalArgumentException("No handler for notification class: " + clazz.getSimpleName());
            }

            handleNotification(handler, notification);

        } catch (Exception e) {
            log.error("Failed to process notification", e);
            // 재시도, DLQ 처리 등
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Notification> void handleNotification(NotificationMessageHandler<T> handler, Notification notification) {
        handler.handle((T) notification);
    }
}
