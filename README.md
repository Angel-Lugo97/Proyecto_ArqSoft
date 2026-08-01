# PendixAPP — Rama `Entrega-Final`

PendixAPP es una aplicación enfocada en la gestión de pendientes y recordatorios personales. El sistema permite registrar tareas, consultar actividades, marcar pendientes como completados y organizar recordatorios desde una interfaz web desarrollada con Java, HTML, CSS y JavaScript.

En esta rama llamada **`Entrega-Final`** se conservaron las pruebas automatizadas con **JUnit 5**, la estructura **Arrange–Act–Assert** y el pipeline de Integración Continua implementados anteriormente. Además, el proyecto fue adaptado para separar físicamente el frontend y el backend:

```text
Frontend:
GitHub Pages

Backend:
Laptop del desarrollador + Java 21 + Cloudflare Quick Tunnel
```

También se incorporó una vista adaptable para teléfonos, un script de ejecución para publicar el backend, endpoints de diagnóstico, compresión GZIP, configuración CORS, despliegue mediante GitHub Actions y una etapa de autorización manual antes de publicar cambios en GitHub Pages.

El objetivo de esta rama es consolidar la arquitectura final de PendixAPP, comprobar que la lógica principal se mantiene estable, publicar la interfaz en internet, exponer temporalmente el backend local y documentar las decisiones, riesgos, trade-offs y puntos de sensibilidad de la solución.

---

## Datos del estudiante

| Campo | Información |
| :--- | :--- |
| **Nombre** | Angel Abraham Lugo Saenz |
| **Matrícula** | SW2409052 |
| **Materia** | Arquitectura de Software |
| **Profesor** | Jorge Javier Pedroza Romero |
| **Proyecto** | PendixAPP |
| **Actividad** | Actividad #40 — Documentación final, demo funcional y evaluación ATAM |
| **Fecha** | 31/07/2026 |
| **Rama** | `Entrega-Final` |
| **Estado** | Arquitectura final, pruebas, CI/CD, GitHub Pages y Cloudflare Tunnel configurados |

---

## Descripción general

PendixAPP es un prototipo funcional orientado a la gestión de pendientes personales.

El proyecto utiliza **Java 21** y un servidor HTTP local que se ejecuta en el puerto `5018`. La aplicación presenta una interfaz web tipo celular desde la que se pueden visualizar pendientes, filtros, recordatorios, calendario, planes, ajustes e inicio de sesión simulado.

Antes de las ramas de pruebas, la validación del proyecto dependía principalmente de ejecutar manualmente la aplicación y comprobar su comportamiento desde el navegador. Posteriormente se incorporó una estructura de compilación con **Gradle**, pruebas automatizadas con **JUnit 5** y un workflow de **GitHub Actions**.

En `Entrega-Final`, la solución se amplió con los siguientes componentes:

```text
- Frontend estático desplegado en GitHub Pages.
- Backend Java ejecutado localmente en la laptop.
- Cloudflare Quick Tunnel para publicar el backend mediante HTTPS.
- Vista responsive para computadoras y teléfonos.
- Endpoints /health y /version para diagnóstico.
- Recursos HTML, CSS y JavaScript separados.
- Compresión GZIP para recursos web.
- Configuración CORS para permitir comunicación entre dominios.
- Script serve.sh para compilar, ejecutar y publicar la aplicación.
- Pipeline CI/CD con pruebas y despliegue de GitHub Pages.
- Environment github-pages con autorización manual.
- Evaluación ATAM integrada en este mismo README.
```

El frontend publicado permanece disponible aunque la laptop esté apagada. Sin embargo, las operaciones que dependan del backend solo funcionan mientras Java y `cloudflared` permanezcan activos en la computadora del desarrollador.

Sitio publicado:

```text
https://angel-lugo97.github.io/Proyecto_ArqSoft/
```

---

## Objetivo de esta rama

El objetivo de la rama **`Entrega-Final`** es integrar la versión final de PendixAPP, mantener las pruebas automatizadas, configurar un flujo CI/CD y demostrar una separación real entre frontend y backend sin utilizar AWS ni servicios de pago.

Esta rama permite:

