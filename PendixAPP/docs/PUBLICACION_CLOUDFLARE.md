# Publicación local de PendixAPP con Cloudflare Quick Tunnel

## Arquitectura de publicación

PendixAPP continúa ejecutándose en la computadora local mediante Java 21 y el puerto `5018`. `cloudflared` crea una conexión saliente hacia Cloudflare y entrega una URL temporal con HTTPS bajo el dominio `trycloudflare.com`.

No se copia el proyecto a un hosting ni se requiere abrir puertos del router.

## Requisitos en Arch Linux

```bash
sudo pacman -Syu
sudo pacman -S --needed jdk21-openjdk curl cloudflared
```

Comprobar versiones:

```bash
java --version
cloudflared --version
curl --version
```

## Activar la publicación

Desde la raíz del proyecto:

```bash
chmod +x serve.sh
./serve.sh
```

El script realiza estas tareas:

1. Construye el JAR de producción con Gradle.
2. Inicia PendixAPP en `http://127.0.0.1:5018`.
3. Abre un Cloudflare Quick Tunnel.
4. Imprime la URL pública HTTPS.
5. Verifica la página principal, `/health`, `styles.css` y `app.js`.
6. Vigila los archivos de `src/`, `build.gradle` y `settings.gradle`.
7. Recompila y reinicia solamente Java cuando detecta cambios.
8. Conserva vivo el túnel para no cambiar la URL durante la sesión.
9. Hace que los navegadores abiertos se recarguen al detectar una nueva versión.

## Desactivar

En la terminal donde se ejecuta `serve.sh`:

```text
Ctrl+C
```

El `trap` del script detiene el proceso Java y `cloudflared`.

## Comprobar si sigue encendido

```bash
curl -i http://127.0.0.1:5018/health
pgrep -af 'PendixAPP-1.0.0.jar'
pgrep -af 'cloudflared tunnel'
ss -lntp | grep ':5018'
```

La respuesta esperada del endpoint de salud es:

```json
{"status":"ok"}
```

## Apagado forzado

Usar solamente si se cerró la terminal de forma incorrecta y quedaron procesos activos:

```bash
pkill -f 'cloudflared tunnel'
pkill -f 'PendixAPP-1.0.0.jar'
pkill -f 'serve.sh'
```

Comprobar que el puerto quedó libre:

```bash
ss -lntp | grep ':5018' || echo 'Puerto 5018 libre'
```

## Verificación manual de la URL pública

Sustituir `<URL_PUBLICA>` por la dirección impresa por `serve.sh`:

```bash
curl -i '<URL_PUBLICA>/'
curl -i '<URL_PUBLICA>/health'

curl -sS -D /tmp/pendix-css.headers \
  -o /tmp/pendix-styles.css \
  --compressed \
  -H 'Accept-Encoding: gzip, br' \
  '<URL_PUBLICA>/styles.css'

wc -c /tmp/pendix-styles.css
grep -iE 'content-type|content-encoding|content-length' /tmp/pendix-css.headers

curl -sS -D /tmp/pendix-js.headers \
  -o /tmp/pendix-app.js \
  --compressed \
  -H 'Accept-Encoding: gzip, br' \
  '<URL_PUBLICA>/app.js'

wc -c /tmp/pendix-app.js
grep -iE 'content-type|content-encoding|content-length' /tmp/pendix-js.headers
```

Los archivos descargados deben tener más de cero bytes. El CSS debe responder con `text/css` y JavaScript con `application/javascript`.

## Persistencia actual

PendixAPP no usa SQLite ni una base de datos del servidor. Los pendientes y la sesión simulada se guardan con `localStorage` dentro de cada navegador.

Consecuencias:

- Cada teléfono o computadora tiene su propia lista de pendientes.
- Los cambios de un visitante no aparecen en los demás dispositivos.
- Limpiar los datos del navegador elimina la información de ese dispositivo.
- El túnel y la computadora local no almacenan una copia central de esos pendientes.

Para contar con datos centralizados en la computadora anfitriona se necesitaría implementar una API CRUD real y persistencia local en SQLite, PostgreSQL o un archivo administrado por el servidor.

## Limitaciones

- La URL funciona únicamente mientras la computadora y `serve.sh` estén encendidos.
- La dirección de Quick Tunnel cambia cada vez que se inicia una sesión nueva.
- El rendimiento depende de la conexión a internet de la computadora anfitriona.
- Quick Tunnel es apropiado para pruebas y demostraciones, no para producción con garantía de disponibilidad.
- No hay SLA ni una URL reservada.
- La autenticación actual es simulada y no debe considerarse seguridad real.
- Los datos están en el navegador de cada visitante, no en una base de datos compartida.

## URL permanente

Para conservar una misma dirección se necesita:

1. Una cuenta gratuita de Cloudflare.
2. Un dominio administrado en Cloudflare.
3. Crear un túnel con nombre.
4. Asociar un hostname del dominio al servicio local `http://127.0.0.1:5018`.
5. Ejecutar el túnel mediante su token o archivo de configuración.

Esta opción mantiene el servidor en la computadora local, pero sustituye la URL aleatoria por un hostname estable.
