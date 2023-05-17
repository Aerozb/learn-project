package com.consumer.model03_publish_subscribe_fanout;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerWorkQueuesFanout {

    //使用@RabbitListener注解中的bindings声明并绑定交换机和队列
    @RabbitListener(bindings = @QueueBinding(value = @Queue, // 创建临时队列
            exchange = @Exchange(name = "logs", type = ExchangeTypes.FANOUT)
    ))
    public void receive1(String message) {
        System.out.println("message1 = " + message);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue, // 创建临时队列
            exchange = @Exchange(name = "logs", type = ExchangeTypes.FANOUT)
    ))
    public void receive2(String message) {
        System.out.println("message2 = " + message);
    }
}
