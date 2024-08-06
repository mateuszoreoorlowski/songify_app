package spring.songify_app.domain.crud;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.Set;

interface GenreRepository extends Repository<Genre, Long> {

    Genre save(Genre genre);

    Set<Genre> findAll();

    boolean existsById(Long id);

    Optional<Genre> findById(Long genreId);

    @Modifying
    @Query("delete from Genre g where g.id = :genreId")
    int deleteById(Long genreId);
}
