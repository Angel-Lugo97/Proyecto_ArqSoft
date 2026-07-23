import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PendienteTest {
    @Test
    void debeCambiarElEstadoACompletado() {
        // Arrange
        Pendiente pendiente = new Pendiente(10, "Estudiar JUnit", "activo");

        // Act
        Pendiente resultado = pendiente.completar();

        // Assert
        assertEquals(10, resultado.id());
        assertEquals("Estudiar JUnit", resultado.titulo());
        assertEquals("completado", resultado.estado());
    }

    @Test
    void debeRechazarUnTituloVacio() {
        // Arrange
        String tituloInvalido = "   ";

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Pendiente(1, tituloInvalido, "activo"));
    }
}
