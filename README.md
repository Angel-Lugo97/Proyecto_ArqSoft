# PendixAPP — Rama `pipeline_CI`

PendixAPP es una aplicación enfocada en la gestión de pendientes y recordatorios personales. El sistema permite registrar tareas, consultar actividades, marcar pendientes como completados y organizar recordatorios desde una interfaz web ejecutada localmente con Java.

En esta rama llamada **`pipeline_CI`** se agregó una suite de pruebas automatizadas con **JUnit 5**, siguiendo la estructura **Arrange–Act–Assert**, además de un pipeline de **Integración Continua con GitHub Actions** que compila el proyecto y ejecuta las pruebas automáticamente en cada `push` y `pull_request`.

El objetivo de esta rama es comprobar que la lógica principal de PendixAPP se mantiene estable ante nuevos cambios, detectar errores antes de integrar código y documentar qué clases fueron seleccionadas para las pruebas y por qué.

---

## Datos del estudiante

| Campo | Información |
| :--- | :--- |
| **Nombre** | Angel Abraham Lugo Saenz |
| **Matrícula** | SW2409052 |
| **Materia** | Arquitectura de Software |
| **Profesor** | Jorge Javier Pedroza Romero |
| **Proyecto** | PendixAPP |
| **Actividad** | Actividad #37 — Suite de pruebas y pipeline CI |
| **Fecha** | 22/07/2026 |
| **Rama** | `pipeline_CI` |
| **Estado** | Suite de pruebas y pipeline CI configurados |

---

## Descripción general

PendixAPP es un prototipo funcional orientado a la gestión de pendientes personales.

El proyecto utiliza **Java 21** y un servidor HTTP local que se ejecuta en el puerto `5018`. La aplicación presenta una interfaz web tipo celular desde la que se pueden visualizar pendientes, filtros, recordatorios, calendario, planes, ajustes e inicio de sesión simulado.

Antes de esta rama, la validación del proyecto dependía principalmente de ejecutar manualmente la aplicación y comprobar su comportamiento desde el navegador. En `pipeline_CI` se incorporó una estructura de compilación con **Gradle**, pruebas automatizadas con **JUnit 5** y un workflow de **GitHub Actions**.

---

## Objetivo de esta rama

El objetivo de la rama **`pipeline_CI`** es agregar pruebas automatizadas y configurar un proceso de Integración Continua que valide el proyecto en cada cambio.

Esta rama permite:

```text
- Ejecutar pruebas automatizadas sobre la lógica principal del proyecto.
- Aplicar la estructura Arrange–Act–Assert en las pruebas.
- Compilar PendixAPP mediante Gradle.
- Ejecutar la suite de pruebas en cada push.
- Ejecutar la suite de pruebas en cada Pull Request.
- Detectar errores antes de integrar cambios.
- Documentar qué clases se probaron y por qué fueron seleccionadas.
- Conservar un historial de commits que muestre la evolución de la actividad.
```

---

## Tecnologías utilizadas

| Tecnología | Uso dentro de la rama |
| :--- | :--- |
| **Java 21** | Lenguaje y versión utilizada para compilar el proyecto |
| **JUnit 5** | Framework de pruebas automatizadas de la familia xUnit para Java |
| **Gradle 8.14.3** | Compilación, administración de dependencias y ejecución de pruebas |
| **Gradle Wrapper** | Garantiza la misma versión de Gradle en local y GitHub Actions |
| **GitHub Actions** | Pipeline de Integración Continua |
| **Git y GitHub** | Control de versiones, ramas, commits y Pull Request |

> Debido a que PendixAPP está desarrollado en Java, se utilizó **JUnit 5**, que representa el enfoque xUnit dentro del ecosistema Java.

---

# Suite de pruebas automatizadas

La suite se encuentra en:

```text
PendixAPP/src/test/java/
```

Clases de prueba agregadas:

```text
HttpResultTest.java
PendienteTest.java
PendienteServiceTest.java
PendixRouterTest.java
```

La suite verifica cuatro clases de producción, superando el requisito mínimo de tres clases.

| Clase probada | Responsabilidad | Motivo de selección |
| :--- | :--- | :--- |
| **Pendiente** | Representa un pendiente y administra su estado | Es una entidad principal del sistema y contiene reglas básicas que deben mantenerse estables |
| **PendienteService** | Registra, busca y completa pendientes | Contiene lógica de negocio y operaciones que pueden afectar directamente el funcionamiento de la aplicación |
| **PendixRouter** | Selecciona respuestas según la ruta y el método HTTP | Es responsable de dirigir las solicitudes y devolver respuestas correctas |
| **HttpResult** | Encapsula estado HTTP, tipo de contenido y cuerpo | Permite validar que las respuestas generadas tengan información coherente |

