package dev.agustinventura.leaderboard.adapter.out.jdbc;

import dev.agustinventura.leaderboard.adapter.out.jdbc.dto.LeaderboardEntryJDBCDTO;
import dev.agustinventura.leaderboard.adapter.out.jdbc.mapper.LeaderboardEntryJDBCDTOMapper;
import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import dev.agustinventura.leaderboard.application.ports.out.LoadLeaderboardPort;
import dev.agustinventura.leaderboard.application.ports.out.SaveLeaderboardPort;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class LeaderboardJDBCAdapter implements LoadLeaderboardPort, SaveLeaderboardPort {

  private final LeaderboardEntryJDBCRepository leaderboardEntryJDBCRepository;

  private final LeaderboardEntryJDBCDTOMapper leaderboardEntryJDBCDTOMapper;

  public LeaderboardJDBCAdapter(LeaderboardEntryJDBCRepository leaderboardEntryJDBCRepository,
      LeaderboardEntryJDBCDTOMapper leaderboardEntryJDBCDTOMapper) {
    this.leaderboardEntryJDBCRepository = leaderboardEntryJDBCRepository;
    this.leaderboardEntryJDBCDTOMapper = leaderboardEntryJDBCDTOMapper;
  }

  @Override
  public Leaderboard load() {
    Iterable<LeaderboardEntryJDBCDTO> leaderboardEntries = leaderboardEntryJDBCRepository.findAll();

    return new Leaderboard(leaderboardEntryJDBCDTOMapper.toEntitySet(leaderboardEntries));
  }

  @Override
  public Leaderboard save(Leaderboard leaderboard) {
    Set<LeaderboardEntryJDBCDTO> savedEntries = new HashSet<>();
    leaderboardEntryJDBCRepository.findAll().forEach(savedEntries::add);
    for (LeaderboardEntry entry : leaderboard.entries()) {
      if (entryIsNotInLeaderboard(entry, savedEntries)) {
        createEntry(leaderboardEntryJDBCDTOMapper.toDTO(entry));
      } else {
        updateEntry(entry, savedEntries);
      }
    }
    for (LeaderboardEntryJDBCDTO entry : savedEntries) {
      if (savedEntryIsNotInLeaderboard(entry, leaderboard)) {
        deleteEntry(entry);
      }
    }
    return load();
  }

  private void deleteEntry(LeaderboardEntryJDBCDTO entry) {
    leaderboardEntryJDBCRepository.delete(entry);
  }

  private boolean savedEntryIsNotInLeaderboard(LeaderboardEntryJDBCDTO entry, Leaderboard leaderboard) {
    return leaderboard.entries().stream().noneMatch(leaderboardEntry -> leaderboardEntry.playerName().equalsIgnoreCase(entry.getPlayerName()));
  }

  private void createEntry(LeaderboardEntryJDBCDTO leaderboardEntryJDBCDTO) {
    leaderboardEntryJDBCRepository.save(leaderboardEntryJDBCDTO);
  }

  private void updateEntry(LeaderboardEntry entry, Set<LeaderboardEntryJDBCDTO> savedEntries) {
    LeaderboardEntryJDBCDTO updatedEntry = leaderboardEntryJDBCDTOMapper.toDTO(entry);
    Optional<LeaderboardEntryJDBCDTO> existingEntry =
        savedEntries.stream().filter(savedEntry -> savedEntry.getPlayerName().equalsIgnoreCase(entry.playerName())).findFirst();
    if (existingEntry.isPresent()) {
      updatedEntry.setId(existingEntry.get().getId());
      leaderboardEntryJDBCRepository.save(updatedEntry);
    }
  }

  private boolean entryIsNotInLeaderboard(LeaderboardEntry entry, Set<LeaderboardEntryJDBCDTO> savedEntries) {
    return savedEntries.stream().noneMatch(savedEntry -> savedEntry.getPlayerName().equalsIgnoreCase(entry.playerName()));
  }
}
