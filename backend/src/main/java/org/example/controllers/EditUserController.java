package org.example.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.utils.HttpUtils;
import org.example.validation.InputValidator;
import org.example.validation.ValidationError;
import org.example.model.UserEntity;
import org.example.service.UserService;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EditUserController implements HttpHandler {
    private UserService userService ;

    public EditUserController(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MainPersistenceUnit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        this.userService = new UserService(entityManager) ;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpUtils.attachCORSHeaders(exchange);

        switch(exchange.getRequestMethod()){
            case "PUT":
                handlePutRequest(exchange);
                break ;
            case "OPTIONS":
                exchange.sendResponseHeaders(204, -1);
                break ;
        }
    }

    private void handlePutRequest(HttpExchange exchange) throws IOException{
        exchange.getResponseHeaders().set("Content-Type", "application/json");

        JSONObject userInput = new JSONObject(HttpUtils.readRequestBody(exchange)) ;

        UserEntity user = userService.findUserByEmail(userInput.getString("email")) ;

        List<ValidationError> validationErrors = InputValidator.validateUserInput(userInput);

        if(user != null && validationErrors.isEmpty()) {
            String username = userInput.getString("username") ;
            String password = userInput.getString("password") ;
            String email = userInput.getString("emailToEdit") ;

            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);

            userService.persistUser(user);

            exchange.sendResponseHeaders(200, -1);

        }else{
            JSONObject response = new JSONObject() ;


            response.append("errors", validationErrors.stream().map(ValidationError::getMessage).toArray()) ;

            String responseJsonAsString = response.toString() ;

            exchange.sendResponseHeaders(400, responseJsonAsString.length());

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(responseJsonAsString.getBytes());
            outputStream.close();
        }
    }
}
