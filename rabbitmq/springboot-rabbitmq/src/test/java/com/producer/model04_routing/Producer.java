package com.producer.model04_routing;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void producer() {
        for (int i = 0; i < 10; i++) {
            //参数1为交换机
            //参数2为路由key,“”表示为任意路由
            //参数3为消息内容
            rabbitTemplate.convertAndSend("directs","error","error 的日志信息");
        }
    }

}

