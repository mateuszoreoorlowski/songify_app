package spring.songify_app.domain.crud.dto;

import java.time.Instant;

public record SongUpdateDto(
        Long id,
        String name,
        Long duration,
        Instant releaseDate,
        SongLanguageDto language,
        Long artistId
) {
}
