package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.SongAlbumDto;
import spring.songify_app.domain.crud.dto.SongDto;
import spring.songify_app.domain.crud.dto.SongLanguageDto;

@Service
@AllArgsConstructor
class SongAssigner {

    private final SongRetriever songRetriever;
    private final AlbumRetriever albumRetriever;

    public SongAlbumDto assignSongToAlbum(Long songId, Long albumId) {
        Song song = songRetriever.findSongById(songId);
        Album album = albumRetriever.findById(albumId);
        song.setAlbum(album);

        SongDto songDto = SongDto.builder()
                .id(song.getId())
                .name(song.getName())
                .duration(song.getDuration())
                .releaseDate(song.getReleaseDate())
                .language(SongLanguageDto.valueOf(song.getLanguage().name()))
                .build();

        return SongAlbumDto.builder()
                .song(songDto)
                .albumId(album.getId())
                .build();
    }
}
