-- Datos iniciales para ejecutar y demostrar PendixAPP.

INSERT INTO pendientes (
    id,
    titulo,
    estado
)
VALUES
    (
        1,
        'Preparar presentación',
        'vencido'
    ),
    (
        2,
        'Comprar víveres',
        'activo'
    ),
    (
        3,
        'Enviar reporte semanal',
        'completado'
    )
ON CONFLICT (id) DO NOTHING;

SELECT setval(
    pg_get_serial_sequence(
        'pendientes',
        'id'
    ),
    GREATEST(
        COALESCE(
            (
                SELECT MAX(id)
                FROM pendientes
            ),
            1
        ),
        1
    ),
    true
);
