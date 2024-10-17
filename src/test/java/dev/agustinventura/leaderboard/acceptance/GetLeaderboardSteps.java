package dev.agustinventura.leaderboard.acceptance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.agustinventura.leaderboard.application.model.LeaderboardEntry;
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

  @Before
  public void restClient() {
    restTestClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
  }

  private String leaderboard;

  @Given("the following leaderboard entries exist:")
  public void theFollowingLeaderboardEntriesExist(List<Map<String, String>> entries) {
    for (Map<String, String> entry : entries) {
      String username = entry.get("player");
      String score = entry.get("score");

      restTestClient.post()
          .uri("/v1/leaderboard")
          .contentType(MediaType.APPLICATION_JSON)
          .body(new LeaderboardEntry(username, score))
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
    List<LeaderboardEntry> actualEntries = objectMapper.readValue(leaderboard, new TypeReference<>() {});

    for (int i = 0; i < expectedEntries.size(); i++) {
      String expectedUsername = expectedEntries.get(i).get("player");
      String expectedScore = expectedEntries.get(i).get("score");

      LeaderboardEntry actualEntry = actualEntries.get(i);
      assert actualEntry.username().equals(expectedUsername) : "Username does not match!";
      assert actualEntry.score().equals(expectedScore) : "Score does not match!";
    }
  }
}

