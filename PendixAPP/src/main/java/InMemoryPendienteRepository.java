import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryPendienteRepository
        implements PendienteRepository {

    private final Map<Integer, Pendiente> pendientes =
            new LinkedHashMap<>();

    public InMemoryPendienteRepository() {
        this(List.of(
                new Pendiente(
                        1,
                        "Preparar presentación",
                        "vencido"
                ),
                new Pendiente(
                        2,
                        "Comprar víveres",
                        "activo"
                ),
                new Pendiente(
                        3,
                        "Enviar reporte semanal",
                        "completado"
                )
        ));
    }

    public InMemoryPendienteRepository(
            List<Pendiente> iniciales
    ) {
        for (Pendiente pendiente : iniciales) {
            guardar(pendiente);
        }
    }

    @Override
    public synchronized Pendiente guardar(
            Pendiente pendiente
    ) {
        if (pendientes.containsKey(pendiente.id())) {
            throw new IllegalArgumentException(
                    "Ya existe un pendiente con id "
                            + pendiente.id()
            );
        }

        pendientes.put(
                pendiente.id(),
                pendiente
        );

        return pendiente;
    }

    @Override
    public synchronized Optional<Pendiente> buscarPorId(
            int id
    ) {
        return Optional.ofNullable(
                pendientes.get(id)
        );
    }

    @Override
    public synchronized Pendiente actualizar(
            Pendiente pendiente
    ) {
        if (!pendientes.containsKey(pendiente.id())) {
            throw new IllegalArgumentException(
                    "Pendiente no encontrado: "
                            + pendiente.id()
            );
        }

        pendientes.put(
                pendiente.id(),
                pendiente
        );

        return pendiente;
    }

    @Override
    public synchronized List<Pendiente> listar() {
        return new ArrayList<>(
                pendientes.values()
        );
    }
}
