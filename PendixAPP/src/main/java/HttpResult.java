import java.util.Objects;

public record HttpResult(int status, String contentType, String body) {
    public HttpResult {
        if (status < 100 || status > 599) {
            throw new IllegalArgumentException("El estado HTTP debe estar entre 100 y 599");
        }
        contentType = Objects.requireNonNull(contentType, "contentType no puede ser null");
        body = Objects.requireNonNull(body, "body no puede ser null");
    }
}