```text
- Ejecutar pruebas automatizadas sobre la lógica principal del proyecto.
- Aplicar la estructura Arrange–Act–Assert en las pruebas.
- Compilar PendixAPP mediante Gradle.
- Ejecutar la suite de pruebas en cada push.
- Ejecutar la suite de pruebas en cada Pull Request.
- Detectar errores antes de integrar cambios.
- Documentar qué clases se probaron y por qué fueron seleccionadas.
- Publicar el frontend mediante GitHub Pages.
- Ejecutar el backend desde la laptop del desarrollador.
- Exponer el backend con una URL HTTPS mediante Cloudflare Tunnel.
- Permitir una autorización manual antes del despliegue.
- Adaptar la interfaz para teléfonos y computadoras.
- Mantener una URL permanente para el frontend.
- Configurar dinámicamente la URL temporal del backend.
- Documentar la arquitectura mediante ADR, C4 y ATAM dentro de este README.
```

---

## Tecnologías utilizadas

| Tecnología | Uso dentro de la rama |
| :--- | :--- |
| **Java 21** | Lenguaje y versión utilizada para compilar y ejecutar el backend |
| **JUnit 5** | Framework de pruebas automatizadas de la familia xUnit para Java |
| **Gradle** | Compilación, administración de dependencias y ejecución de pruebas |
| **Gradle Wrapper** | Garantiza la misma versión de Gradle en local y GitHub Actions |
| **HTML5** | Estructura del frontend |
| **CSS3** | Diseño visual y adaptación responsive |
| **JavaScript** | Interacción del frontend, persistencia local y conexión con el backend |
| **GitHub Actions** | Pipeline de Integración Continua y despliegue |
| **GitHub Pages** | Alojamiento gratuito del frontend estático |
| **Cloudflare Quick Tunnel** | Publicación temporal del backend local mediante HTTPS |
| **Git y GitHub** | Control de versiones, ramas, commits, Pull Requests y aprobación |
| **localStorage** | Persistencia local de pendientes, sesión simulada y URL del backend |
| **CORS** | Comunicación entre GitHub Pages y el backend publicado por Cloudflare |
| **GZIP** | Compresión de recursos HTML, CSS y JavaScript |

> Debido a que PendixAPP está desarrollado en Java, se utilizó **JUnit 5**, que representa el enfoque xUnit dentro del ecosistema Java.

---

# Suite de pruebas automatizadas

La suite se encuentra en:

```text
PendixAPP/src/test/java/
```

Clases de prueba agregadas y conservadas:

```text
HttpResultTest.java
PendienteTest.java
PendienteServiceTest.java
PendixRouterTest.java
GzipSupportTest.java
```

La suite verifica cinco componentes de producción, superando el requisito mínimo de tres clases.

| Clase probada | Responsabilidad | Motivo de selección |
| :--- | :--- | :--- |
| **Pendiente** | Representa un pendiente y administra su estado | Es una entidad principal del sistema y contiene reglas básicas que deben mantenerse estables |
| **PendienteService** | Registra, busca y completa pendientes | Contiene lógica de negocio y operaciones que afectan directamente el funcionamiento |
| **PendixRouter** | Selecciona respuestas según la ruta y el método HTTP | Es responsable de dirigir solicitudes, entregar recursos y responder endpoints |
| **HttpResult** | Encapsula estado HTTP, tipo de contenido y cuerpo | Permite comprobar que las respuestas generadas sean coherentes |
| **GzipSupport** | Detecta y genera respuestas comprimidas | Evita entregar recursos vacíos y comprueba la compatibilidad de compresión |

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
- Entrega los recursos HTML, CSS y JavaScript.
- Responde correctamente el endpoint /health.
- Responde correctamente el endpoint /version.
- Rechaza métodos HTTP que no están permitidos.
- Devuelve 404 cuando la ruta no existe.
```

### Pruebas de `HttpResult`

```text
- Conserva correctamente los datos de una respuesta HTTP.
- Rechaza códigos de estado fuera del rango válido.
```

### Pruebas de `GzipSupport`

```text
- Detecta cuando un cliente acepta compresión GZIP.
- Comprime y permite recuperar correctamente el contenido original.
```

Durante la validación final se ejecutaron **15 pruebas**, todas aprobadas.

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

Para generar el JAR:

```bash
./gradlew clean build
```

Archivo generado:

```text
PendixAPP/build/libs/PendixAPP-1.0.0.jar
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

El workflow final se encuentra en la raíz del repositorio:

```text
.github/workflows/pages.yml
```

Se mantiene en la raíz porque GitHub solamente detecta workflows almacenados dentro de:

```text
.github/workflows/
```

El pipeline está configurado para ejecutarse cuando ocurre:

```yaml
on:
  push:
    branches:
      - Entrega-Final

  pull_request:
    branches:
      - Entrega-Final

  workflow_dispatch:
```

