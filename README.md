# PendixApp - Vistas Arquitectónicas

PendixApp es una aplicación móvil enfocada en la gestión de pendientes y recordatorios personales.  
El objetivo del sistema es permitir que el usuario registre tareas, asigne fechas y horarios, consulte sus pendientes, marque actividades como completadas y reciba recordatorios desde un dispositivo Android.

En esta rama se documentan las **vistas arquitectónicas de PendixApp**, agregando una explicación más completa sobre cómo está organizado el sistema, cómo funciona su flujo principal y cómo se relacionan sus componentes internos.

---

## Datos del Estudiante

| Campo | Información |
| :--- | :--- |
| **Nombre** | Angel Abraham Lugo Saenz |
| **Matrícula** | SW2409052 |
| **Materia** | Arquitectura de Software |
| **Profesor** | Jorge Javier Pedroza Romero |
| **Proyecto** | PendixApp |
| **Tarea** | Vistas arquitectónicas y diagramas del sistema |
| **Fecha** | 15/05/2026 |
| **Estado** | APROBADO |

---

## Descripción General

PendixApp es una aplicación pensada para ayudar a organizar tareas personales desde el celular.

El sistema permite registrar pendientes en un solo lugar, consultar actividades pendientes, editar información, eliminar tareas, marcar actividades como completadas y programar recordatorios para evitar que el usuario olvide actividades importantes.

La aplicación se plantea inicialmente para Android, utilizando **Java** como lenguaje principal. Para el almacenamiento de información se considera **SQLite** como base de datos local, y en una mejora futura se contempla el uso de **Firebase** para sincronización en la nube.

---

## Objetivo de esta Rama

El objetivo de esta rama es documentar las vistas arquitectónicas de PendixApp.

Estas vistas ayudan a explicar el sistema desde diferentes perspectivas:

```text
- Qué funciones tendrá PendixApp.
- Cómo se organiza internamente.
- Dónde se ejecuta la aplicación.
- Cómo fluye el proceso principal al registrar un pendiente.
- Cómo se relaciona la app con SQLite y las notificaciones de Android.
```

Esto permite entender mejor la arquitectura del proyecto, no solo desde la idea general, sino también desde su estructura y comportamiento.

---

## Archivos de la Rama

En esta rama deben aparecer únicamente los archivos relacionados con las vistas arquitectónicas:

```text
Proyecto_ArqSoft/
│
├── ADR-04-Angel-Lugo.md
├── C4_Angel_Lugo.png
├── mermaid-diagram-1780715733924.png
└── README.md
```

---

## Tecnologías y Elementos Utilizados

| Elemento | Uso dentro del proyecto |
| :--- | :--- |
| **Java** | Lenguaje principal para la aplicación Android |
| **Android SDK** | Herramientas para construir la app móvil |
| **SQLite** | Base de datos local para guardar pendientes |
| **Firebase** | Posible servicio futuro para sincronización en la nube |
| **Notificaciones Android** | Recordatorios locales para los pendientes |
| **Markdown** | Documentación del ADR y README |
| **Mermaid** | Representación del flujo del proceso |
| **C4 Model** | Representación del contexto y contenedores del sistema |
| **Git y GitHub** | Control de versiones y entrega del proyecto |

---

## Decisión Principal del ADR

La decisión principal documentada en el ADR es desarrollar PendixApp como una aplicación móvil Android utilizando **Java**.

Esta decisión se tomó porque Java es el lenguaje que más se domina para el desarrollo del proyecto, lo que permite avanzar más rápido y con menos errores durante el cuatrimestre.

La aplicación se organiza de manera sencilla, separando la interfaz de usuario, la lógica de pendientes, el almacenamiento local y el manejo de recordatorios.

---

## Funcionalidades Principales de PendixApp

PendixApp está pensada para incluir las siguientes funciones:

```text
- Crear pendientes.
- Editar pendientes.
- Eliminar pendientes.
- Consultar pendientes registrados.
- Marcar pendientes como completados.
- Asignar fecha y hora a las tareas.
- Programar recordatorios locales.
- Guardar información en SQLite.
```

---

## Vistas Arquitectónicas

Se agregaron vistas arquitectónicas para documentar PendixApp desde diferentes perspectivas.  
Estas vistas permiten explicar no solo qué hará la aplicación, sino también cómo estará organizada, dónde se ejecutará y cómo funcionarán sus procesos principales.

---

## Vista Lógica

La vista lógica permite representar las funciones principales del sistema.

En PendixApp, esta vista ayuda a mostrar cómo el usuario interactúa con la aplicación para:

