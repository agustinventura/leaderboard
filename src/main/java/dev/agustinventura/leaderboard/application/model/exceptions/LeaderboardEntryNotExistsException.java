package dev.agustinventura.leaderboard.application.model.exceptions;

public class LeaderboardEntryNotExistsException extends LeaderboardDomainException {

  public LeaderboardEntryNotExistsException(String message) {
    super(message);
  }
}
