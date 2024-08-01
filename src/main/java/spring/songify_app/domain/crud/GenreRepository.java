package spring.songify_app.domain.crud;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import spring.songify_app.domain.crud.dto.GenreDto;

import java.util.Set;

interface GenreRepository extends Repository<Genre, Long> {

    Genre save(Genre genre);

    Set<Genre> findAll(Pageable pageable);

    boolean existsById(Long id);
}
