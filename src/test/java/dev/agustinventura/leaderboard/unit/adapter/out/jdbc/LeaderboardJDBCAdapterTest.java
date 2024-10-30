package dev.agustinventura.leaderboard.unit.adapter.out.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.agustinventura.leaderboard.adapter.out.jdbc.LeaderboardEntryJDBCRepository;
import dev.agustinventura.leaderboard.adapter.out.jdbc.LeaderboardJDBCAdapter;
import dev.agustinventura.leaderboard.adapter.out.jdbc.dto.LeaderboardEntryJDBCDTO;
import dev.agustinventura.leaderboard.adapter.out.jdbc.mapper.LeaderboardEntryJDBCDTOMapper;
import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class LeaderboardJDBCAdapterTest {

  private LeaderboardEntryJDBCRepository leaderboardEntryJDBCRepository;

  private LeaderboardEntryJDBCDTOMapper leaderboardEntryJDBCDTOMapper;

  private LeaderboardJDBCAdapter leaderboardJDBCAdapter;

  @BeforeEach
  void setUp() {
    leaderboardEntryJDBCRepository = mock(LeaderboardEntryJDBCRepository.class);
    leaderboardEntryJDBCDTOMapper = Mappers.getMapper(LeaderboardEntryJDBCDTOMapper.class);
    leaderboardJDBCAdapter = new LeaderboardJDBCAdapter(leaderboardEntryJDBCRepository, leaderboardEntryJDBCDTOMapper);
  }

  @Test
  void givenNoEntriesShouldReturnEmptyLeaderboard() {
    when(leaderboardEntryJDBCRepository.findAll()).thenReturn(new ArrayList<>());

    Leaderboard loadedLeaderboard = leaderboardJDBCAdapter.load();

    assertThat(loadedLeaderboard.entries()).isEmpty();
  }

  @Test
  void givenOneEntryShouldReturnLeaderboardContainingIt() {
    LeaderboardEntry testEntry = LeaderboardObjectMother.testLeaderboardEntry();
    LeaderboardEntryJDBCDTO testEntryJDBCDTO = leaderboardEntryJDBCDTOMapper.toDTO(testEntry);
    when(leaderboardEntryJDBCRepository.findAll()).thenReturn(List.of(testEntryJDBCDTO));

    Leaderboard loadedLeaderboard = leaderboardJDBCAdapter.load();

    assertThat(loadedLeaderboard.entries()).hasSize(1).contains(testEntry);
  }

  @Test
  void givenANewEntryShouldSaveIt() {
    Leaderboard testLeaderboard = LeaderboardObjectMother.oneEntryLeaderboard();
    LeaderboardEntry testEntry = testLeaderboard.entries().iterator().next();
    LeaderboardEntryJDBCDTO testEntryJDBCDTO = leaderboardEntryJDBCDTOMapper.toDTO(testEntry);
    when(leaderboardEntryJDBCRepository.findAll()).thenReturn(List.of()).thenReturn(List.of(testEntryJDBCDTO));

    Leaderboard savedLeaderboard = leaderboardJDBCAdapter.save(testLeaderboard);

    assertThat(savedLeaderboard.entries()).hasSize(1);
    assertThat(savedLeaderboard.entries()).contains(testEntry);
  }

  @Test
  void givenAnExistingEntryShouldUpdateIt() {
    Leaderboard testLeaderboard = LeaderboardObjectMother.oneEntryLeaderboard();
    LeaderboardEntry testEntry = testLeaderboard.entries().iterator().next();
    LeaderboardEntryJDBCDTO testEntryJDBCDTO = new LeaderboardEntryJDBCDTO(UUID.randomUUID(), testEntry.playerName(), testEntry.score());
    when(leaderboardEntryJDBCRepository.findAll()).thenReturn(List.of()).thenReturn(List.of(testEntryJDBCDTO));

    Leaderboard savedLeaderboard = leaderboardJDBCAdapter.save(testLeaderboard);

    assertThat(savedLeaderboard.entries()).hasSize(1);
    assertThat(savedLeaderboard.entries()).contains(testEntry);
  }
}
