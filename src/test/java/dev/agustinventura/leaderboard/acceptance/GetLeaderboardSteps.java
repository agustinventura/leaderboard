package dev.agustinventura.leaderboard.acceptance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
import dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class GetLeaderboardSteps {

  @LocalServerPort
  private int port;

  private RestClient restTestClient;

  private String leaderboard;

  @Before
  public void restClient() {
    restTestClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
  }

  @Given("the following leaderboard entries exist:")
  public void theFollowingLeaderboardEntriesExist(List<Map<String, String>> entries) {
    for (Map<String, String> entry : entries) {
      String playerName = String.valueOf(entry.get("playerName"));
      int score = Integer.parseInt(entry.get("score"));

      restTestClient.post()
          .uri("/v1/leaderboard/entry")
          .contentType(MediaType.APPLICATION_JSON)
          .body(LeaderboardObjectMother.getLeaderboardEntryRESTDTO(playerName, score))
          .retrieve()
          .toBodilessEntity();
    }
  }

  @When("I retrieve the leaderboard")
  public void iRetrieveTheLeaderboard() {
    leaderboard = restTestClient
        .get()
        .uri("/v1/leaderboard")
        .retrieve()
        .body(String.class);
  }

  @Then("I should see the following leaderboard entries:")
  public void iShouldSeeTheFollowingLeaderboardEntries(List<Map<String, String>> expectedEntries) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    List<LeaderboardEntry> actualEntries = objectMapper.readValue(leaderboard, new TypeReference<>() {
    });

    for (Map<String, String> expectedEntry : expectedEntries) {
      String expectedUsername = expectedEntry.get("playerName");
      String expectedScore = expectedEntry.get("score");

      LeaderboardEntry actualEntry =
          actualEntries.stream().filter(entry -> entry.playerName().equals(expectedUsername)).findFirst().orElseThrow();
      assert actualEntry.playerName().equals(expectedUsername) : "Username does not match!";
      assert actualEntry.score().equals(expectedScore) : "Score does not match!";
    }
  }
}

