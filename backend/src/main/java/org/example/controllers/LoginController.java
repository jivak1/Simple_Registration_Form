package org.example.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.model.UserEntity;
import org.example.service.UserService;
import org.example.utils.HttpUtils;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

public class LoginController implements HttpHandler {
    private UserService userService ;

    public LoginController(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MainPersistenceUnit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        this.userService = new UserService(entityManager) ;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpUtils.attachCORSHeaders(exchange);

        switch(exchange.getRequestMethod()){
            case "POST":
                handlePostRequest(exchange);
                break ;
            case "OPTIONS":
                exchange.sendResponseHeaders(204, -1);

                break ;
        }
    }
    private void handlePostRequest(HttpExchange exchange) throws IOException{
        exchange.getResponseHeaders().set("Content-Type", "application/json");

        JSONObject userInput = new JSONObject(HttpUtils.readRequestBody(exchange)) ;

        UserEntity user = userService.findUserByEmail(userInput.getString("email")) ;

        if(user == null || !user.getPassword().equals(userInput.getString("password"))){
            exchange.sendResponseHeaders(400, -1) ;
        } else if(!user.getIsActive()) {
            if(userInput.getString("verificationToken").equals(user.getVerificationToken())){
                successfulLogin(user, exchange);
            }else{
                exchange.sendResponseHeaders(403, -1);
            }
        }else {
            successfulLogin(user, exchange);
        }
    }
   private void successfulLogin(UserEntity user, HttpExchange exchange) throws IOException {
        user.setSessionVerificationToken(createVerificationToken());

        user.setIsActive(true);

        userService.persistUser(user);

        JSONObject response = new JSONObject() ;

        response.append("sessionVerificationToken", user.getSessionVerificationToken()) ;

        String responseJsonAsString = response.toString() ;

        exchange.sendResponseHeaders(200, responseJsonAsString.length());

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseJsonAsString.getBytes());
        outputStream.close();
    }
    private String createVerificationToken(){
        return UUID.randomUUID().toString();
    }
}
