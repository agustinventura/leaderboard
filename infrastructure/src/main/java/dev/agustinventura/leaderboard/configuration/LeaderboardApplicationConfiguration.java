package dev.agustinventura.leaderboard.configuration;

import dev.agustinventura.leaderboard.adapters.in.rest.LeaderboardApiDelegate;
import dev.agustinventura.leaderboard.adapters.in.rest.LeaderboardRestAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LeaderboardApplicationConfiguration {

    @Bean
    public LeaderboardApiDelegate leaderboardApiDelegate() {
        return new LeaderboardRestAdapter();
    }
}
