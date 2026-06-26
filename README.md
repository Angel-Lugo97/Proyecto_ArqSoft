# PendixApp - ADR-04: Decisión Arquitectónica Inicial

PendixApp es un proyecto de aplicación móvil enfocado en la gestión de pendientes y recordatorios personales.  
El objetivo principal del sistema es permitir que el usuario registre tareas, asigne fechas y horarios, consulte sus actividades pendientes, marque tareas como completadas y reciba recordatorios desde su dispositivo móvil.

Este repositorio documenta el **ADR-04**, donde se explica la decisión de construir PendixApp como una aplicación móvil Android utilizando **Java**, con almacenamiento local mediante **SQLite** y una posible integración futura con **Firebase**.

---

## Datos del Estudiante

| Campo | Información |
| :--- | :--- |
| **Nombre** | Angel Abraham Lugo Saenz |
| **Matrícula** | SW2409052 |
| **Materia** | Arquitectura de Software |
| **Profesor** | Jorge Javier Pedroza Romero |
| **Proyecto** | PendixApp |
| **Tarea** | ADR-04 - Decisión arquitectónica y diagrama C4 |
| **Fecha** | 15/05/2026 |
| **Estado** | APROBADO |

---

## Descripción General

PendixApp es una aplicación pensada para ayudar a las personas a organizar sus tareas personales desde el celular.

El sistema busca resolver un problema común: muchas veces las personas tienen pendientes importantes, pero los olvidan porque están anotados en diferentes lugares, como libretas, mensajes, notas del celular o simplemente en la memoria.

Con PendixApp, el usuario puede registrar sus pendientes en un solo lugar, consultarlos cuando sea necesario, editarlos, eliminarlos, marcarlos como completados y recibir notificaciones para recordar actividades importantes.

---

## Objetivo del ADR

El objetivo de este ADR es documentar la decisión de desarrollar PendixApp como una aplicación móvil para Android utilizando **Java** como lenguaje principal.

Esta decisión permite mantener el alcance del proyecto realista, ya que Java es una tecnología conocida y adecuada para crear aplicaciones móviles nativas en Android.

También se documentan las alternativas consideradas, las razones de la decisión, sus consecuencias técnicas y los posibles riesgos a futuro.

---

## Decisión Arquitectónica

Se decidió desarrollar PendixApp como una aplicación móvil nativa para Android utilizando **Java**.

La aplicación se organizará de forma sencilla, separando la parte visual, la lógica del sistema y el almacenamiento de datos.

La idea principal es que PendixApp pueda realizar funciones como:

```text
- Crear pendientes.
- Editar pendientes.
- Eliminar pendientes.
- Consultar tareas registradas.
- Marcar pendientes como completados.
- Asignar fechas y horarios.
- Gestionar recordatorios locales.
```

Para guardar la información, el sistema puede usar una base de datos local como **SQLite**.  
A futuro, también se considera una integración con **Firebase** para sincronizar datos en la nube.

---

## ¿Por qué se eligió Java para Android?

Se eligió Java porque es el lenguaje que más se domina dentro del alcance del proyecto.  
Esto permite avanzar de forma más segura y rápida durante el desarrollo, evitando dedicar demasiado tiempo a aprender una tecnología nueva desde cero.

Además, Java permite crear aplicaciones Android nativas, por lo que se adapta bien a la idea principal de PendixApp: una aplicación móvil de pendientes y recordatorios.

La decisión también ayuda a mantener el proyecto dentro de un alcance realista para el cuatrimestre, enfocándose primero en que la aplicación funcione correctamente en Android.

---

## Problema que Resuelve PendixApp

PendixApp busca resolver la falta de organización personal al permitir que el usuario registre sus pendientes en una sola aplicación.

Muchas veces una persona recuerda una tarea en el momento, pero después la olvida porque no la anotó o porque la información está dispersa en varios lugares.

Con PendixApp, el usuario puede tener mayor control sobre sus actividades y consultar fácilmente qué tareas siguen activas, cuáles ya completó y cuáles están próximas a vencer.

---

## Tecnologías Consideradas

| Tecnología | Uso dentro del proyecto |
| :--- | :--- |
| **Java** | Lenguaje principal para desarrollar la aplicación Android |
| **Android SDK** | Herramientas necesarias para construir la app móvil |
| **SQLite** | Almacenamiento local de pendientes y tareas |
| **Firebase** | Posible sincronización en la nube a futuro |
| **Git** | Control de versiones del proyecto |
| **GitHub** | Repositorio para subir la documentación y evidencias |
| **Markdown** | Documentación del ADR y README |
| **Diagramas C4** | Representación visual del contexto y contenedores del sistema |

