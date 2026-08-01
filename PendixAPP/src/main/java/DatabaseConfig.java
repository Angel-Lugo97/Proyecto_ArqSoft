import java.util.Map;
import java.util.Objects;

public record DatabaseConfig(
        String url,
        String user,
        String password
) {
    static final String DEFAULT_URL =
            "jdbc:postgresql://localhost:5432/pendix";

    static final String DEFAULT_USER = "postgres";
    static final String DEFAULT_PASSWORD = "";

    public DatabaseConfig {
        url = requireText(url, "DATABASE_URL");
        user = requireText(user, "DATABASE_USER");

        password = Objects.requireNonNull(
                password,
                "DATABASE_PASSWORD no puede ser null"
        );

        if (!url.startsWith("jdbc:postgresql://")) {
            throw new IllegalArgumentException(
                    "DATABASE_URL debe iniciar con jdbc:postgresql://"
            );
        }
    }

    public static DatabaseConfig desdeEntorno() {
        return desde(System.getenv());
    }

    static DatabaseConfig desde(
            Map<String, String> environment
    ) {
        Objects.requireNonNull(
                environment,
                "environment no puede ser null"
        );

        return new DatabaseConfig(
                leerTexto(
                        environment,
                        "DATABASE_URL",
                        DEFAULT_URL
                ),
                leerTexto(
                        environment,
                        "DATABASE_USER",
                        DEFAULT_USER
                ),
                leerPassword(
                        environment,
                        "DATABASE_PASSWORD",
                        DEFAULT_PASSWORD
                )
        );
    }

    private static String leerTexto(
            Map<String, String> environment,
            String nombre,
            String valorPredeterminado
    ) {
        String valor = environment.get(nombre);

        if (valor == null || valor.isBlank()) {
            return valorPredeterminado;
        }

        return valor.trim();
    }

    private static String leerPassword(
            Map<String, String> environment,
            String nombre,
            String valorPredeterminado
    ) {
        String valor = environment.get(nombre);

        return valor == null
                ? valorPredeterminado
                : valor;
    }

    private static String requireText(
            String valor,
            String nombre
    ) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    nombre + " no puede estar vacío"
            );
        }

        return valor.trim();
    }

    @Override
    public String toString() {
        return "DatabaseConfig[url="
                + url
                + ", user="
                + user
                + ", password=***]";
    }
}
