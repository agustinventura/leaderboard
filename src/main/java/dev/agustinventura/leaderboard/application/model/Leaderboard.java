package dev.agustinventura.leaderboard.application.model;

import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryExistsException;
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryNotExistsException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public record Leaderboard(Set<LeaderboardEntry> entries) {

  private static final Comparator<LeaderboardEntry> SCORE_COMPARATOR =
      Comparator.comparingInt((LeaderboardEntry entry) -> Integer.parseInt(entry.score()))
          .reversed()
          .thenComparing(LeaderboardEntry::playerName);

  public Leaderboard(Set<LeaderboardEntry> entries) {
    this.entries = new TreeSet<>(SCORE_COMPARATOR);
    if (entries != null && !entries.isEmpty()) {
      this.entries.addAll(entries);
    }
  }

  public void add(LeaderboardEntry entry) {
    if (entry != null) {
      if (containsPlayerName(entry.playerName())) {
        throw new LeaderboardEntryExistsException("Leaderboard entry already exists");
      }
      this.entries.add(entry);
    }
  }

  private boolean containsPlayerName(String playerName) {
    return entries.stream().anyMatch(entry -> entry.playerName().equals(playerName));
  }

  public void update(LeaderboardEntry entry) {
    if (entry != null) {
      LeaderboardEntry existingEntry = entries.stream().filter(entry1 -> entry1.playerName().equals(entry.playerName())).findFirst()
          .orElseThrow(() -> new LeaderboardEntryNotExistsException("Leaderboard entry not exists"));
      LeaderboardEntry updatedEntry = new LeaderboardEntry(existingEntry.playerName(),
          Integer.toString(Integer.parseInt(existingEntry.score()) + Integer.parseInt(entry.score())));
      this.entries.remove(existingEntry);
      this.entries.add(updatedEntry);
    }
  }
}