---

## Comportamientos verificados

### Pruebas de `Pendiente`

```text
- Permite marcar un pendiente como completado.
- Rechaza un título vacío o inválido.
```

### Pruebas de `PendienteService`

```text
- Permite agregar y buscar un pendiente.
- Permite completar un pendiente existente.
- Rechaza identificadores duplicados.
```

### Pruebas de `PendixRouter`

```text
- Entrega la página principal en la ruta esperada.
- Rechaza métodos HTTP que no están permitidos.
- Devuelve 404 cuando la ruta no existe.
```

### Pruebas de `HttpResult`

```text
- Conserva correctamente los datos de una respuesta HTTP.
- Rechaza códigos de estado fuera del rango válido.
```

---

# Estructura Arrange–Act–Assert

Las pruebas siguen la estructura **Arrange–Act–Assert**, también conocida como AAA.

```text
Arrange: prepara los objetos, datos y condiciones de la prueba.
Act: ejecuta el comportamiento que se desea comprobar.
Assert: compara el resultado obtenido con el resultado esperado.
```

Ejemplo:

```java
@Test
void debeMarcarPendienteComoCompletado() {
    // Arrange
    Pendiente pendiente = new Pendiente(1, "Realizar actividad");

    // Act
    pendiente.completar();

    // Assert
    assertTrue(pendiente.isCompletado());
}
```

Esta estructura ayuda a que las pruebas sean fáciles de leer, mantener y revisar.

---

# Configuración con Gradle

La compilación y las pruebas se administran mediante:

```text
PendixAPP/build.gradle
PendixAPP/settings.gradle
PendixAPP/gradlew
PendixAPP/gradlew.bat
PendixAPP/gradle/wrapper/
```

Dependencias principales de prueba:

```gradle
dependencies {
    testImplementation platform('org.junit:junit-bom:5.12.2')
    testImplementation 'org.junit.jupiter:junit-jupiter'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

La tarea de pruebas utiliza JUnit Platform:

```gradle
test {
    useJUnitPlatform()
}
```

---

## Ejecutar las pruebas localmente

Desde la carpeta `PendixAPP`:

```bash
./gradlew clean test
```

Para ejecutar pruebas y compilación completa:

```bash
./gradlew clean test build --no-daemon
```

Resultado esperado:

```text
BUILD SUCCESSFUL
```

---

## Reporte local de pruebas

Gradle genera un reporte HTML en:

```text
PendixAPP/build/reports/tests/test/index.html
```

En Arch Linux puede abrirse con:

```bash
xdg-open build/reports/tests/test/index.html
```

El reporte permite consultar:

```text
- Número total de pruebas.
- Pruebas aprobadas.
- Pruebas fallidas.
- Porcentaje de éxito.
- Tiempo de ejecución.
- Resultados por clase.
```

---

# Pipeline de Integración Continua

El workflow se encuentra en la raíz del repositorio:

```text
.github/workflows/java-ci.yml
```

Se ubicó en la raíz porque GitHub solamente detecta workflows almacenados dentro de:

```text
.github/workflows/
```

El pipeline está configurado para ejecutarse cuando ocurre:

```yaml
on:
  push:
  pull_request:
