# ADR-09: Suite de pruebas automatizadas y pipeline de Integración Continua en PendixAPP

| Campo | Valor |
| :--- | :--- |
| **Autor** | Angel Abraham Lugo Saenz |
| **Fecha** | 22/07/2026 |
| **Estado** | `Aceptado` |
| **Proyecto** | PendixAPP |
| **Rama** | `pipeline_CI` |
| **ADR relacionado** | ADR-08: Identificación y plan de pago de deuda técnica en PendixAPP |
 
---

## Contexto

PendixAPP es un prototipo desarrollado en Java para gestionar pendientes y recordatorios personales. La aplicación se ejecuta mediante un servidor HTTP local y permite consultar la interfaz desde el navegador utilizando el puerto `5018`.

En etapas anteriores, el funcionamiento del proyecto se comprobaba principalmente de forma manual. Para validar un cambio era necesario compilar la aplicación, iniciar el servidor, abrir el navegador y revisar visualmente que las funciones siguieran trabajando.

Este proceso resultaba suficiente durante las primeras versiones del prototipo, pero no permitía detectar automáticamente errores en la lógica de negocio, en las rutas HTTP o en la construcción de respuestas. Además, cada cambio subido al repositorio dependía de que el desarrollador recordara ejecutar las comprobaciones localmente.

Como parte de la evolución del proyecto y del pago gradual de la deuda técnica identificada en el ADR-08, se separaron responsabilidades que anteriormente estaban concentradas en una sola clase. Esta separación permitió crear componentes que pueden probarse de forma aislada:

```text
Pendiente
PendienteService
PendixRouter
HttpResult
```

También se incorporó Gradle como herramienta de compilación y administración de dependencias, junto con el Gradle Wrapper para asegurar que el proyecto utilice una versión consistente de Gradle tanto en el equipo local como en GitHub Actions.

El proyecto utiliza Java 21, por lo que la suite automatizada se implementó con JUnit 5. JUnit pertenece a la familia de frameworks xUnit y representa la alternativa correspondiente para proyectos Java.

La necesidad principal de esta decisión es asegurar que cada cambio enviado al repositorio sea compilado y probado automáticamente antes de considerarse válido.

---

## Problema

PendixAPP no contaba con un mecanismo automático para comprobar que los cambios conservaran el comportamiento esperado.

Esto generaba los siguientes riesgos:

```text
- Introducir errores en la lógica de pendientes sin detectarlos antes del push.
- Modificar una ruta HTTP y afectar otras respuestas.
- Aceptar datos inválidos dentro de las entidades.
- Romper la compilación del proyecto sin advertirlo antes de revisar la rama.
- Depender únicamente de pruebas manuales desde el navegador.
- Integrar cambios sin evidencia reproducible de que el proyecto funciona.
- Tener resultados diferentes entre el equipo local y el entorno de GitHub.
```

La falta de automatización también dificultaba demostrar de manera objetiva que el proyecto compilaba y que sus componentes principales funcionaban correctamente.

---

## Decisión

Se decidió incorporar una suite de pruebas automatizadas con **JUnit 5** y configurar un pipeline de **Integración Continua con GitHub Actions**.

La solución está formada por los siguientes elementos:

```text
- Java 21 como versión del lenguaje.
- Gradle para compilar y ejecutar las pruebas.
- Gradle Wrapper 8.14.3 para mantener una versión consistente.
- JUnit 5 para implementar las pruebas automatizadas.
- Estructura Arrange–Act–Assert en cada prueba.
- GitHub Actions para ejecutar la validación en cada push.
- GitHub Actions para ejecutar la validación en cada Pull Request.
- Documentación de las clases probadas y su justificación.
- Historial de commits separado por tipo de cambio.
```

La configuración principal se encuentra en:

```text
PendixAPP/build.gradle
PendixAPP/settings.gradle
PendixAPP/gradlew
PendixAPP/gradle/wrapper/
.github/workflows/java-ci.yml
```

---

# Framework de pruebas seleccionado

## JUnit 5

Se eligió JUnit 5 porque PendixAPP está desarrollado en Java 21.

La actividad solicita utilizar pruebas con xUnit. JUnit representa el enfoque xUnit dentro del ecosistema Java y permite definir casos de prueba independientes mediante anotaciones como:

```java
@Test
```

Las dependencias agregadas en Gradle son:

