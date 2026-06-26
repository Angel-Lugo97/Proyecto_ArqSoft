# PendixApp - ADR-05: Evaluación de Arquitectura Hexagonal

PendixApp es una aplicación móvil enfocada en la gestión de pendientes y recordatorios personales.  
El objetivo del sistema es permitir que el usuario registre tareas, asigne fechas y horarios, consulte sus pendientes, marque actividades como completadas y reciba recordatorios desde un dispositivo Android.

En esta rama se documenta el **ADR-05**, donde se analiza la posibilidad de cambiar PendixApp a una **arquitectura hexagonal**, pero se decide **no realizar el cambio por ahora** debido al alcance actual del proyecto, el tiempo disponible y la complejidad que agregaría al desarrollo.

---

## Datos del Estudiante

| Campo | Información |
| :--- | :--- |
| **Nombre** | Angel Abraham Lugo Saenz |
| **Matrícula** | SW2409052 |
| **Materia** | Arquitectura de Software |
| **Profesor** | Jorge Javier Pedroza Romero |
| **Proyecto** | PendixApp |
| **Tarea** | ADR-05: Evaluación de arquitectura hexagonal |
| **Fecha** | 12/06/2026 |
| **Estado** | APROBADO |

---

## Descripción General

PendixApp es una aplicación pensada para ayudar a organizar tareas personales desde el celular.

El sistema permite registrar pendientes en un solo lugar, consultar actividades pendientes, editar información, eliminar tareas, marcar actividades como completadas y programar recordatorios para evitar que el usuario olvide actividades importantes.

La aplicación se plantea inicialmente para Android, utilizando **Java** como lenguaje principal. Para el almacenamiento de información se considera **SQLite** como base de datos local, y en una mejora futura se contempla el uso de **Firebase** para sincronización en la nube.

---

## Objetivo de esta Rama

El objetivo de esta rama es documentar la evaluación de la **arquitectura hexagonal** dentro de PendixApp.

Aunque la rama se llama `Hexagonal`, el objetivo no es implementar completamente esta arquitectura, sino justificar por qué **no conviene cambiar el proyecto a arquitectura hexagonal en este momento**.

Esta rama explica:

```text
- Qué arquitectura se está usando actualmente.
- Por qué PendixApp se mantiene con una arquitectura en capas.
- Qué ventajas tendría la arquitectura hexagonal.
- Por qué no se adopta todavía.
- Qué consecuencias tiene mantener una arquitectura más sencilla.
- Cómo se relacionan los diagramas con la decisión tomada.
```

Esto permite dejar claro que la arquitectura hexagonal fue considerada, pero no elegida como arquitectura principal para la versión actual del proyecto.

---

## Archivos de la Rama

En esta rama deben aparecer únicamente los archivos relacionados con el ADR-05 y sus diagramas:

```text
Proyecto_ArqSoft/
│
├── ADR-05-Angel_Lugo.md
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

## Decisión Principal del ADR-05

La decisión principal del ADR-05 es **mantener PendixApp con una arquitectura en capas** y **no cambiarla todavía a una arquitectura hexagonal**.

Esta decisión se tomó porque PendixApp es una aplicación móvil de alcance escolar, enfocada principalmente en registrar pendientes, guardarlos localmente y generar recordatorios.

La arquitectura en capas permite separar de forma clara:

```text
- Interfaz de usuario.
- Lógica de pendientes.
- Almacenamiento local.
- Servicios de notificaciones.
```

Con esta separación, el sistema puede mantenerse ordenado sin agregar una estructura demasiado compleja para el tamaño actual del proyecto.

---

## ¿Qué es la Arquitectura Hexagonal en este contexto?

La arquitectura hexagonal busca separar el núcleo del sistema de los elementos externos.

En PendixApp, esto significaría aislar la lógica principal de pendientes de detalles como:

```text
- Pantallas de Android.
- Base de datos SQLite.
- Firebase.
- Sistema de notificaciones.
- Otros servicios externos.
```

Esto puede ser útil en proyectos grandes, porque permite cambiar una base de datos, una interfaz o un servicio externo sin modificar directamente la lógica principal del sistema.

Sin embargo, para la versión actual de PendixApp, aplicar esta arquitectura desde el inicio agregaría más clases, interfaces, adaptadores y estructura de la que realmente se necesita.

---

## ¿Por qué no se cambió a Arquitectura Hexagonal?

No se cambió a arquitectura hexagonal porque el proyecto todavía tiene un alcance pequeño y controlado.

PendixApp necesita resolver funciones concretas:

```text
- Crear pendientes.
- Editar pendientes.
- Eliminar pendientes.
- Consultar pendientes registrados.
- Marcar pendientes como completados.
- Asignar fecha y hora.
- Guardar información en SQLite.
- Programar notificaciones locales.
```

Para estas funciones, una arquitectura en capas es suficiente.  
Cambiar a arquitectura hexagonal en este momento aumentaría la complejidad del proyecto sin aportar un beneficio inmediato proporcional.

---

## Razones principales para no usar Hexagonal por ahora

| Razón | Explicación |
| :--- | :--- |
| **Mayor complejidad** | La arquitectura hexagonal requiere más separación, interfaces, adaptadores y organización interna |
| **Alcance pequeño** | PendixApp todavía es una app móvil sencilla de pendientes y recordatorios |
| **Tiempo limitado** | El proyecto se desarrolla dentro del cuatrimestre, por lo que conviene priorizar una versión funcional |
| **No hay múltiples plataformas todavía** | La primera versión está pensada principalmente para Android |
| **No hay servidor obligatorio** | La app puede funcionar localmente con SQLite y notificaciones Android |
| **Menor riesgo de errores** | Usar una arquitectura conocida permite avanzar con menos problemas durante el desarrollo |
| **Aprendizaje gradual** | La arquitectura hexagonal puede considerarse después, cuando PendixApp crezca más |

---

## Arquitectura que se mantiene

La arquitectura que se mantiene es una **arquitectura en capas**.

Esta arquitectura separa el sistema en partes más sencillas de entender:

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

Esta organización permite mantener el proyecto claro, funcional y fácil de explicar.

---

## Comparación: Arquitectura en Capas vs Arquitectura Hexagonal

| Aspecto | Arquitectura en Capas | Arquitectura Hexagonal |
| :--- | :--- | :--- |
| **Complejidad** | Menor y más sencilla de aplicar | Mayor, requiere más abstracción |
| **Organización** | Separa interfaz, lógica, datos y servicios | Separa dominio, puertos y adaptadores |
| **Uso actual en PendixApp** | Adecuada para la primera versión | Excesiva para el alcance actual |
| **Mantenimiento** | Suficiente para una app pequeña | Mejor para proyectos grandes o escalables |
| **Tiempo de desarrollo** | Más rápida de implementar | Requiere más diseño y estructura |
| **Riesgo** | Menor para una entrega escolar | Mayor si no se domina completamente |

---

## Alternativas Consideradas

| Alternativa | Motivo por el que no se eligió |
| :--- | :--- |
| **Arquitectura hexagonal** | Agrega más abstracción y complejidad de la necesaria para el alcance actual del proyecto |
| **Arquitectura cliente-servidor** | No es necesaria porque la primera versión puede funcionar localmente con SQLite |
| **Arquitectura de microservicios** | Es demasiado compleja para una app personal de pendientes |
| **Arquitectura serverless** | No se necesitan funciones en la nube para crear, consultar y recordar pendientes |
| **Arquitectura orientada a eventos** | Se consideró por las notificaciones, pero la mayoría de acciones dependen directamente del usuario |
| **React Native** | Permite Android e iOS, pero requiere aprender mejor JavaScript y React Native |
| **Kotlin** | Es una buena opción para Android, pero Java es el lenguaje que más se domina actualmente |
| **Aplicación web** | No se adapta tan bien al uso móvil con recordatorios locales |

---

## Consecuencias de la Decisión

### Lo que se gana

Al mantener la arquitectura en capas, PendixApp puede desarrollarse de forma más directa y entendible.

También se facilita avanzar con las funciones principales sin perder demasiado tiempo en una estructura más compleja.

### Consecuencia técnica

La aplicación mantiene una separación clara entre interfaz, lógica, datos y notificaciones.

Esto permite que el código sea más ordenado y que sea posible modificar algunas partes sin afectar todo el sistema.

Por ejemplo, si más adelante se cambia SQLite por Firebase, el cambio podría trabajarse principalmente en la parte de datos.

### Consecuencia sobre el proceso

Como el proyecto se realiza dentro del cuatrimestre, mantener una arquitectura más sencilla ayuda a avanzar más rápido y reducir errores.

También permite enfocarse primero en que la aplicación funcione correctamente antes de agregar una arquitectura más avanzada.

---

## Limitaciones y Riesgos

La principal limitación es que la arquitectura en capas no ofrece el mismo nivel de independencia que una arquitectura hexagonal.

Si PendixApp crece demasiado, podría ser necesario separar mejor el dominio, crear interfaces, agregar adaptadores y preparar el sistema para más servicios externos.

También existe el riesgo de que, si no se respeta la separación por capas, el código pueda mezclarse entre pantallas, lógica y base de datos.

Por eso es importante mantener responsabilidades claras desde el inicio.

---

## ¿Cuándo sí convendría usar Arquitectura Hexagonal?

La arquitectura hexagonal podría ser una buena opción en el futuro si PendixApp crece y necesita más flexibilidad.

Podría considerarse cuando el sistema requiera:

```text
- Sincronización real con Firebase.
- API externa o servidor propio.
- Versión para Android e iOS.
- Pruebas más avanzadas de la lógica del sistema.
- Cambiar fácilmente SQLite por otra base de datos.
- Separar completamente la lógica principal de Android.
- Mantener la misma lógica para varias interfaces.
```

En ese caso, la arquitectura hexagonal sí tendría más sentido, porque ayudaría a que el sistema sea más independiente de tecnologías externas.

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

La intención es evitar que PendixApp quede como un proyecto desordenado, donde todo el código esté mezclado en una sola clase.

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
[ ] Evaluar nuevamente arquitectura hexagonal si el proyecto crece.
[ ] Crear una versión multiplataforma en el futuro.
```