```

Esto significa que cada cambio subido a GitHub y cada Pull Request generan una nueva validación automática.

---

## Proceso ejecutado por GitHub Actions

El pipeline realiza los siguientes pasos:

```text
1. Descarga el contenido del repositorio.
2. Configura Java 21.
3. Utiliza el Gradle Wrapper del proyecto.
4. Da permisos de ejecución a gradlew.
5. Compila PendixAPP.
6. Ejecuta la suite de pruebas.
7. Marca la ejecución en verde si todo funciona.
8. Marca la ejecución en rojo si la compilación o una prueba falla.
```

Comando principal del workflow:

```bash
./gradlew clean test build --no-daemon
```

Debido a que el proyecto Gradle está dentro de `PendixAPP`, el workflow utiliza esa carpeta como directorio de trabajo.

---

## Workflow utilizado

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

# Evidencia de pruebas locales

La siguiente captura debe mostrar el reporte generado por Gradle con las pruebas aprobadas.

Ruta recomendada:

```text
assets/01-pruebas-junit-aprobadas.png
```

![Reporte local de pruebas JUnit aprobadas](assets/01-pruebas-junit-aprobadas.png)

> La evidencia debe mostrar que no existen fallos y que la suite terminó correctamente.

---

# Evidencia del pipeline CI

La siguiente captura debe mostrar la ejecución del workflow **Java CI con Gradle** dentro de GitHub Actions.

Ruta recomendada:

```text
assets/02-pipeline-github-actions-verde.png
```

![Pipeline de GitHub Actions ejecutado correctamente](assets/02-pipeline-github-actions-verde.png)

La captura debe permitir observar:

```text
- Nombre del workflow.
- Rama pipeline_CI.
- Trabajo build-and-test.
- Pasos de configuración de Java.
- Compilación y ejecución de pruebas.
- Check verde.
```

---

# Evidencia del Pull Request

El Pull Request permite comprobar que los cambios pueden revisarse antes de integrarse a otra rama y que el pipeline se ejecuta correctamente.

Ruta recomendada:

```text
assets/03-pull-request-check-verde.png
```

![Pull Request con el check verde](assets/03-pull-request-check-verde.png)

Dentro del Pull Request debe aparecer:

```text
All checks have passed
```

---

# ADR de pruebas automatizadas y CI

La decisión técnica se encuentra documentada en:

[`PendixAPP/docs/ADR-08-Pruebas-Automatizadas-CI.md`](PendixAPP/docs/ADR-08-Pruebas-Automatizadas-CI.md)

El ADR explica:

```text
- Por qué se agregó una suite automatizada.
- Por qué se utilizó JUnit 5.
- Qué clases fueron probadas.
- Por qué se eligieron esas clases.
- Qué comportamientos se validan.
- Cómo se ejecutan las pruebas localmente.
- Cómo funciona GitHub Actions.
- Beneficios y consecuencias de la decisión.
```

---

# Estructura principal de la rama

```text
Proyecto_ArqSoft/
│
├── .github/
│   └── workflows/
│       └── java-ci.yml
│
├── README.md
│
└── PendixAPP/
    ├── build.gradle
    ├── settings.gradle
    ├── gradlew
    ├── gradlew.bat
    │
    ├── gradle/
    │   └── wrapper/
    │       ├── gradle-wrapper.jar
    │       └── gradle-wrapper.properties
    │
    ├── src/
    │   ├── main/
    │   │   └── java/
    │   │       ├── HttpResult.java
    │   │       ├── Pendiente.java
    │   │       ├── PendienteService.java
    │   │       ├── PendixAppServer.java
    │   │       └── PendixRouter.java
    │   │
    │   └── test/
    │       └── java/
    │           ├── HttpResultTest.java
    │           ├── PendienteTest.java
    │           ├── PendienteServiceTest.java
    │           └── PendixRouterTest.java
    │
    ├── docs/
    │   └── ADR-08-Pruebas-Automatizadas-CI.md
    │
    ├── iniciar_linux.sh
    ├── iniciar_mac.command
    ├── INICIAR_WINDOWS.bat
    ├── compilar_y_ejecutar_linux.sh
    └── README_EJECUCION.md
