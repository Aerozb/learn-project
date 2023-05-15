package model04_routing;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import utils.RabbitMQUtils;

import java.io.IOException;

/**
 * 消息消费者
 */
public class Consumer_1 {
    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "logs_direct";
        //通道绑定交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
        //获取临时队列名
        String queueName = channel.queueDeclare().getQueue();

        String routingKey = "error";

        //绑定交换机和队列
        channel.queueBind(queueName, exchangeName, routingKey);
        //自动确认关掉
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @SneakyThrows
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者1" + new String(body));
            }
        });
    }
}
