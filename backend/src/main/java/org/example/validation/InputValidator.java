package org.example.validation;

import org.example.model.UserEntity;
import org.example.service.UserService;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InputValidator {

    public static List<ValidationError> validateUserInput(JSONObject userInput) {
        List<ValidationError> validationErrors = new ArrayList<>();
        String username = userInput.getString("username");
        String password = userInput.getString("password");
        String confirmPassword = userInput.getString("confirmPassword");
        String emailToEdit = userInput.has("emailToEdit") ? userInput.getString("emailToEdit") : userInput.getString("email");
        String email = userInput.getString("email") ;


        validationErrors.add(validateUsername(username));
        validationErrors.add(validatePassword(password)) ;
        validationErrors.add(validateConfirmPassword(confirmPassword)) ;
        validationErrors.add(validateConfirmPasswordAndPasswordMatch(password, confirmPassword));
        validationErrors.add(validateEmail(emailToEdit)) ;


        if(userInput.has("sessionVerificationToken")){
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MainPersistenceUnit");

            EntityManager entityManager = entityManagerFactory.createEntityManager();

            validationErrors.add(validateSessionToken(userInput.getString("sessionVerificationToken"), email, new UserService(entityManager))) ;
        }

        validationErrors = validationErrors.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return validationErrors;
    }
    public static ValidationError validateSessionToken(String token, String email, UserService userService) {
        UserEntity user = userService.findUserByEmail(email) ;

        if(user == null){
            return new ValidationError("User with that email not found to confirm token") ;
        }
        if(user.getSessionVerificationToken().equals(token)) {
            return null ;
        }else {
            return new ValidationError("Wrong session token, please log in") ;
        }
    }
    public static ValidationError validateUsername(String username) {
        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        if(username.isEmpty()){
            return new ValidationError("Username must not be empty") ;
        }
        if(username.length() < 6){
            return new ValidationError("Username must be at least 6 characters") ;
        }

        if (matcher.matches()) {
            return null;
        } else {
            return new ValidationError("Username must contain only letters and numbers");
        }
    }

    public static ValidationError validateEmail(String email) {
        String regex = "^[a-zA-Z0-9.]+@[a-zA-Z0-9]+\\.[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (email.isEmpty()) {
            return new ValidationError("Email must not be empty");
        }

        if (matcher.matches()) {
            return null;
        } else {
            return new ValidationError("Please enter a valid email");
        }
    }

    public static ValidationError validatePassword(String password) {
        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if(password.isEmpty()){
            return new ValidationError("Password must not be empty") ;
        }
        if (password.length() < 6) {

            return new ValidationError("Password must be at least 6 characters");
        }
        if (matcher.matches()) {
            return null;
        } else {
            return new ValidationError("Password must contain only letters and numbers");
        }
    }

    public static ValidationError validateConfirmPassword(String confirmPassword){
        String regex = "^[a-zA-Z0-9]+$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(confirmPassword);

        if(confirmPassword.isEmpty()){
            return new ValidationError("Confirm password must not be empty") ;
        }
        if (confirmPassword.length() < 6) {

            return new ValidationError("Confirm password must be at least 6 characters");
        }

        if(matcher.matches()){
            return null ;
        }else {
            return new ValidationError("Confirm password must contain only letters and numbers") ;
        }
    }

    public static ValidationError validateConfirmPasswordAndPasswordMatch(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return null;
        } else {
            return new ValidationError("Password and Confirm Password don't match");
        }
    }
}
