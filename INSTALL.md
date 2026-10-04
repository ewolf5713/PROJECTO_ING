# Instalar RUREX-UCV

Descarga los archivos desde la seccion **Releases** del repositorio:
https://github.com/ewolf5713/PROJECTO_ING/releases/latest

| Archivo | Para | Necesita Java |
|---|---|---|
| `RUREX-windows.zip` | Windows 10 u 11 | No |
| `RUREX-linux.tar.gz` | Linux de 64 bits | No |
| `RUREX.jar` | Windows, Linux o macOS | Si, Java 17 o superior |

## Windows

1. Descarga `RUREX-windows.zip`.
2. Clic derecho sobre el archivo y elige **Extraer todo**.
3. Abre la carpeta `RUREX` que se creo.
4. Doble clic en `RUREX.exe`.

Si aparece **Windows protegió su PC**, haz clic en **Más información** y luego en **Ejecutar de todas formas**. Sale porque la app no tiene firma digital.

No muevas `RUREX.exe` fuera de su carpeta: necesita las carpetas `app` y `runtime` que estan al lado.

## Linux

1. Descarga `RUREX-linux.tar.gz`.
2. Abre una terminal en la carpeta de descargas y ejecuta:

```bash
tar -xzf RUREX-linux.tar.gz
./RUREX/bin/RUREX
```

## Cualquier sistema con Java 17

1. Revisa que tienes Java 17 o superior: `java -version`
2. Descarga `RUREX.jar` y ejecuta:

```bash
java -jar RUREX.jar
```

## Cuentas demo

| Rol | Correo | Clave |
|---|---|---|
| Administrador | `admin@ucv.ve` | `Admin123` |
| Estudiante | `estudiante@ucv.ve` | `Estudiante123` |
| Empleado | `empleado@ucv.ve` | `Empleado123` |
| Conductor | `conductor@ucv.ve` | `Conductor123` |

Los datos se guardan en memoria. Al cerrar la app se pierde lo creado y vuelven los datos de ejemplo.
