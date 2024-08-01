package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.GenreDto;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class GenreRetriever {

    private final GenreRepository genreRepository;

    Set<GenreDto> findAllGenres() {
        return genreRepository.findAll()
                .stream()
                .map(genre -> new GenreDto(
                        genre.getId(),
                        genre.getName()))
                .collect(Collectors.toSet());
    }
}
