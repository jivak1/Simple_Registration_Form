package validationTests;
import org.example.validation.ValidationError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class ValidationErrorClassTests {
    @Test
    public void constructorTest(){
        ValidationError error = new ValidationError("Test message");

        assertEquals("Test message", error.getMessage());
    }
    @Test
    public void gettersAndSettersTest(){
        ValidationError error = new ValidationError("");

        error.setMessage("New message");

        assertEquals("New message", error.getMessage());
    }
}
