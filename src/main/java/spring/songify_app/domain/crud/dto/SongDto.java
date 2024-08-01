package spring.songify_app.domain.crud.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record SongDto(
        Long id,
        String name,
        Long duration,
        Instant releaseDate,
        SongLanguageDto language
) {
}
