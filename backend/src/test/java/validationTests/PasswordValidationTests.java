package validationTests;

import org.example.validation.InputValidator;
import org.example.validation.ValidationError;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidationTests {
    @Test
    public void validatePasswordReturnsNoErrorOnCorrectInput(){
        String password = "123321" ;

        ValidationError validationError = InputValidator.validatePassword(password) ;

        assertNull(validationError);
    }

    @Test
    public void validatePasswordReturnsErrorOnEmptyPassword(){
        String password = "";

        ValidationError validationError = InputValidator.validatePassword(password) ;

        assertEquals("Password must not be empty", validationError.getMessage());
    }

    @Test
    public void validatePasswordReturnsErrorOnPasswordTooShort(){
        String password = "123" ;

        ValidationError validationError = InputValidator.validatePassword(password) ;

        assertEquals("Password must be at least 6 characters", validationError.getMessage());
    }

    @Test
    public void validatePasswordReturnsErrorOnForbiddenCharacters(){
        String password = "#123@./';" ;

        ValidationError validationError = InputValidator.validatePassword(password) ;

        assertEquals("Password must contain only letters and numbers", validationError.getMessage());
    }

    @Test
    public void validateConfirmPasswordReturnsNoErrorOnCorrectInput(){
        String confirmPassword = "abcdef" ;

        ValidationError validationError = InputValidator.validateConfirmPassword(confirmPassword) ;

        assertNull(validationError);
    }

    @Test
    public void validateConfirmPasswordReturnsErrorOnEmptyPassword(){
        String confirmPassword = "";

        ValidationError validationError = InputValidator.validateConfirmPassword(confirmPassword) ;

        assertEquals("Confirm password must not be empty", validationError.getMessage());
    }

    @Test
    public void validateConfirmPasswordReturnsErrorOnPasswordTooShort(){
        String confirmPassword = "123" ;

        ValidationError validationError = InputValidator.validateConfirmPassword(confirmPassword) ;

        assertEquals("Confirm password must be at least 6 characters", validationError.getMessage());
    }

    @Test
    public void validateConfirmPasswordReturnsErrorOnForbiddenCharacters(){
        String confirmPassword = "#123@./';" ;

        ValidationError validationError = InputValidator.validateConfirmPassword(confirmPassword) ;

        assertEquals("Confirm password must contain only letters and numbers", validationError.getMessage());
    }

    @Test
    public void validateErrorWhenPasswordAndConfirmPasswordDonNotMatch(){
        String password = "123321" ;

        String confirmPassword = "abcdef" ;

        ValidationError validationError = InputValidator.validateConfirmPasswordAndPasswordMatch(password, confirmPassword) ;

        assertEquals("Password and Confirm Password don't match", validationError.getMessage());
    }

    @Test
    public void validateNoErrorWhenPasswordAndConfirmPasswordDonNotMatch(){
        String password = "123321" ;

        String confirmPassword = "123321" ;

        ValidationError validationError = InputValidator.validateConfirmPasswordAndPasswordMatch(password, confirmPassword) ;

        assertNull(validationError);
    }
}
