package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.SongDto;
import spring.songify_app.domain.crud.dto.SongLanguageDto;
import spring.songify_app.domain.crud.exceptions.SongNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor()
class SongRetriever {

    private final SongRepository songRepository;

    Set<SongDto> findAll() {
        return songRepository.findAll()
                .stream()
                .map(song -> SongDto.builder()
                        .id(song.getId())
                        .name(song.getName())
                        .duration(song.getDuration())
                        .releaseDate(song.getReleaseDate())
                        .language(Optional.ofNullable(song.getLanguage())
                                .map(language -> SongLanguageDto.valueOf(language.name()))
                                .orElse(null))
                        .build())
                .collect(Collectors.toSet());
    }

    Song findSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + id + " not found"));
    }

    SongDto findSongDtoById(Long id) {
        return songRepository.findById(id)
                .map(song -> SongDto.builder()
                        .id(song.getId())
                        .name(song.getName())
                        .build())
                .orElseThrow(() -> new SongNotFoundException("Song with id: " + id + " not found"));
    }

    String getGenreNameForSong(Long songId) {
        Song song = findSongById(songId);
        Genre genre = song.getGenre();

        return genre != null ? genre.getName() : "default";
    }

    public List<SongDto> findSongsByGenreId(Long genreId) {
        return songRepository.findByGenreId(genreId)
                .stream()
                .map(song -> SongDto.builder()
                        .id(song.getId())
                        .name(song.getName())
                        .build())
                .toList();
    }
}
