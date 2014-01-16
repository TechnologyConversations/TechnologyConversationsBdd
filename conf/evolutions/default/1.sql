# Stories schema

# --- !Ups

CREATE SEQUENCE story_id_seq;
CREATE TABLE story(
    id INTEGER NOT NULL DEFAULT nextval('story_id_seq'),
    name varchar(255)
);

# --- !Downs

DROP TABLE story;
DROP SEQUENCE story_id_seq