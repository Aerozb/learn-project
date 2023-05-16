package com.producer.model02_work_queues;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 生产端没有指定交换机只有routingKey和Object。
     * 消费方产生work队列，放在默认的交换机(AMQP default)上。
     * 而默认的交换机有一个特点，只要你的routerKey的名字与这个
     * 交换机的队列有相同的名字，他就会自动路由上。
     * 生产端routingKey 叫work ，消费端生产work队列。
     * 他们就路由上了
     */
    @Test
    void producer() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("work", "hello work!");
        }
    }

}

