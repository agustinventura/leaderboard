# ADR-005: Elección de la Herramienta de Construcción

- **Fecha:** 2025-08-13
- **Estado:** Aceptado

## Contexto

El proyecto necesita una herramienta de construcción para gestionar su ciclo de vida: administrar dependencias, compilar el código fuente, ejecutar las pruebas y empaquetar la aplicación. La elección de esta herramienta afecta directamente a la productividad del desarrollador, los tiempos de construcción y la mantenibilidad del proyecto.

## Opciones Consideradas

1.  **Apache Maven:** Es el estándar de facto en el ecosistema Java. Utiliza un `pom.xml` declarativo y se basa en el principio de "convención sobre configuración". Es muy robusto y predecible.

2.  **Gradle:** Una herramienta de construcción moderna que ofrece mayor flexibilidad y rendimiento. Usa un DSL (en Groovy o Kotlin) para definir los scripts de construcción, lo que permite una lógica más avanzada.

3.  **Otras (Bazel, Buck, etc.):** Herramientas de alto rendimiento diseñadas principalmente para monorepos a gran escala, con una complejidad de configuración inicial más elevada.

## Decisión

Se ha decidido utilizar **Gradle** como la herramienta de construcción para el proyecto.

**Justificación:**
Aunque existe una profunda experiencia en Maven, la elección de Gradle se alinea mejor con los objetivos multifacéticos de este proyecto:

1.  **Desarrollo Profesional y Familiarización:** Uno de los objetivos principales del proyecto es servir como plataforma para el desarrollo profesional. Adoptar Gradle es una oportunidad para profundizar y ganar experiencia en una herramienta moderna y ampliamente utilizada en el ecosistema Java, lo cual es un activo valioso.

2.  **Rendimiento Superior:** Gradle ofrece un rendimiento notablemente superior al de Maven, especialmente en ciclos de desarrollo activos. Gracias a sus mecanismos avanzados de caché, la compilación incremental y el uso de un proceso "daemon" en segundo plano, los tiempos para compilar y ejecutar pruebas son significativamente más cortos.

3.  **Flexibilidad y Concisión:** Los scripts de Gradle, especialmente usando el DSL de Kotlin (`build.gradle.kts`), son más concisos, legibles y potentes que los ficheros `pom.xml`. Esto permite una mayor expresividad a la hora de definir lógicas de construcción personalizadas si fuesen necesarias en el futuro.

La combinación de una mejora tangible en el rendimiento con el objetivo estratégico de desarrollo profesional hace que Gradle sea la elección idónea para este proyecto.

## Consecuencias

* La configuración del proyecto se definirá en un fichero `build.gradle.kts`, utilizando el DSL de Kotlin para obtener seguridad de tipos y un mejor soporte en el IDE.
* Se invertirá tiempo en familiarizarse con las mejores prácticas y el ecosistema de plugins de Gradle.
* El proyecto se beneficiará de ciclos de feedback más rápidos gracias a la reducción de los tiempos de construcción y testeo.
* Se mantendrá la estructura de directorios estándar de Maven (`src/main/java`, etc.), ya que Gradle la soporta por defecto, facilitando la familiaridad.