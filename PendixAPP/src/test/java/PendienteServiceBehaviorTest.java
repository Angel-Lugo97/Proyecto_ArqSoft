import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PendienteServiceBehaviorTest {
    @Test
    void debeListarLosPendientesDelRepositorio() {
        // Arrange
        PendienteRepository repository =
                new InMemoryPendienteRepository(
                        List.of(
                                new Pendiente(
                                        10,
                                        "Primero",
                                        "activo"
                                ),
                                new Pendiente(
                                        11,
                                        "Segundo",
                                        "vencido"
                                )
                        )
                );

        PendienteService service =
                new PendienteService(repository);

        // Act
        List<Pendiente> pendientes =
                service.listar();

        // Assert
        assertEquals(
                2,
                pendientes.size()
        );

        assertEquals(
                "Primero",
                pendientes.get(0).titulo()
        );

        assertEquals(
                "Segundo",
                pendientes.get(1).titulo()
        );
    }

    @Test
    void debeUsarElIdDeLaRutaAlActualizar() {
        // Arrange
        PendienteRepository repository =
                new InMemoryPendienteRepository(
                        List.of(
                                new Pendiente(
                                        20,
                                        "Título original",
                                        "activo"
                                )
                        )
                );

        PendienteService service =
                new PendienteService(repository);

        Pendiente cambios =
                new Pendiente(
                        999,
                        "Título actualizado",
                        "completado"
                );

        // Act
        Pendiente actualizado =
                service.actualizar(
                        20,
                        cambios
                );

        // Assert
        assertEquals(
                20,
                actualizado.id()
        );

        assertEquals(
                "Título actualizado",
                actualizado.titulo()
        );

        assertEquals(
                "completado",
                actualizado.estado()
        );

        assertFalse(
                service.buscarPorId(999).isPresent()
        );
    }

    @Test
    void debeEliminarUnPendienteExistente() {
        // Arrange
        PendienteRepository repository =
                new InMemoryPendienteRepository(
                        List.of(
                                new Pendiente(
                                        30,
                                        "Pendiente temporal",
                                        "activo"
                                )
                        )
                );

        PendienteService service =
                new PendienteService(repository);

        // Act
        service.eliminar(30);

        // Assert
        assertFalse(
                service.buscarPorId(30).isPresent()
        );
    }

    @Test
    void debeRechazarLaConsultaDeUnPendienteInexistente() {
        // Arrange
        PendienteService service =
                new PendienteService(
                        new InMemoryPendienteRepository(
                                List.of()
                        )
                );

        // Act + Assert
        assertThrows(
                PendienteNoEncontradoException.class,
                () -> service.obtener(700)
        );
    }

    @Test
    void debeRechazarLaEliminacionDeUnPendienteInexistente() {
        // Arrange
        PendienteService service =
                new PendienteService(
                        new InMemoryPendienteRepository(
                                List.of()
                        )
                );

        // Act + Assert
        assertThrows(
                PendienteNoEncontradoException.class,
                () -> service.eliminar(800)
        );
    }

    @Test
    void debeCompletarUnPendienteActivo() {
        // Arrange
        PendienteService service =
                new PendienteService(
                        new InMemoryPendienteRepository(
                                List.of(
                                        new Pendiente(
                                                40,
                                                "Validar pipeline",
                                                "activo"
                                        )
                                )
                        )
                );

        // Act
        Pendiente completado =
                service.completar(40);

        // Assert
        assertEquals(
                "completado",
                completado.estado()
        );

        assertTrue(
                service.buscarPorId(40).isPresent()
        );

        assertEquals(
                "completado",
                service
                        .buscarPorId(40)
                        .orElseThrow()
                        .estado()
        );
    }
}
