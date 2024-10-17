package dev.agustinventura.leaderboard.application.model;

public record LeaderboardEntry(String username, String score ) {

  public LeaderboardEntry {
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("Username must not be null or empty");
    }

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
}
