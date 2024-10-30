Feature: Get Leaderboard

  As a user
  I want to retrieve the leaderboard
  So that I can see all entries with their scores

  Scenario: Retrieve leaderboard with entries
    Given the following leaderboard entries exist:
      | playerName | score |
      | player3    | 100   |
      | player4    | 150   |
      | player5    | 120   |
    When I retrieve the leaderboard
    Then I should see the following leaderboard entries:
      | playerName | score |
      | player4    | 150   |
      | player5    | 120   |
      | player3    | 100   |
