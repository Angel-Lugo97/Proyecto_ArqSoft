import java.util.List;
import java.util.Optional;

public interface PendienteRepository {
    Pendiente guardar(Pendiente pendiente);

    Optional<Pendiente> buscarPorId(int id);

    Pendiente actualizar(Pendiente pendiente);

    List<Pendiente> listar();
}
