package dev.agustinventura.leaderboard.acceptance;

import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

public class DeleteLeaderboardEntrySteps {

  private final CommonSteps commonSteps;

  @LocalServerPort
  private int port;

  private RestClient restTestClient;

  private int responseStatus;

  public DeleteLeaderboardEntrySteps(CommonSteps commonSteps) {
    this.commonSteps = commonSteps;
  }

  @Before
  public void restClient() {
    restTestClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
  }

  @When("I delete the leaderboard entry with playerName {string} and X-USERNAME header {string}")
  public void iCreateALeaderboardEntry(String playerName, String userName) {
    responseStatus = restTestClient.delete()
        .uri("/v1/leaderboard/entry/{playerName}", playerName)
        .header("X-USERNAME", userName)
        .retrieve()
        .onStatus(HttpStatusCode::isError, (request, response) -> responseStatus = response.getStatusCode().value())
        .toBodilessEntity()
        .getStatusCode().value();
    commonSteps.setResponseStatus(responseStatus);
  }

}

