# ADR-08 — Suite de pruebas automatizadas y pipeline de Integración Continua

- **Estado:** Aceptado
- **Fecha:** 2026-07-22

## Contexto

PendixAPP no contaba con pruebas automatizadas ni con un mecanismo que verificara el proyecto después de cada cambio. Esto permitía que un error de compilación o una modificación incorrecta llegara al repositorio sin detectarse antes.

## Decisión

Se incorporó Gradle como herramienta de construcción, JUnit 5 como framework de pruebas de la familia xUnit para Java y un workflow de GitHub Actions que compila el proyecto y ejecuta la suite en cada `push` y `pull_request`.

Las pruebas siguen la estructura **Arrange-Act-Assert** para separar la preparación de datos, la ejecución del comportamiento y la comprobación del resultado.

## Clases probadas y justificación

### `Pendiente`

Se prueba porque representa la información básica de una tarea y contiene reglas importantes: el identificador debe ser positivo, el título no puede estar vacío y el estado debe ser válido. También se comprueba el cambio de una tarea al estado `completado`.

### `PendienteService`

Se eligió porque concentra operaciones del dominio, como agregar, buscar y completar pendientes. Una falla en esta clase afectaría directamente la administración de tareas y podría provocar registros duplicados o cambios de estado incorrectos.

### `PendixRouter`

Se prueba porque decide qué respuesta HTTP recibe el navegador. Se verifican la página principal, los métodos no permitidos y las rutas inexistentes, ya que un error en esta clase impediría acceder correctamente a la aplicación o a su API.

### `HttpResult`

También se cubre esta clase porque transporta el código HTTP, el tipo de contenido y el cuerpo de cada respuesta. Su validación evita crear respuestas con códigos fuera del rango permitido.

## Pipeline CI

El archivo `.github/workflows/java-ci.yml` ejecuta los siguientes pasos:

1. Descarga el repositorio.
2. Configura Java 21.
3. Configura Gradle.
4. Ejecuta `./gradlew clean test build --no-daemon`.
5. Publica el reporte HTML de las pruebas como artefacto.

## Consecuencias

### Positivas

- Los errores de compilación se detectan automáticamente.
- Las reglas principales quedan protegidas por pruebas repetibles.
- Cada push y Pull Request muestra un check de CI.
- El reporte de pruebas queda disponible como evidencia.

### Negativas

- El repositorio incorpora archivos de configuración adicionales.
- Cada cambio tarda unos minutos más en validarse debido a la compilación y ejecución de pruebas.
