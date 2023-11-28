package com.rabbit;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;

public class RabbitMQConfig {
    private final static String QUEUE_NAME = "thumb";

    public static Channel createChannel() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        return channel;
    }

}
