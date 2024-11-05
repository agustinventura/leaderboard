package dev.agustinventura.leaderboard.application.model.exceptions;

public class LeaderboardEntryDeleteNotAllowedException extends LeaderboardDomainException {

  public LeaderboardEntryDeleteNotAllowedException(String message) {
    super(message);
  }
}
