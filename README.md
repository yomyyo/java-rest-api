# Java Servlet Feedback Queue

A Java web application experiment that accepts thumbs-up or thumbs-down
feedback, publishes the event to RabbitMQ, and includes a consumer that can
store received messages in MySQL.

## Flow

1. A client sends `POST /thumbs` with a `type` form parameter.
2. `ThumbsServlet` publishes the message to a RabbitMQ queue.
3. `ThumbConsumer` listens to the thumbs-up and thumbs-down queues.
4. The consumer writes received messages to a MySQL `thumbs` table.

## Technology

- Java Servlet API
- Maven WAR project
- RabbitMQ Java client
- MySQL JDBC driver
- Tomcat Maven plugin

## Requirements

- JDK 8 or newer
- Maven
- RabbitMQ running on `localhost:5672`
- MySQL for the consumer persistence path

## Database Setup

```sql
CREATE DATABASE feedback;
USE feedback;

CREATE TABLE thumbs (
  id INT AUTO_INCREMENT PRIMARY KEY,
  thumb VARCHAR(255) NOT NULL
);
```

Update the placeholder JDBC URL, username, and password in
`ThumbConsumer.java` before running the consumer.

## Build and Run

```bash
cd rabbit
mvn clean package
mvn tomcat7:run
```

The application is packaged as `rabbit.war`. A common local URL is
`http://localhost:8080/rabbit/`.

Start `ThumbConsumer` separately from an IDE or with a Maven-generated runtime
classpath.

## API Example

```bash
curl -X POST \
  -d "type=thumbsUp" \
  http://localhost:8080/rabbit/thumbs
```

Use `thumbsDown` to publish to the other queue.

## Project Status

This is a learning prototype, not a production REST service. Connection values
are configured in source, queue connections are opened per request, and the
consumer requires manual database configuration.
