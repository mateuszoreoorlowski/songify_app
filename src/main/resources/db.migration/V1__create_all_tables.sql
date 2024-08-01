CREATE SEQUENCE IF NOT EXISTS album_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS artist_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS genre_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS song_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE album
(
    id           BIGINT NOT NULL,
    uuid         UUID,
    created_on   TIMESTAMP WITHOUT TIME ZONE,
    version      BIGINT NOT NULL,
    title        VARCHAR(255),
    release_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_album PRIMARY KEY (id)
);

CREATE TABLE artist
(
    id         BIGINT       NOT NULL,
    uuid       UUID,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    version    BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_artist PRIMARY KEY (id)
);

CREATE TABLE artist_albums
(
    albums_id  BIGINT NOT NULL,
    artists_id BIGINT NOT NULL,
    CONSTRAINT pk_artist_albums PRIMARY KEY (albums_id, artists_id)
);

CREATE TABLE genre
(
    id         BIGINT NOT NULL,
    uuid       UUID,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    version    BIGINT NOT NULL,
    name       VARCHAR(255),
    CONSTRAINT pk_genre PRIMARY KEY (id)
);

CREATE TABLE song
(
    id           BIGINT       NOT NULL,
    uuid         UUID,
    created_on   TIMESTAMP WITHOUT TIME ZONE,
    version      BIGINT       NOT NULL,
    name         VARCHAR(255) NOT NULL,
    release_date TIMESTAMP WITHOUT TIME ZONE,
    duration     BIGINT,
    genre_id     BIGINT,
    album_id     BIGINT,
    language     VARCHAR(255),
    CONSTRAINT pk_song PRIMARY KEY (id)
);

CREATE INDEX idx_song_name ON song (name);

ALTER TABLE song
    ADD CONSTRAINT FK_SONG_ON_ALBUM FOREIGN KEY (album_id) REFERENCES album (id);

ALTER TABLE song
    ADD CONSTRAINT FK_SONG_ON_GENRE FOREIGN KEY (genre_id) REFERENCES genre (id);

ALTER TABLE artist_albums
    ADD CONSTRAINT fk_artalb_on_album FOREIGN KEY (albums_id) REFERENCES album (id);

ALTER TABLE artist_albums
    ADD CONSTRAINT fk_artalb_on_artist FOREIGN KEY (artists_id) REFERENCES artist (id);