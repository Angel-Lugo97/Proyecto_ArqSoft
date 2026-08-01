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

public class PendixAppServer {
    private static final int DEFAULT_PORT = 5018;
    private static final String DEFAULT_HOST = "127.0.0.1";

    public static void main(String[] args) {
        String host = obtenerConfiguracion("pendix.host", "PENDIX_HOST", DEFAULT_HOST);
        int port = obtenerPuerto();
        boolean openBrowser = Boolean.parseBoolean(
                obtenerConfiguracion("pendix.openBrowser", "PENDIX_OPEN_BROWSER", "true")
        );
        String browserHost = "0.0.0.0".equals(host) ? "localhost" : host;
        String url = "http://" + browserHost + ":" + port;

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(host, port), 0);
            server.createContext("/", new PendixHandler());
            server.setExecutor(null);
            server.start();

            System.out.println();
            System.out.println("============================================");
            System.out.println(" PendixAPP ejecutandose correctamente");
            System.out.println(" URL: " + url);
            System.out.println(" Servidor Java en " + host + ":" + port);
            System.out.println(" Presiona CTRL + C para detenerlo");
            System.out.println("============================================");
            System.out.println();

            if (openBrowser) {
                abrirNavegador(url);
            }
        } catch (BindException e) {
            System.out.println();
            System.out.println("El puerto " + port + " ya esta ocupado.");
            System.out.println("Abre directamente: " + url);
            System.out.println("O cierra el programa que esta usando ese puerto y vuelve a ejecutar.");
            System.out.println();
        } catch (IOException e) {
            System.out.println("No se pudo iniciar PendixAPP en el puerto " + port + ".");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    private static int obtenerPuerto() {
        String value = obtenerConfiguracion("pendix.port", "PENDIX_PORT", String.valueOf(DEFAULT_PORT));
        try {
            int port = Integer.parseInt(value);
            if (port < 1 || port > 65535) {
                throw new IllegalArgumentException("El puerto debe estar entre 1 y 65535");
            }
            return port;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Puerto no valido: " + value, e);
        }
    }

    private static String obtenerConfiguracion(String property, String environment, String defaultValue) {
        String systemValue = System.getProperty(property);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }
        String environmentValue = System.getenv(environment);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue.trim();
        }
        return defaultValue;
    }

    private static void abrirNavegador(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
                return;
            }
        } catch (Throwable ignored) {
            // Si el sistema no permite abrir navegador automaticamente, el servidor sigue activo.
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
            System.out.println("Abre manualmente en el navegador: " + url);
        }
    }

    private static class PendixHandler implements HttpHandler {
        private final PendixRouter router =
                new PendixRouter(new StaticResourceService(), new PendienteService());

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Headers headers = exchange.getResponseHeaders();

            headers.set("Access-Control-Allow-Origin", "*");
            headers.set(
                    "Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS"
            );
            headers.set(
                    "Access-Control-Allow-Headers",
                    "Content-Type, Authorization"
            );

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }

            String path = exchange.getRequestURI().getPath();
            HttpResult result = router.resolver(exchange.getRequestMethod(), path);
            enviar(exchange, result);
        }

        private void enviar(HttpExchange exchange, HttpResult result) throws IOException {
            byte[] bytes = result.body().getBytes(StandardCharsets.UTF_8);
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", result.contentType());
            headers.set("Cache-Control", "no-store");
            headers.set("X-Content-Type-Options", "nosniff");
            headers.set("Referrer-Policy", "no-referrer");
            headers.set("Vary", "Accept-Encoding");

            String acceptEncoding = exchange.getRequestHeaders().getFirst("Accept-Encoding");
            if (esTexto(result.contentType()) && bytes.length > 256 && GzipSupport.aceptaGzip(acceptEncoding)) {
                bytes = GzipSupport.comprimir(bytes);
                headers.set("Content-Encoding", "gzip");
            }

            exchange.sendResponseHeaders(result.status(), bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private boolean esTexto(String contentType) {
            return contentType.startsWith("text/")
                    || contentType.startsWith("application/javascript")
                    || contentType.startsWith("application/json");
        }
    }
}
