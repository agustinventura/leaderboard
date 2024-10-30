package dev.agustinventura.leaderboard.adapter.in.rest.mapper;

import dev.agustinventura.leaderboard.adapter.in.rest.dto.LeaderboardEntryRESTDTO;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LeaderboardEntryRESTDTOMapper {

  LeaderboardEntryRESTDTO toDTO(LeaderboardEntry leaderboardEntry);

  Set<LeaderboardEntryRESTDTO> toDTOs(Set<LeaderboardEntry> leaderboardEntries);
}
