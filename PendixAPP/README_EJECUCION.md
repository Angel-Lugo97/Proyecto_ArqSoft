# PendixAPP

Aplicación PendixAPP ejecutable con Java en el puerto 5018.

## Funcionalidades corregidas

```text
- Agregar pendientes.
- Modificar pendientes.
- Eliminar pendientes.
- Completar pendientes.
- Deshacer pendientes completados.
- Filtrar por Todos, Activos, Listos y Vencidos.
- Al seleccionar un filtro, baja automáticamente a Mis pendientes.
- Iniciar sesión y crear cuenta de forma simulada.
- Sección Calendario funcional.
- Sección Recordatorios funcional.
- Sección Ajustes funcional.
- Restaurar datos de ejemplo.
```

## Ejecutar rápido

Linux / Arch Linux:

```bash
bash iniciar_linux.sh
```

Mac:

```bash
bash iniciar_mac.command
```

Windows:

```bat
INICIAR_WINDOWS.bat
```

También se puede ejecutar directamente:

```bash
java -jar PendixApp.jar
```

Después abre en cualquier navegador:

```text
http://localhost:5018
```

## Código fuente

El código Java está en:

```text
src/PendixAppServer.java
```

## Compilar manualmente

```bash
mkdir -p out
javac --release 17 -encoding UTF-8 -d out src/PendixAppServer.java
jar cfe PendixApp.jar PendixAppServer -C out .
java -jar PendixApp.jar
```

## Detener servidor

En la terminal presiona:

```text
CTRL + C
```
