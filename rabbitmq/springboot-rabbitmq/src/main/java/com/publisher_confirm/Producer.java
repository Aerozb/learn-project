package com.publisher_confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/confirm")
@Slf4j
public class Producer {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MyCallBack myCallBack;

    //依赖注入 rabbitTemplate 之后再设置它的回调对象
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(myCallBack);
        /*
          true：
          交换机无法将消息进行路由时，会将该消息返回给生产者
          false：
          如果发现消息无法进行路由，则直接丢弃
         */
        //我这个不设置也会执行回退方法，因为配置文件里设置了publisher-returns: true
        rabbitTemplate.setMandatory(true);
        //设置回退消息交给谁处理

        rabbitTemplate.setReturnsCallback(myCallBack);
    }

    @GetMapping("sendMsg/{message}")
    public void sendMsg(@PathVariable String message) throws InterruptedException {
        /*
         *此消息正常发送到队列
         */
        //指定消息 id 为 1
        CorrelationData correlationData1 = new CorrelationData("1");
        String routingKey = "key1";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey, message + routingKey, correlationData1);
        log.info("发送消息内容:{}", message);
        TimeUnit.SECONDS.sleep(3);
        log.info("\n");

        /*
         *此消息发到交换机了，但是路由失败，没发送到队列
         */
        CorrelationData correlationData2 = new CorrelationData("2");
        routingKey = "key2";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey, message + routingKey, correlationData2);
        log.info("发送消息内容:{}", message + routingKey);
        TimeUnit.SECONDS.sleep(3);
        log.info("\n");

        /*
         *没有这个交换机，发送失败
         */
        CorrelationData correlationData3 = new CorrelationData("3");
        routingKey = "key3";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME + 3, routingKey, message + routingKey, correlationData3);
        log.info("发送消息内容:{}", message + routingKey);
    }
}
