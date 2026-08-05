# ADR-015: Gestión de actualizaciones de dependencias

- **Fecha:** 2026-08-05
- **Estado:** Aceptada

## Contexto

El repositorio utiliza Gradle con Kotlin DSL y está organizado en múltiples módulos. En este contexto, las versiones de dependencias pueden quedar desactualizadas con el tiempo y aumentar el riesgo de problemas de seguridad, incompatibilidades o deuda técnica.

Necesitamos un mecanismo para identificar de forma reactiva las actualizaciones disponibles y canalizarlas en un flujo de revisión manejable por el equipo, sin añadir una carga operativa excesiva.

Este ADR documenta únicamente la decisión sobre la herramienta a adoptar para la detección de actualizaciones. No introduce configuración operativa ni ejecuta actualizaciones de dependencias en este cambio.

## Opciones Consideradas

1. **Revisión manual periódica de dependencias**
   Consiste en revisar manualmente versiones nuevas en repositorios o mediante consultas puntuales.
   Ventajas: control total sobre cuándo revisar y qué actualizar.
   Desventajas: alto esfuerzo continuo, mayor probabilidad de omisiones y menor consistencia en la detección temprana.

2. **Renovate (u otro actualizador externo altamente configurable)**
   Permite automatizar detección y propuestas de actualización con reglas avanzadas y gran nivel de personalización.
   Ventajas: elevada flexibilidad para políticas complejas, agrupación avanzada y control fino del comportamiento.
   Desventajas: mayor complejidad de configuración y mantenimiento; no es una capacidad nativa integrada de GitHub, por lo que añade superficie operativa adicional.

3. **Dependabot**
   Herramienta nativa de GitHub para detectar vulnerabilidades y proponer actualizaciones mediante pull requests en ecosistemas compatibles, incluido Gradle.
   Ventajas: integración directa con GitHub, adopción sencilla para un enfoque reactivo y menor carga de mantenimiento inicial.
   Desventajas: menor flexibilidad que alternativas más configurables para escenarios de política avanzada.

## Decisión

Se decide adoptar **Dependabot** como mecanismo reactivo nativo de GitHub para la identificación de actualizaciones de dependencias en Gradle.

**Justificación:**

1. Frente a la revisión manual, Dependabot reduce significativamente el esfuerzo recurrente y mejora la cobertura de detección al automatizar alertas y propuestas.
2. Frente a Renovate y opciones equivalentes, Dependabot ofrece una integración más directa con la plataforma ya utilizada por el proyecto, con menor carga de operación y mantenimiento para las necesidades actuales.
3. Aunque Dependabot ofrece menos configurabilidad avanzada que alternativas externas, proporciona control suficiente a través del flujo de pull requests y revisión habitual del repositorio.
4. La elección prioriza simplicidad operativa e integración nativa en esta etapa, manteniendo abierta la posibilidad de reevaluar herramientas si en el futuro se requieren políticas más complejas.

## Consecuencias

* La generación de pull requests y alertas de actualización de dependencias requerirá una decisión posterior de configuración operativa específica.
* Toda propuesta de actualización seguirá sujeta al proceso normal de revisión del proyecto y a validaciones de pruebas antes de su integración.
* Este ADR no modifica dependencias declaradas ni archivos de build.
* Este ADR no introduce cambios en CI, workflows ni código de aplicación.
