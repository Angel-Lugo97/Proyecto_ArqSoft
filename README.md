# PendixAPP - Rama diagramas

PendixAPP es una aplicación enfocada en la gestión de pendientes y recordatorios personales.  
El sistema permite registrar tareas, asignar fechas y horarios, consultar pendientes, marcar actividades como completadas y organizar recordatorios desde una interfaz web ejecutada localmente con Java.

En esta rama llamada **diagramas** se documenta la arquitectura del proyecto mediante el **Modelo C4**, usando diagramas escritos como código en **Mermaid** dentro de un archivo Markdown.

El objetivo de esta rama es representar de forma clara cómo está estructurado PendixAPP en tres niveles: el contexto general del sistema, los contenedores técnicos principales y los componentes internos más importantes.

Se agregan estos diagramas porque permiten comprender mejor quién usa el sistema, cómo se comunican sus partes principales y qué responsabilidades tiene cada componente dentro del proyecto, sin depender de imágenes sueltas ni documentación separada del repositorio.

---

## Datos del Estudiante

| Campo | Información |
| :--- | :--- |
| **Nombre** | Angel Abraham Lugo Saenz |
| **Matrícula** | SW2409052 |
| **Materia** | Arquitectura de Software |
| **Profesor** | Jorge Javier Pedroza Romero |
| **Proyecto** | PendixAPP |
| **Tarea** | Actividad #28 — Proyecto: creación de rama diagramas y documentación C4 |
| **Fecha** | 07/08/2026 |
| **Estado** | Documentación C4 agregada en rama diagramas |

---

## Descripción General

PendixAPP es un prototipo funcional orientado a la gestión de pendientes personales.

Actualmente, el proyecto se ejecuta de forma local mediante un servidor en **Java**, utilizando `HttpServer` y el puerto `5018`.  
Desde el navegador se muestra una interfaz tipo celular donde el usuario puede administrar sus pendientes y revisar secciones como calendario, recordatorios, planes, ajustes e inicio de sesión simulado.

La información del prototipo se maneja principalmente desde el navegador mediante **JavaScript** y se conserva usando **localStorage**.  
Por eso, la arquitectura documentada en esta rama se enfoca en representar el funcionamiento real del proyecto actual, sin inventar una base de datos externa ni servicios que todavía no forman parte del código.

---

## Objetivo de esta Rama

El objetivo de esta rama es documentar la arquitectura de PendixAPP mediante el **Modelo C4**, utilizando diagramas escritos como código en **Mermaid** dentro de un archivo Markdown.

Esta actividad pide representar la arquitectura del proyecto en tres niveles principales: **Contexto, Contenedores y Componentes**.  
Por eso se creó la rama **diagramas**, donde se agregó la documentación técnica necesaria para explicar cómo está organizado el sistema y cómo se comunican sus partes.

Esta rama explica:

```text
- Quién usa PendixAPP y cuál es el propósito general del sistema.
- Cuáles son las piezas técnicas principales del proyecto.
- Cómo se comunican el navegador, el servidor Java, la interfaz web y el almacenamiento local.
- Qué componentes internos forman la pieza principal del sistema.
- Qué responsabilidades tienen elementos como PendixAppServer, PendixHandler, las vistas, el gestor de tareas y localStorage.
- Por qué los diagramas se agregaron como código Mermaid y no como imágenes sueltas.
- Cómo la documentación C4 ayuda a entender mejor la arquitectura actual del proyecto.
```

Además, esta rama refleja el proceso de trabajo solicitado en la actividad, ya que los diagramas C4 fueron agregados por niveles y posteriormente corregidos para asegurar que se rendericen correctamente en GitHub.

---

## Arquitectura C4 — Contexto, Contenedores y Componentes

La documentación completa de la arquitectura de PendixAPP, versionada como código mediante Mermaid, se encuentra en:

[`PendixAPP/docs/C4_PendixAPP.md`](PendixAPP/docs/C4_PendixAPP.md)

