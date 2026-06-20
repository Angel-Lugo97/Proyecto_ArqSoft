# PendixApp - Sistema de Gestión de Pendientes y Recordatorios

PendixApp es un proyecto enfocado en la organización de pendientes y recordatorios personales. Su objetivo principal es permitir que el usuario registre tareas, consulte sus actividades pendientes, edite información, marque tareas como completadas y administre sus recordatorios de una forma sencilla.

Como mejora del proyecto, se incorporó una **API REST desarrollada con ASP.NET Core Web API**, la cual permite exponer las funciones principales del sistema mediante endpoints. Esta API está documentada con **Swagger**, lo que permite probar y visualizar los endpoints desde el navegador de una forma clara y profesional.

---

## Datos del Estudiante

| Campo | Información |
| :--- | :--- |
| **Nombre** | Angel Abraham Lugo Saenz |
| **Matrícula** | SW2409052 |
| **Materia** | Arquitectura de Software |
| **Profesor** | Jorge Javier Pedroza Romero |
| **Proyecto** | PendixApp |
| **Tarea** | Incorporación de API REST y documentación con Swagger |

---

## Descripción General

PendixApp es una aplicación pensada para ayudar a organizar tareas personales. El sistema permite registrar pendientes con información básica como título, descripción, fecha de vencimiento, prioridad y estado.

La primera idea del proyecto estaba enfocada en una aplicación móvil Android con almacenamiento local. Sin embargo, para mejorar la organización del sistema y acercarlo a una estructura más profesional, se agregó una **API REST** como parte del backend.

La API permite administrar pendientes mediante operaciones básicas como consultar, crear, editar, completar y eliminar tareas.

---

## Objetivo de la API REST

El objetivo de incorporar una API REST es separar parte de la lógica del sistema y permitir que los pendientes puedan ser administrados desde endpoints HTTP.

Esto permite que PendixApp tenga una base más preparada para crecer en el futuro, ya que una aplicación móvil, una aplicación web u otro cliente podrían consumir la misma API.

Además, Swagger permite documentar los endpoints de forma visual, facilitando la prueba y validación del funcionamiento del backend.

---

## Tecnologías Utilizadas

| Tecnología | Uso |
| :--- | :--- |
| **C#** | Lenguaje principal de la API |
| **ASP.NET Core Web API** | Creación de endpoints REST |
| **Swagger / OpenAPI** | Documentación y prueba de endpoints |
| **.NET SDK** | Ejecución y compilación del proyecto |
| **Git** | Control de versiones |
| **GitHub** | Repositorio del proyecto |
| **JSON** | Formato de intercambio de datos |
| **Arquitectura en capas** | Organización general del sistema |

---

## Funcionalidades Principales

La API REST de PendixApp permite realizar las siguientes acciones:

```text
- Consultar todos los pendientes registrados.
- Consultar un pendiente específico por su ID.
- Crear un nuevo pendiente.
- Editar la información de un pendiente existente.
- Marcar un pendiente como completado.
- Eliminar un pendiente.
- Probar los endpoints desde Swagger.
```

---

## Endpoints Implementados

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/pendientes` | Obtiene todos los pendientes |
| `GET` | `/api/pendientes/{id}` | Obtiene un pendiente por su ID |
| `POST` | `/api/pendientes` | Crea un nuevo pendiente |
| `PUT` | `/api/pendientes/{id}` | Actualiza un pendiente existente |
| `PATCH` | `/api/pendientes/{id}/completar` | Marca un pendiente como completado |
| `DELETE` | `/api/pendientes/{id}` | Elimina un pendiente |

---

## Modelo Principal

El modelo principal de la API es `Pendiente`.

```csharp
public class Pendiente
{
    public int Id { get; set; }
    public string Titulo { get; set; } = string.Empty;
    public string? Descripcion { get; set; }
    public DateTime FechaVencimiento { get; set; }
    public bool Completado { get; set; }
    public string Prioridad { get; set; } = "Media";
}
```

Este modelo representa una tarea o actividad pendiente dentro del sistema.

---

## Estructura del Proyecto

```text
Proyecto_ArqSoft/
│
├── ADR/
│   ├── ADR-01-PendixApp.md
│   └── ADR-02-API-REST-PendixApp.md
│
├── PendixApi/
│   ├── Controllers/
│   │   └── PendientesController.cs
│   │
│   ├── Models/
│   │   └── Pendiente.cs
│   │
│   ├── Program.cs
│   ├── appsettings.json
│   ├── appsettings.Development.json
│   └── PendixApi.csproj
│
├── mermaid-diagram-1780715733924.png
├── C4_Angel_Lugo.png
│
├── assets/
│   ├── swagger.png
│   ├── get-pendientes.png
│   └── post-pendiente.png
│
└── README.md
```

---

## Diagramas del Proyecto

Las siguientes imágenes forman parte de la documentación arquitectónica de PendixApp.  
Para que se muestren correctamente, los archivos deben estar en la misma carpeta que este README:

```text
/home/avenkal/Descargas/Proyecto_ArqSoft/
```

### Vista de procesos de PendixApp

Este diagrama muestra el flujo principal cuando el usuario registra un pendiente dentro de la aplicación. Se observa cómo interactúan el usuario, la interfaz, la lógica de pendientes, SQLite y las notificaciones locales de Android.

![Diagrama Mermaid de vistas arquitectónicas](mermaid-diagram-1780715733924.png)

---

### Diagrama C4 de PendixApp

Este diagrama muestra la vista de contexto y contenedores del sistema. Se representa PendixApp como aplicación móvil, su relación con el sistema de notificaciones de Android, la base local SQLite y una posible integración futura con Firebase.

![Diagrama C4 de PendixApp](C4_Angel_Lugo.png)

---

## Organización de la API

La API se organiza de forma sencilla para que sea fácil de entender y mantener.

```text
Cliente / Swagger
        ↓
