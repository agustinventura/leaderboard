# ADR-012: Estrategia de Observabilidad

- **Fecha:** 2025-08-20
- **Estado:** Aceptado

## Contexto

Para que el "Servicio de Leaderboard" pueda ser operado y mantenido en un entorno de producción, es fundamental implementar una estrategia de observabilidad completa. No es suficiente con que la aplicación funcione; debemos ser capaces de monitorizar su estado, diagnosticar problemas, entender su comportamiento en tiempo real y adelantarnos a degradaciones. Esta estrategia se basará en los tres pilares de la observabilidad: **Logs**, **Métricas** y **Trazas**.

## Opciones y Decisión por Componente

Se ha decidido adoptar un stack de observabilidad alineado con los estándares del ecosistema Spring Boot.

### 1. Logs Estructurados
Los logs son el registro de los eventos discretos que ocurren en la aplicación. Para que sean útiles, deben ser estructurados y procesables por máquinas.

* **Fachada de Logging:** Se utilizará **SLF4J**, que es la abstracción estándar en el mundo Java y la recomendada por Spring.
* **Implementación:** Se usará **Logback**, la implementación por defecto que incluye Spring Boot, por su rendimiento y flexibilidad.
* **Formato de Log:** Se compararon los formatos JSON `ECS`, `GELF` y `Logstash`.
    * **Decisión:** Se elige **ECS (Elastic Common Schema)**.
    * **Justificación:** ECS es una especificación abierta y completa que busca unificar el formato no solo de los logs, sino también de las métricas y las trazas. Adoptarlo nos prepara para una plataforma de observabilidad correlacionada y es la opción más agnóstica y profesional a largo plazo.

### 2. Métricas
Las métricas son agregaciones numéricas sobre el comportamiento del sistema a lo largo del tiempo (ej: latencia media, número de peticiones por segundo).

* **Fachada de Métricas:** Se utilizará **Micrometer**, la librería de instrumentación integrada en Spring Boot Actuator.
* **Formato de Exposición:** Se compararon formatos como Prometheus, Graphite e InfluxDB.
    * **Decisión:** Se expondrán las métricas en el formato de **Prometheus**.
    * **Justificación:** Prometheus es el estándar de facto para la monitorización en entornos nativos de la nube (Cloud-Native) y Kubernetes. La integración con Spring Boot es nativa y extremadamente sencilla.

### 3. Trazas Distribuidas
Las trazas nos permiten seguir el ciclo de vida de una petición a medida que viaja a través de nuestro servicio (y potencialmente a través de otros servicios).

* **Fachada de Trazas:** Se utilizará el módulo **Micrometer Tracing** de Spring Boot 3, que unifica la forma de gestionar las trazas.
* **Implementación:** Se comparó OpenTelemetry con el enfoque anterior (Brave, usado por Spring Cloud Sleuth).
    * **Decisión:** Se utilizará **OpenTelemetry** como la implementación de trazado.
    * **Justificación:** OpenTelemetry es el estándar abierto emergente para la telemetría (trazas, métricas y logs) y es la implementación por defecto y recomendada para Micrometer Tracing en Spring Boot 3. Es la opción moderna y a prueba de futuro.

## Justificación General de la Decisión

La combinación de **SLF4J/Logback con formato ECS**, **Micrometer con formato Prometheus** y **Micrometer Tracing con OpenTelemetry** nos proporciona un stack de observabilidad de primer nivel, moderno y basado en estándares abiertos. Esta estrategia se alinea perfectamente con el ecosistema de Spring Boot 3, minimiza el `vendor lock-in` y nos prepara para integrar el servicio con las principales plataformas de monitorización del mercado.

## Consecuencias

* Se añadirá la dependencia de `spring-boot-starter-actuator` al proyecto.
* Se configurarán las dependencias necesarias para la exportación a Prometheus y el uso de OpenTelemetry.
* Se configurará el formato de logs ECS.
* La aplicación expondrá un endpoint `/actuator/prometheus` para que un servidor Prometheus pueda recolectar las métricas.
* Todo el código de la aplicación utilizará la API de SLF4J para el logging.