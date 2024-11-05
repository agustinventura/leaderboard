package dev.agustinventura.leaderboard.application.model.exceptions;

public class LeaderboardEntryUpdateNotAllowedException extends LeaderboardDomainException {

  public LeaderboardEntryUpdateNotAllowedException(String message) {
    super(message);
  }
}
