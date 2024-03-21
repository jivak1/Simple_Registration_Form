package org.example.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.validation.InputValidator;
import org.example.utils.HttpUtils;
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

    //construcotr creates a user service with the needed entity manager
    public EditUserController(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MainPersistenceUnit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        this.userService = new UserService(entityManager) ;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //adds cors headers
        HttpUtils.attachCORSHeaders(exchange);

        //handle requests
        switch(exchange.getRequestMethod()){
            case "PUT":
                //haandles put request
                handlePutRequest(exchange);
                break ;
            case "OPTIONS": //for CORS
                exchange.sendResponseHeaders(204, -1);
                break ;
        }
    }

    private void handlePutRequest(HttpExchange exchange) throws IOException{
        //sends response as json
        exchange.getResponseHeaders().set("Content-Type", "application/json");

        //parses user input from request body json to local json object
        JSONObject userInput = new JSONObject(HttpUtils.readRequestBody(exchange)) ;

        //gets user from database with that email
        UserEntity user = userService.findUserByEmail(userInput.getString("email")) ;

        //validates input and puts all validation errors in a list
        List<ValidationError> validationErrors = InputValidator.validateUserInput(userInput);

        //if user with that email exists and there are no validation errors it modifyes it based on user input
        if(user != null && validationErrors.isEmpty()) {
            String username = userInput.getString("username") ;
            String password = userInput.getString("password") ;
            String email = userInput.getString("emailToEdit") ;

            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);

            //persists changed user
            userService.persistUser(user);
            //response is 200
            exchange.sendResponseHeaders(200, -1);

        }else{
            JSONObject response = new JSONObject() ;

            //appends validation errors to response
            response.append("errors", validationErrors.stream().map(ValidationError::getMessage).toArray()) ;

            String responseJsonAsString = response.toString() ;
            //response is 400
            exchange.sendResponseHeaders(400, responseJsonAsString.length());
            //adds errors to response body
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(responseJsonAsString.getBytes());
            outputStream.close();
        }
    }
}
