package dev.agustinventura.leaderboard.integration.adapter.in.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.agustinventura.leaderboard.adapter.in.rest.LeaderboardRESTAdapter;
import dev.agustinventura.leaderboard.adapter.in.rest.dto.LeaderboardEntryRESTDTO;
import dev.agustinventura.leaderboard.adapter.in.rest.mapper.LeaderboardEntryRESTDTOMapperImpl;
import dev.agustinventura.leaderboard.application.model.Leaderboard;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryExistsException;
import dev.agustinventura.leaderboard.application.model.exceptions.LeaderboardEntryNotExistsException;
import dev.agustinventura.leaderboard.application.ports.in.CreateLeaderboardEntryUseCase;
import dev.agustinventura.leaderboard.application.ports.in.GetLeaderboardUseCase;
import dev.agustinventura.leaderboard.application.ports.in.UpdateLeaderboardEntryUseCase;
import dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LeaderboardRESTAdapter.class)
@Import(LeaderboardEntryRESTDTOMapperImpl.class)
class LeaderboardRESTAdapterIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private GetLeaderboardUseCase getLeaderboardUseCase;

  @MockBean
  private CreateLeaderboardEntryUseCase createLeaderboardEntryUseCase;

  @MockBean
  private UpdateLeaderboardEntryUseCase updateLeaderboardEntryUseCase;

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
    LeaderboardEntry firstEntry = testLeaderboardEntries.get(0);
    LeaderboardEntry secondEntry = testLeaderboardEntries.get(1);
    when(getLeaderboardUseCase.getLeaderboard()).thenReturn(testLeaderboard);

    this.mockMvc
        .perform(
            get("/v1/leaderboard")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))  // Check that the response contains 2 items
        .andExpect(jsonPath("$[0].playerName").value(firstEntry.playerName()))  // First entry playerName
        .andExpect(jsonPath("$[0].score").value(firstEntry.score()))  // First entry score
        .andExpect(jsonPath("$[1].playerName").value(secondEntry.playerName()))  // Second entry playerName
        .andExpect(jsonPath("$[1].score").value(secondEntry.score()));
  }

  @Test
  void givenAnAlreadyExistingEntryCreateShouldReturnBadRequest() throws Exception {
    LeaderboardEntryRESTDTO leaderboardEntryDTO = LeaderboardObjectMother.getLeaderboardEntryRESTDTO();
    when(createLeaderboardEntryUseCase.create(any(), any())).thenThrow(LeaderboardEntryExistsException.class);

    this.mockMvc
        .perform(
            post("/v1/leaderboard/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leaderboardEntryDTO))
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  void givenANonExistingEntryCreateShouldReturnOkAndItsLocation() throws Exception {
    LeaderboardEntryRESTDTO leaderboardEntryDTO = LeaderboardObjectMother.getLeaderboardEntryRESTDTO();

    this.mockMvc
        .perform(
            post("/v1/leaderboard/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leaderboardEntryDTO))
        )
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "http://localhost/v1/leaderboard"));
  }

  @Test
  void givenANonExistingEntryUpdateShouldReturnNotFound() throws Exception {
    LeaderboardEntryRESTDTO leaderboardEntryDTO = LeaderboardObjectMother.getLeaderboardEntryRESTDTO();
    when(updateLeaderboardEntryUseCase.update(any(), any())).thenThrow(LeaderboardEntryNotExistsException.class);

    this.mockMvc
        .perform(
            put("/v1/leaderboard/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leaderboardEntryDTO))
        )
        .andExpect(status().isNotFound());
  }

  @Test
  void givenAnInvalidEntryUpdateShouldReturnBadRequest() throws Exception {
    LeaderboardEntryRESTDTO leaderboardEntryDTO = LeaderboardObjectMother.getLeaderboardEntryRESTDTO();
    when(updateLeaderboardEntryUseCase.update(any(), any())).thenThrow(IllegalArgumentException.class);

    this.mockMvc
        .perform(
            put("/v1/leaderboard/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leaderboardEntryDTO))
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  void givenAValidEntryUpdateShouldReturnAccepted() throws Exception {
    LeaderboardEntryRESTDTO leaderboardEntryDTO = LeaderboardObjectMother.getLeaderboardEntryRESTDTO();

    this.mockMvc
        .perform(
            put("/v1/leaderboard/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leaderboardEntryDTO))
        )
        .andExpect(status().isAccepted());
  }
}
