workspace "Leaderboard Service - Contenedores" "Diagrama de Contenedores C4 para el Servicio de Leaderboard" {

    model {
        player = person "Jugador" "Usuario del videojuego." "Gamer"
        admin = person "Administrador" "Miembro del equipo de soporte." "Support"
        externalSystems = softwareSystem "Sistemas Externos" "Otros sistemas backend que notifican puntuaciones." "Externo"

        leaderboardSystem = softwareSystem "Servicio de Leaderboard" "Permite la gestión y consulta de un ranking de puntuaciones." {

            api = container "API de Servicio" "Aplicación que contiene la lógica de negocio, construida con Spring Boot." "Java / Spring Boot"

            database = container "Base de Datos" "Almacén persistente para los scores y rankings." "PostgreSQL"

            broker = container "Bróker de Mensajes" "Recibe los eventos de puntuación de forma asíncrona." "Apache Kafka"
        }

        webClient = softwareSystem "Cliente Web" "Aplicación front-end que consume la API." "Cliente" {
            spa = container "Single-Page App" "Aplicación web construida con un framework de JavaScript." "JavaScript / React"
        }

        player -> spa "Usa"
        admin -> api "Gestiona puntuaciones" "REST / HTTPS"

        spa -> api "Consulta el ranking" "REST / HTTPS"

        externalSystems -> broker "Publica eventos de puntuación" "Kafka Protocol"
        api -> broker "Consume eventos de puntuación" "Kafka Protocol"

        api -> database "Lee y escribe datos" "JDBC"
    }

    views {
        container leaderboardSystem "DiagramaDeContenedores" "Muestra los bloques tecnológicos principales del Servicio de Leaderboard." {
            include *
            autoLayout
        }
        
        styles {
            element "Person" {
                background #08427b
                color #ffffff
                shape Person
            }
            element "Software System" { 
                background #1168bd
                color #ffffff 
            }
            element "Container" { 
                background #438dd5
                color #ffffff 
            }
            element "Externo" { 
                background #999999
                color #ffffff 
            }
            element "Cliente" { 
                background #6b10a5
                color #ffffff 
            }
        }

    }
}