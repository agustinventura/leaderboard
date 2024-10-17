Feature: Get Leaderboard

  As a user
  I want to retrieve the leaderboard
  So that I can see all entries with their scores

  Scenario: Retrieve leaderboard with entries
    Given the following leaderboard entries exist:
      | player        | score      |
      | player1       | 100        |
      | player2       | 150        |
      | player3       | 120        |
    When I retrieve the leaderboard
    Then I should see the following leaderboard entries:
      | player        | score      |
      | player2       | 150        |
      | player3       | 120        |
      | player1       | 100        |
