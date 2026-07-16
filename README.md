# PendixAPP - Rama deuda-tecnica

PendixAPP es una aplicación enfocada en la gestión de pendientes y recordatorios personales.  
El sistema permite registrar tareas, asignar fechas y horarios, consultar pendientes, marcar actividades como completadas y organizar recordatorios desde una interfaz web ejecutada localmente con Java.

En esta rama llamada **deuda-tecnica** se identifica y documenta la deuda técnica real encontrada en el proyecto, indicando de forma específica qué problema existe, por qué se originó, cuál sería el costo de no atenderlo y qué técnica de refactorización se propone para solucionarlo.

El objetivo de esta rama no es ocultar las decisiones tomadas para entregar rápidamente el prototipo, sino reconocerlas de manera profesional y establecer un plan de pago que permita que PendixAPP continúe creciendo sin acumular problemas difíciles de mantener.

La documentación se basa en el código actual del proyecto y se relaciona con el ADR anterior sobre los patrones GOF **Facade** y **Strategy**.

---

## Datos del Estudiante

| Campo | Información |
| :--- | :--- |
| **Nombre** | Angel Abraham Lugo Saenz |
| **Matrícula** | SW2409052 |
| **Materia** | Arquitectura de Software |
| **Profesor** | Jorge Javier Pedroza Romero |
| **Proyecto** | PendixAPP |
| **Tarea** | Identificación y documentación de deuda técnica |
| **Fecha** | 15/07/2026 |
| **Rama** | `deuda-tecnica` |
| **Estado** | Deudas identificadas y plan de solución documentado |

---

## Descripción General

PendixAPP es un prototipo funcional orientado a la gestión de pendientes personales.

Actualmente, el proyecto se ejecuta de forma local mediante un servidor en **Java**, utilizando `HttpServer`, la dirección `127.0.0.1` y el puerto `5018`. Desde el navegador se presenta una interfaz tipo celular donde el usuario puede administrar pendientes y revisar secciones como calendario, recordatorios, planes, ajustes e inicio de sesión simulado.

La información del prototipo se maneja principalmente desde el navegador mediante **JavaScript** y se conserva con **localStorage**. El servidor, las rutas HTTP, el HTML, los estilos CSS y la lógica JavaScript se encuentran concentrados en `PendixAppServer.java`.

Esta estructura permitió crear una demostración funcional en poco tiempo, pero también generó decisiones técnicas que deben documentarse antes de que el sistema continúe creciendo.

---

## Objetivo de esta Rama

El objetivo de la rama **deuda-tecnica** es identificar deuda técnica real dentro de PendixAPP y documentarla de forma profesional.

Por cada deuda se describen los siguientes puntos:

```text
- Qué es: descripción concreta del problema dentro del proyecto.
- Por qué existe: decisión consciente o descuido no detectado a tiempo.
- Costo de no pagarla: qué puede romperse o empeorar si continúa creciendo.
- Propuesta de solución: técnica de refactorización que permitiría pagarla.
```

Esta rama también busca:

```text
- Reconocer las decisiones temporales usadas para entregar el prototipo.
- Evitar que la configuración continúe escrita directamente en el código.
- Reducir la concentración de responsabilidades en PendixAppServer.java.
- Relacionar las soluciones con los patrones Facade y Strategy.
- Establecer criterios para saber cuándo una deuda puede considerarse pagada.
- Conservar la ejecución funcional y la documentación arquitectónica existente.
```

> Documentar una deuda no significa que ya fue eliminada. En esta rama se registra su existencia y se propone un plan concreto para atenderla.

---

## ¿Qué es la deuda técnica?

La deuda técnica puede entenderse como pedir prestado tiempo del futuro.

En un proyecto de software, una solución rápida permite entregar una funcionalidad hoy, pero puede generar un costo adicional cuando el sistema necesite modificarse, probarse, desplegarse o ampliarse.

No toda deuda técnica se origina por un error. En algunos casos se acepta de forma consciente para cumplir una fecha, preparar una demostración o mantener sencillo un primer prototipo.