Esto significa que cada cambio subido a `Entrega-Final`, cada Pull Request dirigido a esa rama y cada ejecución manual generan una validación automática.

La Integración Continua original se conserva, pero ahora forma parte de un flujo CI/CD completo:

```text
CI:
Compilar y ejecutar pruebas.

CD:
Preparar el frontend, solicitar autorización y publicar GitHub Pages.
```

---

## Proceso ejecutado por GitHub Actions

El pipeline realiza los siguientes pasos:

```text
1. Descarga el contenido del repositorio.
2. Configura Java 21.
3. Utiliza el Gradle Wrapper.
4. Da permisos de ejecución a gradlew.
5. Compila PendixAPP.
6. Ejecuta la suite de pruebas.
7. Prepara los archivos estáticos del frontend.
8. Crea el artefacto de GitHub Pages.
9. Espera la autorización configurada en el environment github-pages.
10. Publica el frontend después de la aprobación.
11. Marca la ejecución en verde si todo funciona.
12. Marca la ejecución en rojo si la compilación o una prueba falla.
```

Comando principal de validación:

```bash
./gradlew clean test --no-daemon
```

Flujo visual:

```text
Push o Pull Request
        ↓
Validate project
        ↓
Pruebas Gradle
        ↓
Preparar frontend
        ↓
Artefacto GitHub Pages
        ↓
Waiting for approval
        ↓
Approve and deploy
        ↓
Deploy to GitHub Pages
```

---

## Workflow utilizado

```yaml
name: PendixAPP - CI/CD GitHub Pages

on:
  push:
    branches:
      - Entrega-Final
  pull_request:
    branches:
      - Entrega-Final
  workflow_dispatch:

permissions:
  contents: read
  pages: write
  id-token: write

concurrency:
  group: github-pages
  cancel-in-progress: false

jobs:
  validate:
    name: Validate project
    runs-on: ubuntu-latest

    steps:
      - name: Descargar repositorio
        uses: actions/checkout@v4

      - name: Configurar Java 21
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: "21"
          cache: gradle

      - name: Dar permisos a Gradle Wrapper
        run: chmod +x PendixAPP/gradlew

      - name: Compilar y ejecutar pruebas
        working-directory: PendixAPP
        run: ./gradlew clean test --no-daemon

      - name: Preparar frontend
        if: github.event_name != 'pull_request'
        run: |
          rm -rf _site
          mkdir -p _site
          cp -R PendixAPP/src/main/resources/static/. _site/
          touch _site/.nojekyll

      - name: Configurar GitHub Pages
        if: github.event_name != 'pull_request'
        uses: actions/configure-pages@v5

      - name: Subir artefacto de GitHub Pages
        if: github.event_name != 'pull_request'
        uses: actions/upload-pages-artifact@v4
        with:
          path: _site

  deploy:
    name: Deploy to GitHub Pages
    needs: validate
    if: github.event_name != 'pull_request'
    runs-on: ubuntu-latest

    environment:
      name: github-pages
      url: ${{ steps.deployment.outputs.page_url }}

    steps:
      - name: Publicar frontend
        id: deployment
        uses: actions/deploy-pages@v4
```

La autorización se configura en:

```text
Settings
→ Environments
→ github-pages
→ Required reviewers
```

No se utiliza una contraseña compartida. Solo pueden autorizar los usuarios o equipos de GitHub seleccionados como revisores.

---

# Evidencia de pruebas locales

La siguiente captura debe mostrar el reporte generado por Gradle con las pruebas aprobadas.

Ruta recomendada:

```text
assets/01-pruebas-junit-aprobadas.png
```

![Reporte local de pruebas JUnit aprobadas](assets/01-pruebas-junit-aprobadas.png)

> La evidencia debe mostrar que no existen fallos y que la suite terminó correctamente.

En la rama final también se debe mostrar la ejecución de las 15 pruebas y el resultado:

```text
BUILD SUCCESSFUL
```

---

# Evidencia del pipeline CI

La siguiente captura debe mostrar la ejecución del workflow **PendixAPP - CI/CD GitHub Pages** dentro de GitHub Actions.

Ruta recomendada:

```text
assets/02-pipeline-github-actions-verde.png
```

![Pipeline de GitHub Actions ejecutado correctamente](assets/02-pipeline-github-actions-verde.png)

La captura debe permitir observar:

```text
- Nombre del workflow.
- Rama Entrega-Final.
- Trabajo Validate project.
- Trabajo Deploy to GitHub Pages.
- Configuración de Java.
- Compilación y ejecución de pruebas.
- Espera de autorización.
- Check verde después del despliegue.
```

