create extension hstore;
create schema movies;

-- Creating tables
-- Actor table
create table if not exists movies."Actor" ("actor_id" BIGSERIAL NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL);

-- ActorDetails table
create table if not exists movies."ActorDetails" ("id" bigserial NOT NULL PRIMARY KEY,"actor_id" BIGINT NOT NULL,"personal_info" jsonb NOT NULL);

-- Movie table
create table if not exists movies."Movie" ("movie_id" BIGSERIAL NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL,"release_date" DATE NOT NULL,"length_in_min" INTEGER NOT NULL);

-- MovieActorMapping table
create table if not exists movies."MovieActorMapping" ("movie_actor_id" BIGSERIAL NOT NULL PRIMARY KEY,"movie_id" BIGINT NOT NULL,"actor_id" BIGINT NOT NULL);

-- StreamingProviderMapping table
create table if not exists movies."StreamingProviderMapping" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"movie_id" BIGINT NOT NULL,"streaming_provider" VARCHAR NOT NULL);

-- MovieLocations table
create table if not exists movies."MovieLocations" ("movie_location_id" BIGSERIAL NOT NULL PRIMARY KEY,"movie_id" BIGINT NOT NULL,"locations" text [] NOT NULL);

-- MovieProperties table
create table if not exists movies."MovieProperties" ("id" bigserial NOT NULL PRIMARY KEY,"movie_id" BIGINT NOT NULL,"properties" jsonb NOT NULL);

-- Inserting data

-- Inserting data for actors
insert into movies."Actor" ("actor_id", "name") values (1, 'Tim Robbins');
insert into movies."Actor" ("actor_id", "name") values (2, 'Morgan Freeman');
insert into movies."Actor" ("actor_id", "name") values (3, 'Tom Hanks');
insert into movies."Actor" ("actor_id", "name") values (4, 'Tom Hardy');
insert into movies."Actor" ("actor_id", "name") values (5, 'Tom Cruise');
insert into movies."Actor" ("actor_id", "name") values (6, 'Al Pacino');

-- Inserting data for actor details
INSERT INTO movies."ActorDetails" ("id","actor_id","personal_info") VALUES (1,1,'{"date_of_birth":"1958-10-16","place_of_birth":"West Covina, California, USA"}');
INSERT INTO movies."ActorDetails" ("id","actor_id","personal_info") VALUES (2,2,'{"date_of_birth":"1937-06-01","place_of_birth":"Memphis, Tennessee, USA"}');
INSERT INTO movies."ActorDetails" ("id","actor_id","personal_info") VALUES (3,3,'{"date_of_birth":"1956-07-09","place_of_birth":"Concord, California, USA"}');
INSERT INTO movies."ActorDetails" ("id","actor_id","personal_info") VALUES (4,4,'{"date_of_birth":"1977-09-15","place_of_birth":"Hammersmith, London, England, UK"}');
INSERT INTO movies."ActorDetails" ("id","actor_id","personal_info") VALUES (5,5,'{"date_of_birth":"1962-07-03","place_of_birth":"Syracuse, New York, USA"}');
INSERT INTO movies."ActorDetails" ("id","actor_id","personal_info") VALUES (6,6,'{"date_of_birth":"1940-04-25","place_of_birth":"Manhattan, New York City, New York, USA"}');

-- Inserting data for movies
INSERT INTO movies."Movie" ("movie_id", "name","release_date","length_in_min") VALUES (1, 'The Shawshank Redemption','1994-09-23',142);
INSERT INTO movies."Movie" ("movie_id", "name","release_date","length_in_min") VALUES (2, 'The Godfather','1972-03-24',175);
INSERT INTO movies."Movie" ("movie_id", "name","release_date","length_in_min") VALUES (3, 'The Dark Knight','2008-07-18',152);
INSERT INTO movies."Movie" ("movie_id", "name","release_date","length_in_min") VALUES (4, 'The Godfather: Part II','1974-12-20',202);
INSERT INTO movies."Movie" ("movie_id", "name","release_date","length_in_min") VALUES (5, 'The Lord of the Rings: The Return of the King','2003-12-17',201);
INSERT INTO movies."Movie" ("movie_id", "name","release_date","length_in_min") VALUES (6, 'Forrest Gump','1994-07-06',142);

-- Inserting data for movie actor mapping
INSERT INTO movies."MovieActorMapping" ("movie_id","actor_id") VALUES (1,1);
INSERT INTO movies."MovieActorMapping" ("movie_id","actor_id") VALUES (1,2);
INSERT INTO movies."MovieActorMapping" ("movie_id","actor_id") VALUES (2,6);
INSERT INTO movies."MovieActorMapping" ("movie_id","actor_id") VALUES (3,4);
INSERT INTO movies."MovieActorMapping" ("movie_id","actor_id") VALUES (3,5);
INSERT INTO movies."MovieActorMapping" ("movie_id","actor_id") VALUES (4,6);
INSERT INTO movies."MovieActorMapping" ("movie_id","actor_id") VALUES (5,3);

-- Inserting data for streaming provider mapping
INSERT INTO movies."StreamingProviderMapping" ("movie_id","streaming_provider") VALUES (1,'Netflix');
INSERT INTO movies."StreamingProviderMapping" ("movie_id","streaming_provider") VALUES (2,'Amazon Prime');
INSERT INTO movies."StreamingProviderMapping" ("movie_id","streaming_provider") VALUES (3,'Netflix');
INSERT INTO movies."StreamingProviderMapping" ("movie_id","streaming_provider") VALUES (4,'Amazon Prime');
INSERT INTO movies."StreamingProviderMapping" ("movie_id","streaming_provider") VALUES (5,'Netflix');
INSERT INTO movies."StreamingProviderMapping" ("movie_id","streaming_provider") VALUES (6,'Amazon Prime');

-- Inserting data for movie locations
INSERT INTO movies."MovieLocations" ("movie_id","locations") VALUES (1,ARRAY['Ohio','Maine']);
INSERT INTO movies."MovieLocations" ("movie_id","locations") VALUES (2,ARRAY['New York','Las Vegas']);
INSERT INTO movies."MovieLocations" ("movie_id","locations") VALUES (3,ARRAY['Chicago','Los Angeles']);
INSERT INTO movies."MovieLocations" ("movie_id","locations") VALUES (4,ARRAY['New York','Las Vegas']);
INSERT INTO movies."MovieLocations" ("movie_id","locations") VALUES (5,ARRAY['New Zealand','Los Angeles']);
INSERT INTO movies."MovieLocations" ("movie_id","locations") VALUES (6,ARRAY['Alabama','Georgia']);

-- Inserting data for movie properties
INSERT INTO movies."MovieProperties" ("movie_id","properties") VALUES (1,'{"genre":"Drama","rating":"9.3"}');
INSERT INTO movies."MovieProperties" ("movie_id","properties") VALUES (2,'{"genre":"Drama","rating":"9.2"}');
INSERT INTO movies."MovieProperties" ("movie_id","properties") VALUES (3,'{"genre":"Action","rating":"9.0"}');
INSERT INTO movies."MovieProperties" ("movie_id","properties") VALUES (4,'{"genre":"Drama","rating":"9.0"}');
INSERT INTO movies."MovieProperties" ("movie_id","properties") VALUES (5,'{"genre":"Adventure","rating":"8.9"}');
INSERT INTO movies."MovieProperties" ("movie_id","properties") VALUES (6,'{"genre":"Drama","rating":"8.8"}');