El problema principal no es que exista deuda técnica, sino que el equipo no sepa que está presente, no conozca sus consecuencias o no tenga una propuesta para pagarla.

---

## Tipos de deuda identificados

| ID | Deuda técnica | Tipo | Origen | Prioridad |
| :--- | :--- | :--- | :--- | :--- |
| **DT-01** | Host, puerto y URL escritos directamente en el código | Configuración e infraestructura | Deliberada | Alta |
| **DT-02** | Servidor, rutas, interfaz y lógica concentrados en un solo archivo | Diseño y mantenibilidad | Accidental | Alta |

La **DT-01** cumple directamente con el requisito de documentar una deuda relacionada con configuración o infraestructura.

---

## Resumen de las deudas técnicas

| Elemento solicitado | DT-01: Configuración fija | DT-02: Responsabilidades concentradas |
| :--- | :--- | :--- |
| **Qué es** | El host `127.0.0.1`, el puerto `5018` y la URL de ejecución están escritos en `PendixAppServer.java` | `PendixAppServer.java` contiene el inicio del servidor, rutas, respuestas HTTP, HTML, CSS y JavaScript |
| **Por qué existe** | Se buscó que la demostración funcionara sin pedir configuración adicional | El prototipo comenzó pequeño y fue acumulando funciones dentro del mismo archivo |
| **Costo de no pagarla** | Cada ambiente o cambio de puerto exigiría modificar y recompilar el proyecto | El archivo será más difícil de entender, probar, modificar y mantener |
| **Solución propuesta** | Extraer una clase `AppConfig` y leer variables de entorno | Aplicar `Extract Class`, `Extract File`, `Move Method` y los patrones Facade y Strategy |

---

# DT-01 — Configuración escrita directamente en el código

## Qué es

En el archivo `PendixAPP/src/PendixAppServer.java`, el host y el puerto del servidor están definidos directamente como constantes:

```java
private static final int PORT = 5018;
private static final String HOST = "127.0.0.1";
```

La URL que se abre en el navegador también se construye con un valor fijo:

```java
String url = "http://localhost:" + PORT;
```

Esto significa que para utilizar otro puerto o dirección es necesario editar el código, volver a compilar las clases y generar nuevamente el archivo JAR.

## Por qué existe

Esta deuda fue una decisión deliberada para simplificar la ejecución de la demostración.

Definir un puerto fijo permitió que los scripts de Linux, macOS y Windows ejecutaran la aplicación sin solicitar parámetros adicionales. La solución fue útil para entregar rápidamente el prototipo, pero no se creó un mecanismo para adaptar la configuración a otros ambientes.

## Costo de no pagarla

Si esta deuda continúa, pueden presentarse los siguientes problemas:

```text
- La aplicación no podrá iniciar cuando el puerto 5018 esté ocupado.
- Cambiar el puerto exigirá modificar y recompilar el código.
- Será difícil ejecutar el proyecto en pruebas, contenedores o servidores.
- Los scripts no podrán reutilizarse con distintas configuraciones.
- Futuros datos sensibles podrían terminar escritos directamente en el código.
- Podrían existir diferencias entre el host configurado y la URL abierta.
```

## Propuesta de solución

Se propone aplicar las técnicas **Replace Hardcoded Value with Configuration** y **Extract Class**.

La configuración debe moverse a una clase independiente llamada `AppConfig`, que obtenga valores desde variables de entorno y conserve valores predeterminados para la ejecución local.

Variables propuestas:

```text
PENDIX_HOST
PENDIX_PORT
```

Ejemplo de ejecución en Linux:

```bash
PENDIX_HOST=127.0.0.1 PENDIX_PORT=8080 java -jar PendixApp.jar
```

Estructura propuesta:

```text
PendixAPP/
└── src/
    ├── PendixAppServer.java
    └── config/
        └── AppConfig.java
```

## Criterios para considerar pagada la DT-01

```text
[ ] El host y el puerto pueden cambiarse sin modificar PendixAppServer.java.
[ ] La aplicación conserva valores predeterminados para el ambiente local.
[ ] PENDIX_PORT se valida antes de iniciar el servidor.
[ ] El mismo host y puerto se usan para iniciar y abrir la aplicación.
[ ] El README explica cómo cambiar la configuración.
[ ] No se requiere recompilar el JAR para utilizar otro puerto.
```

