package dev.agustinventura.leaderboard.adapter.in.kafka;

import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardDomainException;
import dev.agustinventura.leaderboard.application.ports.in.CreateLeaderboardEntryUseCase;
import dev.agustinventura.leaderboard.application.ports.in.UpdateLeaderboardEntryUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class LeaderboardKafkaAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(LeaderboardKafkaAdapter.class);

  private final CreateLeaderboardEntryUseCase createUseCase;

  private final UpdateLeaderboardEntryUseCase updateUseCase;

  public LeaderboardKafkaAdapter(CreateLeaderboardEntryUseCase createUseCase,
      UpdateLeaderboardEntryUseCase updateUseCase) {
    this.createUseCase = createUseCase;
    this.updateUseCase = updateUseCase;
  }

  @KafkaListener(topics = "leaderboard-events")
  public void handleLeaderboardEvent(@Payload LeaderboardEntry entry, @Header(value = "X-USERNAME", required = false) String userName) {
    try {
      String playerName = entry.playerName();

      if (isCreate(userName)) {
        createUseCase.create(playerName, entry.score());
      } else {
        updateUseCase.update(playerName, entry.score(), userName);
      }
    } catch (LeaderboardDomainException e) {
      LOGGER.error("Error performing operation on leaderboard", e);
    }
  }

  private boolean isCreate(String userName) {
    return userName == null;
  }
}
