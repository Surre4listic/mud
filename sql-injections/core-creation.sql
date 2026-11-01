-- Recreate the database and table for players
DROP DATABASE IF EXISTS database;
CREATE TABLE IF NOT EXISTS database;
USE database;

DROP TABLE players;

CREATE TABLE players (
    ID int PRIMARY KEY,
    username varchar(25) NOT NULL,
    password varchar(60) NOT NULL,
    exeperience int NOT NULL,
    strength int NOT NULL,
    dexterity int NOT NULL,
    intelligence int NOT NULL
);