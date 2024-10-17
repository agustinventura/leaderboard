package dev.agustinventura.leaderboard.integration.adapter.in.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother;
import dev.agustinventura.leaderboard.adapter.in.rest.LeaderboardRESTAdapter;
import dev.agustinventura.leaderboard.adapter.in.rest.mapper.LeaderboardEntryMapperImpl;
import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import dev.agustinventura.leaderboard.application.ports.in.GetLeaderboardUseCase;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LeaderboardRESTAdapter.class)
@Import(LeaderboardEntryMapperImpl.class)
class LeaderboardRESTAdapterIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private GetLeaderboardUseCase getLeaderboardUseCase;

  @Test
  void givenNoEntriesShouldReturnEmptyList() throws Exception {
    when(getLeaderboardUseCase.getLeaderboard()).thenReturn(LeaderboardObjectMother.emptyLeaderboard());
    this.mockMvc
        .perform(
            get("/v1/leaderboard")
        )
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  @Test
  void givenTwoEntriesShouldReturnLeaderboardEntriesOrderedByScore() throws Exception {
    Leaderboard testLeaderboard = LeaderboardObjectMother.twoEntriesLeaderboard();
    List<LeaderboardEntry> testLeaderboardEntries = testLeaderboard.entries().stream().toList();
    LeaderboardEntry firstEntry  = testLeaderboardEntries.get(0);
    LeaderboardEntry secondEntry  = testLeaderboardEntries.get(1);
    when(getLeaderboardUseCase.getLeaderboard()).thenReturn(testLeaderboard);

    this.mockMvc
        .perform(
            get("/v1/leaderboard")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))  // Check that the response contains 2 items
        .andExpect(jsonPath("$[0].username").value(firstEntry.username()))  // First entry username
        .andExpect(jsonPath("$[0].score").value(firstEntry.score()))  // First entry score
        .andExpect(jsonPath("$[1].username").value(secondEntry.username()))  // Second entry username
        .andExpect(jsonPath("$[1].score").value(secondEntry.score()));
  }
}
