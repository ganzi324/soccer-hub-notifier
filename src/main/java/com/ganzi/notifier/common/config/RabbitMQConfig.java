package com.ganzi.notifier.common.config;

import com.ganzi.notifier.common.property.RabbitMQProperties;
import com.ganzi.notifier.domain.email.EmailNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final RabbitMQProperties properties;

    public static final String MAIN_EXCHANGE = "notification.exchange";
    public static final String DLX_EXCHANGE = "notification.dlx";

    public static final String MAIN_QUEUE = "notification.queue";
    public static final String RETRY_QUEUE_1 = "notification.retry.queue.1";
    public static final String RETRY_QUEUE_2 = "notification.retry.queue.2";
    public static final String DLQ = "notification.dlq";

    public static final String ROUTING_KEY_MAIN = "notification.main";
    public static final String ROUTING_KEY_RETRY_1 = "notification.retry.1";
    public static final String ROUTING_KEY_RETRY_2 = "notification.retry.2";
    public static final String ROUTING_KEY_DLQ = "notification.dlq";

    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(MAIN_EXCHANGE);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(MAIN_QUEUE)
                .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_RETRY_1)
                .build();
    }

    @Bean
    public Queue retryQueue1() {
        return QueueBuilder.durable(RETRY_QUEUE_1)
                .withArgument("x-message-ttl", 5_000)
                .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_MAIN)
                .build();
    }

    @Bean
    public Queue retryQueue2() {
        return QueueBuilder.durable(RETRY_QUEUE_2)
                .withArgument("x-message-ttl", 10_000)
                .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_MAIN)
                .build();
    }

    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public Binding bindMainQueue() {
        return BindingBuilder.bind(mainQueue()).to(mainExchange()).with(ROUTING_KEY_MAIN);
    }

    @Bean
    public Binding bindRetry1() {
        return BindingBuilder.bind(retryQueue1()).to(mainExchange()).with(ROUTING_KEY_RETRY_1);
    }

    @Bean
    public Binding bindRetry2() {
        return BindingBuilder.bind(retryQueue2()).to(mainExchange()).with(ROUTING_KEY_RETRY_2);
    }

    @Bean
    public Binding bindDLQ() {
        return BindingBuilder.bind(dlqQueue()).to(dlxExchange()).with(ROUTING_KEY_DLQ);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("emailNotification", EmailNotification.class);

        typeMapper.setIdClassMapping(idClassMapping);
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(properties.getHost());
        connectionFactory.setPort(properties.getPort());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}