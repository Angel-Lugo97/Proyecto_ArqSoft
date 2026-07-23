import java.util.Set;

public record Pendiente(int id, String titulo, String estado) {
    private static final Set<String> ESTADOS_VALIDOS =
            Set.of("activo", "vencido", "completado");

    public Pendiente {
        if (id <= 0) {
            throw new IllegalArgumentException("El id debe ser mayor que cero");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        if (estado == null || !ESTADOS_VALIDOS.contains(estado)) {
            throw new IllegalArgumentException("Estado no válido: " + estado);
        }
        titulo = titulo.trim();
    }

    public Pendiente completar() {
        return new Pendiente(id, titulo, "completado");
    }
}
