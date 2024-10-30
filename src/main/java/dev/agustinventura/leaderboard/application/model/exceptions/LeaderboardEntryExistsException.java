package dev.agustinventura.leaderboard.application.model.exceptions;

public class LeaderboardEntryExistsException extends RuntimeException {

  public LeaderboardEntryExistsException(String message) {
    super(message);
  }
}
