# PendixAPP

Aplicación web local construida con Java 21 y Gradle. Se ejecuta en el puerto `5018`, cuenta con diseño adaptable para computadora y teléfono, y puede publicarse temporalmente mediante Cloudflare Quick Tunnel.

## Funcionalidades

```text
- Agregar pendientes.
- Modificar pendientes.
- Eliminar pendientes.
- Completar pendientes.
- Deshacer pendientes completados.
- Filtrar por Todos, Activos, Listos y Vencidos.
- Iniciar sesión y crear cuenta de forma simulada.
- Sección Calendario.
- Sección Recordatorios.
- Sección Ajustes.
- Restaurar datos de ejemplo.
- Vista adaptable para teléfonos reales.
- Recarga automática después de cambios de código.
- Publicación HTTPS temporal con Cloudflare Quick Tunnel.
```

## Ejecutar solamente en local

Linux / Arch Linux:

```bash
bash iniciar_linux.sh
```

También se puede ejecutar el JAR compilado:

```bash
./gradlew clean jar
PENDIX_OPEN_BROWSER=false java -jar build/libs/PendixAPP-1.0.0.jar
```

Abrir:

```text
http://localhost:5018
```

## Publicar temporalmente en internet

Instalar requisitos en Arch Linux:

```bash
sudo pacman -S --needed jdk21-openjdk curl cloudflared
```

Ejecutar:

```bash
chmod +x serve.sh
./serve.sh
```

El script imprimirá una URL similar a:

```text
https://nombre-aleatorio.trycloudflare.com
```

Mantener la terminal abierta. Para detener todo:

```text
Ctrl+C
```

La guía completa está en:

```text
docs/PUBLICACION_CLOUDFLARE.md
```

## Estructura principal

```text
src/main/java/PendixAppServer.java        Servidor HTTP y respuestas comprimidas
src/main/java/PendixRouter.java           Enrutamiento web, API, health y version
src/main/java/StaticResourceService.java  Carga HTML, CSS y JavaScript
src/main/resources/static/index.html      Estructura de la interfaz
src/main/resources/static/styles.css      Diseño y adaptación móvil
src/main/resources/static/app.js          Interacciones y recarga automática
serve.sh                                  Build, servidor, túnel y vigilancia
```

## Persistencia

Los pendientes y la sesión simulada se guardan en `localStorage`. Cada navegador tiene sus propios datos; no existe una base de datos compartida en el servidor.

## Pruebas automatizadas

```bash
./gradlew clean test
```

Reporte HTML:

```text
build/reports/tests/test/index.html
```

## Detener servidor local

En la terminal:

```text
Ctrl+C
```