---

## Funcionalidades Principales Esperadas

PendixApp está pensada para incluir las siguientes funciones principales:

```text
- Registrar una nueva tarea o pendiente.
- Agregar fecha y hora a un pendiente.
- Consultar la lista de pendientes.
- Editar información de una tarea.
- Eliminar pendientes que ya no sean necesarios.
- Marcar tareas como completadas.
- Recibir recordatorios mediante notificaciones locales.
- Guardar la información de forma local en el dispositivo.
```

---

## Estructura Actual de la Rama

En esta rama se encuentran los archivos principales correspondientes al ADR-04:

```text
Proyecto_ArqSoft/
│
├── ADR-04-Angel-Lugo.md
├── C4_Angel_Lugo.png
└── README.md
```

---

## Archivo ADR

El archivo principal del ADR es:

```text
ADR-04-Angel-Lugo.md
```

En este documento se explican los siguientes puntos:

```text
- Contexto del proyecto PendixApp.
- Decisión de usar Java para Android.
- Razones de la decisión.
- Alternativas consideradas.
- Consecuencias técnicas.
- Consecuencias sobre el proceso.
- Riesgos y deuda técnica.
- Diagrama C4 del sistema.
- Cláusula de uso de IA.
```

---

## Diagrama C4 del Proyecto

El diagrama C4 permite representar PendixApp desde dos niveles principales:

```text
- Nivel 1: Contexto del sistema.
- Nivel 2: Contenedores del sistema.
```

Para que la imagen se muestre correctamente en GitHub, el archivo debe estar en la misma carpeta que este README:

```text
C4_Angel_Lugo.png
```

![Diagrama C4 de PendixApp](C4_Angel_Lugo.png)

---

## Nivel 1: Contexto del Sistema

En el primer nivel del diagrama C4 se muestra la relación general entre el usuario y PendixApp.

El usuario utiliza la aplicación para gestionar sus tareas y recordatorios personales.  
PendixApp se comunica con el sistema de notificaciones de Android para programar y mostrar recordatorios locales.

También se considera Firebase como un servicio externo futuro para sincronizar datos en la nube.

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

En el segundo nivel se muestra cómo se organiza PendixApp internamente.

Los contenedores principales son:

| Contenedor | Descripción |
| :--- | :--- |
| **PendixApp Mobile App** | Aplicación móvil donde el usuario interactúa con la interfaz |
| **Local SQLite Database** | Base de datos local para almacenar pendientes y tareas |
| **Sistema de Notificaciones de Android** | Servicio del dispositivo que permite enviar recordatorios |
| **Firebase** | Servicio futuro para almacenamiento y sincronización en la nube |

---

## Organización General del Sistema

La organización propuesta para PendixApp se mantiene sencilla para facilitar su desarrollo y mantenimiento.

```text
Usuario
   ↓
Interfaz de PendixApp
   ↓
Lógica de pendientes
   ↓
Base de datos local SQLite
   ↓
Notificaciones de Android
```

Esta estructura permite separar las responsabilidades del sistema:

```text
- La interfaz muestra las opciones al usuario.
- La lógica controla las acciones sobre los pendientes.
- SQLite guarda la información localmente.
- Android se encarga de mostrar las notificaciones.
```

---

## Alternativas Consideradas

Durante la decisión arquitectónica se analizaron varias alternativas antes de elegir Java para Android.

| Alternativa | Motivo por el que no se eligió |
| :--- | :--- |
| **Solo Android con Java** | Es la opción más sencilla y realista para iniciar, pero como única opción limita el proyecto a Android |
| **React Native** | Permite crear una app para Android y iOS, pero requiere aprender mejor JavaScript y React Native |
| **Página web** | Sería más simple, pero no aprovecha igual las notificaciones y el uso móvil del proyecto |
| **Kotlin** | Es una buena opción para Android, pero no es el lenguaje que más se domina para este proyecto |

---

## Consecuencias de la Decisión

### Lo que se gana

Al usar Java para Android, el desarrollo de PendixApp se vuelve más directo y manejable.  
Se pueden construir las funciones principales del sistema usando una tecnología conocida.