---

# Evidencia del Pull Request

El Pull Request permite comprobar que los cambios pueden revisarse antes de integrarse a `Entrega-Final` y que el pipeline se ejecuta correctamente.

Ruta recomendada:

```text
assets/03-pull-request-check-verde.png
```

![Pull Request con el check verde](assets/03-pull-request-check-verde.png)

Dentro del Pull Request debe aparecer:

```text
All checks have passed
```

Después del merge, el trabajo de despliegue debe quedar esperando autorización si el environment `github-pages` tiene revisores obligatorios.

---

# ADR de pruebas automatizadas y CI

La decisión original de incorporar pruebas automatizadas y CI se conserva dentro de la arquitectura final.

Esta decisión establece:

```text
- Utilizar JUnit 5 para comprobar la lógica de Java.
- Utilizar Gradle Wrapper para reproducibilidad.
- Ejecutar pruebas en cada push y Pull Request.
- Bloquear el despliegue cuando una prueba falla.
- Conservar evidencia mediante GitHub Actions.
```

En `Entrega-Final`, la decisión se amplió para incluir:

```text
- Despliegue del frontend mediante GitHub Pages.
- Backend Java ejecutado desde la laptop.
- Publicación temporal mediante Cloudflare Quick Tunnel.
- Autorización manual antes del despliegue.
- Configuración CORS entre frontend y backend.
- Vista responsive.
- Endpoints de diagnóstico.
- Evaluación ATAM.
```

Toda la decisión arquitectónica final se documenta en este mismo README para evitar dividir la información en varios archivos.

---

# ADR final — Separación de frontend, backend y despliegue gratuito

## Estado

```text
Aceptado
```

## Contexto

PendixAPP necesitaba ser accesible desde internet sin utilizar AWS, tarjetas bancarias ni infraestructura de pago. También era necesario mostrar una separación entre frontend y backend, mantener pruebas automatizadas, utilizar un pipeline y permitir una autorización antes de publicar cambios.

GitHub Pages solo permite alojar contenido estático. Por ello, no puede ejecutar el backend Java. El backend debe permanecer en una computadora que tenga Java y acceso a internet.

## Decisión

Se decidió implementar la siguiente arquitectura:

```text
Usuario
   │
   ▼
GitHub Pages
Frontend HTML, CSS y JavaScript
   │
   │ HTTPS
   ▼
Cloudflare Quick Tunnel
   │
   │ HTTP local
   ▼
Laptop del desarrollador
Backend Java en 127.0.0.1:5018
```

Las decisiones específicas son:

```text
1. Publicar el frontend en GitHub Pages.
2. Ejecutar el backend Java en la laptop.
3. Utilizar Cloudflare Quick Tunnel para exponer el backend.
4. Usar el parámetro ?api= para configurar la URL del backend.
5. Guardar temporalmente la URL del backend en localStorage.
6. Habilitar CORS en el backend.
7. Ejecutar pruebas antes de preparar el despliegue.
8. Usar el environment github-pages para autorización.
9. Mantener el diseño responsive para teléfonos.
10. Utilizar serve.sh para iniciar y detener Java y Cloudflare.
```

## Alternativas consideradas

### AWS

Se descartó debido a la complejidad de configuración, posible requisito de tarjeta y riesgo de cargos.

### Publicar todo únicamente en GitHub Pages

Se descartó porque GitHub Pages no puede ejecutar Java.

### Abrir el puerto 5018 del router

Se descartó por seguridad, configuración de NAT y ausencia de HTTPS administrado.

### Mantener frontend y backend juntos detrás de Cloudflare

Funciona para una demostración, pero no muestra una separación de despliegue tan clara como GitHub Pages más backend local.

### Cloudflare Tunnel con nombre y dominio propio

Es una opción futura para tener una URL estable, pero requiere una cuenta de Cloudflare y un dominio administrado.

## Consecuencias positivas

```text
- El frontend tiene una URL pública permanente.
- El backend permanece bajo control del desarrollador.
- No se abren puertos del router.
- Cloudflare proporciona HTTPS.
- No se requiere AWS.
- El pipeline ejecuta pruebas antes de publicar.
- El despliegue puede esperar autorización.
- Se demuestra separación física entre frontend y backend.
- La solución es suficiente para una demostración académica.
```

## Consecuencias negativas

