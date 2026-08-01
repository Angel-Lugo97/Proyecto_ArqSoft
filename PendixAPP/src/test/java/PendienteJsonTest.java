import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PendienteJsonTest {
    @Test
    void debeUsarEstadoActivoCuandoNoSeEnvia() {
        // Arrange
        String json = """
                {
                    "id": 101,
                    "titulo": "Preparar demostración"
                }
                """;

        // Act
        Pendiente pendiente =
                PendienteJson.leerCreacion(json);

        // Assert
        assertEquals(101, pendiente.id());
        assertEquals(
                "Preparar demostración",
                pendiente.titulo()
        );
        assertEquals(
                "activo",
                pendiente.estado()
        );
    }

    @Test
    void debeLeerUnaActualizacionUsandoElIdDeLaRuta() {
        // Arrange
        int idRuta = 202;

        String json = """
                {
                    "titulo": "Actualizar documentación",
                    "estado": "completado"
                }
                """;

        // Act
        Pendiente pendiente =
                PendienteJson.leerActualizacion(
                        idRuta,
                        json
                );

        // Assert
        assertEquals(
                202,
                pendiente.id()
        );
        assertEquals(
                "Actualizar documentación",
                pendiente.titulo()
        );
        assertEquals(
                "completado",
                pendiente.estado()
        );
    }

    @Test
    void debeEscaparComillasYBarrasAlEscribirJson() {
        // Arrange
        Pendiente pendiente =
                new Pendiente(
                        303,
                        "Revisar \"API\" en C:\\temp",
                        "activo"
                );

        // Act
        String json =
                PendienteJson.escribir(pendiente);

        // Assert
        assertTrue(
                json.contains(
                        "Revisar \\\"API\\\" en C:\\\\temp"
                )
        );

        assertTrue(
                json.contains(
                        "\"estado\":\"activo\""
                )
        );
    }

    @Test
    void debeRechazarUnaCreacionSinId() {
        // Arrange
        String json = """
                {
                    "titulo": "Pendiente sin identificador"
                }
                """;

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> PendienteJson.leerCreacion(json)
        );
    }

    @Test
    void debeRechazarUnCuerpoQueNoSeaObjetoJson() {
        // Arrange
        String json = """
                [
                    {
                        "id": 1
                    }
                ]
                """;

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> PendienteJson.leerCreacion(json)
        );
    }

    @Test
    void debeRechazarUnEstadoNoPermitido() {
        // Arrange
        String json = """
                {
                    "id": 404,
                    "titulo": "Pendiente inválido",
                    "estado": "cancelado"
                }
                """;

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> PendienteJson.leerCreacion(json)
        );
    }
}
