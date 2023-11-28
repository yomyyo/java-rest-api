package com.rabbit;

import com.rabbitmq.client.Channel;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/feedback")
public class FeedbackServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String feedbackType = request.getParameter("type");
            String message = "User gave " + feedbackType + " feedback.";

            // Send feedback to the appropriate RabbitMQ queue
            Channel channel = RabbitMQConfig.createChannel();
            channel.basicPublish("", (feedbackType.equals("thumbsUp") ? RabbitMQConfig.getThumbsUpQueue() : RabbitMQConfig.getThumbsDownQueue()), null, message.getBytes());

            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("Feedback submitted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error processing feedback");
        }
    }
}