```text
- El backend solo funciona con la laptop encendida.
- La URL del Quick Tunnel cambia en cada reinicio.
- Cloudflare Quick Tunnel no ofrece SLA.
- El rendimiento depende de la conexión local.
- Una URL antigua provoca errores.
- Puede aparecer Cloudflare Error 1033.
- Los datos continúan principalmente en localStorage.
- Los datos no se comparten entre navegadores.
```

---

# Evaluación ATAM

La evaluación ATAM analiza las decisiones arquitectónicas de `Entrega-Final` con base en atributos de calidad, riesgos, trade-offs y puntos de sensibilidad.

## Atributos de calidad prioritarios

| Atributo | Objetivo |
| :--- | :--- |
| **Disponibilidad** | Mantener el frontend público y disponer del backend durante la demostración |
| **Desplegabilidad** | Publicar cambios después de validar pruebas y recibir autorización |
| **Modificabilidad** | Permitir cambios independientes en frontend, backend y pipeline |
| **Seguridad** | Evitar abrir puertos del router y utilizar HTTPS |
| **Usabilidad** | Permitir el uso desde computadora y teléfono |
| **Trazabilidad** | Registrar commits, pruebas, Pull Requests, aprobaciones y despliegues |
| **Operabilidad** | Iniciar, comprobar y detener el sistema mediante comandos claros |

## Riesgo arquitectónico R-01 — Dependencia de la laptop y Cloudflare

**Decisión real relacionada:**

```text
Ejecutar el backend Java en la laptop y publicarlo mediante Cloudflare Quick Tunnel.
```

**Descripción:**

Si la laptop se apaga, Java termina, la conexión de internet falla o `cloudflared` se desconecta, el backend deja de responder aunque GitHub Pages continúe mostrando la interfaz.

**Evidencia real:**

Durante las pruebas apareció **Cloudflare Error 1033**, que indica que Cloudflare no encontró una conexión activa del túnel.

**Impacto:**

```text
Alto durante una demostración.
```

**Mitigación:**

```text
- Encender Java y cloudflared antes de la presentación.
- Mantener las terminales abiertas.
- Comprobar http://127.0.0.1:5018/health.
- Comprobar la URL pública /health.
- Evitar cerrar el proceso con Ctrl+C.
```

## Riesgo arquitectónico R-02 — URL temporal del backend

**Decisión real relacionada:**

```text
Utilizar un Quick Tunnel sin cuenta y sin dominio permanente.
```

**Descripción:**

Cada reinicio de Cloudflare puede generar un subdominio diferente. Si GitHub Pages conserva una URL anterior, el frontend no podrá comunicarse con el backend.

**Mitigación:**

```text
Abrir nuevamente GitHub Pages usando:

?api=https://nueva-url.trycloudflare.com
```

## Trade-off T-01 — Costo y simplicidad frente a disponibilidad

**Decisión real relacionada:**

```text
Usar GitHub Pages + laptop + Cloudflare Quick Tunnel en lugar de AWS.
```

**Beneficio obtenido:**

```text
- Costo cero.
- Configuración sencilla.
- HTTPS.
- No se requiere abrir puertos.
- No se requiere tarjeta.
```

**Costo aceptado:**

```text
- Menor disponibilidad.
- Dependencia de la laptop.
- URL temporal.
- Ausencia de garantía de servicio.
```

**Justificación:**

Para una entrega académica y una demostración con pocos usuarios, se priorizó una solución gratuita y comprensible sobre una infraestructura permanente.

## Trade-off T-02 — Aprobación manual frente a rapidez de despliegue

**Decisión real relacionada:**

```text
Configurar Required reviewers en el environment github-pages.
```

**Beneficio obtenido:**

Evita que una versión se publique automáticamente sin revisión.

**Costo aceptado:**

El despliegue tarda más y depende de que un revisor autorizado apruebe.

## Punto de sensibilidad S-01 — URL base del backend

**Elemento sensible:**

```text
?api=https://subdominio.trycloudflare.com
```

**Decisión real relacionada:**

El frontend almacena dinámicamente la URL de Cloudflare para conectarse al backend.

**Sensibilidad:**

Un cambio mínimo, una URL vencida, un error de escritura o un túnel apagado rompe toda la comunicación entre GitHub Pages y Java.

**Atributos afectados:**

```text
Disponibilidad, funcionalidad y usabilidad.
```

**Control:**

```text
Validar siempre:

https://subdominio.trycloudflare.com/health
```

## Punto de sensibilidad S-02 — Configuración CORS

**Elemento sensible:**

```text
Access-Control-Allow-Origin
Access-Control-Allow-Methods
Access-Control-Allow-Headers
```

