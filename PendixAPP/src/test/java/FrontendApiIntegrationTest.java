import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FrontendApiIntegrationTest {
    @Test
    void debeConsumirLaApiDePendientes() throws Exception {
        // Arrange
        InputStream input =
                FrontendApiIntegrationTest.class
                        .getResourceAsStream(
                                "/public/index.html"
                        );

        assertNotNull(
                input,
                "El frontend debe existir"
        );

        String html;

        try (input) {
            html = new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }

        // Assert
        assertTrue(
                html.contains(
                        "__PENDIX_API_URL__"
                )
        );

        assertTrue(
                html.contains(
                        "/api/pendientes"
                )
        );

        assertTrue(
                html.contains(
                        "fetch("
                )
        );

        assertTrue(
                html.contains(
                        "cargarTareas();"
                )
        );

        assertTrue(
                html.contains(
                        "method:'POST'"
                )
        );

        assertTrue(
                html.contains(
                        "method:'PUT'"
                )
        );

        assertTrue(
                html.contains(
                        "method:'DELETE'"
                )
        );
    }

    @Test
    void noDebeUsarLocalStorageComoFuenteDePendientes()
            throws Exception {

        // Arrange
        InputStream input =
                FrontendApiIntegrationTest.class
                        .getResourceAsStream(
                                "/public/index.html"
                        );

        assertNotNull(
                input,
                "El frontend debe existir"
        );

        String html;

        try (input) {
            html = new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }

        // Assert
        assertFalse(
                html.contains(
                        "let tareas=JSON.parse("
                                + "localStorage.getItem("
                                + "'pendixapp_tareas_java5018'"
                )
        );

        assertTrue(
                html.contains(
                        "localStorage.removeItem("
                                + "LEGACY_TASKS_KEY"
                )
        );
    }
}
