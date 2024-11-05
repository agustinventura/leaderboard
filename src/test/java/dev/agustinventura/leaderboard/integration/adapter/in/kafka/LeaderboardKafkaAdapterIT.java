package dev.agustinventura.leaderboard.integration.adapter.in.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import dev.agustinventura.leaderboard.application.ports.in.CreateLeaderboardEntryUseCase;
import dev.agustinventura.leaderboard.application.ports.in.DeleteLeaderboardEntryUseCase;
import dev.agustinventura.leaderboard.application.ports.in.GetLeaderboardUseCase;
import dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother;
import java.time.Duration;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest
@Testcontainers
class LeaderboardKafkaAdapterIT {

  @Container
  @ServiceConnection
  static KafkaContainer kafkaContainer =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.7.1"));

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>("postgres:17");

  @Autowired
  private KafkaTemplate<String, LeaderboardEntry> kafkaTemplate;

  @Autowired
  private GetLeaderboardUseCase getLeaderboardUseCase;

  @Autowired
  private CreateLeaderboardEntryUseCase createLeaderboardEntryUseCase;

  @Autowired
  private DeleteLeaderboardEntryUseCase deleteLeaderboardEntryUseCase;

  private static final String TOPIC = "leaderboard-events";

  private LeaderboardEntry existingEntry;

  @BeforeEach
  void setUp() {
    existingEntry = LeaderboardObjectMother.testLeaderboardEntry();
    createLeaderboardEntryUseCase.create(existingEntry.playerName(), existingEntry.score());
  }

  @AfterEach
  void tearDown() {
    deleteLeaderboardEntryUseCase.delete(LeaderboardObjectMother.testLeaderboardEntry().playerName(), LeaderboardObjectMother.ADMIN_USERNAME);
  }

  @Test
  void givenANewLeaderboardEntryShouldCreateIt() {
    LeaderboardEntry newEntry = new LeaderboardEntry("new_player", LeaderboardObjectMother.TEST_SCORE);

    Message<LeaderboardEntry> entryMessage =
        MessageBuilder.withPayload(newEntry).setHeader(KafkaHeaders.TOPIC, TOPIC).setHeader(KafkaHeaders.KEY, newEntry.playerName())
            .build();
    kafkaTemplate.send(entryMessage);

    await()
        .pollInterval(Duration.ofSeconds(3))
        .atMost(Duration.ofSeconds(20))
        .untilAsserted(() -> {
          Set<LeaderboardEntry> entries = getLeaderboardUseCase.getLeaderboard().entries();
          assertThat(entries)
              .anyMatch(createdEntry ->
                  createdEntry.playerName().equals(newEntry.playerName())
                  && createdEntry.score().equals(newEntry.score()));
        });
  }

  @Test
  void givenANonExistingLeaderboardEntryUpdateMessageShouldLogError(CapturedOutput capturedOutput) {
    LeaderboardEntry nonExistingEntry = new LeaderboardEntry(LeaderboardObjectMother.NOT_EXISTING_PLAYERNAME, LeaderboardObjectMother.TEST_SCORE);

    Message<LeaderboardEntry> entryMessage =
        MessageBuilder.withPayload(nonExistingEntry).setHeader(KafkaHeaders.TOPIC, TOPIC)
            .setHeader(KafkaHeaders.KEY, nonExistingEntry.playerName())
            .setHeader(LeaderboardObjectMother.USERNAME_HEADER, LeaderboardObjectMother.NOT_EXISTING_PLAYERNAME).build();
    kafkaTemplate.send(entryMessage);

    await()
        .pollInterval(Duration.ofSeconds(3))
        .atMost(Duration.ofSeconds(20))
        .untilAsserted(() -> assertThat(capturedOutput.getOut()).contains("Error performing operation on leaderboard"));
  }

  @Test
  void givenANotAuthorizedLeaderboardEntryUpdateMessageShouldLogError(CapturedOutput capturedOutput) {
    Message<LeaderboardEntry> entryMessage =
        MessageBuilder.withPayload(new LeaderboardEntry(existingEntry.playerName(), LeaderboardObjectMother.TEST_SCORE)).setHeader(KafkaHeaders.TOPIC, TOPIC)
            .setHeader(KafkaHeaders.KEY, existingEntry.playerName())
            .setHeader(LeaderboardObjectMother.USERNAME_HEADER, LeaderboardObjectMother.NOT_EXISTING_PLAYERNAME).build();

    kafkaTemplate.send(entryMessage);

    await()
        .pollInterval(Duration.ofSeconds(3))
        .atMost(Duration.ofSeconds(20))
        .untilAsserted(() -> assertThat(capturedOutput.getOut()).contains("Error performing operation on leaderboard"));
  }

  @Test
  void givenAPlayerAuthorizedLeaderboardEntryUpdateMessageShouldUpdateLeaderboard() {
    Message<LeaderboardEntry> entryMessage =
        MessageBuilder.withPayload(new LeaderboardEntry(existingEntry.playerName(), LeaderboardObjectMother.TEST_SCORE)).setHeader(KafkaHeaders.TOPIC, TOPIC)
            .setHeader(KafkaHeaders.KEY, existingEntry.playerName())
            .setHeader(LeaderboardObjectMother.USERNAME_HEADER, existingEntry.playerName()).build();
    String expectedScore = Integer.toString(Integer.parseInt(existingEntry.score()) + Integer.parseInt(LeaderboardObjectMother.TEST_SCORE));
    kafkaTemplate.send(entryMessage);

    await()
        .pollInterval(Duration.ofSeconds(3))
        .atMost(Duration.ofSeconds(20))
        .untilAsserted(() -> {
          Set<LeaderboardEntry> entries = getLeaderboardUseCase.getLeaderboard().entries();
          assertThat(entries)
              .anyMatch(createdEntry ->
                  createdEntry.playerName().equals(existingEntry.playerName())
                  && createdEntry.score().equals(expectedScore));
        });
  }

  @Test
  void givenAnAdminAuthorizedLeaderboardEntryUpdateMessageShouldUpdateLeaderboard() {
    Message<LeaderboardEntry> entryMessage =
        MessageBuilder.withPayload(new LeaderboardEntry(existingEntry.playerName(), LeaderboardObjectMother.TEST_SCORE)).setHeader(KafkaHeaders.TOPIC, TOPIC)
            .setHeader(KafkaHeaders.KEY, existingEntry.playerName())
            .setHeader(LeaderboardObjectMother.USERNAME_HEADER, LeaderboardObjectMother.ADMIN_USERNAME).build();
    String expectedScore = Integer.toString(Integer.parseInt(existingEntry.score()) + Integer.parseInt(LeaderboardObjectMother.TEST_SCORE));
    kafkaTemplate.send(entryMessage);

    await()
        .pollInterval(Duration.ofSeconds(3))
        .atMost(Duration.ofSeconds(20))
        .untilAsserted(() -> {
          Set<LeaderboardEntry> entries = getLeaderboardUseCase.getLeaderboard().entries();
          assertThat(entries)
              .anyMatch(createdEntry ->
                  createdEntry.playerName().equals(existingEntry.playerName())
                  && createdEntry.score().equals(expectedScore));
        });
  }
}
