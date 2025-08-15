# ADR-009: Elección de la Solución de Persistencia de Datos

- **Fecha:** 2025-08-14
- **Estado:** Aceptado

## Contexto

El servicio requiere una solución de persistencia para almacenar los datos del leaderboard, principalmente la relación entre un jugador y su puntuación. Esta elección es fundamental, ya que impacta directamente en el rendimiento de lectura/escritura, la consistencia de los datos, la complejidad operativa y la capacidad de evolucionar el modelo de datos en el futuro. La solución debe integrarse bien con el ecosistema Java/Spring y cumplir con nuestros NFRs.

## Opciones Consideradas

1.  **PostgreSQL (SQL Relacional):** Una base de datos objeto-relacional de código abierto, conocida por su robustez, extensibilidad y estricto cumplimiento del estándar SQL. Es una opción muy madura y fiable.

2.  **Redis (Key-Value con Sorted Sets):** Una base de datos en memoria extremadamente rápida que ofrece estructuras de datos avanzadas. Su estructura `Sorted Set` está específicamente optimizada para casos de uso como los leaderboards.

3.  **MySQL (SQL Relacional):** La base de datos de código abierto más popular para aplicaciones web. Es muy rápida para cargas de trabajo de lectura y cuenta con un ecosistema gigantesco.

4.  **MongoDB (Base de Datos de Documentos):** Una base de datos NoSQL que almacena datos en documentos flexibles tipo JSON. Es ideal para datos no estructurados o semi-estructurados.

## Decisión

Se ha decidido utilizar **PostgreSQL** como la solución de persistencia de datos principal para el proyecto.

**Justificación:**
La elección de PostgreSQL se basa en una combinación equilibrada de **flexibilidad, madurez y rendimiento**, que lo convierten en la opción más segura y versátil para el MVP y su futuro.

* **Madurez y Fiabilidad:** Como base de datos con total conformidad ACID, PostgreSQL nos ofrece las máximas garantías sobre la consistencia de los datos. Su ecosistema en el mundo Java es de primer nivel, con un soporte excelente a través de JPA y JDBC.
* **Rendimiento Adecuado:** Para la consulta principal del leaderboard (obtener el top N de jugadores), el mecanismo de indexación B-Tree de PostgreSQL sobre la columna de puntuación es extremadamente eficiente y cumple sobradamente con los NFRs definidos.
* **Flexibilidad a Futuro:** El soporte avanzado de PostgreSQL para tipos de datos como JSONB nos da la tranquilidad de que, si en el futuro el modelo de datos necesita evolucionar, podremos hacerlo sin necesidad de migrar a otra tecnología.

Cabe hacer una **mención especial y muy favorable a Redis**. Su estructura de datos `Sorted Set` es, sin duda, la solución técnicamente **más eficiente y elegante** para el problema específico de un leaderboard. Sin embargo, se ha optado por PostgreSQL como almacén de datos principal por ser una solución de propósito más general y robusta. Se contempla la posibilidad de introducir Redis en el futuro como una capa de caché o una vista de lectura optimizada si los requisitos de rendimiento se volvieran mucho más exigentes.

Finalmente, MySQL se descarta en favor de la mayor extensibilidad de PostgreSQL, y MongoDB se descarta porque su modelo de documentos no aporta ventajas a nuestro esquema de datos, que es eminentemente estructurado.

## Consecuencias

* La capa de `infrastructure` contendrá un adaptador de persistencia con PostgreSQL.
* Se definirá un esquema de base de datos relacional.
* El proyecto dependerá del driver JDBC de PostgreSQL.