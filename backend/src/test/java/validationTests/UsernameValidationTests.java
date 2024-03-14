package validationTests;

import org.example.validation.InputValidator;
import org.example.validation.ValidationError;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UsernameValidationTests {
    @Test
    public void validateUsernameReturnsNoErrorWhenInputCorrect(){
        String username = "johndoe" ;

        ValidationError validationError = InputValidator.validateUsername(username) ;

        assertNull(validationError);
    }
    @Test
    public void validateUsernameReturnsErrorWhenEmptyTest(){
        String username = "" ;

        ValidationError validationError = InputValidator.validateUsername(username) ;

        assertEquals("Username must not be empty", validationError.getMessage());
    }

    @Test
    public void validateUsernameReturnsErrorWhenWrongInput(){
        String username = "#asdfssa" ;

        ValidationError validationError = InputValidator.validateUsername(username) ;

        assertEquals("Username must contain only letters and numbers", validationError.getMessage());
    }

    @Test
    public void validateUsernameReturnsErrorWhenUsernameTooShort(){
        String username = "123" ;

        ValidationError validationError = InputValidator.validateUsername(username) ;

        assertEquals("Username must be at least 6 characters", validationError.getMessage());
    }
}
