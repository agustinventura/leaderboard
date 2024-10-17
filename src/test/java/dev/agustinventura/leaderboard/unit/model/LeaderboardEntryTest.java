package dev.agustinventura.leaderboard.unit.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import org.junit.jupiter.api.Test;

class LeaderboardEntryTest {

  public static final String TEST_SCORE = "1";

  public static final String EMPTY_STRING = "";

  public static final String BLANK_STRING = " ";

  public static final String TEST_USERNAME = "test_username";

  public static final String INVALID_FORMAT_SCORE = "test";

  public static final String LOWER_THAN_ALLOWED_SCORE = "0";

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
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(TEST_USERNAME, null));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenAnEmptyScoreShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(TEST_USERNAME, EMPTY_STRING));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenABlankScoreShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(TEST_USERNAME, BLANK_STRING));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenANonNumberScoreShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(TEST_USERNAME, INVALID_FORMAT_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenALowerThanOneScoreShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> new LeaderboardEntry(TEST_USERNAME, LOWER_THAN_ALLOWED_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenANonNullEmptyOrBlankUsernameAndABiggerThanZeroScoreShouldReturnLeaderboardEntry() {
    LeaderboardEntry leaderboardEntry = new LeaderboardEntry(TEST_USERNAME, TEST_SCORE);

    assertThat(leaderboardEntry.username()).isEqualTo(TEST_USERNAME);
    assertThat(leaderboardEntry.score()).isEqualTo(TEST_SCORE);
  }
}
