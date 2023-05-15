package model01_Hello_World;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer {
    public static void main(String[] args) throws Exception {
        //创建连接mq的连接工厂对象，重量级对象，类加载时创建一次即可
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置连接rabbitmq的主机
        connectionFactory.setHost("192.168.80.131");
        //设置端口号
        connectionFactory.setPort(5672);
        //设置连接的虚拟主机
        connectionFactory.setVirtualHost("/");
        //设置访问虚拟主机的用户名和密码
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        //获取连接对象
        Connection connection = connectionFactory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        //通道绑定对应消息队列
        /*
         * 参数1 queue：队列名称(不存在自动创建)
         * 参数2 durable：用来定义队列特性是否需要持久化(为true该队列将在服务器重启后保留下来，持久化到硬盘中)
         * 参数3 exclusive：是否独占队列(为true仅限此连接)
         * 参数4 autoDelete：是否在消费完成后自动删除队列
         * 参数5 arguments：队列的其他属性（构造参数）
         * */
        channel.queueDeclare("hello", true, false, false, null);

        channel.basicConsume("hello", true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("consumerTag:" + consumerTag);
                System.out.println("exchange:" + envelope.getExchange());
                System.out.println("routingKey:" + envelope.getRoutingKey());
                System.out.println("properties:" + properties);
                System.out.println("body" + new String(body));
            }
        });
        //channel.close();
        //connection.close();
        //阻塞防止还没消费完，就退出线程了
        while (true) {

        }

    }
}
