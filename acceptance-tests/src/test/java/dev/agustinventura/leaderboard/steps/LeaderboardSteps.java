package dev.agustinventura.leaderboard.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import static io.restassured.RestAssured.given;

public class LeaderboardSteps {

    @LocalServerPort
    private int port;

    private Response response;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Given("a leaderboard without entries")
    public void a_leaderboard_without_entries() {

    }

    @Given("the following leaderboard entries exist:")
    public void the_following_leaderboard_entries_exist(DataTable dataTable) {
        List<Map<String, String>> entries = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> entry : entries) {
            String requestBody = String.format(
                    "{\"playerName\": \"%s\", \"score\": %s}",
                    entry.get("playerName"),
                    entry.get("score")
            );

            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/leaderboard-entries")
                    .then()
                    .statusCode(201);
        }
    }

    @When("I retrieve the leaderboard")
    public void i_retrieve_the_leaderboard() {
        response = given()
                .when()
                .get("/leaderboard");
    }

    @Then("the leaderboard should be empty")
    public void the_leaderboard_should_be_empty() {
        assertThat(response.getStatusCode()).isEqualTo(200);

        List<Object> responseBody = response.jsonPath().getList("$");
        assertThat(responseBody).isEmpty();
    }

    @Then("I should see the following leaderboard entries:")
    public void i_should_see_the_following_leaderboard_entries(DataTable dataTable) {
        List<Map<String, String>> expectedEntries = dataTable.asMaps(String.class, String.class);

        assertThat(response.getStatusCode()).isEqualTo(200);
        List<Map<String, Object>> responseBody = response.jsonPath().getList("$");

        assertThat(responseBody)
                .hasSize(expectedEntries.size())
                .extracting("playerName", "score")
                .containsExactly(
                        expectedEntries.stream()
                                .map(row -> tuple(
                                        row.get("playerName"),
                                        Integer.parseInt(row.get("score"))
                                ))
                                .toArray(org.assertj.core.groups.Tuple[]::new)
                );
    }
}
