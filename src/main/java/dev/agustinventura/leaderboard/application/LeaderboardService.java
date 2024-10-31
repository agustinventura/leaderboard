package dev.agustinventura.leaderboard.application;

import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryDeleteNotAllowedException;
import dev.agustinventura.leaderboard.application.ports.in.CreateLeaderboardEntryUseCase;
import dev.agustinventura.leaderboard.application.ports.in.DeleteLeaderboardEntryUseCase;
import dev.agustinventura.leaderboard.application.ports.in.GetLeaderboardUseCase;
import dev.agustinventura.leaderboard.application.ports.in.UpdateLeaderboardEntryUseCase;
import dev.agustinventura.leaderboard.application.ports.out.LoadLeaderboardPort;
import dev.agustinventura.leaderboard.application.ports.out.SaveLeaderboardPort;

public class LeaderboardService implements GetLeaderboardUseCase, CreateLeaderboardEntryUseCase, UpdateLeaderboardEntryUseCase,
    DeleteLeaderboardEntryUseCase {

  private final LoadLeaderboardPort loadLeaderboardPort;

  private final SaveLeaderboardPort saveLeaderboardPort;

  public LeaderboardService(LoadLeaderboardPort loadLeaderboardPort, SaveLeaderboardPort saveLeaderboardPort) {
    this.loadLeaderboardPort = loadLeaderboardPort;
    this.saveLeaderboardPort = saveLeaderboardPort;
  }

  @Override
  public Leaderboard getLeaderboard() {
    return loadLeaderboardPort.load();
  }

  @Override
  public Leaderboard create(String playerName, String score) {
    LeaderboardEntry leaderboardEntry = createLeaderboard(playerName, score);
    Leaderboard leaderboard = getLeaderboard();
    leaderboard.add(leaderboardEntry);
    return saveLeaderboardPort.save(leaderboard);
  }

  @Override
  public Leaderboard update(String playerName, String score) {
    LeaderboardEntry leaderboardEntry = createLeaderboard(playerName, score);
    Leaderboard leaderboard = getLeaderboard();
    leaderboard.update(leaderboardEntry);
    return saveLeaderboardPort.save(leaderboard);
  }

  private LeaderboardEntry createLeaderboard(String playerName, String score) {
    validatePlayerName(playerName);
    validateScore(score);
    return new LeaderboardEntry(playerName, score);
  }

  private void validatePlayerName(String playerName) {
    if (playerName == null || playerName.isBlank()) {
      throw new IllegalArgumentException("Player name must not be null or empty");
    }
  }

  private void validateScore(String score) {
    if (score == null) {
      throw new IllegalArgumentException("Score must not be null");
    }

    try {
      int scoreValue = Integer.parseInt(score);
      if (scoreValue < 1) {
        throw new IllegalArgumentException("Score must be greater than one");
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Score must be a valid integer");
    }
  }

  @Override
  public Leaderboard delete(String playerName, String userName) {
    Leaderboard leaderboard = getLeaderboard();
    if (playerName != null && !playerName.isBlank() && userName != null && !userName.isBlank()) {
      if (userName.equalsIgnoreCase(playerName) || userName.equalsIgnoreCase("ADMIN")) {
        leaderboard.delete(playerName);
        saveLeaderboardPort.save(leaderboard);
      } else {
        throw new LeaderboardEntryDeleteNotAllowedException(
            "User %s is not allowed to delete player %s entry".formatted(userName, playerName));
      }

    }
    return leaderboard;
  }
}
