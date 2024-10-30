package dev.agustinventura.leaderboard.unit.model;

import static dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother.BLANK_STRING;
import static dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother.EMPTY_STRING;
import static dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother.INVALID_FORMAT_SCORE;
import static dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother.LOWER_THAN_ALLOWED_SCORE;
import static dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother.TEST_PLAYERNAME;
import static dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother.TEST_SCORE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import org.junit.jupiter.api.Test;

class LeaderboardEntryTest {

  @Test
  void givenANullUsernameShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(null, TEST_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenAnEmptyUsernameShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(EMPTY_STRING, TEST_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenABlankUsernameShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(BLANK_STRING, TEST_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenANullScoreShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(TEST_PLAYERNAME, null));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenAnEmptyScoreShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(TEST_PLAYERNAME, EMPTY_STRING));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenABlankScoreShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(TEST_PLAYERNAME, BLANK_STRING));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenANonNumberScoreShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(TEST_PLAYERNAME, INVALID_FORMAT_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenALowerThanOneScoreShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(TEST_PLAYERNAME, LOWER_THAN_ALLOWED_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenANonNullEmptyOrBlankUsernameAndABiggerThanZeroScoreShouldReturnLeaderboardEntry() {
    LeaderboardEntry leaderboardEntry = new LeaderboardEntry(TEST_PLAYERNAME, TEST_SCORE);

    assertThat(leaderboardEntry.playerName()).isEqualTo(TEST_PLAYERNAME);
    assertThat(leaderboardEntry.score()).isEqualTo(TEST_SCORE);
  }
}
