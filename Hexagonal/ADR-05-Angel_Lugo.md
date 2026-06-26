# ADR-01: [PendixApp]

| Campo  | Valor                      |
| ------ | -------------------------- |
| Autor  | [Angel Abraham Lugo Saenz] |
| Fecha  | 12/06/2026                 |
| Estado | `APROBADO`                 |

---

## Contexto

Estoy construyendo PendixApp,una aplicación móvil enfocada en la gestión de pendientes y recordatorios personales, el sistema permitirá que los usuarios registren tareas, asignen fechas y horarios, consulten sus actividades pendientes, marquen tareas como completadas y reciban notificaciones para recordar actividades importantes.

---

## Decisión

Decidí desarrollar Pendix como una aplicación para organizar pendientes y recordatorios, utilizando Java como lenguaje principal.

La aplicación se construirá inicialmente para Android, ya que Java permite desarrollar aplicaciones móviles nativas para este sistema operativo. La arquitectura será sencilla, separando la parte visual de la aplicación, la lógica del sistema y el almacenamiento de datos.

Java se usará para programar funciones como crear pendientes, editar tareas, eliminarlas, marcar actividades como completadas y consultar los pendientes agendados. Para guardar la información, se puede usar una base de datos local como SQLite o una solución en la nube como Firebase, dependiendo del alcance final del proyecto.

Como estilo arquitectónico principal, decidí utilizar una **arquitectura en capas**, ya que PendixApp necesita separar claramente la interfaz de usuario, la lógica del sistema, el almacenamiento de datos y los servicios de notificaciones. Esta decisión permite que cada parte de la aplicación tenga una responsabilidad específica y evita que todo el código quede mezclado en una sola parte del proyecto.

La aplicación se organizará principalmente en una capa de presentación, una capa de lógica de negocio, una capa de datos y una capa de servicios locales. La capa de presentación se encargará de las pantallas y formularios que utiliza el usuario, la capa de lógica de negocio manejará las reglas relacionadas con los pendientes y recordatorios, la capa de datos administrará la información almacenada en SQLite y la capa de servicios locales se encargará de las notificaciones del sistema Android.

### ¿Por qué?

Pendix resuelve el problema de la falta de organización personal al permitir que el usuario registre sus pendientes en un solo lugar, ya que muchas veces las personas recuerdan una tarea en el momento, pero después la olvidan porque no la anotaron o porque la tienen dispersa en libretas, mensajes, notas del celular o simplemente en la memoria.

Otro problema que resuelve es la dificultad para dar seguimiento a las tareas, con Pendix, el usuario puede saber qué pendientes siguen activos, cuáles ya completó y cuáles están próximos a vencer, de esta manera se evita confusiones y ayuda a priorizar mejor las actividades.

La arquitectura en capas resuelve mejor el problema del proyecto porque permite organizar el sistema de una forma clara y fácil de mantener. PendixApp necesita manejar tareas, fechas, horarios, estados de cumplimiento y recordatorios, por lo que separar la interfaz, la lógica y los datos ayuda a que el código sea más entendible y más sencillo de modificar.

Además, este estilo arquitectónico se adapta al alcance actual del proyecto, ya que la primera versión de PendixApp funcionará principalmente en Android, usando almacenamiento local y notificaciones del sistema. No se necesita una arquitectura demasiado compleja como microservicios o serverless, porque el objetivo principal es construir una aplicación funcional, ordenada y mantenible.

## Alternativas consideradas

| Alternativa                             | Por qué la descarté                                                                                                                                                                                         |
| --------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Hacer la app solo para Android con Java | Es la opción más sencilla porque domino más Java, pero la descarté como única opción porque también me gustaría que en un futuro la app pueda funcionar en sistema iOS.                                     |
| Hacer la app con React Native           | La consideré porque permite crear una app para Android y iOS, pero la descarté por ahora porque tendría que aprender mejor JavaScript y React Native, y eso puede tomar más tiempo durante el cuatrimestre. |
| Crear una página web                    | La descarté porque Pendix está pensada para usarse como aplicación móvil, con recordatorios y pendientes desde el celular. Una página web sería más simple, pero no se sentiría igual que una app.          |
| Usar Kotlin                             | La descarté porque, aunque sirve para crear apps Android, no es el lenguaje que más domino. Prefiero usar Java para avanzar más rápido y entregar una versión funcional.                                    |
| Arquitectura cliente-servidor           | La descarté como estilo principal porque la primera versión de PendixApp no requiere un servidor externo. La aplicación puede funcionar localmente utilizando SQLite y los servicios de Android.            |
| Arquitectura de microservicios          | La descarté porque sería demasiado compleja para el tamaño actual del proyecto y requeriría dividir el sistema en varios servicios que no son necesarios para una app de pendientes personal.               |
| Arquitectura serverless                 | La descarté porque PendixApp no necesita ejecutar funciones en la nube para cumplir sus funciones principales de registrar, consultar y recordar pendientes.                                                |
| Arquitectura orientada a eventos        | La consideré por el uso de notificaciones, pero la descarté como estilo principal porque la mayor parte del sistema depende de acciones directas del usuario, como crear, editar o completar pendientes.    |
| Arquitectura hexagonal                  | La descarté porque ofrece un nivel de abstracción mayor al que necesita el proyecto actualmente y aumentaría la complejidad del desarrollo durante el cuatrimestre.                                         |

