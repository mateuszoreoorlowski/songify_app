package spring.songify_app.infrastructure.crud.album.response;

import java.time.Instant;
import java.util.Set;

public record AlbumWithSongsResponseDto(
        Long id,
        String title,
        Instant releaseDate,
        Set<Long> songsIds
) {
}
