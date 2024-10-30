package dev.agustinventura.leaderboard.acceptance;

import dev.agustinventura.leaderboard.fixtures.LeaderboardObjectMother;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class CreateLeaderboardEntrySteps {

  private final CommonSteps commonSteps;

  @LocalServerPort
  private int port;

  private RestClient restTestClient;

  private int responseStatus;

  public CreateLeaderboardEntrySteps(CommonSteps commonSteps) {
    this.commonSteps = commonSteps;
  }

  @Before
  public void restClient() {
    restTestClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
  }

  @When("I create a leaderboard entry with values playerName {string} and score {int}")
  public void iCreateALeaderboardEntry(String playerName, int score) {
    responseStatus = restTestClient.post()
        .uri("/v1/leaderboard/entry")
        .contentType(MediaType.APPLICATION_JSON)
        .body(LeaderboardObjectMother.getLeaderboardEntryRESTDTO(playerName, score))
        .retrieve()
        .onStatus(HttpStatusCode::isError, (request, response) -> responseStatus = response.getStatusCode().value())
        .toBodilessEntity()
        .getStatusCode().value();
    commonSteps.setResponseStatus(responseStatus);
  }

}

