-- -- Table: Game
CREATE TABLE IF NOT EXISTS GameTable (
    uuid VARCHAR(50),
    season INT,
    neutral BOOLEAN,
    playoff VARCHAR(20),
    team1 VARCHAR(50),
team2 VARCHAR(50),
    score1 INT,
    score2 INT
);

-- CREATE TABLE IF NOT EXISTS Person (
--     name VARCHAR(255),
--     age int
-- );

-- -- -- Table: Team
-- -- CREATE TABLE IF NOT EXISTS TeamTable (
-- --     abbreviation VARCHAR(10),
-- --     eloRating DOUBLE,
-- --     mlbRating DOUBLE
-- -- );
-- -- -- Table: Player
-- -- CREATE TABLE IF NOT EXISTS PlayerTable (name VARCHAR(100));
-- -- -- Table: EloRatingSystem
-- -- CREATE TABLE IF NOT EXISTS EloRatingSystemTable (teamName VARCHAR(50), rating DOUBLE);
-- -- -- Table: MLBPredictionRatingSystem
-- -- CREATE TABLE IF NOT EXISTS MLBPredictionRatingSystemTable (teamName VARCHAR(50), rating DOUBLE);