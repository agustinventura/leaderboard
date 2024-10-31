Feature: Update an existing leaderboard entry

  Scenario: Successfully update a leaderboard entry
    Given a leaderboard entry with playerName "player8" and score 150
    When I update a leaderboard entry with values playerName "player8" and score 100
    Then the response status should be 202

  Scenario: Attempt to update a non-existent leaderboard entry
    Given a leaderboard entry with playerName "player9" and score 150
    When I update a leaderboard entry with values playerName "player" and score 100
    Then the response status should be 404

  Scenario: Attempt to update a leaderboard entry with invalid data
    Given a leaderboard entry with playerName "player10" and score 150
    When I update a leaderboard entry with values playerName "player10" and score -10
    Then the response status should be 400