**Decisión real relacionada:**

Frontend y backend se ejecutan en dominios distintos.

**Sensibilidad:**

Una cabecera incorrecta puede provocar que el navegador bloquee todas las solicitudes, aunque el backend esté encendido.

## Resumen ATAM

| Clasificación | Elemento | Justificación |
| :--- | :--- | :--- |
| **Riesgo** | Dependencia de laptop y Cloudflare | El backend desaparece si Java o el túnel se detienen |
| **Trade-off** | Costo frente a disponibilidad | Se evitó AWS, pero se aceptó menor disponibilidad |
| **Punto de sensibilidad** | URL del backend | Una URL antigua o incorrecta rompe la integración |
| **Punto de sensibilidad** | CORS | Una configuración incorrecta bloquea las solicitudes |

## Conclusión ATAM

La arquitectura es adecuada para una entrega académica porque cumple con costo cero, separación frontend-backend, HTTPS, CI/CD, autorización y trazabilidad.

No es una arquitectura de producción porque el backend depende de la laptop, Quick Tunnel utiliza una URL temporal y los datos continúan principalmente en `localStorage`.

---

# Estructura principal de la rama

```text
Proyecto_ArqSoft/
│
├── .github/
│   └── workflows/
│       └── pages.yml
│
├── README.md
│
└── PendixAPP/
    ├── build.gradle
    ├── settings.gradle
    ├── gradlew
    ├── gradlew.bat
    ├── serve.sh
    │
    ├── gradle/
    │   └── wrapper/
    │       ├── gradle-wrapper.jar
    │       └── gradle-wrapper.properties
    │
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   ├── GzipSupport.java
    │   │   │   ├── HttpResult.java
    │   │   │   ├── Pendiente.java
    │   │   │   ├── PendienteService.java
    │   │   │   ├── PendixAppServer.java
    │   │   │   ├── PendixRouter.java
    │   │   │   └── StaticResourceService.java
    │   │   │
    │   │   └── resources/
    │   │       └── static/
    │   │           ├── index.html
    │   │           ├── styles.css
    │   │           └── app.js
    │   │
    │   └── test/
    │       └── java/
    │           ├── GzipSupportTest.java
    │           ├── HttpResultTest.java
    │           ├── PendienteTest.java
    │           ├── PendienteServiceTest.java
    │           └── PendixRouterTest.java
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
./gradlew clean build
java -jar build/libs/PendixAPP-1.0.0.jar
```

Después se abre en el navegador:

```text
http://127.0.0.1:5018
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
- Vista para computadora.
- Vista responsive para teléfono.
```

## Publicar backend y frontend juntos

Para encender Java y Cloudflare con un solo comando:

```bash
cd /home/avenkal/Descargas/Proyecto_ArqSoft_CI/PendixAPP
chmod +x serve.sh
./serve.sh
```

La terminal debe permanecer abierta.

El script muestra una URL similar a:

```text
https://nombre-temporal.trycloudflare.com
```

Para conectar GitHub Pages con esa URL:

```text
https://angel-lugo97.github.io/Proyecto_ArqSoft/?api=https://nombre-temporal.trycloudflare.com
```

## Ejecutar Java y Cloudflare por separado

Terminal 1:

```bash
cd /home/avenkal/Descargas/Proyecto_ArqSoft_CI/PendixAPP

./gradlew clean build
java -jar build/libs/PendixAPP-1.0.0.jar
```

Terminal 2:

```bash
cd /home/avenkal/Descargas/Proyecto_ArqSoft_CI/PendixAPP

cloudflared tunnel \
  --no-autoupdate \
  --protocol http2 \
  --edge-ip-version 4 \
  --url http://127.0.0.1:5018
```

Comprobación local:

```bash
curl -i http://127.0.0.1:5018/health
```

Comprobación pública:

```bash
curl -i https://nombre-temporal.trycloudflare.com/health
```

Para apagar ambos procesos:

```text
Ctrl+C
```

---

## Evidencia de ejecución funcional

![Captura de ejecución funcional de PendixAPP](Captura.png)

```text
URL local: http://127.0.0.1:5018
Frontend: https://angel-lugo97.github.io/Proyecto_ArqSoft/
Backend público: URL temporal de trycloudflare.com
Java: 21
Puerto: 5018
```

La evidencia final también debe mostrar:

```text
- La vista de computadora.
- La vista desde un teléfono.
- El endpoint /health.
- La URL pública de Cloudflare.
- GitHub Pages funcionando.
- El pipeline en verde.
- La espera de autorización.
```

