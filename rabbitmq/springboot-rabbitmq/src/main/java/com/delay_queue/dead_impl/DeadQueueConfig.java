package com.delay_queue.dead_impl;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeadQueueConfig {
    public static final String EXCHANGE_X = "X";
    public static final String ROUTING_KEY_XA = "XA";
    public static final String ROUTING_KEY_XB = "XB";
    public static final String ROUTING_KEY_XC = "XC";
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    public static final String EXCHANGE_Y = "Y";
    public static final String QUEUE_D = "QD";
    public static final String ROUTING_KEY_YD = "YD";
    public static final int X_MESSAGE_TTL_QA = 10;
    public static final int X_MESSAGE_TTL_QB = 20;

    // 声明 交换机X
    @Bean
    public DirectExchange exchangeX() {
        return new DirectExchange(EXCHANGE_X);
    }

    //声明队列 A ttl 为 10s 并绑定到对应的死信交换机
    @Bean
    public Queue queueA() {
        return QueueBuilder.durable(QUEUE_A)
                //声明当前队列绑定的死信交换机
                .deadLetterExchange(EXCHANGE_Y)
                //声明当前队列的死信路由 key
                .deadLetterRoutingKey(ROUTING_KEY_YD)
                //声明队列的 TTL
                .ttl(X_MESSAGE_TTL_QA * 1000)
                .build();
    }

    // 声明队列 A 绑定 X 交换机
    @Bean
    public Binding queueABindExchangeX(Queue queueA, DirectExchange exchangeX) {
        return BindingBuilder.bind(queueA).to(exchangeX).with(ROUTING_KEY_XA);
    }

    //声明队列 B ttl 为 20s 并绑定到对应的死信交换机
    @Bean
    public Queue queueB() {
        return QueueBuilder.durable(QUEUE_B)
                .deadLetterExchange(EXCHANGE_Y)
                .deadLetterRoutingKey(ROUTING_KEY_YD)
                .ttl(X_MESSAGE_TTL_QB * 1000)
                .build();
    }

    //声明队列 B 绑定 X 交换机
    @Bean
    public Binding queueBBindExchangeX(Queue queueB, DirectExchange exchangeX) {
        return BindingBuilder.bind(queueB).to(exchangeX).with(ROUTING_KEY_XB);
    }

    //声明队列C，ttl为生产者设置，并绑定到对应的死信交换机
    @Bean
    public Queue queueC() {
        return QueueBuilder.durable(QUEUE_C)
                //声明当前队列绑定的死信交换机
                .deadLetterExchange(EXCHANGE_Y)
                //声明当前队列的死信路由 key
                .deadLetterRoutingKey(ROUTING_KEY_YD)
                .build();
    }

    //声明队列 QC 绑定关系
    @Bean
    public Binding queueCBindExchangeX(Queue queueC, DirectExchange exchangeX) {
        return BindingBuilder.bind(queueC).to(exchangeX).with(ROUTING_KEY_XC);
    }

    // 声明 交换机Y
    @Bean
    public DirectExchange exchangeY() {
        return new DirectExchange(EXCHANGE_Y);
    }

    //声明死信队列 QD
    @Bean
    public Queue queueD() {
        return new Queue(QUEUE_D);
    }

    //声明死信队列 QD 绑定关系
    @Bean
    public Binding queueDBindExchangeX(Queue queueD, DirectExchange exchangeY) {
        return BindingBuilder.bind(queueD).to(exchangeY).with(ROUTING_KEY_YD);
    }



}
