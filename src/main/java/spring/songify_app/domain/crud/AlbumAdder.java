package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.songify_app.domain.crud.dto.AlbumWithSongsDto;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
class AlbumAdder {

    private final AlbumRepository albumRepository;
    private final SongRetriever songRetriever;

    AlbumWithSongsDto addAlbumWithSongs(String title, Instant releaseDate , Set<Long> ids) {
        Set<Song> songs = ids.stream()
                .map(songRetriever::findSongById)
                .collect(Collectors.toSet());

        Album album = new Album();
        album.setTitle(title);
        album.setReleaseDate(releaseDate);
        album.addSongToAlbum(songs);

        Album savedAlbum = albumRepository.save(album);
        Set<Long> songsIds = savedAlbum.getSongs().stream().map(Song::getId).collect(Collectors.toSet());

        return new AlbumWithSongsDto(savedAlbum.getTitle(), savedAlbum.getReleaseDate(), songsIds);
    }
}
