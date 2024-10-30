# About

This is a test project to learn and apply different concepts.

# Leaderboard

The covered domain is an online game leaderboard with the following characteristics.

## 1. **Objective**

- The primary goal is to **rank players globally** based on their all-time points in the game.
- The system will handle scores from casual players all over the world, reflecting global competition.

## 2. **Feature Set**

- **Ranking Criteria:**
    - Players will be ranked based on their points, with each new score reflecting their current standing.
    - Only one **global leaderboard** will be available, showing all-time high scores.
- **Real-Time Updates:**
    - The leaderboard will update in real time, displaying changes as soon as new scores are submitted via Kafka or the admin API.
- **Player Profiles:**
    - The leaderboard will display **only the player’s username** and their **score**.
- **No Seasonality or Rewards:**
    - No resets, meaning the leaderboard will track players' scores from the very start, with no in-game rewards or incentives tied to
      leaderboard standings.

# User Stories

We have three kind of users:

- **Users**: Anyone can query the leaderboard.
- **Players**: Users which submit scores to the leaderboard.
- **Admins**: Users which can adjust scores and delete other users.

Based on this, we have six user stories:

## Query leaderboard

As a user I want to be able to query the leaderboard to get all players position ordered by all-time points in the game.

## Player related user stories

### New player

As new player I want to submit my username and my score and if my username is not in use and the score is bigger than zero, store the
username and the score so I can compare my score with other players.

### Existing player

As an existing player I want to submit my score and if it is bigger than zero, sum it to the previous existing score, so I can keep my score
updated for comparison purposes.

## Admin related user stories

### Reset player score

As an admin I want to be able to set a player score to zero given its username so it can start again submitting scores.

### Adjust player score

As an admin I want to be able to add or subtract an amount from a player score given its username and being the minimum score zero so the
score is real.

### Delete user

As an admin I want to be able to delete an entry from the leaderboard given its username so the player no longer competes.

# Architecture

As we want to assure maximum flexibility to test and apply different concepts and technologies we lean on
a [hexagonal/onion architecture](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software))
![Hexagonal Architecture](https://raw.githubusercontent.com/agustinventura/leaderboard/docs/img/hexagonal_architecture.svg)

In this schema we have the following elements:

- **Frameworks**: broad spectrum libraries which provide a great number of functions and utilities (from HTTP servers to database drivers)
  with the caveat of tangling our code with theirs.
- **Input Adapters**: receives application inputs with the help of frameworks and invokes application ports to process these inputs.
- **Input Ports**: interfaces which define the application logic or use cases. Also called primary ports.
- **Services**: implementation of ports to accomplish application logic interacting with the domain.
- **Output Ports**: interfaces which define how does the application interact with external systems. Also called secondary ports.
- **Output Adapters**: implementation of output ports with the help of frameworks.

Given our initial feature set and the selected frameworks we will have this diagram:
![Leaderboard Hexagonal Architecture](https://raw.githubusercontent.com/agustinventura/leaderboard/docs/img/leaderboard_hexagonal_architecture.svg)

# Design

# Stack and tools

- **Java 21**
- Cucumber