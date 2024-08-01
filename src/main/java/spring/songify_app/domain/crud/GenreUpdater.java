package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.GenreDto;

@Service
@AllArgsConstructor
class GenreUpdater {

    private GenreRetriever genreRetriever;

    public GenreDto updateGenreNameById(final Long genreId, final String newName) {
        Genre genre = genreRetriever.findById(genreId);
        genre.setName(newName);
        return new GenreDto(genre.getId(), genre.getName());
    }
}
