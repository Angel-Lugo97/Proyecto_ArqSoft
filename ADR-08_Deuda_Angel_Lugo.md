# ADR-08: Identificación y plan de pago de deuda técnica en PendixApp

| Campo           | Valor                                            |
| --------------- | ------------------------------------------------ |
| Autor           | Angel Abraham Lugo Saenz                         |
| Fecha           | 15/07/2026                                       |
| Estado          | `Aceptado`                                       |
| ADR relacionado | ADR-07: Integración de Patrones GOF en PendixApp |

---

## Contexto

PendixApp es un prototipo para gestionar pendientes y recordatorios personales. Actualmente, la aplicación se ejecuta mediante un servidor HTTP local desarrollado en Java, mientras que la interfaz completa se entrega al navegador desde el archivo `src/PendixAppServer.java`.

El proyecto ya permite agregar, modificar, completar, eliminar y filtrar pendientes, además de conservar información en el navegador mediante `localStorage`. También cuenta con una ruta `/api/pendientes`, aunque por ahora esta devuelve una respuesta simulada y todavía no administra los datos reales de la aplicación.

Durante el desarrollo se priorizó tener un prototipo funcional, fácil de ejecutar y listo para demostrar. Esta decisión permitió avanzar con rapidez, pero también generó deuda técnica que debe reconocerse antes de que el sistema continúe creciendo.

La deuda técnica no significa que el proyecto esté mal realizado, sino que algunas soluciones temporales o simplificadas tendrán un costo futuro si no se corrigen. Por ello, este ADR registra dos deudas técnicas reales encontradas en el código actual, explica su origen, el costo de mantenerlas y una propuesta concreta para resolverlas.

---

## Decisión

Se decidió registrar y priorizar las siguientes deudas técnicas:

| ID    | Deuda técnica                                                      | Tipo                                       | Prioridad |
| ----- | ------------------------------------------------------------------ | ------------------------------------------ | --------- |
| DT-01 | Host, puerto y URL escritos directamente en el código              | Configuración e infraestructura deliberada | Alta      |
| DT-02 | Servidor, rutas, interfaz y lógica concentrados en un solo archivo | Diseño accidental                          | Alta      |

Al menos una de las deudas está relacionada directamente con configuración e infraestructura, como solicita la actividad. Las soluciones propuestas buscan mejorar el proyecto de manera gradual, sin reemplazar toda la aplicación ni agregar una arquitectura demasiado compleja para su alcance escolar.

---

# DT-01: Configuración del servidor escrita directamente en el código

## Qué es

En `src/PendixAppServer.java`, la dirección y el puerto del servidor están definidos mediante constantes escritas directamente en el código:

```java
private static final int PORT = 5018;
private static final String HOST = "127.0.0.1";
```

Además, la URL que se muestra y se abre en el navegador se construye utilizando nuevamente un valor fijo:

```java
String url = "http://localhost:" + PORT;
```

Los scripts de Linux, macOS y Windows ejecutan directamente `PendixApp.jar`, pero tampoco permiten indicar el host o el puerto que se desea utilizar.

Esto significa que cualquier cambio de ambiente requiere modificar el archivo Java, volver a compilar el proyecto y generar otra vez el archivo JAR.

## Por qué existe

Esta deuda fue una decisión deliberada para simplificar la ejecución de la demostración. Usar siempre el puerto `5018` y la dirección local permitió que los scripts fueran sencillos y que el usuario pudiera abrir la aplicación sin configurar parámetros adicionales.

La solución fue útil para entregar rápidamente un prototipo funcional, pero no se preparó un mecanismo para cambiar los valores cuando la aplicación se ejecutara en otro equipo o ambiente.

## Costo de no pagarla

Si esta deuda crece, pueden presentarse los siguientes problemas:

* La aplicación no iniciará cuando el puerto `5018` ya esté ocupado.
* Para cambiar el puerto será necesario editar el código y volver a compilar el JAR.
* El proyecto será difícil de ejecutar en ambientes de pruebas, contenedores o servidores donde la dirección y el puerto sean diferentes.
* Los scripts de inicio no podrán reutilizarse con distintas configuraciones.
* Puede existir una diferencia entre el host donde escucha el servidor y la URL que se intenta abrir, porque se usa `127.0.0.1` para iniciar el servidor y `localhost` para construir la URL.
* Una futura configuración de base de datos, credenciales o servicios externos podría terminar escrita de la misma forma dentro del código.

