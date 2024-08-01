package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
class GenreAssigner {

    private final SongRetriever songRetriever;
    private final GenreRetriever genreRetriever;

    void assignDefaultGenreToSong(Long songId) {
        Song song = songRetriever.findSongById(songId);
        Genre genre = genreRetriever.findById(1L);
        song.setGenre(genre);
    }

    void assignGenreToSong(Long genreId, Long songId) {
        Song song = songRetriever.findSongById(songId);
        Genre genre = genreRetriever.findById(genreId);
        song.setGenre(genre);
    }
}
