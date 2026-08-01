import java.util.Objects;

public class PendixRouter {
    private final StaticResourceService staticResourceService;
    private final PendienteService pendienteService;
    private final String serverVersion = Long.toString(System.currentTimeMillis());

    public PendixRouter(StaticResourceService staticResourceService, PendienteService pendienteService) {
        this.staticResourceService = Objects.requireNonNull(staticResourceService);
        this.pendienteService = Objects.requireNonNull(pendienteService);
    }

    public HttpResult resolver(String metodo, String ruta) {
        if (!"GET".equalsIgnoreCase(metodo)) {
            return new HttpResult(405, "text/plain; charset=UTF-8", "Metodo no permitido");
        }

        if ("/health".equals(ruta)) {
            return new HttpResult(200, "application/json; charset=UTF-8", "{\"status\":\"ok\"}");
        }

        if ("/version".equals(ruta)) {
            return new HttpResult(
                    200,
                    "application/json; charset=UTF-8",
                    "{\"version\":\"" + serverVersion + "\"}"
            );
        }

        if ("/api/pendientes".equals(ruta)) {
            return new HttpResult(
                    200,
                    "application/json; charset=UTF-8",
                    pendienteService.listarComoJson()
            );
        }

        return staticResourceService.resolver(ruta);
    }
}
