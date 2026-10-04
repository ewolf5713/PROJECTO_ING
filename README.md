# RUREX-UCV

![Java](https://img.shields.io/badge/Java-17-orange)
![Maven](https://img.shields.io/badge/Maven-3.8+-blue)
![JUnit](https://img.shields.io/badge/JUnit-5.10-green)
![Swing](https://img.shields.io/badge/UI-Swing-lightgrey)

Sistema de gestion de transporte universitario para la comunidad de la Universidad Central de Venezuela (UCV).
Proyecto de Ingenieria de Software, desarrollado con XP y arquitectura MVC en Java Swing.

## Tabla de contenido

- [Features](#features)
- [Tech stack](#tech-stack)
- [Getting started](#getting-started)
- [Cuentas demo](#cuentas-demo)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Tests](#tests)
- [Contribuir](#contribuir)
- [Equipo](#equipo)
- [Licencia](#licencia)

## Features

Alcance del Incremento 1:

| Modulo | Historias de usuario | Que hace |
|---|---|---|
| Acceso y Autenticacion | HU-001, HU-002 | Registro con password hasheado (PBKDF2) y login con panel segun rol: administrador, pasajero (estudiante o empleado) y conductor |
| Gestion de Flota | HU-022, HU-023, HU-024 | Registro de unidades con placa unica, listado con filtro por estado operativo y edicion de modelo, capacidad y estado |
| Control de Itinerarios | HU-010 | Crear y eliminar itinerarios con unidades activas, limite de cupos por capacidad y bloqueo de cruces de horario por unidad o conductor |

Extra: demo de estado del recorrido (HU-008) dentro de los paneles de pasajero y conductor.

## Tech stack

- Java 17 (OpenJDK)
- Swing para las interfaces graficas
- Maven para build y paquetes
- JUnit 5 para pruebas unitarias
- Git y GitHub para control de versiones

## Getting started

### Descargar la app

Para usar la app sin compilar nada, descarga el paquete de Windows o Linux desde [Releases](https://github.com/ewolf5713/PROJECTO_ING/releases/latest). Los pasos estan en [INSTALL.md](INSTALL.md).

### Requisitos

- JDK 17
- Maven 3.8 o superior

### Instalacion

```bash
git clone https://github.com/ewolf5713/PROJECTO_ING.git
cd PROJECTO_ING
mvn clean install
```

### Ejecutar la app

```bash
mvn compile exec:java -Dexec.mainClass="com.rurex.Main"
```

O con el jar:

```bash
mvn package
java -jar target/rurex-transporte-1.0-SNAPSHOT.jar
```

### Demo del recorrido (HU-008)

```bash
mvn compile && java -cp target/classes com.rurex.TripDemo
```

## Cuentas demo

| Rol | Correo | Password |
|---|---|---|
| Administrador | `admin@ucv.ve` | `Admin123` |
| Estudiante | `estudiante@ucv.ve` | `Estudiante123` |
| Empleado | `empleado@ucv.ve` | `Empleado123` |
| Conductor | `conductor@ucv.ve` | `Conductor123` |

Los datos viven en memoria, al cerrar la app se pierden los registros nuevos.

## Estructura del proyecto

```
src/main/java/com/rurex
├── Main.java          punto de entrada
├── model/             entidades: User, TransportUnit, Itinerary, Trip
├── service/           reglas de negocio y validaciones
├── controller/        conecta las vistas con los services
└── view/              pantallas Swing y estilos compartidos
src/test/java/com/rurex/service   pruebas unitarias JUnit
```

## Tests

```bash
mvn clean test
```

Las pruebas cubren registro y login, registro y filtrado de flota, cambio de estado de unidades, creacion de itinerarios, cruces de horario y el estado del recorrido.

## Contribuir

1. Crear una rama desde `main` con el formato `feature/HU-XXX-descripcion`.
2. Hacer commits en ingles con el formato `tipo(HU-XXX): descripcion`, por ejemplo `feat(HU-022): add transport unit registration`.
3. Correr `mvn clean test` antes de subir.
4. Abrir un Pull Request hacia `main` y pedir review a otro integrante.

No subir archivos por la web de GitHub ni dejar clases fuera de `src/`.

## Equipo

Equipo 1, Seccion C2, Ingenieria de Software, UCV.

- Daniel Quiaro
- Enrique Rubio
- Sebastian Pirela
- Victor Castro

## Licencia

Proyecto academico de la Escuela de Computacion, Facultad de Ciencias, UCV. Uso educativo.
