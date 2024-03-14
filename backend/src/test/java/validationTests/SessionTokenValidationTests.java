package validationTests;

import static org.mockito.Mockito.*;

import org.example.model.UserEntity;
import org.example.service.UserService;
import org.example.validation.InputValidator;
import org.example.validation.ValidationError;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SessionTokenValidationTests {
    @Mock
    private UserService userService;

    @InjectMocks
    private InputValidator inputValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateSessionTokenReturnsNoErrorWhenTokensMatch(){
        String token = "validToken";
        String email = "user@example.com";

        UserEntity user = new UserEntity();
        user.setSessionVerificationToken("validToken");

        when(userService.findUserByEmail(email)).thenReturn(user);

        ValidationError validationError = inputValidator.validateSessionToken(token, email, userService) ;

        assertNull(validationError);
    }

    @Test
    public void validateSessionTokenReturnsErrorWhenUserNotFound(){
        String token = "validToken";
        String email = "user@example.com";
        when(userService.findUserByEmail(email)).thenReturn(null);

        ValidationError validationError = inputValidator.validateSessionToken(token, email, userService);

        assertEquals("User with that email not found to confirm token", validationError.getMessage());
    }

    @Test
    public void validateSessionTokenReturnsErrorWhenTokenMissmatch(){
        String token = "validToken";
        String email = "user@example.com";

        UserEntity user = new UserEntity();
        user.setSessionVerificationToken("invalidToken");

        when(userService.findUserByEmail(email)).thenReturn(user);

        ValidationError validationError = inputValidator.validateSessionToken(token, email, userService);

        assertEquals("Wrong session token, please log in", validationError.getMessage());
    }
}
