package dev.agustinventura.leaderboard.application.model.exceptions;

public class LeaderboardEntryNotExistsException extends RuntimeException {

  public LeaderboardEntryNotExistsException(String message) {
    super(message);
  }
}
