import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GzipSupportTest {
    @Test
    void debeDetectarQueElClienteAceptaGzip() {
        assertTrue(GzipSupport.aceptaGzip("gzip, deflate, br"));
        assertFalse(GzipSupport.aceptaGzip("br"));
        assertFalse(GzipSupport.aceptaGzip(null));
    }

    @Test
    void debeComprimirYRecuperarElContenido() throws Exception {
        byte[] original = "PendixAPP contenido de prueba".getBytes(StandardCharsets.UTF_8);
        byte[] comprimido = GzipSupport.comprimir(original);

        byte[] recuperado;
        try (GZIPInputStream gzip = new GZIPInputStream(new java.io.ByteArrayInputStream(comprimido))) {
            recuperado = gzip.readAllBytes();
        }

        assertArrayEquals(original, recuperado);
    }
}
