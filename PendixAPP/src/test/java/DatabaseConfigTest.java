import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DatabaseConfigTest {
    @Test
    void debeUsarConfiguracionLocalPredeterminada() {
        // Arrange
        Map<String, String> environment = Map.of();

        // Act
        DatabaseConfig config =
                DatabaseConfig.desde(environment);

        // Assert
        assertEquals(
                "jdbc:postgresql://localhost:5432/pendix",
                config.url()
        );

        assertEquals("postgres", config.user());
        assertEquals("", config.password());
    }

    @Test
    void debeLeerLasVariablesProporcionadas() {
        // Arrange
        Map<String, String> environment = Map.of(
                "DATABASE_URL",
                "jdbc:postgresql://db.example:5432/pendix_prod",
                "DATABASE_USER",
                "pendix_app",
                "DATABASE_PASSWORD",
                "secret-test-value"
        );

        // Act
        DatabaseConfig config =
                DatabaseConfig.desde(environment);

        // Assert
        assertEquals(
                "jdbc:postgresql://db.example:5432/pendix_prod",
                config.url()
        );

        assertEquals(
                "pendix_app",
                config.user()
        );

        assertEquals(
                "secret-test-value",
                config.password()
        );
    }

    @Test
    void noDebeMostrarLaPasswordEnLosLogs() {
        // Arrange
        DatabaseConfig config =
                DatabaseConfig.desde(Map.of(
                        "DATABASE_PASSWORD",
                        "secret-test-value"
                ));

        // Act
        String description = config.toString();

        // Assert
        assertFalse(
                description.contains("secret-test-value")
        );
    }

    @Test
    void debeRechazarUnaUrlQueNoSeaPostgresql() {
        // Arrange
        Map<String, String> environment = Map.of(
                "DATABASE_URL",
                "jdbc:mysql://localhost:3306/pendix"
        );

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> DatabaseConfig.desde(environment)
        );
    }
}
