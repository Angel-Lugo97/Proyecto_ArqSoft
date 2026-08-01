import java.util.Objects;
import java.util.function.Supplier;

public class PendixRouter {
    private final Supplier<String> htmlSupplier;
    private final PendienteService pendienteService;

    public PendixRouter(
            Supplier<String> htmlSupplier,
            PendienteService pendienteService
    ) {
        this.htmlSupplier = Objects.requireNonNull(htmlSupplier);
        this.pendienteService = Objects.requireNonNull(pendienteService);
    }

    public HttpResult resolver(String metodo, String ruta) {
        if (!"GET".equalsIgnoreCase(metodo)) {
            return new HttpResult(
                    405,
                    "text/plain; charset=UTF-8",
                    "Metodo no permitido"
            );
        }

        if ("/".equals(ruta) || "/index.html".equals(ruta)) {
            return new HttpResult(
                    200,
                    "text/html; charset=UTF-8",
                    htmlSupplier.get()
            );
        }

        if ("/api/health".equals(ruta)) {
            return new HttpResult(
                    200,
                    "application/json; charset=UTF-8",
                    """
                    {"status":"UP","service":"PendixAPP"}
                    """.trim()
            );
        }

        if ("/api/pendientes".equals(ruta)) {
            return new HttpResult(
                    200,
                    "application/json; charset=UTF-8",
                    pendienteService.listarComoJson()
            );
        }

        return new HttpResult(
                404,
                "text/plain; charset=UTF-8",
                "Archivo no encontrado"
        );
    }
}
