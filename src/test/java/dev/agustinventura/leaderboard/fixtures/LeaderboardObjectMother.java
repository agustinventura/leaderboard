package dev.agustinventura.leaderboard.fixtures;

import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import java.util.Set;

public class LeaderboardObjectMother {

  public static Leaderboard emptyLeaderboard() {
    return new Leaderboard(Set.of());
  }

  public static Leaderboard twoEntriesLeaderboard() {
    LeaderboardEntry firstEntry = new LeaderboardEntry("First User", "10000");
    LeaderboardEntry secondEntry = new LeaderboardEntry("Second User", "5000");
    return new Leaderboard(Set.of(firstEntry, secondEntry));
  }
}