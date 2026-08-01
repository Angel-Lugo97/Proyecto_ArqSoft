import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class JdbcPendienteRepositoryIntegrationTest {
    private static final int TEST_ID = 900001;

    @Test
    void debeGuardarBuscarActualizarYListarEnPostgresql()
            throws Exception {

        assumeTrue(
                Boolean.parseBoolean(
                        System.getenv(
                                "RUN_DATABASE_TESTS"
                        )
                ),
                "Prueba PostgreSQL deshabilitada"
        );

        DatabaseConnectionFactory connectionFactory =
                new DatabaseConnectionFactory(
                        DatabaseConfig.desdeEntorno()
                );

        new DatabaseInitializer(
                connectionFactory
        ).inicializar();

        JdbcPendienteRepository repository =
                new JdbcPendienteRepository(
                        connectionFactory
                );

        eliminarPendienteDePrueba(
                connectionFactory
        );

        try {
            Pendiente guardado =
                    repository.guardar(
                            new Pendiente(
                                    TEST_ID,
                                    "Validar persistencia JDBC",
                                    "activo"
                            )
                    );

            assertEquals(
                    TEST_ID,
                    guardado.id()
            );

            assertTrue(
                    repository
                            .buscarPorId(TEST_ID)
                            .isPresent()
            );

            Pendiente actualizado =
                    repository.actualizar(
                            guardado.completar()
                    );

            assertEquals(
                    "completado",
                    actualizado.estado()
            );

            assertTrue(
                    repository
                            .listar()
                            .stream()
                            .anyMatch(
                                    pendiente ->
                                            pendiente.id()
                                                    == TEST_ID
                            )
            );
        } finally {
            eliminarPendienteDePrueba(
                    connectionFactory
            );
        }
    }

    private void eliminarPendienteDePrueba(
            DatabaseConnectionFactory connectionFactory
    ) throws Exception {
        try (
                Connection connection =
                        connectionFactory.abrir();

                PreparedStatement statement =
                        connection.prepareStatement(
                                "DELETE FROM pendientes "
                                        + "WHERE id = ?"
                        )
        ) {
            statement.setInt(
                    1,
                    TEST_ID
            );

            statement.executeUpdate();
        }
    }
}
