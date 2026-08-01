import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PendixRouterApiContractTest {
    private static final String JSON =
            "application/json; charset=UTF-8";

    @Test
    void debeCrearUnPendienteConEstadoActivoPredeterminado() {
        // Arrange
        PendixRouter router = crearRouterVacio();

        String body = """
                {
                    "id": 51,
                    "titulo": "Validar contrato HTTP"
                }
                """;

        // Act
        HttpResult result = router.resolver(
                "POST",
                "/api/pendientes",
                body
        );

        // Assert
        assertEquals(
                201,
                result.status()
        );

        assertEquals(
                JSON,
                result.contentType()
        );

        assertTrue(
                result.body().contains(
                        "\"id\":51"
                )
        );

        assertTrue(
                result.body().contains(
                        "\"estado\":\"activo\""
                )
        );
    }

    @Test
    void debeResponderJsonAlConsultarUnPendiente() {
        // Arrange
        PendienteRepository repository =
                new InMemoryPendienteRepository(
                        List.of(
                                new Pendiente(
                                        61,
                                        "Consultar contrato",
                                        "activo"
                                )
                        )
                );

        PendixRouter router =
                new PendixRouter(
                        () -> "<html></html>",
                        new PendienteService(repository)
                );

        // Act
        HttpResult result = router.resolver(
                "GET",
                "/api/pendientes/61"
        );

        // Assert
        assertEquals(
                200,
                result.status()
        );

        assertEquals(
                JSON,
                result.contentType()
        );

        assertTrue(
                result.body().contains(
                        "Consultar contrato"
                )
        );
    }

    @Test
    void debeResponder404ParaUnPendienteInexistente() {
        // Arrange
        PendixRouter router = crearRouterVacio();

        // Act
        HttpResult result = router.resolver(
                "GET",
                "/api/pendientes/999"
        );

        // Assert
        assertEquals(
                404,
                result.status()
        );

        assertEquals(
                JSON,
                result.contentType()
        );

        assertTrue(
                result.body().contains(
                        "\"error\""
                )
        );
    }

    @Test
    void debeResponder405ParaUnMetodoNoPermitido() {
        // Arrange
        PendixRouter router = crearRouterVacio();

        // Act
        HttpResult result = router.resolver(
                "PATCH",
                "/api/pendientes",
                "{}"
        );

        // Assert
        assertEquals(
                405,
                result.status()
        );

        assertEquals(
                JSON,
                result.contentType()
        );

        assertTrue(
                result.body().contains(
                        "Método no permitido"
                )
        );
    }

    @Test
    void debeResponder400ParaUnIdNegativo() {
        // Arrange
        PendixRouter router = crearRouterVacio();

        // Act
        HttpResult result = router.resolver(
                "GET",
                "/api/pendientes/-5"
        );

        // Assert
        assertEquals(
                400,
                result.status()
        );

        assertTrue(
                result.body().contains(
                        "\"error\""
                )
        );
    }

    @Test
    void debeResponder404ParaUnaRutaDesconocida() {
        // Arrange
        PendixRouter router = crearRouterVacio();

        // Act
        HttpResult result = router.resolver(
                "GET",
                "/api/desconocida"
        );

        // Assert
        assertEquals(
                404,
                result.status()
        );

        assertEquals(
                JSON,
                result.contentType()
        );

        assertTrue(
                result.body().contains(
                        "Ruta no encontrada"
                )
        );
    }

    @Test
    void debeResponder409ParaUnIdDuplicado() {
        // Arrange
        PendienteRepository repository =
                new InMemoryPendienteRepository(
                        List.of(
                                new Pendiente(
                                        71,
                                        "Pendiente existente",
                                        "activo"
                                )
                        )
                );

        PendixRouter router =
                new PendixRouter(
                        () -> "<html></html>",
                        new PendienteService(repository)
                );

        String body = """
                {
                    "id": 71,
                    "titulo": "Pendiente duplicado",
                    "estado": "activo"
                }
                """;

        // Act
        HttpResult result = router.resolver(
                "POST",
                "/api/pendientes",
                body
        );

        // Assert
        assertEquals(
                409,
                result.status()
        );

        assertEquals(
                JSON,
                result.contentType()
        );
    }

    private PendixRouter crearRouterVacio() {
        PendienteRepository repository =
                new InMemoryPendienteRepository(
                        List.of()
                );

        return new PendixRouter(
                () -> "<html></html>",
                new PendienteService(repository)
        );
    }
}