| Nivel | Archivo | Descripción |
| :--- | :--- | :--- |
| **C4 Nivel 1 — Contexto** | [`C4_PendixAPP.md`](PendixAPP/docs/C4_PendixAPP.md) | Muestra quién usa PendixAPP y cuál es el propósito general del sistema |
| **C4 Nivel 2 — Contenedores** | [`C4_PendixAPP.md`](PendixAPP/docs/C4_PendixAPP.md) | Muestra las piezas técnicas grandes del sistema, como el navegador, el servidor Java, la interfaz web y el almacenamiento local |
| **C4 Nivel 3 — Componentes** | [`C4_PendixAPP.md`](PendixAPP/docs/C4_PendixAPP.md) | Muestra los componentes internos principales, como `PendixAppServer`, `PendixHandler`, las vistas, el gestor de tareas y la persistencia con `localStorage` |

> Los diagramas C4 no se agregan como imágenes sueltas, sino como código Mermaid dentro del archivo Markdown.

---

## ¿Por qué se usó el Modelo C4?

El Modelo C4 se utilizó porque permite explicar la arquitectura del sistema por niveles, de lo general a lo técnico.

En lugar de mostrar un solo diagrama con demasiada información, se separó la documentación en tres vistas principales:

```text
Nivel 1: Contexto
Nivel 2: Contenedores
Nivel 3: Componentes
```

Esto facilita que una persona no técnica entienda primero qué es PendixAPP y quién lo usa, mientras que una persona con conocimientos técnicos puede revisar después cómo se divide el sistema y qué componentes internos lo forman.

---

## C4 Nivel 1 — Contexto

El **Nivel 1** explica PendixAPP en términos simples.

Este nivel responde:

```text
¿Quién usa el sistema y para qué sirve PendixAPP?
```

En este nivel se representa la relación entre:

```text
- Usuario.
- PendixAPP.
- Navegador web.
```

La finalidad es mostrar que el usuario utiliza PendixAPP para registrar, consultar, modificar y eliminar pendientes desde una interfaz web ejecutada localmente.

---

## C4 Nivel 2 — Contenedores

El **Nivel 2** muestra las piezas técnicas principales del sistema.

Este nivel responde:

```text
¿Cuáles son las partes grandes de PendixAPP y cómo se comunican?
```

En este nivel se representan elementos como:

```text
- Navegador web.
- Servidor local Java.
- PendixAppServer.
- Interfaz HTML, CSS y JavaScript.
- API simulada /api/pendientes.
- localStorage.
```

Este nivel ayuda a entender que el navegador solicita la aplicación al servidor Java, recibe la interfaz web y guarda información localmente mediante `localStorage`.

---

## C4 Nivel 3 — Componentes

El **Nivel 3** muestra los componentes internos de la pieza principal del sistema.

Este nivel responde:

```text
¿Qué hay dentro del componente principal de PendixAPP?
```

En este nivel se representan componentes como:

```text
- main()
- abrirNavegador()
- PendixHandler
- handle()
- enviar()
- html()
- Vista de pendientes
- Vista calendario
- Vista recordatorios
- Vista ajustes
- Gestor de tareas JS
- Gestor de sesión JS
- Persistencia con localStorage
```

Este nivel permite explicar mejor qué responsabilidad tiene cada parte del sistema y cómo se conectan la lógica del servidor Java con la interfaz web que usa el usuario.

---

## Archivos de la Rama

La rama **diagramas** contiene la documentación necesaria para cumplir con la actividad del Modelo C4.

La estructura principal es:

```text
Proyecto_ArqSoft/
│
├── README.md
│
└── PendixAPP/
    │
    └── docs/
        └── C4_PendixAPP.md
```

El archivo principal de la entrega es:

```text
PendixAPP/docs/C4_PendixAPP.md
```

Dentro de ese archivo se encuentran los tres diagramas C4 escritos como código Mermaid.

---

## Evidencia de Ejecución Funcional

La siguiente imagen muestra el prototipo **PendixAPP** ejecutándose correctamente desde el navegador mediante un servidor local en Java usando el puerto `5018`.

En la captura se puede observar la interfaz tipo celular, el tema oscuro con tonos púrpuras, los filtros con conteo integrado, la sección de notificaciones vencidas y la sección de planes e inicio de sesión.

```text
URL de ejecución: http://localhost:5018
Servidor: Java
Puerto: 5018
```

![Captura de ejecución funcional de PendixAPP](Captura.png)

> Esta captura solo funciona como evidencia visual del prototipo. Los diagramas C4 de la actividad están documentados como código Mermaid en `PendixAPP/docs/C4_PendixAPP.md`.

