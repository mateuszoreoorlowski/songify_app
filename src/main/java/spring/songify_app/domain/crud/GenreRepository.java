package spring.songify_app.domain.crud;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import spring.songify_app.domain.crud.dto.GenreDto;

import java.util.Optional;
import java.util.Set;

interface GenreRepository extends Repository<Genre, Long> {

    Genre save(Genre genre);

    Set<Genre> findAll();

    boolean existsById(Long id);

    Optional<Genre> findById(Long genreId);
}
