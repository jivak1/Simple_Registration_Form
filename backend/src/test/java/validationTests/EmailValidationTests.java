package validationTests;

import org.example.validation.InputValidator;
import org.example.validation.ValidationError;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class EmailValidationTests {

    @Test
    public void validateEmailReturnsNoErrorOnCorrectInput(){
        String email = "john@doe.com" ;

        ValidationError validationError = InputValidator.validateEmail(email) ;

        assertNull(validationError);
    }

    @Test
    public void validateEmailReturnsErrorOnEmptyEmail(){
        String email = "" ;

        ValidationError validationError = InputValidator.validateEmail(email) ;

        assertEquals("Email must not be empty", validationError.getMessage());
    }

    @Test
    public void validateEmailReturnsErrorOnForbiddenCharacters(){
        String email = "!234.asd@asd.fdg" ;

        ValidationError validationError = InputValidator.validateEmail(email) ;

        assertEquals("Please enter a valid email", validationError.getMessage());
    }
}