---

## Ejecución del Prototipo Java

Además de la documentación C4, se mantiene una versión funcional del prototipo web de PendixAPP ejecutada con un servidor local en **Java**.

La finalidad de esta versión es mostrar visualmente cómo se comporta la aplicación desde el navegador, respetando la arquitectura documentada en los diagramas.

### Estructura de ejecución

```text
PendixAPP/
│
├── src/
│   └── PendixAppServer.java
│
├── public/
│   └── index.html
│
├── PendixApp.jar
├── iniciar_linux.sh
├── iniciar_mac.command
├── INICIAR_WINDOWS.bat
├── compilar_y_ejecutar_linux.sh
└── README_EJECUCION.md
```

### Cómo ejecutarlo

En Linux o Mac:

```bash
bash iniciar_linux.sh
```

O directamente con Java:

```bash
java -jar PendixApp.jar
```

Después se abre en el navegador:

```text
http://localhost:5018
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
```

---

## Proceso de Trabajo en Git

La actividad solicita que el repositorio muestre una rama llamada **diagramas** y que el trabajo no se suba en un solo commit.

Por eso, la documentación se trabajó en commits separados, agregando los niveles C4 por partes.

Ejemplo del proceso esperado:

```bash
git switch -c diagramas
git add PendixAPP/docs/C4_PendixAPP.md
git commit -m "docs: agregar C4 nivel 1 contexto"

git add PendixAPP/docs/C4_PendixAPP.md
git commit -m "docs: agregar C4 nivel 2 contenedores"

git add PendixAPP/docs/C4_PendixAPP.md
git commit -m "docs: agregar C4 nivel 3 componentes"

git add README.md
git commit -m "docs: actualizar README de rama diagramas"
git push origin diagramas
```

---

## Gestión con Git

Comandos básicos para trabajar sobre la rama **diagramas**:

```bash
cd /home/avenkal/Descargas/Proyecto_ArqSoft
git switch diagramas
git status
git add README.md PendixAPP/docs/C4_PendixAPP.md
git commit -m "docs: actualizar documentacion C4"
git push origin diagramas
```

> Importante: antes de subir cambios, revisar con `git status` para confirmar que solo se agreguen los archivos necesarios.

---

## Cómo Visualizar la Rama en GitHub

Para revisar correctamente esta documentación en GitHub:

```text
1. Entrar al repositorio Proyecto_ArqSoft.
2. Cambiar la rama de main a diagramas.
3. Abrir el archivo README.md.
4. Entrar al enlace PendixAPP/docs/C4_PendixAPP.md.
5. Verificar que se muestren los tres niveles C4.
6. Confirmar que los diagramas estén escritos en Mermaid y no como imágenes sueltas.
```

---

## Mejoras Futuras

```text
[ ] Mantener actualizados los diagramas C4 cuando cambie el código.
[ ] Separar la interfaz HTML, CSS y JavaScript en archivos independientes si el proyecto crece.
[ ] Agregar una capa de servicios si la lógica de tareas aumenta.
[ ] Evaluar una base de datos local o externa si se requiere persistencia más formal.
[ ] Documentar nuevos componentes en el Nivel 3 si se agregan más módulos.
[ ] Revisar que los enlaces del README sigan funcionando después de mover archivos.
```

---

## Conclusión

En esta rama se documenta la arquitectura de PendixAPP mediante el **Modelo C4**.

Se agregaron tres niveles principales: **Contexto, Contenedores y Componentes**, con el propósito de explicar el sistema desde una vista general hasta una vista más técnica.

La documentación se realizó con **Mermaid** dentro de un archivo Markdown para que los diagramas formen parte del repositorio como código y puedan visualizarse directamente desde GitHub.

Con esta rama, PendixAPP cuenta con una explicación más clara de su estructura actual, de sus piezas técnicas principales y de los componentes internos que permiten su funcionamiento.

---

## Cláusula de IA

```text
Yo, Angel Abraham Lugo Saenz, declaro que utilicé IA como apoyo para organizar y redactar este README, así como para estructurar la explicación relacionada con la rama diagramas y la documentación C4 de PendixAPP.

El contenido principal, las decisiones del proyecto y la documentación de la arquitectura fueron trabajados como parte de la actividad escolar de Arquitectura de Software.
```
