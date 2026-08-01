import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PendixRouterTest {
    private final PendixRouter router =
            new PendixRouter(new StaticResourceService(), new PendienteService());

    @Test
    void debeEntregarLaPaginaPrincipal() {
        HttpResult result = router.resolver("GET", "/");

        assertEquals(200, result.status());
        assertEquals("text/html; charset=UTF-8", result.contentType());
        assertTrue(result.body().contains("PendixAPP"));
        assertTrue(result.body().contains("/styles.css"));
        assertTrue(result.body().contains("/app.js"));
    }

    @Test
    void debeEntregarLosRecursosWeb() {
        HttpResult css = router.resolver("GET", "/styles.css");
        HttpResult javascript = router.resolver("GET", "/app.js");

        assertEquals(200, css.status());
        assertEquals("text/css; charset=UTF-8", css.contentType());
        assertTrue(css.body().contains(".phone"));
        assertEquals(200, javascript.status());
        assertEquals("application/javascript; charset=UTF-8", javascript.contentType());
        assertTrue(javascript.body().contains("render()"));
    }

    @Test
    void debeResponderElEndpointDeSalud() {
        HttpResult result = router.resolver("GET", "/health");

        assertEquals(200, result.status());
        assertEquals("application/json; charset=UTF-8", result.contentType());
        assertEquals("{\"status\":\"ok\"}", result.body());
    }

    @Test
    void debeEntregarUnaVersionDeServidor() {
        HttpResult result = router.resolver("GET", "/version");

        assertEquals(200, result.status());
        assertEquals("application/json; charset=UTF-8", result.contentType());
        assertTrue(result.body().matches("\\{\"version\":\"[0-9]+\"\\}"));
    }

    @Test
    void debeRechazarMetodosQueNoSeanGet() {
        HttpResult result = router.resolver("POST", "/");

        assertEquals(405, result.status());
        assertEquals("Metodo no permitido", result.body());
    }

    @Test
    void debeResponder404ParaUnaRutaDesconocida() {
        HttpResult result = router.resolver("GET", "/ruta-inexistente");

        assertEquals(404, result.status());
    }
}
