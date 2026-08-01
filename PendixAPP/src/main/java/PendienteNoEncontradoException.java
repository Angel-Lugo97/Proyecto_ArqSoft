public class PendienteNoEncontradoException
        extends RuntimeException {

    public PendienteNoEncontradoException(
            int id
    ) {
        super(
                "Pendiente no encontrado: " + id
        );
    }
}
