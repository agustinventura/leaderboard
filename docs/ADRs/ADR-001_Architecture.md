# ADR-001: Elección del Patrón de Arquitectura Principal

- **Fecha:** 2025-08-12
- **Estado:** Aceptado

## Contexto

Necesitamos establecer un patrón de arquitectura fundamental para el "Servicio de Leaderboard". Esta decisión es crítica, ya que definirá cómo se estructura el código, cómo interactúan sus componentes y cómo evolucionará el sistema. El patrón elegido debe el desarrollo, facilitar la mantenibilidad a largo plazo y permitirnos ser independientes de la tecnología externa, considerando que tenemos múltiples puntos de entrada (API síncrona, eventos asíncronos) y salida (base de datos).

## Opciones Consideradas

### 1. Arquitectura en Capas (N-Tier)
- **Descripción:** El enfoque tradicional que organiza el código en capas horizontales (Presentación, Lógica de Negocio, Acceso a Datos). La comunicación es estrictamente jerárquica y hacia abajo.
- **Ventajas:** Es un patrón simple, muy conocido y rápido de implementar para proyectos sencillos.
- **Inconvenientes:** Genera un fuerte acoplamiento de la lógica de negocio con la capa de datos. Dificulta las pruebas unitarias aisladas del dominio y hace que cambiar tecnologías (como la base de datos) sea una tarea compleja y arriesgada.

### 2. Arquitectura Hexagonal (Puertos y Adaptadores)
- **Descripción:** Aísla el núcleo de la aplicación (el dominio con la lógica de negocio) del mundo exterior. El núcleo define "puertos" (interfaces) para la comunicación, y las tecnologías externas se "enchufan" a través de "adaptadores" que implementan esos puertos.
- **Ventajas:** La lógica de negocio es totalmente independiente de la infraestructura, lo que permite una testabilidad completa y aislada (ideal para TDD). Facilita enormemente el cambio o la adición de tecnologías (ej: añadir un nuevo tipo de consumidor de eventos) sin tocar el dominio.
- **Inconvenientes:** Requiere más disciplina y puede introducir más clases/interfaces (boilerplate) al principio.

### 3. CQRS (Command Query Responsibility Segregation)
- **Descripción:** Un patrón que separa completamente el modelo utilizado para escribir datos (Comandos) del modelo utilizado para leerlos (Consultas).
- **Ventajas:** Permite optimizar de forma independiente las escrituras y las lecturas, lo cual es muy útil en un leaderboard (muchas escrituras de puntuaciones, muchas lecturas del ranking). Puede mejorar drásticamente el rendimiento y la escalabilidad.
- **Inconvenientes:** Aumenta significativamente la complejidad del sistema, ya que a menudo requiere gestionar dos modelos de datos y su sincronización (consistencia eventual).

## Decisión

Se ha decidido adoptar la **Arquitectura Hexagonal (Puertos y Adaptadores)** como el patrón principal para el proyecto.

**Justificación:**
Esta arquitectura se alinea perfectamente con nuestros objetivos principales. Nos permite **aislar la lógica de negocio** (el cálculo del ranking, la gestión de puntuaciones) en un núcleo puro y agnóstico a la tecnología. Esto es fundamental para poder aplicar **TDD** de manera efectiva sobre el dominio.

La separación mediante puertos y adaptadores nos da la flexibilidad para independizarnos de la infraestructura. La API síncrona y el consumidor de eventos asíncrono serán simplemente dos adaptadores de entrada diferentes que interactúan con el mismo núcleo, y la base de datos será un adaptador de salida.

**Se descarta CQRS en esta fase inicial**. Aunque el patrón encaja bien con el problema de un leaderboard, la complejidad que introduce se considera excesiva para el MVP. La Arquitectura Hexagonal no impide que, en el futuro, si los requisitos de rendimiento lo exigen, se pueda implementar CQRS dentro de la estructura ya existente.

## Consecuencias

* La estructura del proyecto se organizará en torno a un módulo de `dominio` y un módulo de `infraestructura`.
* El desarrollo inicial puede ser ligeramente más lento debido a la necesidad de definir interfaces (puertos) y sus implementaciones (adaptadores).
* A largo plazo, se espera que la mantenibilidad sea mayor y que la adición de nuevas funcionalidades o tecnologías sea más sencilla y segura.