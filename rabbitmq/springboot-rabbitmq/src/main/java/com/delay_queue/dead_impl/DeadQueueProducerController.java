package com.delay_queue.dead_impl;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 生产者
 */
@Slf4j
@RequestMapping("ttl")
@RestController
public class DeadQueueProducerController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间：{},发送一条信息给两个 TTL 队列:{}", DateUtil.now(), message);
        //第三个参数：要发送的消息
        rabbitTemplate.convertAndSend(DeadQueueConfig.EXCHANGE_X, DeadQueueConfig.ROUTING_KEY_XA, "消息来自 ttl 为 " + DeadQueueConfig.X_MESSAGE_TTL_QA + "S 的队列: " + message);
        rabbitTemplate.convertAndSend(DeadQueueConfig.EXCHANGE_X, DeadQueueConfig.ROUTING_KEY_XB, "消息来自 ttl 为 " + DeadQueueConfig.X_MESSAGE_TTL_QB + "S 的队列: " + message);
    }

    @GetMapping("sendExpirationMsg/{message}/{ttlTimeMillisecond}")
    public void sendMsg(@PathVariable String message, @PathVariable String ttlTimeMillisecond) {
        rabbitTemplate.convertAndSend(DeadQueueConfig.EXCHANGE_X, DeadQueueConfig.ROUTING_KEY_XC, message, correlationData -> {
            correlationData.getMessageProperties().setExpiration(ttlTimeMillisecond);
            return correlationData;
        });
        log.info("当前时间：{},发送一条时长{}秒 TTL 信息给队列 C:{}", DateUtil.now(), TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(ttlTimeMillisecond)), message);
    }

}
