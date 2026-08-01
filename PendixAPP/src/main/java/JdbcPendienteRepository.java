import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JdbcPendienteRepository
        implements PendienteRepository {

    private static final String SQL_INSERTAR = """
            INSERT INTO pendientes (
                id,
                titulo,
                estado
            )
            VALUES (?, ?, ?)
            RETURNING id, titulo, estado
            """;

    private static final String SQL_BUSCAR_POR_ID = """
            SELECT
                id,
                titulo,
                estado
            FROM pendientes
            WHERE id = ?
            """;

    private static final String SQL_ACTUALIZAR = """
            UPDATE pendientes
            SET
                titulo = ?,
                estado = ?,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            RETURNING id, titulo, estado
            """;

    private static final String SQL_LISTAR = """
            SELECT
                id,
                titulo,
                estado
            FROM pendientes
            ORDER BY id
            """;

    private final DatabaseConnectionFactory
            connectionFactory;

    public JdbcPendienteRepository(
            DatabaseConnectionFactory connectionFactory
    ) {
        this.connectionFactory =
                Objects.requireNonNull(
                        connectionFactory,
                        "connectionFactory no puede ser null"
                );
    }

    @Override
    public Pendiente guardar(
            Pendiente pendiente
    ) {
        Objects.requireNonNull(
                pendiente,
                "pendiente no puede ser null"
        );

        try (
                Connection connection =
                        connectionFactory.abrir();

                PreparedStatement statement =
                        connection.prepareStatement(
                                SQL_INSERTAR
                        )
        ) {
            statement.setInt(
                    1,
                    pendiente.id()
            );

            statement.setString(
                    2,
                    pendiente.titulo()
            );

            statement.setString(
                    3,
                    pendiente.estado()
            );

            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {
                if (!result.next()) {
                    throw new PendienteRepositoryException(
                            "PostgreSQL no devolvió "
                                    + "el pendiente guardado"
                    );
                }

                return mapear(result);
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new IllegalArgumentException(
                        "Ya existe un pendiente con id "
                                + pendiente.id(),
                        e
                );
            }

            throw error(
                    "guardar",
                    e
            );
        }
    }

    @Override
    public Optional<Pendiente> buscarPorId(
            int id
    ) {
        try (
                Connection connection =
                        connectionFactory.abrir();

                PreparedStatement statement =
                        connection.prepareStatement(
                                SQL_BUSCAR_POR_ID
                        )
        ) {
            statement.setInt(
                    1,
                    id
            );

            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {
                if (!result.next()) {
                    return Optional.empty();
                }

                return Optional.of(
                        mapear(result)
                );
            }
        } catch (SQLException e) {
            throw error(
                    "buscar por id",
                    e
            );
        }
    }

    @Override
    public Pendiente actualizar(
            Pendiente pendiente
    ) {
        Objects.requireNonNull(
                pendiente,
                "pendiente no puede ser null"
        );

        try (
                Connection connection =
                        connectionFactory.abrir();

                PreparedStatement statement =
                        connection.prepareStatement(
                                SQL_ACTUALIZAR
                        )
        ) {
            statement.setString(
                    1,
                    pendiente.titulo()
            );

            statement.setString(
                    2,
                    pendiente.estado()
            );

            statement.setInt(
                    3,
                    pendiente.id()
            );

            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {
                if (!result.next()) {
                    throw new IllegalArgumentException(
                            "Pendiente no encontrado: "
                                    + pendiente.id()
                    );
                }

                return mapear(result);
            }
        } catch (SQLException e) {
            throw error(
                    "actualizar",
                    e
            );
        }
    }

    @Override
    public List<Pendiente> listar() {
        try (
                Connection connection =
                        connectionFactory.abrir();

                PreparedStatement statement =
                        connection.prepareStatement(
                                SQL_LISTAR
                        );

                ResultSet result =
                        statement.executeQuery()
        ) {
            List<Pendiente> pendientes =
                    new ArrayList<>();

            while (result.next()) {
                pendientes.add(
                        mapear(result)
                );
            }

            return pendientes;
        } catch (SQLException e) {
            throw error(
                    "listar",
                    e
            );
        }
    }

    private Pendiente mapear(
            ResultSet result
    ) throws SQLException {
        return new Pendiente(
                result.getInt("id"),
                result.getString("titulo"),
                result.getString("estado")
        );
    }

    private PendienteRepositoryException error(
            String operacion,
            SQLException cause
    ) {
        return new PendienteRepositoryException(
                "No se pudo "
                        + operacion
                        + " pendientes en PostgreSQL",
                cause
        );
    }
}
