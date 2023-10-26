package com.publisher_confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    /**
     * 生产者发送消息到交换机这一步，不论成功失败，这个回调方法都会被调用，从回调方法可以知道是否发送成功，从而进行下一步处理，比如重新发送
     * CorrelationData:消息相关数据
     * ack:交换机是否收到消息
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到 id 为:{}的消息", id);
        } else {
            log.info("交换机还未收到 id 为:{}消息,由于原因:{}", id, cause);
        }
    }

    /**
     * 回退消息：当消息发送给Exchange后，Exchange路由到Queue失败后才执行returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("没发送到队列的消息{}",returned);
    }
}
