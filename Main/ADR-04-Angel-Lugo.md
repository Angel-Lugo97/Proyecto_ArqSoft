# ADR-01: [PendixApp]

| Campo  | Valor |
|--------|-------|
| Autor  | [Angel Abraham Lugo Saenz] |
| Fecha  | 6/05/2026 |
| Estado | `APROBADO` |

---

## Contexto

Estoy construyendo PendixApp,una aplicación móvil enfocada en la gestión de pendientes y recordatorios personales, el sistema permitirá que los usuarios registren tareas, asignen fechas y horarios, consulten sus actividades pendientes, marquen tareas como completadas y reciban notificaciones para recordar actividades importantes.

---

## Decisión
Decidí desarrollar Pendix como una aplicación para organizar pendientes y recordatorios, utilizando Java como lenguaje principal.

La aplicación se construirá inicialmente para Android, ya que Java permite desarrollar aplicaciones móviles nativas para este sistema operativo. La arquitectura será sencilla, separando la parte visual de la aplicación, la lógica del sistema y el almacenamiento de datos.

Java se usará para programar funciones como crear pendientes, editar tareas, eliminarlas, marcar actividades como completadas y consultar los pendientes agendados. Para guardar la información, se puede usar una base de datos local como SQLite o una solución en la nube como Firebase, dependiendo del alcance final del proyecto.

### ¿Por qué?

Pendix resuelve el problema de la falta de organización personal al permitir que el usuario registre sus pendientes en un solo lugar, ya que muchas veces las personas recuerdan una tarea en el momento, pero después la olvidan porque no la anotaron o porque la tienen dispersa en libretas, mensajes, notas del celular o simplemente en la memoria.

Otro problema que resuelve es la dificultad para dar seguimiento a las tareas, con Pendix, el usuario puede saber qué pendientes siguen activos, cuáles ya completó y cuáles están próximos a vencer, de esta manera se evita confusiones y ayuda a priorizar mejor las actividades.


## Alternativas consideradas

| Alternativa | Por qué la descarté |
|-------------|---------------------|
| Hacer la app solo para Android con Java | Es la opción más sencilla porque domino más Java, pero la descarté como única opción porque también me gustaría que en un futuro la app pueda funcionar en sistema iOS. |
| Hacer la app con React Native | La consideré porque permite crear una app para Android y iOS, pero la descarté por ahora porque tendría que aprender mejor JavaScript y React Native, y eso puede tomar más tiempo durante el cuatrimestre. |
| Crear una página web | La descarté porque Pendix está pensada para usarse como aplicación móvil, con recordatorios y pendientes desde el celular. Una página web sería más simple, pero no se sentiría igual que una app. |
| Usar Kotlin | La descarté porque, aunque sirve para crear apps Android, no es el lenguaje que más domino. Prefiero usar Java para avanzar más rápido y entregar una versión funcional. |
---

## Consecuencias

**Lo que gano:**

**Consecuencia técnica:**  
Al desarrollar la aplicación con **Java para Android**, se vuelve más fácil construir las funciones principales de Pendix, como crear pendientes, editarlos, eliminarlos, marcarlos como completados y asignarles fecha u hora. También puedo organizar el código usando clases, lo que ayuda a mantener separada la información de cada pendiente y la lógica de la aplicación.

**Consecuencia sobre el proceso o el equipo:**  
Como Java es el lenguaje que más domino, puedo avanzar más rápido y con menos errores durante el desarrollo. Esto ayuda a aprovechar mejor el tiempo del cuatrimestre, ya que me puedo enfocar en que la aplicación funcione bien en lugar de dedicar demasiado tiempo a aprender una tecnología nueva desde cero.

** Lo que sacrifico o asumo:**

**Limitación técnica:**  
Al desarrollar Pendix principalmente con **Java para Android**, la aplicación funcionará primero en dispositivos Android. Esto significa que no podré publicarla fácilmente en **iOS** sin hacer una versión diferente o adaptar el proyecto con otra tecnología.

**Deuda o riesgo:**  
Si la aplicación crece en el futuro, podría ser necesario cambiar o mejorar la forma en que se guardan los datos, agregar sincronización en la nube o crear una versión para iOS. Esto podría requerir más tiempo y posiblemente aprender nuevas tecnologías o modificar parte del código original.

## Diagrama

Un boceto de cómo se estructura tu sistema (draw.io, Mermaid o a mano escaneado)
![Diagrama C4 de PendixApp](C4_Angel_Lugo.png)

## Comentarios

Repositorio de GitHub: https://github.com/Angel-Lugo97/Proyecto_ArqSoft.git

## Clausula de IA
Yo Angel Abraham Lugo Saenz declaro que utilice IA en este caso utilice CHATGPT para ayudarme a pensar como hacer mis vistas arquitectonicas
