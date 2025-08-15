workspace "Leaderboard Service" "Diagrama de Contexto para el Servicio de Leaderboard" {

    model {
        # --- Definición de personas/actores ---
        player = person "Jugador" "Usuario del videojuego que compite por la mejor puntuación." "Gamer"
        admin = person "Administrador" "Miembro del equipo de soporte que gestiona el sistema." "Support"

        # --- Definición de Sistemas ---
        leaderboardSystem = softwareSystem "Servicio de Leaderboard" "Permite la gestión y consulta de un ranking de puntuaciones." "Nuestro Sistema"
        externalSystems = softwareSystem "Sistemas Externos" "Otros sistemas backend que notifican las puntuaciones obtenidas por los jugadores." "Externo"

        # --- Definición de las relaciones ---
        player -> leaderboardSystem "Consulta rankings" "API Síncrona"
        admin -> leaderboardSystem "Gestiona puntuaciones" "API Síncrona"
        externalSystems -> leaderboardSystem "Envía puntuaciones de jugadores" "Interfaz Asíncrona"

    }

    views {
        systemContext leaderboardSystem "DiagramaDeContexto" "Diagrama de Contexto C4 para el Servicio de Leaderboard." {
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
            element "Nuestro Sistema" {
                background #008242
                color #ffffff
            }
             element "Externo" {
                background #999999
                color #ffffff
            }
        }
    }
}