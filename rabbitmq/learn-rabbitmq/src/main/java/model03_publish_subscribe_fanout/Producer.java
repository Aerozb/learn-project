package model03_publish_subscribe_fanout;

import com.rabbitmq.client.BuiltinExchangeType;
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

        //将通道声明指定交换机
        //参数1: 交换机名称    参数2: 交换机类型  fanout 广播类型
        channel.exchangeDeclare("logs", BuiltinExchangeType.FANOUT);

        //发消息
        channel.basicPublish("logs", "", null, "fanout type message".getBytes());

        RabbitMQUtils.closeConnectionAndChanel(channel, connection);
    }
}
