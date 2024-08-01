package spring.songify_app.domain.crud.dto;

import java.util.List;

public record ArtistWithAlbumsDto(
        Long id,
        String name,
        List<AlbumDto> albums
) {
}
