package dev.agustinventura.leaderboard.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestClient;

@ContextConfiguration
public class CommonSteps {

  private int responseStatus;

  @LocalServerPort
  private int port;

  private RestClient restTestClient;

  @Before
  public void restClient() {
    restTestClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
  }

  @Given("a leaderboard entry with playerName {string} and score {int}")
  public void aLeaderboardEntryWithPlayerNameAndScore(String playerName, int score) {
    restTestClient.post()
        .uri("/v1/leaderboard/entry")
        .contentType(MediaType.APPLICATION_JSON)
        .body(LeaderboardObjectMother.getLeaderboardEntryRESTDTO(playerName, score))
        .retrieve()
        .toBodilessEntity();
  }

  @Then("the response status should be {int}")
  public void theResponseStatusShouldBe(int expectedStatus) {
    assertThat(responseStatus).isEqualTo(expectedStatus);
  }

  public void setResponseStatus(int responseStatus) {
    this.responseStatus = responseStatus;
  }

}
