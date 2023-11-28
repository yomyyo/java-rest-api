package com.rabbit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/members";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                String sql = "SELECT * FROM names";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    try(ResultSet rs = stmt.executeQuery()){
                        response.getWriter().println("DB Responses: "); 

                        while(rs.next()){
                            String content = rs.getString("name");
                            response.getWriter().println("- " + content);
                        }
                    }
                    
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().println(e.getMessage());
                response.getWriter().println("Error connecting to the database");
            }
        } catch (Exception e){
            response.getWriter().println(e.getMessage());
        }
        
    }


    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {

                // Read the request body
                BufferedReader reader = req.getReader();
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }

                // Extract the message from the request body
                String name = requestBody.toString();

                // Insert the message into the database
                String sql = "INSERT INTO names (name) VALUES (?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, name);
                    statement.executeUpdate();
                }

                // Send a response to the client
                res.setContentType("text/plain");
                res.setStatus(HttpServletResponse.SC_CREATED);
                PrintWriter out = res.getWriter();
                out.println("Name successfully added to the database");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            res.setContentType("text/plain");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = res.getWriter();
            out.println("Error processing the request");
        }
    }
}