El costo principal es que cada ambiente obligaría a generar una versión diferente de la aplicación, aumentando el mantenimiento y el riesgo de errores de configuración.

## Propuesta de solución

Se aplicará la técnica de refactorización **Replace Magic Number/Hardcoded Value with Configuration**, acompañada de **Extract Class**.

Se propone crear una clase `AppConfig` responsable de obtener la configuración desde variables de entorno o argumentos de ejecución. La aplicación conservará valores predeterminados para facilitar su uso local, pero permitirá reemplazarlos sin modificar el código.

Variables propuestas:

```text
PENDIX_HOST
PENDIX_PORT
```

Ejemplo de configuración:

```java
public final class AppConfig {
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 5018;

    private final String host;
    private final int port;

    public AppConfig() {
        host = System.getenv().getOrDefault("PENDIX_HOST", DEFAULT_HOST);
        port = obtenerPuerto(System.getenv("PENDIX_PORT"));
    }

    private int obtenerPuerto(String valor) {
        if (valor == null || valor.isBlank()) {
            return DEFAULT_PORT;
        }

        try {
            int puerto = Integer.parseInt(valor);

            if (puerto < 1 || puerto > 65535) {
                throw new IllegalArgumentException("Puerto fuera de rango");
            }

            return puerto;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "PENDIX_PORT debe ser numérico", e
            );
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUrl() {
        return "http://" + host + ":" + port;
    }
}
```

Después, `PendixAppServer` utilizará esa configuración para iniciar el servidor y construir la URL:

```java
AppConfig config = new AppConfig();

HttpServer server = HttpServer.create(
    new InetSocketAddress(config.getHost(), config.getPort()),
    0
);
```

Los scripts de ejecución también deberán aceptar las variables de entorno. Por ejemplo, en Linux:

```bash
PENDIX_HOST=127.0.0.1 PENDIX_PORT=8080 java -jar PendixApp.jar
```

## Criterios para considerar pagada la deuda

La deuda DT-01 se considerará pagada cuando:

* El host y el puerto no estén definidos como valores obligatorios dentro de `PendixAppServer.java`.
* La aplicación funcione con los valores predeterminados sin configuración adicional.
* Sea posible cambiar el puerto mediante `PENDIX_PORT` sin recompilar el proyecto.
* Se valide que el puerto sea numérico y se encuentre entre `1` y `65535`.
* La misma configuración se utilice para iniciar el servidor y construir la URL.
* El README documente cómo ejecutar la aplicación con valores personalizados.

---

# DT-02: Responsabilidades concentradas en `PendixAppServer.java`

## Qué es

El archivo `src/PendixAppServer.java` concentra responsabilidades que pertenecen a distintas partes del sistema. Actualmente contiene:

```text
- El método main que inicia el servidor.
- La configuración fija del host y el puerto.
- La lógica para abrir el navegador.
- El manejador de peticiones HTTP.
- La validación del método HTTP.
- La selección de rutas y códigos de respuesta.
- La ruta simulada /api/pendientes.
- La generación de respuestas HTTP.
- Todo el HTML de la interfaz.
- Todos los estilos CSS.
- Toda la lógica JavaScript de pendientes, filtros, calendario, sesión y localStorage.
```

La interfaz completa se encuentra dentro del método `html()` como un bloque de texto. Por ello, una modificación visual o una corrección en JavaScript obliga a editar un archivo Java y volver a compilar el servidor.

Además, la implementación actual todavía no materializa completamente la separación descrita en el ADR-07, donde se propuso utilizar `PendienteFacade` para centralizar operaciones y estrategias independientes para aplicar filtros.

## Por qué existe

Esta deuda es principalmente accidental. La primera versión se construyó en un solo archivo para facilitar la demostración y evitar una estructura difícil de ejecutar, pero con el crecimiento de las funciones no se detectó a tiempo que `PendixAppServer.java` estaba acumulando demasiadas responsabilidades.

Agregar pendientes, filtros, calendario, recordatorios, ajustes, inicio de sesión simulado y persistencia local hizo que el archivo dejara de ser únicamente el punto de entrada del servidor y se convirtiera en una clase con características de **God Class**.

## Costo de no pagarla

Si la deuda continúa creciendo, pueden aparecer los siguientes problemas:

