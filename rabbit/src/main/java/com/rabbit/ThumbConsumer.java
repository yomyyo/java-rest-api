package com.rabbit;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ThumbConsumer {

    // Database configuration
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/yourdatabase";
    private static final String JDBC_USER = "your_mysql_username";
    private static final String JDBC_PASSWORD = "your_mysql_password";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();

        // RabbitMQ server host
        factory.setHost("localhost"); 

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declare the thumbs-up queue
            channel.queueDeclare(RabbitMQConfig.getThumbsUpQueue(), false, false, false, null);

            // Declare the thumbs-down queue
            channel.queueDeclare(RabbitMQConfig.getThumbsDownQueue(), false, false, false, null);

            // Set up a consumer for thumbs-up queue
            // Todo: Set up insert to DB
            Consumer thumbsUpConsumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    saveThumbToDb(message);
                    System.out.println("Received Thumbs Up: " + message);
                }
            };

            // Set up a consumer for thumbs-down queue
            // Todo: Set up insert to DB
            Consumer thumbsDownConsumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    saveThumbToDb(message);
                    System.out.println("Received Thumbs Down: " + message);
                }
            };

            // Start consuming messages from the queues
            channel.basicConsume(RabbitMQConfig.getThumbsUpQueue(), true, thumbsUpConsumer);
            channel.basicConsume(RabbitMQConfig.getThumbsDownQueue(), true, thumbsDownConsumer);

            System.out.println("Feedback Consumers Started. Waiting for feedback...");
            // Keep the consumers running to listen for incoming messages
            while (true) {
                Thread.sleep(1000);
            }
        }
    }

    private static void saveThumbToDb(String thumb) {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {

                // Insert the feedback into the database
                String sql = "INSERT INTO thumbs (thumb) VALUES (?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, thumb);
                    statement.executeUpdate();
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println("Error storing feedback in the database");
        }
    }
}
