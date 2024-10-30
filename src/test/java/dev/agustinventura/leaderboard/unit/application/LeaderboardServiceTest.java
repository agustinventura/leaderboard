package dev.agustinventura.leaderboard.unit.application;

import static dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother.BLANK_STRING;
import static dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother.EMPTY_STRING;
import static dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother.INVALID_FORMAT_SCORE;
import static dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother.LOWER_THAN_ALLOWED_SCORE;
import static dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother.TEST_PLAYERNAME;
import static dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother.TEST_SCORE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.agustinventura.leaderboard.application.LeaderboardService;
import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryExistsException;
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryNotExistsException;
import dev.agustinventura.leaderboard.application.ports.out.LoadLeaderboardPort;
import dev.agustinventura.leaderboard.application.ports.out.SaveLeaderboardPort;
import dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaderboardServiceTest {

  private LeaderboardService leaderboardService;

  private LoadLeaderboardPort loadLeaderboardPort;

  private SaveLeaderboardPort saveLeaderboardPort;

  @BeforeEach
  void setUp() {
    loadLeaderboardPort = mock(LoadLeaderboardPort.class);
    saveLeaderboardPort = mock(SaveLeaderboardPort.class);
    this.leaderboardService = new LeaderboardService(loadLeaderboardPort, saveLeaderboardPort);
  }

  @Test
  void givenAnEmptyLeaderboardShouldReturnIt() {
    when(loadLeaderboardPort.load()).thenReturn(LeaderboardObjectMother.emptyLeaderboard());

    Leaderboard leaderboard = leaderboardService.getLeaderboard();

    assertThat(leaderboard.entries()).isEmpty();
  }

  @Test
  void givenALeaderboardWithEntriesShouldReturnIt() {
    Leaderboard testLeaderboard = LeaderboardObjectMother.twoEntriesLeaderboard();
    when(loadLeaderboardPort.load()).thenReturn(testLeaderboard);

    Leaderboard returnedLeaderboard = leaderboardService.getLeaderboard();

    assertThat(returnedLeaderboard).isEqualTo(testLeaderboard);
  }

  @Test
  void givenANullPlayerNameCreateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.create(null, TEST_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenAnEmptyPlayerNameCreateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.create(EMPTY_STRING, TEST_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenABlankPlayerNameCreateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.create(BLANK_STRING, TEST_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenANullScoreCreateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.create(TEST_PLAYERNAME, null));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenAnEmptyScoreCreateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.create(TEST_PLAYERNAME, EMPTY_STRING));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenABlankScoreCreateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.create(TEST_PLAYERNAME, BLANK_STRING));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenANonNumberScoreCreateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.create(TEST_PLAYERNAME, INVALID_FORMAT_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenALowerThanAllowedScoreCreateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.create(TEST_PLAYERNAME, LOWER_THAN_ALLOWED_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenAnExistingPlayerNameCreateShouldThrowLeaderboardEntryAlreadyExistsException() {
    Leaderboard leaderboard = LeaderboardObjectMother.twoEntriesLeaderboard();
    LeaderboardEntry entry = leaderboard.entries().iterator().next();
    when(loadLeaderboardPort.load()).thenReturn(leaderboard);

    Throwable thrown = catchThrowable(() -> leaderboardService.create(entry.playerName(), entry.score()));

    assertThat(thrown).isInstanceOf(LeaderboardEntryExistsException.class);
  }

  @Test
  void givenANonExistingPlayerNameAndABiggerThanZeroScoreShouldReturnUpdatedLeaderboard() {
    Leaderboard leaderboard = LeaderboardObjectMother.twoEntriesLeaderboard();
    LeaderboardEntry testLeaderboardEntry = LeaderboardObjectMother.testLeaderboardEntry();
    when(loadLeaderboardPort.load()).thenReturn(leaderboard);
    when(saveLeaderboardPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    Leaderboard updatedLeaderboard = leaderboardService.create(testLeaderboardEntry.playerName(), testLeaderboardEntry.score());

    assertThat(updatedLeaderboard.entries()).contains(testLeaderboardEntry);
  }

  @Test
  void givenANullPlayerNameUpdateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.update(null, TEST_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenAnEmptyPlayerNameUpdateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.update(EMPTY_STRING, TEST_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenABlankPlayerNameUpdateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.update(BLANK_STRING, TEST_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenANullScoreUpdateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.update(TEST_PLAYERNAME, null));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenAnEmptyScoreUpdateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.update(TEST_PLAYERNAME, EMPTY_STRING));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenABlankScoreUpdateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.update(TEST_PLAYERNAME, BLANK_STRING));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenANonNumberScoreUpdateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.update(TEST_PLAYERNAME, INVALID_FORMAT_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenALowerThanAllowedScoreUpdateShouldThrowIllegalArgumentException() {
    Throwable thrown = catchThrowable(() -> leaderboardService.update(TEST_PLAYERNAME, LOWER_THAN_ALLOWED_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenANonExistingPlayerNameCreateShouldThrowLeaderboardEntryNotExistsException() {
    Leaderboard leaderboard = LeaderboardObjectMother.emptyLeaderboard();
    LeaderboardEntry entry = LeaderboardObjectMother.testLeaderboardEntry();
    when(loadLeaderboardPort.load()).thenReturn(leaderboard);

    Throwable thrown = catchThrowable(() -> leaderboardService.update(entry.playerName(), entry.score()));

    assertThat(thrown).isInstanceOf(LeaderboardEntryNotExistsException.class);
  }

  @Test
  void givenAnExistingPlayerNameAndABiggerThanZeroScoreShouldReturnLeaderboardWithUpdatedEntry() {
    Leaderboard leaderboard = LeaderboardObjectMother.twoEntriesLeaderboard();
    LeaderboardEntry testLeaderboardEntry = leaderboard.entries().iterator().next();
    String scoreToAdd = Integer.toString(1);
    int newScore = Integer.parseInt(testLeaderboardEntry.score()) + Integer.parseInt(scoreToAdd);
    when(loadLeaderboardPort.load()).thenReturn(leaderboard);
    when(saveLeaderboardPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    Leaderboard updatedLeaderboard = leaderboardService.update(testLeaderboardEntry.playerName(), scoreToAdd);

    assertThat(
        updatedLeaderboard.entries().stream().filter(entry -> entry.playerName().equals(testLeaderboardEntry.playerName())).findFirst()
            .get().score()).isEqualTo(Integer.toString(newScore));
  }
}