---

# DT-02 — Responsabilidades concentradas en un solo archivo

## Qué es

El archivo `PendixAPP/src/PendixAppServer.java` concentra responsabilidades que deberían encontrarse separadas.

Actualmente contiene:

```text
- El método main que inicia el servidor.
- La configuración del host y el puerto.
- La apertura automática del navegador.
- El manejador de peticiones HTTP.
- La validación del método HTTP.
- La selección de rutas y códigos de respuesta.
- La ruta simulada /api/pendientes.
- La generación de respuestas HTTP.
- El documento HTML completo.
- Los estilos CSS de la interfaz.
- La lógica JavaScript de pendientes y filtros.
- El calendario, recordatorios, sesión y localStorage.
```

El archivo funciona, pero ha adquirido características de una **God Class**, porque conoce y controla demasiadas partes del sistema.

## Por qué existe

Esta deuda es principalmente accidental.

La primera versión se mantuvo en un solo archivo para facilitar la demostración y evitar una estructura difícil de ejecutar. Conforme se agregaron pendientes, filtros, calendario, recordatorios, ajustes, planes e inicio de sesión simulado, el archivo continuó creciendo y la concentración de responsabilidades no se corrigió a tiempo.

## Costo de no pagarla

Si esta deuda crece, pueden aparecer los siguientes problemas:

```text
- Será más difícil localizar y corregir errores.
- Un error en HTML o JavaScript podrá afectar la compilación de Java.
- Cada cambio visual obligará a recompilar el archivo JAR.
- El manejador HTTP crecerá con múltiples condiciones y rutas.
- Los filtros y operaciones serán difíciles de probar de forma aislada.
- La API simulada seguirá desconectada de los datos de localStorage.
- Varias personas tendrán conflictos al editar el mismo archivo.
- Facade y Strategy quedarían solo documentados y no implementados.
```

## Propuesta de solución

Se propone aplicar una refactorización incremental para evitar reemplazar todo el proyecto de una sola vez.

Las técnicas propuestas son:

```text
- Extract File.
- Extract Class.
- Move Method.
- Replace Conditional with Polymorphism.
- Separate Concerns.
```

La estructura objetivo sería:

```text
PendixAPP/
│
├── src/main/java/pendixapp/
│   ├── PendixAppServer.java
│   ├── config/
│   │   └── AppConfig.java
│   ├── controller/
│   │   └── PendientesHandler.java
│   ├── facade/
│   │   └── PendienteFacade.java
│   ├── model/
│   │   └── Pendiente.java
│   └── strategy/
│       ├── FiltroPendientesStrategy.java
│       ├── FiltroTodosStrategy.java
│       ├── FiltroActivosStrategy.java
│       ├── FiltroCompletadosStrategy.java
│       └── FiltroVencidosStrategy.java
│
└── src/main/resources/static/
    ├── index.html
    ├── css/
    │   └── styles.css
    └── js/
        └── app.js
```

## Relación con Facade

`PendienteFacade` centralizará las operaciones principales de los pendientes:

```text
- Obtener todos los pendientes.
- Obtener un pendiente por ID.
- Crear un pendiente.
- Actualizar un pendiente.
- Completar un pendiente.
- Eliminar un pendiente.
- Aplicar un filtro.
```

El manejador HTTP se limitará a recibir la petición, solicitar la operación a la fachada y construir la respuesta.

## Relación con Strategy

Las formas de filtrar pendientes se separarán en estrategias independientes:

```text
FiltroTodosStrategy
FiltroActivosStrategy
FiltroCompletadosStrategy
FiltroVencidosStrategy
```

Esto evitará agregar más bloques `if` o `switch` dentro del controlador o del archivo JavaScript.

## Criterios para considerar pagada la DT-02

