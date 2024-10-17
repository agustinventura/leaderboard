package dev.agustinventura.leaderboard.application.model;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public record Leaderboard(Set<LeaderboardEntry> entries) {

  private static final Comparator<LeaderboardEntry> SCORE_COMPARATOR =
      Comparator.comparingInt((LeaderboardEntry entry) -> Integer.parseInt(entry.score())).reversed();

  public Leaderboard(Set<LeaderboardEntry> entries) {
    this.entries = new TreeSet<>(SCORE_COMPARATOR);
    if (entries != null && !entries.isEmpty()) {
      this.entries.addAll(entries);
    }
  }
}
