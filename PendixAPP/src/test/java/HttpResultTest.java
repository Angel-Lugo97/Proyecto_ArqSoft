import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpResultTest {
    @Test
    void debeConservarLosDatosDeLaRespuesta() {
        // Arrange
        int status = 200;
        String contentType = "application/json";
        String body = "{}";

        // Act
        HttpResult result = new HttpResult(status, contentType, body);

        // Assert
        assertEquals(200, result.status());
        assertEquals("application/json", result.contentType());
        assertEquals("{}", result.body());
    }

    @Test
    void debeRechazarUnEstadoHttpFueraDeRango() {
        // Arrange
        int statusInvalido = 700;

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new HttpResult(statusInvalido, "text/plain", "error"));
    }
}
