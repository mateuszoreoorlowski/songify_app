package spring.songify_app.domain.crud.dto;

import lombok.Builder;

@Builder
public record SongAlbumDto(
    SongDto song,
    Long albumId
) {
}
