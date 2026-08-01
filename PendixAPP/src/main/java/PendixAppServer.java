import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class PendixAppServer {
    private static final int DEFAULT_PORT = 5018;
    private static final String DEFAULT_HOST = "0.0.0.0";
    private static final String DEFAULT_ALLOWED_ORIGIN = "*";

    public static void main(String[] args) {
        int port = obtenerPuerto();
        String host = obtenerVariable("HOST", DEFAULT_HOST);
        String allowedOrigin = obtenerVariable(
                "CORS_ALLOWED_ORIGIN",
                DEFAULT_ALLOWED_ORIGIN
        );

        boolean autoOpenBrowser = Boolean.parseBoolean(
                obtenerVariable("AUTO_OPEN_BROWSER", "true")
        );

        String browserHost = "0.0.0.0".equals(host)
                ? "localhost"
                : host;

        String url = "http://" + browserHost + ":" + port;

        try {
            inicializarBaseDeDatosSiEstaHabilitada();

            PendienteService pendienteService =
                    crearPendienteService();

            HttpServer server = HttpServer.create(
                    new InetSocketAddress(host, port),
                    0
            );

            server.createContext(
                    "/",
                    new PendixHandler(
                            allowedOrigin,
                            pendienteService
                    )
            );

            server.setExecutor(null);
            server.start();

            System.out.println();
            System.out.println("============================================");
            System.out.println(" PendixAPP ejecutándose correctamente");
            System.out.println(" URL local: " + url);
            System.out.println(" Host: " + host);
            System.out.println(" Puerto: " + port);
            System.out.println(" CORS permitido: " + allowedOrigin);
            System.out.println(" Health check: " + url + "/api/health");
            System.out.println(" Presiona CTRL + C para detenerlo");
            System.out.println("============================================");
            System.out.println();

            if (autoOpenBrowser) {
                abrirNavegador(url);
            }
        } catch (BindException e) {
            System.out.println();
            System.out.println("El puerto " + port + " ya está ocupado.");
            System.out.println("Abre directamente: " + url);
            System.out.println("O cierra el proceso que está usando ese puerto.");
            System.out.println();
        } catch (SQLException e) {
            System.err.println();
            System.err.println(
                    "No se pudo inicializar PostgreSQL."
            );
            System.err.println(
                    "Detalle: " + e.getMessage()
            );
            System.err.println();

            System.exit(1);
        } catch (IOException e) {
            System.out.println(
                    "No se pudo iniciar PendixAPP en el puerto " + port + "."
            );
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    private static PendienteService crearPendienteService() {
        boolean databaseEnabled =
                Boolean.parseBoolean(
                        obtenerVariable(
                                "DATABASE_ENABLED",
                                "false"
                        )
                );

        if (!databaseEnabled) {
            System.out.println(
                    "Repositorio activo: memoria local."
            );

            return new PendienteService();
        }

        DatabaseConfig config =
                DatabaseConfig.desdeEntorno();

        System.out.println(
                "Repositorio activo: PostgreSQL."
        );

        return new PendienteService(
                new JdbcPendienteRepository(
                        new DatabaseConnectionFactory(
                                config
                        )
                )
        );
    }

    private static void inicializarBaseDeDatosSiEstaHabilitada()
            throws SQLException {
        boolean enabled = Boolean.parseBoolean(
                obtenerVariable(
                        "DATABASE_INIT_ENABLED",
                        "false"
                )
        );

        if (!enabled) {
            System.out.println(
                    "Inicialización de PostgreSQL deshabilitada."
            );
            return;
        }

        DatabaseConfig config =
                DatabaseConfig.desdeEntorno();

        System.out.println(
                "Inicializando PostgreSQL en: "
                        + config.url()
        );

        DatabaseInitializer initializer =
                new DatabaseInitializer(
                        new DatabaseConnectionFactory(
                                config
                        )
                );

        initializer.inicializar();

        System.out.println(
                "Esquema y datos iniciales verificados."
        );
    }

    private static int obtenerPuerto() {
        String portValue = System.getenv("PORT");

        if (portValue == null || portValue.isBlank()) {
            return DEFAULT_PORT;
        }

        try {
            int port = Integer.parseInt(portValue);

            if (port < 1 || port > 65535) {
                throw new IllegalArgumentException(
                        "PORT debe estar entre 1 y 65535."
                );
            }

            return port;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La variable PORT debe contener un número válido.",
                    e
            );
        }
    }

    private static String obtenerVariable(
            String nombre,
            String valorPredeterminado
    ) {
        String valor = System.getenv(nombre);

        if (valor == null || valor.isBlank()) {
            return valorPredeterminado;
        }

        return valor.trim();
    }

    private static void abrirNavegador(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
                return;
            }
        } catch (Throwable ignored) {
            // El servidor continúa aunque no pueda abrir el navegador.
        }

        String os = System.getProperty("os.name").toLowerCase();
        String[] comando;

        if (os.contains("win")) {
            comando = new String[]{"cmd", "/c", "start", url};
        } else if (os.contains("mac")) {
            comando = new String[]{"open", url};
        } else {
            comando = new String[]{"xdg-open", url};
        }

        try {
            Runtime.getRuntime().exec(comando);
        } catch (IOException ignored) {
            System.out.println(
                    "Abre manualmente en el navegador: " + url
            );
        }
    }

    private static class PendixHandler implements HttpHandler {
        private final PendixRouter router;
        private final String allowedOrigin;

        private PendixHandler(
                String allowedOrigin,
                PendienteService pendienteService
        ) {
            this.allowedOrigin = allowedOrigin;
            this.router = new PendixRouter(
                    PendixAppServer::html,
                    pendienteService
            );
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(
                    exchange.getRequestMethod()
            )) {
                enviarPreflight(exchange);
                return;
            }

            String path = exchange.getRequestURI().getPath();

            HttpResult result;

            try {
                result = router.resolver(
                        exchange.getRequestMethod(),
                        path
                );
            } catch (PendienteRepositoryException e) {
                System.err.println(
                        "Error de persistencia: "
                                + e.getMessage()
                );

                result = new HttpResult(
                        500,
                        "application/json; charset=UTF-8",
                        "{\"error\":"
                                + "\"No se pudo acceder "
                                + "a los pendientes\"}"
                );
            }

            enviar(
                    exchange,
                    result.status(),
                    result.contentType(),
                    result.body()
            );
        }

        private void enviarPreflight(
                HttpExchange exchange
        ) throws IOException {
            Headers headers = exchange.getResponseHeaders();
            aplicarHeaders(headers);

            exchange.sendResponseHeaders(204, -1);
            exchange.close();
        }

        private void enviar(
                HttpExchange exchange,
                int status,
                String contentType,
                String body
        ) throws IOException {
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            Headers headers = exchange.getResponseHeaders();

            headers.set("Content-Type", contentType);
            headers.set("Cache-Control", "no-store");
            aplicarHeaders(headers);

            exchange.sendResponseHeaders(status, bytes.length);

            try (OutputStream output = exchange.getResponseBody()) {
                output.write(bytes);
            }
        }

        private void aplicarHeaders(Headers headers) {
            headers.set(
                    "Access-Control-Allow-Origin",
                    allowedOrigin
            );

            headers.set(
                    "Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS"
            );

            headers.set(
                    "Access-Control-Allow-Headers",
                    "Content-Type, Authorization"
            );

            headers.set(
                    "Access-Control-Max-Age",
                    "3600"
            );
        }
    }

    private static String html() {
        try (var stream =
                     PendixAppServer.class.getResourceAsStream(
                             "/public/index.html"
                     )) {

            if (stream == null) {
                throw new IllegalStateException(
                        "No se encontró el frontend en /public/index.html"
                );
            }

            return new String(
                    stream.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo cargar el frontend",
                    e
            );
        }
    }
}
