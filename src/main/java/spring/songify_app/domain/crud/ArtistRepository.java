package spring.songify_app.domain.crud;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.Set;

interface ArtistRepository extends Repository<Artist, Long> {

    Artist save(Artist artist);

    Set<Artist> findAll();

    Optional<Artist> findById(Long id);

    @Modifying
    @Query("delete from Artist a where a.id = :id")
    int deleteById(Long id);
}