---

## Gestión con Git

Comandos básicos para subir únicamente el README de esta rama:

```bash
# Ir a la raíz del repositorio
cd /home/avenkal/Descargas/Proyecto_ArqSoft

# Ver la rama actual
git branch --show-current

# Debe decir:
# Hexagonal

# Ver el estado de los archivos
git status

# Agregar solo el README
git add README.md

# Crear commit
git commit -m "Agrega README del ADR 05"

# Subir la rama
git push origin Hexagonal
```

> Importante: no usar `git add .` si solo se quiere subir el README.

---

## Cómo Visualizar la Rama en GitHub

Para revisar correctamente esta documentación en GitHub:

```text
1. Entrar al repositorio Proyecto_ArqSoft.
2. Cambiar la rama de main a Hexagonal.
3. Abrir el archivo README.md.
4. Verificar que se muestre el diagrama C4.
5. Verificar que se muestre el diagrama Mermaid de vista de procesos.
6. Abrir el archivo ADR-05-Angel_Lugo.md para revisar el ADR completo.
```

---

## Conclusión

En el ADR-05 se documenta que la arquitectura hexagonal fue considerada para PendixApp, pero no se eligió como arquitectura principal en este momento.

La razón principal es que PendixApp todavía tiene un alcance pequeño y puede funcionar correctamente con una arquitectura en capas. Esta arquitectura permite separar interfaz, lógica, almacenamiento y notificaciones sin agregar una complejidad innecesaria.

La arquitectura hexagonal puede ser útil en el futuro si el proyecto crece, necesita conectarse a más servicios externos o requiere mayor independencia entre la lógica del sistema y las tecnologías utilizadas.

Por ahora, mantener una arquitectura en capas permite que PendixApp sea más fácil de desarrollar, explicar, mantener y entregar dentro del tiempo disponible.

---

## Cláusula de IA

```text
Yo, Angel Abraham Lugo Saenz, declaro que utilicé IA como apoyo para organizar y redactar este README, así como para estructurar la explicación relacionada con el ADR-05 y la evaluación de la arquitectura hexagonal en PendixApp.

El contenido principal, las decisiones del proyecto y la documentación del ADR fueron trabajados como parte de la actividad escolar de Arquitectura de Software.
```