* Será más difícil localizar y corregir errores porque muchas funciones se encuentran en el mismo archivo.
* Un error de sintaxis dentro del HTML, CSS o JavaScript incrustado puede impedir la compilación del código Java.
* Cada cambio de la interfaz requerirá recompilar y generar nuevamente el JAR.
* El manejador HTTP crecerá con más condiciones conforme se agreguen endpoints.
* Será difícil realizar pruebas unitarias sobre filtros, creación de pendientes o validaciones, porque la lógica no está separada.
* La ruta `/api/pendientes` puede seguir siendo una simulación desconectada de los datos guardados en `localStorage`.
* Agregar nuevos filtros aumentará los condicionales y funciones dentro del mismo bloque JavaScript.
* Varios integrantes podrían generar conflictos de Git al editar simultáneamente el mismo archivo.
* Los patrones Facade y Strategy quedarían únicamente documentados en el ADR, pero no reflejados completamente en la implementación.

El costo principal es que cada nueva funcionalidad exigirá más esfuerzo y tendrá mayor riesgo de afectar partes que ya funcionan.

## Propuesta de solución

La deuda se pagará mediante una refactorización incremental. No se propone reescribir toda la aplicación, sino separar responsabilidades en pasos controlados.

### 1. Aplicar Extract File y Extract Class

La interfaz se moverá a archivos independientes:

```text
src/main/resources/static/
├── index.html
├── css/
│   └── styles.css
└── js/
    └── app.js
```

El servidor dejará de construir la interfaz dentro del método `html()` y se encargará únicamente de leer y devolver los recursos estáticos.

### 2. Aplicar Extract Class y Move Method al servidor

Se propone separar el código Java en componentes con responsabilidades claras:

```text
src/main/java/pendixapp/
├── PendixAppServer.java
├── config/
│   └── AppConfig.java
├── controller/
│   └── PendientesHandler.java
├── facade/
│   └── PendienteFacade.java
├── model/
│   └── Pendiente.java
└── strategy/
    ├── FiltroPendientesStrategy.java
    ├── FiltroTodosStrategy.java
    ├── FiltroActivosStrategy.java
    ├── FiltroCompletadosStrategy.java
    └── FiltroVencidosStrategy.java
```

### 3. Implementar Facade según el ADR-07

`PendienteFacade` centralizará las operaciones principales:

```text
- obtenerPendientes()
- obtenerPendientePorId(id)
- crearPendiente(pendiente)
- actualizarPendiente(id, pendiente)
- completarPendiente(id)
- eliminarPendiente(id)
- filtrarPendientes(filtro)
```

El controlador HTTP solo deberá recibir la petición, validar los datos básicos, llamar a la fachada y construir la respuesta correspondiente.

### 4. Implementar Strategy según el ADR-07

Se aplicará la técnica **Replace Conditional with Polymorphism** para separar los filtros. Cada estrategia tendrá una sola responsabilidad y podrá probarse de forma independiente.

Ejemplo:

```java
public interface FiltroPendientesStrategy {
    List<Pendiente> filtrar(List<Pendiente> pendientes);
}
```

```java
public class FiltroCompletadosStrategy
        implements FiltroPendientesStrategy {

    @Override
    public List<Pendiente> filtrar(
            List<Pendiente> pendientes) {

        return pendientes.stream()
                .filter(Pendiente::isCompletado)
                .toList();
    }
}
```

### 5. Definir una sola fuente de datos

La API y la interfaz no deben manejar listas independientes. Como paso inicial, `PendienteFacade` puede trabajar con almacenamiento en memoria. Después podrá reemplazarse por persistencia local o una base de datos sin modificar el controlador.

La interfaz deberá consumir los endpoints de la API mediante `fetch`, en lugar de administrar por separado todos los pendientes únicamente en `localStorage`.

## Criterios para considerar pagada la deuda

La deuda DT-02 se considerará pagada cuando:

* El HTML, CSS y JavaScript ya no estén incrustados dentro de `PendixAppServer.java`.
* `PendixAppServer` se limite a configurar e iniciar el servidor.
* Las rutas HTTP se encuentren en un manejador o controlador separado.
* Las operaciones de pendientes se realicen mediante `PendienteFacade`.
* Los filtros activos, completados y vencidos se implementen mediante estrategias independientes.
* La API entregue y modifique los datos reales de los pendientes, no una respuesta simulada.
* Sea posible probar la lógica de filtros sin iniciar el navegador ni el servidor completo.

---

## Orden de atención

