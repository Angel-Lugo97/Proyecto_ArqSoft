# ADR-07: Integración de Patrones GOF en PendixApp

| Campo  | Valor |
| ------ | ----- |
| Autor  | Angel Abraham Lugo Saenz |
| Fecha  | 26/06/2026 |
| Estado | `ADR actualizado por ADR-07` |

---

## Contexto

PendixApp es una aplicación móvil enfocada en la gestión de pendientes y recordatorios personales. En avances anteriores, el sistema se planteó como una aplicación Android con almacenamiento local y posteriormente se decidió incorporar una API REST para exponer las funciones principales del sistema mediante endpoints.

La API REST de PendixApp permite administrar pendientes mediante operaciones como consultar, crear, actualizar, completar y eliminar registros. Esta decisión ayuda a que el proyecto tenga una estructura más profesional y pueda crecer en el futuro.

Ahora que el sistema empieza a tomar más forma, es necesario integrar patrones de diseño GOF que ayuden a organizar mejor el código sin hacerlo demasiado complejo. Como el proyecto sigue siendo de alcance escolar y debe ser fácil de implementar, se eligieron patrones sencillos, reales para el sistema y útiles para mejorar la estructura de la API.

---

## Decisión

Se decidió integrar dos patrones de diseño GOF en PendixApp:

| Patrón GOF | Categoría | Uso dentro de PendixApp |
| ---------- | --------- | ----------------------- |
| **Facade** | Estructural | Centralizar las operaciones principales de pendientes en una clase de servicio |
| **Strategy** | Comportamiento | Separar las formas de filtrar pendientes, como todos, activos, completados o vencidos |

Estos patrones fueron elegidos porque son fáciles de implementar, no agregan una complejidad exagerada y sí resuelven problemas reales dentro del proyecto.

Además, cumplen con la restricción de la actividad, ya que pertenecen a **categorías distintas**:

```text
Facade   -> Patrón estructural
Strategy -> Patrón de comportamiento
```

---

## Problema General

Al crecer PendixApp con una API REST, existe el riesgo de que todo el código se concentre en los controladores.

Por ejemplo, el controlador de pendientes podría terminar haciendo demasiadas tareas:

```text
- Recibir peticiones HTTP.
- Validar datos.
- Crear pendientes.
- Actualizar pendientes.
- Marcar pendientes como completados.
- Filtrar pendientes.
- Decidir qué respuesta devolver.
```

Si todo eso se deja dentro del controlador, el código se vuelve más difícil de leer, modificar y mantener.

Por eso se integran patrones GOF sencillos que ayudan a separar responsabilidades.

---

## Patrón 1: Facade

### Categoría

```text
Estructural
```

### ¿Dónde se aplica?

El patrón **Facade** se aplicará en una clase de servicio llamada, por ejemplo:

```text
PendienteFacade
```

Esta clase se encargará de concentrar las operaciones principales relacionadas con los pendientes.

El controlador ya no tendría que manejar toda la lógica directamente, sino que llamaría a la fachada para realizar las acciones necesarias.

---

### ¿Qué problema resuelve?

Facade resuelve el problema de tener controladores demasiado cargados.

En lugar de que el controlador haga todo el proceso, la fachada se encarga de coordinar las operaciones principales:

```text
Controlador
     ↓
PendienteFacade
     ↓
Lógica de pendientes / almacenamiento / filtros
```

Esto permite que el controlador se enfoque principalmente en recibir la petición y devolver una respuesta.

---

### Ejemplo de uso en PendixApp

Sin Facade, el controlador podría tener demasiada lógica.

Con Facade, el controlador solo llama métodos sencillos como:

```text
- ObtenerPendientes()
- ObtenerPendientePorId(id)
- CrearPendiente(pendiente)
- ActualizarPendiente(id, pendiente)
- CompletarPendiente(id)
- EliminarPendiente(id)
```

Ejemplo simple:

```csharp
public class PendienteFacade
{
    private readonly List<Pendiente> _pendientes;

    public PendienteFacade()
    {
        _pendientes = new List<Pendiente>();
    }

    public List<Pendiente> ObtenerPendientes()
    {
        return _pendientes;
    }

    public Pendiente CrearPendiente(Pendiente pendiente)
    {
        pendiente.Id = _pendientes.Count + 1;
        pendiente.Completado = false;
        _pendientes.Add(pendiente);
        return pendiente;
    }

    public bool CompletarPendiente(int id)
    {
        var pendiente = _pendientes.FirstOrDefault(p => p.Id == id);

        if (pendiente == null)
        {
            return false;
        }

        pendiente.Completado = true;
        return true;
    }
}
```

