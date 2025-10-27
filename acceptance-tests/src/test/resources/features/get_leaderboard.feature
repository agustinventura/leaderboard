Feature: Get Leaderboard

  As a user
  I want to retrieve the leaderboard
  So that I can see all entries sorted by score

  Scenario: Retrieve empty leaderboard
    Given a leaderboard without entries
    When I retrieve the leaderboard
    Then the leaderboard should be empty

  Scenario: Retrieve leaderboard sorted by score
    Given the following leaderboard entries exist:
      | playerName | score |
      | player5    | 100   |
      | player6    | 150   |
      | player7    | 120   |
    When I retrieve the leaderboard
    Then I should see the following leaderboard entries:
      | playerName | score |
      | player6    | 150   |
      | player7    | 120   |
      | player5    | 100   |