```text
[ ] El HTML, CSS y JavaScript están en archivos independientes.
[ ] PendixAppServer solo configura e inicia el servidor.
[ ] Las rutas HTTP se encuentran en un manejador separado.
[ ] Las operaciones pasan por PendienteFacade.
[ ] Los filtros se implementan mediante estrategias independientes.
[ ] La API utiliza los datos reales de los pendientes.
[ ] La lógica puede probarse sin abrir el navegador.
```

---

## Orden propuesto para pagar la deuda

| Orden | Deuda | Motivo |
| :---: | :--- | :--- |
| **1** | DT-01: Configuración del servidor | Es un cambio de menor tamaño y prepara el proyecto para distintos ambientes |
| **2** | DT-02: Separación de responsabilidades | Requiere una migración gradual para conservar el funcionamiento actual |

El pago de la deuda debe realizarse en cambios pequeños, verificables y registrados en commits independientes.

---

## ADR de Deuda Técnica

La explicación completa de las deudas, sus consecuencias, alternativas y criterios de cierre se encuentra en:

[`ADR-08_Deuda_Tecnica_PendixApp.md`](ADR-08_Deuda_Tecnica_PendixApp.md)

| Documento | Descripción |
| :--- | :--- |
| **ADR-08** | Identifica dos deudas técnicas reales y propone un plan para pagarlas |
| **ADR-07** | Define el uso de los patrones GOF Facade y Strategy |
| **C4 PendixAPP** | Documenta el contexto, los contenedores y los componentes actuales |

> El ADR-08 registra la decisión arquitectónica. Este README presenta un resumen accesible de la rama y conserva la información necesaria para ejecutar y revisar el proyecto.

---

## Documentación C4 conservada

La documentación arquitectónica creada anteriormente se conserva porque ayuda a comprender dónde se encuentran las deudas técnicas dentro del sistema actual.

El archivo C4 se encuentra en:

[`PendixAPP/docs/C4_PendixAPP.md`](PendixAPP/docs/C4_PendixAPP.md)

| Nivel | Descripción |
| :--- | :--- |
| **C4 Nivel 1 — Contexto** | Muestra quién utiliza PendixAPP y cuál es el propósito general del sistema |
| **C4 Nivel 2 — Contenedores** | Explica la comunicación entre navegador, servidor Java, interfaz y localStorage |
| **C4 Nivel 3 — Componentes** | Expone la concentración actual de responsabilidades dentro del componente principal |

La DT-02 se detecta con mayor claridad en el Nivel 3, ya que `PendixAppServer`, `PendixHandler`, `html()`, las vistas y los gestores JavaScript dependen del mismo archivo principal.

---

## Archivos de la Rama

La rama **deuda-tecnica** conserva el prototipo funcional y agrega la documentación de deuda técnica.

Estructura principal esperada:

```text
Proyecto_ArqSoft/
│
├── README.md
├── ADR-08_Deuda_Tecnica_PendixApp.md
│
└── PendixAPP/
    ├── src/
    │   └── PendixAppServer.java
    ├── docs/
    │   └── C4_PendixAPP.md
    ├── PendixApp.jar
    ├── iniciar_linux.sh
    ├── iniciar_mac.command
    ├── INICIAR_WINDOWS.bat
    ├── compilar_y_ejecutar_linux.sh
    └── README_EJECUCION.md
```

Archivos principales de esta entrega:

```text
README.md
ADR-08_Deuda_Tecnica_PendixApp.md
PendixAPP/src/PendixAppServer.java
PendixAPP/docs/C4_PendixAPP.md
```

---

## Evidencia de Ejecución Funcional

La siguiente imagen muestra el prototipo **PendixAPP** ejecutándose correctamente desde el navegador mediante un servidor local en Java usando el puerto `5018`.

En la captura se observa la interfaz tipo celular, el tema oscuro con tonos púrpuras, los filtros con conteo integrado, la sección de notificaciones vencidas y la sección de planes e inicio de sesión.

```text
URL de ejecución: http://localhost:5018
Servidor: Java
Host actual: 127.0.0.1
Puerto actual: 5018
```

![Captura de ejecución funcional de PendixAPP](Captura.png)

> La captura de ejecución se conserva como evidencia de que el prototipo sigue funcionando. La DT-01 documenta precisamente que el host y el puerto mostrados todavía están escritos directamente en el código.