```text
- Crear pendientes.
- Editar tareas.
- Eliminar pendientes.
- Consultar actividades.
- Marcar tareas como completadas.
- Programar recordatorios.
```

Esta vista es importante porque explica la estructura funcional del sistema sin enfocarse todavía en archivos, código o dispositivos físicos.

---

## Vista Física

La vista física permite mostrar cómo podría organizarse el proyecto dentro de Android Studio.

Esta vista ayuda a visualizar la separación entre pantallas, modelos, base de datos, adaptadores y clases de notificaciones.

La intención es evitar que PendixApp quede como un proyecto desordenado, donde todo el código esté mezclado en una sola clase. Por eso, se propone una organización más clara y fácil de mantener.

Ejemplo de organización:

```text
PendixApp/
│
├── app/
│   ├── activities/
│   │   └── MainActivity.java
│   │
│   ├── models/
│   │   └── Pendiente.java
│   │
│   ├── database/
│   │   └── SQLiteHelper.java
│   │
│   ├── adapters/
│   │   └── PendienteAdapter.java
│   │
│   └── notifications/
│       └── NotificationHelper.java
```

---

## Vista de Despliegue

La vista de despliegue muestra dónde se ejecutará PendixApp y qué elementos necesita para funcionar.

En la primera versión, la aplicación se ejecutará directamente en un dispositivo Android.  
Usará SQLite como almacenamiento local y el sistema de notificaciones del dispositivo para mostrar recordatorios.

Esto permite que PendixApp funcione sin depender inicialmente de servidores externos o servicios en la nube.

```text
Dispositivo Android
│
├── PendixApp
├── SQLite local
└── Sistema de notificaciones Android
```

Firebase queda considerado como una mejora futura para sincronizar información en la nube.

---

## Vista de Procesos

La vista de procesos representa el flujo principal de uso de la aplicación.

En este caso, se muestra el proceso cuando el usuario registra un nuevo pendiente:

```text
1. El usuario abre PendixApp.
2. Escribe los datos del pendiente.
3. Selecciona fecha y hora.
4. La interfaz envía los datos a la lógica de pendientes.
5. La lógica valida que los datos estén completos.
6. El pendiente se guarda en SQLite.
7. Se programa una notificación local.
8. La app actualiza la lista de pendientes.
9. El usuario visualiza el pendiente registrado.
```

Esta vista es importante porque muestra el comportamiento del sistema paso a paso.

---

## Diagrama de Vista de Procesos

El siguiente diagrama muestra el flujo principal cuando el usuario registra un pendiente en PendixApp.

Para que la imagen se muestre correctamente en GitHub, el archivo debe estar en la misma carpeta que este README.

```text
mermaid-diagram-1780715733924.png
```

![Diagrama Mermaid de vistas arquitectónicas](mermaid-diagram-1780715733924.png)

---

## Diagrama C4 de PendixApp

El diagrama C4 permite representar PendixApp en dos niveles:

```text
- Nivel 1: Contexto del sistema.
- Nivel 2: Contenedores.
```

Este diagrama muestra la relación entre el usuario, PendixApp, el sistema de notificaciones de Android, SQLite y Firebase como posible servicio futuro.

```text
C4_Angel_Lugo.png
```

![Diagrama C4 de PendixApp](C4_Angel_Lugo.png)

---

## Nivel 1: Contexto del Sistema

En el nivel de contexto se muestra la relación general entre el usuario y PendixApp.

El usuario utiliza la aplicación para gestionar tareas y recordatorios personales.  
PendixApp interactúa con el sistema de notificaciones de Android para enviar recordatorios locales.

También se contempla Firebase como un servicio externo futuro para sincronizar datos.

```text
Usuario
   ↓
PendixApp
   ↓
Sistema de Notificaciones de Android

PendixApp
   ↓
Firebase futura integración
```

---

## Nivel 2: Contenedores

En el nivel de contenedores se muestra la organización general del sistema.

| Contenedor | Responsabilidad |
| :--- | :--- |
| **PendixApp Mobile App** | Contiene la interfaz, la lógica de pendientes y la gestión de recordatorios |
| **Local SQLite Database** | Guarda los pendientes y tareas de forma local |
| **Sistema de Notificaciones de Android** | Envía recordatorios al usuario |
| **Firebase** | Posible almacenamiento y sincronización futura en la nube |

---

## Organización General del Sistema

La organización general de PendixApp se puede representar de esta forma:

```text
Usuario
   ↓
Interfaz de Usuario
   ↓
Lógica de Pendientes
   ↓
SQLite
   ↓
Notificaciones Android
```

Cada parte tiene una responsabilidad clara:

```text
- La interfaz permite al usuario interactuar con la aplicación.
- La lógica valida y procesa los pendientes.
- SQLite guarda la información localmente.
- Las notificaciones Android muestran los recordatorios.
```

---

## Alternativas Consideradas

| Alternativa | Motivo por el que no se eligió |
| :--- | :--- |
| **Solo Android con Java** | Es la opción principal por ahora, aunque limita el proyecto inicialmente a Android |
| **React Native** | Permite Android e iOS, pero requiere aprender mejor JavaScript y React Native |
| **Página web** | No se adapta tan bien al uso móvil con recordatorios locales |
| **Kotlin** | Es una buena opción para Android, pero no es el lenguaje que más se domina actualmente |
| **Firebase desde el inicio** | Puede ser útil, pero agrega configuración y dependencia externa desde una etapa temprana |

---

## Consecuencias de la Decisión

### Lo que se gana

Al usar Java para Android, PendixApp puede desarrollarse con una tecnología conocida y adecuada para aplicaciones móviles nativas.

También se facilita el uso de funciones propias del dispositivo, como SQLite y notificaciones locales.

### Consecuencia técnica

La aplicación puede guardar información de manera local y trabajar sin depender de internet en su primera versión.

Además, la arquitectura puede mantenerse sencilla, separando interfaz, lógica, datos y notificaciones.

### Consecuencia sobre el proceso

Como Java es el lenguaje que más se domina, se puede avanzar más rápido durante el cuatrimestre y reducir errores por aprendizaje de tecnologías nuevas.

---

## Limitaciones y Riesgos

La principal limitación es que PendixApp funcionará primero en Android.

Esto significa que para funcionar en iOS sería necesario crear otra versión o migrar a una tecnología multiplataforma.

También existe el riesgo de que, si el proyecto crece, sea necesario mejorar la arquitectura, agregar sincronización en la nube o modificar la forma de almacenar los datos.

---

## Mejoras Futuras

```text
[ ] Crear una versión funcional inicial en Android.
[ ] Implementar SQLite como almacenamiento local.
[ ] Agregar notificaciones locales.
[ ] Mejorar la interfaz de usuario.
[ ] Agregar categorías para los pendientes.
[ ] Agregar prioridades para las tareas.
[ ] Agregar filtros por estado o fecha.
[ ] Agregar Firebase para sincronización en la nube.
[ ] Agregar inicio de sesión de usuarios.
[ ] Crear una versión multiplataforma en el futuro.
```

---

## Gestión con Git

Comandos básicos para subir los cambios de esta rama:

```bash
# Ir a la raíz del repositorio
cd /home/avenkal/Descargas/Proyecto_ArqSoft

# Ver la rama actual
git branch --show-current

# Ver el estado de los archivos
git status

# Agregar solo los archivos de esta rama
git add ADR-04-Angel-Lugo.md C4_Angel_Lugo.png mermaid-diagram-1780715733924.png README.md

# Crear commit
git commit -m "Agrega README de vistas arquitectonicas"

# Subir la rama
git push origin VistasArquitectonicas
```

---

## Cómo Visualizar la Rama en GitHub

Para revisar correctamente esta documentación en GitHub:

```text
1. Entrar al repositorio Proyecto_ArqSoft.
2. Cambiar la rama de main a VistasArquitectonicas.
3. Abrir el archivo README.md.
4. Verificar que se muestre el diagrama C4.
5. Verificar que se muestre el diagrama Mermaid de vista de procesos.
6. Abrir el archivo ADR-04-Angel-Lugo.md para revisar el ADR completo.
```

---

## Conclusión

Las vistas arquitectónicas permiten explicar PendixApp de una forma más completa.

Con estas modificaciones, el ADR ya no solo describe la decisión de usar Java para Android, sino que también muestra cómo se entiende el sistema desde sus funciones, organización interna, despliegue y proceso principal.

Esto ayuda a que el proyecto sea más claro, ordenado y fácil de explicar dentro de la materia de Arquitectura de Software.

---

## Cláusula de IA

```text
Yo, Angel Abraham Lugo Saenz, declaro que utilicé IA como apoyo para organizar y redactar este README, así como para estructurar la documentación relacionada con las vistas arquitectónicas de PendixApp.

El contenido principal, las decisiones del proyecto y la documentación del ADR fueron trabajados como parte de la actividad escolar de Arquitectura de Software.
```
