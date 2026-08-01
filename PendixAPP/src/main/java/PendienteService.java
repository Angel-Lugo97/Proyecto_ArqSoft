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

    public Pendiente obtener(
            int id
    ) {
        return buscarPorId(id)
                .orElseThrow(
                        () ->
                                new PendienteNoEncontradoException(
                                        id
                                )
                );
    }

    public Pendiente actualizar(
            int id,
            Pendiente cambios
    ) {
        obtener(id);

        Pendiente actualizado =
                new Pendiente(
                        id,
                        cambios.titulo(),
                        cambios.estado()
                );

        return repository.actualizar(
                actualizado
        );
    }

    public Pendiente completar(
            int id
    ) {
        Pendiente actual =
                obtener(id);

        return repository.actualizar(
                actual.completar()
        );
    }

    public void eliminar(
            int id
    ) {
        boolean eliminado =
                repository.eliminarPorId(id);

        if (!eliminado) {
            throw new PendienteNoEncontradoException(
                    id
            );
        }
    }

    public List<Pendiente> listar() {
        return repository.listar();
    }

    public String listarComoJson() {
        return PendienteJson.escribirLista(
                listar()
        );
    }

    public String obtenerComoJson(
            int id
    ) {
        return PendienteJson.escribir(
                obtener(id)
        );
    }
}
