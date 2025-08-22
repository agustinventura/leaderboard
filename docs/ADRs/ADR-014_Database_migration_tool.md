# ADR-014: Estrategia de Migraciones de Base de Datos

- **Fecha:** 2025-08-20
- **Estado:** Aceptado

## Contexto

A medida que el proyecto evolucione, el esquema de nuestra base de datos necesitará cambiar para dar soporte a nuevas funcionalidades. Es fundamental contar con un proceso automatizado, versionado y fiable para aplicar estos cambios de forma consistente en todos los entornos.

## Opciones Consideradas

1.  **Generación Automática de Esquema (JPA/Hibernate DDL-Auto):** Permitir que el framework de persistencia genere el esquema de la base de datos directamente a partir de las clases `@Entity` de Java. Es muy rápido para prototipar, pero no ofrece control de versiones y es inseguro para producción.

2.  **Liquibase:** Una herramienta potente que gestiona los cambios a través de "changesets" abstractos, definidos normalmente en ficheros XML, YAML o JSON. Esto permite, en teoría, que las migraciones sean independientes del motor de base de datos.

3.  **Flyway:** Una herramienta popular que se basa en la simplicidad. Gestiona las migraciones ejecutando scripts de SQL plano que están versionados siguiendo una convención de nombrado.

## Decisión

Se ha decidido utilizar **Flyway** para la gestión de las migraciones de la base de datos.

**Justificación:**

1.  **Descarte de la Generación Automática:** Esta opción se descarta por dos motivos principales. Primero, es una práctica insegura y no controlada para entornos productivos. Segundo, y más importante desde una perspectiva de diseño, **crearía un fuerte acoplamiento de nuestro esquema con una tecnología de persistencia específica (JPA/Hibernate)**. Nuestra Arquitectura Hexagonal busca precisamente evitar esto, y aún no hemos formalizado la elección de la tecnología de acceso a datos.

2.  **Análisis de Flyway vs. Liquibase:** Ambas son herramientas excelentes y robustas. Sin embargo, se elige Flyway por su **simplicidad y enfoque directo**. Para las necesidades de nuestro proyecto, la claridad de escribir y versionar scripts de SQL plano es una ventaja significativa.

Aunque el uso de SQL puede acoplar las migraciones a PostgreSQL, este riesgo se considera bajo y se puede mitigar **utilizando SQL estándar ANSI siempre que sea posible**, evitando instrucciones específicas del proveedor a menos que sea estrictamente necesario. El control total sobre el SQL que se ejecutará se valora por encima de la capa de abstracción que ofrece Liquibase, que para este proyecto se considera una complejidad innecesaria.

En resumen, Flyway representa la solución más pragmática, dándonos control, simplicidad y un proceso robusto que se integra a la perfección con Spring Boot.

## Consecuencias

* Se añadirá la dependencia `spring-boot-starter-flyway` al proyecto.
* Se creará un directorio en los recursos (`src/main/resources/db/migration`) donde se almacenarán todos los scripts de migración SQL.
* Los scripts seguirán la convención de nombrado `V<VERSION>__<DESCRIPCION>.sql` (ej: `V1__Crear_tabla_leaderboard.sql`).
* Por defecto, Spring Boot ejecutará las migraciones pendientes de Flyway automáticamente al arrancar la aplicación.
* Todo cambio futuro en la estructura de la base de datos deberá ser registrado a través de un nuevo script de migración.