```gradle
dependencies {
    testImplementation platform('org.junit:junit-bom:5.12.2')
    testImplementation 'org.junit.jupiter:junit-jupiter'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

La ejecución utiliza JUnit Platform:

```gradle
test {
    useJUnitPlatform()
}
```

Se agregó `junit-platform-launcher` porque Gradle necesita el lanzador de la plataforma durante la ejecución de la suite. Sin esta dependencia, el ejecutor de pruebas podía finalizar antes de cargar correctamente los casos.

---

# Clases seleccionadas para las pruebas

Se eligieron cuatro clases del proyecto, superando el requisito mínimo de tres.

| Clase | Responsabilidad | Razón de selección |
| :--- | :--- | :--- |
| **Pendiente** | Representa un pendiente y administra su estado | Es una entidad principal y contiene reglas que deben mantenerse estables |
| **PendienteService** | Registra, consulta y completa pendientes | Contiene lógica de negocio y coordina operaciones importantes |
| **PendixRouter** | Resuelve rutas y métodos HTTP | Determina qué respuesta recibe el usuario según la solicitud |
| **HttpResult** | Encapsula el estado, el contenido y el cuerpo de una respuesta | Permite verificar que las respuestas HTTP sean coherentes y válidas |

Estas clases fueron seleccionadas porque representan diferentes niveles del sistema:

```text
Pendiente         → Entidad de dominio
PendienteService  → Lógica de aplicación
PendixRouter      → Enrutamiento HTTP
HttpResult        → Construcción de respuestas
```

La combinación permite comprobar reglas internas y comportamiento externo sin depender de abrir el navegador.

---

# Comportamientos probados

## `Pendiente`

Las pruebas verifican:

```text
- Que un pendiente pueda marcarse como completado.
- Que un título vacío o inválido sea rechazado.
```

Estas pruebas protegen las reglas básicas de la entidad principal.

## `PendienteService`

Las pruebas verifican:

```text
- Que un pendiente pueda agregarse.
- Que un pendiente pueda buscarse por su identificador.
- Que un pendiente existente pueda completarse.
- Que no se permitan identificadores duplicados.
```

Estas pruebas se eligieron porque el servicio concentra operaciones que afectan directamente el estado de los pendientes.

## `PendixRouter`

Las pruebas verifican:

```text
- Que la ruta principal entregue la respuesta esperada.
- Que se rechacen métodos HTTP no permitidos.
- Que una ruta inexistente produzca una respuesta 404.
```

Estas pruebas ayudan a detectar errores en la selección de rutas y códigos HTTP.

## `HttpResult`

Las pruebas verifican:

```text
- Que la respuesta conserve su código, tipo de contenido y cuerpo.
- Que se rechacen códigos de estado fuera del rango HTTP válido.
```

Estas comprobaciones evitan crear respuestas inconsistentes desde el servidor.

---

# Estructura Arrange–Act–Assert

Cada prueba sigue la estructura **Arrange–Act–Assert**.

```text
Arrange: prepara objetos, entradas y condiciones iniciales.
Act: ejecuta la operación que se desea comprobar.
Assert: verifica que el resultado coincida con lo esperado.
```

Ejemplo:

```java
@Test
void debeCompletarUnPendiente() {
    // Arrange
    Pendiente pendiente = new Pendiente(1, "Realizar actividad");

    // Act
    pendiente.completar();

    // Assert
    assertTrue(pendiente.isCompletado());
}
```

La estructura AAA fue seleccionada porque permite identificar con claridad la preparación, la acción y la validación de cada caso.

---

# Organización de la suite

La estructura de pruebas quedó organizada de la siguiente manera:

```text
PendixAPP/
└── src/
    ├── main/
    │   └── java/
    │       ├── HttpResult.java
    │       ├── Pendiente.java
    │       ├── PendienteService.java
    │       ├── PendixAppServer.java
    │       └── PendixRouter.java
    │
    └── test/
        └── java/
            ├── HttpResultTest.java
            ├── PendienteTest.java
            ├── PendienteServiceTest.java
            └── PendixRouterTest.java
```

Esta separación permite distinguir claramente el código de producción del código de prueba.

---

# Ejecución local

La suite puede ejecutarse desde la carpeta `PendixAPP` con:

```bash
./gradlew clean test
```

La validación completa se ejecuta con:

```bash
./gradlew clean test build --no-daemon
```

El resultado esperado es:

```text
BUILD SUCCESSFUL
```

Gradle genera un reporte HTML en:

```text
PendixAPP/build/reports/tests/test/index.html
```

En Arch Linux puede abrirse con:

```bash
xdg-open build/reports/tests/test/index.html
```

El reporte muestra el número de pruebas ejecutadas, fallos, pruebas ignoradas y porcentaje de éxito.

---

# Pipeline de Integración Continua

## Ubicación

El workflow se encuentra en la raíz del repositorio:

```text
.github/workflows/java-ci.yml
```

La ubicación es importante porque GitHub solamente detecta workflows dentro de `.github/workflows/` en la raíz del repositorio.

Durante la configuración inicial, el archivo se encontraba dentro de `PendixAPP/.github/workflows/`. Fue necesario moverlo a la raíz para que GitHub Actions pudiera reconocerlo y ejecutarlo.

---

## Eventos que activan el pipeline

El workflow se ejecuta en:

```yaml
on:
  push:
  pull_request:
