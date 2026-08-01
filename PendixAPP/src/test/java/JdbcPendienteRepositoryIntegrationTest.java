import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class JdbcPendienteRepositoryIntegrationTest {
    private static final int CRUD_TEST_ID = 900001;
    private static final int DELETE_TEST_ID = 900002;
    private static final int DUPLICATE_TEST_ID = 900003;

    @Test
    void debeGuardarBuscarActualizarYListarEnPostgresql()
            throws Exception {

        verificarQueLasPruebasEstanHabilitadas();

        DatabaseConnectionFactory connectionFactory =
                crearConnectionFactory();

        inicializar(connectionFactory);

        eliminarPendienteDePrueba(
                connectionFactory,
                CRUD_TEST_ID
        );

        JdbcPendienteRepository repository =
                new JdbcPendienteRepository(
                        connectionFactory
                );

        try {
            // Guardar
            Pendiente guardado =
                    repository.guardar(
                            new Pendiente(
                                    CRUD_TEST_ID,
                                    "Validar persistencia JDBC",
                                    "activo"
                            )
                    );

            assertEquals(
                    CRUD_TEST_ID,
                    guardado.id()
            );

            // Buscar
            assertTrue(
                    repository
                            .buscarPorId(CRUD_TEST_ID)
                            .isPresent()
            );

            // Actualizar
            Pendiente actualizado =
                    repository.actualizar(
                            new Pendiente(
                                    CRUD_TEST_ID,
                                    "Persistencia JDBC validada",
                                    "completado"
                            )
                    );

            assertEquals(
                    "Persistencia JDBC validada",
                    actualizado.titulo()
            );

            assertEquals(
                    "completado",
                    actualizado.estado()
            );

            // Listar
            assertTrue(
                    repository
                            .listar()
                            .stream()
                            .anyMatch(
                                    pendiente ->
                                            pendiente.id()
                                                    == CRUD_TEST_ID
                            )
            );
        } finally {
            eliminarPendienteDePrueba(
                    connectionFactory,
                    CRUD_TEST_ID
            );
        }
    }

    @Test
    void debeEliminarUnPendienteEnPostgresql()
            throws Exception {

        verificarQueLasPruebasEstanHabilitadas();

        DatabaseConnectionFactory connectionFactory =
                crearConnectionFactory();

        inicializar(connectionFactory);

        eliminarPendienteDePrueba(
                connectionFactory,
                DELETE_TEST_ID
        );

        JdbcPendienteRepository repository =
                new JdbcPendienteRepository(
                        connectionFactory
                );

        try {
            repository.guardar(
                    new Pendiente(
                            DELETE_TEST_ID,
                            "Pendiente para eliminar",
                            "activo"
                    )
            );

            // Primera eliminación
            assertTrue(
                    repository.eliminarPorId(
                            DELETE_TEST_ID
                    )
            );

            assertFalse(
                    repository
                            .buscarPorId(DELETE_TEST_ID)
                            .isPresent()
            );

            // Segunda eliminación: ya no existe
            assertFalse(
                    repository.eliminarPorId(
                            DELETE_TEST_ID
                    )
            );
        } finally {
            eliminarPendienteDePrueba(
                    connectionFactory,
                    DELETE_TEST_ID
            );
        }
    }

    @Test
    void debeRechazarUnIdDuplicadoEnPostgresql()
            throws Exception {

        verificarQueLasPruebasEstanHabilitadas();

        DatabaseConnectionFactory connectionFactory =
                crearConnectionFactory();

        inicializar(connectionFactory);

        eliminarPendienteDePrueba(
                connectionFactory,
                DUPLICATE_TEST_ID
        );

        JdbcPendienteRepository repository =
                new JdbcPendienteRepository(
                        connectionFactory
                );

        try {
            repository.guardar(
                    new Pendiente(
                            DUPLICATE_TEST_ID,
                            "Pendiente original",
                            "activo"
                    )
            );

            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            repository.guardar(
                                    new Pendiente(
                                            DUPLICATE_TEST_ID,
                                            "Pendiente duplicado",
                                            "activo"
                                    )
                            )
            );
        } finally {
            eliminarPendienteDePrueba(
                    connectionFactory,
                    DUPLICATE_TEST_ID
            );
        }
    }

    private void verificarQueLasPruebasEstanHabilitadas() {
        assumeTrue(
                Boolean.parseBoolean(
                        System.getenv(
                                "RUN_DATABASE_TESTS"
                        )
                ),
                "Pruebas PostgreSQL deshabilitadas"
        );
    }

    private DatabaseConnectionFactory crearConnectionFactory() {
        return new DatabaseConnectionFactory(
                DatabaseConfig.desdeEntorno()
        );
    }

    private void inicializar(
            DatabaseConnectionFactory connectionFactory
    ) throws Exception {
        new DatabaseInitializer(
                connectionFactory
        ).inicializar();
    }

    private void eliminarPendienteDePrueba(
            DatabaseConnectionFactory connectionFactory,
            int id
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
                    id
            );

            statement.executeUpdate();
        }
    }
}
