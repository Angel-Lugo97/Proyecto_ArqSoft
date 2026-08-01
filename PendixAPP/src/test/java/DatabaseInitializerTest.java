import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseInitializerTest {
    @Test
    void debeCargarElEsquemaDePendientes() {
        // Act
        List<String> statements =
                DatabaseInitializer.cargarSentencias(
                        "/db/schema.sql"
                );

        // Assert
        assertEquals(
                2,
                statements.size()
        );

        assertTrue(
                statements.stream().anyMatch(
                        statement ->
                                statement.contains(
                                        "CREATE TABLE IF NOT EXISTS pendientes"
                                )
                )
        );

        assertTrue(
                statements.stream().anyMatch(
                        statement ->
                                statement.contains(
                                        "CREATE INDEX IF NOT EXISTS"
                                )
                )
        );
    }

    @Test
    void debeCargarLosDatosIniciales() {
        // Act
        List<String> statements =
                DatabaseInitializer.cargarSentencias(
                        "/db/seed.sql"
                );

        // Assert
        assertEquals(
                2,
                statements.size()
        );

        assertTrue(
                statements.get(0).contains(
                        "INSERT INTO pendientes"
                )
        );

        assertTrue(
                statements.get(0).contains(
                        "ON CONFLICT (id) DO NOTHING"
                )
        );

        assertTrue(
                statements.get(1).contains(
                        "pg_get_serial_sequence"
                )
        );
    }

    @Test
    void debeIgnorarComentariosYLineasVacias() {
        // Arrange
        String sql = """
                -- Primer comentario

                CREATE TABLE ejemplo (
                    id INTEGER
                );

                -- Segundo comentario
                INSERT INTO ejemplo (id)
                VALUES (1);
                """;

        // Act
        List<String> statements =
                DatabaseInitializer.separarSentencias(
                        sql
                );

        // Assert
        assertEquals(
                2,
                statements.size()
        );

        assertTrue(
                statements.get(0).startsWith(
                        "CREATE TABLE ejemplo"
                )
        );

        assertTrue(
                statements.get(1).startsWith(
                        "INSERT INTO ejemplo"
                )
        );
    }

    @Test
    void debeRechazarUnRecursoInexistente() {
        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        DatabaseInitializer
                                .cargarSentencias(
                                        "/db/inexistente.sql"
                                )
        );
    }
}
