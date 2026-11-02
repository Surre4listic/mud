-- docker exec -i mud mysql -dbpass -dbuser < core-creation.sql
-- docker exec -i mysql-lecture3 mysql -uroot -prootpass < demo_02_docker_init.sql
-- docker exec -it <container_namn> mysql -u root -p
-- docker exec -it mud mysql -u root -p


CREATE TABLE players (
    ID int PRIMARY KEY,
    username varchar(25) NOT NULL,
    password varchar(60) NOT NULL,
    experience int NOT NULL,
    strength int NOT NULL,
    dexterity int NOT NULL,
    intelligence int NOT NULL,
    room_id int NOT NULL
);