| Orden | Deuda                                          | Motivo                                                                                          |
| ----- | ---------------------------------------------- | ----------------------------------------------------------------------------------------------- |
| 1     | DT-01: Configuración del servidor              | Tiene menor esfuerzo de corrección y prepara el proyecto para ejecutarse en distintos ambientes |
| 2     | DT-02: Archivo con múltiples responsabilidades | Requiere una refactorización gradual y permitirá implementar correctamente Facade y Strategy    |

La DT-01 debe resolverse primero porque crea una base de configuración reutilizable. Después, la DT-02 puede pagarse por etapas para evitar romper las funciones que actualmente ya trabajan.

---

## Alternativas consideradas

| Alternativa                                                           | Motivo por el que se descartó                                                                          |
| --------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------ |
| Mantener todos los valores escritos directamente en el código         | Continuaría obligando a modificar y recompilar la aplicación para cada ambiente                        |
| Crear un JAR diferente para cada puerto                               | Generaría múltiples versiones del mismo programa y aumentaría el mantenimiento                         |
| Seguir agregando funciones dentro de `PendixAppServer.java`           | Incrementaría el tamaño de la clase y el riesgo de errores                                             |
| Reescribir inmediatamente todo el proyecto con un framework diferente | Sería una solución demasiado grande para el alcance actual y podría eliminar funciones que ya trabajan |
| Documentar la deuda sin asignar criterios de cierre                   | No permitiría saber cuándo la deuda fue realmente pagada                                               |

---

## Consecuencias

### Lo que se gana

* La configuración podrá cambiarse sin modificar ni recompilar el código.
* El servidor, la interfaz y la lógica tendrán responsabilidades más claras.
* Será más fácil agregar endpoints y filtros.
* Facade y Strategy dejarán de ser solamente una propuesta y quedarán reflejados en el código.
* Las pruebas podrán enfocarse en componentes pequeños.
* Se reducirá el riesgo de que una modificación visual afecte el servidor.
* El proyecto estará mejor preparado para incorporar persistencia real en el futuro.

### Lo que se sacrifica o asume

* Se agregarán más carpetas, archivos y clases.
* Será necesario ajustar los scripts de compilación para incluir recursos estáticos.
* Durante la separación deberán realizarse pruebas para verificar que las funciones actuales no cambien.
* La migración desde `localStorage` hacia una API real tendrá que realizarse de forma gradual.
* El equipo deberá respetar la nueva separación para evitar volver a concentrar responsabilidades.

---

## Relación con el ADR-07

El ADR-07 decidió integrar los patrones GOF **Facade** y **Strategy**. El análisis de deuda técnica muestra que la implementación actual todavía concentra las operaciones, los filtros y la interfaz en un solo archivo.

Por ello, pagar la DT-02 permitirá aplicar de forma real las decisiones del ADR-07:

```text
PendientesHandler
        ↓
PendienteFacade
        ↓
FiltroPendientesStrategy
        ↓
FiltroTodosStrategy
FiltroActivosStrategy
FiltroCompletadosStrategy
FiltroVencidosStrategy
```

De esta manera, la deuda técnica no se documenta de forma aislada, sino que su solución continúa la evolución arquitectónica que ya había sido definida para PendixApp.

---

## Decisión final

Se reconoce que PendixApp tiene dos deudas técnicas reales que deben controlarse antes de continuar agregando funciones.

La primera corresponde a la configuración e infraestructura, debido a que el host, el puerto y la URL se encuentran escritos directamente en el código. Se propone extraer esos valores a una clase de configuración y permitir su definición mediante variables de entorno.

La segunda corresponde al diseño interno, porque `PendixAppServer.java` concentra el inicio del servidor, las rutas HTTP, la interfaz, los estilos y la lógica JavaScript. Se propone separar los recursos, extraer clases y aplicar los patrones Facade y Strategy definidos en el ADR-07.

Ambas deudas surgieron por priorizar una entrega funcional y sencilla. Documentarlas permite conocer su existencia, medir sus consecuencias y establecer una forma concreta de pagarlas sin rehacer completamente el proyecto.

---

## Cláusula de IA

Yo, Angel Abraham Lugo Saenz, declaro que utilicé inteligencia artificial como apoyo para analizar la estructura actual de PendixApp, organizar la documentación de la deuda técnica y redactar las propuestas de refactorización.

La revisión del código, las decisiones del proyecto, la selección de las deudas y la adaptación final del contenido fueron realizadas como parte de la actividad escolar de Arquitectura de Software.
