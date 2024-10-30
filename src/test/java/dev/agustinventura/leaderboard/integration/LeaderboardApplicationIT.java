package dev.agustinventura.leaderboard.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class LeaderboardApplicationIT {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17")
      .withDatabaseName("leaderboard")
      .withUsername("leaderboard")
      .withPassword("leaderboard");

  @Test
  void contextLoads() {
    //Tests if Spring context can be created
  }

}