Endpoints REST
        ↓
PendientesController
        ↓
Modelo Pendiente
        ↓
Lista de datos en memoria
```

Por ahora, la API puede trabajar con una lista en memoria para mantener el alcance del proyecto sencillo. En una mejora futura se podría conectar con SQLite, SQL Server o una base de datos en la nube.

---

## Configuración de Swagger

Swagger se configuró en `Program.cs` para documentar y probar la API desde el navegador.

```csharp
var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

app.UseSwagger();
app.UseSwaggerUI();

app.UseHttpsRedirection();

app.MapControllers();

app.Run();
```

Al ejecutar el proyecto, Swagger se puede abrir desde:

```text
https://localhost:PUERTO/swagger
```

o también:

```text
http://localhost:PUERTO/swagger
```

dependiendo de la configuración local.

---

## Ejemplo de Datos

Ejemplo de un pendiente en formato JSON:

```json
{
  "id": 1,
  "titulo": "Estudiar arquitectura de software",
  "descripcion": "Repasar ADR, API REST y Swagger",
  "fechaVencimiento": "2026-06-20T18:00:00",
  "completado": false,
  "prioridad": "Alta"
}
```

---

## Ejemplo de Petición POST

Para crear un pendiente nuevo se puede usar el endpoint:

```text
POST /api/pendientes
```

Con el siguiente cuerpo JSON:

```json
{
  "titulo": "Entregar actividad de API REST",
  "descripcion": "Subir el repositorio con Swagger funcionando",
  "fechaVencimiento": "2026-06-20T23:59:00",
  "completado": false,
  "prioridad": "Alta"
}
```

---

## ¿Cómo Ejecutar el Proyecto?

### 1. Clonar el repositorio

```bash
git clone https://github.com/Angel-Lugo97/Proyecto_ArqSoft.git
```

### 2. Entrar a la carpeta del proyecto

```bash
cd Proyecto_ArqSoft
```

### 3. Restaurar dependencias

```bash
dotnet restore
```

### 4. Compilar el proyecto

```bash
dotnet build
```

### 5. Ejecutar la API

```bash
dotnet run --project PendixApi/PendixApi.csproj
```

### 6. Abrir Swagger

Después de ejecutar el proyecto, abrir en el navegador:

```text
https://localhost:PUERTO/swagger
```

---

## Rutas Principales para Probar

```text
GET
/api/pendientes

GET
/api/pendientes/1

POST
/api/pendientes

PUT
/api/pendientes/1

PATCH
/api/pendientes/1/completar

