package dev.agustinventura.leaderboard.application.model.exceptions;

public class LeaderboardEntryUpdateNotAllowedException extends RuntimeException {

  public LeaderboardEntryUpdateNotAllowedException(String message) {
    super(message);
  }
}
