package com.rabbit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.rabbitmq.client.Channel;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MessageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            BufferedReader reader = req.getReader();

            StringBuilder reqBody = new StringBuilder();

            String line;

            while((line = reader.readLine()) != null){
                reqBody.append(line);
            }

            String message = reqBody.toString();


        } catch (Exception e) {
            res.getWriter().println(e.getMessage());
        }
    }
}
