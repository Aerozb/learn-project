package model02_work_queues.manual_ack;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 消息消费者
 */
public class Consumer_1 {
    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1);//每一次只能消费一个消息
        channel.queueDeclare("hello", true, false, false, null);
        //自动确认关掉
        channel.basicConsume("hello", false, new DefaultConsumer(channel) {
            @SneakyThrows
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(new String(body));
                //参数1:手动确认消息标识 参数2:false 每次确认一个
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
