import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PendienteJson {
    private PendienteJson() {
    }

    public static Pendiente leerCreacion(
            String json
    ) {
        validarObjeto(json);

        int id = leerEnteroObligatorio(
                json,
                "id"
        );

        String titulo = leerTextoObligatorio(
                json,
                "titulo"
        );

        String estado = leerTextoOpcional(
                json,
                "estado",
                "activo"
        );

        return new Pendiente(
                id,
                titulo,
                estado
        );
    }

    public static Pendiente leerActualizacion(
            int id,
            String json
    ) {
        validarObjeto(json);

        String titulo = leerTextoObligatorio(
                json,
                "titulo"
        );

        String estado = leerTextoObligatorio(
                json,
                "estado"
        );

        return new Pendiente(
                id,
                titulo,
                estado
        );
    }

    public static String escribir(
            Pendiente pendiente
    ) {
        return "{\"id\":"
                + pendiente.id()
                + ",\"titulo\":\""
                + escapar(pendiente.titulo())
                + "\",\"estado\":\""
                + escapar(pendiente.estado())
                + "\"}";
    }

    public static String escribirLista(
            List<Pendiente> pendientes
    ) {
        return pendientes.stream()
                .map(PendienteJson::escribir)
                .reduce(
                        "[",
                        (acumulado, item) ->
                                acumulado.equals("[")
                                        ? acumulado + item
                                        : acumulado + "," + item
                )
                + "]";
    }

    public static String error(
            String mensaje
    ) {
        String texto = mensaje == null
                ? "Solicitud inválida"
                : mensaje;

        return "{\"error\":\""
                + escapar(texto)
                + "\"}";
    }

    private static int leerEnteroObligatorio(
            String json,
            String campo
    ) {
        Pattern pattern = Pattern.compile(
                "\""
                        + Pattern.quote(campo)
                        + "\"\\s*:\\s*(\\d+)"
        );

        Matcher matcher =
                pattern.matcher(json);

        if (!matcher.find()) {
            throw new IllegalArgumentException(
                    "El campo "
                            + campo
                            + " es obligatorio y debe ser numérico"
            );
        }

        try {
            return Integer.parseInt(
                    matcher.group(1)
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El campo "
                            + campo
                            + " contiene un número inválido",
                    e
            );
        }
    }

    private static String leerTextoObligatorio(
            String json,
            String campo
    ) {
        String valor = leerTexto(
                json,
                campo
        );

        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    "El campo "
                            + campo
                            + " es obligatorio"
            );
        }

        return valor;
    }

    private static String leerTextoOpcional(
            String json,
            String campo,
            String valorPredeterminado
    ) {
        String valor = leerTexto(
                json,
                campo
        );

        if (valor == null || valor.isBlank()) {
            return valorPredeterminado;
        }

        return valor;
    }

    private static String leerTexto(
            String json,
            String campo
    ) {
        Pattern pattern = Pattern.compile(
                "\""
                        + Pattern.quote(campo)
                        + "\"\\s*:\\s*\""
                        + "((?:\\\\.|[^\"\\\\])*)"
                        + "\""
        );

        Matcher matcher =
                pattern.matcher(json);

        if (!matcher.find()) {
            return null;
        }

        return desescapar(
                matcher.group(1)
        );
    }

    private static void validarObjeto(
            String json
    ) {
        if (json == null || json.isBlank()) {
            throw new IllegalArgumentException(
                    "El cuerpo JSON es obligatorio"
            );
        }

        String trimmed = json.trim();

        if (
                !trimmed.startsWith("{")
                        || !trimmed.endsWith("}")
        ) {
            throw new IllegalArgumentException(
                    "El cuerpo debe ser un objeto JSON"
            );
        }
    }

    private static String escapar(
            String texto
    ) {
        return texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String desescapar(
            String texto
    ) {
        StringBuilder resultado =
                new StringBuilder();

        boolean escapado = false;

        for (char caracter : texto.toCharArray()) {
            if (escapado) {
                switch (caracter) {
                    case 'n' ->
                            resultado.append('\n');
                    case 'r' ->
                            resultado.append('\r');
                    case 't' ->
                            resultado.append('\t');
                    case '\\' ->
                            resultado.append('\\');
                    case '"' ->
                            resultado.append('"');
                    default ->
                            resultado.append(caracter);
                }

                escapado = false;
            } else if (caracter == '\\') {
                escapado = true;
            } else {
                resultado.append(caracter);
            }
        }

        if (escapado) {
            resultado.append('\\');
        }

        return resultado.toString();
    }
}
