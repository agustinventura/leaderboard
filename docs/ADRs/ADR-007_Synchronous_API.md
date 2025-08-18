# ADR-007: Elección del Mecanismo para la Interfaz Síncrona

- **Fecha:** 2025-08-13
- **Estado:** Aceptado

## Contexto

El servicio debe exponer una interfaz síncrona para ser consumida por clientes externos (ej: un front-end web, una app móvil) y herramientas de administración. Es necesario seleccionar un estándar o tecnología para implementar esta API. La elección debe ser pragmática, alineada con el alcance del MVP, y debe priorizar la simplicidad de implementación y la compatibilidad para acelerar el desarrollo.

## Opciones Consideradas

1.  **REST (REpresentational State Transfer):** El estándar de facto para APIs web, basado en la manipulación de recursos a través de HTTP. Universalmente soportado.
2.  **GraphQL:** Un lenguaje de consulta para APIs que permite a los clientes solicitar estructuras de datos específicas para evitar obtener más o menos información de la necesaria.
3.  **gRPC:** Un framework de alto rendimiento para llamadas a procedimientos remotos, ideal para la comunicación interna entre servicios.
4.  **SOAP:** Un protocolo antiguo y robusto basado en XML con un contrato estricto (WSDL).

## Decisión

Se ha decidido utilizar **REST** como el estilo de arquitectura para la implementación de las interfaces síncronas.

**Justificación:**
El proceso de decisión se ha basado en un descarte progresivo:

1.  **Descarte de SOAP:** Se descarta por ser un estándar antiguo, en desuso y verboso. La complejidad y el coste de desarrollo asociados a sus contratos (WSDL) no se justifican para un proyecto nuevo.

2.  **Descarte de gRPC:** Aunque es una tecnología de muy alto rendimiento, está optimizada para la comunicación interna entre microservicios. Dado que esta API es hipotéticamente pública y debe ser fácilmente consumible por una variedad de clientes (incluidos navegadores), gRPC no es la opción más adecuada.

3.  **Comparativa REST vs. GraphQL:** Ambas son aproximaciones modernas y válidas. Sin embargo, para el alcance del MVP (un listado de nombres de usuario y sus puntuaciones), los problemas de *over-fetching* y *under-fetching* que GraphQL resuelve de manera brillante no son una preocupación real. En cambio, el soporte para **REST** en todas las herramientas del ecosistema (librerías cliente, gateways de API, etc.) es sobresaliente y universal.

Por lo tanto, se escoge REST porque su sencillez de implementación y su compatibilidad total acelerarán el desarrollo y la adopción de la API, siendo la opción más pragmática y productiva para las necesidades actuales del proyecto.

## Consecuencias

* La API se diseñará siguiendo los principios REST, organizándola en torno a recursos.
* Se utilizarán los verbos HTTP y los códigos de estado estándar para las operaciones y sus resultados.
* Se adoptará la especificación **OpenAPI 3.0** para definir y documentar el contrato formal de la API.
* El formato de intercambio de datos por defecto será **JSON**.