-- Table: Game
CREATE TABLE IF NOT EXISTS Game (
    date VARCHAR(10),
    season INT,
    neutral BOOLEAN,
    playoff VARCHAR(20),
    team1 VARCHAR(50),
    team2 VARCHAR(50),
    elo1_pre DOUBLE,
    elo2_pre DOUBLE,
    elo_prob1 DOUBLE,
    elo_prob2 DOUBLE,
    elo1_post DOUBLE,
    elo2_post DOUBLE,
    rating1_pre DOUBLE,
    rating2_pre DOUBLE,
    pitcher1 VARCHAR(50),
    pitcher2 VARCHAR(50),
    pitcher1_rgs DOUBLE,
    pitcher2_rgs DOUBLE,
    pitcher1_adj DOUBLE,
    pitcher2_adj DOUBLE,
    rating_prob1 DOUBLE,
    rating_prob2 DOUBLE,
    rating1_post DOUBLE,
    rating2_post DOUBLE,
    score1 INT,
    score2 INT
);

-- Table: Team
CREATE TABLE IF NOT EXISTS Team (
    abbreviation VARCHAR(10),
    eloRating DOUBLE,
    mlbRating DOUBLE
);
-- Table: Player
CREATE TABLE IF NOT EXISTS Player (name VARCHAR(100));

-- Table: EloRatingSystem
CREATE TABLE IF NOT EXISTS EloRatingSystem (teamName VARCHAR(50), rating DOUBLE);

-- Table: MLBPredictionRatingSystem
CREATE TABLE IF NOT EXISTS MLBPredictionRatingSystem (teamName VARCHAR(50), rating DOUBLE);