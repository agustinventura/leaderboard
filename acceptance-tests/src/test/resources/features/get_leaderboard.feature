Feature: Get Leaderboard

  As a user
  I want to retrieve the leaderboard
  So that I can see all entries sorted by score

  Scenario: Retrieve empty leaderboard
    Given a leaderboard without entries
    When I retrieve the leaderboard
    Then the leaderboard should be empty