# Stories schema

# --- !Ups

CREATE TABLE story(
    name varchar(255)
);

# --- !Downs

DROP TABLE story;