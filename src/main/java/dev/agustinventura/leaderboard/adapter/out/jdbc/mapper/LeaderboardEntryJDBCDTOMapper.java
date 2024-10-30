package dev.agustinventura.leaderboard.adapter.out.jdbc.mapper;

import dev.agustinventura.leaderboard.adapter.out.jdbc.dto.LeaderboardEntryJDBCDTO;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LeaderboardEntryJDBCDTOMapper {

  Set<LeaderboardEntry> toEntitySet(Iterable<LeaderboardEntryJDBCDTO> leaderboardEntryDTO);

  LeaderboardEntryJDBCDTO toDTO(LeaderboardEntry leaderboardEntry);
}
