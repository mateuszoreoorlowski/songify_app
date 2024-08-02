package spring.songify_app.domain.crud;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
class ArtistDeleter {

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRetriever artistRetriever;
    private final AlbumRetriever albumRetriever;
    private final AlbumDeleter albumDeleter;
    private final SongDeleter songDeleter;

    @Transactional
    void deleteArtistByIdWithAlbumsAndSongs(final Long artistId) {
        Artist artist = artistRetriever.findById(artistId);
        Set<Album> artistAlbums = albumRetriever.findAlbumsByArtistId(artist.getId());

        if (artistAlbums.isEmpty()) {
            artistRepository.deleteById(artistId);
            return;
        }

        // Remove artist from albums where there are multiple artists
        artistAlbums.stream()
                .filter(album -> album.getArtists().size() > 1)
                .forEach(album -> {
                    album.removeArtist(artist);
                    albumRepository.save(album);
                });

        // Find albums where this artist is the only artist left
        Set<Album> albumsWithOnlyOneArtist = artistAlbums.stream()
                .filter(album -> album.getArtists().size() == 1)
                .collect(Collectors.toSet());

        Set<Long> allSongsIdsFromAllAlbumsWhereWasOnlyThisArtist = albumsWithOnlyOneArtist.stream()
                .flatMap(album -> album.getSongs().stream())
                .map(Song::getId)
                .collect(Collectors.toSet());

        songDeleter.deleteAllSongsById(allSongsIdsFromAllAlbumsWhereWasOnlyThisArtist);

        Set<Long> albumIdsToDelete = albumsWithOnlyOneArtist.stream()
                .map(Album::getId)
                .collect(Collectors.toSet());

        albumDeleter.deleteAllAlbumsByIds(albumIdsToDelete);

        artistRepository.deleteById(artistId);
    }
}
