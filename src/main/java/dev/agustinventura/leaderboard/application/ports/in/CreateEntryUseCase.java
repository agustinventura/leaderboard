package dev.agustinventura.leaderboard.application.ports.in;

public interface CreateEntryUseCase {

  void create(String username, String score);
}
