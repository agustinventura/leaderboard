# Requisitos No Funcionales (NFRs) - Servicio de Leaderboard

- **Versión:** 1.0
- **Fecha:** 2025-08-12
- **Estado:** Aceptado

## 1. Introducción

Este documento describe los Requisitos No Funcionales para la Versión 1.0 (MVP) del Servicio de Leaderboard. Estos requisitos definen los criterios de calidad, rendimiento y operación del sistema y servirán como guía para las decisiones de arquitectura y tecnología.

---

## 2. Rendimiento 🚀

El rendimiento mide la velocidad y capacidad de respuesta del sistema bajo condiciones normales.

* **NFR-01 (API de Consulta):** Las peticiones a la API para consultar el ranking deben tener un tiempo de respuesta inferior a **200 milisegundos en el percentil 99 (p99)**.
* **NFR-02 (API de Administración):** Las peticiones a la API para gestionar puntuaciones deben tener un tiempo de respuesta inferior a **500 milisegundos**.
* **NFR-03 (Latencia de Actualización):** Una puntuación enviada a través de la interfaz asíncrona debe estar reflejada en el ranking en menos de **5 segundos**.

---

## 3. Escalabilidad 📈

La escalabilidad mide la capacidad del sistema para crecer y manejar un aumento de la carga de trabajo.

* **NFR-04 (Ingesta de Eventos):** El sistema debe ser capaz de procesar un mínimo de **100 eventos de puntuación por segundo** a través de la interfaz asíncrona.
* **NFR-05 (Consultas Concurrentes):** La API de consulta debe poder manejar **50 peticiones por segundo (RPS)** de forma sostenida.
* **NFR-06 (Volumen de Datos):** La solución de persistencia debe poder gestionar de forma eficiente un ranking con hasta **1 millón de jugadores únicos**.

---

## 4. Disponibilidad  uptime: 99.9%

La disponibilidad define el tiempo que se espera que el sistema esté operativo y accesible.

* **NFR-07 (Objetivo de Disponibilidad):** El servicio debe tener una disponibilidad del **99.9%** (conocido como "tres nueves"), lo que equivale a no más de 8.77 horas de inactividad al año.
* **NFR-08 (Tiempo de Recuperación):** En caso de un fallo completo, el sistema debe poder ser restaurado y estar operativo en menos de **30 minutos (RTO)**.

---

## 5. Seguridad 🛡️

La seguridad define las medidas para proteger el sistema y sus datos de amenazas.

* **NFR-09 (Autenticación):** Todos los endpoints de la API de administración deben estar protegidos y requerir autenticación.
* **NFR-10 (Protección de Datos):** El sistema no almacenará ninguna Información de Identificación Personal (PII) más allá del `username` del jugador y del administrador.
* **NFR-11 (Validación de Entradas):** Todas las entradas de datos (APIs, eventos) deben ser validadas para prevenir ataques comunes (ej: inyección de código).

---

## 6. Mantenibilidad y Operación 🛠️

Estos requisitos definen la facilidad para mantener, operar y actualizar el sistema.

* **NFR-12 (Cobertura de Código):** La lógica de negocio principal (el núcleo del dominio) debe mantener una cobertura de pruebas unitarias superior al **80%**.
* **NFR-13 (Despliegue Automatizado):** El proceso de despliegue a producción debe ser completamente automatizado (CI/CD).

---

## 7. Coste 💰

El coste define los límites presupuestarios para la infraestructura del proyecto.

* **NFR-14 (Límite Mensual):** El coste total de la infraestructura en la nube para este servicio no deberá superar los **50 € al mes** durante la fase de MVP.