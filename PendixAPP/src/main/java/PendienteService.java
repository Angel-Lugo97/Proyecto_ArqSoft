import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PendienteService {
    private final Map<Integer, Pendiente> pendientes = new LinkedHashMap<>();

    public PendienteService() {
        agregar(new Pendiente(1, "Preparar presentación", "vencido"));
        agregar(new Pendiente(2, "Comprar víveres", "activo"));
        agregar(new Pendiente(3, "Enviar reporte semanal", "completado"));
    }

    public Pendiente agregar(Pendiente pendiente) {
        if (pendientes.containsKey(pendiente.id())) {
            throw new IllegalArgumentException("Ya existe un pendiente con id " + pendiente.id());
        }
        pendientes.put(pendiente.id(), pendiente);
        return pendiente;
    }

    public Optional<Pendiente> buscarPorId(int id) {
        return Optional.ofNullable(pendientes.get(id));
    }

    public Pendiente completar(int id) {
        Pendiente actual = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pendiente no encontrado: " + id));
        Pendiente completado = actual.completar();
        pendientes.put(id, completado);
        return completado;
    }

    public List<Pendiente> listar() {
        return new ArrayList<>(pendientes.values());
    }

    public String listarComoJson() {
        return listar().stream()
                .map(p -> "{\"id\":" + p.id()
                        + ",\"titulo\":\"" + escaparJson(p.titulo())
                        + "\",\"estado\":\"" + p.estado() + "\"}")
                .reduce("[", (acumulado, item) -> acumulado.equals("[")
                        ? acumulado + item
                        : acumulado + "," + item) + "]";
    }

    private String escaparJson(String texto) {
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
