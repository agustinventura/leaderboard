package dev.agustinventura.leaderboard.adapters.in.rest;

import dev.agustinventura.leaderboard.adapters.in.rest.dto.LeaderboardEntryRESTDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LeaderboardRestAdapterTest {

    private final LeaderboardRestAdapter sut = new LeaderboardRestAdapter();

    @Test
    @DisplayName("Given a leaderboard without entries, it should return an empty list")
    void givenALeaderboardWithoutEntriesShouldReturnEmptyLeaderboard() {
        ResponseEntity<List<LeaderboardEntryRESTDTO>> response = sut.getLeaderboard();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEmpty();
    }
}
