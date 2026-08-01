import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DatabaseInitializer {
    private static final String SCHEMA_RESOURCE =
            "/db/schema.sql";

    private static final String SEED_RESOURCE =
            "/db/seed.sql";

    private final DatabaseConnectionFactory connectionFactory;

    public DatabaseInitializer(
            DatabaseConnectionFactory connectionFactory
    ) {
        this.connectionFactory = Objects.requireNonNull(
                connectionFactory,
                "connectionFactory no puede ser null"
        );
    }

    public void inicializar() throws SQLException {
        List<String> schemaStatements =
                cargarSentencias(SCHEMA_RESOURCE);

        List<String> seedStatements =
                cargarSentencias(SEED_RESOURCE);

        try (Connection connection =
                     connectionFactory.abrir()) {

            boolean originalAutoCommit =
                    connection.getAutoCommit();

            connection.setAutoCommit(false);

            try {
                ejecutar(
                        connection,
                        schemaStatements
                );

                ejecutar(
                        connection,
                        seedStatements
                );

                connection.commit();
            } catch (SQLException | RuntimeException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }

                throw e;
            } finally {
                connection.setAutoCommit(
                        originalAutoCommit
                );
            }
        }
    }

    static List<String> cargarSentencias(
            String resourcePath
    ) {
        Objects.requireNonNull(
                resourcePath,
                "resourcePath no puede ser null"
        );

        try (InputStream input =
                     DatabaseInitializer.class
                             .getResourceAsStream(
                                     resourcePath
                             )) {

            if (input == null) {
                throw new IllegalArgumentException(
                        "No se encontró el recurso SQL: "
                                + resourcePath
                );
            }

            String sql = new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            return separarSentencias(sql);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo leer el recurso SQL: "
                            + resourcePath,
                    e
            );
        }
    }

    static List<String> separarSentencias(
            String sql
    ) {
        Objects.requireNonNull(
                sql,
                "sql no puede ser null"
        );

        List<String> statements =
                new ArrayList<>();

        StringBuilder current =
                new StringBuilder();

        for (String line : sql.split("\\R")) {
            String trimmed = line.trim();

            if (
                    trimmed.isEmpty()
                            || trimmed.startsWith("--")
            ) {
                continue;
            }

            current.append(trimmed).append(' ');

            if (trimmed.endsWith(";")) {
                agregarSentencia(
                        statements,
                        current
                );
            }
        }

        if (!current.toString().isBlank()) {
            agregarSentencia(
                    statements,
                    current
            );
        }

        return List.copyOf(statements);
    }

    private static void agregarSentencia(
            List<String> statements,
            StringBuilder current
    ) {
        String statement =
                current.toString().trim();

        if (statement.endsWith(";")) {
            statement = statement.substring(
                    0,
                    statement.length() - 1
            ).trim();
        }

        if (!statement.isBlank()) {
            statements.add(statement);
        }

        current.setLength(0);
    }

    private static void ejecutar(
            Connection connection,
            List<String> statements
    ) throws SQLException {
        try (Statement statement =
                     connection.createStatement()) {

            for (String sql : statements) {
                statement.execute(sql);
            }
        }
    }
}