---

## Ejecución del Prototipo Java

Además de la documentación de deuda técnica, se mantiene una versión funcional del prototipo web de PendixAPP ejecutada con un servidor local en **Java**.

La finalidad de esta versión es comprobar que el sistema funciona antes de realizar las refactorizaciones propuestas. De esta manera, cada cambio futuro podrá compararse contra el comportamiento actual.

### Estructura de ejecución actual

```text
PendixAPP/
│
├── src/
│   └── PendixAppServer.java
│
├── PendixApp.jar
├── iniciar_linux.sh
├── iniciar_mac.command
├── INICIAR_WINDOWS.bat
├── compilar_y_ejecutar_linux.sh
└── README_EJECUCION.md
```

### Cómo ejecutarlo

En Linux:

```bash
cd PendixAPP
bash iniciar_linux.sh
```

También puede ejecutarse directamente con Java:

```bash
cd PendixAPP
java -jar PendixApp.jar
```

Después se abre en el navegador:

```text
http://localhost:5018
```

### Compilar y ejecutar en Linux

```bash
cd PendixAPP
bash compilar_y_ejecutar_linux.sh
```

### Qué muestra el prototipo

```text
- Interfaz tipo celular en el navegador.
- Gestión visual de pendientes.
- Filtros con conteo: Todos, Activos, Listos y Vencidos.
- Notificaciones vencidas.
- Sección de planes e inicio de sesión.
- Tema oscuro con colores púrpuras y violetas.
- Tareas completadas con opción de deshacer.
- Persistencia temporal mediante localStorage.
```

---

## Comprobación de las deudas en el código

Para localizar la configuración escrita directamente en el código:

```bash
grep -n "PORT\|HOST\|localhost" PendixAPP/src/PendixAppServer.java
```

Para localizar las responsabilidades concentradas dentro del mismo archivo:

```bash
grep -n "class PendixHandler\|String html\|/api/pendientes\|localStorage" PendixAPP/src/PendixAppServer.java
```

También puede revisarse el tamaño del archivo:

```bash
wc -l PendixAPP/src/PendixAppServer.java
```

Estos comandos no solucionan la deuda, pero permiten verificar que la documentación se basa en elementos reales del proyecto.

---

## Proceso de Trabajo en Git

La documentación de deuda técnica debe trabajarse en una rama independiente para que los cambios puedan revisarse sin afectar directamente la rama principal.

Ejemplo del proceso recomendado:

```bash
cd /home/avenkal/Descargas/Proyecto_ArqSoft

git switch main
git pull origin main
git switch -c deuda-tecnica

git add ADR-08_Deuda_Tecnica_PendixApp.md
git commit -m "docs: identificar deudas tecnicas de PendixAPP"

git add README.md
git commit -m "docs: enfocar README en deuda tecnica"

git push -u origin deuda-tecnica
```

Si la rama ya existe:

```bash
git switch deuda-tecnica
git pull origin deuda-tecnica
```

---

## Gestión con Git

Comandos básicos para trabajar sobre la rama **deuda-tecnica**:

```bash
cd /home/avenkal/Descargas/Proyecto_ArqSoft
git switch deuda-tecnica
git status
git add README.md ADR-08_Deuda_Tecnica_PendixApp.md
git commit -m "docs: actualizar deuda tecnica de PendixAPP"
git push origin deuda-tecnica
```

Para revisar los commits de la rama:

```bash
git log --oneline --decorate --graph -10
```

Para comprobar las diferencias con `main`:

```bash
git diff main..deuda-tecnica
```

> Antes de subir cambios, se recomienda ejecutar `git status` y confirmar que no se estén agregando archivos innecesarios, configuraciones personales de IntelliJ o archivos temporales.

---

## Cómo Visualizar la Rama en GitHub

Para revisar correctamente la entrega:

