package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.AlbumDto;

@AllArgsConstructor
@Service
class AlbumUpdater {

    private final AlbumRetriever albumRetriever;

    AlbumDto updateAlbumTitle(final Long albumId, final String title) {
        Album album = albumRetriever.findById(albumId);
        album.setTitle(title);
        return new AlbumDto(album.getId(), album.getTitle());
    }
}
