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

    //construcotr creates a user service with the needed entity manager
    public LoginController(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MainPersistenceUnit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        this.userService = new UserService(entityManager) ;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //attatches CORS header
        HttpUtils.attachCORSHeaders(exchange);
        //handles http requests
        switch(exchange.getRequestMethod()){
            case "POST":
                handlePostRequest(exchange);
                break ;
            case "OPTIONS": //returns 204 response with correct CORS headers
                exchange.sendResponseHeaders(204, -1);

                break ;
        }
    }
    private void handlePostRequest(HttpExchange exchange) throws IOException{
        //sends response as json
        exchange.getResponseHeaders().set("Content-Type", "application/json");

        //parses user input from request body json to local json object
        JSONObject userInput = new JSONObject(HttpUtils.readRequestBody(exchange)) ;

        //gets user from database with that email
        UserEntity user = userService.findUserByEmail(userInput.getString("email")) ;

        //if there is no user with that email or passwords don't match it sends response 400
        if(user == null || !user.getPassword().equals(userInput.getString("password"))){
            exchange.sendResponseHeaders(400, -1) ;
        } else if(!user.getIsActive()) {    //if user has not verified email it checks email verification token from input
            //if verification token is correct user gets logged in
            if(userInput.getString("verificationToken").equals(user.getVerificationToken())){
                //activates user, so email verification token is no longer required
                user.setIsActive(true);
                //logs user in
                successfulLogin(user, exchange);
            }else{
                //if email verification token is wrong it sends 403, so user can retry to verify his email
                exchange.sendResponseHeaders(403, -1); //else it sends 403, so user can input his verification token
            }
        }else {
            //logs in user
            successfulLogin(user, exchange);
        }
    }
   private void successfulLogin(UserEntity user, HttpExchange exchange) throws IOException {
        //creates a session verification token and addes it to user
        user.setSessionVerificationToken(createVerificationToken());

        //persists user
        userService.persistUser(user);

       //parses user input from request body json to local json object
        JSONObject response = new JSONObject() ;

        //adds session verification token to response
        response.append("sessionVerificationToken", user.getSessionVerificationToken()) ;

        String responseJsonAsString = response.toString() ;
        //response is 200
        exchange.sendResponseHeaders(200, responseJsonAsString.length());

        //adds session verification token to response body
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseJsonAsString.getBytes());
        outputStream.close();
    }

    //creates verification token
    private String createVerificationToken(){
        return UUID.randomUUID().toString();
    }
}
