CREATE TABLE leaderboard_entry
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_name VARCHAR(100) NOT NULL UNIQUE,
    total_score VARCHAR(100)     DEFAULT '0',
    CHECK (leaderboard_entry.player_name <> '')
);