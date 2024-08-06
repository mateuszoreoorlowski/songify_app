package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.exceptions.GenreWasNotDeletedException;

@Service
@AllArgsConstructor
class GenreDeleter {

    private final GenreRepository genreRepository;
    private final SongRepository songRepository;

    public void deleteGenre(Long genreId) {
        if (songRepository.existsByGenreId(genreId)) {
            throw new GenreWasNotDeletedException("genre not deleted, there are songs related to it");
        }
        genreRepository.deleteById(genreId);
    }
}
