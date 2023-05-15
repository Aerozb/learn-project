package model02_work_queues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitMQUtils;

/**
 * 消息生产者
 */
public class Producer {
    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("hello", true, false, false, null);
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("", "hello", null, ("hello world-" + i).getBytes());
        }
        RabbitMQUtils.closeConnectionAndChanel(channel, connection);
    }
}
