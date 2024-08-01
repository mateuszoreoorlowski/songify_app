package spring.songify_app.domain.crud;

import org.springframework.data.repository.Repository;

interface SongRepository extends Repository<Song, Long> {

    Song save(Song song);
}
