package dev.agustinventura.leaderboard.unit.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother;
import dev.agustinventura.leaderboard.adapter.in.rest.LeaderboardRESTAdapter;
import dev.agustinventura.leaderboard.adapter.in.rest.dto.LeaderboardEntryDTO;
import dev.agustinventura.leaderboard.adapter.in.rest.mapper.LeaderboardEntryMapper;
import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import dev.agustinventura.leaderboard.application.ports.in.GetLeaderboardUseCase;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;

class LeaderboardRESTAdapterTest {

  private LeaderboardRESTAdapter leaderboardRESTAdapter;

  private GetLeaderboardUseCase getLeaderboardUseCase;

  @BeforeEach
  void setUp() {
    getLeaderboardUseCase = mock(GetLeaderboardUseCase.class);
    LeaderboardEntryMapper leaderboardEntryMapper = Mappers.getMapper(LeaderboardEntryMapper.class);
    leaderboardRESTAdapter = new LeaderboardRESTAdapter(getLeaderboardUseCase, leaderboardEntryMapper);
  }

  @Test
  void givenNoEntriesShouldReturnEmptyLeaderboard() {
    when(getLeaderboardUseCase.getLeaderboard()).thenReturn(LeaderboardObjectMother.emptyLeaderboard());
    ResponseEntity<List<LeaderboardEntryDTO>> leaderboard = leaderboardRESTAdapter.getLeaderboard();

    assertTrue(Objects.requireNonNull(leaderboard.getBody()).isEmpty());
  }

  @Test
  void givenTwoEntriesShouldReturnLeaderboardWithEntriesOrderedByScore() {
    Leaderboard testLeaderboard = LeaderboardObjectMother.twoEntriesLeaderboard();
    when(getLeaderboardUseCase.getLeaderboard()).thenReturn(testLeaderboard);

    ResponseEntity<List<LeaderboardEntryDTO>> obtainedLeaderboard = leaderboardRESTAdapter.getLeaderboard();

    assertFalse(Objects.requireNonNull(obtainedLeaderboard.getBody()).isEmpty());
    List<LeaderboardEntryDTO> entries = obtainedLeaderboard.getBody();
    assertThat(entries).hasSize(2);
    assertThat(entries)
        .extracting(LeaderboardEntryDTO::getScore)
        .isSortedAccordingTo(Comparator.reverseOrder());
    assertThat(entries).extracting(LeaderboardEntryDTO::getUsername)
        .containsAll(testLeaderboard.entries().stream().map(LeaderboardEntry::username).toList());
  }

}