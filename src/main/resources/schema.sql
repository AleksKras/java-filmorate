DROP TABLE IF EXISTS "films_likes", "users", "films", "users_relation",  "genres", "ratings", "film_genres"; 

CREATE TABLE IF NOT EXISTS "users" (
                         "id" SERIAL PRIMARY KEY,
                         "email" varchar(50) NOT NULL,
                         "login" varchar NOT NULL,
                         "name" varchar,
                         "birthday" date,
                         PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "genres" (
                          "id" SERIAL PRIMARY KEY,
                          "name" varchar(50)
);

CREATE TABLE IF NOT EXISTS "ratings" (
                           "id" SERIAL PRIMARY KEY,
                           "name" varchar (50),
                           "description" varchar(200)
);

CREATE TABLE IF NOT EXISTS "films" (
                         "id" SERIAL PRIMARY KEY,
                         "name" varchar(50) NOT NULL,
                         "description" varchar(200),
                         "release_date" date NOT NULL,
                         "duration" int,
                         "rating_id" int REFERENCES "ratings" ("id") NOT NULL,
                         PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "film_genres" (
                                  "film_id" int REFERENCES "films" ("id") ON DELETE CASCADE,
                                  "genre_id" int REFERENCES "genres" ("id") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "users_relation" (
                                  "user_id" int REFERENCES "users" ("id") ON DELETE CASCADE,
                                  "friend_id" int REFERENCES "users" ("id") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "films_likes" (
                               "film_id" int REFERENCES "films" ("id") ON DELETE CASCADE,
                               "user_id" int REFERENCES "users" ("id") ON DELETE CASCADE
);

