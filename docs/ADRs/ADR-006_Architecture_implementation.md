# ADR-006: Implementación de la Arquitectura Hexagonal

- **Fecha:** 2025-08-13
- **Estado:** Aceptado

## Contexto

Tras la decisión de adoptar una **Arquitectura Hexagonal** ([ADR-001](ADR-001_Architecture.md)), es crucial definir cómo se implementará esta estructura físicamente en nuestro proyecto Gradle. La forma en que organicemos el código (en paquetes o en módulos) determinará el nivel de protección que tendremos para evitar violaciones arquitectónicas y asegurar que el dominio permanezca aislado de la infraestructura.

## Opciones Consideradas

### 1. Un Solo Módulo (Separación por Paquetes)
- **Descripción:** Todo el código reside en un único módulo de Gradle. La separación entre `domain`, `application` e `infrastructure` se realiza únicamente a través de paquetes de Java.
- **Ventajas:** Es la configuración más simple y rápida de iniciar.
- **Inconvenientes:** No ofrece ninguna garantía en tiempo de compilación. El respeto a las fronteras de la arquitectura depende enteramente de la disciplina del desarrollador.

### 2. Multi-módulo de Gradle
- **Descripción:** El proyecto se divide en módulos de Gradle independientes (`domain`, `application`, `infrastructure`). Las dependencias entre ellos se definen explícitamente en los ficheros de construcción, permitiendo que `infrastructure` dependa de `application`, y `application` de `domain`, pero nunca al revés.
- **Ventajas:** Proporciona garantías en tiempo de compilación, haciendo imposible crear dependencias que violen la arquitectura. La estructura del proyecto refleja fielmente el diseño arquitectónico.
- **Inconvenientes:** La configuración de la build es ligeramente más compleja al tener que gestionar las relaciones entre módulos.

### 3. Módulos de Java (JPMS)
- **Descripción:** Utiliza el Sistema de Módulos de la Plataforma Java (JPMS, desde Java 9) para un aislamiento aún más fuerte, definido a nivel de la JVM.
- **Ventajas:** Ofrece el máximo nivel de encapsulación y protección de fronteras (en tiempo de compilación y ejecución).
- **Inconvenientes:** Tiene una curva de aprendizaje más elevada y puede presentar fricción con librerías del ecosistema que no estén completamente modularizadas.

## Decisión

Se ha decidido implementar la Arquitectura Hexagonal utilizando un enfoque de **multi-módulo de Gradle**.

**Justificación:**
La principal razón para esta elección es que proporciona **mayores garantías estructurales** en cuanto a la organización del proyecto. Al definir explícitamente las dependencias entre los módulos (`domain`, `application`, `infrastructure`), utilizamos el propio compilador como una herramienta para hacer cumplir nuestra arquitectura. Esto previene de forma automática y sistemática las violaciones de la regla de dependencia (que el dominio no conozca la infraestructura), que es el pilar de la Arquitectura Hexagonal.

Este enfoque representa el punto de equilibrio ideal para nuestro proyecto: es significativamente más robusto y seguro que la simple separación por paquetes, pero evita la complejidad y la posible fricción del ecosistema que podría suponer una implementación completa con JPMS en esta fase.

## Consecuencias

* El proyecto se organizará en una estructura de directorios multi-módulo. Como mínimo, se crearán los siguientes módulos: `domain`, `application` e `infrastructure`.
* El fichero `settings.gradle.kts` definirá los módulos incluidos en la construcción.
* Los ficheros `build.gradle.kts` de cada módulo definirán sus dependencias. El módulo `domain`, por ejemplo, no tendrá dependencias de frameworks.
* Esta estructura modular será la base para todo el desarrollo futuro, asegurando un desacoplamiento limpio entre las distintas capas de la aplicación.