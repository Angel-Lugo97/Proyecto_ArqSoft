# ADR-09 — Publicación local mediante Cloudflare Quick Tunnel

- **Estado:** Aceptado
- **Fecha:** 2026-07-31

## Contexto

PendixAPP funciona como un servidor HTTP local construido con Java 21. Para la demostración final se necesita compartir la aplicación con personas conectadas desde otra red, sin contratar infraestructura, mover el código a un hosting o abrir puertos en el router.

La interfaz estaba incrustada dentro de `PendixAppServer.java`, el servidor utilizaba un puerto fijo y no existía un proceso automatizado para compilar, iniciar, publicar, verificar y reiniciar la aplicación.

## Decisión

Se decidió utilizar Cloudflare Quick Tunnel con `cloudflared` para exponer temporalmente `http://127.0.0.1:5018` mediante una URL HTTPS aleatoria de `trycloudflare.com`.

Además:

- HTML, CSS y JavaScript se separan en recursos estáticos.
- El servidor permite configurar host, puerto y apertura del navegador mediante variables de entorno.
- Se agrega `/health` para comprobar disponibilidad.
- Se agrega `/version` para que el navegador detecte reinicios y recargue la interfaz.
- Se habilita compresión GZIP para contenido textual.
- `serve.sh` construye el JAR, ejecuta Java, mantiene el túnel vivo y vigila cambios de código.
- `Ctrl+C` detiene de forma coordinada la aplicación y el túnel.

## Alternativas consideradas

### Hosting gratuito externo

Se descartó porque movería la aplicación fuera de la computadora local y algunos servicios requieren cuenta, límites de uso o tarjeta.

### Abrir el puerto del router

Se descartó por seguridad, configuración de red, dependencia del proveedor de internet y necesidad de administrar certificados HTTPS.

### Túnel permanente con dominio propio

Es una alternativa válida, pero requiere cuenta de Cloudflare y un dominio. Para la demostración actual se prefiere Quick Tunnel por su menor configuración.

## Consecuencias positivas

- La app puede abrirse desde otra red mediante HTTPS.
- No se necesita una IP pública ni configurar el router.
- La URL permanece estable mientras `serve.sh` continúa ejecutándose.
- Los cambios del código reinician solamente Java y conservan el túnel.
- La interfaz se adapta a teléfonos reales.
- Los recursos CSS y JavaScript se pueden verificar por separado.

## Consecuencias negativas

- La URL cambia después de detener y volver a iniciar el túnel.
- La disponibilidad depende de que la computadora esté encendida.
- El rendimiento depende de la red local.
- Quick Tunnel no ofrece SLA.
- La autenticación de PendixAPP continúa siendo simulada.
- La persistencia continúa en `localStorage`, por lo que cada navegador mantiene datos independientes.
