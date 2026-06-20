# ADR-06: Incorporación de API REST en PendixApp

| Campo  | Valor                    |
| ------ | ------------------------ |
| Autor  | Angel Abraham Lugo Saenz |
| Fecha  | 19/06/2026               |
| Estado | `APROBADO`               |

---

## Contexto

PendixApp es una aplicación móvil enfocada en la gestión de pendientes y recordatorios personales. En su primera versión, la aplicación funciona principalmente de forma local, permitiendo registrar pendientes, asignar fechas, consultar actividades, marcarlas como completadas y recibir recordatorios desde el dispositivo Android.

Sin embargo, para mejorar la organización del sistema y acercarlo a una forma de trabajo más profesional, se decidió incorporar una API REST. Esta API permitirá exponer las funciones principales de PendixApp mediante endpoints, facilitando la consulta, creación, edición y eliminación de pendientes desde un servicio externo.

La incorporación de la API no reemplaza la aplicación móvil, sino que funciona como una extensión del proyecto. La app Android podrá seguir existiendo como cliente, mientras que la API se encargará de administrar los datos de los pendientes de una forma más ordenada y documentada.

---

## Decisión

Se decidió desarrollar una API REST utilizando ASP.NET Core Web API. La API estará enfocada en la administración de pendientes de PendixApp.

Los endpoints principales serán:

| Método | Endpoint                         | Descripción                               |
| ------ | -------------------------------- | ----------------------------------------- |
| GET    | `/api/pendientes`                | Obtener todos los pendientes registrados  |
| GET    | `/api/pendientes/{id}`           | Obtener un pendiente específico por su ID |
| POST   | `/api/pendientes`                | Crear un nuevo pendiente                  |
| PUT    | `/api/pendientes/{id}`           | Actualizar la información de un pendiente |
| PATCH  | `/api/pendientes/{id}/completar` | Marcar un pendiente como completado       |
| DELETE | `/api/pendientes/{id}`           | Eliminar un pendiente                     |

La API será documentada con Swagger, para que los endpoints puedan visualizarse y probarse desde el navegador. Esto permitirá validar de forma clara que la API funciona correctamente y que cada endpoint cumple con su propósito.

---

## ¿Qué problema resuelve?

La API REST resuelve el problema de tener la lógica de los pendientes únicamente dentro de la aplicación móvil. Al agregar una API, las funciones principales del sistema quedan disponibles mediante endpoints, lo cual permite que PendixApp tenga una base más preparada para crecer.

También permite documentar mejor el sistema, ya que Swagger muestra de forma visual qué endpoints existen, qué datos reciben y qué respuestas devuelven.

Además, esta decisión prepara el proyecto para posibles mejoras futuras, como sincronización de datos, conexión con una base de datos externa o consumo de la API desde diferentes clientes.

---

## ¿Por qué REST?

Se eligió REST porque se adapta bien a las operaciones principales de PendixApp. El sistema necesita crear, consultar, actualizar y eliminar pendientes, y estas acciones se pueden representar de forma sencilla con métodos HTTP como GET, POST, PUT, PATCH y DELETE.

REST también es una opción adecuada porque es fácil de entender, fácil de probar y ampliamente utilizada en el desarrollo de APIs. Para el alcance actual del proyecto, permite implementar una solución funcional sin agregar una complejidad innecesaria.

---

## Alternativas consideradas

| Alternativa                                   | Por qué se descartó                                                                                                                     |
| --------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| Mantener solo almacenamiento local con SQLite | Es más simple, pero no permite exponer las funciones del sistema mediante endpoints.                                                    |
| Usar Firebase                                 | Puede servir para sincronización, pero agrega configuración externa y dependencia de un servicio en la nube.                            |
| Usar GraphQL                                  | Es flexible, pero demasiado complejo para las necesidades actuales de PendixApp.                                                        |
| Usar microservicios                           | No se justifica para una aplicación pequeña de pendientes personales.                                                                   |
| Usar Minimal API                              | Es una opción válida, pero se eligieron controladores porque permiten organizar mejor los endpoints y documentarlos de forma más clara. |

---

## Consecuencias

**Lo que gano:**

La aplicación queda mejor preparada para crecer, ya que las funciones principales de los pendientes pueden exponerse mediante una API.

También se obtiene una documentación más profesional mediante Swagger, lo que facilita probar y validar los endpoints.

Además, el proyecto se acerca más a una estructura utilizada en la industria, donde una aplicación cliente consume información desde una API.

**Lo que sacrifico o asumo:**

El proyecto aumenta un poco su complejidad, porque ahora no solo existe la aplicación móvil, sino también un backend.

También será necesario mantener organizados los endpoints, modelos y controladores para evitar que la API crezca de forma desordenada.

Por ahora, la API se implementará con funciones básicas de pendientes. Características más avanzadas como autenticación, usuarios, sincronización en la nube o notificaciones desde servidor se dejarán como posibles mejoras futuras.

### Clausula de IA
Yo Angel Abraham Lugo Saenz declaro que utilice la IA para ayudarme a estructurar las ideas y planificar como implementare el API Rest solicitado por el profesor.

