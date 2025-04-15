package com.ganzi.notifier.infra.messaging;

import com.ganzi.notifier.application.exception.NotificationSendFailException;
import com.ganzi.notifier.application.handler.NotificationHandlerMapper;
import com.ganzi.notifier.application.handler.NotificationMessageHandler;
import com.ganzi.notifier.common.config.RabbitMQConfig;
import com.ganzi.notifier.domain.Notification;
import com.ganzi.notifier.infra.persistence.NotificationPersistenceService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class NotificationConsumer {

    private final Map<Class<? extends NotificationMessage>, NotificationMessageHandler<?>> handlers;
    private final RabbitTemplate rabbitTemplate;
    private final NotificationMessageMapperFactory mapperFactory;
    private final NotificationPersistenceService persistenceService;

    public NotificationConsumer(NotificationHandlerMapper handlerMapper, RabbitTemplate rabbitTemplate, NotificationMessageMapperFactory mapperFactory, NotificationPersistenceService persistenceService) {
        this.handlers = handlerMapper.getHandlerMap();
        this.rabbitTemplate = rabbitTemplate;
        this.mapperFactory = mapperFactory;
        this.persistenceService = persistenceService;
    }

    @RabbitListener(queues = RabbitMQConfig.MAIN_QUEUE)
    public void receive(NotificationMessage notificationMessage, Message message, Channel channel) throws IOException {
        Notification notification = null;

        try {
            Class<? extends NotificationMessage> clazz = notificationMessage.getClass();
            NotificationMessageHandler<?> handler = handlers.get(clazz);

            if (handler == null) {
                throw new IllegalArgumentException("No handler for notification class: " + clazz.getSimpleName());
            }

            notification = persistenceService.findById(notificationMessage.getId())
                    .orElseGet(() -> convertMessage(notificationMessage));

            handleNotification(handler, notification);
            saveHistory(notification);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (NotificationSendFailException exception) {
            log.error("Failed to handle notification", exception);
            long retryCount = getRetryCount(message);

            if (notification != null) {
                notification.retry();
                saveHistory(notification);
            }

            if (retryCount == 1) {
                sendToRetryQueue(RabbitMQConfig.ROUTING_KEY_RETRY_2, message);
            } else if (retryCount >= 2) {
                sendToDLQ(message);
            } else {
                sendToRetryQueue(RabbitMQConfig.ROUTING_KEY_RETRY_1, message);
            }

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Notification> void handleNotification(NotificationMessageHandler<T> handler, Notification notification) {
        try {
            handler.handle((T) notification);
            notification.markAsSuccess();
        } catch (NotificationSendFailException e) {
            throw e;
        } catch (Exception e) {
            log.error("🔁 An exception occurred while processing the notice", e);
            throw new NotificationSendFailException("Notification processing failed", e);
        }
    }

    private long getRetryCount(Message message) {
        List<Map<String, Object>> xDeath = (List<Map<String, Object>>) message.getMessageProperties().getHeaders().get("x-death");

        if (xDeath == null || xDeath.isEmpty()) return 0;

        return xDeath.stream()
                .map(death -> (Long) death.get("count"))
                .mapToInt(Long::intValue)
                .sum();
    }

    private void sendToRetryQueue(String routingKey, Message message) {
        rabbitTemplate.send(RabbitMQConfig.MAIN_EXCHANGE, routingKey, message);

    }

    private void sendToDLQ(Message message) {
        rabbitTemplate.send(RabbitMQConfig.DLX_EXCHANGE, RabbitMQConfig.ROUTING_KEY_DLQ, message);
    }

    private Notification convertMessage(NotificationMessage notificationMessage) {
        NotificationMessageMapper<Notification, ? extends NotificationMessage> mapper = mapperFactory.getMapper(notificationMessage.getClass());

        return ((NotificationMessageMapper<Notification, NotificationMessage>)mapper).toDomain(notificationMessage);
    }

    private void saveHistory(Notification notification) {
        persistenceService.save(notification);
    }
}
