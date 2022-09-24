DROP TABLE "films_likes", "users", "films", "users_relation",  "genres", "ratings", "film_genres"; 

CREATE TABLE IF NOT EXISTS "users" (
                         "id" SERIAL PRIMARY KEY,
                         "email" varchar,
                         "login" varchar,
                         "name" varchar,
                         "birthday" date,
                         PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "genres" (
                          "id" SERIAL PRIMARY KEY,
                          "name" varchar
);

CREATE TABLE IF NOT EXISTS "ratings" (
                           "id" SERIAL PRIMARY KEY,
                           "name" varchar,
                           "description" varchar
);

CREATE TABLE IF NOT EXISTS "films" (
                         "id" SERIAL PRIMARY KEY,
                         "name" varchar,
                         "description" varchar,
                         "release_date" date,
                         "duration" int,
                         "rating_id" int REFERENCES "ratings" ("id"),
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

