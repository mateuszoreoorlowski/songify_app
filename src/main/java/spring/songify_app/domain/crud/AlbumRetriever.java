package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.songify_app.domain.crud.dto.AlbumDto;
import spring.songify_app.domain.crud.dto.AlbumInfo;
import spring.songify_app.domain.crud.exceptions.AlbumNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
class AlbumRetriever {

    private final AlbumRepository albumRepository;

    Set<AlbumDto> findAllAlbums() {
        return albumRepository.findAll().stream()
                .map(album ->
                        new AlbumDto(album.getId(),
                        album.getTitle()))
                .collect(Collectors.toSet());
    }

    public Album findById(Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id: " + albumId + " not found"));
    }

    AlbumInfo findAlbumByIdWithArtistsAndSongs(final Long id) {
        return albumRepository.findAlbumByIdWithSongsAndArtists(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id: " + id + " not found"));
    }

    Set<Album> findAlbumsByArtistId(final Long artistId) {
        return albumRepository.findAllAlbumsByArtistId(artistId);
    }

    Set<AlbumDto> findAlbumsDtoByArtistId(final Long artistId) {
        return findAlbumsByArtistId(artistId).stream()
                .map(album -> new AlbumDto(album.getId(), album.getTitle()))
                .collect(Collectors.toSet());
    }

    long countArtistsByAlbumId(final Long id) {
        return findById(id)
                .getArtists()
                .size();
    }

    AlbumDto findAlbumDtoById(final Long albumId) {
        Album album = findById(albumId);
        return new AlbumDto(
                album.getId(),
                album.getTitle()
        );
    }

    Set<Song> findSongsByAlbumId(final Long albumId) {
        return albumRepository.findSongsByAlbumId(albumId);
    }

    public Set<Artist> findArtistsByAlbumId(final Long albumId) {
        return albumRepository.findArtistsByAlbumId(albumId);
    }
}
