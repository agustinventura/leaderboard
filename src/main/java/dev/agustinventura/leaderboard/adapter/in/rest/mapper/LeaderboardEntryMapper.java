package dev.agustinventura.leaderboard.adapter.in.rest.mapper;

import dev.agustinventura.leaderboard.adapter.in.rest.dto.LeaderboardEntryDTO;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LeaderboardEntryMapper {

  LeaderboardEntryDTO toDTO(LeaderboardEntry leaderboardEntry);

  Set<LeaderboardEntryDTO> toDTOs(Set<LeaderboardEntry> leaderboardEntries);
}
