Feature: Update an existing leaderboard entry
  As a user or admin
  I want to modify the score in a leaderboard entry
  So that I can update scores in the leaderboard

  Scenario: Successfully update a leaderboard entry as the player
    Given a leaderboard entry with playerName "player8" and score 150
    When I update a leaderboard entry with values playerName "player8" and score 100 and X-USERNAME header "player8"
    Then the response status should be 202

  Scenario: Successfully update a leaderboard entry as an admin
    Given a leaderboard entry with playerName "player9" and score 150
    When I update a leaderboard entry with values playerName "player9" and score 100 and X-USERNAME header "admin"
    Then the response status should be 202

  Scenario: Attempt to update a leaderboard entry with an unauthorized user
    Given a leaderboard entry with playerName "player10" and score 120
    When I update a leaderboard entry with values playerName "player10" and score 100 and X-USERNAME header "unauthorizedUser"
    Then the response status should be 403

  Scenario: Attempt to update a non-existent leaderboard entry
    Given a leaderboard entry with playerName "player11" and score 150
    When I update a leaderboard entry with values playerName "player" and score 100 and X-USERNAME header "admin"
    Then the response status should be 404

  Scenario: Attempt to update a leaderboard entry with invalid data
    Given a leaderboard entry with playerName "player12" and score 150
    When I update a leaderboard entry with values playerName "player12" and score -10 and X-USERNAME header "player12"
    Then the response status should be 400
