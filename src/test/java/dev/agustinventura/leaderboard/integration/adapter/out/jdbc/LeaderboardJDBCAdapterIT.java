package dev.agustinventura.leaderboard.integration.adapter.out.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import dev.agustinventura.leaderboard.adapter.out.jdbc.LeaderboardEntryJDBCRepository;
import dev.agustinventura.leaderboard.adapter.out.jdbc.LeaderboardJDBCAdapter;
import dev.agustinventura.leaderboard.adapter.out.jdbc.mapper.LeaderboardEntryJDBCDTOMapper;
import dev.agustinventura.leaderboard.adapter.out.jdbc.mapper.LeaderboardEntryJDBCDTOMapperImpl;
import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJdbcTest
@Testcontainers
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
@Import(LeaderboardEntryJDBCDTOMapperImpl.class)
class LeaderboardJDBCAdapterIT {

  public static final String TEST_PLAYER_NAME = "test";

  public static final String TEST_SCORE = "1234";

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>("postgres:17");

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private LeaderboardEntryJDBCRepository leaderboardEntryJDBCRepository;

  @Autowired
  private LeaderboardEntryJDBCDTOMapper leaderboardEntryJDBCDTOMapper;

  private LeaderboardJDBCAdapter adapter;

  @BeforeEach
  void setUp() {
    this.adapter = new LeaderboardJDBCAdapter(leaderboardEntryJDBCRepository, leaderboardEntryJDBCDTOMapper);
  }

  @Test
  void givenNoLeaderboardEntriesShouldReturnEmptyLeaderboard() {
    Leaderboard loadedLeaderboard = adapter.load();

    assertThat(loadedLeaderboard.entries()).isEmpty();
  }

  @Test
  void givenALeaderboardEntryShouldReturnALeaderboardContainingIt() {
    insertTestLeaderboardEntry();

    Leaderboard loadedLeaderboard = adapter.load();

    assertThat(loadedLeaderboard.entries()).hasSize(1);
    LeaderboardEntry entry = loadedLeaderboard.entries().iterator().next();
    assertThat(entry.playerName()).isEqualTo(TEST_PLAYER_NAME);
    assertThat(entry.score()).isEqualTo(TEST_SCORE);
  }

  private void insertTestLeaderboardEntry() {
    jdbcTemplate.execute(
        "INSERT INTO leaderboard_entry(player_name, total_score) VALUES ('" + TEST_PLAYER_NAME + "', '" + TEST_SCORE + "')");
  }

  @Test
  void givenAnExistingLeaderboardEntryShouldUpdateIt() {
    insertTestLeaderboardEntry();
    Leaderboard loadedLeaderboard = adapter.load();
    assertThat(loadedLeaderboard.entries()).hasSize(1);

    Leaderboard savedLeaderboard = adapter.save(loadedLeaderboard);

    assertThat(savedLeaderboard).isEqualTo(loadedLeaderboard);
  }

  @Test
  void givenANonExistingLeaderboardEntryShouldSaveIt() {
    insertTestLeaderboardEntry();
    Leaderboard loadedLeaderboard = adapter.load();
    assertThat(loadedLeaderboard.entries()).hasSize(1);
    loadedLeaderboard.add(LeaderboardObjectMother.testLeaderboardEntry());

    Leaderboard savedLeaderboard = adapter.save(loadedLeaderboard);

    assertThat(savedLeaderboard.entries()).hasSize(2);
  }
}