DELETE
/api/pendientes/1
```

---

## Pruebas con Swagger

Swagger permite probar los endpoints sin necesidad de usar Postman u otra herramienta externa.

Desde Swagger se puede:

```text
- Ver todos los endpoints disponibles.
- Consultar los pendientes registrados.
- Crear un nuevo pendiente.
- Editar un pendiente existente.
- Marcar un pendiente como completado.
- Eliminar un pendiente.
- Revisar las respuestas HTTP de la API.
```

---

## Relación con el ADR

La incorporación de la API REST se documentó mediante un nuevo ADR, donde se explica la decisión de agregar un backend con ASP.NET Core Web API.

La decisión se tomó porque PendixApp necesita una forma más profesional de exponer sus funciones principales. REST es una opción adecuada porque las operaciones del sistema son sencillas y se relacionan directamente con acciones CRUD.

La API no reemplaza la aplicación móvil, sino que funciona como una extensión que permite preparar el proyecto para mejoras futuras.

---

## ¿Por qué REST?

Se eligió REST porque se adapta bien a las necesidades actuales del proyecto.

PendixApp necesita administrar pendientes, y esas operaciones pueden representarse de forma clara con métodos HTTP:

```text
GET     → Consultar pendientes
POST    → Crear pendientes
PUT     → Actualizar pendientes
PATCH   → Cambiar estado de un pendiente
DELETE  → Eliminar pendientes
```

REST también es fácil de probar, documentar y entender, por lo que es una buena opción para el alcance actual del proyecto.

---

## Alternativas Consideradas

| Alternativa | Motivo por el que no se eligió |
| :--- | :--- |
| **Solo SQLite local** | No permite exponer funciones mediante endpoints |
| **Firebase** | Agrega dependencia externa y configuración adicional |
| **GraphQL** | Es más flexible, pero más complejo para este proyecto |
| **Microservicios** | No se justifica para una app pequeña de pendientes |
| **Minimal API** | Es válida, pero se eligieron controladores por claridad y organización |

---

## Retos del Proyecto

Durante la incorporación de la API REST se presentaron algunos retos importantes:

```text
- Entender cómo separar la API del proyecto móvil.
- Definir endpoints claros y relacionados con PendixApp.
- Crear un modelo sencillo para representar los pendientes.
- Configurar ASP.NET Core Web API.
- Configurar Swagger correctamente.
- Probar los métodos GET, POST, PUT, PATCH y DELETE.
- Mantener la API en un alcance realista para el proyecto.
- Documentar la decisión mediante un ADR.
```

---

## Gestión con Git

Comandos básicos para subir los cambios al repositorio:

```bash
# Ver la rama actual
git branch

# Ver archivos modificados
git status

# Agregar cambios
git add .

# Crear commit
git commit -m "Agrega API REST para PendixApp con Swagger"

# Subir cambios
git push
```

Si es la primera vez que se sube la rama:

```bash
git push -u origin nombre-de-tu-rama
```

---

## Evidencias de Ejecución

En esta sección se pueden agregar capturas del proyecto funcionando.

### Swagger funcionando

Agregar captura de Swagger mostrando los endpoints de `PendientesController`.

```text
assets/swagger.png
```

```markdown
![Swagger funcionando](assets/swagger.png)
```

### Consulta de pendientes

Agregar captura del endpoint `GET /api/pendientes`.

```text
assets/get-pendientes.png
```

```markdown
![Consulta de pendientes](assets/get-pendientes.png)
```

### Creación de pendiente

Agregar captura del endpoint `POST /api/pendientes`.

```text
assets/post-pendiente.png
```

```markdown
![Creación de pendiente](assets/post-pendiente.png)
```

---

## Mejoras Futuras

```text
[ ] Conectar la API con SQLite.
[ ] Agregar validaciones en el modelo Pendiente.
[ ] Agregar usuarios para separar pendientes por persona.
[ ] Agregar autenticación básica.
[ ] Agregar filtros por prioridad.
[ ] Agregar filtros por pendientes completados o pendientes activos.
[ ] Agregar búsqueda por título.
[ ] Conectar la app Android con la API REST.
[ ] Agregar pruebas unitarias para los endpoints.
[ ] Publicar la API en un servicio externo.
```

---

## Conclusión

La incorporación de la API REST permite que PendixApp tenga una estructura más profesional y preparada para crecer. Aunque el proyecto inició como una aplicación móvil con almacenamiento local, la API permite exponer las funciones principales mediante endpoints documentados con Swagger.

Esta mejora ayuda a entender mejor cómo se comunican los clientes con un backend, cómo se organizan los endpoints y cómo se documenta una API en un proyecto de software.

El alcance se mantiene sencillo y realista, ya que la API se enfoca únicamente en la administración de pendientes, que es la función principal de PendixApp.

---

## Cláusula de IA

```text
Yo, Angel Abraham Lugo Saenz, declaro que utilicé IA como apoyo para organizar y redactar este README, así como para estructurar la documentación relacionada con la incorporación de una API REST en PendixApp.

El código, la estructura del proyecto y las decisiones principales fueron trabajadas como parte de la actividad escolar de Arquitectura de Software.
```