También se facilita la organización del código mediante clases, separando los datos de cada pendiente y la lógica de la aplicación.

### Consecuencia técnica

La aplicación puede aprovechar herramientas nativas de Android, como el sistema de notificaciones locales y el almacenamiento con SQLite.

Esto ayuda a que PendixApp funcione de manera adecuada en dispositivos Android sin depender desde el inicio de servicios externos.

### Consecuencia sobre el proceso

Como Java es una tecnología conocida, se puede avanzar con menos errores y con mayor seguridad durante el cuatrimestre.

Esto permite dedicar más tiempo a que la aplicación sea funcional y menos tiempo a aprender una herramienta nueva desde cero.

---

## Limitaciones y Riesgos

La principal limitación es que PendixApp funcionará primero en dispositivos Android.

Esto significa que no podrá publicarse fácilmente en iOS sin hacer una versión diferente o adaptar el proyecto con otra tecnología.

También existe el riesgo de que, si el proyecto crece, sea necesario modificar la forma en que se guardan los datos, agregar sincronización en la nube o mejorar la arquitectura interna.

---

## Posible Deuda Técnica

Si PendixApp crece en el futuro, podrían aparecer necesidades como:

```text
- Migrar de SQLite local a una base de datos en la nube.
- Agregar inicio de sesión para usuarios.
- Sincronizar pendientes entre varios dispositivos.
- Crear una versión para iOS.
- Mejorar la arquitectura del código.
- Agregar pruebas automatizadas.
- Separar mejor las capas internas de la aplicación.
```

Estas mejoras podrían requerir más tiempo y posiblemente aprender nuevas tecnologías.

---

## Mejoras Futuras

```text
[ ] Crear una versión funcional inicial en Android.
[ ] Implementar almacenamiento local con SQLite.
[ ] Agregar recordatorios mediante notificaciones locales.
[ ] Mejorar la interfaz de usuario.
[ ] Agregar categorías para organizar pendientes.
[ ] Agregar prioridades para las tareas.
[ ] Agregar búsqueda de pendientes.
[ ] Agregar sincronización con Firebase.
[ ] Agregar autenticación de usuarios.
[ ] Crear una versión multiplataforma en el futuro.
```

---

## Relación con el ADR

Este README resume y complementa el ADR-04.

El ADR documenta la decisión principal del proyecto: desarrollar PendixApp como aplicación móvil Android con Java, manteniendo una arquitectura sencilla y considerando SQLite como almacenamiento local.

El README sirve como una vista general del contenido del ADR y facilita entender rápidamente qué decisión se tomó, por qué se tomó y cuáles son sus consecuencias.

---

## Gestión con Git

Comandos básicos para trabajar con esta rama:

```bash
# Ver la rama actual
git branch

# Ver el estado de los archivos
git status

# Agregar cambios específicos
git add README.md ADR-04-Angel-Lugo.md C4_Angel_Lugo.png

# Crear commit
git commit -m "Agrega README para ADR 04 de PendixApp"

# Subir cambios
git push origin main
```

Si se está trabajando en otra rama:

```bash
git push -u origin nombre-de-la-rama
```

---

## Cómo Visualizar el Proyecto en GitHub

Para revisar correctamente esta documentación en GitHub:

```text
1. Entrar al repositorio del proyecto.
2. Seleccionar la rama correspondiente.
3. Abrir el archivo README.md.
4. Verificar que el diagrama C4 se muestre correctamente.
5. Abrir el archivo ADR-04-Angel-Lugo.md para revisar el ADR completo.
```

---

## Conclusión

La decisión de desarrollar PendixApp con Java para Android permite mantener el proyecto dentro de un alcance realista y funcional.

Esta elección facilita avanzar con una tecnología conocida, aprovechar características nativas de Android y construir una primera versión enfocada en pendientes y recordatorios personales.

Aunque la decisión limita inicialmente el proyecto a Android, también permite dejar una base clara para futuras mejoras, como sincronización con Firebase, autenticación de usuarios o una versión multiplataforma.

---

## Cláusula de IA

```text
Yo, Angel Abraham Lugo Saenz, declaro que utilicé IA como apoyo para organizar y redactar este README, así como para estructurar la documentación relacionada con el ADR-04 de PendixApp.

El contenido principal, la decisión arquitectónica y el enfoque del proyecto fueron trabajados como parte de la actividad escolar de Arquitectura de Software.
```
