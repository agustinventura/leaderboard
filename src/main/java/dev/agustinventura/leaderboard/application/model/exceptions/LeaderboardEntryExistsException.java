package dev.agustinventura.leaderboard.application.model.exceptions;

public class LeaderboardEntryExistsException extends LeaderboardDomainException {

  public LeaderboardEntryExistsException(String message) {
    super(message);
  }
}
