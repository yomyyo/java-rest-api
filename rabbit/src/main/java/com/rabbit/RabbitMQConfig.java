package com.rabbit;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;

public class RabbitMQConfig {
    private final static String THUMBS_UP_QUEUE = "thumbs_up_queue";
    private final static String THUMBS_DOWN_QUEUE = "thumbs_down_queue";

    public static Channel createChannel() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();

        // Maybe you want to do factory.setUsername and factory.setPassword here too the specify server
        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(THUMBS_UP_QUEUE, false, false, false, null);
        channel.queueDeclare(THUMBS_DOWN_QUEUE, false, false, false, null);

        return channel;
    }

    public static String getThumbsUpQueue() {
        return THUMBS_UP_QUEUE;
    }

    public static String getThumbsDownQueue() {
        return THUMBS_DOWN_QUEUE;
    }

}