---

# Historial de commits

La actividad se desarrolló inicialmente mediante commits separados para mostrar la evolución del trabajo. En la integración final también se incorporó un commit amplio para consolidar los cambios de despliegue.

Ejemplos de commits utilizados:

```text
docs: documentar clases probadas y estrategia de CI
ci: configurar pipeline de pruebas con GitHub Actions
test: agregar suite JUnit con Arrange Act Assert
refactor: preparar PendixAPP para pruebas automatizadas
fix: ubicar workflow en la raiz del repositorio
feat(project): add mobile interface and local Cloudflare deployment
ci: add Gradle build and test pipeline
feat(deploy): separate frontend and deploy with GitHub Pages
fix(deploy): keep Cloudflare tunnel alive
docs(readme): update final branch documentation
```

Para consultar el historial:

```bash
git log --oneline --decorate --graph -15
```

La separación de commits permite distinguir:

```text
- Preparación y refactorización del proyecto.
- Incorporación de pruebas.
- Configuración del pipeline.
- Adaptación responsive.
- Separación del frontend.
- Publicación mediante Cloudflare.
- Despliegue en GitHub Pages.
- Autorización del despliegue.
- Documentación arquitectónica.
```

---

# Proceso de trabajo con Git

Cambiar a la rama:

```bash
git switch Entrega-Final
```

Actualizar la rama:

```bash
git pull origin Entrega-Final
```

Ejecutar pruebas:

```bash
cd PendixAPP
./gradlew clean test build --no-daemon
```

Subir cambios:

```bash
cd ..
git add -A
git commit -m "docs(readme): update final project documentation"
git push origin Entrega-Final
```

Flujo recomendado para un colaborador:

```bash
git switch Entrega-Final
git pull origin Entrega-Final
git switch -c cambio-colaborador
```

Después:

```bash
git add -A
git commit -m "feat: update PendixAPP"
git push -u origin cambio-colaborador
```

Finalmente debe crear un Pull Request:

```text
cambio-colaborador → Entrega-Final
```

---

# Cómo revisar la rama en GitHub

```text
1. Entrar al repositorio Proyecto_ArqSoft.
2. Cambiar a la rama Entrega-Final.
3. Abrir README.md.
4. Revisar PendixAPP/src/test/java.
5. Confirmar que existen pruebas para al menos tres clases.
6. Revisar .github/workflows/pages.yml.
7. Entrar en la pestaña Actions.
8. Abrir PendixAPP - CI/CD GitHub Pages.
9. Confirmar que Validate project aparece en verde.
10. Confirmar que Deploy to GitHub Pages aparece como segundo trabajo.
11. Revisar la espera de autorización.
12. Aprobar desde Review deployments.
13. Confirmar el despliegue en GitHub Pages.
14. Abrir https://angel-lugo97.github.io/Proyecto_ArqSoft/.
15. Ejecutar el backend Java en la laptop.
16. Ejecutar Cloudflare Tunnel.
17. Conectar GitHub Pages mediante ?api=.
18. Probar la aplicación desde un teléfono.
19. Revisar el Pull Request.
20. Verificar que aparezca All checks have passed.
21. Revisar el historial de commits.
22. Revisar el ADR y la evaluación ATAM integrados en este README.
```

---

# Lista de verificación de la actividad