```

---

# Ejecución de PendixAPP

Además de las pruebas automatizadas, la aplicación puede ejecutarse localmente para comprobar su funcionamiento visual.

Desde la carpeta `PendixAPP`:

```bash
./gradlew run
```

Después se abre en el navegador:

```text
http://localhost:5018
```

También puede utilizarse:

```bash
bash iniciar_linux.sh
```

La interfaz conserva:

```text
- Gestión visual de pendientes.
- Filtros por estado.
- Notificaciones vencidas.
- Calendario y recordatorios.
- Sección de planes.
- Inicio de sesión simulado.
- Tema oscuro con tonos púrpuras.
```

---

## Evidencia de ejecución funcional

![Captura de ejecución funcional de PendixAPP](Captura.png)

```text
URL local: http://localhost:5018
Java: 21
Puerto: 5018
```

---

# Historial de commits

La actividad se desarrolló mediante commits separados para mostrar la evolución del trabajo.

```text
docs: documentar clases probadas y estrategia de CI
ci: configurar pipeline de pruebas con GitHub Actions
test: agregar suite JUnit con Arrange Act Assert
refactor: preparar PendixAPP para pruebas automatizadas
fix: ubicar workflow en la raiz del repositorio
```

Para consultar el historial:

```bash
git log --oneline --decorate --graph -10
```

La separación de commits permite distinguir:

```text
- Preparación y refactorización del proyecto.
- Incorporación de pruebas.
- Configuración del pipeline.
- Documentación del ADR.
- Ajustes necesarios para la ejecución en GitHub.
```

---

# Proceso de trabajo con Git

Cambiar a la rama:

```bash
git switch pipeline_CI
```

Ejecutar pruebas:

```bash
cd PendixAPP
./gradlew clean test build --no-daemon
```

Subir cambios:

```bash
cd ..
git push origin pipeline_CI
```

Revisar diferencias con la rama anterior:

```bash
git diff Deuda-Tecnica..pipeline_CI
```

---

# Cómo revisar la rama en GitHub

```text
1. Entrar al repositorio Proyecto_ArqSoft.
2. Cambiar a la rama pipeline_CI.
3. Abrir README.md.
4. Revisar PendixAPP/src/test/java.
5. Confirmar que existen pruebas para al menos tres clases.
6. Revisar .github/workflows/java-ci.yml.
7. Abrir PendixAPP/docs/ADR-08-Pruebas-Automatizadas-CI.md.
8. Entrar en la pestaña Actions.
9. Abrir la ejecución Java CI con Gradle.
10. Confirmar que build-and-test aparece en verde.
11. Abrir el Pull Request.
12. Verificar que aparezca All checks have passed.
13. Revisar el historial de commits.
```

---

# Lista de verificación de la actividad

```text
[x] Se agregó una suite de pruebas automatizadas.
[x] Se utilizó JUnit 5 como framework xUnit para Java.
[x] Se probaron al menos tres clases del proyecto.
[x] Se probaron cuatro clases en total.
[x] Las pruebas siguen Arrange–Act–Assert.
[x] Las pruebas se ejecutan mediante Gradle.
[x] La compilación local termina en BUILD SUCCESSFUL.
[x] Se agregó el Gradle Wrapper.
[x] Se configuró Java 21.
[x] Se creó un workflow de GitHub Actions.
[x] El workflow se ejecuta en cada push.
[x] El workflow se ejecuta en cada Pull Request.
[x] El pipeline compila y ejecuta las pruebas.
[x] Se documentaron las clases probadas en el ADR.
[x] Se explicó por qué fueron seleccionadas.
[x] Los cambios se organizaron en commits separados.
[ ] Agregar la captura del reporte local en assets.
[ ] Agregar la captura del pipeline verde en assets.
[ ] Agregar la captura del Pull Request en assets.
[ ] Copiar el enlace del Pull Request para la entrega.
[ ] Entregar el enlace actualizado del repositorio.
```

---

# Mejoras futuras

```text
[ ] Agregar pruebas para rutas HTTP adicionales.
[ ] Incorporar pruebas parametrizadas.
[ ] Agregar pruebas para fechas y pendientes vencidos.
[ ] Agregar pruebas de integración del servidor.
[ ] Publicar el reporte de pruebas como artefacto de GitHub Actions.
[ ] Agregar análisis de cobertura con JaCoCo.
[ ] Definir un porcentaje mínimo de cobertura.
[ ] Bloquear la integración cuando el pipeline falle.
[ ] Proteger la rama principal mediante reglas de revisión.
[ ] Separar pruebas unitarias y pruebas de integración.
[ ] Ejecutar análisis estático del código.
```

---

# Resultado de esta rama

La rama **`pipeline_CI`** transforma la validación de PendixAPP de un proceso principalmente manual a un proceso automatizado y repetible.

Ahora la lógica principal puede comprobarse sin abrir el navegador, Gradle administra la compilación y las dependencias, JUnit ejecuta las pruebas y GitHub Actions repite la validación automáticamente en cada cambio.

Esta integración reduce el riesgo de introducir errores, permite detectar fallos antes de integrar código y genera evidencia visible mediante los checks del pipeline y del Pull Request.

---

# Conclusión

En esta rama se agregó una suite de pruebas automatizadas para cuatro clases principales de PendixAPP: `Pendiente`, `PendienteService`, `PendixRouter` y `HttpResult`.

Las pruebas fueron desarrolladas con JUnit 5 y siguen la estructura Arrange–Act–Assert. También se configuró Gradle con Java 21 para compilar el proyecto y ejecutar la suite de forma local.

Finalmente, se agregó un workflow de GitHub Actions que repite este proceso en cada `push` y `pull_request`. La estrategia de pruebas y las razones de selección de las clases quedaron documentadas en el ADR correspondiente.

Con estos cambios, PendixAPP cuenta con una base para validar futuras modificaciones, detectar regresiones y mantener un proceso de integración más seguro y ordenado.

---

## Cláusula de IA

```text
Yo, Angel Abraham Lugo Saenz, declaro que utilicé IA como apoyo para analizar la estructura de PendixAPP, organizar la suite de pruebas, configurar el pipeline de Integración Continua y redactar la documentación de esta rama.

El código, las decisiones del proyecto, la ejecución de las pruebas, la validación del pipeline y la adaptación final de la documentación fueron revisados como parte de la actividad escolar de Arquitectura de Software.
```
