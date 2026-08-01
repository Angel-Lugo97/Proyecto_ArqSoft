import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;

public final class GzipSupport {
    private GzipSupport() {
    }

    public static boolean aceptaGzip(String acceptEncoding) {
        if (acceptEncoding == null || acceptEncoding.isBlank()) {
            return false;
        }
        return acceptEncoding.toLowerCase(Locale.ROOT).contains("gzip");
    }

    public static byte[] comprimir(byte[] contenido) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(buffer)) {
            gzip.write(contenido);
        }
        return buffer.toByteArray();
    }
}
