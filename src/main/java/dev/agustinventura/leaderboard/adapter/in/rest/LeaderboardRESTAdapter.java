package dev.agustinventura.leaderboard.adapter.in.rest;

import dev.agustinventura.leaderboard.adapter.in.rest.dto.LeaderboardEntryRESTDTO;
import dev.agustinventura.leaderboard.adapter.in.rest.mapper.LeaderboardEntryRESTDTOMapper;
import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryExistsException;
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryNotExistsException;
import dev.agustinventura.leaderboard.application.ports.in.CreateLeaderboardEntryUseCase;
import dev.agustinventura.leaderboard.application.ports.in.GetLeaderboardUseCase;
import dev.agustinventura.leaderboard.application.ports.in.UpdateLeaderboardEntryUseCase;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class LeaderboardRESTAdapter implements V1Api {

  private final GetLeaderboardUseCase getLeaderboardUseCase;

  private final CreateLeaderboardEntryUseCase createLeaderboardEntryUseCase;

  private final UpdateLeaderboardEntryUseCase updateLeaderboardEntryUseCase;

  private final LeaderboardEntryRESTDTOMapper leaderboardEntryRESTDTOMapper;

  public LeaderboardRESTAdapter(GetLeaderboardUseCase getLeaderboardUseCase, CreateLeaderboardEntryUseCase createLeaderboardEntryUseCase,
      UpdateLeaderboardEntryUseCase updateLeaderboardEntryUseCase,
      LeaderboardEntryRESTDTOMapper leaderboardEntryRESTDTOMapper) {
    this.getLeaderboardUseCase = getLeaderboardUseCase;
    this.createLeaderboardEntryUseCase = createLeaderboardEntryUseCase;
    this.updateLeaderboardEntryUseCase = updateLeaderboardEntryUseCase;
    this.leaderboardEntryRESTDTOMapper = leaderboardEntryRESTDTOMapper;
  }

  @Override
  public ResponseEntity<List<LeaderboardEntryRESTDTO>> getLeaderboard() {
    Leaderboard leaderboard = getLeaderboardUseCase.getLeaderboard();
    return ResponseEntity.ok().body(leaderboardEntryRESTDTOMapper.toDTOs(leaderboard.entries()).stream().toList());
  }

  @Override
  public ResponseEntity<Void> postLeaderboardEntry(LeaderboardEntryRESTDTO leaderboardEntryDTO) {
    createLeaderboardEntryUseCase.create(leaderboardEntryDTO.getPlayerName(), String.valueOf(leaderboardEntryDTO.getScore()));
    URI location = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path("/v1/leaderboard")
        .build()
        .toUri();
    return ResponseEntity.created(location).build();
  }

  @Override
  public ResponseEntity<Void> putLeaderboardEntry(LeaderboardEntryRESTDTO leaderboardEntryRESTDTO) {
    updateLeaderboardEntryUseCase.update(leaderboardEntryRESTDTO.getPlayerName(), String.valueOf(leaderboardEntryRESTDTO.getScore()));
    return ResponseEntity.accepted().build();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(LeaderboardEntryExistsException.class)
  public ResponseEntity<String> handleLeaderboardEntryExistsException(LeaderboardEntryExistsException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(LeaderboardEntryNotExistsException.class)
  public ResponseEntity<String> handleLeaderboardEntryNotExistsException(LeaderboardEntryNotExistsException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }
}
