package com.producer.model03_publish_subscribe_fanout;

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
            rabbitTemplate.convertAndSend("logs","","这是日志广播");
        }
    }

}

