package dev.agustinventura.leaderboard.unit.application;

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
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryDeleteNotAllowedException;
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryExistsException;
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryNotExistsException;
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryUpdateNotAllowedException;
import dev.agustinventura.leaderboard.application.ports.out.LoadLeaderboardPort;
import dev.agustinventura.leaderboard.application.ports.out.SaveLeaderboardPort;
import dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

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

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " "})
  void givenAnInvalidPlayerNameCreateShouldThrowIllegalArgumentException(String playerName) {
    Throwable thrown = catchThrowable(() -> leaderboardService.create(playerName, TEST_SCORE));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " ", "not_number", "0"})
  void givenAnInvalidScoreCreateShouldThrowIllegalArgumentException(String score) {
    Throwable thrown = catchThrowable(() -> leaderboardService.create(TEST_PLAYERNAME, score));

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

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " "})
  void givenAnInvalidPlayerNameUpdateShouldThrowIllegalArgumentException(String playerName) {
    Throwable thrown = catchThrowable(() -> leaderboardService.update(playerName, TEST_SCORE, "ADMIN"));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " ", "not_number", "0"})
  void givenAnInvalidScoreUpdateShouldThrowIllegalArgumentException(String score) {
    Throwable thrown = catchThrowable(() -> leaderboardService.update(TEST_PLAYERNAME, score, "ADMIN"));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " "})
  void givenAnInvalidUserNameUpdateShouldThrowLeaderboardEntryUpdateNotAllowedException(String userName) {
    Throwable thrown = catchThrowable(() -> leaderboardService.update(TEST_PLAYERNAME, TEST_SCORE, userName));

    assertThat(thrown).isInstanceOf(LeaderboardEntryUpdateNotAllowedException.class);
  }

  @Test
  void givenANonExistingPlayerNameUpdateShouldThrowLeaderboardEntryNotExistsException() {
    Leaderboard leaderboard = LeaderboardObjectMother.emptyLeaderboard();
    LeaderboardEntry entry = LeaderboardObjectMother.testLeaderboardEntry();
    when(loadLeaderboardPort.load()).thenReturn(leaderboard);

    Throwable thrown = catchThrowable(() -> leaderboardService.update(entry.playerName(), entry.score(), "ADMIN"));

    assertThat(thrown).isInstanceOf(LeaderboardEntryNotExistsException.class);
  }

  @Test
  void givenAnExistingPlayerNameAndABiggerThanZeroScoreAndAdminUserNameUpdateShouldReturnLeaderboardWithUpdatedEntry() {
    Leaderboard leaderboard = LeaderboardObjectMother.twoEntriesLeaderboard();
    LeaderboardEntry testLeaderboardEntry = leaderboard.entries().iterator().next();
    String scoreToAdd = Integer.toString(1);
    int newScore = Integer.parseInt(testLeaderboardEntry.score()) + Integer.parseInt(scoreToAdd);
    when(loadLeaderboardPort.load()).thenReturn(leaderboard);
    when(saveLeaderboardPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    Leaderboard updatedLeaderboard = leaderboardService.update(testLeaderboardEntry.playerName(), scoreToAdd, "ADMIN");

    assertThat(
        updatedLeaderboard.entries().stream().filter(entry -> entry.playerName().equals(testLeaderboardEntry.playerName())).findFirst()
            .get().score()).isEqualTo(Integer.toString(newScore));
  }

  @Test
  void givenAnExistingPlayerNameAndABiggerThanZeroScoreAndCoincidentUserNameUpdateShouldReturnLeaderboardWithUpdatedEntry() {
    Leaderboard leaderboard = LeaderboardObjectMother.twoEntriesLeaderboard();
    LeaderboardEntry testLeaderboardEntry = leaderboard.entries().iterator().next();
    String scoreToAdd = Integer.toString(1);
    int newScore = Integer.parseInt(testLeaderboardEntry.score()) + Integer.parseInt(scoreToAdd);
    when(loadLeaderboardPort.load()).thenReturn(leaderboard);
    when(saveLeaderboardPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    Leaderboard updatedLeaderboard =
        leaderboardService.update(testLeaderboardEntry.playerName(), scoreToAdd, testLeaderboardEntry.playerName());

    assertThat(
        updatedLeaderboard.entries().stream().filter(entry -> entry.playerName().equals(testLeaderboardEntry.playerName())).findFirst()
            .get().score()).isEqualTo(Integer.toString(newScore));
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " "})
  void givenAnInvalidPlayerNameDeleteShouldDoNothing(String playerName) {
    Leaderboard testLeaderboard = LeaderboardObjectMother.oneEntryLeaderboard();
    when(loadLeaderboardPort.load()).thenReturn(testLeaderboard);

    Leaderboard returnedLeaderboard = leaderboardService.delete(playerName, null);

    assertThat(returnedLeaderboard.entries()).hasSize(1);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " "})
  void givenAnInvalidUserNameDeleteShouldThrownLeaderboardEntryDeleteNotAllowedException(String username) {
    Leaderboard testLeaderboard = LeaderboardObjectMother.oneEntryLeaderboard();
    when(loadLeaderboardPort.load()).thenReturn(testLeaderboard);

    Leaderboard returnedLeaderboard = leaderboardService.delete("playerName", username);

    assertThat(returnedLeaderboard.entries()).hasSize(1);
  }

  @Test
  void givenNotCoincidentPlayerNameAndUserNameDeleteShouldThrowLeaderboardEntryNotAllowedException() {
    Leaderboard testLeaderboard = LeaderboardObjectMother.oneEntryLeaderboard();
    when(loadLeaderboardPort.load()).thenReturn(testLeaderboard);

    Throwable thrown = catchThrowable(() -> leaderboardService.delete("playerName", "userName"));

    assertThat(thrown).isInstanceOf(LeaderboardEntryDeleteNotAllowedException.class);
  }

  @Test
  void givenCoincidentPlayerNameAndUserNameShouldDeleteLeaderboardEntry() {
    Leaderboard testLeaderboard = LeaderboardObjectMother.oneEntryLeaderboard();
    when(loadLeaderboardPort.load()).thenReturn(testLeaderboard);
    when(saveLeaderboardPort.save(any())).thenReturn(LeaderboardObjectMother.emptyLeaderboard());
    LeaderboardEntry entry = testLeaderboard.entries().iterator().next();

    Leaderboard returnedLeaderboard = leaderboardService.delete(entry.playerName(), entry.playerName());

    assertThat(returnedLeaderboard.entries()).isEmpty();
  }

  @Test
  void givenAdminUserNameShouldDeleteLeaderboardEntry() {
    Leaderboard testLeaderboard = LeaderboardObjectMother.oneEntryLeaderboard();
    when(loadLeaderboardPort.load()).thenReturn(testLeaderboard);
    when(saveLeaderboardPort.save(any())).thenReturn(LeaderboardObjectMother.emptyLeaderboard());
    LeaderboardEntry entry = testLeaderboard.entries().iterator().next();

    Leaderboard returnedLeaderboard = leaderboardService.delete(entry.playerName(), "admin");

    assertThat(returnedLeaderboard.entries()).isEmpty();
  }
}