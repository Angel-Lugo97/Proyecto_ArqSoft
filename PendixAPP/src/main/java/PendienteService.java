import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PendienteService {
    private final PendienteRepository repository;

    public PendienteService() {
        this(
                new InMemoryPendienteRepository()
        );
    }

    public PendienteService(
            PendienteRepository repository
    ) {
        this.repository =
                Objects.requireNonNull(
                        repository,
                        "repository no puede ser null"
                );
    }

    public Pendiente agregar(
            Pendiente pendiente
    ) {
        return repository.guardar(
                pendiente
        );
    }

    public Optional<Pendiente> buscarPorId(
            int id
    ) {
        return repository.buscarPorId(
                id
        );
    }

    public Pendiente completar(
            int id
    ) {
        Pendiente actual = buscarPorId(id)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "Pendiente no encontrado: "
                                                + id
                                )
                );

        return repository.actualizar(
                actual.completar()
        );
    }

    public List<Pendiente> listar() {
        return repository.listar();
    }

    public String listarComoJson() {
        return listar().stream()
                .map(
                        pendiente ->
                                "{\"id\":"
                                        + pendiente.id()
                                        + ",\"titulo\":\""
                                        + escaparJson(
                                                pendiente.titulo()
                                        )
                                        + "\",\"estado\":\""
                                        + pendiente.estado()
                                        + "\"}"
                )
                .reduce(
                        "[",
                        (acumulado, item) ->
                                acumulado.equals("[")
                                        ? acumulado + item
                                        : acumulado + "," + item
                )
                + "]";
    }

    private String escaparJson(
            String texto
    ) {
        return texto
                .replace(
                        "\\",
                        "\\\\"
                )
                .replace(
                        "\"",
                        "\\\""
                );
    }
}
