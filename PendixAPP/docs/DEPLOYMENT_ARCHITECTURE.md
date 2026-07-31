# Arquitectura de despliegue final de PendixAPP

## Frontend

El frontend será separado del servidor Java y publicado como archivos estáticos
HTML, CSS y JavaScript mediante GitHub Pages.

## Backend

El backend Java será empaquetado como una aplicación ejecutable y desplegado
en AWS. La configuración sensible se proporcionará mediante variables de entorno.

## Base de datos

Los pendientes serán almacenados en PostgreSQL alojado en Amazon RDS.
PgAdmin será utilizado como cliente para administrar y poblar la base de datos.

## Comunicación

El frontend consumirá la API mediante HTTPS. El backend permitirá únicamente
el origen autorizado de GitHub Pages mediante la configuración CORS.

## CI/CD

GitHub Actions realizará las siguientes etapas:

1. Validar los archivos del frontend.
2. Compilar el backend.
3. Ejecutar las pruebas automatizadas.
4. Solicitar autorización para producción.
5. Desplegar el backend en AWS.
6. Publicar el frontend en GitHub Pages.

Los despliegues solamente se ejecutarán cuando todas las validaciones hayan
terminado correctamente.
