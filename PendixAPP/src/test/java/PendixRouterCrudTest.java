import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PendixRouterCrudTest {
    @Test
    void debeCrearConsultarActualizarYEliminarUnPendiente() {
        // Arrange
        PendixRouter router = crearRouterVacio();

        String creacion = """
                {
                    "id": 50,
                    "titulo": "Preparar despliegue",
                    "estado": "activo"
                }
                """;

        // Act: crear
        HttpResult creado = router.resolver(
                "POST",
                "/api/pendientes",
                creacion
        );

        // Assert: crear
        assertEquals(
                201,
                creado.status()
        );

        assertTrue(
                creado.body().contains(
                        "\"id\":50"
                )
        );

        // Act: consultar
        HttpResult encontrado = router.resolver(
                "GET",
                "/api/pendientes/50"
        );

        // Assert: consultar
        assertEquals(
                200,
                encontrado.status()
        );

        assertTrue(
                encontrado.body().contains(
                        "Preparar despliegue"
                )
        );

        String actualizacion = """
                {
                    "titulo": "Despliegue preparado",
                    "estado": "completado"
                }
                """;

        // Act: actualizar
        HttpResult actualizado = router.resolver(
                "PUT",
                "/api/pendientes/50",
                actualizacion
        );

        // Assert: actualizar
        assertEquals(
                200,
                actualizado.status()
        );

        assertTrue(
                actualizado.body().contains(
                        "\"estado\":\"completado\""
                )
        );

        // Act: eliminar
        HttpResult eliminado = router.resolver(
                "DELETE",
                "/api/pendientes/50"
        );

        // Assert: eliminar
        assertEquals(
                204,
                eliminado.status()
        );

        HttpResult inexistente = router.resolver(
                "GET",
                "/api/pendientes/50"
        );

        assertEquals(
                404,
                inexistente.status()
        );
    }

    @Test
    void debeRechazarUnJsonInvalido() {
        // Arrange
        PendixRouter router = crearRouterVacio();

        // Act
        HttpResult result = router.resolver(
                "POST",
                "/api/pendientes",
                """
                {
                    "titulo": "Sin identificador"
                }
                """
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
    void debeResponderConflictoParaUnIdDuplicado() {
        // Arrange
        PendixRouter router = crearRouterVacio();

        String json = """
                {
                    "id": 80,
                    "titulo": "Pendiente duplicado",
                    "estado": "activo"
                }
                """;

        router.resolver(
                "POST",
                "/api/pendientes",
                json
        );

        // Act
        HttpResult duplicado = router.resolver(
                "POST",
                "/api/pendientes",
                json
        );

        // Assert
        assertEquals(
                409,
                duplicado.status()
        );
    }

    @Test
    void debeRechazarUnIdDeRutaInvalido() {
        // Arrange
        PendixRouter router = crearRouterVacio();

        // Act
        HttpResult result = router.resolver(
                "GET",
                "/api/pendientes/abc"
        );

        // Assert
        assertEquals(
                400,
                result.status()
        );
    }

    private PendixRouter crearRouterVacio() {
        PendienteRepository repository =
                new InMemoryPendienteRepository(
                        List.of()
                );

        return new PendixRouter(
                () -> "<html></html>",
                new PendienteService(
                        repository
                )
        );
    }
}