Este ejemplo se mantiene sencillo porque el objetivo no es hacer una arquitectura complicada, sino organizar mejor el código.

---

### ¿Por qué se eligió Facade?

Se eligió Facade porque ayuda a mantener el proyecto ordenado sin exigir una estructura difícil.

También permite que, si después se cambia el almacenamiento de datos o se agregan más validaciones, el controlador no tenga que modificarse demasiado.

Facade es adecuado para PendixApp porque el sistema tiene varias operaciones relacionadas con pendientes y conviene agruparlas en un punto claro.

---

## Patrón 2: Strategy

### Categoría

```text
Comportamiento
```

### ¿Dónde se aplica?

El patrón **Strategy** se aplicará para manejar diferentes formas de filtrar pendientes.

Por ejemplo, PendixApp puede necesitar mostrar:

```text
- Todos los pendientes.
- Pendientes activos.
- Pendientes completados.
- Pendientes vencidos.
```

En lugar de escribir muchos `if` o `switch` dentro del controlador, cada filtro puede manejarse como una estrategia separada.

---

### ¿Qué problema resuelve?

Strategy resuelve el problema de tener muchas condiciones para filtrar pendientes.

Sin este patrón, el código podría crecer así:

```csharp
if (filtro == "todos")
{
    // mostrar todos
}
else if (filtro == "activos")
{
    // mostrar activos
}
else if (filtro == "completados")
{
    // mostrar completados
}
else if (filtro == "vencidos")
{
    // mostrar vencidos
}
```

Esto funciona al principio, pero si se agregan más filtros, el código se vuelve más largo y menos ordenado.

Con Strategy, cada filtro tiene su propia clase y responsabilidad.

---

### Ejemplo de uso en PendixApp

Primero se puede crear una interfaz sencilla:

```csharp
public interface IFiltroPendientesStrategy
{
    List<Pendiente> Filtrar(List<Pendiente> pendientes);
}
```

Después se crean estrategias concretas:

```csharp
public class FiltroCompletadosStrategy : IFiltroPendientesStrategy
{
    public List<Pendiente> Filtrar(List<Pendiente> pendientes)
    {
        return pendientes.Where(p => p.Completado).ToList();
    }
}
```

```csharp
public class FiltroActivosStrategy : IFiltroPendientesStrategy
{
    public List<Pendiente> Filtrar(List<Pendiente> pendientes)
    {
        return pendientes.Where(p => !p.Completado).ToList();
    }
}
```

Luego la fachada o servicio puede usar la estrategia necesaria según el filtro solicitado.

Ejemplo de endpoint futuro:

```text
GET /api/pendientes?filtro=completados
GET /api/pendientes?filtro=activos
GET /api/pendientes?filtro=vencidos
```

---

### ¿Por qué se eligió Strategy?

Se eligió Strategy porque PendixApp necesita consultar pendientes de diferentes formas.

Este patrón permite agregar nuevos filtros sin modificar demasiado el código existente.

También es fácil de entender para el nivel actual del proyecto, porque se basa en una idea sencilla: cada clase representa una forma diferente de filtrar la información.

---

## Relación con la API REST

La API REST de PendixApp seguirá manejando los endpoints principales definidos anteriormente:

| Método | Endpoint | Descripción |
| ------ | -------- | ----------- |
| GET | `/api/pendientes` | Obtener todos los pendientes registrados |
| GET | `/api/pendientes/{id}` | Obtener un pendiente específico por su ID |
| POST | `/api/pendientes` | Crear un nuevo pendiente |
| PUT | `/api/pendientes/{id}` | Actualizar la información de un pendiente |
| PATCH | `/api/pendientes/{id}/completar` | Marcar un pendiente como completado |
| DELETE | `/api/pendientes/{id}` | Eliminar un pendiente |

Con los patrones GOF, la organización interna puede quedar así:

```text
PendientesController
        ↓
PendienteFacade
        ↓
IFiltroPendientesStrategy
        ↓
FiltroActivosStrategy
FiltroCompletadosStrategy
FiltroVencidosStrategy
```