```text
1. Entrar al repositorio Proyecto_ArqSoft.
2. Cambiar la rama de main a deuda-tecnica.
3. Abrir el archivo README.md.
4. Abrir ADR-08_Deuda_Tecnica_PendixApp.md.
5. Verificar que se documenten al menos dos deudas técnicas.
6. Confirmar que una deuda esté relacionada con configuración o infraestructura.
7. Revisar que cada deuda incluya descripción, origen, costo y solución.
8. Confirmar que la evidencia de ejecución Captura.png siga visible.
9. Revisar el historial de commits de la rama.
10. Copiar el enlace de la rama o del repositorio para la entrega.
```

---

## Lista de verificación de la actividad

```text
[x] Se identificaron al menos dos deudas técnicas reales.
[x] Se explicó concretamente qué es cada deuda.
[x] Se indicó por qué existe cada deuda.
[x] Se describió el costo de no pagarla.
[x] Se propuso una técnica de refactorización.
[x] Una deuda está relacionada con configuración o infraestructura.
[x] Las deudas se basan en el código real de PendixAPP.
[x] Se agregó un ADR con la documentación completa.
[x] Se conservó la evidencia visual de ejecución.
[x] Se conservaron los comandos de ejecución y gestión con Git.
[x] Se mantuvo la documentación C4 como referencia arquitectónica.
```

---

## Mejoras Futuras

```text
[ ] Crear AppConfig para centralizar la configuración.
[ ] Leer PENDIX_HOST y PENDIX_PORT desde variables de entorno.
[ ] Validar que el puerto se encuentre entre 1 y 65535.
[ ] Separar index.html de PendixAppServer.java.
[ ] Separar los estilos en css/styles.css.
[ ] Separar la lógica del navegador en js/app.js.
[ ] Crear PendientesHandler para las rutas HTTP.
[ ] Implementar PendienteFacade para centralizar operaciones.
[ ] Implementar estrategias para todos, activos, completados y vencidos.
[ ] Conectar la interfaz con una API que administre datos reales.
[ ] Agregar pruebas unitarias para los filtros y operaciones.
[ ] Actualizar los diagramas C4 cuando cambie la estructura.
[ ] Registrar como pagada cada deuda únicamente cuando cumpla sus criterios.
```

---

## Resultado de esta Rama

La rama **deuda-tecnica** no elimina automáticamente todos los problemas identificados. Su resultado principal es hacer visible la deuda y convertirla en trabajo técnico concreto.

Antes de esta documentación, el host fijo, el puerto fijo y la concentración de responsabilidades podían considerarse solamente detalles de implementación. Después del análisis, cada punto cuenta con una causa, un costo, una prioridad, una solución propuesta y criterios para determinar cuándo puede considerarse resuelto.

Esto permite que las futuras refactorizaciones se realicen de forma controlada y sin perder el comportamiento funcional demostrado por el prototipo actual.

---

## Conclusión

En esta rama se identificaron y documentaron dos deudas técnicas reales de PendixAPP.

La primera deuda está relacionada con configuración e infraestructura, debido a que el host, el puerto y la URL de ejecución se encuentran escritos directamente en `PendixAppServer.java`.

La segunda deuda está relacionada con diseño y mantenibilidad, porque el servidor, las rutas HTTP, la interfaz HTML, los estilos CSS y la lógica JavaScript se encuentran concentrados en un solo archivo.

Para atenderlas se propusieron técnicas concretas de refactorización, como `Extract Class`, `Extract File`, `Move Method`, configuración mediante variables de entorno y separación de condicionales mediante Strategy. También se relacionó la solución con el patrón Facade definido anteriormente.

Con esta documentación, PendixAPP mantiene su evidencia de ejecución, sus instrucciones de uso y su documentación arquitectónica, pero ahora también reconoce los costos técnicos que deben controlarse para permitir el crecimiento ordenado del sistema.

---

## Cláusula de IA

```text
Yo, Angel Abraham Lugo Saenz, declaro que utilicé IA como apoyo para analizar la estructura actual de PendixAPP, organizar y redactar este README, así como para documentar las deudas técnicas y sus propuestas de refactorización.

El contenido principal, las decisiones del proyecto, la revisión del código y la adaptación final de la documentación fueron trabajados como parte de la actividad escolar de Arquitectura de Software.
```
