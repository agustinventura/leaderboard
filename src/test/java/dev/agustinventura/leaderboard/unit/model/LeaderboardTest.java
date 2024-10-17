package dev.agustinventura.leaderboard.unit.model;

import static dev.agustinventura.leaderboard.unit.model.LeaderboardEntryTest.TEST_SCORE;
import static dev.agustinventura.leaderboard.unit.model.LeaderboardEntryTest.TEST_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import java.util.Comparator;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LeaderboardTest {

  @Test
  void givenANullLeaderboardEntrySetLeaderboardShouldBeEmpty() {
    Leaderboard leaderboard = new Leaderboard(null);

    assertThat(leaderboard.entries()).isEmpty();
  }

  @Test
  void givenAnEmptyLeaderboardEntrySetLeaderboardShouldBeEmpty() {
    Leaderboard leaderboard = new Leaderboard(Set.of());

    assertThat(leaderboard.entries()).isEmpty();
  }

  @Test
  void givenALeaderboardEntrySetWithOneEntryLeaderboardShouldContainIt() {
    LeaderboardEntry entry = new LeaderboardEntry(TEST_USERNAME, TEST_SCORE);
    Leaderboard leaderboard = new Leaderboard(Set.of(entry));

    assertThat(leaderboard.entries()).hasSize(1).contains(entry);
  }

  @Test
  void givenALeaderboardEntrySetWithTwoEntryLeaderboardShouldContainThemOrderedByScore() {
    LeaderboardEntry firstEntry = new LeaderboardEntry("first_test_player", "1000");
    LeaderboardEntry secondEntry = new LeaderboardEntry("first_second_player", "500");

    Leaderboard leaderboard = new Leaderboard(Set.of(secondEntry, firstEntry));

    assertThat(leaderboard.entries()).hasSize(2).contains(firstEntry, secondEntry);
    assertThat(leaderboard.entries())
        .extracting(leaderboardEntry -> Integer.parseInt(leaderboardEntry.score()))
        .isSortedAccordingTo(Comparator.reverseOrder());
  }
}
