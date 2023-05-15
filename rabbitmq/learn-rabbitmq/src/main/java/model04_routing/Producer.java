package model04_routing;

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

        String exchangeName = "logs_direct";
        //将通道声明指定交换机
        //参数1: 交换机名称    参数2: 交换机类型  DIRECT 直连类型
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);

        String routingKey = "info";
//        String routingKey = "error";
        //发消息
        channel.basicPublish(exchangeName, routingKey, null, ("这是direct模型发布的基于route key: [" + routingKey + "] 发送的消息").getBytes());

        RabbitMQUtils.closeConnectionAndChanel(channel, connection);
    }
}
