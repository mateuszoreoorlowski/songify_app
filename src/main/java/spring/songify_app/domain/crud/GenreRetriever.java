package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.GenreDto;
import spring.songify_app.domain.crud.dto.GenreWithSongsDto;
import spring.songify_app.domain.crud.dto.SongDto;
import spring.songify_app.domain.crud.dto.SongLanguageDto;
import spring.songify_app.domain.crud.exceptions.GenreNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class GenreRetriever {

    private final SongRepository songRepository;
    private final GenreRepository genreRepository;

    Set<GenreDto> findAllGenres() {
        return genreRepository.findAll()
                .stream()
                .map(genre -> new GenreDto(
                        genre.getId(),
                        genre.getName()))
                .collect(Collectors.toSet());
    }

    Genre findById(final Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException("Genre with id " + genreId + " not found"));
    }

    GenreWithSongsDto retrieveGenreWithSongs(Long genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException("Genre with id " + genreId + " not found"));

        List<SongDto> songs = songRepository.findByGenreName(genre.getName()).stream()
                .map(song -> new SongDto(
                        song.getId(),
                        song.getName(),
                        song.getDuration(),
                        song.getReleaseDate(),
                        SongLanguageDto.valueOf(song.getLanguage().name()
                        )))
                .collect(Collectors.toList());

        return new GenreWithSongsDto(genre.getId(), genre.getName(), songs);
    }

    public Genre findGenresBySongId(Long songId) {
        return songRepository.findById(songId)
                .map(Song::getGenre)
                .orElseThrow(() -> new GenreNotFoundException("Genre with song id " + songId + " not found"));
    }

    public GenreDto findGenreBySongId(Long id) {
        return songRepository.findById(id)
                .map(song -> new GenreDto(
                        song.getGenre().getId(),
                        song.getGenre().getName()))
                .orElseThrow(() -> new GenreNotFoundException("Genre with song id " + id + " not found"));
    }
}
