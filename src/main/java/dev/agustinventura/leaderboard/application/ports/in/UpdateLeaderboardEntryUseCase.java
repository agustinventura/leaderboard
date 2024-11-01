package dev.agustinventura.leaderboard.application.ports.in;

import dev.agustinventura.leaderboard.application.model.Leaderboard;

public interface UpdateLeaderboardEntryUseCase {

  Leaderboard update(String playerName, String score, String userName);
}
