package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.SongDto;
import spring.songify_app.domain.crud.dto.SongLanguageDto;
import spring.songify_app.domain.crud.exceptions.SongNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor()
class SongRetriever {

    private final SongRepository songRepository;

    Set<SongDto> findAll(Pageable pageable) {
        return songRepository.findAll(pageable)
                .stream()
                .map(song -> SongDto.builder()
                        .id(song.getId())
                        .name(song.getName())
                        .duration(song.getDuration())
                        .releaseDate(song.getReleaseDate())
                        .language(SongLanguageDto.valueOf(song.getLanguage().name()))
                        .build())
                .collect(Collectors.toSet());
    }

    Song findSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + id + " not found"));
    }

}
