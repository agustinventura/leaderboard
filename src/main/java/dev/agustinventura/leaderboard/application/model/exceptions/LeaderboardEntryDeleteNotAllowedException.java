package dev.agustinventura.leaderboard.application.model.exceptions;

public class LeaderboardEntryDeleteNotAllowedException extends RuntimeException {

  public LeaderboardEntryDeleteNotAllowedException(String message) {
    super(message);
  }
}
