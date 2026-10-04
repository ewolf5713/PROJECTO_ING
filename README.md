# RUREX-UCV

Sistema de transporte universitario para la comunidad UCV.
Incremento 1 desarrollado bajo metodologia XP con arquitectura MVC en Java Swing.

## Requerimientos
- Java 17 (OpenJDK)
- Maven 3.8+

## Modulos implementados (Sprint 1)
- Registro e inicio de sesion (estudiantes, empleados, admin) con hash PBKDF2
- Gestion de unidades de transporte (registro, listado, filtro y cambio de estado)
- Gestion y control de itinerarios con asignacion de unidades activas

## Compilar y correr pruebas
```bash
mvn clean test
```

## Ejecutar la app
```bash
mvn compile exec:java -Dexec.mainClass="com.rurex.Main"
```
o empaquetar el jar:
```bash
mvn package
java -jar target/rurex-transporte-1.0-SNAPSHOT.jar
```

## Demo de estado de recorrido (HU-008)

La demo Swing es independiente de la aplicación principal y usa un verificador de reserva en memoria:
```bash
mvn compile && java -cp target/classes com.rurex.TripDemo
```

## Cuentas demo para pruebas
- Admin: `admin@ucv.ve` / `Admin123`
- Estudiante: `estudiante@ucv.ve` / `Estudiante123`
- Empleado: `empleado@ucv.ve` / `Empleado123`
- Conductor: `conductor@ucv.ve` / `Conductor123`
