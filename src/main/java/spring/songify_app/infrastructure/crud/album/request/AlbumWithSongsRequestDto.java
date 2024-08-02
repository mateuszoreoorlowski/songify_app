package spring.songify_app.infrastructure.crud.album.request;

import lombok.Builder;

import java.time.Instant;
import java.util.Set;

@Builder
public record AlbumWithSongsRequestDto(
        String title,
        Instant releaseDate,
        Set<Long> songsIds
) {
}
