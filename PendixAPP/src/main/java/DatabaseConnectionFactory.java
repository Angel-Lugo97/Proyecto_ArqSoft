import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public final class DatabaseConnectionFactory {
    private final DatabaseConfig config;

    public DatabaseConnectionFactory(
            DatabaseConfig config
    ) {
        this.config = Objects.requireNonNull(
                config,
                "config no puede ser null"
        );
    }

    public Connection abrir() throws SQLException {
        return DriverManager.getConnection(
                config.url(),
                config.user(),
                config.password()
        );
    }
}
