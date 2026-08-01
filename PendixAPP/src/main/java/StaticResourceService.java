import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class StaticResourceService {
    private static final Map<String, ResourceDefinition> RESOURCES = Map.of(
            "/", new ResourceDefinition("static/index.html", "text/html; charset=UTF-8"),
            "/index.html", new ResourceDefinition("static/index.html", "text/html; charset=UTF-8"),
            "/styles.css", new ResourceDefinition("static/styles.css", "text/css; charset=UTF-8"),
            "/app.js", new ResourceDefinition("static/app.js", "application/javascript; charset=UTF-8")
    );

    public HttpResult resolver(String ruta) {
        ResourceDefinition definition = RESOURCES.get(ruta);
        if (definition == null) {
            return new HttpResult(404, "text/plain; charset=UTF-8", "Archivo no encontrado");
        }

        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(definition.classpathLocation())) {
            if (stream == null) {
                return new HttpResult(500, "text/plain; charset=UTF-8", "Recurso web no disponible");
            }
            String body = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            return new HttpResult(200, definition.contentType(), body);
        } catch (IOException e) {
            return new HttpResult(500, "text/plain; charset=UTF-8", "No se pudo leer el recurso web");
        }
    }

    private record ResourceDefinition(String classpathLocation, String contentType) {
    }
}
