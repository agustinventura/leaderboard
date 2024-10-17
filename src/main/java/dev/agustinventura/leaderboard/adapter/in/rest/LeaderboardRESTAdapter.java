package dev.agustinventura.leaderboard.adapter.in.rest;

import dev.agustinventura.leaderboard.adapter.in.rest.dto.LeaderboardEntryDTO;
import dev.agustinventura.leaderboard.adapter.in.rest.mapper.LeaderboardEntryMapper;
import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.ports.in.GetLeaderboardUseCase;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LeaderboardRESTAdapter implements V1Api {

  private final GetLeaderboardUseCase getLeaderboardUseCase;

  private final LeaderboardEntryMapper leaderboardEntryMapper;

  public LeaderboardRESTAdapter(GetLeaderboardUseCase getLeaderboardUseCase, LeaderboardEntryMapper leaderboardEntryMapper) {
    this.getLeaderboardUseCase = getLeaderboardUseCase;
    this.leaderboardEntryMapper = leaderboardEntryMapper;
  }

  @Override
  public ResponseEntity<List<LeaderboardEntryDTO>> getLeaderboard() {
    Leaderboard leaderboard = getLeaderboardUseCase.getLeaderboard();
    return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(leaderboardEntryMapper.toDTOs(leaderboard.entries()).stream().toList());
  }

  @Override
  public ResponseEntity<Void> postLeaderboard(LeaderboardEntryDTO leaderboardEntryDTO) throws Exception {
    return V1Api.super.postLeaderboard(leaderboardEntryDTO);
  }
}
