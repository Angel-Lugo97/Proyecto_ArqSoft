
## C4 Nivel 2 — Contenedores

**Para quién es:**  
Este nivel está dirigido a personas con conocimientos técnicos básicos, ya que muestra las piezas principales del sistema y cómo se comunican.

**Qué pregunta responde:**  
¿Cuáles son las partes grandes de PendixAPP y cómo interactúan entre sí?

```mermaid
flowchart LR
    Usuario["Persona: Usuario"]

    subgraph Dispositivo["Computadora del usuario"]
        Navegador["Contenedor: Navegador web<br/>Ejecuta la interfaz HTML, CSS y JavaScript"]
        LocalStorage["Contenedor: localStorage<br/>Guarda pendientes y sesión simulada en el navegador"]
    end

    subgraph ServidorLocal["Servidor local Java"]
        JavaServer["Contenedor: PendixAppServer<br/>Servidor HTTP local en Java usando HttpServer"]
        HtmlApp["Contenedor: Interfaz web incrustada<br/>HTML, CSS y JavaScript dentro del archivo Java"]
        ApiSimulada["Contenedor: API simulada<br/>Ruta /api/pendientes"]
    end

    Usuario -->|"Usa la aplicación"| Navegador
    Navegador -->|"Solicita http://localhost:5018"| JavaServer
    JavaServer -->|"Entrega HTML/CSS/JS"| HtmlApp
    HtmlApp -->|"Se ejecuta en"| Navegador
    Navegador -->|"Guarda y consulta datos"| LocalStorage
    Navegador -->|"Puede consultar"| ApiSimulada
```
