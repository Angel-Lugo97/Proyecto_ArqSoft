import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PendienteServiceTest {
    @Test
    void debeAgregarYBuscarUnPendiente() {
        // Arrange
        PendienteService service = new PendienteService();
        Pendiente nuevo = new Pendiente(20, "Configurar CI", "activo");

        // Act
        service.agregar(nuevo);

        // Assert
        assertTrue(service.buscarPorId(20).isPresent());
        assertEquals("Configurar CI", service.buscarPorId(20).orElseThrow().titulo());
    }

    @Test
    void debeCompletarUnPendienteExistente() {
        // Arrange
        PendienteService service = new PendienteService();

        // Act
        Pendiente resultado = service.completar(2);

        // Assert
        assertEquals("completado", resultado.estado());
        assertEquals("completado", service.buscarPorId(2).orElseThrow().estado());
    }

    @Test
    void debeRechazarIdsDuplicados() {
        // Arrange
        PendienteService service = new PendienteService();
        Pendiente duplicado = new Pendiente(1, "Otro pendiente", "activo");

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.agregar(duplicado));
    }
}
