package dev.agustinventura.leaderboard.application.ports.in;

import dev.agustinventura.leaderboard.application.model.Leaderboard;

public interface CreateLeaderboardEntryUseCase {

  Leaderboard create(String playerName, String score);
}
