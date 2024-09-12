package com.schizoscrypt.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue requestQueue() {
        return new Queue("account_request", true);
    }

    @Bean
    public DirectExchange accountExchange() {
        return new DirectExchange("account_request");
    }

    @Bean
    public Binding accountBinding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("account.request.key");
    }
}
