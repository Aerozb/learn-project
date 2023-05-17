package com.consumer.demo01_hello_word;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 生产端没有指定交换机只有routingKey和Object。
 * 消费方产生hello队列，放在默认的交换机(AMQP default)上。
 * 而默认的交换机有一个特点，只要你的routerKey的名字与这个
 * 交换机的队列有相同的名字，他就会自动路由上。
 * 生产端routingKey 叫hello ，消费端生产hello队列。
 * 他们就路由上了
 */
@Component
@RabbitListener(queuesToDeclare = @Queue(value = "hello"))
public class ConsumerHelloWord {

    @RabbitHandler
    public void receive(String message) {
        System.out.println("message = " + message);
    }

}

