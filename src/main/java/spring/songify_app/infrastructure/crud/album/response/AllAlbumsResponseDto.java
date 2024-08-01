package spring.songify_app.infrastructure.crud.album.response;

import spring.songify_app.domain.crud.dto.AlbumDto;

import java.util.Set;

public record AllAlbumsResponseDto(Set<AlbumDto> albums) {
}
