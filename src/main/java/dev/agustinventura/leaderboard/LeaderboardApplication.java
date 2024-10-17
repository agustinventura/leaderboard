package dev.agustinventura.leaderboard;

import dev.agustinventura.leaderboard.application.LeaderboardService;
import dev.agustinventura.leaderboard.application.ports.in.CreateEntryUseCase;
import dev.agustinventura.leaderboard.application.ports.in.GetLeaderboardUseCase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LeaderboardApplication {

  public static void main(String[] args) {
    SpringApplication.run(LeaderboardApplication.class, args);
  }

  @Bean
  public GetLeaderboardUseCase getLeaderboardUseCase() {
    return new LeaderboardService();
  }

  @Bean
  public CreateEntryUseCase createEntryUseCase() {
    return new LeaderboardService();
  }
}
