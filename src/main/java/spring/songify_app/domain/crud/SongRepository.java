package spring.songify_app.domain.crud;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.*;

interface SongRepository extends Repository<Song, Long> {

    Song save(Song song);

    Set<Song> findAll();

    @Query("SELECT s FROM Song s WHERE s.id =:id")
    Optional<Song> findById(Long id);

    List<Song> findByGenreName(String genreName);

    @Modifying
    @Query("delete from Song s where s.id in :ids")
    int deleteByIdIn(Collection<Long> ids);
}
