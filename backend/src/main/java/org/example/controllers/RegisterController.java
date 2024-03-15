package org.example.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;

import jakarta.mail.MessagingException;
import org.example.utils.HttpUtils;
import org.example.validation.InputValidator;
import org.example.validation.ValidationError;
import org.example.email.EmailSender;
import org.example.model.UserEntity;
import org.example.service.UserService;
import org.json.* ;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegisterController implements HttpHandler {
    private UserService userService ;

    //construcotr creates a user service with the needed entity manager
    public RegisterController(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MainPersistenceUnit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        this.userService = new UserService(entityManager) ;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //adds CORS headers
        HttpUtils.attachCORSHeaders(exchange);

        //handle requests
        switch(exchange.getRequestMethod()){
            case "POST":
                handlePostRequest(exchange);
                break ;
            case "OPTIONS": //for CORS
                exchange.sendResponseHeaders(204, -1);

                break ;
        }
    }
    private void handlePostRequest(HttpExchange exchange) throws IOException{
        //sends response as json
        exchange.getResponseHeaders().set("Content-Type", "application/json");

        //parses user input from request body json to local json object
        JSONObject userInput = new JSONObject(HttpUtils.readRequestBody(exchange)) ;
        //validates input and puts all validation errors in a list
        List<ValidationError> validationErrors = InputValidator.validateUserInput(userInput);
        //gets user from database with that email
        UserEntity user = userService.findUserByEmail(userInput.getString("email")) ;

        //if user with that email is not found and there are no errors it creates new user nad persists it
        if(user == null && validationErrors.isEmpty()){
            user = new UserEntity(userInput.getString("username"), userInput.getString("password"), userInput.getString("email"), createVerificationToken()) ;
            userService.persistUser(user);
            //sends email verification token via email
            try {
                sendVerificationEmail(userInput.getString("email"), user);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            //returns response with code 200
            exchange.sendResponseHeaders(200, -1);
        }else{
            //adds validation error if user with the same email exists
            if(!userInput.getString("email").isEmpty()) {
                ValidationError emailExistsError = new ValidationError("User with this email already exists");
                validationErrors.add(emailExistsError) ;
            }

            JSONObject response = new JSONObject() ;

            //appends are validation errors
            response.append("errors", validationErrors.stream().map(ValidationError::getMessage).toArray()) ;

            String responseJsonAsString = response.toString() ;
            //sends code 400
            exchange.sendResponseHeaders(400, responseJsonAsString.length());
            //adds all validation errors to response body
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(responseJsonAsString.getBytes());
            outputStream.close();
        }
    }
    //method sends verification email using the email sender class
    private void sendVerificationEmail(String email, UserEntity user) throws MessagingException {
        EmailSender.sendVerificationEmail("verificator48@gmail.com", email, "Verification Email", user.getVerificationToken());
    }

    //creates email verification token
    private String createVerificationToken(){
        return UUID.randomUUID().toString();
    }

}
