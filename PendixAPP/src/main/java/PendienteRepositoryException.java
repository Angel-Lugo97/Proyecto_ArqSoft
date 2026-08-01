public class PendienteRepositoryException
        extends RuntimeException {

    public PendienteRepositoryException(
            String message
    ) {
        super(message);
    }

    public PendienteRepositoryException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}
