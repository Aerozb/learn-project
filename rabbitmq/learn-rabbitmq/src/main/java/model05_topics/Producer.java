package model05_topics;

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

        String exchangeName = "topics";
        //将通道声明指定交换机
        //参数1: 交换机名称    参数2: 交换机类型  TOPIC 主题类型
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);

        String routingKey = "user.save";
        //发消息
        channel.basicPublish(exchangeName, routingKey, null, ("这是topic模型发布的基于route key: [" + routingKey + "] 发送的消息").getBytes());
        channel.basicPublish(exchangeName, "a.pp", null, ("这是topic模型发布的基于route key: [" + "a.pp" + "] 发送的消息").getBytes());

        RabbitMQUtils.closeConnectionAndChanel(channel, connection);
    }
}
