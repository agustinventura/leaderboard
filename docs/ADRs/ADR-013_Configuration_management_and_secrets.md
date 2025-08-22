 # ADR-013: Gestión de la Configuración y Secretos

- **Fecha:** 2025-08-20
- **Estado:** Aceptado

## Contexto

Toda aplicación necesita gestionar dos tipos de configuración: la configuración general (no sensible, como el puerto del servidor o el nivel de los logs) y los secretos (información sensible, como contraseñas de bases de datos o API keys). Es necesario definir una estrategia clara y segura para manejar ambos tipos, asegurando que la configuración sea flexible para distintos entornos (desarrollo, producción) y que los secretos nunca se expongan.

## Opciones y Decisión

Se han analizado las estrategias para la configuración general y la de secretos de forma separada.

### 1. Gestión de la Configuración General

* **Opciones Consideradas:**
    * **Ficheros de Configuración de Spring Boot:** Utilizar el mecanismo nativo de `application.properties` o `application.yml` junto con perfiles para gestionar las diferencias entre entornos. Es el enfoque por defecto.
    * **Servidor de Configuración Externo (ej: Spring Cloud Config):** Centralizar la configuración de uno o varios servicios en una aplicación externa, usualmente respaldada por un repositorio de Git.

* **Decisión y Justificación:**
    Por simplicidad, se ha decidido utilizar los ficheros de configuración por defecto de Spring Boot. Para un único servicio, la complejidad de configurar y mantener un servidor de configuración externo no aporta beneficios suficientes. El sistema de perfiles de Spring es una herramienta potente y probada para manejar las distintas configuraciones de nuestros entornos. La adopción de un servidor externo se podrá reevaluar si el proyecto escala a múltiples microservicios o se complejiza la configuración.

### 2. Gestión de Secretos

* **Opciones Consideradas:**
    * **Variables de Entorno (Enfoque 12-Factor App):** La aplicación lee los secretos de las variables de entorno, siendo la plataforma de despliegue (Kubernetes, Docker, CI/CD) la responsable de inyectarlas de forma segura.
    * **Servidor de Secretos Dedicado (ej: HashiCorp Vault):** Utilizar un servicio externo especializado en el almacenamiento y gestión segura de secretos. Es la opción más potente y segura, pero también la más compleja de operar.
    * **Secretos Cifrados en Git (ej: SOPS):** Versionar los secretos en el repositorio de Git, pero en un formato cifrado que solo puede ser desencriptado por personal o sistemas autorizados.

* **Decisión y Justificación:**
    Para el único secreto que tenemos actualmente (la contraseña de la base de datos), se ha decidido utilizar la **inyección a través de variables de entorno**.

    Esta opción representa el mejor equilibrio entre seguridad y simplicidad para nuestro caso de uso. Es el estándar de facto para aplicaciones nativas de la nube (cloud-native) y desacopla completamente la aplicación de la gestión de secretos. La responsabilidad de almacenar y proporcionar el secreto de forma segura se delega a la plataforma de orquestación o de CI/CD, que es la práctica recomendada. Se evita así la complejidad operacional de mantener un servidor como Vault.

## Consecuencias

* El proyecto contendrá ficheros de configuración como `application.yml` (para valores por defecto) y `application-prod.yml` (para sobreescribir valores en producción), los cuales serán versionados en Git.
* **Ningún secreto será almacenado en texto plano en el repositorio.**
* El código de la aplicación leerá la contraseña de la base de datos desde una variable de entorno. La configuración en `application.yml` contendrá un placeholder: `spring.datasource.password: ${DB_PASSWORD}`.
* La documentación de despliegue deberá especificar las variables de entorno requeridas por la aplicación para funcionar.