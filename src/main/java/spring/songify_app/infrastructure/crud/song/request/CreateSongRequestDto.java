package spring.songify_app.infrastructure.crud.song.request;

import lombok.Builder;
import spring.songify_app.domain.crud.dto.SongLanguageDto;

import java.time.Instant;

@Builder
public record CreateSongRequestDto(
        String name,
        Instant releaseDate,
        Long duration,
        SongLanguageDto language
) {
}
