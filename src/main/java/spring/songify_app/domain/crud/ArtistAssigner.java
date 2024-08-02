package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.ArtistAlbumDto;

@AllArgsConstructor
@Service
class ArtistAssigner {

    private final ArtistRetriever artistRetriever;
    private final AlbumRetriever albumRetriever;

    ArtistAlbumDto assignArtistToAlbum(final Long artistId, final Long albumId) {
        Artist artist = artistRetriever.findById(artistId);
        Album album = albumRetriever.findById(albumId);
        artist.addAlbum(album);

        return new ArtistAlbumDto(artist.getId(), album.getId());
    }

    void addArtistToAlbum(final Long artistId, final Long albumId) {
        Artist artist = artistRetriever.findById(artistId);
        Album album = albumRetriever.findById(albumId);
        artist.addAlbum(album);
    }
}
