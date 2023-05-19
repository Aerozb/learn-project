package com.delay_queue.delayed_plugins_impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class DelayedQueueConsumer {
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";

    @RabbitListener(queues = DELAYED_QUEUE_NAME)
    public void receiveDelayedQueue(Message message) {
        String msg = StrUtil.str(message.getBody(), StandardCharsets.UTF_8);
        log.info("当前时间：{},收到延时队列的消息：{}", DateUtil.now(), msg);
    }

}
