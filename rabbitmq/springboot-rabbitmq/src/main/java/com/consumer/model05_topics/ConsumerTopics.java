package com.consumer.model05_topics;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerTopics {

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    key = {"user.*"},
                    exchange = @Exchange(type = ExchangeTypes.TOPIC, name = "topics")
            )
    })
    public void receive1(String message) {
        System.out.println("message1 = " + message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    key = {"user.#"},
                    exchange = @Exchange(type = ExchangeTypes.TOPIC, name = "topics")
            )
    })
    public void receive2(String message) {
        System.out.println("message2 = " + message);
    }
}
