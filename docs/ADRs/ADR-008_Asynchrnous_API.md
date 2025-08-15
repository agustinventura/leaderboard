# ADR-008: Elección del Mecanismo para la Interfaz Asíncrona

- **Fecha:** 2025-08-13
- **Estado:** Aceptado

## Contexto

El servicio requiere un mecanismo para la ingesta de eventos de forma asíncrona, desacoplando a los productores de eventos del "Servicio de Leaderboard". Es necesario seleccionar una plataforma de mensajería o streaming que sea robusta, escalable y que se alinee con los objetivos estratégicos del proyecto.

## Opciones Consideradas

1.  **Servicios Gestionados en la Nube (AWS SQS, Google Pub/Sub):** Plataformas de mensajería totalmente gestionadas por proveedores cloud. Ofrecen escalabilidad infinita y cero carga operacional, pero generan un fuerte acoplamiento a un proveedor específico.

2.  **RabbitMQ:** Un bróker de mensajes tradicional, maduro y muy flexible. Es robusto, más ligero en recursos que las plataformas de streaming y excelente para colas de trabajo y enrutamiento complejo.

3.  **Apache Kafka:** Una plataforma de streaming de eventos distribuida, diseñada como un log inmutable para un rendimiento extremo. Es el estándar de facto en la industria para arquitecturas basadas en eventos a gran escala.

4.  **Apache Pulsar:** Una plataforma de nueva generación que combina streaming y colas de mensajes con una arquitectura muy flexible y escalable.

## Decisión

Se ha decidido utilizar **Apache Kafka** como la plataforma para la interfaz asíncrona del servicio.

**Justificación:**
El proceso de decisión se ha basado en los siguientes puntos:

1.  **Descarte de Servicios Cloud:** Se excluyen las opciones gestionadas en la nube para mantener el proyecto agnóstico a un proveedor y evitar el `vendor lock-in` en esta fase inicial.

2.  **Análisis Estratégico (Kafka vs. RabbitMQ):** Se reconoce que, desde una perspectiva puramente técnica y de adecuación a los NFRs actuales, **RabbitMQ sería una opción más ligera y adecuada**. Su menor complejidad operacional y su menor consumo de recursos encajarían perfectamente con la escala del MVP.

Sin embargo, la decisión final se toma basándose en un criterio estratégico, similar al que motivó la elección de Java y Gradle: el desarrollo profesional y la alineación con las tecnologías más relevantes del mercado. **Apache Kafka es la tecnología líder** en el ámbito del streaming de datos y las arquitecturas de eventos. Aunque técnicamente está sobredimensionada para la carga inicial de este proyecto, su adopción representa una oportunidad de gran valor para:
    * **Profundizar** en un ecosistema de alta demanda.
    * **Aplicar** patrones de diseño asociados a Kafka (productores, consumidores, gestión de topics).
    * **Generar contenido técnico** de gran interés (artículos de blog, charlas) sobre una tecnología puntera.

    Por lo tanto, se prioriza el valor estratégico, de aprendizaje y de posicionamiento profesional que ofrece Kafka por encima de la simplicidad pragmática que ofrecería RabbitMQ.

## Consecuencias

* El proyecto requerirá la configuración y gestión de un clúster de Kafka (o una instancia de desarrollo).
* Se asume una mayor complejidad inicial en la configuración del entorno de desarrollo y despliegue en comparación con RabbitMQ.
* El foco del desarrollo se centrará en aplicar las mejores prácticas del ecosistema Kafka.