Feature: Delete a leaderboard entry
  As a user or admin
  I want to delete a leaderboard entry
  So that I can remove specific entries from the leaderboard

  Scenario: Successfully delete a leaderboard entry as the player
    Given a leaderboard entry with playerName "player4" and score 100
    When I delete the leaderboard entry with playerName "player4" and X-USERNAME header "player4"
    Then the response status should be 204

  Scenario: Successfully delete a leaderboard entry as admin
    Given a leaderboard entry with playerName "player4" and score 150
    When I delete the leaderboard entry with playerName "player4" and X-USERNAME header "admin"
    Then the response status should be 204

  Scenario: Attempt to delete a leaderboard entry with an unauthorized user
    Given a leaderboard entry with playerName "player4" and score 120
    When I delete the leaderboard entry with playerName "player4" and X-USERNAME header "unauthorizedUser"
    Then the response status should be 403

  Scenario: Attempt to delete a non-existent leaderboard entry
    When I delete the leaderboard entry with playerName "nonExistentPlayer" and X-USERNAME header "admin"
    Then the response status should be 204
