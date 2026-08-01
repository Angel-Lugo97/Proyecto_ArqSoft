import java.util.Objects;
import java.util.function.Supplier;

public class PendixRouter {
    private static final String JSON =
            "application/json; charset=UTF-8";

    private static final String TEXT =
            "text/plain; charset=UTF-8";

    private static final String BASE_PATH =
            "/api/pendientes";

    private final Supplier<String> htmlSupplier;
    private final PendienteService pendienteService;

    public PendixRouter(
            Supplier<String> htmlSupplier,
            PendienteService pendienteService
    ) {
        this.htmlSupplier =
                Objects.requireNonNull(
                        htmlSupplier
                );

        this.pendienteService =
                Objects.requireNonNull(
                        pendienteService
                );
    }

    public HttpResult resolver(
            String metodo,
            String ruta
    ) {
        return resolver(
                metodo,
                ruta,
                ""
        );
    }

    public HttpResult resolver(
            String metodo,
            String ruta,
            String cuerpo
    ) {
        try {
            if (
                    "/".equals(ruta)
                            || "/index.html".equals(ruta)
            ) {
                if ("GET".equalsIgnoreCase(metodo)) {
                    return new HttpResult(
                            200,
                            "text/html; charset=UTF-8",
                            htmlSupplier.get()
                    );
                }

                return new HttpResult(
                        405,
                        TEXT,
                        "Metodo no permitido"
                );
            }

            if ("/api/health".equals(ruta)) {
                if ("GET".equalsIgnoreCase(metodo)) {
                    return new HttpResult(
                            200,
                            JSON,
                            "{\"status\":\"UP\","
                                    + "\"service\":\"PendixAPP\"}"
                    );
                }

                return metodoNoPermitido();
            }

            if (BASE_PATH.equals(ruta)) {
                return resolverColeccion(
                        metodo,
                        cuerpo
                );
            }

            if (
                    ruta.startsWith(
                            BASE_PATH + "/"
                    )
            ) {
                int id = extraerId(ruta);

                return resolverRecurso(
                        metodo,
                        id,
                        cuerpo
                );
            }

            return new HttpResult(
                    404,
                    JSON,
                    PendienteJson.error(
                            "Ruta no encontrada"
                    )
            );
        } catch (PendienteNoEncontradoException e) {
            return new HttpResult(
                    404,
                    JSON,
                    PendienteJson.error(
                            e.getMessage()
                    )
            );
        } catch (IllegalArgumentException e) {
            int status = esConflicto(e)
                    ? 409
                    : 400;

            return new HttpResult(
                    status,
                    JSON,
                    PendienteJson.error(
                            e.getMessage()
                    )
            );
        }
    }

    private HttpResult resolverColeccion(
            String metodo,
            String cuerpo
    ) {
        if ("GET".equalsIgnoreCase(metodo)) {
            return new HttpResult(
                    200,
                    JSON,
                    pendienteService.listarComoJson()
            );
        }

        if ("POST".equalsIgnoreCase(metodo)) {
            Pendiente pendiente =
                    PendienteJson.leerCreacion(
                            cuerpo
                    );

            Pendiente creado =
                    pendienteService.agregar(
                            pendiente
                    );

            return new HttpResult(
                    201,
                    JSON,
                    PendienteJson.escribir(
                            creado
                    )
            );
        }

        return metodoNoPermitido();
    }

    private HttpResult resolverRecurso(
            String metodo,
            int id,
            String cuerpo
    ) {
        if ("GET".equalsIgnoreCase(metodo)) {
            return new HttpResult(
                    200,
                    JSON,
                    pendienteService.obtenerComoJson(
                            id
                    )
            );
        }

        if ("PUT".equalsIgnoreCase(metodo)) {
            Pendiente cambios =
                    PendienteJson.leerActualizacion(
                            id,
                            cuerpo
                    );

            Pendiente actualizado =
                    pendienteService.actualizar(
                            id,
                            cambios
                    );

            return new HttpResult(
                    200,
                    JSON,
                    PendienteJson.escribir(
                            actualizado
                    )
            );
        }

        if ("DELETE".equalsIgnoreCase(metodo)) {
            pendienteService.eliminar(id);

            return new HttpResult(
                    204,
                    JSON,
                    ""
            );
        }

        return metodoNoPermitido();
    }

    private int extraerId(
            String ruta
    ) {
        String value = ruta.substring(
                (BASE_PATH + "/").length()
        );

        if (
                value.isBlank()
                        || value.contains("/")
        ) {
            throw new IllegalArgumentException(
                    "El id de la ruta no es válido"
            );
        }

        try {
            int id = Integer.parseInt(value);

            if (id <= 0) {
                throw new NumberFormatException();
            }

            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El id de la ruta debe ser un número positivo"
            );
        }
    }

    private boolean esConflicto(
            IllegalArgumentException exception
    ) {
        return exception.getMessage() != null
                && exception
                .getMessage()
                .startsWith("Ya existe");
    }

    private HttpResult metodoNoPermitido() {
        return new HttpResult(
                405,
                JSON,
                PendienteJson.error(
                        "Método no permitido"
                )
        );
    }
}
