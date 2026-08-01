import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryPendienteRepositoryTest {
    @Test
    void debeGuardarBuscarYListarPendientes() {
        // Arrange
        InMemoryPendienteRepository repository =
                new InMemoryPendienteRepository(
                        List.of()
                );

        Pendiente pendiente =
                new Pendiente(
                        10,
                        "Validar repositorio",
                        "activo"
                );

        // Act
        repository.guardar(
                pendiente
        );

        // Assert
        assertEquals(
                1,
                repository.listar().size()
        );

        assertTrue(
                repository.buscarPorId(10).isPresent()
        );

        assertEquals(
                "Validar repositorio",
                repository
                        .buscarPorId(10)
                        .orElseThrow()
                        .titulo()
        );
    }

    @Test
    void debeActualizarUnPendienteExistente() {
        // Arrange
        InMemoryPendienteRepository repository =
                new InMemoryPendienteRepository(
                        List.of(
                                new Pendiente(
                                        20,
                                        "Preparar demo",
                                        "activo"
                                )
                        )
                );

        // Act
        Pendiente actualizado =
                repository.actualizar(
                        new Pendiente(
                                20,
                                "Preparar demo",
                                "completado"
                        )
                );

        // Assert
        assertEquals(
                "completado",
                actualizado.estado()
        );

        assertEquals(
                "completado",
                repository
                        .buscarPorId(20)
                        .orElseThrow()
                        .estado()
        );
    }

    @Test
    void debeRechazarIdsDuplicados() {
        // Arrange
        InMemoryPendienteRepository repository =
                new InMemoryPendienteRepository(
                        List.of(
                                new Pendiente(
                                        30,
                                        "Pendiente inicial",
                                        "activo"
                                )
                        )
                );

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        repository.guardar(
                                new Pendiente(
                                        30,
                                        "Pendiente duplicado",
                                        "activo"
                                )
                        )
        );
    }
}
