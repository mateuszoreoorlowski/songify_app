package spring.songify_app.domain.crud.dto;

import java.time.Instant;
import java.util.Set;

public record AlbumWithSongsDto(
        Long id,
        String title,
        Instant releaseDate,
        Set<Long> songsIds
) {
}