---

## Consecuencias

**Lo que gano:**

**Consecuencia técnica:**
Al desarrollar la aplicación con **Java para Android**, se vuelve más fácil construir las funciones principales de Pendix, como crear pendientes, editarlos, eliminarlos, marcarlos como completados y asignarles fecha u hora. También puedo organizar el código usando clases, lo que ayuda a mantener separada la información de cada pendiente y la lógica de la aplicación.

Al utilizar una **arquitectura en capas**, el sistema queda mejor organizado porque se separa la interfaz, la lógica del sistema, el almacenamiento de datos y las notificaciones. Esto facilita modificar una parte de la aplicación sin afectar directamente a las demás. Por ejemplo, si más adelante se cambia SQLite por Firebase, ese cambio se podría trabajar principalmente en la capa de datos.

**Consecuencia sobre el proceso o el equipo:**
Como Java es el lenguaje que más domino, puedo avanzar más rápido y con menos errores durante el desarrollo. Esto ayuda a aprovechar mejor el tiempo del cuatrimestre, ya que me puedo enfocar en que la aplicación funcione bien en lugar de dedicar demasiado tiempo a aprender una tecnología nueva desde cero.

La arquitectura en capas también ayuda a trabajar de forma más ordenada, porque permite identificar mejor dónde debe ir cada parte del código. Esto hace que el desarrollo sea más claro y evita que todas las funciones queden mezcladas en una sola clase o pantalla.

** Lo que sacrifico o asumo:**

**Limitación técnica:**
Al desarrollar Pendix principalmente con **Java para Android**, la aplicación funcionará primero en dispositivos Android. Esto significa que no podré publicarla fácilmente en **iOS** sin hacer una versión diferente o adaptar el proyecto con otra tecnología.

Al usar una arquitectura en capas, asumo que el proyecto tendrá una estructura sencilla y adecuada para su alcance actual, pero no tan flexible como otras arquitecturas más avanzadas. Si PendixApp crece demasiado o necesita funcionar en varias plataformas con usuarios conectados, podría ser necesario complementar esta arquitectura con otros componentes.

**Deuda o riesgo:**
Si la aplicación crece en el futuro, podría ser necesario cambiar o mejorar la forma en que se guardan los datos, agregar sincronización en la nube o crear una versión para iOS. Esto podría requerir más tiempo y posiblemente aprender nuevas tecnologías o modificar parte del código original.

También existe el riesgo de que, si no se respeta bien la separación por capas durante el desarrollo, el código vuelva a mezclarse y se pierda parte del beneficio de esta arquitectura. Por eso será importante mantener separadas las responsabilidades de la interfaz, la lógica, los datos y las notificaciones.

## Vistas arquitectonicas

Se eligieron estas vistas arquitectónicas porque permiten documentar PendixApp desde diferentes perspectivas, mostrando no solo qué hará la aplicación, sino también cómo estará organizada, dónde se ejecutará y cómo funcionarán sus procesos principales.

La **vista lógica** se eligió porque ayuda a representar las funciones principales del sistema. En el caso de PendixApp, permite mostrar cómo el usuario interactúa con la aplicación para crear pendientes, editarlos, eliminarlos, consultarlos, marcarlos como completados y programar recordatorios. Esta vista es importante porque explica la estructura funcional del sistema sin enfocarse todavía en archivos o código.

La **vista física** se eligió porque permite mostrar cómo estará organizado el proyecto dentro de Android Studio. Esta vista ayuda a visualizar la separación entre pantallas, modelos, base de datos, adaptadores y clases de notificaciones. Esto es importante porque PendixApp no debe quedar como un proyecto donde todo el código esté mezclado en una sola clase, sino como una aplicación organizada y fácil de mantener.

La **vista de despliegue** se eligió porque muestra dónde se ejecutará la aplicación y qué elementos necesita para funcionar. En este proyecto, PendixApp se ejecutará directamente en un dispositivo Android, usará SQLite como base de datos local y utilizará el sistema de notificaciones de Android. Esta vista también ayuda a dejar claro que el sistema no depende de servidores externos ni de servicios en la nube para su primera versión.

La **vista de procesos** se eligió porque permite representar el flujo principal de uso de la aplicación. Por ejemplo, cuando el usuario registra un pendiente, la aplicación recibe los datos, los valida, los guarda en SQLite y programa una notificación local. Esta vista es importante porque muestra el comportamiento del sistema paso a paso.

![Diagrama Mermaid de vistas arquitectónicas](mermaid-diagram-1780715733924.png)

## Diagrama

Un boceto de cómo se estructura tu sistema (draw.io, Mermaid o a mano escaneado)
![Diagrama C4 de PendixApp](C4_Angel_Lugo.png)

## Comentarios

Repositorio de GitHub: https://github.com/Angel-Lugo97/Proyecto_ArqSoft.git

## Clausula de IA

Yo Angel Abraham Lugo Saenz declaro que utilice IA en este caso utilice CHATGPT para ayudarme a pensar como hacer mis vistas arquitectonicas y para documentar la decisión del estilo arquitectónico de PendixApp.
