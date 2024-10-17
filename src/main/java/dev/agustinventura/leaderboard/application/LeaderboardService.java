package dev.agustinventura.leaderboard.application;

import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.ports.in.CreateEntryUseCase;
import dev.agustinventura.leaderboard.application.ports.in.GetLeaderboardUseCase;
import java.util.Set;

public class LeaderboardService implements GetLeaderboardUseCase, CreateEntryUseCase {

  @Override
  public void create(String username, String score) {
  }

  @Override
  public Leaderboard getLeaderboard() {
    return new Leaderboard(Set.of());
  }
}
