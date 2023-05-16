package com.consumer.model02_work_queues;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 生产端没有指定交换机只有routingKey和Object。
 * 消费方产生work队列，放在默认的交换机(AMQP default)上。
 * 而默认的交换机有一个特点，只要你的routerKey的名字与这个
 * 交换机的队列有相同的名字，他就会自动路由上。
 * 生产端routingKey 叫work ，消费端生产work队列。
 * 他们就路由上了
 */
@Component
public class ConsumerWorkQueues {

    @RabbitListener(queuesToDeclare = @Queue("work"))
    public void receive1(String message) {
        System.out.println("work message1 = " + message);
    }


    @RabbitListener(queuesToDeclare = @Queue("work"))
    public void receive2(String message) {
        System.out.println("work message2 = " + message);
    }
}

