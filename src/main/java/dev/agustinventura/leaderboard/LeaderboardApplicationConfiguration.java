package dev.agustinventura.leaderboard;

import dev.agustinventura.leaderboard.adapter.out.jdbc.LeaderboardEntryJDBCRepository;
import dev.agustinventura.leaderboard.adapter.out.jdbc.LeaderboardJDBCAdapter;
import dev.agustinventura.leaderboard.adapter.out.jdbc.mapper.LeaderboardEntryJDBCDTOMapper;
import dev.agustinventura.leaderboard.application.LeaderboardService;
import dev.agustinventura.leaderboard.application.ports.out.LoadLeaderboardPort;
import dev.agustinventura.leaderboard.application.ports.out.SaveLeaderboardPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LeaderboardApplicationConfiguration {

  @Bean
  public LeaderboardJDBCAdapter leaderboardJDBCAdapter(LeaderboardEntryJDBCRepository leaderboardEntryJDBCRepository,
      LeaderboardEntryJDBCDTOMapper leaderboardEntryJDBCDTOMapper) {
    return new LeaderboardJDBCAdapter(leaderboardEntryJDBCRepository, leaderboardEntryJDBCDTOMapper);
  }

  @Bean
  public LeaderboardService leaderboardService(LoadLeaderboardPort loadLeaderboardRepository,
      SaveLeaderboardPort saveLeaderboardRepository) {
    return new LeaderboardService(loadLeaderboardRepository, saveLeaderboardRepository);
  }
}
