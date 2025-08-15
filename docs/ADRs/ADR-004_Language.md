# ADR-004: Elección del Lenguaje de Programación

- **Fecha:** 2025-08-13
- **Estado:** Aceptado

## Contexto

Es necesario seleccionar un lenguaje de programación principal para la implementación del "Servicio de Leaderboard". Esta es una de las decisiones de tecnología más fundamentales, ya que impactará el ecosistema de librerías, el framework, el rendimiento, y el flujo de trabajo general. La elección debe alinearse no solo con los requisitos técnicos del proyecto, sino también con sus objetivos de desarrollo profesional.

## Opciones Consideradas

1.  **Java:** Un lenguaje orientado a objetos, de alto rendimiento y con uno de los ecosistemas más grandes y maduros del mundo. Es el estándar de facto en el mundo corporativo para aplicaciones backend.

2.  **Kotlin:** Una alternativa moderna en la JVM. Es más conciso y seguro que Java, con el que es 100% interoperable.

3.  **Go (Golang):** Un lenguaje simple y compilado, conocido por su excelente modelo de concurrencia y su rapidez de compilación.

4.  **Rust:** Un lenguaje compilado enfocado en la máxima seguridad de memoria y rendimiento, sin un recolector de basura.

5.  **Otros (C#, Node.js/TypeScript):** Ecosistemas muy potentes y populares para el desarrollo backend, cada uno con sus propias fortalezas en rendimiento, tooling y paradigmas.

## Decisión

Se ha decidido utilizar **Java** como el lenguaje de programación principal para el desarrollo del servicio.

**Justificación:**
La decisión se basa en un criterio estratégico que va más allá de una simple comparación técnica entre lenguajes:

1.  **Alineación con los Objetivos Profesionales:** El objetivo principal de este proyecto es el desarrollo profesional y la adquisición de nuevos conocimientos dentro del **ecosistema Java y Cloud**. Adoptar Java permite centrar todos los esfuerzos en aplicar patrones avanzados (DDD, Arquitectura Hexagonal) y explorar tecnologías del ecosistema (Spring, Kafka, etc.) en profundidad, en lugar de desviar el foco hacia el aprendizaje de la sintaxis y las particularidades de un nuevo lenguaje.

2.  **Maximización de la Productividad:** Al utilizar un lenguaje en el que ya se posee una experiencia sólida, se elimina la curva de aprendizaje inicial. Esto permite avanzar directamente a los desafíos arquitectónicos y de diseño, así como integrar nuevas tecnologías que son el verdadero núcleo de este proyecto.

3.  **Ecosistema Maduro:** El ecosistema de Java es robusto, está probado en batalla y ofrece librerías y frameworks de primer nivel para cada una de las necesidades que nuestro proyecto requiere, desde el framework web hasta las herramientas de construcción y las librerías de pruebas.

Aunque lenguajes como Kotlin ofrecen mejoras en concisión o Go en concurrencia, la elección de Java es la que mejor sirve al propósito global del proyecto.

## Consecuencias

* El proyecto se desarrollará sobre la plataforma Java, utilizando una versión LTS reciente (ej: Java 21) para aprovechar sus características modernas.
* El desarrollo se apoyará fuertemente en las herramientas y frameworks del ecosistema Java, como Spring Boot.
* Se asume la verbosidad de Java como un "trade-off" aceptable a cambio de la alineación con los objetivos del proyecto.