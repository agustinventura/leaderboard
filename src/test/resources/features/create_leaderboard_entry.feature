Feature: Create a leaderboard entry

  Scenario: Successfully create a leaderboard entry
    When I create a leaderboard entry with values playerName "player1" and score 100
    Then the response status should be 201

  Scenario: Attempt to create an existent leaderboard entry
    Given a leaderboard entry with playerName "player2" and score 150
    When I create a leaderboard entry with values playerName "player2" and score 100
    Then the response status should be 400

  Scenario: Attempt to create a leaderboard entry with invalid data
    When I create a leaderboard entry with values playerName "player3" and score -10
    Then the response status should be 400