package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.exceptions.AlbumNotFoundException;
import spring.songify_app.domain.crud.exceptions.SongNotFoundException;

import java.util.Set;

@AllArgsConstructor
@Service
class SongDeleter {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;

    void deleteAllSongsById(final Set<Long> songsIds) {
        songRepository.deleteByIdIn(songsIds);
    }

    public void deleteSongFromAlbumById(Long songId, Long albumId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException(songId.toString()));

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException(albumId.toString()));

        album.getSongs().remove(song);
        albumRepository.save(album);

        songRepository.deleteById(songId);
    }
}
