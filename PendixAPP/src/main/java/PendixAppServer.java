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
    private static final int PORT = 5018;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        String url = "http://localhost:" + PORT;

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(HOST, PORT), 0);
            server.createContext("/", new PendixHandler());
            server.setExecutor(null);
            server.start();

            System.out.println();
            System.out.println("============================================");
            System.out.println(" PendixAPP ejecutandose correctamente");
            System.out.println(" URL: " + url);
            System.out.println(" Servidor Java en puerto " + PORT);
            System.out.println(" Presiona CTRL + C para detenerlo");
            System.out.println("============================================");
            System.out.println();

            abrirNavegador(url);
        } catch (BindException e) {
            System.out.println();
            System.out.println("El puerto " + PORT + " ya esta ocupado.");
            System.out.println("Abre directamente: " + url);
            System.out.println("O cierra el programa que esta usando ese puerto y vuelve a ejecutar.");
            System.out.println();
        } catch (IOException e) {
            System.out.println("No se pudo iniciar PendixAPP en el puerto " + PORT + ".");
            System.out.println("Detalle: " + e.getMessage());
        }
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
                new PendixRouter(PendixAppServer::html, new PendienteService());

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            HttpResult result = router.resolver(exchange.getRequestMethod(), path);
            enviar(exchange, result.status(), result.contentType(), result.body());
        }

        private void enviar(HttpExchange exchange, int status, String contentType, String body) throws IOException {
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", contentType);
            headers.set("Cache-Control", "no-store");
            exchange.sendResponseHeaders(status, bytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    private static String html() {
        try (var stream =
                     PendixAppServer.class.getResourceAsStream("/public/index.html")) {

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