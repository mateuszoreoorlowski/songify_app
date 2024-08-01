package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.SongDto;
import spring.songify_app.domain.crud.dto.SongLanguageDto;
import spring.songify_app.infrastructure.crud.song.request.CreateSongRequestDto;

@Service
@AllArgsConstructor
class SongAdder {

    private final SongRepository songRepository;
    private final GenreRepository genreRepository;
    private final GenreAssigner genreAssigner;

    SongDto addSong(final CreateSongRequestDto songDto) {
        SongLanguageDto language = songDto.language();
        SongLanguage songLanguage = SongLanguage.valueOf(language.name());
        Song song = new Song(songDto.name(), songDto.releaseDate(), songDto.duration(), songLanguage);
        Song save = songRepository.save(song);

        // Ensure default genre exists before assigning
        if (!genreRepository.existsById(1L)) {
            Genre defaultGenre = new Genre("default");
            genreRepository.save(defaultGenre);
        }

        genreAssigner.assignDefaultGenreToSong(save.getId());

        return new SongDto(save.getId(), save.getName(), save.getDuration(), save.getReleaseDate(), language);
    }
}