```text
[x] Se agregó una suite de pruebas automatizadas.
[x] Se utilizó JUnit 5 como framework xUnit para Java.
[x] Se probaron al menos tres clases del proyecto.
[x] Se probaron cinco componentes en total.
[x] Las pruebas siguen Arrange–Act–Assert.
[x] Las pruebas se ejecutan mediante Gradle.
[x] La compilación local termina en BUILD SUCCESSFUL.
[x] Se agregó el Gradle Wrapper.
[x] Se configuró Java 21.
[x] Se creó un workflow de GitHub Actions.
[x] El workflow se ejecuta en cada push.
[x] El workflow se ejecuta en cada Pull Request.
[x] El pipeline compila y ejecuta las pruebas.
[x] El frontend se separó en HTML, CSS y JavaScript.
[x] Se agregó una vista responsive.
[x] Se agregó el endpoint /health.
[x] Se agregó el endpoint /version.
[x] Se agregó compresión GZIP.
[x] Se agregó configuración CORS.
[x] Se creó serve.sh.
[x] El backend funciona en la laptop.
[x] El backend se publica mediante Cloudflare Quick Tunnel.
[x] El frontend se publicó en GitHub Pages.
[x] El pipeline prepara y despliega GitHub Pages.
[x] Se configuró un environment para autorización.
[x] Se documentó un ADR final.
[x] Se agregó una evaluación ATAM.
[x] ATAM contiene al menos un riesgo.
[x] ATAM contiene al menos un trade-off.
[x] ATAM contiene al menos un punto de sensibilidad.
[x] Los elementos ATAM están justificados con decisiones reales.
[x] Se conservó la cláusula de IA.
[ ] Agregar la captura del reporte local en assets.
[ ] Agregar la captura del pipeline verde en assets.
[ ] Agregar la captura del Pull Request en assets.
[ ] Agregar captura de Waiting for approval.
[ ] Agregar captura de GitHub Pages.
[ ] Agregar captura de Cloudflare funcionando.
[ ] Agregar captura de la vista móvil.
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
[ ] Implementar CRUD completo en el backend.
[ ] Agregar una base de datos central.
[ ] Sustituir localStorage como fuente principal de datos.
[ ] Implementar autenticación real.
[ ] Utilizar un Cloudflare Tunnel con nombre.
[ ] Configurar un dominio permanente.
[ ] Limitar CORS al dominio exacto de GitHub Pages.
[ ] Agregar logs persistentes.
[ ] Agregar monitoreo del backend.
[ ] Automatizar la actualización de la URL del backend.
```

---

# Resultado de esta rama

La rama **`Entrega-Final`** transforma PendixAPP en una solución demostrable mediante una arquitectura distribuida y gratuita.

La validación automatizada de la rama anterior se conserva:

```text
- Gradle compila el proyecto.
- JUnit ejecuta las pruebas.
- GitHub Actions valida cada cambio.
```

Además, se incorporó:

```text
- Frontend publicado en GitHub Pages.
- Backend Java ejecutado en la laptop.
- Cloudflare Tunnel para acceso HTTPS.
- Vista responsive para teléfonos.
- Endpoints de salud y versión.
- Compresión de recursos.
- Configuración CORS.
- Pipeline CI/CD.
- Autorización manual.
- ADR final.
- Evaluación ATAM.
```

El frontend queda disponible permanentemente en:

```text
https://angel-lugo97.github.io/Proyecto_ArqSoft/
```

El backend permanece disponible mientras Java y Cloudflare estén ejecutándose en la laptop.

---

# Conclusión

En esta rama se conservó y amplió la suite de pruebas automatizadas de PendixAPP. Las pruebas fueron desarrolladas con JUnit 5, siguen la estructura Arrange–Act–Assert y validan la entidad `Pendiente`, el servicio `PendienteService`, el router `PendixRouter`, las respuestas `HttpResult` y la compresión `GzipSupport`.

También se mantuvo Gradle con Java 21 para compilar el proyecto y ejecutar la suite de forma local y automática.

La arquitectura final separa el frontend del backend. El frontend se publica mediante GitHub Pages y el backend Java se ejecuta en la laptop del desarrollador. Cloudflare Quick Tunnel permite acceder temporalmente al backend desde internet mediante HTTPS.

Finalmente, se configuró un workflow de GitHub Actions que valida las pruebas, prepara el frontend, espera autorización y despliega GitHub Pages. La evaluación ATAM identifica riesgos, trade-offs y puntos de sensibilidad relacionados con decisiones reales de esta arquitectura.

Con estos cambios, PendixAPP cuenta con una base para validar futuras modificaciones, detectar regresiones, controlar despliegues y demostrar una arquitectura separada sin utilizar AWS.

---

## Cláusula de IA

```text
Yo, Angel Abraham Lugo Saenz, declaro que utilicé IA como apoyo para analizar la estructura de PendixAPP, organizar la suite de pruebas, configurar el pipeline de Integración Continua y redactar la documentación de esta rama.

El código, las decisiones del proyecto, la ejecución de las pruebas, la validación del pipeline y la adaptación final de la documentación fueron revisados como parte de la actividad escolar de Arquitectura de Software.
```

Para la rama `Entrega-Final`, la IA también se utilizó como apoyo para organizar la documentación relacionada con GitHub Pages, Cloudflare Tunnel, la separación entre frontend y backend, la adaptación móvil, el pipeline CI/CD, el proceso de autorización y la evaluación ATAM.

Las decisiones arquitectónicas finales, la ejecución de comandos, la verificación del backend, la publicación del frontend, las pruebas, la revisión de los resultados y la validación de la entrega fueron realizadas y supervisadas como parte de la actividad escolar de Arquitectura de Software.
