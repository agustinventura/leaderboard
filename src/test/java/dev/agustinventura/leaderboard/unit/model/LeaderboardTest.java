package dev.agustinventura.leaderboard.unit.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryExistsException;
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryNotExistsException;
import dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
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
    LeaderboardEntry entry = LeaderboardObjectMother.testLeaderboardEntry();
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

  @Test
  void givenALeaderboardWithoutEntriesIsEmptyShouldReturnTrue() {
    Leaderboard leaderboard = LeaderboardObjectMother.emptyLeaderboard();

    assertThat(leaderboard.entries()).isEmpty();
  }

  @Test
  void givenALeaderboardWithEntriesIsEmptyShouldReturnFalse() {
    Leaderboard leaderboard = LeaderboardObjectMother.twoEntriesLeaderboard();

    assertThat(leaderboard.entries()).isNotEmpty();
  }

  @Test
  void givenANullEntryAddShouldDoNothing() {
    Leaderboard leaderboard = LeaderboardObjectMother.emptyLeaderboard();

    leaderboard.add(null);

    assertThat(leaderboard.entries()).isEmpty();
  }

  @Test
  void givenALeaderboardEntryAddShouldAddIt() {
    Leaderboard leaderboard = LeaderboardObjectMother.emptyLeaderboard();

    leaderboard.add(LeaderboardObjectMother.testLeaderboardEntry());

    assertThat(leaderboard.entries()).isNotEmpty();
  }

  @Test
  void givenALeaderboardWithEntriesAndAnEntryAddShouldAddEntryOrderedByScore() {
    Leaderboard leaderboard = LeaderboardObjectMother.twoEntriesLeaderboard();

    leaderboard.add(LeaderboardObjectMother.testLeaderboardEntry());

    LeaderboardEntry addedEntry = ((TreeSet<LeaderboardEntry>) leaderboard.entries()).last();
    assertThat(addedEntry.playerName()).isEqualTo(LeaderboardObjectMother.TEST_PLAYERNAME);
    assertThat(addedEntry.score()).isEqualTo(LeaderboardObjectMother.TEST_SCORE);
  }

  @Test
  void givenALeaderboardWithEntriesAndAnEntryWithTheSameScoreAsAnExistingOneShouldAddItOrderedByPlayerName() {
    Leaderboard leaderboard = LeaderboardObjectMother.emptyLeaderboard();
    LeaderboardEntry firstEntry = new LeaderboardEntry("a", "100");
    LeaderboardEntry secondEntry = new LeaderboardEntry("b", "100");

    leaderboard.add(firstEntry);
    leaderboard.add(secondEntry);

    assertThat(leaderboard.entries()).hasSize(2).contains(firstEntry, secondEntry);
    assertThat(leaderboard.entries().iterator().next()).isEqualTo(firstEntry);
  }

  @Test
  void givenAnExistingPlayerNameAddShouldThrowLeaderboardEntryExistsException() {
    Leaderboard leaderboard = LeaderboardObjectMother.emptyLeaderboard();
    LeaderboardEntry entry = LeaderboardObjectMother.testLeaderboardEntry();

    leaderboard.add(entry);
    Throwable thrown = catchThrowable(() -> leaderboard.add(entry));

    assertThat(thrown).isInstanceOf(LeaderboardEntryExistsException.class);
  }

  @Test
  void givenANullEntryUpdateShouldDoNothing() {
    Leaderboard leaderboard = LeaderboardObjectMother.emptyLeaderboard();

    leaderboard.update(null);

    assertThat(leaderboard.entries()).isEmpty();
  }

  @Test
  void givenANotExistingPlayerNameUpdateShouldThrowLeaderboardEntryNotExistsException() {
    Leaderboard leaderboard = LeaderboardObjectMother.emptyLeaderboard();
    LeaderboardEntry entry = LeaderboardObjectMother.testLeaderboardEntry();

    Throwable thrown = catchThrowable(() -> leaderboard.update(entry));

    assertThat(thrown).isInstanceOf(LeaderboardEntryNotExistsException.class);
  }

  @Test
  void givenAnExistingPlayerNameUpdateShouldSetNewScoreAsTheSumOfTheExistingOneAndTheProvidedOne() {
    Leaderboard leaderboard = LeaderboardObjectMother.oneEntryLeaderboard();
    LeaderboardEntry existingEntry = leaderboard.entries().iterator().next();
    String existingScore = existingEntry.score();
    LeaderboardEntry updatedEntry = new LeaderboardEntry(existingEntry.playerName(), LeaderboardObjectMother.TEST_SCORE);

    leaderboard.update(updatedEntry);

    assertThat(leaderboard.entries().iterator().next().score()).isEqualTo(
        Integer.toString(Integer.parseInt(existingScore) + Integer.parseInt(LeaderboardObjectMother.TEST_SCORE)));
  }
}
