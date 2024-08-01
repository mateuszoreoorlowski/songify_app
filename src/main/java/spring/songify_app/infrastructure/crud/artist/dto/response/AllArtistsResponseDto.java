package spring.songify_app.infrastructure.crud.artist.dto.response;

import java.util.Set;

public record AllArtistsResponseDto(Set<ArtistResponseDto> artists) {
}
