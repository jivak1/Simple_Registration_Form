package utilsTests;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.example.utils.HttpUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpUtilsTests {
    private HttpExchange httpExchange;

    @BeforeEach
    public void setUp() {
        httpExchange = mock(HttpExchange.class);
    }

    @Test
    public void readRequestBodyShouldReturnCorrectStringTest() throws IOException {
        String requestBody = "123321 test test body";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(requestBody.getBytes());
        when(httpExchange.getRequestBody()).thenReturn(inputStream);

        String result = HttpUtils.readRequestBody(httpExchange);

        assertEquals(requestBody, result);
    }

    @Test
    public void attachCORSHeadersShouldAddCorrectHeadersTest() {
        Headers headers = new Headers();
        when(httpExchange.getResponseHeaders()).thenReturn(headers);

        HttpUtils.attachCORSHeaders(httpExchange);

        assertEquals("*", headers.getFirst("Access-Control-Allow-Origin"));
        assertEquals("GET, POST, PUT, OPTIONS", headers.getFirst("Access-Control-Allow-Methods"));
        assertEquals("Content-Type, Authorization", headers.getFirst("Access-Control-Allow-Headers"));
    }
}
