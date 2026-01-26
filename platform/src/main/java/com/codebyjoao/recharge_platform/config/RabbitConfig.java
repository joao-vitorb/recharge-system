package com.codebyjoao.recharge_platform.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "recharge.exchange";

    public static final String RECHARGE_REQUESTS_QUEUE = "recharge.requests";
    public static final String RECHARGE_RESULTS_QUEUE = "recharge.results";

    public static final String RECHARGE_REQUEST_KEY = "recharge.request";
    public static final String RECHARGE_RESULT_KEY = "recharge.result";

    @Bean
    public DirectExchange rechargeExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue rechargeRequestsQueue() {
        return QueueBuilder.durable(RECHARGE_REQUESTS_QUEUE).build();
    }

    @Bean
    public Queue rechargeResultsQueue() {
        return QueueBuilder.durable(RECHARGE_RESULTS_QUEUE).build();
    }

    @Bean
    public Binding rechargeRequestsBinding(DirectExchange rechargeExchange, Queue rechargeRequestsQueue) {
        return BindingBuilder.bind(rechargeRequestsQueue).to(rechargeExchange).with(RECHARGE_REQUEST_KEY);
    }

    @Bean
    public Binding rechargeResultsBinding(DirectExchange rechargeExchange, Queue rechargeResultsQueue) {
        return BindingBuilder.bind(rechargeResultsQueue).to(rechargeExchange).with(RECHARGE_RESULT_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