Esto ayuda a que el proyecto no dependa de un controlador lleno de lógica.

---

## Estructura propuesta del proyecto

Una estructura sencilla para implementar estos patrones podría ser:

```text
PendixApp.Api/
│
├── Controllers/
│   └── PendientesController.cs
│
├── Models/
│   └── Pendiente.cs
│
├── Facades/
│   └── PendienteFacade.cs
│
├── Strategies/
│   ├── IFiltroPendientesStrategy.cs
│   ├── FiltroTodosStrategy.cs
│   ├── FiltroActivosStrategy.cs
│   ├── FiltroCompletadosStrategy.cs
│   └── FiltroVencidosStrategy.cs
│
└── Program.cs
```

Esta estructura es simple y suficiente para un avance escolar.

No se busca implementar una arquitectura demasiado avanzada, sino aplicar patrones GOF de forma clara y entendible.

---

## Alternativas consideradas

| Alternativa | Por qué se descartó |
| ----------- | ------------------- |
| **Singleton** | No se eligió porque ASP.NET Core ya maneja servicios mediante inyección de dependencias y usar Singleton manualmente podría complicar el proyecto |
| **Factory Method** | Se consideró para crear pendientes, pero por ahora la creación de objetos es sencilla y no requiere una fábrica |
| **Observer** | Se consideró por el tema de notificaciones, pero todavía no se necesita un sistema de eventos o suscriptores |
| **Decorator** | No se eligió porque PendixApp aún no necesita agregar comportamientos dinámicos a los pendientes |
| **Command** | Podría servir para acciones como crear o eliminar pendientes, pero sería más complejo de lo necesario para el alcance actual |
| **Repository** | Aunque es útil, no es un patrón GOF, por lo que no cumple directamente con la instrucción de la actividad |

---

## Consecuencias

### Lo que gano

Con **Facade**, el código queda más ordenado porque el controlador no concentra toda la lógica del sistema.

Con **Strategy**, los filtros de pendientes quedan separados y pueden crecer sin llenar el controlador de condiciones.

Ambos patrones ayudan a que PendixApp tenga una estructura más limpia y fácil de mantener.

---

### Consecuencia técnica

El proyecto tendrá más clases, pero cada una tendrá una responsabilidad más clara.

Esto facilita entender qué parte del sistema se encarga de recibir peticiones, cuál coordina las operaciones y cuál aplica los filtros.

También será más sencillo agregar nuevos filtros en el futuro, como:

```text
- Pendientes por fecha.
- Pendientes por prioridad.
- Pendientes vencidos.
- Pendientes completados.
```

---

### Consecuencia sobre el proceso

Estos patrones son fáciles de implementar y no requieren cambiar completamente el proyecto.

Permiten avanzar con una solución realista para un estudiante de tercer cuatrimestre, manteniendo el sistema funcional y entendible.

Además, ayudan a demostrar que PendixApp ya no es solo una idea, sino un sistema que empieza a tener organización interna.

---

## Lo que sacrifico o asumo

Al integrar estos patrones, se agregan más archivos y clases al proyecto.

Esto puede parecer más trabajo al principio, pero ayuda a evitar que el código crezca de forma desordenada.

También se asume que los patrones se implementarán de forma sencilla, sin intentar hacer una arquitectura demasiado avanzada.

El objetivo es aplicar los patrones donde realmente aportan valor y no usarlos solo por cumplir.

---

## Decisión final

Se decidió integrar los patrones **Facade** y **Strategy** en PendixApp.

Facade se usará para centralizar las operaciones principales de pendientes y evitar controladores con demasiada lógica.

Strategy se usará para separar las formas de filtrar pendientes y evitar condicionales grandes dentro del controlador o servicio.

Estos dos patrones son adecuados para el alcance actual del proyecto porque son sencillos, pertenecen a categorías GOF distintas y resuelven problemas reales dentro del sistema.

---

## Clausula de IA

Yo Angel Abraham Lugo Saenz declaro que utilicé IA como apoyo para organizar y redactar este ADR, así como para estructurar la explicación de los patrones GOF aplicados a PendixApp.

El contenido principal, las decisiones del proyecto y la documentación del ADR fueron trabajados como parte de la actividad escolar de Arquitectura de Software.
