package spring.songify_app.domain.crud;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Set;

interface SongRepository extends Repository<Song, Long> {

    Song save(Song song);

    Set<Song> findAll(Pageable pageable);
}
