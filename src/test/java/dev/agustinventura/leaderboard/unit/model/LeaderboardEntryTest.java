package dev.agustinventura.leaderboard.unit.model;

import static dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother.TEST_PLAYERNAME;
import static dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother.TEST_SCORE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class LeaderboardEntryTest {

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " "})
  void givenAnInvalidPlayerNameShouldThrowIllegalArgumentException(String playerName) {
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(playerName, TEST_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " ", "not valid", "0"})
  void givenAnInvalidScoreShouldThrowIllegalArgumentException(String score) {
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(TEST_PLAYERNAME, score));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenANonNullEmptyOrBlankUsernameAndABiggerThanZeroScoreShouldReturnLeaderboardEntry() {
    LeaderboardEntry leaderboardEntry = new LeaderboardEntry(TEST_PLAYERNAME, TEST_SCORE);

    assertThat(leaderboardEntry.playerName()).isEqualTo(TEST_PLAYERNAME);
    assertThat(leaderboardEntry.score()).isEqualTo(TEST_SCORE);
  }
}
