package dev.agustinventura.leaderboard.application.model.exceptions;

public class LeaderboardDomainException extends RuntimeException {
  public LeaderboardDomainException(String message) {
    super(message);
  }
}
