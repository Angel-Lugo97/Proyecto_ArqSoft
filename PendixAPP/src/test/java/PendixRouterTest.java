import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PendixRouterTest {
    @Test
    void debeEntregarLaPaginaPrincipal() {
        // Arrange
        PendixRouter router = new PendixRouter(
                () -> "<html>PendixAPP</html>", new PendienteService());

        // Act
        HttpResult result = router.resolver("GET", "/");

        // Assert
        assertEquals(200, result.status());
        assertEquals("text/html; charset=UTF-8", result.contentType());
        assertTrue(result.body().contains("PendixAPP"));
    }

    @Test
    void debeRechazarMetodosQueNoSeanGet() {
        // Arrange
        PendixRouter router = new PendixRouter(
                () -> "<html></html>", new PendienteService());

        // Act
        HttpResult result = router.resolver("POST", "/");

        // Assert
        assertEquals(405, result.status());
        assertEquals("Metodo no permitido", result.body());
    }

    @Test
    void debeResponder404ParaUnaRutaDesconocida() {
        // Arrange
        PendixRouter router = new PendixRouter(
                () -> "<html></html>", new PendienteService());

        // Act
        HttpResult result = router.resolver("GET", "/ruta-inexistente");

        // Assert
        assertEquals(404, result.status());
    }

    @Test
    void debeInformarQueLaApiEstaDisponible() {
        // Arrange
        PendixRouter router = new PendixRouter(
                () -> "<html></html>",
                new PendienteService()
        );

        // Act
        HttpResult result = router.resolver(
                "GET",
                "/api/health"
        );

        // Assert
        assertEquals(200, result.status());
        assertEquals(
                "application/json; charset=UTF-8",
                result.contentType()
        );
        assertTrue(result.body().contains("\"status\":\"UP\""));
        assertTrue(result.body().contains("PendixAPP"));
    }
}
