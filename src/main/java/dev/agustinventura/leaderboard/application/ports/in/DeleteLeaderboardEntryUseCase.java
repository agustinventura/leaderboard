package dev.agustinventura.leaderboard.application.ports.in;

import dev.agustinventura.leaderboard.application.model.Leaderboard;

public interface DeleteLeaderboardEntryUseCase {

  Leaderboard delete(String playerName, String userName);
}
