# Modelo C4 — PendixAPP

## C4 Nivel 1 — Contexto

```mermaid
flowchart LR
    Usuario["Persona: Usuario"]
    PendixAPP["Sistema: PendixAPP<br/>Gestiona pendientes desde un navegador"]
    Cloudflare["Sistema externo: Cloudflare Quick Tunnel<br/>Entrega una URL HTTPS temporal"]

    Usuario -->|"Usa desde computadora o teléfono"| Cloudflare
    Cloudflare -->|"Reenvía solicitudes HTTPS"| PendixAPP
```

## C4 Nivel 2 — Contenedores

```mermaid
flowchart LR
    Usuario["Usuario"]

    subgraph DispositivoVisitante["Dispositivo del visitante"]
        Navegador["Navegador<br/>HTML, CSS y JavaScript"]
        LocalStorage["localStorage<br/>Pendientes y sesión simulada"]
    end

    subgraph ComputadoraLocal["Computadora anfitriona"]
        Cloudflared["cloudflared<br/>Quick Tunnel"]
        JavaServer["PendixAppServer<br/>Java HttpServer :5018"]
        Recursos["Recursos estáticos<br/>index.html, styles.css y app.js"]
        Api["Rutas HTTP<br/>/api/pendientes, /health y /version"]
    end

    Usuario --> Navegador
    Navegador -->|"HTTPS"| Cloudflared
    Cloudflared -->|"HTTP local"| JavaServer
    JavaServer --> Recursos
    JavaServer --> Api
    Navegador --> LocalStorage
```

## C4 Nivel 3 — Componentes

```mermaid
flowchart TB
    Main["PendixAppServer<br/>Inicia y configura HttpServer"]
    Handler["PendixHandler<br/>Recibe solicitudes y envía respuestas"]
    Router["PendixRouter<br/>Resuelve rutas"]
    Static["StaticResourceService<br/>Carga recursos del classpath"]
    Gzip["GzipSupport<br/>Comprime contenido textual"]
    Domain["Pendiente y PendienteService<br/>Modelo y API de ejemplo"]
    Html["index.html<br/>Estructura visual"]
    Css["styles.css<br/>Diseño responsive"]
    Js["app.js<br/>Interacciones, localStorage y recarga"]
    Script["serve.sh<br/>Build, procesos, túnel y vigilancia"]

    Script --> Main
    Main --> Handler
    Handler --> Router
    Handler --> Gzip
    Router --> Static
    Router --> Domain
    Static --> Html
    Static --> Css
    Static --> Js
```

## Nota de persistencia

PendixAPP no utiliza una base de datos compartida. La interfaz guarda pendientes y sesión simulada en `localStorage`, por lo que cada navegador conserva información independiente.
