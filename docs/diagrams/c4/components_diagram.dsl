workspace "Leaderboard Service - Completo" "Diagrama C4 para el Servicio de Leaderboard" {

    model {
        player = person "Jugador" "Usuario del videojuego." "Gamer"
        admin = person "Administrador" "Miembro del equipo de soporte." "Support"
        externalSystems = softwareSystem "Sistemas Externos" "Otros sistemas backend que notifican puntuaciones." "Externo"

        leaderboardSystem = softwareSystem "Servicio de Leaderboard" "Permite la gestión y consulta de un ranking de puntuaciones." {

            api = container "API de Servicio" "Aplicación que contiene la lógica de negocio, construida con Spring Boot." "Java / Spring Boot" {

                restController = component "Controlador REST" "Gestiona las peticiones síncronas de la API (REST)." "Spring MVC" "Adaptador de Entrada"
                kafkaConsumer = component "Consumidor de Kafka" "Escucha y procesa eventos de puntuación de forma asíncrona." "Spring for Kafka" "Adaptador de Entrada"
                applicationService = component "Servicio de Aplicación" "Orquesta la lógica de negocio. Implementa los casos de uso." "Java" "Capa de Aplicación"
                domainCore = component "Núcleo del Dominio" "Contiene las entidades, objetos de valor y la lógica de negocio pura. Define los puertos de salida (interfaces de repositorio)." "Java / DDD" "Capa de Dominio"
                persistenceAdapter = component "Adaptador de Persistencia" "Implementa los puertos de repositorio para interactuar con la base de datos." "Spring Data JPA" "Adaptador de Salida"
            }

            database = container "Base de Datos" "Almacén persistente para los scores y rankings." "PostgreSQL"
            broker = container "Bróker de Mensajes" "Recibe los eventos de puntuación de forma asíncrona." "Apache Kafka"
        }

        webClient = softwareSystem "Cliente Web" "Aplicación front-end que consume la API." "Cliente" {
            spa = container "Single-Page App" "Aplicación web construida con un framework de JavaScript." "JavaScript / React"
        }

        player -> spa "Usa"
        admin -> restController "Gestiona puntuaciones" "REST / HTTPS"
        spa -> restController "Consulta el ranking" "REST / HTTPS"
        kafkaConsumer -> broker "Consume eventos" "Kafka Protocol"
        restController -> applicationService "Invoca"
        kafkaConsumer -> applicationService "Invoca"
        applicationService -> domainCore "Usa la lógica de negocio"
        domainCore -> persistenceAdapter "Define el puerto que implementa" "[Interfaz de Repositorio]"
        persistenceAdapter -> database "Lee y escribe" "JDBC"

    }

    views {
        systemContext leaderboardSystem "DiagramaDeContexto" "Vista de Contexto del sistema." {
            include *
            autoLayout
        }

        container leaderboardSystem "DiagramaDeContenedores" "Vista de los contenedores tecnológicos." {
            include *
            autoLayout
        }
        
        component api "DiagramaDeComponentes" "Vista de los componentes internos de la API de Servicio." {
            include *
            include spa
            include admin
            include broker
            include database
            autoLayout
        }

        styles {
            element Person {
                background #08427b
                color #ffffff
                shape Person
            }
            element SoftwareSystem {
                background #1168bd
                color #ffffff
            }
            element Container {
                background #438dd5
                color #ffffff
            }
            element Component {
                background #85bbf0
                color #000000
            }
            element "Externo" {
                background #999999
                color #ffffff
            }
            element "Cliente" {
                background #6b10a5
                color #ffffff
            }
            element "Adaptador de Entrada" {
                background #68ED9B
            }
            element "Adaptador de Salida" {
                background #FBB271
            }
            element "Capa de Aplicación" {
                background #A6C8F5
            }
            element "Capa de Dominio" {
                background #F2D2A2
            }
        }
    }
}