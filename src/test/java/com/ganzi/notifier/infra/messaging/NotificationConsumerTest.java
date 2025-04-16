package com.ganzi.notifier.infra.messaging;

import com.ganzi.notifier.application.handler.NotificationHandlerMapper;
import com.ganzi.notifier.application.handler.NotificationMessageHandler;
import com.ganzi.notifier.common.data.AmqpMessageTestDataFactory;
import com.ganzi.notifier.common.data.EmailNotificationMessageTestData;
import com.ganzi.notifier.common.data.EmailNotificationTestData;
import com.ganzi.notifier.domain.Notification;
import com.ganzi.notifier.domain.email.EmailNotification;
import com.ganzi.notifier.infra.persistence.NotificationPersistenceService;
import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock
    private NotificationHandlerMapper handlerMapper;

    @Mock
    private NotificationMessageHandler<Notification> handler;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private EmailNotificationMapper emailNotificationMapper;

    @Mock
    private NotificationMessageMapperFactory mapperFactory;

    @Mock
    private NotificationMessageMapper<Notification, NotificationMessage> mapper;

    @Mock
    private NotificationPersistenceService persistenceService;

    @Mock
    private Message amqpMessage;

    @Mock
    private Channel channel;

    private NotificationConsumer consumer;

    @BeforeEach
    void setUp() {
        Map<Class<? extends NotificationMessage>, NotificationMessageHandler<?>> handlerMap = Map.of(
                EmailNotificationMessage.class, handler
        );

        mapperFactory = new NotificationMessageMapperFactory(List.of(
                emailNotificationMapper
        ));

        amqpMessage = AmqpMessageTestDataFactory.create(1, 0);

        Mockito.when(handlerMapper.getHandlerMap()).thenReturn(handlerMap);
        consumer = new NotificationConsumer(handlerMapper, rabbitTemplate, mapperFactory, persistenceService);
    }

    @Test
    void receiveValidMessage_shouldBeHandledSuccessfully() throws Exception {
        // given
        EmailNotificationMessage message = EmailNotificationMessageTestData.createTestMessage();
        EmailNotification notification = EmailNotificationTestData.createTestNotification();

        when(emailNotificationMapper.toDomain(message)).thenReturn(notification);
        when(persistenceService.findById(any(UUID.class))).thenReturn(Optional.empty());
        doNothing().when(persistenceService).save(notification);
        doNothing().when(handler).handle(notification);

        // when
        consumer.receive(message, amqpMessage, channel);

        // then
        verify(handler).handle(notification);
        verify(persistenceService).save(notification);
        verify(channel).basicAck(anyLong(), eq(false));
    }

    @Test
    void receiveMessageWithoutHandler_shouldThrowException() {
        // given
        UnknownNotificationMessage unknownMessage = new UnknownNotificationMessage();

        // then
        assertThatThrownBy(() -> consumer.receive(unknownMessage, amqpMessage, channel))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    static class UnknownNotificationMessage implements NotificationMessage {
        @Override
        public UUID getId() {
            return null;
        }
    }
}
