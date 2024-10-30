package dev.agustinventura.leaderboard.adapter.out.jdbc.dto;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("leaderboard_entry")
public class LeaderboardEntryJDBCDTO {

  @Id
  private UUID id;

  @Column("player_name")
  private String playerName;

  @Column("total_score")
  private String score;

  public LeaderboardEntryJDBCDTO(UUID id, String playerName, String score) {
    this.id = id;
    this.playerName = playerName;
    this.score = score;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public String getScore() {
    return score;
  }

  public void setScore(String score) {
    this.score = score;
  }
}
