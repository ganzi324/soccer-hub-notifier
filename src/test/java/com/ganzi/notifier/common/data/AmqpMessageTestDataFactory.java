package com.ganzi.notifier.common.data;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.List;
import java.util.Map;

public class AmqpMessageTestDataFactory {
    public static Message create(long deliveryTag, int retryCount) {
        MessageProperties messageProperties = new MessageProperties();

        messageProperties.getHeaders().put("x-death", List.of(
                Map.of("count", (long) retryCount)
        ));

        messageProperties.setDeliveryTag(deliveryTag);

        return new Message("{}".getBytes(), messageProperties);
    }
}
