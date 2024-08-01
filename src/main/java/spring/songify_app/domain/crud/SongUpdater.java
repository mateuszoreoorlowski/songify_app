package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.SongLanguageDto;
import spring.songify_app.domain.crud.dto.SongUpdateDto;

@Service
@AllArgsConstructor
class SongUpdater {

    private final SongRetriever songRetriever;
    private final ArtistRetriever artistRetriever;

    SongUpdateDto updateSong(SongUpdateDto dto) {
        Song song = songRetriever.findSongById(dto.id());

        song.setName(dto.name());
        song.setDuration(dto.duration());
        song.setReleaseDate(dto.releaseDate());
        song.setLanguage(SongLanguage.valueOf(dto.language().name()));

        Artist newArtist = artistRetriever.findById(dto.artistId());

        return new SongUpdateDto(song.getId(), song.getName(), song.getDuration(), song.getReleaseDate(), dto.language(), newArtist.getId());
    }

}
