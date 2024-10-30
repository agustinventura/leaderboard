package dev.agustinventura.leaderboard.application.ports.out;

import dev.agustinventura.leaderboard.application.model.Leaderboard;

public interface SaveLeaderboardPort {

  Leaderboard save(Leaderboard leaderboard);
}