```

Esto permite validar automáticamente:

```text
- Cada cambio enviado mediante git push.
- Cada Pull Request creado o actualizado.
```

---

## Pasos del pipeline

El pipeline realiza los siguientes pasos:

```text
1. Descarga el repositorio.
2. Configura Java 21 mediante Temurin.
3. Configura la caché de Gradle.
4. Utiliza PendixAPP como directorio de trabajo.
5. Otorga permisos de ejecución a gradlew.
6. Ejecuta clean, test y build.
7. Finaliza en verde si la compilación y las pruebas pasan.
8. Finaliza en rojo si existe un error.
```

El comando principal es:

```bash
./gradlew clean test build --no-daemon
```

---

## Workflow aprobado

```yaml
name: Java CI con Gradle

on:
  push:
  pull_request:

permissions:
  contents: read

jobs:
  build-and-test:
    runs-on: ubuntu-latest

    defaults:
      run:
        working-directory: PendixAPP

    steps:
      - name: Descargar repositorio
        uses: actions/checkout@v4

      - name: Configurar Java 21
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'
          cache: gradle
          cache-dependency-path: |
            PendixAPP/*.gradle*
            PendixAPP/gradle/wrapper/gradle-wrapper.properties

      - name: Dar permisos a Gradle Wrapper
        run: chmod +x gradlew

      - name: Compilar y ejecutar pruebas
        run: ./gradlew clean test build --no-daemon
```

---

# Evidencia

La decisión se considera verificada mediante las siguientes evidencias:

```text
- Ejecución local terminada en BUILD SUCCESSFUL.
- Reporte HTML de Gradle sin pruebas fallidas.
- Workflow visible en la pestaña Actions de GitHub.
- Ejecución build-and-test finalizada con check verde.
- Pull Request con el mensaje All checks have passed.
- Historial de commits que muestra la evolución de la actividad.
```

Rutas sugeridas para guardar las capturas:

```text
assets/01-pruebas-junit-aprobadas.png
assets/02-pipeline-github-actions-verde.png
assets/03-pull-request-check-verde.png
```

---

# Historial de evolución

Los cambios se organizaron en commits separados para mostrar la evolución del trabajo.

Ejemplos:

```text
refactor: preparar PendixAPP para pruebas automatizadas
test: agregar suite JUnit con Arrange Act Assert
ci: configurar pipeline de pruebas con GitHub Actions
docs: documentar clases probadas y estrategia de CI
fix: ubicar workflow en la raiz del repositorio
```

Esta separación permite identificar:

```text
- La preparación del proyecto.
- La creación de la suite.
- La configuración del pipeline.
- La documentación de la decisión.
- Los ajustes necesarios para que GitHub detectara el workflow.
```

---

## Alternativas consideradas

| Alternativa | Motivo por el que se descartó |
| :--- | :--- |
| Continuar únicamente con pruebas manuales | No permite validar automáticamente cada cambio |
| Utilizar xUnit de .NET | PendixAPP está desarrollado en Java y no es compatible con ese framework |
| Utilizar una versión antigua de JUnit | JUnit 5 ofrece integración actual con Java 21 y Gradle |
| Ejecutar las pruebas solo en el equipo local | No genera una validación independiente en GitHub |
| Configurar el workflow dentro de `PendixAPP/.github` | GitHub no detecta workflows fuera de la raíz del repositorio |
| Utilizar Gradle instalado globalmente en CI | Podría producir diferencias de versión entre entornos |
| Crear un solo commit con todos los cambios | No mostraría claramente la evolución solicitada |
| Ejecutar el pipeline solo manualmente | No cumpliría la validación automática en cada cambio |

---

## Consecuencias

### Lo que se gana

```text
- Detección automática de errores en cada cambio.
- Validación independiente dentro de GitHub.
- Evidencia reproducible del funcionamiento del proyecto.
- Mayor seguridad antes de integrar una rama.
- Pruebas aisladas de reglas de dominio, servicio, rutas y respuestas.
- Compilación consistente mediante Gradle Wrapper.
- Compatibilidad con Java 21.
- Historial de cambios más claro.
- Base preparada para agregar más pruebas en el futuro.
```

### Lo que se sacrifica o asume

```text
- Se agregan archivos de configuración y dependencias.
- El proyecto debe mantener actualizado el workflow.
- Las pruebas deberán ajustarse cuando cambien las reglas del sistema.
- El pipeline aumenta ligeramente el tiempo de cada push.
- El equipo debe evitar subir carpetas temporales como build y .gradle.
- Las nuevas funciones deberán acompañarse de nuevas pruebas.
- Una falla real impedirá obtener el check verde hasta ser corregida.
```

---

## Relación con el ADR-08

El ADR-08 identificó que `PendixAppServer.java` concentraba demasiadas responsabilidades y que esa estructura dificultaba realizar pruebas aisladas.

La creación de:

```text
Pendiente
PendienteService
PendixRouter
HttpResult
```

representa un avance en el pago de esa deuda técnica, porque permite separar la entidad, la lógica de aplicación, el enrutamiento y las respuestas HTTP.

El ADR-09 continúa esa evolución al establecer pruebas automatizadas para esas clases y un pipeline que verifica continuamente que la separación siga funcionando.

Por lo tanto, la Integración Continua no se agregó como un elemento aislado, sino como una medida para proteger las mejoras arquitectónicas realizadas en el proyecto.

---

## Criterios de aceptación

La decisión se considera implementada cuando:

```text
[x] El proyecto utiliza Java 21.
[x] El proyecto puede compilarse mediante Gradle.
[x] Existe un Gradle Wrapper dentro del repositorio.
[x] JUnit 5 está configurado.
[x] JUnit Platform Launcher está configurado.
[x] Existen pruebas para al menos tres clases.
[x] Se probaron cuatro clases.
[x] Las pruebas siguen Arrange–Act–Assert.
[x] La ejecución local termina en BUILD SUCCESSFUL.
[x] Existe un workflow dentro de .github/workflows.
[x] El workflow utiliza Java 21.
[x] El workflow compila el proyecto.
[x] El workflow ejecuta las pruebas.
[x] El workflow se activa en cada push.
[x] El workflow se activa en cada Pull Request.
[x] Las clases probadas y su justificación están documentadas.
[x] El historial muestra commits separados.
[ ] Se agrega la captura definitiva del pipeline en verde.
[ ] Se agrega el enlace del Pull Request con el check aprobado.
```

---

## Mejoras futuras

```text
[ ] Incorporar pruebas parametrizadas.
[ ] Agregar pruebas para fechas y pendientes vencidos.
[ ] Crear pruebas de integración para el servidor HTTP.
[ ] Publicar el reporte de Gradle como artefacto del workflow.
[ ] Incorporar JaCoCo para medir cobertura.
[ ] Definir un porcentaje mínimo de cobertura.
[ ] Configurar protección de ramas.
[ ] Impedir merge cuando el pipeline falle.
[ ] Agregar análisis estático del código.
[ ] Separar pruebas unitarias y pruebas de integración.
[ ] Ejecutar pruebas en más de una versión de Java si el proyecto lo requiere.
```

---

## Decisión final

Se acepta la incorporación de JUnit 5, Gradle y GitHub Actions como base de pruebas e Integración Continua para PendixAPP.

Las pruebas cubren cuatro clases representativas del sistema y siguen la estructura Arrange–Act–Assert. Gradle permite ejecutar la suite y compilar el proyecto de forma repetible, mientras que el Gradle Wrapper conserva una versión consistente entre el entorno local y GitHub.

El workflow se ejecuta automáticamente en cada `push` y `pull_request`, utilizando Java 21 y el directorio `PendixAPP`. Si la compilación o una prueba falla, GitHub Actions marca la ejecución en rojo y evita considerar el cambio como validado.

Esta decisión permite detectar regresiones, generar evidencia técnica y proteger la evolución del proyecto. También establece una base para ampliar la cobertura, agregar pruebas de integración y aplicar reglas de protección sobre las ramas en versiones futuras.

---

## Cláusula de IA

Yo, Angel Abraham Lugo Saenz, declaro que utilicé inteligencia artificial como apoyo para analizar la estructura de PendixAPP, organizar la suite de pruebas, configurar Gradle y GitHub Actions, resolver errores de ejecución y redactar este ADR.

La revisión del código, la ejecución de las pruebas, la configuración del repositorio, la validación del pipeline y la adaptación final del contenido fueron realizadas como parte de la actividad escolar de Arquitectura de Software.
