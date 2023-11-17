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
     * CorrelationData:对象内部只有一个 id 属性，用来表示当前消息的唯一性。
     * ack:消息投递到broker 的状态，true表示成功。
     * cause：表示投递失败的原因。
     *
     *
     *
     * correlationData 和 deliveryTag 的产生时间是不一样的，因为它们分别用于不同的目的。
     * correlationData：
     * 产生时间：correlationData 是由生产者在消息发送时生成的。
     * 作用：它用于关联生产者发送的消息和消息代理的响应。通常，correlationData 包含一个唯一标识符，用于标识消息。
     *
     * deliveryTag：
     * 产生时间：deliveryTag 是在消息被消息代理（例如 RabbitMQ）投递给消费者时由消息代理生成的。
     * 作用：它是一个用于标识消息的整数标签。每个投递的消息都会被分配一个唯一的 deliveryTag。在手动确认模式下，消费者可以使用 deliveryTag 来确认或拒绝消息。
     * 总的来说，这两者的产生时间和作用不同。correlationData 与消息一起发送到消息代理，而 deliveryTag 是在消息被投递给消费者时由消息代理生成的。
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
