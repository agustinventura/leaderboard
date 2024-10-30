package dev.agustinventura.leaderboard.adapter.out.jdbc;

import dev.agustinventura.leaderboard.adapter.out.jdbc.dto.LeaderboardEntryJDBCDTO;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface LeaderboardEntryJDBCRepository extends CrudRepository<LeaderboardEntryJDBCDTO, UUID> {

}
