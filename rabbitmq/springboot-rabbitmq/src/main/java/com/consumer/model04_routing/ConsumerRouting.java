package com.consumer.model04_routing;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerRouting {

    //使用@RabbitListener注解中的bindings声明并绑定交换机和队列
    @RabbitListener(bindings = @QueueBinding(value = @Queue("directsQueue"),//指定队列名
            key = {"info", "error"},
            exchange = @Exchange(name = "directs", type = ExchangeTypes.DIRECT)
    ))
    public void receive1(String message) {
        System.out.println("message1 = " + message);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue("directsQueue"),
            key = {"error"},
            exchange = @Exchange(name = "directs", type = ExchangeTypes.DIRECT)
    ))
    public void receive2(String message) {
        System.out.println("message2 = " + message);
    }
}
