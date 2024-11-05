package dev.agustinventura.leaderboard.fixtures;

import dev.agustinventura.leaderboard.adapter.in.rest.dto.LeaderboardEntryRESTDTO;
import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class LeaderboardObjectMother {

  public static final String TEST_SCORE = "1";

  public static final String TEST_PLAYERNAME = "test_playername";

  public static final String NOT_EXISTING_PLAYERNAME = "not_exists";

  public static final String USERNAME_HEADER = "X-USERNAME";

  public static final String ADMIN_USERNAME = "ADMIN";

  public static @NotNull Leaderboard emptyLeaderboard() {
    return new Leaderboard(Set.of());
  }

  public static @NotNull Leaderboard oneEntryLeaderboard() {
    LeaderboardEntry firstEntry = new LeaderboardEntry("First User", "10000");
    return new Leaderboard(Set.of(firstEntry));
  }

  public static @NotNull Leaderboard twoEntriesLeaderboard() {
    LeaderboardEntry firstEntry = new LeaderboardEntry("First User", "10000");
    LeaderboardEntry secondEntry = new LeaderboardEntry("Second User", "5000");
    return new Leaderboard(Set.of(firstEntry, secondEntry));
  }

  public static @NotNull LeaderboardEntry testLeaderboardEntry() {
    return new LeaderboardEntry(TEST_PLAYERNAME, TEST_SCORE);
  }

  public static @NotNull LeaderboardEntryRESTDTO getLeaderboardEntryRESTDTO() {
    LeaderboardEntryRESTDTO leaderboardEntryDTO = new LeaderboardEntryRESTDTO();
    leaderboardEntryDTO.setPlayerName(TEST_PLAYERNAME);
    leaderboardEntryDTO.setScore(Integer.parseInt(TEST_SCORE));
    return leaderboardEntryDTO;
  }

  public static @NotNull LeaderboardEntryRESTDTO getLeaderboardEntryRESTDTO(String playerName, int score) {
    LeaderboardEntryRESTDTO leaderboardEntry = new LeaderboardEntryRESTDTO();
    leaderboardEntry.setPlayerName(playerName);
    leaderboardEntry.setScore(score);
    return leaderboardEntry;
  }
}