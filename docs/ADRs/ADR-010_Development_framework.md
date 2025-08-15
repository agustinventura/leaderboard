# ADR-010: Elección del Framework de Desarrollo

- **Fecha:** 2025-08-14
- **Estado:** Aceptado

## Contexto

Tras haber seleccionado Java como lenguaje de programación (ADR-004), es necesario elegir un framework de desarrollo. Este framework nos proporcionará una estructura y un conjunto de herramientas para simplificar la implementación de funcionalidades transversales como la gestión de peticiones web, la inyección de dependencias, la configuración y el acceso a datos. La elección debe equilibrar la productividad, el rendimiento y la alineación con los objetivos del proyecto.

## Opciones Consideradas

1.  **Spring Boot:** El framework estándar de facto en el ecosistema Java. Ofrece un ecosistema inmenso y un enfoque de "convención sobre configuración" que acelera enormemente el desarrollo. Su modelo se basa en la inyección de dependencias en tiempo de ejecución.

2.  **Frameworks Cloud-Native (Quarkus, Micronaut):** Frameworks modernos diseñados para optimizar el rendimiento (tiempo de arranque, consumo de memoria) en contenedores y entornos serverless. Realizan la mayor parte del trabajo pesado en tiempo de compilación.

3.  **Librerías Ligeras (Javalin):** Proporcionan únicamente la capa web de forma simple y no opinada, dejando la responsabilidad de integrar el resto de las funcionalidades al desarrollador.

4.  **Sin Framework:** El enfoque de usar Java puro, conectando librerías manualmente para tener un control total y un `overhead` mínimo a costa de una menor productividad inicial.

## Decisión

Se ha decidido utilizar **Spring Boot** como el framework de desarrollo principal para el proyecto.

**Justificación:**
La decisión se basa en un criterio estratégico similar al de la elección de Java (ADR-004): el objetivo es **centrarse en los retos de arquitectura y diseño de software, no en el aprendizaje de un nuevo framework**.

1.  **Aprovechamiento de la Experiencia:** Se utiliza la familiaridad con el ecosistema Spring para maximizar la productividad y permitirnos abordar directamente los aspectos más complejos del proyecto, como la implementación de la Arquitectura Hexagonal y los patrones de DDD.

2.  **Ecosistema Maduro e Integración:** Spring Boot posee el ecosistema más completo, con integraciones robustas y probadas para todas las tecnologías elegidas (PostgreSQL con Spring Data JPA, Kafka con Spring for Kafka, etc.). Esto reduce el riesgo y el tiempo de desarrollo.

3.  **Visión a Futuro con Spring Native:** Se reconoce que los frameworks como Quarkus o Micronaut ofrecen un rendimiento superior para ejecutables nativos. Sin embargo, esta ventaja no es exclusiva. Se contempla como un **paso futuro la posibilidad de utilizar Spring Native** para compilar la aplicación a un ejecutable nativo con GraalVM. Esto nos permitiría obtener un rendimiento y una eficiencia de recursos muy similares a los de los frameworks cloud-native, pero manteniéndonos dentro del ecosistema de Spring que ya conocemos.

Por lo tanto, Spring Boot nos ofrece la máxima productividad ahora, con un camino claro para alcanzar el máximo rendimiento en el futuro.

## Consecuencias

* El proyecto se estructurará como una aplicación Spring Boot.
* La inyección de dependencias será gestionada por el contenedor de Inversión de Control (IoC) de Spring.
* La capa de `infrastructure` hará un uso extensivo de las anotaciones y abstracciones de Spring (`@RestController`, `@Service`, etc.) para implementar los adaptadores.
* El objetivo inicial será una aplicación que corre sobre la JVM, dejando la compilación nativa como una optimización a futuro.