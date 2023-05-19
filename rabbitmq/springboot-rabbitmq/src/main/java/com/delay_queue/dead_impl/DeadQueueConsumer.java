package com.delay_queue.dead_impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class DeadQueueConsumer {

    @RabbitListener(queues = DeadQueueConfig.QUEUE_D)
    public void receiveD(Message message){
        String msg = StrUtil.str(message.getBody(), StandardCharsets.UTF_8);
        log.info("当前时间：{},收到死信队列信息{}", DateUtil.now(), msg);
    }

}
